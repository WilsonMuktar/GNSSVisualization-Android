package com.yenwie.world.gnss;

import com.example.gnss.main.SecondActivity;
import com.yenwie.world.gnss.satelliteView.GlRenderer;

import android.graphics.PointF;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class SSsimulationListener implements OnTouchListener {
		
	float scale,oldDist = 0;
	PointF start = new PointF(), mid = new PointF();
	int mode = 0;
	static final int NONE = 0;
	static final int DRAG = 1;
	static final int ZOOM = 2;
	int totalfinger = 0;	
	
	@Override
	public boolean onTouch(View arg0, MotionEvent event) {

		// Handle touch events here...
		switch (event.getAction() & MotionEvent.ACTION_MASK) 
		{
			// first finger down only
			case MotionEvent.ACTION_DOWN: 
				start.set(event.getX(), event.getY());
				mode = DRAG;
				totalfinger = 1;
				//SecondActivity.print("Finger1");
				return true;
				
			// first finger lifted
			case MotionEvent.ACTION_UP:  
			// second finger lifted
			case MotionEvent.ACTION_POINTER_UP:
				mode = NONE;
				totalfinger = 0;
				break;
				
			// second finger down
			case MotionEvent.ACTION_POINTER_DOWN: 
				totalfinger = 2;
				oldDist = spacing(event); // calculates the distance between two points where user touched.
				
				// minimal distance between both the fingers
				if (oldDist > 200f) {
					midPoint(mid, event); // sets the mid-point of the straight line between two points where user touched.
					mode = ZOOM;
				}else mode = DRAG;
				return true;
	
			case MotionEvent.ACTION_MOVE:
				if (mode == DRAG) 
				{ 
					// movement of one finger
					if(totalfinger==1)
					{
						//X-axis
						if(Math.abs(event.getX()-start.x)>Math.abs(event.getY()-start.y))
						{
							if(event.getX()-start.x>=5){//to right
								GlRenderer.rotateY( event.getX()-start.x );
							}
							else if(event.getX()-start.x<=-5){//to left
								GlRenderer.rotateY( (event.getX()-start.x) );
							}
						}
						else
						{
							if(event.getY()-start.y>=5){//to top
								GlRenderer.rotateZ( event.getY()-start.y );
							}
							else if(event.getY()-start.y<=-5){//to down
								GlRenderer.rotateZ( (event.getY()-start.y) );
							}
						}
					}
					else if(totalfinger==2)
					{
						//X-axis
						if(Math.abs(event.getX()-start.x)>Math.abs(event.getY()-start.y))
						{
							GlRenderer.move(event.getX()-start.x, 0);
						}
						else
						{
							GlRenderer.move(0,event.getY()-start.y);
						}
					}
				}
				else if (mode == ZOOM) 
				{ 
					// movement of two finger pinch zooming
					float newDist = spacing(event);
					if (newDist > 5f) {
						scale = newDist / oldDist; // scale of distance
						if(scale>1){
							GlRenderer.zoom(1);
						}
						else{
							GlRenderer.zoom(-1);
						}
					}
				}
				return true;
		}

		return false; // indicate event not keep continuous
	}

	private float spacing(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return FloatMath.sqrt(x * x + y * y);
	}

	private void midPoint(PointF point, MotionEvent event) {
		float x = event.getX(0) + event.getX(1);
		float y = event.getY(0) + event.getY(1);
		point.set(x / 2, y / 2);
	}
 
}
