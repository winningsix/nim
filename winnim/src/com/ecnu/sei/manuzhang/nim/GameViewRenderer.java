package com.ecnu.sei.manuzhang.nim;


import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLSurfaceView;

public class GameViewRenderer implements GLSurfaceView.Renderer {
	private static final String TAG = GameViewRenderer.class.getSimpleName();

	float[] origin = new float[]{0.0f, 0.0f, 0.0f};
	public static ArrayList<TorusInfo> mTorusList = new ArrayList<TorusInfo>();


	@Override
	public void onDrawFrame(GL10 gl) {
		checkGLError(gl);
/*		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		
		GLU.gluLookAt(gl, 0f, 0f, 5.0f, 0f, 0f, 0f, 0f, 1.0f, 0f);
		gl.glRotatef(-85.0f, 1.0f, 0.0f, 0.0f);

		// left cylinder with 5 Toruses
		gl.glPushMatrix();
		gl.glTranslatef(-ratio * 3, 0.0f, 0.0f);
		gl.glColor4f(1.0f, 0.0f, 0.5f, 1.0f);
		mCylinder.draw(gl);
		gl.glColor4f(1.0f, 1.0f, 0.0f, 0.5f);
		mTorus.drawTorus(gl);
		gl.glTranslatef(0.0f, 0.0f, distance);
		mTorus.drawTorus(gl);
		gl.glTranslatef(0.0f, 0.0f, distance);
		mTorus.drawTorus(gl);
		gl.glTranslatef(0.0f, 0.0f, -distance * 3);
		mTorus.drawTorus(gl);
		gl.glTranslatef(0.0f, 0.0f, -distance);
		mTorus.drawTorus(gl);
		gl.glPopMatrix();

		// middle with 2 toruses
		gl.glPushMatrix();
		gl.glColor4f(1.0f, 0.0f, 0.5f, 1.0f);
		mCylinder.draw(gl);
		gl.glColor4f(1.0f, 1.0f, 0.0f, 0.5f);
		gl.glTranslatef(0.0f, 0.0f, distance);
		mTorus.drawTorus(gl);
		gl.glTranslatef(0.0f, 0.0f, -distance * 2);
		mTorus.drawTorus(gl);
		gl.glPopMatrix();

		// right with 4 torus
		gl.glPushMatrix();
		gl.glTranslatef(ratio * 3, 0.0f, 0.0f);
		gl.glColor4f(1.0f, 0.0f, 0.5f, 1.0f);
		mCylinder.draw(gl);
		gl.glColor4f(1.0f, 1.0f, 0.0f, 0.5f);
		mTorus.drawTorus(gl);
		gl.glTranslatef(0.0f, 0.0f, distance);
		mTorus.drawTorus(gl);
		gl.glTranslatef(0.0f, 0.0f, distance);
		mTorus.drawTorus(gl);
		gl.glTranslatef(0.0f, 0.0f, -distance * 3);
		mTorus.drawTorus(gl);
		gl.glPopMatrix();

		checkGLError(gl);*/

		OpenGLJNILib.onDrawFrame();
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		checkGLError(gl);
/*		gl.glViewport(0, 0, width, height);

		// make adjustments for screen ratio
		ratio = (float) width / height;
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glFrustumf(-ratio, ratio, -1, 1, 1, 10);
		checkGLError(gl);*/

		OpenGLJNILib.onSurfaceChanged(width, height);
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		checkGLError(gl);
/*		mTorus = new Torus(gl, 60, 60, 0.5f, 0.1f);
		mCylinder = new Cylinder(gl, 60, 60, 0.3f, 18.0f);

		// set the background frame color
		gl.glClearColor(0.0f, 0.0f, 1.0f, 1.0f);

		checkGLError(gl);*/

		OpenGLJNILib.onSurfaceCreated();
	}

	public static void checkGLError(GL gl) {
		int error = ((GL10) gl).glGetError();
		if (error != GL10.GL_NO_ERROR) {
			throw new RuntimeException("GLError 0x" + Integer.toHexString(error));
		}
	} 
	
	class TorusInfo {
		private float coordX;
		private float coordY;
		private float[] coordColor = new float[4];
		
		public TorusInfo(float[] xy) {
			if (xy.length != 2) {
				throw new RuntimeException("TorusInfo need exactly two elements: x and y");
			}
			
			coordX = xy[0];
			coordY = xy[1];
		}
		
		public TorusInfo(float x, float y) {
			coordX = x;
			coordY = y;
		}
		
		public TorusInfo(float x, float y, float[] color) {
			coordX = x;
			coordY = y;
			coordColor = color;
		}
	}
	
}
