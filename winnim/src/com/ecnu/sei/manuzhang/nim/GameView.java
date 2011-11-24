package com.ecnu.sei.manuzhang.nim;

import com.ecnu.sei.manuzhang.nim.GameActivity.State;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class GameView extends GLSurfaceView {
	private GameViewRenderer mRenderer;
    private static final int MSG_GRAY = 1;
    private final Handler mHandler = new Handler(new MyHandler());
    
    private State mCurrentPlayer = State.UNKNOWN;
    private State mWinner = State.EMPTY;
    
    private boolean mGrayDisplayOff;
    
	@Override
	public boolean onTouchEvent(final MotionEvent event) {
		float x = event.getXPrecision();
		float y = event.getYPrecision();
		
		switch (event.getActionMasked()) {
		case MotionEvent.ACTION_DOWN:
			
			break;
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
	
	
}
