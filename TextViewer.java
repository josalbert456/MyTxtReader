package com.example.root.mytxtreaderone.platform;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.text.Layout;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.root.mytxtreaderone.R;
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
    //PopupWindow layout_pop;
    PopupManager popupManager;
    @Override
    public void onCreate(Bundle instance){
        super.onCreate(instance);
        setContentView(R.layout.text_view);
        if (Intent.ACTION_VIEW.equals(getIntent().getAction()))
        {
            Constants.file = new File(getIntent().getData().getPath());
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

        File file = new File(Environment.getExternalStorageDirectory() + "/file.txt");
        try{
            if(!file.exists()){}else{
                InputStream is = new FileInputStream(file);
                byte[] buffer = new byte[(int)file.length()];
                is.read(buffer);
                String markData = new String(buffer);
                while(true){

                    int pos = markData.indexOf('\n');
                    if(pos==-1)break;
                    String record = markData.substring(0, pos);
                    fileProcessor.prevReadLengths[fileProcessor.pageNo] = Integer.valueOf(record);

                    markData = markData.substring(pos + 1);
                    fileProcessor.pageNo++;

                }
                skipLength = Integer.valueOf(markData);

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
        popupManager = new PopupManager(getLayoutInflater().inflate(R.layout.background, null), this);
        popupManager.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                TextView tv = (TextView)popupManager.getView(R.id.background);
                Toast.makeText(getBaseContext(), tv.getText(), Toast.LENGTH_SHORT).show();
            }
        });
        popupManager.setAnimation(R.style.mypopwindow_anim_style);
        /*popupView = getLayoutInflater().inflate(R.layout.background, null);

        layout_pop = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, true);
        layout_pop.setTouchable(true);
        layout_pop.setOutsideTouchable(true);
        layout_pop.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));*/
    }
    //View popupView;
    boolean ifFirstRead = true;
    String txtBuffer = "";
    long time;
    public boolean onTouch(View view, MotionEvent motionEvent){
        switch (motionEvent.getAction()&MotionEvent.ACTION_MASK){
            case MotionEvent.ACTION_DOWN:
                time = System.currentTimeMillis();
                float pos = motionEvent.getX();
                if(pos>size.x/2){
                    calcDisplayInfo();
                    try{
                        // guarantee that the txtBuffer always has 2048 chars

                        String text = fileProcessor.read(DisplayInfo.DISPLAY_TEXT_END);
                        txtBuffer = txtBuffer.substring(DisplayInfo.DISPLAY_TEXT_END);
                        txtBuffer += text;
                        textView.setText(txtBuffer);
                    }catch (IOException ie){
                        Toast.makeText(this, ie.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }else{
                    try{

                        String text = fileProcessor.readBackward();
                        int end = txtBuffer.length() - text.length();
                        txtBuffer = text + txtBuffer.substring(0, end);
                        textView.setText(txtBuffer);
                        return true;
                    }catch (IOException ie){

                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                    if(System.currentTimeMillis()-time>300){
                        /*layout_pop.showAsDropDown(findViewById(R.id.placeHolder), 10, 10);
                        Toast.makeText(this, ((TextView)popupView.findViewById(R.id.background)).getText(),
                                Toast.LENGTH_SHORT).show();*/
                        popupManager.showPopup(findViewById(R.id.placeHolder), size.x/4, 10);
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
        File file = new File(Environment.getExternalStorageDirectory() + "/file.txt");

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
