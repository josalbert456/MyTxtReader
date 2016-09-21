package com.example.root.mytxtreader.reader_platform;

import android.app.Activity;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.root.mytxtreader.R;
import com.example.root.mytxtreader.file_processor.FileProcessor;
import java.io.IOException;

public class TextViewer extends Activity implements View.OnTouchListener{
    TextView textView;
    FileProcessor fileProcessor;
    @Override
    public void onCreate(Bundle instance){
        super.onCreate(instance);
        setContentView(R.layout.text_view);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        textView = (TextView)findViewById(R.id.text_viewer);
        textView.setOnTouchListener(this);
        String path = Environment.getExternalStorageDirectory() + "/test.txt";
        try{
            fileProcessor = new FileProcessor(path);
        }catch (IOException ie){

        }
        try{
            txtBuffer = fileProcessor.read(1512);
            textView.setText(txtBuffer);
        }catch (IOException ie){
            Toast.makeText(this, ie.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    boolean ifFirstRead = true;
    String txtBuffer = "";

    public boolean onTouch(View view, MotionEvent motionEvent){
        switch (motionEvent.getAction()&MotionEvent.ACTION_MASK){
            case MotionEvent.ACTION_DOWN:

                calcDisplayInfo();

                try{
                    String text = fileProcessor.read(DisplayInfo.DISPLAY_TEXT_END);

                    txtBuffer = txtBuffer.substring(DisplayInfo.DISPLAY_TEXT_END);
                    txtBuffer += text;
                    textView.setText(txtBuffer);
                }catch (IOException ie){
                    Toast.makeText(this, ie.getMessage(), Toast.LENGTH_SHORT).show();
                }
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

    }
}
