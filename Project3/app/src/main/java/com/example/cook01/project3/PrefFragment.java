package com.example.cook01.project3;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by Cook01 on 3/23/2017.
 */

public class PrefFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
