package com.ecnu.sei.manuzhang.nim;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.ecnu.sei.manuzhang.nim.GameView.IViewListener;
import com.ecnu.sei.manuzhang.nim.GameView.State;

public class GameActivity extends Activity {
	private static final String TAG = GameActivity.class.getSimpleName();
	private static final String EXTRA_START_PLAYER = 
			"com.ecnu.sei.manuzhang.nim.GameActivity.EXTRA_START_PLAYER";

	public static final String NUM_1 = "com.ecnu.sei.manuzhang.nim.num1";
	public static final String NUM_2 = "com.ecnu.sei.manuzhang.nim.num2";
	public static final String NUM_3 = "com.ecnu.sei.manuzhang.nim.num3";


	private static final int MSG_COMPUTER_TURN = 1;
	private static final long COMPUTER_DELAY_MS = 3000;
	private static final long PLAYER_DELAY_MS = 2500;
	private static final int DIALOG_KEY = 0;

	private ProgressThread mProgressThread;
	private ProgressDialog mProgressDialog;

	private Handler mHandler = new Handler(new Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			if (msg.what == MSG_COMPUTER_TURN) {
				int num_1 = GameViewRenderer.num_1;
				int num_2 = GameViewRenderer.num_2;
				int num_3 = GameViewRenderer.num_3;
				if (getNim(num_1, num_2, num_3) == 0) { // we are almost doomed
					if (num_1 != 0)
						num_1 -= 1;
					else if (num_2 != 0)
						num_2 -= 1;
					else if (num_3 != 0)
						num_3 -= 1;
				}
				else { // showtime
					if (num_1 == num_2)
						num_3 = 0;
					else if (num_1 == num_3)
						num_2 = 0;
					else if (num_2 == num_3)
						num_1 = 0;
					else {
						int tmp_1 = num_1;
						int tmp_2 = num_2;
						int tmp_3 = num_3;

						while (getNim(tmp_1, tmp_2, tmp_3) != 0 && tmp_1 != 0) 
							tmp_1 -= 1;
						if (tmp_1 == 0) {
							tmp_1 = num_1;
							while (getNim(tmp_1, tmp_2, tmp_3) !=0 && tmp_2 != 0)
								tmp_2 -= 1;
							if (tmp_2 == 0) {
								tmp_2 = num_2;
								while (getNim(tmp_1, tmp_2, tmp_3) != 0 && tmp_3 != 0)
									tmp_3 -= 1;
							}
						}
						num_1 = tmp_1;
						num_2 = tmp_2;
						num_3 = tmp_3;
					}
				}					
				mGameView.setComputerMove(num_1, num_2, num_3);
				try {
					Thread.sleep(PLAYER_DELAY_MS);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				finishTurn();
				return true;
			}
			return false;
		}

		private int getNim(int a, int b, int c) {
			return (a ^ b) ^ c;
		}

	});
	private GameView mGameView;
	private TextView mInfoView;
	private Button mButtonNext;



	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "game created");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game);

        getNums();

		mGameView = (GameView) findViewById(R.id.game_view);
		mInfoView = (TextView) findViewById(R.id.info_turn);
		mButtonNext = (Button) findViewById(R.id.next_turn);

		mGameView.setFocusableInTouchMode(true);
		mGameView.setViewListener(new IViewListener() {

			@Override
			public void onTouchView(boolean selected) {
				if (mGameView.getCurrentPlayer() == State.PLAYER1) {
					mButtonNext.setEnabled(selected);
				}

			}
		});

		mButtonNext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				State player = mGameView.getCurrentPlayer();

				if (player == State.WIN) {
					GameActivity.this.finish();
				} else if (player == State.PLAYER1) {
					mGameView.setOneStep();
					try {
						Thread.sleep(COMPUTER_DELAY_MS);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					finishTurn();
				}

			}

		});
		showDialog(DIALOG_KEY);
	}

	private void getNums() {
		int button = getIntent().getIntExtra(Winnim.BUTTON, Winnim.NEW_BUTTON);
		switch (button) {
		case Winnim.NEW_BUTTON:
			GameViewRenderer.num_1 = getIntent().getIntExtra(NUM_1, Integer.parseInt(getString(R.string.default1)));
			GameViewRenderer.num_2 = getIntent().getIntExtra(NUM_2, Integer.parseInt(getString(R.string.default2)));
			GameViewRenderer.num_3 = getIntent().getIntExtra(NUM_3, Integer.parseInt(getString(R.string.default3)));
			GameViewRenderer.mNum = GameViewRenderer.num_1 
					+ GameViewRenderer.num_2
					+ GameViewRenderer.num_3;
			break;
		case Winnim.CONTINUE_BUTTON:
			GameViewRenderer.num_1 = getPreferences(MODE_PRIVATE).getInt(GameActivity.NUM_1, 
					Integer.parseInt(getText(R.string.default1).toString()));
			GameViewRenderer.num_2 = getPreferences(MODE_PRIVATE).getInt(GameActivity.NUM_2, 
					Integer.parseInt(getText(R.string.default2).toString()));
			GameViewRenderer.num_3 = getPreferences(MODE_PRIVATE).getInt(GameActivity.NUM_3, 
					Integer.parseInt(getText(R.string.default3).toString()));
			GameViewRenderer.mNum = GameViewRenderer.num_1 
					+ GameViewRenderer.num_2
					+ GameViewRenderer.num_3;
		}
	}
	@Override
	public void onStart() {
		Log.d(TAG, "game started");
		super.onStart();
	}

	@Override
	public void onResume() {
		Log.d(TAG, "game resumed");
		super.onResume();

		State player = mGameView.getCurrentPlayer();
		if (player == State.UNKNOWN) {
			player = State.fromInt(getIntent().getIntExtra(EXTRA_START_PLAYER, 1));
			if (!checkGameFinished(player))
				selectTurn(player);
		}
		if (player == State.PLAYER2)
			mHandler.sendEmptyMessageDelayed(MSG_COMPUTER_TURN, COMPUTER_DELAY_MS);
		if (player == State.WIN)
			setWinState(mGameView.getWinner());
	}



	@Override
	public void onPause() {
		Log.d(TAG, "game paused");
		super.onPause();
		getPreferences(MODE_PRIVATE).edit()
		.putInt(NUM_1, GameViewRenderer.num_1)
		.putInt(NUM_2, GameViewRenderer.num_2)
		.putInt(NUM_3, GameViewRenderer.num_3)
		.commit();
	}

	@Override
	public void onStop() {
		Log.d(TAG, "game stopped");
		super.onStop();
	}

	@Override
	public void onDestroy() {
		Log.d(TAG, "game destroyed");
		super.onDestroy();
	}

	@Override
	public void onRestart() {
		Log.d(TAG, "game restarted");
		super.onRestart();
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch(id) {
		case DIALOG_KEY:
			mProgressDialog = new ProgressDialog(this);
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			mProgressDialog.setMessage("Loading...");
			return mProgressDialog;
		default:
			return null;
		}
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		switch(id) {
		case DIALOG_KEY:
			mProgressDialog.setProgress(0);
			mProgressThread = new ProgressThread(handler);
			mProgressThread.start();
		}
	}

	// Define the Handler that receives messages from the thread and update the progress
	final Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			int total = msg.arg1;
			mProgressDialog.setProgress(total);
			if (total >= 100){
				dismissDialog(DIALOG_KEY);
				mProgressThread.setState(ProgressThread.STATE_DONE);
			}
		}
	};

	/** Nested class that performs progress calculations (counting) */
	private class ProgressThread extends Thread {
		Handler mHandler;
		final static int STATE_DONE = 0;
		final static int STATE_RUNNING = 1;
		int mState;
		int total;

		ProgressThread(Handler h) {
			mHandler = h;
		}

		public void run() {
			mState = STATE_RUNNING;   
			total = 0;
			while (mState == STATE_RUNNING) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					Log.e("ERROR", "Thread Interrupted");
				}
				Message msg = mHandler.obtainMessage();
				msg.arg1 = total;
				mHandler.sendMessage(msg);
				total++;
			}
		}

		/* sets the current state for the thread,
		 * used to stop the thread */
		public void setState(int state) {
			mState = state;
		}
	}

	private State selectTurn(State player) {
		mButtonNext.setEnabled(false);
		mGameView.setCurrentPlayer(player);

		if (player == State.PLAYER1) {
			mInfoView.setText(R.string.player1_turn);
			mGameView.setEnabled(true);
		} else if (player == State.PLAYER2) {
			mInfoView.setText(R.string.player2_turn);
			mGameView.setEnabled(false);
		}
		return player;
	}

	private State getOtherPlayer(State player) {
		return player == State.PLAYER1 ? State.PLAYER2 : State.PLAYER1;
	}

	private void finishTurn() {
		State player = mGameView.getCurrentPlayer();
		if (!checkGameFinished(player)) {
			player = selectTurn(getOtherPlayer(player));
			if (player == State.PLAYER2) {
				mHandler.sendEmptyMessageDelayed(MSG_COMPUTER_TURN, 0);
			}
		}
	}

	private boolean checkGameFinished(State player) {
		if (GameViewRenderer.mNum == 0) {
			setFinished(player);
			return true;
		}
		return false;
	}

	private void setWinState(State player) {
		String text;

		if (player == State.PLAYER1)
			text = getString(R.string.player1_win);
		else 
			text = getString(R.string.player2_win);
		mInfoView.setText(text);		

		mButtonNext.setEnabled(true);
		mButtonNext.setText("Back");
	}

	private void setFinished(State player) {
		mGameView.setCurrentPlayer(State.WIN);
		mGameView.setWinner(player);
		mGameView.setEnabled(false);

		setWinState(player);
	}

}



