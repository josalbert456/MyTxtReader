package com.example.root.mytxtreaderone.gadgets;

/**
 * Created by root on 16-9-23.
 */
public class Timer {
    public static void delay(long time){
        try{
            Thread.sleep(time);
        }catch (InterruptedException ie){

        }
    }
}
