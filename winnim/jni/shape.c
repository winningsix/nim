#include "com_ecnu_sei_manuzhang_nim_OpenGLJNILib.h"
#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <GLES/gl.h>

#define BUFFER_OFFSET(offset) ((GLvoid *) NULL + offset)
#define TorusVertex    0
#define TorusIndex     1
#define CylinderVertex 2
#define CylinderIndex  3
#define BUFFER_NUMS    4

#define IndexType      GLushort

GLfloat ratio;
GLfloat distance = 1.0f;
const GLfloat PI = 3.1415926;

GLint mW;
GLint mH;
GLint mIndexCount;
GLint mVertexCount;

struct Vertex
{
	GLfloat x, y, z;  // vertex
	GLfloat nx, ny, nz; // normal
};

const GLint mVertexSize = sizeof (struct Vertex);
const GLint mIndexSize = sizeof (IndexType);
const GLint mNormalSize = sizeof (GLfloat);

GLuint buffer[BUFFER_NUMS];

// These buffers are used to hold the vertex and index data while
// constructing the grid. Once createBufferObjects() is called
// the buffers are nulled out to save memory.

struct Vertex* mVertexBuffer;
IndexType* mIndexBuffer;

void initGrid(GLint w, GLint h)
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
	GLint vertexCount = w * h;
	mVertexCount = vertexCount;
	mVertexBuffer = (struct Vertex*) malloc(mVertexCount * mVertexSize);

	GLint quadW = mW - 1;
	GLint quadH = mH - 1;
	GLint quadCount = quadW * quadH;
	mIndexCount = quadCount * 6; // one mesh is two triangles of three vertices
	mIndexBuffer = (IndexType*) malloc(mIndexCount * mIndexSize);

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

	GLint i = 0, y, x;
	for (y = 0; y < quadH; y++)
		for (x = 0; x < quadW; x++)
		{
			IndexType a = (IndexType) (y * mW + x);
			IndexType b = (IndexType) (y * mW + x + 1);
			IndexType c = (IndexType) ((y + 1) * mW + x);
			IndexType d = (IndexType) ((y + 1) * mW + x + 1);

			mIndexBuffer[i++] = a;
			mIndexBuffer[i++] = c;
			mIndexBuffer[i++] = b;

			mIndexBuffer[i++] = b;
			mIndexBuffer[i++] = c;
			mIndexBuffer[i++] = d;
		}
}


void set(GLint i, GLint j, GLfloat x, GLfloat y, GLfloat z, GLfloat nx, GLfloat ny, GLfloat nz)
{
	if (i < 0 || i >= mW)
	{
		perror("i");
	}
	if (j < 0 || j >= mH)
	{
		perror("j");
	}

	GLint index = mW * j + i;

	mVertexBuffer[index].x = x;
	mVertexBuffer[index].y = y;
	mVertexBuffer[index].z = z;
	mVertexBuffer[index].nx = nx;
	mVertexBuffer[index].ny = ny;
	mVertexBuffer[index].nz = nz;
}

void createBufferObjects(GLuint* mVertexBufferObjectId, GLuint* mIndexBufferObjectId)
{
	// Generate vertex and element buffer IDs
	GLuint vboIds[2];
	glGenBuffers(2, vboIds);
	*mVertexBufferObjectId = vboIds[0];
	*mIndexBufferObjectId = vboIds[1];

	// Upload the vertex data
	glBindBuffer(GL_ARRAY_BUFFER, *mVertexBufferObjectId);
	glBufferData(GL_ARRAY_BUFFER, mVertexSize * mVertexCount, mVertexBuffer, GL_STATIC_DRAW);

	// Upload the index data
	glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, *mIndexBufferObjectId);
	glBufferData(GL_ELEMENT_ARRAY_BUFFER, mIndexSize * mIndexCount, mIndexBuffer, GL_STATIC_DRAW);

	// We don't need the in-memory data any more
	free(mVertexBuffer);
	free(mIndexBuffer);
}

