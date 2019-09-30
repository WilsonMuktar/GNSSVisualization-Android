package com.yenwie.world.gnss.satelliteView;
 
import java.util.ArrayList;

import com.yenwie.world.gnss.satellite.SatelliteSystem;

import android.content.Context;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.view.View;
import android.widget.RelativeLayout;

public class GLsatellitesystemView extends RelativeLayout {

	GLSurfaceView mGLSurfaceView;
	GlRenderer GLR;
	
	public GLsatellitesystemView(Context context, ArrayList<SatelliteSystem> SSs) {
		super(context);
		mGLSurfaceView = new GLSurfaceView(context);
        GLR = new GlRenderer(context,SSs);
        mGLSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        mGLSurfaceView.setRenderer(GLR);
        mGLSurfaceView.getHolder().setFormat(PixelFormat.RGBA_8888);
        mGLSurfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        mGLSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
        this.addView(mGLSurfaceView);
	}

	public void destroy(){
		mGLSurfaceView.destroyDrawingCache();
		mGLSurfaceView.setVisibility(View.GONE);
	}

	public void Stop(){
        mGLSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
	}
	
	public void Start(){
        mGLSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
	}
	
}
