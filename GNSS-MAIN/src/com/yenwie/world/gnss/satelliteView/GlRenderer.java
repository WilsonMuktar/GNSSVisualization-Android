package com.yenwie.world.gnss.satelliteView;

import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.example.gnss.main.R;
import com.example.gnss.main.SecondActivity;
import com.yenwie.world.gnss.satellite.SatelliteSystem;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PointF;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;

public class GlRenderer implements Renderer {
    /**
     * Tilt the spheres a little.
     */
//    private static final int AXIAL_TILT_DEGRESS = 30;

    /**
     * Clear colour, alpha component.
     */
    private static final float CLEAR_RED = 0.0f;
    private static final float CLEAR_GREEN = 0.0f;
    private static final float CLEAR_BLUE = 0.0f;
    private static final float CLEAR_ALPHA = 0.5f;

    /**
     * Perspective setup, field of view component.
     */
    private static final float FOVY = 45.0f;

    /**
     * Perspective setup, near component.
     */
    private static final float Z_NEAR = 0.1f;
    private static final float Z_FAR = 400.0f;
    private static float OBJECT_DISTANCE = -120.0f;

    /**
     * The earth's sphere. 
     */
    private final Sphere mEarth;

    public static Quad[][][] satellites;
    public static int[][] orbitcolors;
    
    /**
     * The context.
     */
    private final Context mContext;

    /**
     * The rotation angle, just to give the screen some action.
     */
    public static float mRotationAngleY;
    public static float mRotationAngleZ;

    public static int autorotate = 0;
    public static int earthrotate = 0;
    public static int earthrotatescale = 1;

    ArrayList<SatelliteSystem> SSs;
    
    //for 1
    ArrayList<Orbit> Orbits = new ArrayList<Orbit>();
    
    public GlRenderer(final Context context, ArrayList<SatelliteSystem> SSs) {
        //initialize satellite object
        this.mContext = context;
        this.mEarth = new Sphere(3, 6.378f);
        this.SSs = SSs;
        
        //Work for one satellite system
        satellites = new Quad[SSs.size()][SSs.get(0).getOrbit().size()][10];
        orbitcolors = new int[SSs.size()][SSs.get(0).getOrbit().size()];
        		
        for(int i=0;i<SSs.size();i++){
        	for(int j=0;j<SSs.get(i).getOrbitCount();j++){
				Orbits.add(new Orbit(
						SSs.get(i).getOrbit().get(j).getRadius(),
						SSs.get(i).getOrbit().get(j).getIncline(), 
						SSs.get(i).getOrbit().get(j).getEcc(), 
						SSs.get(i).getOrbit().get(j).getRAAN(), 
						SSs.get(i).getOrbit().get(j).getPerigee()));
        		orbitcolors[i][j] = Color.WHITE;
        		for(int k=0;k<SSs.get(i).getOrbit().get(j).getSatelliteCount();k++)
        		{
        			satellites[i][j][k] = new Quad();
        		}
        	}
        }
        
        //initializing
        mRotationAngleY = 30.0f;
        mRotationAngleZ = 0.0f;
        offset = new PointF();
        SecondActivity.vis_fill=false;
        SecondActivity.vis_radius=false;
        SecondActivity.vis_satellite=true;
        SecondActivity.vis_orbit=true;
        rotatebackY=0;
        rotatebackZ=0;
        
    }

    @Override
    public void onSurfaceCreated(final GL10 gl, final EGLConfig config) {
    	
        //active transparency
        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        //active texture
        gl.glEnable(GL10.GL_TEXTURE_2D);
        gl.glShadeModel(GL10.GL_SMOOTH);
        gl.glClearColor(CLEAR_RED, CLEAR_GREEN, CLEAR_BLUE, CLEAR_ALPHA);
        gl.glClearDepthf(1.0f);
        gl.glDisable(GL10.GL_DEPTH_TEST);//disable depth [terhalang]
        gl.glDepthFunc(GL10.GL_NEVER);
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
    	
        //load texture to each satellite object
        mEarth.loadGLTexture(gl, this.mContext, R.drawable.earth);
        
        for(int i=0;i<SSs.size();i++)
        	for(int j=0;j<SSs.get(i).getOrbitCount();j++)
        		for(int k=0;k<SSs.get(i).getOrbit().get(j).getSatelliteCount();k++)
        		{
        			satellites[i][j][k].loadGLTexture(gl, SecondActivity.context, R.drawable.satellite1,
        					SSs.get(i).getOrbit().get(j).getOrbitTitle() +"_"+ (k + 1));
        		}

    }

