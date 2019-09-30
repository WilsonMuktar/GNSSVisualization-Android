package com.yenwie.world.gnss;

import java.util.ArrayList;

import com.example.gnss.main.R;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class ButtonLayout {

	public static ArrayList<View> bls = new ArrayList<View>();
	
	View bl;
	public ButtonLayout(Activity activity,int i,String title, OnClickListener click){
		bl = activity.getLayoutInflater().inflate(R.layout.button_layout, null);
			bl.setId(i);
		TextView bl_title = (TextView) bl.findViewById(R.id.bl_title);
			bl_title.setId(i);
			bl_title.setText(title);
			bl_title.setOnClickListener(click);
		bls.add(bl);
	}
	
	public View getView(){
		return bl;
	}
	
	public void Clear(){
		bls.clear();
	}
	
	public static void resetcolor(View v){

		for(int i=0;i<bls.size();i++){
			bls.get(i).setBackgroundColor(Color.TRANSPARENT);
		}
		bls.get(v.getId()).setBackgroundColor(Color.argb(50, 255, 255, 255));
		
	}
	
}
