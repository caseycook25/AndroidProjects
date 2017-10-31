package com.example.cook01.myapplication;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.Date;

/**
 * Created by Cook01 on 4/19/2017.
 */

public class MyService extends Service {

    private static final int MYNOTIFICATION = 1;
    private static final String TAG = "MyService";
    public int eggCount = 0;
    private static final String addOne = "1";
    private static final String addTwo = "2";
    private static final String delOne = "-1";
    private static final String makeBreakfast = "3";


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        eggCount = getValue();
        Log.e(TAG, "onStart count = " + Integer.toString(eggCount));
        String addEggs = intent.getStringExtra("eggs");

        switch (addEggs) {
            case addOne:
                eggCount += 1;
                break;
            case addTwo:
                eggCount += 2;
                break;
            case delOne:
                eggCount -= 1;
                break;
            case makeBreakfast:
                Log.e(TAG, "After make breakfast count = " + Integer.toString(eggCount));
                NotifyMakeBreakfast(eggCount);
                stopSelf();
                return START_STICKY;
        }

        setValue(eggCount);
        int eggs = Integer.parseInt(addEggs);
        NotifyAddEggs(eggs);
        stopSelf();

        return START_NOT_STICKY;
    }

    private void NotifyMakeBreakfast(int eggCount) {
        if (eggCount < 6) {
            setValue(eggCount);
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.ic_launcher)
                            .setContentTitle("@string/app_name")
                            .setContentText("We are having gruel, we have " + eggCount + " eggs available");

            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(MYNOTIFICATION, mBuilder.build());
        } else {
            eggCount -= 6;
            setValue(eggCount);
            Log.e(TAG, "set value after notifybreak and subtract 6 count = " + Integer.toString(eggCount));
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.ic_launcher)
                            .setContentTitle("@string/app_name")
                            .setContentText("We are having omelets, we have " + eggCount + " egg(s) available");

            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(MYNOTIFICATION, mBuilder.build());
        }
    }

    private void NotifyAddEggs(int eggsToAdd) {

        int m = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("@string/app_name")
                        .setContentText(eggsToAdd + " added!");

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(m, mBuilder.build());

    }

    public void onCreate() {
        eggCount = getValue();
    }
    /*public void onDestroy() {
        setValue(eggCount);
    }*/

    public int getValue() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        return prefs.getInt("value_key", 0);
    }

    public void setValue(int newValue) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("value_key", newValue);
        editor.commit();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
