package com.ecnu.sei.manuzhang.nim;

import javax.microedition.khronos.opengles.GL10;

public class Cylinder extends Grid {

	public Cylinder(GL10 gl, int uSteps, int vSteps, float radius, float height) {
		generateCylinder(gl, uSteps, vSteps, radius, height);
	}

	public void drawCylinder(GL10 gl) {
		draw(gl);
	}

	// x(u,v) = r * cos(v)
	// y(u,v) = r * sin(V)
	// z(u,v) = r * tan(u)
	private void generateCylinder(GL10 gl, int uSteps, int vSteps, float radius, float height) {
		super.initGrid(uSteps + 1, vSteps + 1);
		for (int j = 0; j <= vSteps; j++) {
			double angleV = Math.PI * 2 * j / vSteps;
			float cosV = (float) Math.cos(angleV);
			float sinV = (float) Math.sin(angleV);
			for (int i = 0; i <= uSteps; i++) {
				double angleU = Math.PI * 2 * i / uSteps;
				float cosU = (float) Math.cos(angleU);
				float sinU = (float) Math.sin(angleU);
				float tanU = (float) Math.tan(angleU);

				float d = radius;
				float z = d * tanU;

				if (z >= -height / 2 && z <= height / 2)
				{
					float x = d * cosV;
					float y = d * (-sinV);


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
		}
		super.createBufferObjects(gl);
	}
}
