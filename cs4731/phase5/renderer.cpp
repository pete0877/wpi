///////////////////////////////////////////////////////////////////////
// Renderer program version 1.0
// Peter Golaszewski
// CS4831 C98
///////////////////////////////////////////////////////////////////////


#include "definitions.h"

///////////////////////////////////////////////////////////////////////

void quit(Widget w, void *data) { exit(0); }

///////////////////////////////////////////////////////////////////////

void draw_model(Widget w, int width, int height, void *data)
{
   ClearDrawArea();   // clear screen

   SetColor(GetRGBColor(BGCOLOR,BGCOLOR,BGCOLOR));
   DrawFilledBox(0,0,MAXSX,MAXSY);

   my_view_noZ.Open_View_Port();
   my_view_noZ.Render_Model(NO_Z_VIEW);

   my_view_noX.Open_View_Port();
   my_view_noX.Render_Model(NO_X_VIEW);

   my_view_eye.Open_View_Port();
   my_view_eye.Render_Model(EYE_VIEW);

   my_view_par.Open_View_Port();
   my_view_par.Render_Model(EYE_VIEW);

}

///////////////////////////////////////////////////////////////////////

void main(int argc, char **argv)
{

   the_model.Read_In("model.format"); // read in data from the model file

   my_view_noZ.Read_In("view_noZ.format");
   my_view_noX.Read_In("view_noX.format");
   my_view_eye.Read_In("view_eye.format");
   my_view_par.Read_In("view_par.format");

   Widget w[2];

   argc = OpenDisplay(argc, argv);
   if (argc == 0) exit(5);

   w[0] = MakeButton("Quit", quit, NULL);
   w[1] = MakeDrawArea(950,950, draw_model, NULL);

   SetWidgetPos(w[1], PLACE_UNDER, w[0], NO_CARE, NULL);

   ShowDisplay();
   GetStandardColors();

   MainLoop();

}

///////////////////////////////////////////////////////////////////////


