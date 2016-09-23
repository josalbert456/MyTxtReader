package com.example.root.mytxtreaderone.platform;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Environment;
import android.text.method.ScrollingMovementMethod;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.root.mytxtreaderone.R;
import com.example.root.mytxtreaderone.dict.Chinese;
import com.example.root.mytxtreaderone.dict.Dictionary;
import com.example.root.mytxtreaderone.dict.English;
import com.example.root.mytxtreaderone.gadgets.BackgroundSetter;
import com.example.root.mytxtreaderone.gadgets.ConfigureSetter;
import com.example.root.mytxtreaderone.gadgets.DictSearcher;
import com.example.root.mytxtreaderone.gadgets.PopupManager;
import com.example.root.mytxtreaderone.processors.FileProcessor;
import com.example.root.mytxtreaderone.utils.Constants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class TextViewer extends Activity implements View.OnTouchListener{
    TextView textView;
    FileProcessor fileProcessor;
    Point size;
    String fileName;
    PopupManager text_menu, background_popup, dict_popup;
    PopupManager config_popup;
    Dictionary dictionary;
    @Override
    public void onCreate(Bundle instance){
        super.onCreate(instance);
        setContentView(R.layout.text_view);
        switch (DictSearcher.type){
            case "zh":dictionary = new Chinese();break;
        }
        dictionary.openDict(this);
        if (Intent.ACTION_VIEW.equals(getIntent().getAction()))
        {
            Constants.file = new File(getIntent().getData().getPath());
            fileName = Constants.file.getName();
        }

        Display display = getWindowManager().getDefaultDisplay();
        size = new Point();
        display.getSize(size);
        textView = (TextView)findViewById(R.id.text_viewer);
        textView.setOnTouchListener(this);
        try{
            fileProcessor = new FileProcessor("",this);
        }catch (IOException ie){

        }

        File file = new File(Environment.getExternalStorageDirectory() + "/mytxt/" + fileName);
        File dir = new File(Environment.getExternalStorageDirectory() + "/mytxt");
        if(!dir.exists())dir.mkdir();
        try{
            if(!file.exists()){}else{
                InputStream is = new FileInputStream(file);
                byte[] buffer = new byte[(int)file.length()];
                is.read(buffer);
                String markData = new String(buffer);
                skipLength = 0;
                while(true){
                    int pos = markData.indexOf('\n');
                    if(pos==-1)break;
                    String record = markData.substring(0, pos);
                    fileProcessor.prevReadLengths[fileProcessor.pageNo] = Integer.valueOf(record);
                    markData = markData.substring(pos + 1);
                    fileProcessor.pageNo++;
                    skipLength += Integer.valueOf(record);
                }
                file.delete();
            }
        }catch (IOException ie){

        }
        try{
            txtBuffer = fileProcessor.readForward(FileProcessor.MAX_READ_COUNTS, skipLength);
            textView.setText(txtBuffer);
        }catch (IOException ie){
            Toast.makeText(this, ie.getMessage(), Toast.LENGTH_SHORT).show();
        }
        text_menu = new PopupManager(getLayoutInflater().inflate(R.layout.text_menu, null), this);
        text_menu.setAnimation(R.style.mypopwindow_anim_style);

        background_popup = new PopupManager(getLayoutInflater().inflate(R.layout.background, null), this);
        background_popup.setAnimation(R.style.mypopwindow_anim_style);

        dict_popup = new PopupManager(getLayoutInflater().inflate(R.layout.dict_layout, null), this);
        dict_popup.setAnimation(R.style.mypopwindow_anim_style);
        TextView search_result = (TextView)dict_popup.getView(R.id.search_result);
        search_result.setMovementMethod(new ScrollingMovementMethod());

        config_popup = new PopupManager(getLayoutInflater().inflate(R.layout.configuration, null),this);
        config_popup.setAnimation(R.style.mypopwindow_anim_style);
    }

    public void setBackground(View view){
        BackgroundSetter.Set(textView, background_popup);
    }
    public void confirm(View view){
        //Button bt;
        final RelativeLayout layout = (RelativeLayout)findViewById(R.id.text_viewer_layout);
        switch (view.getId()){
            case R.id.background_confirmer:
                background_popup.dismiss();
                break;
            case R.id.menu_background:
                background_popup.showPopup(layout.findViewById(R.id.placeHolder),
                        view.getWidth() + view.getPaddingLeft(),
                        view.getHeight() + view.getPaddingTop());
                text_menu.dismiss();
                break;

            case R.id.menu_dict:
                dict_popup.showPopup(layout.findViewById(R.id.placeHolder),
                        size.x / 24, size.y / 6);
                text_menu.dismiss();
                break;
            case R.id.dict_search:
                DictSearcher.search(dict_popup, dictionary);
                break;
            case R.id.menu_config:
                config_popup.showPopup(layout.findViewById(R.id.placeHolder),
                        size.x / 24, size.y / 6);
                text_menu.dismiss();
                break;
            case R.id.config_confirm:
                ConfigureSetter.confirmConfig(config_popup, fileName);
                break;
            case R.id.en_dict_toggler:
                DictSearcher.type = "en";
                dictionary = new English();
                DictSearcher.toggleDicts(dict_popup, dictionary);
                break;
            case R.id.ch_dict_toggler:
                DictSearcher.type = "zh";
                dictionary = new Chinese();
                DictSearcher.toggleDicts(dict_popup, dictionary);
                /*dictionary = new Chinese();
                bt = (Button)(dict_popup.getView(R.id.ch_dict_toggler));
                bt.setBackgroundColor(Color.rgb(200, 200, 30));
                bt = (Button)(dict_popup.getView(R.id.en_dict_toggler));
                bt.setBackgroundColor(Color.rgb(222, 222, 222));*/
                break;
        }
    }
    boolean ifFirstRead = true;
    String txtBuffer = "";
    long time;

    // Better action: move for page up/down, long click to show menu
    public boolean onTouch(View view, MotionEvent motionEvent){
        switch (motionEvent.getAction()&MotionEvent.ACTION_MASK){
            case MotionEvent.ACTION_DOWN:
                time = System.currentTimeMillis();
                float pos = motionEvent.getX();
                if(pos>size.x/2){
                    calcDisplayInfo();
                    try{
                        // guarantee that the txtBuffer always has 2048 chars
                        // if we read back just now, we only need to read 2048 forward
                        if(fileProcessor.backFlag){
                            txtBuffer = fileProcessor.read(FileProcessor.MAX_READ_COUNTS);
                            textView.setText(txtBuffer);
                            fileProcessor.backFlag = false;
                        }else{
                            // if not, we read text and stuff the textbuffer
                            String text = fileProcessor.read(DisplayInfo.DISPLAY_TEXT_END);
                            txtBuffer = txtBuffer.substring(DisplayInfo.DISPLAY_TEXT_END);
                            txtBuffer += text;
                            textView.setText(txtBuffer);
                        }

                    }catch (IOException ie){
                        Toast.makeText(this, ie.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }else{
                    try{
                        if(fileProcessor.pageNo<=1){}else{
                            String text = fileProcessor.readBackward();
                            int end = txtBuffer.length() - text.length();
                            txtBuffer = text + txtBuffer.substring(0, end);
                            textView.setText(txtBuffer);
                        }

                        return true;
                    }catch (IOException ie){

                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                    if(System.currentTimeMillis()-time>1000){

                        text_menu.showPopup(findViewById(R.id.placeHolder), size.x/24, size.x/24);
                       return true;
                    }
                break;
        }
        return false;
    }
    private void calcDisplayInfo(){
        //int lineCount = textView.getLineCount();
        //int entireTextViewHeight = lineCount * lineHeight;

        int lineHeight = textView.getLineHeight();
        int displayTextViewHeight = textView.getHeight() -
                textView.getPaddingTop() - textView.getPaddingBottom();
        if(ifFirstRead){
            DisplayInfo.BOTTOM_LINE_NUMBER = (displayTextViewHeight /*+ currentScrollTop*/)/lineHeight;
            ifFirstRead = false;
        }
        DisplayInfo.DISPLAY_TEXT_END = textView.getLayout().getLineEnd(DisplayInfo.BOTTOM_LINE_NUMBER-1);

        fileProcessor.prevReadLengths[fileProcessor.pageNo] = DisplayInfo.DISPLAY_TEXT_END;
        fileProcessor.pageNo++;
    }
    int skipLength = 0;
    public void onResume(){
        super.onResume();

    }
    public void onPause(){
        super.onPause();
            File file = new File(Environment.getExternalStorageDirectory() + "/mytxt/" + fileName);
            try{
                if(!file.exists()){
                    file.createNewFile();
                }

                OutputStream os = new FileOutputStream(file, true);
                for(int i=1; i<fileProcessor.pageNo; i++){
                    String pos = String.valueOf(fileProcessor.prevReadLengths[i]) + "\n";
                    os.write(pos.getBytes());
                }
                String length = String.valueOf(fileProcessor.readLength);
                os.write(length.getBytes());
            }catch (IOException ie){

            }
        System.exit(0);
    }
}
