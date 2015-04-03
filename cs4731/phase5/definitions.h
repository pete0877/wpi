///////////////////////////////////////////////////////////////////////
// Class Definitions for Modeler and Renderer version 1.0
// Peter Golaszewski
// CS4831 C98
///////////////////////////////////////////////////////////////////////

///////////////////////////////////////////////////////////////////////
// Libraries:

#include <iostream.h>
#include <stdio.h>
#include <string.h>
#include "libsx.h"
#include "matrix.h"
#include <math.h>

///////////////////////////////////////////////////////////////////////
// Rendering Constants:

#define Kd 0.99
#define Il 0.99
#define Ia 0.4
#define Ks 0.7

///////////////////////////////////////////////////////////////////////
// Other Constatns:

#define MAX_EDGES_IN_POLY 5
#define ERROR -1
#define NO_X_VIEW 0
#define NO_Y_VIEW 1
#define NO_Z_VIEW 2
#define EYE_VIEW 3
#define PI 3.14159265359
#define BGCOLOR 0
#define MAXSX 950
#define MAXSY 950
#define MAX_VERT 3000
#define NORM_1 1
#define NORM_2 -1
#define COLORS_LIMIT 200

///////////////////////////////////////////////////////////////////////

typedef struct vertex_structure{
  float x,y,z;	   /* vertex coordinates */
  vertex_structure *next;       // pt to next node in the list.
} vertex;

typedef struct edge_structure{
  int v1,v2;		    //vertex IDs (0..n)
  edge_structure *next;         // pt to next node in the list.
} edge;

typedef struct polygon_structure{
  int *first_edge;					// array of edges
  int color;			// color of the polygon
  int number_of_edges;          // number of edges in first_edge array
  float x_norm, y_norm, z_norm; // vector normals
  polygon_structure *next;		// pt to next node in the list.
} polygon;

typedef struct poly_ID_list{
	int ID;
        float depth;
} PIDLs;

typedef struct colors_list_type{
	int r,g,b;
} colors_list;

///////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////

float unitize(float *v)
{ 
  float mag = getlen(v); 
  if (mag != 0)
  {
	v[0] = v[0] / mag;
	v[1] = v[1] / mag;
	v[2] = v[2] / mag;
  }
}
float unitize(float *v, float scale)
{ 
  float mag = getlen(v); 
  if (mag != 0)
  {
	v[0] = scale * (v[0] / mag);
	v[1] = scale * (v[1] / mag);
	v[2] = scale * (v[2] / mag);
  }
}

///////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////

class VList // list of vertexes
{
 public:
  int number;
  vertex *first;
  void Add(float x, float y, float z)
    {
      number++;
      if (first == NULL)
	{
	  first = new vertex;
	  first->x = x;
	  first->y = y;
	  first->z = z;
	  first->next = NULL;
	} else {
	  vertex *pt1 = first;
	  while (pt1->next != NULL) { pt1 = pt1->next; }
	  pt1->next = new vertex;
	  pt1->next->x = x;
	  pt1->next->y = y;
	  pt1->next->z = z;
	  pt1->next->next = NULL;
	}
    }
  void Get(int number, float &x, float &y, float &z)
    {
      int id=0;
      vertex *pt1 = first;
      while ((id != number) && (pt1 != NULL))
	{
	  id++;
	  pt1 = pt1->next;
	}
      if (pt1 != NULL)
	{
	  x = pt1->x;
	  y = pt1->y;
	  z = pt1->z;
	} else {
	  x = ERROR;
	  y = ERROR;
	  z = ERROR;
	}
    }
  VList()
    {
      first = NULL;
      number=0;
    }
  ~VList()
    {
      vertex *pt1 = first, *pt2;
      // Delete all vertexes;
      while (pt1 != NULL)
	{
	  pt2 = pt1->next;
	  delete pt1;
	  pt1 = pt2;
	}
      first = NULL;
      number=0;
    }
};

///////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////

