package com.example.root.mytxtreaderone;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.root.mytxtreaderone.platform.TextViewer;

import java.util.logging.Logger;


public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
    public void startRead(View view){
        startActivity(new Intent(this, TextViewer.class));
    }
    public void onPause(){
        super.onPause();
        System.exit(0);
    }
}
