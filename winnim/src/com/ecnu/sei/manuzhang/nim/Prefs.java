package com.ecnu.sei.manuzhang.nim;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.util.Log;


public class Prefs extends PreferenceActivity {
	private static final String TAG = Prefs.class.getSimpleName();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "prefs created");
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings);
	}

	  @Override
    public void onStart() {
    	Log.d(TAG, "prefs started");
    	super.onStart();
    }
    
    @Override
    public void onResume() {
    	Log.d(TAG, "prefs resumed");
    	super.onResume();
    }
    
    @Override
    public void onPause() {
    	Log.d(TAG, "prefs paused");
    	super.onPause();
    }
    
    @Override
    public void onStop() {
    	Log.d(TAG, "prefs stopped");
    	super.onStop();
    }
    
    @Override
    public void onDestroy() {
    	Log.d(TAG, "prefs destroyed");
    	super.onDestroy();
    }
    
    @Override
    public void onRestart() {
        Log.d(TAG, "prefs restarted");	
        super.onRestart();
    }
}