class EList // list of edges
{
 public:
  int number;
  edge *first;
  void Add(int v1, int v2)
    {
      number++;
      if (first == NULL)
	{
	  first = new edge;
	  first->v1 = v1;
	  first->v2 = v2;
	  first->next = NULL;
	} else {
	  edge *pt1 = first;
	  while (pt1->next != NULL) { pt1 = pt1->next; }
	  pt1->next = new edge;
	  pt1->next->v1 = v1;
	  pt1->next->v2 = v2;
	  pt1->next->next = NULL;
	}
    }
  void Get(int number, int &v1, int &v2)
    {
      int id=0;
      edge *pt1 = first;
      while ((id != number) && (pt1 != NULL))
	{
	  id++;
	  pt1 = pt1->next;
	}
      if (pt1 != NULL)
	{
	  v1 = pt1->v1;
	  v2 = pt1->v2;
	} else {
	  v1 = ERROR;
	  v2 = ERROR;
	}
    }
  EList()
    {
      first = NULL;
      number=0;
    }
  ~EList()
    {
      edge *pt1 = first, *pt2;
      // Delete all edges;
      while (pt1 != NULL)
	{
	  pt2 = pt1->next;
	  delete pt1;
	  pt1 = pt2;
	}
      first = NULL;
      number=0;
    }
};

///////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////

class PList // list of polygons
{
 public:
  int number;
  polygon *first;
  void Add(int number_of_edges,
	   int *list_of_edges,
	   int color,
	   float x_norm,
	   float y_norm,
	   float z_norm)
    {
      number++;
      if (first == NULL)
	{
	  first = new polygon;
	  first->number_of_edges = number_of_edges;
	  first->first_edge = list_of_edges;
	  first->color = color;
	  first->x_norm = x_norm;
	  first->y_norm = y_norm;
	  first->z_norm = z_norm;
	  first->next = NULL;
	} else {
	  polygon *pt1 = first;
	  while (pt1->next != NULL) { pt1 = pt1->next; }
	  pt1->next = new polygon;
	  pt1->next->number_of_edges = number_of_edges;
	  pt1->next->first_edge = list_of_edges;
	  pt1->next->color = color;
	  pt1->next->x_norm = x_norm;
	  pt1->next->y_norm = y_norm;
	  pt1->next->z_norm = z_norm;
	  pt1->next->next = NULL;
	}
    }
  void Get(int number,
	   int &number_of_edges,
	   int *& list_of_edges,
	   int &color,
	   float &x_norm,
	   float &y_norm,
	   float &z_norm)
    {
      int id=0;
      polygon *pt1 = first;
      while ((id != number) && (pt1 != NULL))
	{
	  id++;
	  pt1 = pt1->next;
	}
      if (pt1 != NULL)
	{
	  number_of_edges = pt1->number_of_edges;
	  list_of_edges = pt1->first_edge;
	  x_norm = pt1->x_norm;
	  y_norm = pt1->y_norm;
	  z_norm = pt1->z_norm;
	  color = pt1->color;
	} else {
	  list_of_edges = NULL;
	  number_of_edges = 0;
	}
    }
  PList()
    {
      first = NULL;
      number=0;
    }
  ~PList()
    {
      polygon *pt1 = first, *pt2;
      // Delete all polygons;
      while (pt1 != NULL)
	{
	  pt2 = pt1->next;
	  delete pt1->first_edge;
	  delete pt1;
	  pt1 = pt2;
	}
      first = NULL;
      number=0;
    }
};

///////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////

class Model
{
 private:
  VList Vertexes;
  EList Edges;
  PList Polygons;
  char  text[255]; 	// First line comment (max-len 255)
 public:
  colors_list colors[COLORS_LIMIT];
  int ncol;
  void Read_In(char *file_name);      // reads in data from file
  void Check();                       // checks for errors in data
  void Info();                        // display data
  void Write_Out(char *file_name);    // write out model to file 
};

///////////////////////////////////////////////////////////////////////

