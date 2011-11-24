#include<stdio.h>
#include<math.h>
#include<GL/glut.h>
#include "com_ecnu_sei_manuzhang_nim_GameViewRenderer.h"

const float PI = 3.1415926;

struct Vertex
{
	float x, y, z;  // vertex
	float nx, ny, nz; // normal
};


int mVertexSize = sizeof (struct Vertex);
int mIndexSize = sizeof (unsigned int);
int mNormalSize = sizeof (float);
int mNormalOffset = 3 * mNormalSize;

int mVertexBufferObjectId;
int mIndexBufferObjectId;

// These buffers are used to hold the vertex and index data while
// constructing the grid. Once createBufferObjects() is called
// the buffers are nulled out to save memory.

struct Vertex* mVertexBuffer;
unsigned short* mIndexBuffer;

int mW;
int mH;
int mIndexCount;
int mVertexCount;

void initGrid(int w, int h)
{
	if (w < 0 || w >= 65536)
	{
		perror("w");
	}
	if (h < 0 || h >= 65536)
	{
		perror("h");
	}
	if (w * h >= 65536)
	{
		perror("w * h >= 65536");
	}

	mW = w;
	mH = h;
	int vertexCount = w * h;

	mVertexBuffer = malloc(vertexCount * mVertexSize);

	int quadW = mW - 1;
	int quadH = mH - 1;
	int quadCount = quadW * quadH;
	mIndexCount = quadCount * 6; // one mesh is two triangles of three vertices
	mIndexBuffer = malloc(mIndexCount * mIndexSize);

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

	int i = 0, y, x;
	for (y = 0; y < quadH; y++)
	{
		for (x = 0; x < quadW; x++)
		{
			unsigned short a = (unsigned short) (y * mW + x);
			unsigned short b = (unsigned short) (y * mW + x + 1);
			unsigned short c = (unsigned short) ((y + 1) * mW + x);
			unsigned short d = (unsigned short) ((y + 1) * mW + x + 1);

			mIndexBuffer[i++] = a;
			mIndexBuffer[i++] = c;
			mIndexBuffer[i++] = b;

			mIndexBuffer[i++] = b;
			mIndexBuffer[i++] = c;
			mIndexBuffer[i++] = d;
		}
	}
}

void set(int i, int j, float x, float y, float z, float nx, float ny, float nz)
{
	if (i < 0 || i >= mW) {
		perror("i");
	}
	if (j < 0 || j >= mH) {
		perror("j");
	}

	int index = mW * j + i;

	mVertexBuffer[index].x = x;
	mVertexBuffer[index].y = y;
	mVertexBuffer[index].z = z;
	mVertexBuffer[index].nx = nx;
	mVertexBuffer[index].ny = ny;
	mVertexBuffer[index].nz = nz;
}

void createBufferObjects()
{
	// Generate vertex and element buffer IDs
	int[] vboIds = new int[2];
	glGenBuffers(2, vboIds);
	mVertexBufferObjectId = vboIds[0];
	mIndexBufferObjectId = vboIds[1];

	// Upload the vertex data
	glBindBuffer(GL_ARRAY_BUFFER, mVertexBufferObjectId);
	glBufferData(GL_ARRAY_BUFFER, mVertexCount * mVertexSize, mVertexBuffer, GL_STATIC_DRAW);

	// Upload the index data
	glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, mIndexBufferObjectId);
	glBufferData(GL11.GL_ELEMENT_ARRAY_BUFFER, mIndexCount * mIndexSize, mIndexBuffer, GL_STATIC_DRAW);

	// We don't need the in-memory data any more
	free(mVertexBuffer);
	free(mIndexBuffer);
}

void draw()
{
	glEnableClientState(GL_VERTEX_ARRAY);
	glBindBuffer(GL_ARRAY_BUFFER, mVertexBufferObjectId);
	glVertexPointer(3, GL_FLOAT, mVertexSize, 0);

	glEnableClientState(GL_NORMAL_ARRAY);
	glNormalPointer(GL_FLOAT, mVertexSize, mNormalOffset);

	glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, mIndexBufferObjectId);

	glDrawElements(GL_TRIANGLES, mIndexCount, GL_UNSIGNED_SHORT, mIndexBuffer);

	glDisableClientState(GL_VERTEX_ARRAY);
	glDisableClientState(GL_NORMAL_ARRAY);
	glBindBuffer(GL_ARRAY_BUFFER, 0);
	glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
}

