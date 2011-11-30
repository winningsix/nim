package com.ecnu.sei.manuzhang.nim;

import javax.microedition.khronos.opengles.GL10;

public class Torus extends Grid {
	
	public Torus(GL10 gl, int uSteps, int vSteps, float majorRadius, float minorRadius) {
	    generateTorus(gl, uSteps, vSteps, majorRadius, minorRadius);
	}

	public void drawTorus(GL10 gl, int num, float stride, int selected) {
		if (num == 0)
			return;
		if (selected > num)
			selected = 0;
		
		gl.glTranslatef(0.0f, 0.0f, stride * num / 2);
		for (int i = 0; i < num; i++) {
			if (i < selected)
				gl.glColor4f(0.5f, 0.5f, 0.5f, 0.8f);
			else
				gl.glColor4f(1.0f, 1.0f, 0.0f, 0.8f);
			draw(gl);
			gl.glTranslatef(0.0f, 0.0f, -stride);
		}
	}

	// x(u,v) = (R + r * cos(u)) * cos(v)
	// y(u,v) = (R + r * cos(u)) * sin(v)
	// z(u,v) = r * sin(u)
	// r is the radius of the circle
	// R is the distance from the center of torus to the center of the circle
	private void generateTorus(GL10 gl, int uSteps, int vSteps, float majorRadius, float minorRadius) {
		super.initGrid(uSteps + 1, vSteps + 1);
		for (int j = 0; j <= vSteps; j++) {
			double angleV = Math.PI * 2 * j / vSteps;
			float cosV = (float) Math.cos(angleV);
			float sinV = (float) Math.sin(angleV);
			for (int i = 0; i <= uSteps; i++) {
				double angleU = Math.PI * 2 * i / uSteps;
				float cosU = (float) Math.cos(angleU);
				float sinU = (float) Math.sin(angleU);
				float d = majorRadius + minorRadius * cosU;
				float x = d * cosV;
				float y = d * (-sinV);
				float z = minorRadius * sinU;

				float nx = cosV * cosU;
				float ny = -sinV * cosU;
				float nz = sinU;

				float length = (float) Math.sqrt(nx * nx + ny * ny + nz * nz);
				nx /= length;
				ny /= length;
				nz /= length;

				super.set(i, j, x, y, z, nx, ny, nz);
			}
		}
		super.createBufferObjects(gl);
	}
}