void Model::Read_In(char *file_name)
{
  printf(" Model: Reading in definition file (%s)\n", file_name);

  float tx, ty, tz;
  int   xxx,nf, nv, ve, vp, n, app,bpp,npp;
  float u,v,du,dv,x,y,z,rx,ry,rz;  // for sphere
  float nx,ny,nz; // normal
  float dx1,dx2,dy1,dy2,dz1,dz2;
  float x1,x2,x3,y1,y2,y3,z1,z2,z3;
  int e1,e2,e3, v1,v2,v3;
  int   d, color;    //  d - number used for parametric equations.
  int   count;
  int   type;
  float mag;
  char  c=' ';
  int   a,b,pne;
  int   *edges_list, ne;
  float dv1[3],dv2[3],norm[3];

  FILE *file;
  file = fopen(file_name, "r");

  // Get the comment string:
  count = 0;
  while (c!='\n')
    {
      fscanf(file,"%c",&c);
      text[count++] = c;
    }
  text[count] = '\0';
  
  // get number of figures:
  fscanf(file,"%d",&nf);
  
  for (count=0; count<nf; count++)
    {
      // read in type of the figure
      // 0 - spere     (x,y,z,rx,ry,rz,d,color)
      fscanf(file,"%d",&type);
      if (type==0)
	{
	  // Sphere :
	  fscanf(file,"%f%f%f",&x,&y,&z);
	  fscanf(file,"%f%f%f",&rx,&ry,&rz);
	  fscanf(file,"%d%d",&d,&color);
	  // Create parametric sphere:
	  dv = (PI)/(d-1);
	  du = (2 * PI)/(d-1);
	  nv = Vertexes.number;
	  for (a=0; a<=d; a++)
	    {
	      for (b=0; b<=d; b++)
		{
		  u = du * b;
		  v = dv * a - (PI/2) ;
		  tx = x + rx * cos (u+0.0) * cos (v+0.0);
		  ty = y + ry * sin (v+0.0);
		  tz = z + rz * cos (v+0.0) * sin (u+0.0);
		  Vertexes.Add(tx, ty, tz);
		}
	    }

	  pne = Edges.number;

	  for (a=0; a<d; a++)
	    {
	      for (b=0; b<d; b++)
		{
		  n = nv + a * (d+1) + b;
		  
		  Edges.Add(n+1,n);
		  Edges.Add(n,n+d+1);
		  Edges.Add(n+d+1,n+1);
		  if ((a<d-1) && (b<d-1))
		  {  
		  	edges_list = new int[MAX_EDGES_IN_POLY];
		  	ne = Edges.number;
			e1 = ne - 1;
			e2 = ne - 2;
			e3 = ne - 3;
		  	edges_list[0] = e1;
		  	edges_list[1] = e2;
		  	edges_list[2] = e3;
			
			Edges.Get(e1,v2,v3);
			Edges.Get(e2,v1,v2);
			Vertexes.Get(v1,x1,y1,z1);
			Vertexes.Get(v2,x2,y2,z2);
			Vertexes.Get(v3,x3,y3,z3);

			dv1[0] = - (x2 - x1);
			dv1[1] = - (y2 - y1);
			dv1[2] = - (z2 - z1);

			dv2[0] = x3 - x2;
			dv2[1] = y3 - y2;
			dv2[2] = z3 - z2;

			//do    dv2 X dv1:

			cross(dv2,dv1,norm);

 			unitize(norm,NORM_1);

            Polygons.Add(3,edges_list,color,norm[0],norm[1],norm[2]);  
		  }
		}
	    }

	  for (app=0; app<d-1; app++)
	    {
	      for (bpp=0; bpp<d-1; bpp++)
		{
		  npp = pne + app * 3 * d + bpp * 3 + 4;
		  e1 = npp;
		  e2 = npp - 2;
		  e3 = pne + (app+1) * 3 * d + bpp * 3;

	  	  edges_list = new int[MAX_EDGES_IN_POLY];

		  edges_list[0] = e1;
		  edges_list[1] = e2;
		  edges_list[2] = e3;

		  Edges.Get(e3,v1,v2);
		  Edges.Get(e2,v2,v3);
		  
                  Vertexes.Get(v1,x1,y1,z1);
		  Vertexes.Get(v2,x2,y2,z2);
		  Vertexes.Get(v3,x3,y3,z3);

		  dv1[0] = - (x2 - x1);
		  dv1[1] = - (y2 - y1);
		  dv1[2] = - (z2 - z1);

		  dv2[0] = x3 - x2;
		  dv2[1] = y3 - y2;
		  dv2[2] = z3 - z2;


		  // do dv1 X dv2:

		  cross(dv2,dv1,norm);
   		  unitize(norm,NORM_2);

            Polygons.Add(3,edges_list,color,norm[0],norm[1],norm[2]);  
		}
	    }

	  
	}
    }

  // get number of colors
  fscanf(file,"%d",&ncol);  
  for (count=0; count<ncol; count++)
  { fscanf(file,"%d%d%d",
          &colors[count].r,
          &colors[count].g,
          &colors[count].b); 
  }  
  fclose(file);
}

