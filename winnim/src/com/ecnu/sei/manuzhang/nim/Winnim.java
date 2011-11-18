package com.ecnu.sei.manuzhang.nim;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Winnim extends Activity implements OnClickListener {
	private final String TAG = Winnim.class.getSimpleName();
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "nim created");
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // setup button and button listener
        Button newGameButton = (Button) findViewById(R.id.new_game_button);
        newGameButton.setOnClickListener(this);
        Button continueButton = (Button) findViewById(R.id.continue_button);
        continueButton.setOnClickListener(this);
        Button tutorialButton = (Button) findViewById(R.id.tutorial_button);
        tutorialButton.setOnClickListener(this);
        Button aboutButton = (Button) findViewById(R.id.about_button);
        aboutButton.setOnClickListener(this);
    }
    
    @Override
    public void onStart() {
    	Log.d(TAG, "nim started");
    	super.onStart();
    }
    
    @Override
    public void onResume() {
    	Log.d(TAG, "nim resumed");
    	super.onResume();
    	
    }
    
    @Override
    public void onPause() {
    	Log.d(TAG, "nim paused");
    	super.onPause();
    }
    
    @Override
    public void onStop() {
    	Log.d(TAG, "nim stopped");
    	super.onStop();
    }
    
    @Override
    public void onDestroy() {
    	Log.d(TAG, "nim destroyed");
    	super.onDestroy();
    }
    
    @Override
    public void onRestart() {
        Log.d(TAG, "nim restarted");	
        super.onRestart();
    }
    
    @Override
    public void onClick(View view) {
    	switch(view.getId()) {
    	case R.id.new_game_button:
    		Log.d("new_game", "new game button clicked");
    		startActivity(new Intent(this, Game.class));
    		break;
    	case R.id.continue_button:
    		break;
    	case R.id.tutorial_button:
    		break;
    	case R.id.about_button:
    		break;
    	}
    }
}