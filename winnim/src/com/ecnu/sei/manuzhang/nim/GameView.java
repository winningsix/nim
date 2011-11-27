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
		else if (action == MotionEvent.ACTION_UP) {
			int selected = OpenGLJNILib.onTorus(x, y);
/*			if (selected >= 1) {
				OpenGLJNILib.onDrawFrame(GameViewRenderer.num_1,
						GameViewRenderer.num_2,
						GameViewRenderer.num_3,
						selected);
			} */
		}
			OpenGLJNILib.onDrawFrame(GameViewRenderer.num_1,
						GameViewRenderer.num_2,
						GameViewRenderer.num_3,
						3);
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
