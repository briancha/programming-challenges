/*  
    
    Specification : a fox that walks left across the screen, with a shadow following it 
  
*/

#include <stdio.h>
#include <string.h>
#include <time.h>
#include <math.h>

#include "glut.h"

#define WIN_POSX    400
#define WIN_POSY    400
#define WIN_WIDTH   400
#define WIN_HEIGHT  300

// rotation angles of front and back leg and position of animal respectively
double front_leg_pos, back_leg_pos, body_pos, body_rotate, tail_pos;
int forward = 0;					// boolean variable indicating whether leg position is straight or crooked

void cone()
// draw a standard 2 x 2 cone with 3 sides whose center is at (0, 1, 0)
{
	glPushMatrix();
	glTranslatef(0, 1, 0);
	glutSolidCone(2, 2, 3, 1);
	glPopMatrix();
}

void draw_torso(void)
{
	glPushMatrix();
	// draw torso : rectangle with x-vertices 50 & -50 and y-vertices 70 & 10
	glRectf(50.0, 70.0, -50.0, 10.0);
	glPopMatrix();
}

void draw_head(void)
{
	glPushMatrix();
	// draw head : rectangle with x-vertices 18 & -20 and y-vertices 34 & 0
	glRectf(18.0, 34.0, -20.0, 0.0);
	glPopMatrix();
}

void draw_ear(void)
{
	glPushMatrix();
	// draw ear (left and right) : cone of dimension 4 x 5 x 4 
	glScalef(4.0, 5.0, 4.0);
	cone();
	glPopMatrix();
}

void draw_tail(void)
{
	glPushMatrix();
	// draw tail : rectangle with x-vertices 18 & -20 and y-vertices 15 & 5
	glRectf(18.0, 15.0, -20.0, 5.0);
	glPopMatrix();
}

void draw_leg(void)
{
	glPushMatrix();
	// draw leg (front and back): rectangle with x-vertices 10 & 0 and y-vertices 15 & -30
	glRectf(10.0, 15.0, 0.0, -30.0);
	glPopMatrix();
}

void object(void)
{
	// allows fox to move across the screen
	glTranslatef(body_pos, 0.0, 0.0);
	// rotate body by body_rotate degrees
	glRotatef(body_rotate, 0.0, 0.0, 1.0);

	glPushMatrix();
	// draw body and body parts of fox
	draw_torso();

	glTranslatef(-40.0, 70.0, 0.0);		// M_(torso to head)
	draw_head();

	glTranslatef(-12.0, 34.0, 0.0);			// M_(head to left ear)
	draw_ear();

	glTranslatef(23.0, 0.0, 0.0);			// M_(left ear to right ear)
	// rotate tail by tail_pos degrees
	glRotatef(tail_pos, 0, 0, 1);
	draw_ear();

	glTranslatef(90.0, -60.0, 0.0);		// M_(right ear to tail)
	draw_tail();

	// using its own matrix allows the front leg to pivot independently of the back leg
	glPushMatrix();
	glTranslatef(-110, -45, 0);	// M_(tail to front leg)
	// rotate front leg by front_leg_pos degrees
	glRotatef(front_leg_pos, 0.0, 0.0, 1.0);
	draw_leg();
	glPopMatrix();

	glPushMatrix();
	glTranslatef(-25, -45, 0);		// M_(front leg to back leg)
	// rotate back leg by back_leg_pos degrees
	glRotatef(back_leg_pos, 0.0, 0.0, 1.0);
	draw_leg();
	glPopMatrix();

	glPopMatrix();
}

// Texture map a quadrilateral
void texture(void) 
{
	// Based directly on Lecture 10 slide 20
	GLubyte texArray[200][200][4];

	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);

	glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, 200, 200, 0, GL_RGBA,
	GL_UNSIGNED_BYTE, texArray);

	glEnable(GL_TEXTURE_2D);
	
	// Assign the full range of texture colors to a quadrilateral
	glBegin(GL_QUADS);
		glColor3f(0.0, 0.5, 0.0); // Green color for quadrilateral texture
		glTexCoord2f(0.0, 0.0); glVertex3f(-200, -100, -100);
		glTexCoord2f(1.0, 0.0); glVertex3f(-100, -100, -100);
		glTexCoord2f(1.0, 1.0); glVertex3f(0, -50, 0);
		glTexCoord2f(0.0, 1.0); glVertex3f(0, -100, 0);
	glEnd();

	glDisable(GL_TEXTURE_2D);
}