// x(u,v) = (R + r * cos(u)) * cos(v)
// y(u,v) = (R + r * cos(u)) * sin(v)
// z(u,v) = r * sin(u)
// r is the radius of the circle
// R is the distance from the center of torus to the center of the circle
void generateTorus(GLint uSteps, GLint vSteps, GLfloat majorRadius, GLfloat minorRadius)
{
	GLint i, j;
	initGrid(uSteps + 1, vSteps + 1);
	for (j = 0; j <= vSteps; j++)
	{
		double angleV = PI * 2 * j / (vSteps - 2);
		GLfloat cosV = (GLfloat) cos(angleV);
		GLfloat sinV = (GLfloat) sin(angleV);
		for (i = 0; i <= uSteps; i++)
		{
			double angleU = PI * 2 * i / (uSteps - 2);
			GLfloat cosU = (GLfloat) cos(angleU);
			GLfloat sinU = (GLfloat) sin(angleU);
			GLfloat d = majorRadius + minorRadius * cosU;
			GLfloat x = d * cosV;
			GLfloat y = d * (-sinV);
			GLfloat z = minorRadius * sinU;

			GLfloat nx = cosV * cosU;
			GLfloat ny = -sinV * cosU;
			GLfloat nz = sinU;

			GLfloat length = (GLfloat) sqrt(nx * nx + ny * ny + nz * nz);
			nx /= length;
			ny /= length;
			nz /= length;

			set(i, j, x, y, z, nx, ny, nz);
		}
	}
	createBufferObjects(buffer + TorusVertex, buffer + TorusIndex);
}


// x(u,v) = r * cos(v)
// y(u,v) = r * sin(V)
// z(u,v) = r * tan(u)
void generateCylinder(GLint uSteps, GLint vSteps, GLfloat radius, GLfloat height)
{
	GLint j, i;
	initGrid(uSteps + 1, vSteps + 1);
	for (j = 0; j <= vSteps; j++)
	{
		double angleV = PI * 2 * j / vSteps;
		GLfloat cosV = (GLfloat) cos(angleV);
		GLfloat sinV = (GLfloat) sin(angleV);
		for (i = 0; i <= uSteps; i++)
		{
			double angleU = PI * 2 * i / uSteps;
			GLfloat cosU = (GLfloat) cos(angleU);
			GLfloat sinU = (GLfloat) sin(angleU);
			GLfloat tanU = (GLfloat) tan(angleU);

			GLfloat d = radius;
			GLfloat z = d * tanU;

			if (z >= -height / 2 && z <= height / 2)
			{
				GLfloat x = d * cosV;
				GLfloat y = d * (-sinV);


				GLfloat nx = cosV * cosU;
				GLfloat ny = -sinV * cosU;
				GLfloat nz = sinU;

				GLfloat length = (GLfloat) sqrt(nx * nx + ny * ny + nz * nz);
				nx /= length;
				ny /= length;
				nz /= length;

				set(i, j, x, y, z, nx, ny, nz);
			}
		}
	}

	createBufferObjects(buffer + CylinderVertex, buffer + CylinderIndex);
}

void draw(GLuint mVertexBufferObjectId, GLuint mIndexBufferObjectId)
{

	glBindBuffer(GL_ARRAY_BUFFER, mVertexBufferObjectId);
	glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, mIndexBufferObjectId);

	glEnableClientState(GL_VERTEX_ARRAY);
	glEnableClientState(GL_NORMAL_ARRAY);

	glVertexPointer(3, GL_FLOAT, mVertexSize, BUFFER_OFFSET(0));
	glNormalPointer(GL_FLOAT, mVertexSize, BUFFER_OFFSET(3));
	glDrawElements(GL_TRIANGLES, mIndexCount, GL_UNSIGNED_SHORT, BUFFER_OFFSET(0));

	glDisableClientState(GL_VERTEX_ARRAY);
	glDisableClientState(GL_NORMAL_ARRAY);
	glBindBuffer(GL_ARRAY_BUFFER, mVertexBufferObjectId);
	glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, mIndexBufferObjectId);

}

