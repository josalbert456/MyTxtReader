package com.example.root.mytxtreaderone.processors;

import android.content.Context;

import com.example.root.mytxtreaderone.utils.Constants;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class FileProcessor {
    public int[] prevReadLengths = new int[102400];
    public int pageNo = 1;
    public static int MAX_READ_COUNTS = 2048;
    File file;
    Reader r;
    String path = "";
    public long length = 0;
    public long readLength = 0;
    public String readBuffer = "";
    Context context;
    public FileProcessor(String path, Context context) throws IOException{
        file = Constants.file;
        length = file.length();
        r = new FileReader(file);
        this.context = context;
        this.path = path;
    }

    public String read(int length)throws IOException{
        char[] buffer = new char[length];
        r.read(buffer);
        readBuffer = "";
        for(int i=0; i<buffer.length; i++){
            readBuffer += buffer[i];
        }
        readLength += length;

        return  readBuffer;
    }
    public String readForward(int length, int skipLength)throws IOException{
        /*char [] wholeFile = new char[(int)this.length];
        r.read(wholeFile);
        r.close();
        r = new FileReader(file);
        int l = wholeFile.length;
        Logger.open();
        Logger.info(String.valueOf(l));
        Logger.close();*/
        char[] buffer = new char[length];
        r.skip(skipLength);
        r.read(buffer);
        readBuffer = "";
        for(int i=0; i<buffer.length; i++){
            readBuffer += buffer[i];
        }
        readLength += length;

        return  readBuffer;
    }

    public String readBackward()throws IOException{
        int skipLength = 0;
        for(int i=1; i<pageNo-1; i++){
            skipLength += prevReadLengths[i];
        }

        r.close();
        file = Constants.file;
        r = new FileReader(file);
        r.skip(skipLength);
        readLength-=prevReadLengths[pageNo];
        pageNo--;
        char[] buffer = new char[prevReadLengths[pageNo]];
        r.read(buffer);
        readBuffer = "";
        for(int i=0; i<buffer.length; i++){
            readBuffer += buffer[i];
        }

        return readBuffer;

    }
    public void close()throws IOException{
        r.close();
    }
}
