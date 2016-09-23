package com.example.root.mytxtreaderone.dict;

import android.content.Context;

/**
 * Created by root on 16-9-23.
 */
public interface Dictionary {
    void openDict(Context context);
    void loadDictMap(Context context);
    String searchEntry(String entry);
}