// x(u,v) = (R + r * cos(u)) * cos(v)
// y(u,v) = (R + r * cos(u)) * sin(v)
// z(u,v) = r * sin(u)
// r is the radius of the circle
// R is the distance from the center of torus to the center of the circle
void generateTorus(int uSteps, int vSteps, float majorRadius, float minorRadius)
{
	int i, j;
	initGrid(uSteps + 1, vSteps + 1);
	for (j = 0; j <= vSteps; j++) {
		double angleV = PI * 2 * j / vSteps;
		float cosV = (float) cos(angleV);
		float sinV = (float) sin(angleV);
		for (i = 0; i <= uSteps; i++) {
			double angleU = PI * 2 * i / uSteps;
			float cosU = (float) cos(angleU);
			float sinU = (float) sin(angleU);
			float d = majorRadius + minorRadius * cosU;
			float x = d * cosV;
			float y = d * (-sinV);
			float z = minorRadius * sinU;

			float nx = cosV * cosU;
			float ny = -sinV * cosU;
			float nz = sinU;

			float length = (float) sqrt(nx * nx + ny * ny + nz * nz);
			nx /= length;
			ny /= length;
			nz /= length;

			set(i, j, x, y, z, nx, ny, nz);
		}
	}
	createBufferObjects());
}


// x(u,v) = r * cos(v)
// y(u,v) = r * sin(V)
// z(u,v) = r * tan(u)
void generateCylinder(int uSteps, int vSteps, float radius, float height)
{
	int j, i;
	initGrid(uSteps + 1, vSteps + 1);
	for (j = 0; j <= vSteps; j++) {
		double angleV = PI * 2 * j / vSteps;
		float cosV = (float) cos(angleV);
		float sinV = (float) sin(angleV);
		for (i = 0; i <= uSteps; i++) {
			double angleU = PI * 2 * i / uSteps;
			float cosU = (float) cos(angleU);
			float sinU = (float) sin(angleU);
			float tanU = (float) tan(angleU);

			float d = radius;
			float z = d * tanU;

			if (z >= -height / 2 && z <= height / 2)
			{
				float x = d * cosV;
				float y = d * (-sinV);


				float nx = cosV * cosU;
				float ny = -sinV * cosU;
				float nz = sinU;

				float length = (float) sqrt(nx * nx + ny * ny + nz * nz);
				nx /= length;
				ny /= length;
				nz /= length;

				set(i, j, x, y, z, nx, ny, nz);
			}
		}
	}
	createBufferObjects();
}


/*
 * Class:     com_ecnu_sei_manuzhang_nim_GameViewRenderer
 * Method:    draw
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_ecnu_sei_manuzhang_nim_GameViewRenderer_draw
  (JNIEnv* env, jclass obj)
{
	draw();
}

/*
 * Class:     com_ecnu_sei_manuzhang_nim_GameViewRenderer
 * Method:    generateTorus
 * Signature: (IIFF)V
 */
JNIEXPORT void JNICALL Java_com_ecnu_sei_manuzhang_nim_GameViewRenderer_generateTorus
  (JNIEnv* env, jclass obj, jint u, jint v, jfloat major, jfloat minor)
{
	generateTorus(u, v, major, minor);
}

/*
 * Class:     com_ecnu_sei_manuzhang_nim_GameViewRenderer
 * Method:    generateCylinder
 * Signature: (IIFF)V
 */
JNIEXPORT void JNICALL Java_com_ecnu_sei_manuzhang_nim_GameViewRenderer_generateCylinder
  (JNIEnv* env, jclass obj, jint u, jint v, jfloat major, jfloat minor)
{
	generateCylinder(u, v, major, minor);
}
