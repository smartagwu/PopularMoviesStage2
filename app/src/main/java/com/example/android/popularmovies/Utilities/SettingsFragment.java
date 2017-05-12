package com.example.android.popularmovies.Utilities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.preference.PreferenceScreen;

import com.example.android.popularmovies.R;

/**
 * Created by AGWU SMART ELEZUO on 5/6/2017.
 */

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener{


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

        addPreferencesFromResource(R.xml.preference_layout);

        PreferenceScreen preferenceScreen = getPreferenceScreen();
        SharedPreferences S = PreferenceManager.getDefaultSharedPreferences(getContext());
        int count = preferenceScreen.getPreferenceCount();

        for (int i = 0; i <count; i++){
            Preference preference = preferenceScreen.getPreference(i);
            String data = S.getString(preference.getKey(), "");
            setPreferenceSummary(preference, data);
        }
    }

    private void setPreferenceSummary(Preference preference, Object value){

        if (preference instanceof ListPreference){
            ListPreference listPreference = (ListPreference) preference;
            String data = value.toString();
            int index = listPreference.findIndexOfValue(data);

            if (index >= 0){
                listPreference.setSummary(listPreference.getEntries()[index]);
            }else {
                listPreference.setSummary(data);
            }
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference preference = findPreference(key);
        String data = sharedPreferences.getString(key, "");
        setPreferenceSummary(preference, data);
    }

    @Override
    public void onStart() {
        super.onStart();
        PreferenceManager.getDefaultSharedPreferences(getContext()).registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        PreferenceManager.getDefaultSharedPreferences(getContext()).unregisterOnSharedPreferenceChangeListener(this);
    }
}
