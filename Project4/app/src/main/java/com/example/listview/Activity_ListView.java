package com.example.listview;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import java.util.List;

public class Activity_ListView extends AppCompatActivity {

    private CustomAdapter adapter;
    private String imageUrl = "";
    private List<BikeData> bikeList;
    private ListView my_listview;
    private SharedPreferences myPreference;
    private static final String DEFAULT_URL = "http://www.tetonsoftware.com/bikes";
    public static String _url = DEFAULT_URL;
    private String url = DEFAULT_URL;
    private static final String listPrefKey = "resource_list";
    public ImageView imgView;
    SharedPreferences.OnSharedPreferenceChangeListener listener;

    private static final String TAG = "Activity_ListView";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Change title to indicate sort by
        setTitle("Sort by:");

        //listview that you will operate on
        my_listview = (ListView) findViewById(R.id.lv);

        imgView = (ImageView)findViewById(R.id.imageView1);

        //toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //android.support.v7.app.ActionBar actionBar = getSupportActionBar();

        ConnectivityCheck.isNetworkReachableAlertUserIfNot(this);

        //set the listview onclick listener
        setupListViewOnClickListener();

        myPreference = PreferenceManager.getDefaultSharedPreferences(this);
        startPrefListener();

        //url = myPreference.getString(listPrefKey, "Nothing Found");
        if (url == "Nothing Found") {
            url = DEFAULT_URL;
            _url = DEFAULT_URL;
        }

        //TODO call a thread to get the JSON list of bikes
        downloadJsonTask();

        //TODO when it returns it should process this data with bindData



    }

    private void setupListViewOnClickListener() {
        //TODO you want to call my_listviews setOnItemClickListener with a new instance of android.widget.AdapterView.OnItemClickListener() {

        my_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Activity_ListView.this);
                alertDialogBuilder.setMessage(bikeList.get(position).toString());
                alertDialogBuilder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            }
        });
    }

    /**
     * Takes the string of bikes, parses it using JSONHelper
     * Sets the adapter with this list using a custom row layout and an instance of the CustomAdapter
     * binds the adapter to the Listview using setAdapter
     *
     * @param JSONString complete string of all bikes
     */
    protected void bindData(String JSONString) {
        bikeList = JSONHelper.parseAll(JSONString);
        Log.e(TAG, "result list = " + bikeList);

        adapter = new CustomAdapter(this, R.layout.listview_row_layout, bikeList, imgView);
        my_listview.setAdapter(adapter);

    }

    Spinner spinner;

    /**
     * create a data adapter to fill above spinner with choices(Company,Location and Price),
     * bind it to the spinner
     * Also create a OnItemSelectedListener for this spinner so
     * when a user clicks the spinner the list of bikes is resorted according to selection
     * dontforget to bind the listener to the spinner with setOnItemSelectedListener!
     */
    public void setupSimpleSpinner() {
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.sortable_fields, R.layout.spinner_item);
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            private static final int SELECTED_ITEM = 0;

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int pos, long rowid) {
                if (arg0.getChildAt(SELECTED_ITEM) != null) {
                    //Toast.makeText(Activity_ListView.this, "You have clicked: " + arg0.getItemAtPosition(pos), Toast.LENGTH_SHORT).show();


                    //TODO sort listview

                    //if (Activity_ListView.this.adapter != null)
                    Activity_ListView.this.adapter.sortList(pos);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
    }

    private void startPrefListener() {
        listener = new SharedPreferences.OnSharedPreferenceChangeListener() {

            @Override
            public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                Log.e(TAG, "startpreflistener");
                if (key.equals(listPrefKey)) {
                    url = myPreference.getString(listPrefKey, "Nothing Found");
                    _url = url;
                    Toast.makeText(Activity_ListView.this, "You have selected url: " + url, Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "url" + url);
                    downloadJsonTask();
                }
            }
        };

        myPreference.registerOnSharedPreferenceChangeListener(listener);
    }

    private void downloadJsonTask() {
        Log.e(TAG, "url" + url);
        try {
            if (ConnectivityCheck.isNetworkReachableAlertUserIfNot(this)) {
                DownloadTask myTask = new DownloadTask(this);
                String jsonUrl = url + "/bikes.json";
                myTask.execute(jsonUrl);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(Activity_ListView.this, "Not able to connect to the url: " + url, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent myIntent = new Intent(this, activityPreference.class);
                startActivity(myIntent);
            case R.id.action_refresh:
                Activity_ListView.this.adapter.sortList(0);
                setupSimpleSpinner();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
}
