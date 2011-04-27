package com.milang.torparknow;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceManager;

public class PreferencesActivity extends PreferenceActivity {

    boolean CheckboxPreference;
    String ListPreference;
    String editTextPreference;
    String ringtonePreference;
    String secondEditTextPreference;
    String customPref;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        
    	super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        
        Preference numOfResults = (Preference) findPreference("pref_numOfResults");
        numOfResults.setOnPreferenceClickListener(new OnPreferenceClickListener() {

                public boolean onPreferenceClick(Preference preference) {                    
                    //SharedPreferences customSharedPreference = getPreferences(Activity.MODE_PRIVATE);
                    //SharedPreferences.Editor editor = customSharedPreference.edit();
                    //editor.putString("pref_numOfResults","This is text being changed");
                    //editor.commit();
                    return true;
                }
            });
        }
    
    @Override
    protected void onStop() {
    	super.onStop();
    	
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        editTextPreference = prefs.getString("pref_numOfResults","Nothing has been entered");
    }
 
    @SuppressWarnings("unused")
	private void getPrefs() {
    	
        // Get the xml/preferences.xml preferences
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        CheckboxPreference = prefs.getBoolean("checkboxPref", true);
        ListPreference = prefs.getString("listPref", "nr1");
        editTextPreference = prefs.getString("editTextPref","Nothing has been entered");
        ringtonePreference = prefs.getString("ringtonePref","DEFAULT_RINGTONE_URI");
        secondEditTextPreference = prefs.getString("SecondEditTextPref","Nothing has been entered");
        
        // Get the custom preference
        SharedPreferences mySharedPreferences = getSharedPreferences(
        "myCustomSharedPrefs", Activity.MODE_PRIVATE);
        customPref = mySharedPreferences.getString("myCusomPref", "");
    }
}