package smartmeca.smar;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;
//import android.os.SystemClock;
//import android.view.MotionEvent;

public class GLRendererEx implements Renderer{
	public int posx,posy;
	public Context context;
	public float zoom=1.f,rectI=1.f;
	float pas=.5f;
	
	
	//private GLCube tr;
	public smar sm;
	public GLRendererEx(Context c,Context c2){
		context=c;
		sm=new smar(context,c2);
		posx=0;
		posy=0;
	}
	
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		// TODO Auto-generated method stub
		gl.glDisable(GL10.GL_DITHER);
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
	//	gl.glClearColor(.8f, 0f, .2f, 1f);
		gl.glClearColor(sm.bgR,sm.bgG, sm.bgB, 1f);
		gl.glClearDepthf(1f);
		
	}
	@Override
	public void onDrawFrame(GL10 gl) {
		// TODO Auto-generated method stub
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		GLU.gluLookAt(gl, 0, 0, zoom*sm.dd*1.5f, 0, 0, 0, 0, 1, 0);
		gl.glDisable(GL10.GL_TEXTURE_2D);
		gl.glShadeModel (GL10.GL_SMOOTH);
		//adding light:)
		float mat_ambient[] = { 1,1, 1, 1 };
		float mat_specular[] = { 1, 1, 1, 1 };
//		   GLfloat light_position[] = { 0.0, 0.0, 10.0, 1.0 };
//		   GLfloat lm_ambient[] = { 0.2, 0.2, 0.2, 1.0 };

		gl.glMaterialfv(GL10.GL_FRONT, GL10.GL_AMBIENT, mat_ambient,0);
		gl.glMaterialfv(GL10.GL_FRONT, GL10.GL_SPECULAR, mat_specular,0);
		gl.glMaterialf(GL10.GL_FRONT, GL10.GL_SHININESS, 50.0f);
		//gl.glColorMaterial(GL10.GL_FRONT_AND_BACK, GL10.GL_AMBIENT_AND_DIFFUSE);
		gl.glEnable(GL10.GL_COLOR_MATERIAL);
		float ambientColor[] = {0.5f, 0.5f, 0.5f, 1.0f}; //Color(0.2, 0.2, 0.2)
	//	FloatBuffer aC;
	//	aC
	 //   gl.glLightModelfv(GL10.GL_LIGHT_MODEL_AMBIENT,GL10.GL_CO, ambientColor);
		gl.glRotatef((float)posy, 1, 0, 0);
		gl.glRotatef((float)posx, 0, 1, 0);
		gl.glTranslatef((-sm.lx1-sm.lx2)/2,(-sm.ly1-sm.ly2)/2, (-sm.lz1-sm.lz2)/2);
			
		gl.glLightModelfv(GL10.GL_LIGHT_MODEL_AMBIENT, ambientColor,0);
		
		float lightColor0[] = {1, 1, 1, 1.0f}; //Color (0.5, 0.5, 0.5)
	    float lightPos0[] = {0, 0.0f, 30*sm.dd, 1.0f}; //Positioned at (4, 0, 8)//samir 0309
	    gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_DIFFUSE, lightColor0,0);
	    gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, lightPos0,0);
		
		
		sm.draw(gl);
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		// TODO Auto-generated method stub
		gl.glViewport(0, 0, width, height);
		float ratio=(float) width/height;
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glFrustumf(-ratio, ratio, -1, 1, 1, 1000*sm.dd);//???
		//gl.glFrustumf(-10f, 10f, -10f, 5f, 1, 25);
	}

	

}
