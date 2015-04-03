/* routines for manipulating and multiplying 4X4 floating point matrices
   for use in graphics.  by Matthew Ward.  Need to include "matrix_ops.h".  
   Use at your own risk! */

#include <math.h>

/* output a label and 4 by 4 matrix */

void printmat(float mat[4][4], char *str)
{
	int i,j;
	printf("matrix %s\n", str);
	for(i = 0;i < 4;i++)	{
		for(j = 0;j < 4;j++)
			printf("%f\t",mat[i][j]);
		printf("\n");
		}
	}

/* copy one matrix into another */

void copymat(float mat[4][4], float mat2[4][4])
{
	int i,j;
	for(i = 0;i < 4;i++)
		for(j = 0;j < 4;j++)
			mat2[i][j] = mat[i][j];
	}

/* initialize a matrix to the identity matrix */

void initmat(float mat[4][4])
{
	int i,j;
	for(i = 0;i < 4;i++)
		for(j = 0;j < 4;j++)
			if(i == j)
				mat[i][j] = 1;
			else
				mat[i][j] = 0;
	}

/* multiply first matrix by second giving third.  Note first matrix
   is left of second */

void mult4by4(float m1[4][4], float m2[4][4], float mr[4][4])
{
	int i,j,k;
	float tmp;
	for(i = 0;i < 4;i++)
		for(j = 0;j < 4;j++)	{
			tmp = 0.;
			for(k = 0;k < 4;k++)
				tmp+= m1[i][k] * m2[k][j];
			mr[i][j] = tmp;
			}
		}

/* multiply a vector times a matrix giving a new vector */
/* Note: should probably rename as pointmult for clarity */

void vecmult(float m1[4][4], float v1[], float vr[])
{
	int i, j;
	float tmp;
	for(i = 0;i < 4;i++)	{
		tmp = 0.;
		for(j = 0;j < 4;j++)
			tmp += m1[j][i] * v1[j];
		vr[i] = tmp;
		}
	}

/* routines to perform transformations on 3-D floating point matrices.
   by Matthew Ward.  Use at your own risk.  All routines are destructive,
   i.e., they change the input array.  */


/* perform scaling on matrix by creating a scaling matrix and multiplying
   by matrix passed */

void scalemat(float mat[4][4], float sx, float sy, float sz)
{
	float tmp1[4][4], tmp2[4][4];
	int i,j;
/* make copy of original */
	copymat(mat, tmp1);
/* create the scaling matrix and multiply into the original */
	initmat(tmp2);
	tmp2[0][0] = sx;
	tmp2[1][1] = sy;
	tmp2[2][2] = sz;
	mult4by4(tmp1, tmp2, mat);
	}

/* perform translation by specified amount following above steps */

void transmat(float mat[4][4], float dx, float dy, float dz)
{
	float tmp1[4][4], tmp2[4][4];
	int i,j;
	copymat(mat, tmp1);
	initmat(tmp2);
	tmp2[3][0] = dx;
	tmp2[3][1] = dy;
	tmp2[3][2] = dz;
	mult4by4(tmp1, tmp2, mat);
	}

/* perform rotation about x axis by supplied cosine and sine (not
   given as angle!) following above steps */

void xrotmat(float mat[4][4], float cosangle, float sinangle)
{
	float tmp1[4][4], tmp2[4][4];
	int i,j;
	copymat(mat, tmp1);
	initmat(tmp2);
	tmp2[1][1] = tmp2[2][2] = cosangle;
	tmp2[1][2] = sinangle;
	tmp2[2][1] = -sinangle;
	mult4by4(tmp1, tmp2, mat);
	}

/* perform rotation about y axis by supplied cosine and sine (not
   given as angle!) following above steps */

void yrotmat(float mat[4][4], float cosangle, float sinangle)
{
	float tmp1[4][4], tmp2[4][4];
	int i,j;
	copymat(mat, tmp1);
	initmat(tmp2);
	tmp2[0][0] = tmp2[2][2] = cosangle;
	tmp2[2][0] = sinangle;
	tmp2[0][2] = -sinangle;
	mult4by4(tmp1, tmp2, mat);
	}

/* perform rotation about z axis by supplied cosine and sine (not
   given as angle!) following above steps */

void zrotmat(float mat[4][4], float cosangle, float sinangle)
{
	float tmp1[4][4], tmp2[4][4];
	int i,j;
	copymat(mat, tmp1);
	initmat(tmp2);
	tmp2[1][1] = tmp2[0][0] = cosangle;
	tmp2[0][1] = sinangle;
	tmp2[1][0] = -sinangle;
	mult4by4(tmp1, tmp2, mat);
	}

/* routines for manipulating 3-D floating point vectors.  
   By Matthew Ward.  Use at your own risk! */

/* compute dot product of 2 3-D vectors */

float dotprod(float a[], float b[])
{
	float res;
	res = a[0]*b[0] + a[1]*b[1] + a[2]*b[2];
	return(res);
	}

/* compute cross product of 2 3-D vectors and store in third */

void cross(float a[], float b[], float c[])
{
	c[0] = a[1]*b[2] - a[2]*b[1];
	c[1] = a[2]*b[0] - a[0]*b[2];
	c[2] = a[0]*b[1] - a[1]*b[0];
	}

/* compute magnitude of a 3-D vector */
	
float getlen(float a[])
{
	double tmp;
	tmp = a[0]*a[0] + a[1]*a[1] + a[2]*a[2];
	return(sqrt(tmp));
	}

/* print a label and a vector */

void printvec(float vec[], char *str)
{
	int i;
	printf("vector %s\n", str);
	for(i = 0;i < 3;i++)	{
			printf("%f\t",vec[i]);
		printf("\n");
		}
	}

