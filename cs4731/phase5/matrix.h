/* header file for matrix_ops.c, utilities for matrix and vector operations,
   written by Matthew Ward.  Use at your own risk!
   */

/* 4 by 4 matrix stuff */

void initmat(float [4][4]);		/* create identity matrix */
void copymat(float [4][4], float [4][4]);
void mult4by4(float [4][4], float [4][4], float [4][4]);
void printmat(float [4][4], char *);	/* prints array and message */

/* 3-D vector stuff */

float dotprod(float [], float []);	
void cross(float [], float [], float []);
float getlen(float []);	
void vecmult(float [4][4], float [], float []);
void printvec(float [], char *);

/* affine homogeneous (4 by 4) transformations */
/* note: pass in cosine and sine for rotations*/

void scalemat(float [4][4], float, float, float);
void transmat(float [4][4], float, float, float);
void xrotmat(float [4][4], float, float);	
void yrotmat(float [4][4], float, float);
void zrotmat(float [4][4], float, float);

