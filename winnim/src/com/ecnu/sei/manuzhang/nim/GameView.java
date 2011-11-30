package com.ecnu.sei.manuzhang.nim;

import java.util.ArrayList;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.ecnu.sei.manuzhang.nim.GameActivity.State;

public class GameView extends GLSurfaceView {
	private GameViewRenderer mRenderer;
	private static final int MSG_GRAY = 1;
	//  private final Handler mHandler = new Handler(new MyHandler());

	private State mCurrentPlayer = State.UNKNOWN;
	private State mWinner = State.EMPTY;

	private boolean mGrayDisplayOff;

	@Override
	public boolean onTouchEvent(final MotionEvent event) {
		float width = getWidth();
		float height = getHeight();
		float x = (event.getX() - width / 2) / (height / 2);
		float y = (height / 2 - event.getY()) / (height / 2);

		int action = event.getActionMasked();

		if (action == MotionEvent.ACTION_DOWN)
			return true;
		else if (action == MotionEvent.ACTION_UP)  {
			int selected = onTorus(x, y, GameViewRenderer.mTorusMask);
			if (selected > 0) {
				GameViewRenderer.mSelected = selected;
				requestRender();
				GameActivity.getButton().setEnabled(true);
			}
		}

		return true;
	}


	public GameView(Context context) {
		super(context);
		initRender();
	}

	public GameView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initRender();
	}

	private void initRender() {
		// Turn on error-checking and logging
		setDebugFlags(DEBUG_CHECK_GL_ERROR | DEBUG_LOG_GL_CALLS);
		mRenderer = new GameViewRenderer();
		setRenderer(mRenderer);
		setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
	}

	public void setOneStep() {
		int selected = GameViewRenderer.mSelected;
		int num_1 = GameViewRenderer.num_1;
		int num_2 = GameViewRenderer.num_2;
		int num_3 = GameViewRenderer.num_3;
		if (selected <= num_1)
			num_1 = num_1 - selected;
		else if (selected <= num_1 + num_2)
			num_2 = num_2 - (selected - num_1);
		else
			num_3 = num_3 - (selected - num_1 - num_2);
		GameViewRenderer.num_1 = num_1;
		GameViewRenderer.num_2 = num_2;
		GameViewRenderer.num_3 = num_3;
		GameViewRenderer.mNum = num_1 + num_2 + num_3;
		GameViewRenderer.mSelected = 0;
		GameViewRenderer.mTorusMask.clear();
		requestRender();
		mRenderer.setTorusMask();
	}

	public State getCurrentPlayer() {
		return mCurrentPlayer;
	}

	public void setCurrentPlayer(State player) {
		mCurrentPlayer = player;
	}

	public State getWinner() {
		return mWinner;
	}

	public void setWinner(State winner) {
		mWinner = winner;
	}

	private int onTorus(float x, float y, ArrayList<Float[]> mask) {
		int count = mask.size();
		for (int i = 0; i < count; i++) {
			float x1 = mask.get(i)[0];
			float z1 = mask.get(i)[1];
			float x3 = mask.get(i)[2];
			float z3 = mask.get(i)[3];
			if (x >= x1 && x <= x3 && y >= z3 && y <= z1)
				return i + 1;
		}
		return 0;
	}
}
