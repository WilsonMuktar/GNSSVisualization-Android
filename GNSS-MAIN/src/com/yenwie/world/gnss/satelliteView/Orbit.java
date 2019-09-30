package com.yenwie.world.gnss.satelliteView;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;

import javax.microedition.khronos.opengles.GL10;

import com.example.gnss.main.SecondActivity;
import com.yenwie.world.gnss.satellite.OrbitValue;


public class Orbit {

    public ArrayList<HashMap<String,String>> orbitpositions = new ArrayList<HashMap<String,String>>();
    private float orbitvertices[] = {0.0f, 0.0f, 0.0f};
    private float radiusvertices[] = {0.0f, 0.0f, 0.0f};
    private FloatBuffer vertBuff = null;

    int MAXtotalpoint;
    HashMap<String,String> Position = new HashMap<String,String>();
    int maxscale = 10;
    
    Orbit(float a, float incline, float ecc, float RAAN, float perigee) {
        //init variable
        MAXtotalpoint = 360 * ((maxscale-SecondActivity.scale)>0?(maxscale-SecondActivity.scale):1);
        orbitpositions = new ArrayList<HashMap<String,String>>();
        a += 3.189f;
        RAAN = (float) Math.toRadians(RAAN);
        perigee = (float) Math.toRadians(perigee);
        incline = (float) Math.toRadians(incline);
        double f = 0;
        int j = 0;
        orbitvertices = new float[(MAXtotalpoint + 1) * 3];
        //calculate all point
        for (int i = 0; i <= MAXtotalpoint; i++) {
            f = Math.toRadians((float) i / ((maxscale-SecondActivity.scale)>0?(maxscale-SecondActivity.scale):1));
            double x = (a * (1 - (ecc * ecc)) / (1 + ecc * Math.cos(f)))
                    * ((Math.cos(RAAN) * Math.cos(perigee + f))
                    - (Math.sin(RAAN) * Math.sin(perigee + f) * Math.cos(incline)));
            double y = (a * (1 - (ecc * ecc)) / (1 + ecc * Math.cos(f)))
                    * ((Math.sin(RAAN) * Math.cos(perigee + f))
                    + (Math.cos(RAAN) * Math.sin(perigee + f) * Math.cos(incline)));
            double z = (a * (1 - (ecc * ecc)) / (1 + ecc * Math.cos(f)))
                    * ((Math.sin(perigee + f) * Math.sin(incline)));

            Position = new HashMap<String,String>();
            Position.put("x",x+"");
            Position.put("y",y+"");
            Position.put("z",z+"");
            orbitpositions.add(Position);
            orbitvertices[j] = (float) y;
            orbitvertices[j + 1] = (float) z;
            orbitvertices[j + 2] = (float) x;
            j += 3;
        }
        //ordering?
        ByteBuffer bBuff = null;
        bBuff = ByteBuffer.allocateDirect(orbitvertices.length * 4);
        bBuff.order(ByteOrder.nativeOrder());
        vertBuff = bBuff.asFloatBuffer();
        vertBuff.put(orbitvertices);
        vertBuff.position(0);
    }

    public void draw(GL10 gl, float red, float green, float blue, float alpha, OrbitValue orbitvalue,int a1,int a2) {
        //orbit line
        gl.glDisable(GL10.GL_TEXTURE_2D);
        gl.glEnable(GL10.GL_DEPTH_TEST);//enable depth [terhalang]
        gl.glDepthFunc(GL10.GL_LEQUAL);
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
	        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertBuff);
	        //Fill or not
	        if(SecondActivity.vis_fill==true){
	        	gl.glEnable(GL10.GL_BLEND);
	        	gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
	            gl.glColor4f(red, green, blue, 0.2f);
	        	gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 2, MAXtotalpoint - 1);
	        	gl.glDisable(GL10.GL_BLEND);
	        }
	        //LINE
	        gl.glColor4f(red, green, blue, alpha);
	        gl.glDrawArrays(GL10.GL_LINE_LOOP, 2, MAXtotalpoint - 1);
	        //RADIUS
	        if(SecondActivity.vis_radius==true){
		        radiusvertices = new float[]{
			        0.0f,
		        	0.0f,
			        0.0f,
		        	orbitvertices[MAXtotalpoint/2],
		        	orbitvertices[MAXtotalpoint/2+1],
		        	orbitvertices[MAXtotalpoint/2+2]
		        };
		        ByteBuffer vbb = ByteBuffer.allocateDirect(radiusvertices.length*4);  
		        vbb.order(ByteOrder.nativeOrder());
		        FloatBuffer vertex = vbb.asFloatBuffer();  
		        vertex.put(radiusvertices);  
		        vertex.position(0);
		        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertex);
		        gl.glDrawArrays(GL10.GL_LINE_STRIP, 0, 2);
	        }
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisable(GL10.GL_DEPTH_TEST);//disable depth [terhalang]
        gl.glDepthFunc(GL10.GL_NEVER);
        gl.glEnable(GL10.GL_TEXTURE_2D);
        
        //orbit satellite
        MAXtotalpoint = 360 * ((maxscale-SecondActivity.scale)>0?(maxscale-SecondActivity.scale):0);
        if((maxscale-SecondActivity.scale)==0)
        	MAXtotalpoint = 360;
        if((maxscale-SecondActivity.scale)<maxscale){
	        //rotation
	        if (GlRenderer.autorotate >= MAXtotalpoint)
	            GlRenderer.autorotate %= MAXtotalpoint>1?MAXtotalpoint-1:MAXtotalpoint;
//	        else
//	            GlRenderer.autorotate++;
        }
        
        for (int i = 0; i < orbitpositions.size(); i++) {
                for (int j = 0; j < orbitvalue.getSatelliteCount(); j++) {
                	if(GlRenderer.satellites[a1][a2][j]!=null&&
                		GlRenderer.satellites[a1][a2][j].visibility==true){
	                    int step = GlRenderer.autorotate + ((MAXtotalpoint / orbitvalue.getSatelliteCount()) * j);
	                    if (step >= MAXtotalpoint)
	                        step %= MAXtotalpoint!=0?MAXtotalpoint:1;
	                    if (i == step) {
	                        double x2 = Double.parseDouble(orbitpositions.get(i).get("x"));
	                        double y2 = Double.parseDouble(orbitpositions.get(i).get("y"));
	                        double z2 = Double.parseDouble(orbitpositions.get(i).get("z"));
	                        gl.glPushMatrix();
	                        gl.glEnable(GL10.GL_BLEND);
	                        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
	                        gl.glTranslatef((float) y2, (float) z2, (float) x2);
	                        gl.glRotatef(GlRenderer.rotatebackY, 0, 1, 0);// ?success
	                        gl.glRotatef(GlRenderer.rotatebackZ, 1, 0, 0);// ?
	                        gl.glScalef(3f, 3f, 1.0f);
	                        gl.glColor4f(1f, 1f, 1f, 1f);
	                        GlRenderer.satellites[a1][a2][j].Draw(gl);
	                    	gl.glDisable(GL10.GL_BLEND);
	                        gl.glPopMatrix();
	                    }
                	}
                }
        }
    }

}
