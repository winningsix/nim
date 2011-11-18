package com.ecnu.sei.manuzhang.nim;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLSurfaceView;
import android.opengl.GLU;

public class GameViewRenderer implements GLSurfaceView.Renderer {
	private Torus torus;
	@Override
	public void onDrawFrame(GL10 gl) {
		checkGLError(gl);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();

		GLU.gluLookAt(gl, 0f, 0f, -5.0f, 0f, 0f, 0f, 0f, 1.0f, 0f);
		gl.glRotatef(45.0f, 1.0f, 0.0f, 0.0f);

		checkGLError(gl);

		torus.drawTorus(gl);

		checkGLError(gl);
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		checkGLError(gl);
		gl.glViewport(0, 0, width, height);

		// make adjustments for screen ratio
		float ratio = (float) width / height;
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glFrustumf(-ratio, ratio, -1, 1, 1, 10);
		checkGLError(gl);
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		checkGLError(gl);
		torus = new Torus(gl, 60, 60, 1.0f, 0.25f);

		// set the background frame color
		gl.glClearColor(0.0f, 0.0f, 1.0f, 1.0f);

		checkGLError(gl);
	}

	public static void checkGLError(GL gl) {
		int error = ((GL10) gl).glGetError();
		if (error != GL10.GL_NO_ERROR) {
			throw new RuntimeException("GLError 0x" + Integer.toHexString(error));
		}
	} 
}
