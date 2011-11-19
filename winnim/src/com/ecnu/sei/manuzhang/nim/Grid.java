package com.ecnu.sei.manuzhang.nim;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

	/** A grid is a topologically rectangular array of vertices.
	 *
	 * This grid class is customized for the vertex data required for this
	 * example.
	 *
	 * The vertex and index data are held in VBO objects because on most
	 * GPUs VBO objects are the fastest way of rendering static vertex
	 * and index data.
	 *
	 */

public class Grid {

	protected int mVertexBufferObjectId;
	protected int mIndexBufferObjectId;
	
	// These buffers are used to hold the vertex and index data while
	// constructing the grid. Once createBufferObjects() is called
	// the buffers are nulled out to save memory.

	protected FloatBuffer mVertexBuffer;
	protected ShortBuffer mIndexBuffer;

	protected int mW;
	protected int mH;
	protected int mIndexCount;

	protected void initGrid(int w, int h) {
		if (w < 0 || w >= 65536) {
			throw new IllegalArgumentException("w");
		}
		if (h < 0 || h >= 65536) {
			throw new IllegalArgumentException("h");
		}
		if (w * h >= 65536) {
			throw new IllegalArgumentException("w * h >= 65536");
		}

		mW = w;
		mH = h;
		int vertexCount = w * h;

		mVertexBuffer = ByteBuffer.allocateDirect(Size.VERTEX_SIZE * vertexCount)
				.order(ByteOrder.nativeOrder()).asFloatBuffer();

		int quadW = mW - 1;
		int quadH = mH - 1;
		int quadCount = quadW * quadH;
		int indexCount = quadCount * 6; // one mesh is two triangles of three vertices
		mIndexCount = indexCount;
		mIndexBuffer = ByteBuffer.allocateDirect(Size.INDEX_SIZE * indexCount)
				.order(ByteOrder.nativeOrder()).asShortBuffer();

		/*
		 * Initialize triangle list mesh.
		 *
		 *     [0]-----[  1] ...
		 *      |    /   |
		 *      |   /    |
		 *      |  /     |
		 *     [w]-----[w+1] ...
		 *      |       |
		 *
		 */

		{
			int i = 0;
			for (int y = 0; y < quadH; y++) {
				for (int x = 0; x < quadW; x++) {
					short a = (short) (y * mW + x);
					short b = (short) (y * mW + x + 1);
					short c = (short) ((y + 1) * mW + x);
					short d = (short) ((y + 1) * mW + x + 1);

					mIndexBuffer.put(i++, a);
					mIndexBuffer.put(i++, c);
					mIndexBuffer.put(i++, b);

					mIndexBuffer.put(i++, b);
					mIndexBuffer.put(i++, c);
					mIndexBuffer.put(i++, d);
				}
			}
		}
	}
	
	protected void set(int i, int j, float x, float y, float z, float nx, float ny, float nz) {
		if (i < 0 || i >= mW) {
			throw new IllegalArgumentException("i");
		}
		if (j < 0 || j >= mH) {
			throw new IllegalArgumentException("j");
		}

		int index = mW * j + i;

		mVertexBuffer.position(index * Size.VERTEX_SIZE / Size.FLOAT_SIZE);
		mVertexBuffer.put(x);
		mVertexBuffer.put(y);
		mVertexBuffer.put(z);
		mVertexBuffer.put(nx);
		mVertexBuffer.put(ny);
		mVertexBuffer.put(nz);
	}
	
	protected void createBufferObjects(GL10 gl) {
		GameViewRenderer.checkGLError(gl);
		// Generate vertex and element buffer IDs
		int[] vboIds = new int[2];
	    GL11 gl11 = (GL11) gl;
		gl11.glGenBuffers(2, vboIds, 0);
		mVertexBufferObjectId = vboIds[0];
		mIndexBufferObjectId = vboIds[1];

		// Upload the vertex data
		gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, mVertexBufferObjectId);
		mVertexBuffer.position(0);
		gl11.glBufferData(GL11.GL_ARRAY_BUFFER, mVertexBuffer.capacity() * Size.FLOAT_SIZE, mVertexBuffer, GL11.GL_STATIC_DRAW);

		// Upload the index data
		gl11.glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, mIndexBufferObjectId);
		mIndexBuffer.position(0);
		gl11.glBufferData(GL11.GL_ELEMENT_ARRAY_BUFFER, mIndexBuffer.capacity() * Size.SHORT_SIZE, mIndexBuffer, GL11.GL_STATIC_DRAW);

		// We don't need the in-memory data any more
		mVertexBuffer = null;
		mIndexBuffer = null;
		GameViewRenderer.checkGLError(gl);
	}

	protected void draw(GL10 gl) {
		GameViewRenderer.checkGLError(gl);
		GL11 gl11 = (GL11) gl;

		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, mVertexBufferObjectId);
		gl11.glVertexPointer(3, GL10.GL_FLOAT, Size.VERTEX_SIZE, 0);

		gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
		gl11.glNormalPointer(GL10.GL_FLOAT, Size.VERTEX_SIZE, Size.VERTEX_NORMAL_BUFFER_INDEX_OFFSET * Size.FLOAT_SIZE);

		gl11.glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, mIndexBufferObjectId);
		
		gl11.glDrawElements(GL10.GL_TRIANGLES, mIndexCount, GL10.GL_UNSIGNED_SHORT, 0);
		
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
		gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, 0);
		gl11.glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, 0);
		GameViewRenderer.checkGLError(gl);
	}
}
