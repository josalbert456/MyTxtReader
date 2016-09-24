package com.example.root.mytxtreaderone.gadgets;

import android.os.Environment;

import com.example.root.mytxtreaderone.processors.FileProcessor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by root on 16-9-24.
 */
public class BookMarker {
    private String filePath;
    // if set one page as book mark,
    // all we need to do is simply write a space and a flag
    // after the page word number when recording
    private boolean bookMarkFlag = false;
    public BookMarker(String fileName){
        filePath = Environment.getExternalStorageDirectory() + "/mytxt/" + fileName;
        File dir = new File(Environment.getExternalStorageDirectory() + "/mytxt");
        if(!dir.exists())dir.mkdir();
    }
    public void record(FileProcessor fileProcessor){
        File file = new File(filePath);
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

            os.close();
        }catch (IOException ie){

        }
    }
    public int decord(FileProcessor fileProcessor){
        File file = new File(filePath);
        int skipLength = 0;

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
                    skipLength += Integer.valueOf(record);
                }
                is.close();
                file.delete();
            }
        }catch (IOException ie){

        }
        return skipLength;
    }
}
