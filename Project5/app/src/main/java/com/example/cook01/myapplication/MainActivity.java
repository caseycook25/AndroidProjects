package com.example.cook01.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void addOne(View view) {
        Log.e(TAG, "hit addone");
        Intent startIntent = new Intent(Constants.ACTION_ADD_ONE);
        sendBroadcast(startIntent);
    }

    public void addTwo(View view) {
        Log.e(TAG, "hit addtwo");
        Intent startIntent = new Intent(Constants.ACTION_ADD_TWO);
        sendBroadcast(startIntent);
    }

    public void delOne(View view) {
        Log.e(TAG, "hit delone");
        Intent startIntent = new Intent(Constants.ACTION_DEL_ONE);
        sendBroadcast(startIntent);
    }

    public void makeBreak(View view) {
        Log.e(TAG, "hit make");
        Intent startIntent = new Intent(Constants.ACTION_MAKE_BREAKFAST);
        sendBroadcast(startIntent);
    }
}