///////////////////////////////////////////////////////////////////////

void Model::Check()
{
  int Eerrors = 0, Perrors= 0;
  int count,count2;
  
  float x1,y1,z1,x2,y2,z2;
  int	v1,v2;
  int   color;
  int   number_of_edges;
  int  *list_of_edges;
  float x_norm, y_norm, z_norm;

  printf(" Model: Checking components:");
  
  printf("\n         Bad edges   : ");
  for (count = 0; count < Edges.number; count++)
    {
      Edges.Get(count,v1,v2);
      Vertexes.Get(v1,x1,y1,z1);
      Vertexes.Get(v2,x2,y2,z2);
      if (x1 == ERROR || x2 == ERROR)
	{
	  printf("%d ",count);
	  Eerrors++;
	}
    }
  if (Eerrors == 0) { printf("none"); }
  
  printf("\n         Bad polygons: ");
  for (count = 0; count < Polygons.number; count++)
    {
      Polygons.Get(count, number_of_edges, list_of_edges, color, x_norm, y_norm, z_norm);
      
      for (count2 = 0; count2 < number_of_edges; count2++)
	{
	  Edges.Get(list_of_edges[count2],v1,v2);
	  if (v1 == ERROR || v2 == ERROR)
	    {
	      printf("%d (%d) ",count, count2);
	      Perrors++;
	    }
	}
    }
  if (Perrors == 0) { printf("none"); }
  printf("\n        %d errors found.\n", Perrors + Eerrors);
}

///////////////////////////////////////////////////////////////////////

void Model::Info()
{
  int count, count2;
  
  float x,y,z;
  int	v1,v2;
  int   color;
  int   number_of_edges;
  int  *list_of_edges;
  float x_norm, y_norm, z_norm;
  
  printf(" Model: Components display:\n");
  printf("   Comment Line:\n");
  printf("   %s", text);
  
  printf("         %d vertexes:\n",Vertexes.number);
  for (count = 0; count < Vertexes.number; count++)
    {
      Vertexes.Get(count,x,y,z);
      printf("           #%d : %f %f %f\n",count, x, y, z);
    }
  
  printf("         %d edges\n",Edges.number);
  for (count = 0; count < Edges.number; count++)
    {
      Edges.Get(count,v1,v2);
      printf("           #%d : %d %d\n",count, v1, v2);
    }
  
  printf("         %d polygons\n",Polygons.number);
  for (count = 0; count < Polygons.number; count++)
    {
      Polygons.Get(count, number_of_edges, list_of_edges, color, x_norm, y_norm, z_norm);
      printf("           #%d : [%d] ",count,number_of_edges);
      for (count2 = 0; count2 < number_of_edges; count2++)
	{ printf("%d ",list_of_edges[count2]); }
      printf("(%d) {%f,%f,%f}\n",color, x_norm, y_norm, z_norm);
    }
}

///////////////////////////////////////////////////////////////////////

void Model::Write_Out(char *file_name)
{
  int count, count2;
  
  float x,y,z;
  int	v1,v2;
  int   color;
  int   number_of_edges;
  int  *list_of_edges;
  float x_norm, y_norm, z_norm;
  
  printf(" Model: Writing model file (%s)\n",file_name);
  
  FILE *file;
  file = fopen(file_name, "w");
  
  fprintf(file,"%s",text);
  fprintf(file,"%d ",Vertexes.number);
  fprintf(file,"%d ",Edges.number);
  fprintf(file,"%d \n",Polygons.number);
  
  printf("         %d vertexes\n",Vertexes.number);
  for (count = 0; count < Vertexes.number; count++)
    {
      Vertexes.Get(count,x,y,z);
      fprintf(file,"%f %f %f \n",x,y,z);
    }

  printf("         %d edges\n",Edges.number);
  for (count = 0; count < Edges.number; count++)
    {
      Edges.Get(count,v1,v2);
      fprintf(file,"%d %d \n",v1,v2);
    }
  
  printf("         %d polygons\n",Polygons.number);
  for (count = 0; count < Polygons.number; count++)
    {
      Polygons.Get(count, number_of_edges, list_of_edges, color, x_norm, y_norm, z_norm);
      fprintf(file,"%d ",number_of_edges);
      for (count2 = 0; count2 < number_of_edges; count2++)
	{ fprintf(file,"%d ",list_of_edges[count2]); }
      fprintf(file,"%d %f %f %f \n",color,x_norm,y_norm,z_norm);
    }

  printf("         %d colors\n",ncol);
  fprintf(file,"%d\n",ncol);
  for (count = 0; count < ncol; count++)
  {
   fprintf(file,"%d %d %d \n",
       colors[count].r,
       colors[count].g,
       colors[count].b);
  }

  fclose(file);
}