void drawTorus()
{
	draw(buffer[TorusVertex], buffer[TorusIndex]);
}

void drawCylinder()
{
	draw(buffer[CylinderVertex], buffer[CylinderIndex]);
}

void onDrawFrame()
{
	glClear (GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	glEnable(GL_DEPTH_TEST);
	glMatrixMode(GL_MODELVIEW);
	glLoadIdentity();

	glTranslatef(0.0f, 0.0f, -5.0f);
	glRotatef(-85.0f, 1.0f, 0.0f, 0.0f);

	// left cylinder with 5 Toruses
	glPushMatrix();
	glTranslatef(-2.5 * ratio, 0.0f, 0.0f);
	glColor4f(1.0f, 0.0f, 0.5f, 1.0f);

	drawCylinder();
	glColor4f(1.0f, 1.0f, 0.0f, 0.5f);
	drawTorus();
	glTranslatef(0.0f, 0.0f, distance);
	drawTorus();
	glTranslatef(0.0f, 0.0f, distance);
	drawTorus();
	glTranslatef(0.0f, 0.0f, -distance * 3);
	drawTorus();
	glTranslatef(0.0f, 0.0f, -distance);
	drawTorus();
	glPopMatrix();

	// middle with 2 toruses
	glPushMatrix();
	glColor4f(1.0f, 0.0f, 0.5f, 1.0f);
	drawCylinder();
	glColor4f(1.0f, 1.0f, 0.0f, 0.5f);
	glTranslatef(0.0f, 0.0f, distance);
	drawTorus();
	glTranslatef(0.0f, 0.0f, -distance * 2);
	drawTorus();
	glPopMatrix();

	// right with 4 torus
	glPushMatrix();
	glTranslatef(2.5 * ratio, 0.0f, 0.0f);
	glColor4f(1.0f, 0.0f, 0.5f, 1.0f);
	drawCylinder();
	glColor4f(1.0f, 1.0f, 0.0f, 0.5f);
	drawTorus();
	glTranslatef(0.0f, 0.0f, distance);
	drawTorus();
	glTranslatef(0.0f, 0.0f, distance);
	drawTorus();
	glTranslatef(0.0f, 0.0f, -distance * 3);
	drawTorus();
	glPopMatrix();
	glFlush();
}

void onSurfaceChanged(int width, int height)
{
	glViewport(0, 0, width, height);

	// make adjustments for screen ratio
	ratio = (float) width / height;
	glMatrixMode(GL_PROJECTION);
	glLoadIdentity();
	glFrustumf(-ratio, ratio, -1, 1, 1, 10);
}


void onSurfaceCreated()
{
	generateTorus(80, 80, 0.5f, 0.1f);
	generateCylinder(80, 80, 0.3f, 18.0f);

	// set the background frame color
	glClearColor(0.0f, 0.0f, 1.0f, 1.0f);
}

/*
 * Class:     com_ecnu_sei_manuzhang_nim_OpenGLJNILib
 * Method:    onDrawFrame
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_ecnu_sei_manuzhang_nim_OpenGLJNILib_onDrawFrame
(JNIEnv* env, jclass obj)
{
	onDrawFrame();
}

/*
 * Class:     com_ecnu_sei_manuzhang_nim_OpenGLJNILib
 * Method:    onSurfaceChanged
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_com_ecnu_sei_manuzhang_nim_OpenGLJNILib_onSurfaceChanged
(JNIEnv* env, jclass obj, jint width, jint height)
{
	onSurfaceChanged(width, height);
}

/*
 * Class:     com_ecnu_sei_manuzhang_nim_OpenGLJNILib
 * Method:    onSurfaceCreated
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_ecnu_sei_manuzhang_nim_OpenGLJNILib_onSurfaceCreated
(JNIEnv* env, jclass obj)
{
	onSurfaceCreated();
}


