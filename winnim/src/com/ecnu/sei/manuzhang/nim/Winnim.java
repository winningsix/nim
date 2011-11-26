package com.ecnu.sei.manuzhang.nim;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class Winnim extends Activity {
	private static final String TAG = Winnim.class.getSimpleName();
	MyOnClickListener mOnClickListener = new MyOnClickListener();
	View mDialogLayout;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "nim created");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);


		// setup button and button listener
		Button newGameButton = (Button) findViewById(R.id.new_game_button);
		newGameButton.setOnClickListener(mOnClickListener);
		Button continueButton = (Button) findViewById(R.id.continue_button);
		continueButton.setOnClickListener(mOnClickListener);
		Button tutorialButton = (Button) findViewById(R.id.tutorial_button);
		tutorialButton.setOnClickListener(mOnClickListener);
		Button aboutButton = (Button) findViewById(R.id.about_button);
		aboutButton.setOnClickListener(mOnClickListener);
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
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	@Override 
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.settings:
			startActivity(new Intent(this, Prefs.class));
			return true;
		}
		return false;
	}

	private void openNewGameDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
		mDialogLayout = inflater.inflate(R.layout.custom, (ViewGroup) findViewById(R.id.layout_root));

		Button button_plus1 = (Button) mDialogLayout.findViewById(R.id.button_plus1);
		button_plus1.setOnClickListener(mOnClickListener);
		Button button_minus1 = (Button) mDialogLayout.findViewById(R.id.button_minus1);
		button_minus1.setOnClickListener(mOnClickListener);

		Button button_plus2 = (Button) mDialogLayout.findViewById(R.id.button_plus2);
		button_plus2.setOnClickListener(mOnClickListener);
		Button button_minus2 = (Button) mDialogLayout.findViewById(R.id.button_minus2);
		button_minus2.setOnClickListener(mOnClickListener);

		Button button_plus3 = (Button) mDialogLayout.findViewById(R.id.button_plus3);
		button_plus3.setOnClickListener(mOnClickListener);
		Button button_minus3 = (Button) mDialogLayout.findViewById(R.id.button_minus3);
		button_minus3.setOnClickListener(mOnClickListener);

		builder
		.setView(mDialogLayout)
		.setPositiveButton("start", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				startGame();
			}
		})
		.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		})
		.show();
	}

	private void startGame() {
		startActivity(new Intent(this, GameActivity.class));
	}

	class MyOnClickListener implements OnClickListener {

		@Override
		public void onClick(View view) {
			EditText editText;
			int text;
			
			switch(view.getId()) {
			case R.id.new_game_button:
				Log.d("new_game", "new game button clicked");
				openNewGameDialog();
				//startActivity(new Intent(Winnim.this, GameActivity.class));
				break;
			case R.id.continue_button:
				break;
			case R.id.tutorial_button:
				break;
			case R.id.about_button:
				break;
			case R.id.button_plus1:
				editText = (EditText) mDialogLayout.findViewById(R.id.edit_text1);
				text = Integer.parseInt(editText.getText().toString());
                editText.setText(Integer.toString(text + 1));
				break;
			case R.id.button_minus1:
				editText = (EditText) mDialogLayout.findViewById(R.id.edit_text1);
				text = Integer.parseInt(editText.getText().toString());
                editText.setText(Integer.toString(text - 1));
				break;
			case R.id.button_plus2:
				editText = (EditText) mDialogLayout.findViewById(R.id.edit_text2);
				text = Integer.parseInt(editText.getText().toString());
                editText.setText(Integer.toString(text + 1));
				break;
			case R.id.button_minus2:
				editText = (EditText) mDialogLayout.findViewById(R.id.edit_text2);
				text = Integer.parseInt(editText.getText().toString());
                editText.setText(Integer.toString(text - 1));
				break;
			case R.id.button_plus3:
				editText = (EditText) mDialogLayout.findViewById(R.id.edit_text3);
				text = Integer.parseInt(editText.getText().toString());
                editText.setText(Integer.toString(text + 1));
				break;
			case R.id.button_minus3:
				editText = (EditText) mDialogLayout.findViewById(R.id.edit_text3);
				text = Integer.parseInt(editText.getText().toString());
                editText.setText(Integer.toString(text - 1));
				break;
			}

		}

	}
}