///////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////

class Rendered_Model
{
 public:
  char  text[255]; 							// First line comment (max-len 255)
  void Read_In(char *file_name);      // reads in data from model file
  vertex *svl;  // Static vertex list - for speed
  int vnumber;
  edge *sel;  // Static egdes list - for speed
  int enumber;
  polygon *spl;  // Static polygons list - for speed
  int pnumber;
  int ncol;
  colors_list colors[COLORS_LIMIT];
};

///////////////////////////////////////////////////////////////////////

void Rendered_Model::Read_In(char *file_name)
{
  int   nv,ne,np; // number of vertexes, edges and polygons
  float x,y,z;
  int   v1,v2;
  int   color;
  float x_norm, y_norm, z_norm;
  int   *edges_list;
  int   edge_number;
  int 	count, count2, count3;
  char  c=' ';

  FILE *file;
  file = fopen(file_name, "r");

  count = 0;
  while (c!='\n')
    {
      fscanf(file,"%c",&c);
      text[count++] = c;
    }
  text[count] = '\0';

  fscanf(file,"%d%d%d",&nv,&ne,&np);

  svl = new vertex[nv + 1];
  vnumber = nv;

  sel = new edge[ne + 1];
  enumber = ne;

  spl = new polygon[np + 1];
  pnumber = np;

  for (count=0; count<nv; count++)
    {
      fscanf(file,"%f%f%f",&x,&y,&z);
      svl[count].x = x;
      svl[count].y = y;
      svl[count].z = z;
    }
  
  for (count=0; count<ne; count++)
    {
      fscanf(file,"%d%d",&v1,&v2);
      sel[count].v1 = v1;
      sel[count].v2 = v2;
    }
  
  for (count=0; count<np; count++)
    {
      fscanf(file,"%d",&count2);
      edges_list = new int[count2];
      for (count3 = 0; count3 < count2; count3++)
	{ fscanf(file,"%d",&edges_list[count3]); }
      fscanf(file,"%d%f%f%f",&color,&x_norm,&y_norm,&z_norm);

      spl[count].first_edge = edges_list;
      spl[count].color = color;
      spl[count].number_of_edges = count2;
      spl[count].x_norm = x_norm;
      spl[count].y_norm = y_norm;
      spl[count].z_norm = z_norm;
    }

  // get number of colors
  fscanf(file,"%d",&ncol);  
  for (count=0; count<ncol; count++)
  { fscanf(file,"%d%d%d",
          &colors[count].r,
          &colors[count].g,
          &colors[count].b); 
  }  

  fclose(file);
}

///////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////

class View
{
 public:
  char  text[255];
  float view_lox, view_loy, view_loz, view_hix, view_hiy, view_hiz;
  int   screen_lox, screen_loy, screen_hix, screen_hiy;
  int   plane_lox, plane_loy, plane_hix, plane_hiy;
  float vpr_x, vpr_y, vpr_z;
  float look_x, look_y, look_z;
  float u[3];
  float n[3];
  float v[3];
  float r[3];
  float light[3];
  float refl[3];
  float m[4][4];
  float up_x, up_y, up_z;
  float eye_x, eye_y, eye_z;
  float eye_n;
  float light_x, light_y, light_z;
  PIDLs *pidl;
  void  Read_In(char *file_name);
  void  Open_View_Port();
  void  Draw_World_Line(int view_type, float x1, float y1, float z1,
			float x2, float y2, float z2);
  void  Render_Model(int view_type);
  vertex *cav;  // Static vertex list - for speed (in camera world)
};

///////////////////////////////////////////////////////////////////////
// Decleare global objects to be used by Modeler and Renderer
///////////////////////////////////////////////////////////////////////

View my_view_noX;				 // create view
View my_view_eye;				 // create view
View my_view_par;				 // create view
View my_view_noZ;				 // create view
Rendered_Model the_model;  // create new model for rendering
Model my_model;            // create new model for data input

