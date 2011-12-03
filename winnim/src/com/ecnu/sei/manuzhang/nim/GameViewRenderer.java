package com.ecnu.sei.manuzhang.nim;

import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLSurfaceView;
import android.util.Log;

public class GameViewRenderer implements GLSurfaceView.Renderer {
	private static final String TAG = GameViewRenderer.class.getSimpleName();
	//private Torus mTorus;
	//private Cylinder mCylinder;
	public static int num_1;
	public static int num_2;
	public static int num_3;
	public static int mNum;
	public static int mSelected = 0;
	public static ArrayList<Float[]> mTorusMask = new ArrayList<Float[]>();
	private float mtStride = 0.8f; // the distance between every two torus
	private float mcStride; // the distance between every two cylinder
	private final float mtorusMinorRadius = 0.2f;
	private final float mtorusMajorRadius = 0.6f;
	private final float mcylinderRadius = 0.3f;
	private final float mcylinderHeight = 18.0f;

	@Override
	public void onDrawFrame(GL10 gl) {
		Log.d(TAG, "onDrawFrame");
		OpenGLJNILib.onDrawFrame(num_1, num_2, num_3, mtStride, mcStride, mSelected);
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		Log.d(TAG, "onSurfaceChanged");
		mcStride = (float) 2.5 * width / height;
	    setTorusMask();
		OpenGLJNILib.onSurfaceChanged(width, height);
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		Log.d(TAG, "onSurfaceCreated");
		OpenGLJNILib.onSurfaceCreated(mtorusMinorRadius, mtorusMajorRadius, mcylinderRadius, mcylinderHeight);
	}

	public void setTorusMask() {
		Float[] origin = new Float[]{-mcStride, mtStride * num_1 / 2}; // center of torus, starting from the left cylinder
		Float offsetX = mtorusMinorRadius + mtorusMajorRadius; // half width
		Float offsetY = mtorusMinorRadius; // half height
		for (int i = 0; i < mNum; i++) {
			if (i == num_1) {  // move to the middle cylinder
				origin[0] = origin[0] + mcStride;
				origin[1] = mtStride * num_2 / 2;
			}
			if (i == num_1 + num_2) { // move to the right cylinder
				origin[0] = origin[0] + mcStride;
				origin[1] = mtStride * num_3 / 2;
			}
			Float[] mask = new Float[]{(origin[0] - offsetX) / 5, (origin[1] + offsetY) / 5, 
					                   (origin[0] + offsetX) / 5, (origin[1] - offsetY) / 5};
			mTorusMask.add(mask);
			origin[1] = origin[1] - mtStride;
		}
	}
}



















