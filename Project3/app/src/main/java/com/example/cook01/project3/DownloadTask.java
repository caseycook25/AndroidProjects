package com.example.cook01.project3;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;



public class DownloadTask extends AsyncTask<String, Void, String> {

    private static final int READ_THIS_AMOUNT = 8096;
    private String myQuery = "";
    private static final int TIMEOUT = 1000;
    MainActivity myActivity;
    private static final String TAG = "DownloadTask";

    public DownloadTask(MainActivity activity) {
        attach(activity);
    }

    @Override
    protected String doInBackground(String... params) {
        String myUrl = params[0];
        myUrl += "/pets.json";

        try {
            URL url = new URL(myUrl);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            connection.setReadTimeout(TIMEOUT);
            connection.setConnectTimeout(TIMEOUT);
            connection.setRequestProperty("Accept-Charset", "UTF-8");

            BufferedReader in = null;
            try {
                connection.connect();

                int statusCode = connection.getResponseCode();
                if (statusCode / 100 != 2) {
                    return null;
                }

                in = new BufferedReader(new InputStreamReader(connection.getInputStream()), READ_THIS_AMOUNT);

                String myData;
                StringBuffer sb = new StringBuffer();

                while ((myData = in.readLine()) != null) {
                    sb.append(myData);
                }
                Log.e(TAG, "JSON string=" + sb.toString());
                return sb.toString();

            } finally {
                in.close();
                connection.disconnect();
            }
        } catch (Exception exc) {
            exc.printStackTrace();
            Log.e(TAG, "hit exception");
            return null;
        }
    }

    @Override
    protected void onPostExecute(String result) {
        Log.e(TAG, "myActivity == " + myActivity);
        if (myActivity != null) {
            Log.e(TAG, "hits this if myActivity != null");
            myActivity.processJSON(result);
        }
    }



    void detach() {
        myActivity = null;
    }

    void attach(MainActivity activity) {
        this.myActivity = activity;
    }
}

