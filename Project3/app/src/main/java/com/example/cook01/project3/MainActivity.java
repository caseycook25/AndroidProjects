package com.example.cook01.project3;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Spinner spinner;
    private ImageView myImage;
    SharedPreferences myPreference;
    SharedPreferences.OnSharedPreferenceChangeListener listener;
    private String url = "";
    private String imageUrl = "";
    JSONArray jsonArray;
    int numberentries = -1;
    private List<String> spinnerItems;
    private List<String> fileNames;
    private static final String TAG = "MainActivity";
    private static final String NAME = "name";
    private static final String FILE = "file";
    private static final String DEFAULT_URL = "http://www.tetonsoftware.com/pets/";
    private static final String listPrefKey = "resource_list";
    private static final String JSON_OBJECT_NAME = "pets";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myPreference = PreferenceManager.getDefaultSharedPreferences(this);

        myImage = (ImageView) findViewById(R.id.myImage);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        startPrefListener();
        url = myPreference.getString(listPrefKey, "Nothing Found");
        if (url == "Nothing Found")
            url = DEFAULT_URL;

        downloadJsonTask();
    }

    private void downloadJsonTask() {
        try {
            if (checkWifi() == true) {
                DownloadTask myTask = new DownloadTask(this);
                Log.e(TAG, "url===" + url);
                myTask.execute(url);
            }
        }
        catch (Exception e) {
                e.printStackTrace();
            }
    }

    private void downloadImageTask() {
        try {
            if (checkWifi() == true) {
                DownloadImage myTask = new DownloadImage(this);
                Log.e(TAG, "url===" + url);
                myTask.execute(imageUrl);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void processJSON(String string) {
        if (string != null) {

            try {
                Log.e(TAG, "JSON string=" + string);
                JSONObject jsonobject = new JSONObject(string);

                jsonArray = jsonobject.getJSONArray(JSON_OBJECT_NAME);
                Log.e(TAG, "JSON array=" + jsonArray);

                numberentries = jsonArray.length();
                Log.e(TAG, "JSON array length=" + numberentries);

                parseJsonArray();

            } catch (Exception e) {
                e.printStackTrace();
            }

            setupSimpleSpinner();
        }
        else
            Toast.makeText(MainActivity.this, "ERROR when connecting to:" + url, Toast.LENGTH_SHORT).show();

    }

    public void parseJsonArray() throws JSONException {
        spinnerItems = new ArrayList<>();
        fileNames = new ArrayList<>();
        for (int i = 0; i < numberentries; i++) {
            JSONObject jobj = jsonArray.getJSONObject(i);

            String petName = jobj.getString(NAME);
            String petFile = jobj.getString(FILE);

            fileNames.add(petFile);
            spinnerItems.add(petName);

        }
        Log.e(TAG, "file names= " + fileNames);
        Log.e(TAG, "Spinner array=" + spinnerItems);

    }

    private void startPrefListener() {
        listener = new SharedPreferences.OnSharedPreferenceChangeListener() {

            @Override
            public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                if (key.equals(listPrefKey)) {
                    url = myPreference.getString(listPrefKey, "Nothing Found");
                    downloadJsonTask();
                }
            }
        };

        myPreference.registerOnSharedPreferenceChangeListener(listener);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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


    private void setupSimpleSpinner() {
        String[] list2Array = spinnerItems.toArray(new String[spinnerItems.size()]);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item_simple, list2Array);
        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        spinner = (Spinner) findViewById(R.id.action_spinner);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public static final int SELECTED_ITEM = 0;

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int pos, long rowid) {
                if (arg0.getChildAt(SELECTED_ITEM) != null) {
                    imageUrl = url + fileNames.get(pos);
                    Log.e(TAG, "image url" + imageUrl);
                    downloadImageTask();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
    }

    private boolean checkWifi() {
        ConnectivityManager cm =  (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean wifi = true;

        try {
            boolean isWiFi = activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;
            boolean isMobile = activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE;

            if (isWiFi == true)
               Log.e(TAG, "using wifi");
            else if (isMobile == true)
                Log.e(TAG, "using cellular");
        }
        catch (Exception e) {
            Toast.makeText(MainActivity.this, "You are not connected to the internet", Toast.LENGTH_SHORT).show();
            wifi = false;
        }
        return wifi;
    }
}
