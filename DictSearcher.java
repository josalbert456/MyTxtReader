package com.example.root.mytxtreaderone.gadgets;

import android.graphics.Color;
import android.text.Html;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.root.mytxtreaderone.R;
import com.example.root.mytxtreaderone.dict.Chinese;
import com.example.root.mytxtreaderone.dict.Dictionary;
import com.example.root.mytxtreaderone.dict.English;

/**
 * Created by root on 16-9-23.
 */
public class DictSearcher {
    public static String type = "zh";
    public static void search(PopupManager popupManager, Dictionary dicts){
        EditText entry_view = (EditText)popupManager.getView(R.id.dict_entry);
        String entry = entry_view.getText().toString();
        TextView search_result = (TextView)popupManager.getView(R.id.search_result);

        switch (type){
            case "zh":
                Chinese chinese = (Chinese)dicts;
                search_result.setText(chinese.searchEntry(entry));
                break;
            case "en":
                English english = (English)dicts;
                search_result.setText(Html.fromHtml(english.searchEntry(entry)));
                break;
        }

    }
    public static void toggleDicts(PopupManager dict_popup){
        Button bt;
        switch (DictSearcher.type){
            case "zh":
                bt = (Button)(dict_popup.getView(R.id.ch_dict_toggler));
                bt.setBackgroundColor(Color.rgb(200, 200, 30));
                bt = (Button)(dict_popup.getView(R.id.en_dict_toggler));
                bt.setBackgroundColor(Color.rgb(222, 222, 222));
                break;
            case "en":
                bt = (Button)(dict_popup.getView(R.id.en_dict_toggler));
                bt.setBackgroundColor(Color.rgb(200, 200, 30));
                bt = (Button)(dict_popup.getView(R.id.ch_dict_toggler));
                bt.setBackgroundColor(Color.rgb(222, 222, 222));
                break;
        }
    }
}