///////////////////////////////////////////////////////////////////////

void View::Read_In(char *file_name)
{
  char c=64;

  pidl = NULL;

  FILE *file;
  file = fopen(file_name, "r");

  int count = 0;
  while (c!='\n')
    {
      fscanf(file,"%c",&c);
      if (c!='\n') { text[count++] = c; }
    }
  text[count] = '\0';

  fscanf(file,"%f%f%f%f%f%f",&view_lox, &view_loy, &view_loz, &view_hix, &view_hiy, &view_hiz);
  fscanf(file,"%d%d%d%d",&plane_lox, &plane_loy, &plane_hix, &plane_hiy);
  fscanf(file,"%f%f%f",&vpr_x, &vpr_y, &vpr_z);
  fscanf(file,"%f%f%f",&look_x, &look_y, &look_z);
  fscanf(file,"%f%f%f",&up_x, &up_y, &up_z);
  fscanf(file,"%f%f%f",&eye_x, &eye_y, &eye_z);
  fscanf(file,"%f%f%f",&light_x, &light_y, &light_z);
  fscanf(file,"%d%d%d%d",&screen_lox, &screen_loy, &screen_hix, &screen_hiy);
  fclose(file);

  eye_n = sqrt(eye_x * eye_x + eye_y * eye_y + eye_z * eye_z);

  light[0] = light_x;
  light[1] = light_y;
  light[2] = light_z;

  unitize(light);

  r[0] = vpr_x;
  r[1] = vpr_y;
  r[2] = vpr_z;
 
  float v1[3],v2[3],vr[3];
  float mag;

  // make the up vector more exact:

  v1[0] = look_x - vpr_x; 
  v1[1] = look_y - vpr_y; 
  v1[2] = look_z - vpr_z; 

  unitize(v1);

  n[0] = v1[0];
  n[1] = v1[1];
  n[2] = v1[2];

  unitize(n);

  v2[0] = up_x;
  v2[1] = up_y;
  v2[2] = up_z;

  unitize(v2);

  // set U vector:
  cross(n,v2,vr);

  u[0] = vr[0];
  u[1] = vr[1];
  u[2] = vr[2];

  unitize(u);

  // correct UP verctor V:
  cross(v1,vr,v2);

  v[0] = - v2[0];
  v[1] = - v2[1];
  v[2] = - v2[2];

  unitize(v);

   // calculate the transformation matrix:
 
	    m[0][0] = u[0];
	    m[1][0] = u[1];
	    m[2][0] = u[2];
	    m[3][0] = -1 * dotprod(r,u);

	    m[0][1] = v[0];
	    m[1][1] = v[1];
	    m[2][1] = v[2];
	    m[3][1] = -1 * dotprod(r,v);

	    m[0][2] = n[0];
	    m[1][2] = n[1];
	    m[2][2] = n[2];
	    m[3][2] = -1 * dotprod(r,n);

	    m[0][3] = 0;
	    m[1][3] = 0;
	    m[2][3] = 0;
	    m[3][3] = 1;
   
            cav = new vertex[the_model.enumber + 3];
}

///////////////////////////////////////////////////////////////////////

void View::Open_View_Port()
{
  SetLineWidth(2);   // show viewport window
  SetColor(GetRGBColor(255,30,30));
  DrawBox(screen_lox - 2,
	  screen_loy - 2,
	  screen_hix - screen_lox + 4,
	  screen_hiy - screen_loy + 4);
  SetColor(GetRGBColor(0,0,0));
  SetLineWidth(1);   // show viewport description
  DrawText(text,screen_lox,screen_loy - 10);
}

///////////////////////////////////////////////////////////////////////

