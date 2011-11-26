package com.ecnu.sei.manuzhang.nim;

public class OpenGLJNILib {

	// Native implementation
	static {
		System.loadLibrary("shape");
	}

	public native static void onDrawFrame(int num_1, int num_2, int num_3);
	public native static void onSurfaceChanged(int width, int height);
	public native static void onSurfaceCreated();

}
