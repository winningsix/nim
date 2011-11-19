package com.ecnu.sei.manuzhang.nim;


import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;

public class Game extends Activity {
	private final String TAG = Game.class.getSimpleName();
	private GLSurfaceView mGLView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "game created");
		super.onCreate(savedInstanceState);
		mGLView = new GameView(this);
		setContentView(mGLView);
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
		mGLView.onResume();
	}

	@Override
	public void onPause() {
		Log.d(TAG, "game paused");
		super.onPause();
		mGLView.onPause();
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
}

class GameView extends GLSurfaceView {
	private GameViewRenderer mRenderer;

	public GameView(Context context) {
		super(context);
        // Turn on error-checking and logging
        setDebugFlags(DEBUG_CHECK_GL_ERROR | DEBUG_LOG_GL_CALLS);
		mRenderer = new GameViewRenderer();
		setRenderer(mRenderer);
		setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
	}
	
}
