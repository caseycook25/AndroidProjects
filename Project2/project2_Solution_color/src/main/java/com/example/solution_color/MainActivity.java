package com.example.solution_color;


import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.library.bitmap_utilities.BitMap_Helpers;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private ImageView myImage;
    private String mCurrentPhotoPath;
    private final int TAKE_PICTURE = 1;
    private int screenheight;
    private int screenwidth;
    private Bitmap imageBitmap;
    private Bitmap colorBitmap;
    private Bitmap thresholdBitmap;
    private SharedPreferences myPreference;
    private SharedPreferences.OnSharedPreferenceChangeListener listener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        imageBitmap = BitMap_Helpers.copyBitmap(getResources().getDrawable(R.drawable.gutters));

        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent myIntent = new Intent(this, SettingsActivity.class);
                startActivity(myIntent);
                break;
            default:
                break;
        }
        return true;
    }


    // click camera image
    public void doCamera(View view) {
        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (pictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (Exception e) {
                Toast exceptionToast = Toast.makeText(getApplicationContext(), "Error occured while creating file", Toast.LENGTH_LONG);
                exceptionToast.show();
            }
            if (photoFile != null) {
                Uri photoURI = getUri(photoFile);
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(pictureIntent, TAKE_PICTURE);
            }
        }
    }

    // Create file for image
    private File createImageFile() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_.jpg";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = new File(storageDir, imageFileName);

        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }


    // Result of Camera Activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        galleryAddPic();
        myImage = (ImageView) findViewById(R.id.myImage);
        if (requestCode == TAKE_PICTURE && resultCode == RESULT_OK) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, requestCode);
            getDemensions();

            imageBitmap = Camera_Helpers.loadAndScaleImage(mCurrentPhotoPath, screenheight, screenwidth);
            myImage.setImageBitmap(imageBitmap);
        }
    }

    // Add imaage to photos
    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = getUri(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    // Get URI from file
    private Uri getUri(File f) {
        return Uri.fromFile(f);
    }


    // Reset to default image
    public void doReset(MenuItem item) {
        myImage = (ImageView) findViewById(R.id.myImage);
        Camera_Helpers.delSavedImage(mCurrentPhotoPath);
        myImage.setImageResource(R.drawable.gutters);
        myImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
        myImage.setScaleType(ImageView.ScaleType.FIT_XY);
    }

    //
    public void doSketch(MenuItem item) {
        threshold();
        myImage.setImageBitmap(thresholdBitmap);
    }

    private void threshold() {
        //SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        //int threshold = preferences.getInt("sketchiness", 0);

        myImage = (ImageView) findViewById(R.id.myImage);
        thresholdBitmap = BitMap_Helpers.thresholdBmp(imageBitmap, 25);
    }

    private void colorize() {
       // SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
       // int saturation = preferences.getInt("saturation", 0);

        myImage = (ImageView) findViewById(R.id.myImage);
        colorBitmap = BitMap_Helpers.colorBmp(imageBitmap, 50);
    }

    public void doColorize(MenuItem item) {
        threshold();
        colorize();
        BitMap_Helpers.merge(colorBitmap, thresholdBitmap);
        myImage.setImageBitmap(colorBitmap);
    }


    // Get screen height and width
    private void getDemensions() {
        DisplayMetrics metrics = this.getResources().getDisplayMetrics();
        screenheight = metrics.heightPixels;
        screenwidth = metrics.widthPixels;
    }


    // Share Picture
    public void doShare(MenuItem item) {
        try {
            File imageFile = new File(mCurrentPhotoPath);
            Intent shareIntent = new Intent();

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            String subjectDefault = preferences.getString("subject", "");
            String textDefault = preferences.getString("ShareText", "");

            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, getUri(imageFile));
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, subjectDefault);
            shareIntent.putExtra(Intent.EXTRA_TEXT, textDefault);
            shareIntent.setType("image/jpg");
            startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.send_to)));
        } catch (Exception e) {
            Toast exceptionToast = Toast.makeText(getApplicationContext(), "Can't find image path.", Toast.LENGTH_LONG);
            exceptionToast.show();
        }
    }
}




/*
    //
    // Preferences
    //
    public void getMyPrefs() {
        myPreference = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean hasBeenShown = myPreference.getBoolean("PREF_CHECKBOX", false);
    }

    public void doPrefChangeListener(View view) {
        listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                Toast.makeText(MainActivity.this, "Key=" + key, Toast.LENGTH_SHORT).show();
                if (key.equals("PREF_LIST")) {
                    String myString = myPreference.getString("PREF_LIST", "Nothing Found");
                    Toast.makeText(MainActivity.this, "From Listener PREF_LIST=" + myString,
                            Toast.LENGTH_SHORT).show();
                }
            }
        };
        // register the listener
        myPreference.registerOnSharedPreferenceChangeListener(listener);
    }
*/


