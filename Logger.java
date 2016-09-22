package com.example.root.mytxtreaderone.utils;

import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class Logger {
    private static OutputStream os;
    public static void open(){
        String path = Environment.getExternalStorageDirectory() + "/log.txt";
        File file = new File(path);
        try{
            if(!file.exists()) file.createNewFile();
                os = new FileOutputStream(file, true);
        }catch (IOException ie){

        }

    }
    public static void info(String info){
        try{
            os.write(info.getBytes());
            os.write("\n".getBytes());
        }catch (IOException ie){

        }
    }
    public static void close(){
        try{
            os.close();
        }catch (IOException ie){

        }
    }
}
