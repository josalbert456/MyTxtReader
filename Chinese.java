package com.example.root.mytxtreaderone.dict;

import android.content.Context;
import android.content.res.AssetManager;

import com.example.root.mytxtreaderone.gadgets.DictSearcher;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * Created by root on 16-9-23.
 */
public class Chinese implements Dictionary{
    public static HashMap<String, Integer> dict_map;
    static String dictionary_string;
    @Override
    public void openDict(final Context context){
        (new Thread(new Runnable() {
            @Override
            public void run() {
                loadDictMap(context);
            }
        })).start();
    }
    @Override
    public void loadDictMap(Context context){
        AssetManager assets = context.getAssets();
        dict_map = new HashMap<>();

        try{
            InputStream dict_io = assets.open("dicts/ch/zh_dict.txt");
            byte[] dict_byte = new byte[dict_io.available()];
            dict_io.read(dict_byte);
            dictionary_string = new String(dict_byte);
            dict_io.close();

            InputStream is = assets.open("dicts/ch/zh_map.txt");
            byte[] buf = new byte[is.available()];
            is.read(buf);
            is.close();
            String map_str = new String(buf);
            int start = 0, end;
            for(int i=0; i<3888; i++){
                end = map_str.indexOf('\n');
                String cur_entry_str = map_str.substring(start, end);
                int mid = cur_entry_str.indexOf(' ');
                String cur_entry_line = cur_entry_str.substring(0, mid);
                String cur_entry = cur_entry_str.substring(mid+1);

                int entry_line_int = Integer.valueOf(cur_entry_line);
                dict_map.put(cur_entry, entry_line_int);
                map_str = map_str.substring(end+1);

            }
            String cur_entry_str = map_str;
            int mid = cur_entry_str.indexOf(' ');
            String cur_entry_line = cur_entry_str.substring(0, mid);
            String cur_entry = cur_entry_str.substring(mid+1);

            int entry_line_int = Integer.valueOf(cur_entry_line);
            dict_map.put(cur_entry, entry_line_int);
            dict_map.put("你", 20540);
            dict_map.put("饭", 20545);
        }catch (IOException ie){

        }
    }
    @Override
    public String searchEntry(String entry){
        if(!dict_map.containsKey(entry)){
            return "No Entry!";
        }
        int line_number = dict_map.get(entry);
        String tmp = new String (dictionary_string);
        int line_counter = 0;
        while(true){
            tmp = tmp.substring(tmp.indexOf('\n')+1);
            line_counter++;
            if(line_number==line_counter)break;
        }
        int index = tmp.indexOf("\n\n");
        if(index<0){
            return tmp;
        }
        tmp = tmp.substring(0, index);
        return tmp;
    }
}
