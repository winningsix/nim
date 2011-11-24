#include "com_ecnu_sei_manuzhang_nim_OpenGLJNILib.h"
#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <GLES/gl.h>

#define BUFFER_OFFSET(offset) ((GLvoid *) NULL + offset)

GLfloat ratio;
GLfloat distance = 1.0f;
const GLfloat PI = 3.1415926;

struct Vertex
{
  GLfloat x, y, z;  // vertex
  GLfloat nx, ny, nz; // normal
};


const int mVertexSize = sizeof (struct Vertex);
const int mIndexSize = sizeof (GLushort);
const int mNormalSize = sizeof (GLfloat);

int mTorusVertexId;
int mTorusIndexId;
int mCylinderVertexId;
int mCylinderIndexId;

// These buffers are used to hold the vertex and index data while
// constructing the grid. Once createBufferObjects() is called
// the buffers are nulled out to save memory.

struct Vertex* mVertexBuffer;
GLushort* mIndexBuffer;

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

  mVertexBuffer = (struct Vertex*) malloc(vertexCount * mVertexSize);

  int quadW = mW - 1;
  int quadH = mH - 1;
  int quadCount = quadW * quadH;
  mIndexCount = quadCount * 6; // one mesh is two triangles of three vertices
  mIndexBuffer = (GLushort*) malloc(mIndexCount * mIndexSize);

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
	  GLushort a = (GLushort) (y * mW + x);
	  GLushort b = (GLushort) (y * mW + x + 1);
	  GLushort c = (GLushort) ((y + 1) * mW + x);
	  GLushort d = (GLushort) ((y + 1) * mW + x + 1);

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
  if (i < 0 || i >= mW)
  {
    perror("i");
  }
  if (j < 0 || j >= mH)
  {
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

void createBufferObjects(int* mVertexBufferObjectId, int* mIndexBufferObjectId)
{
  // Generate vertex and element buffer IDs
  int vboIds[2];
  glGenBuffers(2, vboIds);
  *mVertexBufferObjectId = vboIds[0];
  *mIndexBufferObjectId = vboIds[1];

  // Upload the vertex data
  glBindBuffer(GL_ARRAY_BUFFER, *mVertexBufferObjectId);
  glBufferData(GL_ARRAY_BUFFER, mVertexCount * mVertexSize, mVertexBuffer, GL_STATIC_DRAW);

  // Upload the index data
  glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, *mIndexBufferObjectId);
  glBufferData(GL_ELEMENT_ARRAY_BUFFER, mIndexCount * mIndexSize, mIndexBuffer, GL_STATIC_DRAW);

  // We don't need the in-memory data any more
  free(mVertexBuffer);
  free(mIndexBuffer);
}

void draw(int mVertexBufferObjectId, int mIndexBufferObjectId)
{
  glEnableClientState(GL_VERTEX_ARRAY);
  glBindBuffer(GL_ARRAY_BUFFER, mVertexBufferObjectId);
  glVertexPointer(3, GL_FLOAT, mVertexSize, BUFFER_OFFSET(0));

  glEnableClientState(GL_NORMAL_ARRAY);
  glNormalPointer(GL_FLOAT, mVertexSize, BUFFER_OFFSET(3));

  glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, mIndexBufferObjectId);

  glDrawElements(GL_TRIANGLES, mIndexCount, GL_UNSIGNED_SHORT, BUFFER_OFFSET(0));

  glDisableClientState(GL_VERTEX_ARRAY);
  glDisableClientState(GL_NORMAL_ARRAY);
  glBindBuffer(GL_ARRAY_BUFFER, 0);
  glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
}

void drawTorus()
{
  draw(mTorusVertexId, mTorusIndexId);
}

void drawCylinder()
{
  draw(mCylinderVertexId, mCylinderIndexId);
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
  createBufferObjects(&mTorusVertexId, &mTorusIndexId);
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
  createBufferObjects(&mCylinderVertexId, &mCylinderIndexId);
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
  glTranslatef(-3.0f, 0.0f, 0.0f);
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
  glTranslatef(3.0f, 0.0f, 0.0f);
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
	generateTorus(60, 60, 0.5f, 0.1f);
	generateCylinder(60, 60, 0.3f, 18.0f);

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


