package com.ecnu.sei.manuzhang.nim;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class GameActivity extends Activity {
	private static final String TAG = GameActivity.class.getSimpleName();
	private static final String EXTRA_START_PLAYER = 
			"com.ecnu.sei.manuzhang.nim.GameActivity.EXTRA_START_PLAYER";

	public static final String NUM_1 = "com.ecnu.sei.manuzhang.nim.num1";
	public static final String NUM_2 = "com.ecnu.sei.manuzhang.nim.num2";
	public static final String NUM_3 = "com.ecnu.sei.manuzhang.nim.num3";

	private static final int MSG_COMPUTER_TURN = 1;
	private static final long COMPUTER_DELAY_MS = 500;

	private Handler mHandler = new Handler(new Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			if (msg.what == MSG_COMPUTER_TURN) {
				finishTurn();
				return true;
			}
			return false;
		}

	});
	private GameView mGameView;
	private TextView mInfoView;
	private static Button mButtonNext;

	public enum State {
		UNKNOWN(-3),
		WIN(-2),
		EMPTY(0),
		PLAYER1(1),
		PLAYER2(2);

		private int mValue;

		private State(int value) {
			mValue = value;
		}

		public int getValue() {
			return mValue;
		}

		public static State fromInt(int i) {
			for (State s : values()) {
				if (s.getValue() == i) 
					return s;
			}
			return EMPTY;
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "game created");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game);

		GameViewRenderer.num_1 = getIntent().getIntExtra(NUM_1, Integer.parseInt(getString(R.string.default1)));
		GameViewRenderer.num_2 = getIntent().getIntExtra(NUM_2, Integer.parseInt(getString(R.string.default2)));
		GameViewRenderer.num_3 = getIntent().getIntExtra(NUM_3, Integer.parseInt(getString(R.string.default3)));
		GameViewRenderer.mNum = GameViewRenderer.num_1 
				+ GameViewRenderer.num_2
				+ GameViewRenderer.num_3;

		mGameView = (GameView) findViewById(R.id.game_view);
		mInfoView = (TextView) findViewById(R.id.info_turn);
		mButtonNext = (Button) findViewById(R.id.next_turn);

		mGameView.setFocusableInTouchMode(true);
		mButtonNext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				State player = mGameView.getCurrentPlayer();

				if (player == State.WIN) {
					GameActivity.this.finish();
				} else if (player == State.PLAYER1) {
				    mGameView.setOneStep();
					finishTurn();
				}

			}

		});
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

	private State selectTurn(State player) {
		mGameView.setCurrentPlayer(player);
		mButtonNext.setEnabled(false);

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
				mHandler.sendEmptyMessageDelayed(MSG_COMPUTER_TURN, COMPUTER_DELAY_MS);
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
		mButtonNext.setEnabled(true);
		mButtonNext.setText("Back");

		String text;

		if (player == State.PLAYER1)
			text = getString(R.string.player1_win);
		else 
			text = getString(R.string.player2_win);
		mInfoView.setText(text);
	}

	private void setFinished(State player) {
		mGameView.setCurrentPlayer(State.WIN);
		mGameView.setWinner(player);
		mGameView.setEnabled(false);

		setWinState(player);
	}

	public static Button getButton() {
		return mButtonNext;
	}
}



