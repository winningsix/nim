package com.ecnu.sei.manuzhang.nim;

public class Size {
	// Size of vertex data elements in bytes:
	final static int FLOAT_SIZE = 4;
	final static int INT_SIZE = 4;
    final static int SHORT_SIZE = 2;
    
	// Vertex structure:
	// float x, y, z;
	// float nx, ny, nx;
	final static int VERTEX_SIZE = 6 * FLOAT_SIZE;

	final static int INDEX_SIZE = SHORT_SIZE;
	final static int VERTEX_NORMAL_BUFFER_INDEX_OFFSET = 3;
}