void drawscene (void)     
{
    //////////////////////////////////////////////////////////////////
    // 
    // Setup perspective projection and the rotation
    // 
    GLint viewport[4];
    glGetIntegerv( GL_VIEWPORT, viewport ); // viewport is by default the display window
    glMatrixMode(GL_PROJECTION);
      glLoadIdentity();
      gluPerspective( 45, double(viewport[2])/viewport[3], 0.1, 1000 );
    glMatrixMode(GL_MODELVIEW);
      glLoadIdentity();
      gluLookAt( 0, 0, 400, 0, 0, 0, 0,1,0 );
    //
    //////////////////////////////////////////////////////////////////

	/*  Enable Z buffer method for visibility determination. */
	//  Z buffer code starts
	
        glClear (GL_DEPTH_BUFFER_BIT);
        glEnable (GL_DEPTH_TEST);
	
	// Z buffer code ends */

	glClearColor (1, 1, 1, 0.0);	// Set display-window color to white.
	glClear (GL_COLOR_BUFFER_BIT);		// Clear display window.

	texture(); // Texture map quadrilateral

	// Shadow - Based on Quiz 2 Qn 4(c) answer

	// The proportion between Xs, Ys, Zs and -1.0 / 9 determines how large 
	// the shadow is compared to the fox
	GLdouble Xs = 7;
	GLdouble Ys = 7;
	GLdouble Zs = 7;
	GLdouble M[16] = { 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, -1.0 / 9, 0, 0, 0, 0 };
	glPushMatrix();
	glColor3f(0.65, 0.16, 0.16); // Brown color for fox
	object(); // Draw brown fox

	glTranslatef(Xs, Ys, Zs);
	glMultMatrixd(M);
	glTranslatef(-Xs, -Ys, -Zs);
	glColor3f(0.7, 0.7, 0.7); // Gray color for fox's shadow
	object(); // Draw fox's shadow
	
	glPopMatrix(); // Restore state

	glutSwapBuffers();
}

void movement(void) {
	// simulates fox walking by moving legs back and forth, rotating body, and wagging tail

	if (forward == 0) {
		// move fox's legs backward
		front_leg_pos += 20;
		back_leg_pos += 20;
		// rotate body
		body_rotate -= 4;
		// wag tail
		tail_pos += 5;
		forward = 1;
	}

	else {
		// move fox's legs forward
		front_leg_pos -= 20;
		back_leg_pos -= 20;
		// rotate body back
		body_rotate += 4;
		// wag tail back
		tail_pos -= 5;
		forward = 0;
	}

	body_pos -= 0.03; // move fox left 

	// when fox disappears off the left side of the screen, move it back to the right side
	if (body_pos < -170) {
		body_pos = 170;
	}
}


void animate(void)
{
	movement(); // move fox

	glutPostRedisplay();
}

void main (int argc, char** argv)
{
     
		glutInit (&argc, argv);			                      // Initialize GLUT
		glutInitDisplayMode (GLUT_DOUBLE | GLUT_RGB |  GLUT_DEPTH); // Set display mode
		
		glutInitWindowPosition( WIN_POSX, WIN_POSY );         // Set display-window position at (WIN_POSX, WIN_POSY) 
                                                              // where (0, 0) is top left corner of monitor screen
        glutInitWindowSize( WIN_WIDTH, WIN_HEIGHT );		  // Set display-window width and height.

		glutCreateWindow ("Robotic Bipedal Fox" );					  // Create display window.

		glutIdleFunc(animate);
	
		glutDisplayFunc (drawscene);   // put everything you wish to draw in drawscene

		glutMainLoop ( );
	
}