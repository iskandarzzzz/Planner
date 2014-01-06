package com.varun.planner.Fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.varun.planner.R;

/**
 * Created by VSC on 02/11/13.
 */
public class PrefsFragment extends PreferenceFragment
{

    public PrefsFragment()
    {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.prefrences);
    }
}