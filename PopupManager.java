package com.example.root.mytxtreaderone.gadgets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

public class PopupManager {
    private PopupWindow popupWindow;
    private View layout;
    public PopupManager(View layout, Context context){
        this.layout = layout;
        popupWindow = new PopupWindow(layout, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable(context.getResources(), (Bitmap) null));
    }
    public void setAnimation(int styleId){
        popupWindow.setAnimationStyle(styleId);
    }
    public void showPopup(View relative, int x, int y){
        popupWindow.showAsDropDown(relative, x, y);
    }
    public View getView(int resId){
        return layout.findViewById(resId);
    }
    public void setOnDismissListener(PopupWindow.OnDismissListener listener){
        popupWindow.setOnDismissListener(listener);
    }

}
