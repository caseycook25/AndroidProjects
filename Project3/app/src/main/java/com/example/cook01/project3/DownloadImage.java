package com.example.cook01.project3;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Cook01 on 3/25/2017.
 */

public class DownloadImage extends AsyncTask<String, Void, Bitmap> {

    MainActivity myActivity;
    private ImageView myImage;

    private static final String TAG = "DownloadImage";


    public DownloadImage(MainActivity activity) {
        attach(activity);
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        String myUrl = params[0];
        Bitmap bmp = null;


        try {
            URL url = new URL(myUrl);

            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            InputStream is = con.getInputStream();
            bmp = BitmapFactory.decodeStream(is);
            if (null != bmp)
                return bmp;

        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        myImage = (ImageView) myActivity.findViewById(R.id.myImage);
        myImage.setImageBitmap(result);

    }

    void detach() {
        myActivity = null;
    }

    void attach(MainActivity activity) {
        this.myActivity = activity;
    }
}