void View::Draw_World_Line(int view_type, float x1, float y1, float z1,
	                   float x2, float y2, float z2)
{
 int xs1,ys1, xs2,ys2;  // screen coordinates
 int xr,yr;
 float vx,vy,vz;
 float px,py;
 xr = screen_hix - screen_lox;
 yr = screen_hiy - screen_loy;
 vx = view_hix - view_lox;
 vy = view_hiy - view_loy;
 vz = view_hiz - view_loz;

 if (view_type == NO_Z_VIEW) {
  xs1 = (int) (screen_lox + ((xr) * ((x1 - view_lox)/(vx))));
  xs2 = (int) (screen_lox + ((xr) * ((x2 - view_lox)/(vx))));
  ys1 = (int) (screen_hiy - ((yr) * ((y1 - view_loy)/(vy)) ));
  ys2 = (int) (screen_hiy - ((yr) * ((y2 - view_loy)/(vy)) ));
  DrawLine(xs1,ys1,xs2,ys2);
 } else
 if (view_type == NO_Y_VIEW) {
  xs1 = (int) (screen_lox + ((xr) * ((x1 - view_lox )/(vx)) ));
  xs2 = (int) (screen_lox + ((xr) * ((x2 - view_lox )/(vx)) ));
  ys1 = (int) (screen_hiy - ((yr) * ((z1 - view_loz )/(vz)) ));
  ys2 = (int) (screen_hiy - ((yr) * ((z2 - view_loz )/(vz)) ));
  DrawLine(xs1,ys1,xs2,ys2);
 } else
 if (view_type == NO_X_VIEW) {
  xs1 = (int) (screen_lox + ((xr) * ((z1 - view_loz )/(vz)) ));
  xs2 = (int) (screen_lox + ((xr) * ((z2 - view_loz )/(vz)) ));
  ys1 = (int) (screen_hiy - ((yr) * ((y1 - view_loy )/(vy)) ));
  ys2 = (int) (screen_hiy - ((yr) * ((y2 - view_loy )/(vy)) ));
  DrawLine(xs1,ys1,xs2,ys2);
 } 
}

///////////////////////////////////////////////////////////////////////