    @Override
    public void onSurfaceChanged(final GL10 gl, final int width, final int height) {
        final float aspectRatio = (float) width / (float) (height == 0 ? 1 : height);
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        GLU.gluPerspective(gl, FOVY, aspectRatio, Z_NEAR, Z_FAR);
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
        gl.glTranslatef(0, 0, -2);
    }

    int currentscale = SecondActivity.scale;
    @Override
    public void onDrawFrame(final GL10 gl) { //LOOPING
    	if(SecondActivity.vis_all==true)
    	{
	        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
	        gl.glLoadIdentity();
	
	        //think as camera [for input controller parameter] 
	        gl.glTranslatef(offset.x, offset.y, OBJECT_DISTANCE);
	        gl.glRotatef(this.mRotationAngleY, 0, 1, 0);
	        gl.glRotatef(this.mRotationAngleZ, 0, 0, 1);
	
	        //EARTH
	        gl.glPushMatrix();
	        gl.glEnable(GL10.GL_DEPTH_TEST);//enable depth [terhalang]
	        gl.glDepthFunc(GL10.GL_LEQUAL);
	        if((earthrotate+=(SecondActivity.scale/4))>=360)
	        	earthrotate %= 360;
	        gl.glRotatef( earthrotate , 0, 1, 0);
	        this.mEarth.draw(gl);
	        gl.glDisable(GL10.GL_DEPTH_TEST);//disable depth [tdk terhalang]
	        gl.glDepthFunc(GL10.GL_NEVER);
	        gl.glPopMatrix();
	
	        GlRenderer.autorotate++;
	        
	        //ORBIT
	        for(int i=0;i<SSs.size();i++){
	        	for(int j=0;j<SSs.get(i).getOrbitCount();j++){
	        		//only redraw when change scale of speed
	//        		if(currentscale != SecondActivity.scale)
						Orbit o = new Orbit(
								SSs.get(i).getOrbit().get(j).getRadius(),
								SSs.get(i).getOrbit().get(j).getIncline(), 
								SSs.get(i).getOrbit().get(j).getEcc(), 
								SSs.get(i).getOrbit().get(j).getRAAN(), 
								SSs.get(i).getOrbit().get(j).getPerigee());
	        		//only render Orbit when satellite is visible
	        		if(orbitvisibility(j)==true)
				        o.draw(gl,
				        		(float) Color.red(orbitcolors[0][j]) / 255,
				        		(float) Color.green(orbitcolors[0][j]) / 255,
				        		(float) Color.blue(orbitcolors[0][j]) / 255,
				        		orbitcolors[0][j] == Color.TRANSPARENT ? 0f : 1f,
				        		SSs.get(i).getOrbit().get(j),i,j);
	        		
	        	}
	        }
	//        currentscale = SecondActivity.scale;
	//        System.out.println(SSs.size());
    	}
    }

    //return true if one satellite is visible / selected
    public boolean orbitvisibility(int orbitid){
    	for(int k=0;k<SSs.get(0).getOrbit().get(orbitid).getSatelliteCount();k++)
    	{
    		if(satellites[0][orbitid][k].visibility==true)
    			return true;
    	}
    	return false;
    }
    

	public static float rotatebackY = 0,rotatebackZ = 0;
    public static void rotateY(float n) {
        mRotationAngleY += n/100;
        rotatebackY -= n/100;
    }

    public static void rotateZ(float n) {
        mRotationAngleZ += n/100;
        rotatebackZ -= n/100;
    }

    public static PointF offset;
    public static void move(float xDelta, float yDelta) {
        offset.x += xDelta;
        offset.y += yDelta;
    }

    public static void zoom(float value) {
        OBJECT_DISTANCE += value;
    }
}
