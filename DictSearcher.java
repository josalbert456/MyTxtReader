package com.example.root.mytxtreaderone.gadgets;

import android.widget.EditText;
import android.widget.TextView;

import com.example.root.mytxtreaderone.R;
import com.example.root.mytxtreaderone.dict.Chinese;

/**
 * Created by root on 16-9-23.
 */
public class DictSearcher {
    public static void search(PopupManager popupManager){
        EditText entry_view = (EditText)popupManager.getView(R.id.dict_entry);
        String entry = entry_view.getText().toString();
        TextView search_result = (TextView)popupManager.getView(R.id.search_result);
        search_result.setText(Chinese.searchEntry(entry));
    }
}
