package com.example.root.mytxtreader.file_processor;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class FileProcessor {
    File file;
    Reader r;
    public long length = 0;
    public long readLength = 0;
    public String readBuffer = "";
    public FileProcessor(String path) throws IOException{
        file = new File(path);
        length = file.length();
        r = new FileReader(file);
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

    boolean first = true;
    public void skip(int length)throws IOException{
        r.skip(length);
    }
    public void close()throws IOException{
        r.close();
    }
}
