package com.yenwie.world.gnss;

import java.util.ArrayList;

import com.example.gnss.main.R; 
import com.yenwie.world.gnss.satellite.OrbitValue;
import com.yenwie.world.gnss.satelliteView.GlRenderer;
import com.yenwie.world.gnss.satelliteView.Quad;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ControlLayout {

	View view;
	public static ArrayList<RelativeLayout> onoffs = new ArrayList<RelativeLayout>();
	public int satelliteid, orbitid;
	
	public ControlLayout(Activity activity, int orbitid, int satelliteid, OrbitValue orbit, final OnClickListener click, boolean visibility){
		this.satelliteid = satelliteid;
		this.orbitid = orbitid;
		
		view = activity.getLayoutInflater().inflate(R.layout.contollayout, null);
		view.setId(satelliteid);
		
		TextView cl_title = (TextView)view.findViewById(R.id.cl_title);
		cl_title.setText(orbit.getOrbitTitle()+"_"+(satelliteid+1));
		
		RelativeLayout onoff = (RelativeLayout)view.findViewById(R.id.onoff);
		onoff.setId(satelliteid);
		onoff.setOnClickListener(click);
		onoffs.add(onoff);
		
		//visibility state
		if(visibility==true)
			onoff.setTag("off");
		else
			onoff.setTag("on");
		switchonoff(onoff);
	}
	
	public Quad getSatellite(){
		return GlRenderer.satellites[0][orbitid][satelliteid];
	}
	
	public View getView(){
		return view;
	}
	
	public void Clear(){
		onoffs.clear();
	}
	
	public static void switchon(View v){
		v.setBackgroundResource(R.drawable.on);
	}
	public static void switchoff(View v){
		v.setBackgroundResource(R.drawable.off);
	}
	public static void switchonoff(View v){
		if(v.getTag().equals("on")){
			v.setBackgroundResource(R.drawable.off);
			v.setTag("off");
		}
		else if(v.getTag().equals("off")){
			v.setBackgroundResource(R.drawable.on);
			v.setTag("on");
		}
	}
	
	public static void resetonoff(View v){

		for(int i=0;i<onoffs.size();i++){
			switchoff(onoffs.get(i));
		}
		switchon(onoffs.get(v.getId()));
		
	}
	
}
