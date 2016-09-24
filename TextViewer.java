package com.example.root.mytxtreaderone.platform;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.root.mytxtreaderone.R;
import com.example.root.mytxtreaderone.dict.Chinese;
import com.example.root.mytxtreaderone.dict.Dictionary;
import com.example.root.mytxtreaderone.dict.English;
import com.example.root.mytxtreaderone.gadgets.AutoReadSetter;
import com.example.root.mytxtreaderone.gadgets.BackgroundSetter;
import com.example.root.mytxtreaderone.gadgets.BookMarker;
import com.example.root.mytxtreaderone.gadgets.ConfigureSetter;
import com.example.root.mytxtreaderone.gadgets.DictSearcher;
import com.example.root.mytxtreaderone.gadgets.PopupManager;
import com.example.root.mytxtreaderone.processors.FileProcessor;
import com.example.root.mytxtreaderone.utils.Constants;

import java.io.File;
import java.io.IOException;

public class TextViewer extends Activity implements View.OnTouchListener{
    TextView textView;
    FileProcessor fileProcessor;
    Point size;
    String fileName;
    PopupManager text_menu, background_popup, dict_popup;
    PopupManager config_popup, auto_popup;
    Dictionary dictionary;
    BookMarker bookMarker;
    @Override
    public void onCreate(Bundle instance){
        super.onCreate(instance);
        setContentView(R.layout.text_view);
        switch (DictSearcher.type){
            case "zh":dictionary = new Chinese();break;
            case "en": dictionary = new English(); break;
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

        bookMarker = new BookMarker(fileName);
        int skipLength = bookMarker.decord(fileProcessor);
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

        auto_popup = new PopupManager(getLayoutInflater().inflate(R.layout.auto_read, null), this);
        auto_popup.setAnimation(R.style.mypopwindow_anim_style);

        autoReadSetter = new AutoReadSetter();
    }

    public void confirm(View view){
        final RelativeLayout layout = (RelativeLayout)findViewById(R.id.text_viewer_layout);
        switch (view.getId()){
            // menu for sub-popup
            case R.id.menu_auto_start:
                auto_popup.showPopup(layout.findViewById(R.id.placeHolder),
                        size.x / 24, size.y / 6);
                text_menu.dismiss();
                break;
            case R.id.menu_background:
                background_popup.showPopup(layout.findViewById(R.id.placeHolder),
                        view.getWidth() + view.getPaddingLeft(),
                        view.getHeight() + view.getPaddingTop());
                text_menu.dismiss();
                break;
            case R.id.menu_config:
                config_popup.showPopup(layout.findViewById(R.id.placeHolder),
                        size.x / 24, size.y / 6);
                text_menu.dismiss();
                break;
            case R.id.menu_dict:
                dict_popup.showPopup(layout.findViewById(R.id.placeHolder),
                        size.x / 24, size.y / 6);
                text_menu.dismiss();
                break;
            // background pop up
            case R.id.background_preview:
                BackgroundSetter.Set(textView, background_popup);
                break;
            case R.id.background_confirmer:
                background_popup.dismiss();
                break;
            // configure pop up
            case R.id.config_confirm:
                ConfigureSetter.confirmConfig(config_popup, fileName);
                break;
            // dict pop up
            case R.id.dict_search:
                DictSearcher.search(dict_popup, dictionary);
                break;
            case R.id.en_dict_toggler:
                DictSearcher.type = "en";
                dictionary = new English();
                dictionary.openDict(this);
                DictSearcher.toggleDicts(dict_popup);
                break;
            case R.id.ch_dict_toggler:
                DictSearcher.type = "zh";
                dictionary = new Chinese();
                dictionary.openDict(this);
                DictSearcher.toggleDicts(dict_popup);
                break;
            case R.id.dict_exit:
                dict_popup.dismiss();
                break;
            // autoread popup
            case R.id.autoread_confirm:
                autoReadSetter.setDelay(auto_popup);
                AutoReadingTask art = new AutoReadingTask();
                art.execute();
                auto_popup.dismiss();
                break;
            case R.id.cancel_autoread:
                autoReadSetter.autoReadingFlag = false;
                auto_popup.dismiss();
                break;
        }
    }
    AutoReadSetter autoReadSetter;
    private class AutoReadingTask extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {}
        @Override
        protected String doInBackground(String... params) {
            while(autoReadSetter.autoReadingFlag){
                try{
                    for(int i=0; i<autoReadSetter.delayTime; i++){
                        Thread.sleep(1000);
                        if(!autoReadSetter.autoReadingFlag)break;
                    }
                    publishProgress(0);
                }catch (InterruptedException ie){}
            }
            return "";
        }
        @Override
        protected void onProgressUpdate(Integer... progresses) {
            readForward();
        }
        @Override
        protected void onPostExecute(String result) {}
        @Override
        protected void onCancelled() {}
    }
    boolean ifFirstRead = true;
    String txtBuffer = "";
    long time;

    private void readForward(){
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
    }
    private void readBackward(){
        try{
            if(fileProcessor.pageNo<=1){}else{
                String text = fileProcessor.readBackward();
                int end = txtBuffer.length() - text.length();
                txtBuffer = text + txtBuffer.substring(0, end);
                textView.setText(txtBuffer);
            }

        }catch (IOException ie){

        }
    }
    // move for page up/down, long click to show menu
    float touchDownPos = 0, touchUpPos = 0;
    public boolean onTouch(View view, MotionEvent motionEvent){
        switch (motionEvent.getAction()&MotionEvent.ACTION_MASK){
            case MotionEvent.ACTION_DOWN:
                time = System.currentTimeMillis();
                touchDownPos = motionEvent.getX();
                return true;
            case MotionEvent.ACTION_MOVE:
                    touchUpPos = motionEvent.getX();
                    if(touchDownPos - touchUpPos < 50 && touchDownPos - touchUpPos > -50){
                        if(System.currentTimeMillis()-time>500){
                            text_menu.showPopup(findViewById(R.id.placeHolder), size.x/24, size.x/24);
                        }
                    }
                return true;

            case MotionEvent.ACTION_UP:
                if(touchDownPos - touchUpPos > 50) readForward();
                else if(touchDownPos - touchUpPos < -50) readBackward();
                return true;
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
    //int skipLength = 0;
    public void onResume(){
        super.onResume();

    }
    public void onPause(){
        super.onPause();
        bookMarker.record(fileProcessor);
        System.exit(0);
    }
}
