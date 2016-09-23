package com.example.root.mytxtreaderone.gadgets;

import android.widget.EditText;

import com.example.root.mytxtreaderone.R;

/**
 * Created by root on 16-9-24.
 */
public class AutoReadSetter {
    public static long delayTime = 0;
    public static boolean autoReadingFlag = false;
    public static void setDelay(PopupManager popupManager){
        EditText dt = (EditText)popupManager.getView(R.id.autoread_factor);
        delayTime = Long.valueOf(dt.getText().toString());
        autoReadingFlag = true;
    }
}
