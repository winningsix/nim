package com.ecnu.sei.manuzhang.nim;

public class OpenGLJNILib {

	// Native implementation
	static {
		System.loadLibrary("shape");
	}

	public native static void onDrawFrame();
	public native static void onSurfaceChanged(int width, int height);
	public native static void onSurfaceCreated();

}
