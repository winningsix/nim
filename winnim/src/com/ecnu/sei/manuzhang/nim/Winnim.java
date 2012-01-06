package com.ecnu.sei.manuzhang.nim;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
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
import android.widget.TextView;

public class Winnim extends Activity {
	private static final String TAG = Winnim.class.getSimpleName();
	private MyOnClickListener mOnClickListener = new MyOnClickListener();
	private View mDialogLayout;
	public static final String BUTTON = "com.ecnu.sei.manuzhang.nim.button";
	public static final int NEW_BUTTON = 0;
	public static final int CONTINUE_BUTTON = 1;

	private Intent mIntent;
	private int mMax = 7;
	private int mMin = 0;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "nim created");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// can't be instantiated globally
		mIntent = new Intent(this, GameActivity.class);
		// setup button and button listener
		Button newGameButton = (Button) findViewById(R.id.new_game_button);
		newGameButton.setOnClickListener(mOnClickListener);
		Button continueButton = (Button) findViewById(R.id.continue_button);
		continueButton.setOnClickListener(mOnClickListener);
/*		Button tutorialButton = (Button) findViewById(R.id.tutorial_button);
		tutorialButton.setOnClickListener(mOnClickListener);*/
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
				int num_1 = Integer.parseInt(((EditText)mDialogLayout.findViewById(R.id.edit_text1)).getText().toString());
				int num_2 = Integer.parseInt(((EditText)mDialogLayout.findViewById(R.id.edit_text2)).getText().toString());
				int num_3 = Integer.parseInt(((EditText)mDialogLayout.findViewById(R.id.edit_text3)).getText().toString());

				startGame(num_1, num_2, num_3, NEW_BUTTON);
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

	private void startGame(int button) {
		mIntent.putExtra(BUTTON, button);
		startActivity(mIntent);
	}

	private void startGame(int num_1, int num_2, int num_3, int button) {
		mIntent.putExtra(GameActivity.NUM_1, inLimit(num_1));
		mIntent.putExtra(GameActivity.NUM_2, inLimit(num_2));
		mIntent.putExtra(GameActivity.NUM_3, inLimit(num_3));
		mIntent.putExtra(BUTTON, button);
		startActivity(mIntent);
	}

	private static class AboutDialogFragment extends DialogFragment {
		int mNum;

		private static final String ARGS = "num";

		static AboutDialogFragment newInstance(int num) {
			AboutDialogFragment aboutDF = new AboutDialogFragment();
			Bundle args = new Bundle();
			args.putInt(ARGS, num);
			aboutDF.setArguments(args);
			return aboutDF;
		}

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			mNum = getArguments().getInt(ARGS);

			int style = DialogFragment.STYLE_NORMAL;
			int theme = 0;

			switch (mNum % 8) {
			case 0: style = DialogFragment.STYLE_NO_TITLE; break;
			case 1: style = DialogFragment.STYLE_NO_FRAME; break;
			case 2: style = DialogFragment.STYLE_NO_INPUT; break;
			case 3: style = DialogFragment.STYLE_NORMAL; break;
            case 4: style = DialogFragment.STYLE_NORMAL; break;
            case 5: style = DialogFragment.STYLE_NO_TITLE; break;
            case 6: style = DialogFragment.STYLE_NO_FRAME; break;
            case 7: style = DialogFragment.STYLE_NORMAL; break;
			}

			switch (mNum % 8) {
			case 3: theme = android.R.style.Theme_Holo; break;
			case 4: theme = android.R.style.Theme_Holo_Light_Dialog; break;
			case 5: theme = android.R.style.Theme_Holo_Light; break;
			case 6: theme = android.R.style.Theme_Holo_Light_Panel; break;
			case 7: theme = android.R.style.Theme_Holo_Light; break;
			}
			setStyle(style, theme);
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View v = inflater.inflate(R.layout.about, container, false);
			View tv = v.findViewById(R.id.about_content);
			((TextView) tv).setText(R.string.about_text);
            getDialog().setTitle("Nim Game");
            getDialog().setCanceledOnTouchOutside(true);
			return v;
		}
	}
	private class MyOnClickListener implements OnClickListener {
		@Override
		public void onClick(View view) {
			EditText editText;
			int text;

			switch(view.getId()) {
			case R.id.new_game_button:
				Log.d("new_game", "new game button clicked");
				openNewGameDialog();
				break;
			case R.id.continue_button:
				Log.d("continue", "continue button clicked");
				startGame(CONTINUE_BUTTON);
				break;
/*			case R.id.tutorial_button:
				break;*/
			case R.id.about_button:
				DialogFragment newFragment = AboutDialogFragment.newInstance(4);
				newFragment.show(getFragmentManager(), "dialog");
				break;
			case R.id.button_plus1:
				editText = (EditText) mDialogLayout.findViewById(R.id.edit_text1);
				text = Integer.parseInt(editText.getText().toString()) + 1;
				editText.setText(Integer.toString(inLimit(text)));
				break;
			case R.id.button_minus1:
				editText = (EditText) mDialogLayout.findViewById(R.id.edit_text1);
				text = Integer.parseInt(editText.getText().toString()) - 1;
				editText.setText(Integer.toString(inLimit(text)));
				break;
			case R.id.button_plus2:
				editText = (EditText) mDialogLayout.findViewById(R.id.edit_text2);
				text = Integer.parseInt(editText.getText().toString()) + 1;
				editText.setText(Integer.toString(inLimit(text)));
				break;
			case R.id.button_minus2:
				editText = (EditText) mDialogLayout.findViewById(R.id.edit_text2);
				text = Integer.parseInt(editText.getText().toString()) - 1;
				editText.setText(Integer.toString(inLimit(text)));
				break;
			case R.id.button_plus3:
				editText = (EditText) mDialogLayout.findViewById(R.id.edit_text3);
				text = Integer.parseInt(editText.getText().toString()) + 1;
				editText.setText(Integer.toString(inLimit(text)));
				break;
			case R.id.button_minus3:
				editText = (EditText) mDialogLayout.findViewById(R.id.edit_text3);
				text = Integer.parseInt(editText.getText().toString()) - 1;
				editText.setText(Integer.toString(inLimit(text)));
				break;
			}

		}


	}		

	private int inLimit(int text) {
		if (text > mMax)
			return mMax;
		else if (text < mMin)
			return mMin;
		else
			return text;
	}

}
