void View::Render_Model(int view_type)
{
  SetLineWidth(1);

  int count, count2;

 int xs1,ys1, xs2,ys2;  // screen coordinates
 int xr,yr;
 float vx,vy,vz;
 float px,py;
 xr = screen_hix - screen_lox;
 yr = screen_hiy - screen_loy;
 vx = view_hix - view_lox;
 vy = view_hiy - view_loy;
 vz = view_hiz - view_loz;
 XPoint poly_sx[10];
 int vcn;

  float x1,y1,z1, x2,y2,z2;
  int   nv, ne, np;
  int	v1,v2,v3;
  int   color;
  int   a,b;
  float rxp,ryp,rzp;
  int   number_of_edges;
  int  *list_of_edges;
  float x_norm, y_norm, z_norm;
  int   hide;
  float temp;
  float ns[3];

  int   rcolor;
  int   gcolor;
  int   bcolor;

  float sid,si1,si2,six;

  float pw1[4]; // point in world
  float pw2[4]; // point in world
  float cw1[4]; // point in camera world
  float cw2[4]; // point in camera world
  float pr1[4];  // result
  float pr2[4];  // result

  nv = the_model.vnumber;
  ne = the_model.enumber;
  np = the_model.pnumber;
  

  if (pidl == NULL) 
   { pidl = new PIDLs[np+10]; }

  // Move all the vertexes into camera vertex list:
  int nip;
  for (count=0; count <nv; count ++)
  {
	  pw1[0] = the_model.svl[count].x;
	  pw1[1] = the_model.svl[count].y;
	  pw1[2] = the_model.svl[count].z;
	  pw1[3] = 1;

	  vecmult(m,pw1,pr1);

	  cav[count].x = pr1[0];
	  cav[count].y = pr1[1];
	  cav[count].z = pr1[2];

  }

  // Sort polygons:
  float totz;
  np = the_model.pnumber;
  for (count=0; count <np; count++)
  {
      totz=0;
      number_of_edges = the_model.spl[count].number_of_edges;
      list_of_edges = the_model.spl[count].first_edge; 
      for (count2=0; count2<number_of_edges; count2++)
      {
	  v1 = the_model.sel[list_of_edges[count2]].v1;
	  v2 = the_model.sel[list_of_edges[count2]].v2;
	  totz = totz + cav[v1].z;
	  totz = totz + cav[v2].z;
      }
      pidl[count].ID = count;
      pidl[count].depth = totz;
  }

  // Selection sort the array of polygon IDs:
  int p_id;
  float p_de;

  np = the_model.pnumber;  
  for (a=0; a<np-1; a++)
  {
   p_de = pidl[a].depth;
   p_id = a;
   for (b=a+1; b<np; b++)
   {
	if (p_de<pidl[b].depth)
        {
	     p_id = b;
             p_de = pidl[b].depth;
        }
   }
   b = pidl[p_id].ID;
   pidl[p_id].ID = pidl[a].ID;
   pidl[a].ID = b;

   pidl[p_id].depth = pidl[a].depth;
   pidl[a].depth = p_de; 
  }

  // Draw all polygons:
  int count_ps;
  np = the_model.pnumber;
  for (count_ps=0; count_ps <np; count_ps ++)
    {
      count = pidl[count_ps].ID;
      number_of_edges = the_model.spl[count].number_of_edges;
      list_of_edges = the_model.spl[count].first_edge;
      rcolor = the_model.colors[the_model.spl[count].color].r;
      gcolor = the_model.colors[the_model.spl[count].color].g;
      bcolor = the_model.colors[the_model.spl[count].color].b;

      x_norm = the_model.spl[count].x_norm;
      y_norm = the_model.spl[count].y_norm;
      z_norm = the_model.spl[count].z_norm;

      ns[0] = x_norm;	
      ns[1] = y_norm;	
      ns[2] = z_norm;	

      temp = dotprod (ns, n);
      if (temp>=0) 
	{ hide=1; }
 	  else
        { hide=0; }

      if (view_type!=EYE_VIEW)
      {

        for (count2=0; count2<number_of_edges; count2++)
	{
	  v1 = the_model.sel[list_of_edges[count2]].v1;
	  v2 = the_model.sel[list_of_edges[count2]].v2;

	  x1 = the_model.svl[v1].x;
	  y1 = the_model.svl[v1].y;
	  z1 = the_model.svl[v1].z;

	  x2 = the_model.svl[v2].x;
	  y2 = the_model.svl[v2].y;
	  z2 = the_model.svl[v2].z;

          SetColor(GetRGBColor(rcolor,gcolor,bcolor));
	  Draw_World_Line(view_type,x1,y1,z1, x2,y2,z2); 

        }
      } else {

     int verts[20];
     int okver[4];
     int vnnn;

     if (hide==0) {

        vnnn = 0;
        for (count2=0; count2<number_of_edges; count2++)
	{
	  verts[vnnn++] = the_model.sel[list_of_edges[count2]].v1;
	  verts[vnnn++] = the_model.sel[list_of_edges[count2]].v2;
        }
        vnnn = 0;
        okver[0] = -1;
        okver[1] = -1;
        okver[2] = -1;
        for (count2=0; count2<6; count2++)
        {
	  if ( 
               (verts[count2]!=okver[0]) &&
               (verts[count2]!=okver[1]) &&
               (verts[count2]!=okver[2])
             )
           okver[vnnn++] = verts[count2];
        }

 xr = screen_hix - screen_lox;
 yr = screen_hiy - screen_loy;
 vx = view_hix - view_lox;
 vy = view_hiy - view_loy;
 vz = view_hiz - view_loz;

        for (count2=0; count2<3; count2++)
        {
	  x1 = cav[okver[count2]].x;
	  y1 = cav[okver[count2]].y;
	  z1 = cav[okver[count2]].z;

          px = x1 / (1 + (z1/fabs(eye_n)));
          py = y1 / (1 + (z1/fabs(eye_n)));

xs1 = (int) ( screen_lox+ (xr/2) + (xr/2) *(px/(plane_hix-plane_lox)) );
ys1 = (int) ( screen_hiy-((yr/2) + (yr/2) *(py/(plane_hiy-plane_loy))));

	poly_sx[count2].x = xs1;
	poly_sx[count2].y = ys1;
           }

      six = 0.5 * (Ia * Kd + Il * Kd * dotprod(light,ns));
      rcolor = (int) (six * rcolor);
      gcolor = (int) (six * gcolor);
      bcolor = (int) (six * bcolor);

      if (rcolor > 255) { rcolor = 255; }
      if (rcolor < 0)   { rcolor = 0;}
      if (gcolor > 255) { gcolor = 255; }
      if (gcolor < 0)   { gcolor = 0;}
      if (bcolor > 255) { bcolor = 255; }
      if (bcolor < 0)   { bcolor = 0;}

        SetColor(GetRGBColor(rcolor,gcolor,bcolor));
        DrawFilledPolygon(poly_sx,3);

	   }
	  }
    }
}

///////////////////////////////////////////////////////////////////////


