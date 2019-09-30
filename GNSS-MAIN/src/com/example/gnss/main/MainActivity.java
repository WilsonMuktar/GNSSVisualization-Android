package com.example.gnss.main;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
 
public class MainActivity extends Activity {

	String currentSS = "Beidou";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//FULLSCREEN
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        setContentView(R.layout.activity_main);
         
        ImageView includebeidoucard = (ImageView) this.findViewById(R.id.includebeidoucard);
        includebeidoucard.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent secondmain = new Intent();
				secondmain.setClass(getApplicationContext(), SecondActivity.class);
					Bundle b = new Bundle();
					b.putInt("SatelliteSystem", 1); //Your id
					secondmain.putExtras(b);
				startActivity(secondmain);
			}
        });
        
        ImageView includegpscard = (ImageView) this.findViewById(R.id.includegpscard);
        includegpscard.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent secondmain = new Intent();
				secondmain.setClass(getApplicationContext(), SecondActivity.class);	Bundle b = new Bundle();
					b.putInt("SatelliteSystem", 2); //Your id
					secondmain.putExtras(b);
				startActivity(secondmain);
			}
        });

        ImageView includeglonasscard = (ImageView) this.findViewById(R.id.includeglonasscard);
        includeglonasscard.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent secondmain = new Intent();
				secondmain.setClass(getApplicationContext(), SecondActivity.class);	Bundle b = new Bundle();
					b.putInt("SatelliteSystem", 3); //Your id
					secondmain.putExtras(b);
				startActivity(secondmain);
			}
        });

        ImageView includegalileocard = (ImageView) this.findViewById(R.id.includegalileocard);
        includegalileocard.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent secondmain = new Intent();
				secondmain.setClass(getApplicationContext(), SecondActivity.class);	Bundle b = new Bundle();
					b.putInt("SatelliteSystem", 4); //Your id
					secondmain.putExtras(b);
				startActivity(secondmain);
			}
        });

        ImageView include4mergedcard = (ImageView) this.findViewById(R.id.include4mergedcard);
        include4mergedcard.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent secondmain = new Intent();
				secondmain.setClass(getApplicationContext(), SecondActivity.class);	Bundle b = new Bundle();
					b.putInt("SatelliteSystem", 5); //Your id
					secondmain.putExtras(b);
				startActivity(secondmain);
			}
        });

        ImageView includemeodifferencecard = (ImageView) this.findViewById(R.id.includemeodifferencecard);
        includemeodifferencecard.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent secondmain = new Intent();
				secondmain.setClass(getApplicationContext(), SecondActivity.class);	Bundle b = new Bundle();
					b.putInt("SatelliteSystem", 6); //Your id
					secondmain.putExtras(b);
				startActivity(secondmain);
			}
        });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
