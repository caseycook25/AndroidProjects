package com.example.cook01.myapplication;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Cook01 on 4/19/2017.
 */

public class MyReceiver extends BroadcastReceiver {

    private static final String TAG = "MyReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        //Log.e(TAG, "hit onreceive");
        //Toast.makeText(context, "In Broadcast Receiver", Toast.LENGTH_SHORT).show();

        int eggs = 0;
        if (action.equals(Constants.ACTION_ADD_ONE)) {
            Log.e(TAG, "add one");
            eggs = 1;
        } else if (action.equals(Constants.ACTION_ADD_TWO)) {
            Log.e(TAG, "add two");
            eggs = 2;
        } else if (action.equals(Constants.ACTION_DEL_ONE)) {
            Log.e(TAG, "minuns one");
            eggs = -1;
        } else if (action.equals(Constants.ACTION_MAKE_BREAKFAST)) {
            Log.e(TAG, "gimme breakfast");
            eggs = 3;
        }

        Intent myIntent = new Intent(context, MyService.class);
        myIntent.putExtra("eggs", Integer.toString(eggs));
        context.startService(myIntent);
    }
}
