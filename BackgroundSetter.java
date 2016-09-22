package com.example.root.mytxtreaderone.gadgets;

import android.graphics.Color;
import android.widget.EditText;
import android.widget.TextView;

import com.example.root.mytxtreaderone.R;

/**
 * Created by root on 16-9-22.
 */
public class BackgroundSetter {
    public static int background[] = new int[]{
            R.drawable.one, R.drawable.two, R.drawable.three,
            R.drawable.four, R.drawable.five, R.drawable.six,
            R.drawable.seven, R.drawable.eight, R.drawable.nine
    };
    public static int colors[] = new int[]{
            Color.rgb(0, 0, 0), Color.rgb(255, 255, 255), Color.rgb(122, 122, 122),
            Color.rgb(200, 0, 0), Color.rgb(0, 200, 0), Color.rgb(0, 0, 200),
            Color.rgb(120, 120, 0), Color.rgb(120, 0, 120), Color.rgb(0, 120, 120)
    };
    public static void Set(TextView textView, PopupManager background_popup){
        EditText backgroundImage = (EditText)background_popup.getView(R.id.backgroundType);
        String x = backgroundImage.getText().toString();
        int type = Integer.valueOf(x) - 1;
        if(type>=0 && type <=8){}
        else type = 0;

        textView.setBackgroundResource(background[type]);

        EditText textColor = (EditText)background_popup.getView(R.id.textColor);
        x = textColor.getText().toString();
        type = Integer.valueOf(x) - 1;
        if(type>=0 && type<=8){}
        else type = 0;
        textView.setTextColor(colors[type]);
    }
}
