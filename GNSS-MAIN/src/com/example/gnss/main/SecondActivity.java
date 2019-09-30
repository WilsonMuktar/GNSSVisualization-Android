package com.example.gnss.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
  
import com.joanzapata.pdfview.PDFView;
import com.joanzapata.pdfview.util.FileUtils;
import com.warnyul.android.widget.FastVideoView;
import com.yenwie.world.gnss.ButtonLayout;
import com.yenwie.world.gnss.ColorPickerDialog;
import com.yenwie.world.gnss.ControlLayout;
import com.yenwie.world.gnss.FileLayout;
import com.yenwie.world.gnss.SSsimulationListener;
import com.yenwie.world.gnss.satellite.OrbitValue;
import com.yenwie.world.gnss.satellite.ParseXML;
import com.yenwie.world.gnss.satellite.SatelliteSystem;
import com.yenwie.world.gnss.satelliteView.GLsatellitesystemView;
import com.yenwie.world.gnss.satelliteView.GlRenderer;
import com.yenwie.world.gnss.satelliteView.Quad;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.*;
import android.widget.SeekBar.OnSeekBarChangeListener;
import at.markushi.ui.ActionView;
import at.markushi.ui.RevealColorView;
import at.markushi.ui.action.BackAction;
import at.markushi.ui.action.DrawerAction;

public class SecondActivity extends Activity {

	/*initialize*/
	public static Context context;
	boolean open = false;
	int[] hundred = new int[100];
	String currentSystem = "xx";
	public static int orientation;//0potrait 3landscape
	
	/*Speed scale value*/
	public static int scale = 10;
	
	/*visibility variable*/
    public static boolean vis_all = false;
    public static boolean vis_fill = false;
    public static boolean vis_radius = false;
    public static boolean vis_orbit = false;
    public static boolean vis_satellite = false;

    /*Components*/
    RelativeLayout 
		leftpanel,
		rightpanel,
		splitpanel,
		spinnerpanel,
		menu_document,
		menu_video,
		menu_launch,
		menu_setting,
		set_control,
		set_display,
		RevealColorLayout;

	View 
		settingpanel;
	
	LinearLayout	
		menupanel,
		selectionpanel,
		set_menu,
		set_control_onoff,
		set_control_display,
		sp_setting,
		bottompanel;
		
	ImageView
		splitlogo,
		spinner_logo,
		sd_speed_img,
		sd_fill_img,
		sd_radius_img,
		sd_satellite_img,
		sd_orbit_img,
		sc_display_color,
		sc_display_icon,
		sc_display_reset;
	
	TextView
		spinner_name,
		menu_document_txt,
		menu_video_txt,
		menu_launch_txt,
		menu_setting_txt,
		set_systemtitle,
		sd_speed_value;
		;
	
	SeekBar
		sd_speed_slider;

	ActionView FullscreenView;
	boolean isFullscreen = false;
	
	WebView webpanel;
	FastVideoView videopanel;
	PDFView pdfpanel;
	RelativeLayout GNSSview;
	
	ParseXML px;

	public static GLsatellitesystemView GLVIEW;
	public final int blurcolor = Color.argb(20, 255, 255, 255);
	
	Button rightpanel_rotate;
	
	@SuppressWarnings("deprecation")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        //overridePendingTransition(R.layout.leftpanel_in,R.layout.rightpanelout);
        
        //FULLSCREEN
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
        orientation = this.getWindowManager().getDefaultDisplay().getOrientation();
        //print(orientation+"");
        setContentView(R.layout.activity_second);

		//Important Parameter selecting satellitesystem to visualize
		getParameterfromMainActivity();
		
        //INITIALIZING all components...
        //leftpanel
        leftpanel = (RelativeLayout)this.findViewById(R.id.leftpanel);
        spinnerpanel = (RelativeLayout)this.findViewById(R.id.spinnerpanel);
        spinner_name = (TextView)this.findViewById(R.id.spinner_name);
        	spinner_name.setText(currentSystem);
        splitpanel = (RelativeLayout)this.findViewById(R.id.splitpanel);
        	splitpanel.setOnClickListener(c);
        selectionpanel = (LinearLayout)this.findViewById(R.id.selectionpanel);
        bottompanel = (LinearLayout)this.findViewById(R.id.bottompanel);
        
        //Animate FULLSCREEN button
        FullscreenView = new ActionView(this);
        if(orientation==0)
        	FullscreenView.setRotation(90);
        else
        	FullscreenView.setRotation(0);
        FullscreenView.setAction(new BackAction());
        FullscreenView.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(isFullscreen==false){
			        FullscreenView.setAction(new DrawerAction());
			        if(orientation==0)
			        	FullscreenView.setRotation(-90);
			        else
			        	FullscreenView.setRotation(180);
					FullscreenView.setAction(
							new DrawerAction(),
							new BackAction(),
							ActionView.ROTATE_COUNTER_CLOCKWISE,
							(long)500);
					Collapse();
					isFullscreen = true;
				}
				else{
			        FullscreenView.setAction(new DrawerAction());
			        if(orientation==0)
			        	FullscreenView.setRotation(90);
			        else
			        	FullscreenView.setRotation(0);
		        	FullscreenView.setAction(
							new DrawerAction(),
							new BackAction(),
							ActionView.ROTATE_CLOCKWISE,
							(long)500);
					Expand();
					isFullscreen = false;
				}
			}
        });
        splitpanel.addView(FullscreenView);
        
        menupanel = (LinearLayout)this.findViewById(R.id.menupanel);
        menu_document = (RelativeLayout)this.findViewById(R.id.Menu_Document);
        	menu_document.setOnClickListener(c);
        menu_video = (RelativeLayout)this.findViewById(R.id.Menu_Video);
        	menu_video.setOnClickListener(c);
        menu_launch = (RelativeLayout)this.findViewById(R.id.Menu_Launch);
        	menu_launch.setOnClickListener(c);
        menu_setting = (RelativeLayout)this.findViewById(R.id.Menu_Setting);
        	menu_setting.setOnClickListener(c);
        
        sp_setting = (LinearLayout)this.findViewById(R.id.sp_setting);
        
        /*Setting panel*/
        settingpanel = getLayoutInflater().inflate(R.layout.settingpanel, null);
        set_systemtitle = (TextView) settingpanel.findViewById(R.id.set_systemtitle);
        	set_systemtitle.setText(currentSystem);
        set_menu = (LinearLayout) settingpanel.findViewById(R.id.set_menu);
        set_control = (RelativeLayout) settingpanel.findViewById(R.id.set_control);
        set_display = (RelativeLayout) settingpanel.findViewById(R.id.set_display);
        set_control_onoff = (LinearLayout) settingpanel.findViewById(R.id.set_control_onoff);
        sd_speed_img = (ImageView)settingpanel.findViewById(R.id.sd_speed_img);
        sd_speed_value= (TextView)settingpanel.findViewById(R.id.sd_speed_value);
        sd_speed_slider= (SeekBar)settingpanel.findViewById(R.id.sd_speed_slide);
       		sd_speed_slider.setProgress(scale);
       		sd_speed_slider.setMax(10);
       		sd_speed_value.setText(scale*10+"%");
        	sd_speed_slider.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){
				@Override
				public void onProgressChanged(SeekBar arg0, int arg1,
						boolean arg2) {
					// TODO Auto-generated method stub
					sd_speed_value.setText(arg0.getProgress()*10+"%");
					scale = arg0.getProgress();
				}
				@Override
				public void onStartTrackingTouch(SeekBar arg0) {
					// TODO Auto-generated method stub
					
				}
				@Override
				public void onStopTrackingTouch(SeekBar arg0) {
					// TODO Auto-generated method stub
					
				}
        	});
        sd_fill_img = (ImageView)settingpanel.findViewById(R.id.sdd_fill_img);
        	sd_fill_img.setId(0);sd_fill_img.setOnClickListener(c);
        sd_radius_img = (ImageView)settingpanel.findViewById(R.id.sdd_radius_img);
        	sd_radius_img.setId(0);sd_radius_img.setOnClickListener(c);
        sd_satellite_img = (ImageView)settingpanel.findViewById(R.id.sdd_satellite_img);
        	sd_satellite_img.setId(0);sd_satellite_img.setOnClickListener(c);
        sd_orbit_img = (ImageView)settingpanel.findViewById(R.id.sdd_orbit_img);
        	sd_orbit_img.setId(0);sd_orbit_img.setOnClickListener(c);
       
        set_control_display = (LinearLayout)settingpanel.findViewById(R.id.set_control_display);
        	set_control_display.setVisibility(View.GONE);
        sc_display_color = (ImageView)settingpanel.findViewById(R.id.sc_display_color);
        	sc_display_color.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					ColorPickerDialog dialog = new ColorPickerDialog(context, GlRenderer.orbitcolors[0][currentorbit],
			                getResources().getString(R.string.app_name),
			                new ColorPickerDialog.OnColorChangedListener() {
			                    @Override
			                    public void colorChanged(int color) {
			                        // TODO Auto-generated method stub
			    					changecolorbutton(sc_display_color,color);
									//print(currentorbit+"");
			    					GlRenderer.orbitcolors[0][currentorbit] = color;
			                    }
					});
//					dialog.withDuration(700);   
//				    dialog.withEffect(Effectstype.RotateBottom);
					dialog.show();
				}
			});
        sc_display_icon = (ImageView)settingpanel.findViewById(R.id.sc_display_icon);
        sc_display_reset = (ImageView)settingpanel.findViewById(R.id.sc_display_reset);
        
        //rightpanel
        rightpanel = (RelativeLayout)this.findViewById(R.id.rightpanel);
        webpanel = (WebView)this.findViewById(R.id.webpanel);
        pdfpanel = (PDFView)this.findViewById(R.id.pdfview);
        videopanel = (FastVideoView)this.findViewById(R.id.videopanel);
        videopanel.setMediaController(new MediaController(this));   
        GNSSview = (RelativeLayout)this.findViewById(R.id.GNSSview);
        rightpanel_rotate = (Button)this.findViewById(R.id.rightpanel_rotate);
	        rightpanel_rotate.setVisibility(View.GONE);
	        rightpanel_rotate.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View arg0) {
			        rotaterightpanel();
				}
	        });
        
        //hundred
        for(int i=0;i<50;i++)
        	hundred[i] = i;
        
        //OnStart
		menu_setting.setBackgroundColor(blurcolor);
		resetrightpanel();
		GNSSview.setVisibility(View.VISIBLE);
		rightrotatevisible();
		addSettingpanel(px.getSSs());
		
		//revealView
		RevealColorView rh = new RevealColorView(this,null,0);
		rh.reveal(1000, 1000, blurcolor, 1, 1000 ,null);
		RevealColorLayout = (RelativeLayout)this.findViewById(R.id.RevealColorLayout);
		RevealColorLayout.addView(rh);
		
		//Dialog
//		NiftyDialogBuilder dialog = new NiftyDialogBuilder(this);
//		dialog.withEffect(Effectstype.RotateBottom);
//		dialog.show();

		//copy files from assets
		copyfromasset("launch");
	}

	public void getParameterfromMainActivity(){
		//from intent of mainactivity
        try{
	        //from main gnss activity
	        Bundle b = getIntent().getExtras();
	        int value = b.getInt("SatelliteSystem");
	        if(value==1){
	        	//Toast.makeText(getApplicationContext(), "BEIDOU", Toast.LENGTH_SHORT).show();
	        	currentSystem = "BEIDOU";
	        }
	        else if(value==2){
	        	//Toast.makeText(getApplicationContext(), "GPS", Toast.LENGTH_SHORT).show();
	        	currentSystem = "GPS";
	        }
	        else if(value==3){
	        	//Toast.makeText(getApplicationContext(), "GLONASS", Toast.LENGTH_SHORT).show();
	        	currentSystem = "GLONASS";
	        }
	        else if(value==4){
	        	//Toast.makeText(getApplicationContext(), "GALILEO", Toast.LENGTH_SHORT).show();
	        	currentSystem = "GALILEO";
	        }
	        else if(value==5){
	        	//Toast.makeText(getApplicationContext(), "4Merged", Toast.LENGTH_SHORT).show();
	        	currentSystem = "4Merged";
	        }
	        else if(value==6){
	        	//Toast.makeText(getApplicationContext(), "MEOdifferences", Toast.LENGTH_SHORT).show();
	        	currentSystem = "MEOdifferences";
	        }
	        
	        /*
	         * READ XML configuration file for specific SS
	         * */
	        try {
				if((FileUtils.fileFromAsset(getApplicationContext(), ("SS_"+currentSystem+".xml"))).exists())
				{
					px = new ParseXML();
					px.Parse(getResources().getAssets().open("SS_"+currentSystem+".xml"));
					
				}
				else{
					printUI("System not available!");
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }catch(Exception e){}
	}
	
    OnClickListener c = new click();
	public class click implements OnClickListener{
		@Override
		public void onClick(View v) {
			if(v==splitpanel){
				if(open==true){
					open = false;
					Expand();
				}else{
					open = true;
					Collapse();
				}
			}
			else if(v==menu_document){
				removeview();
				menu_document.setBackgroundColor(blurcolor);
				menu_video.setBackgroundColor(Color.TRANSPARENT);
				menu_launch.setBackgroundColor(Color.TRANSPARENT);
				menu_setting.setBackgroundColor(Color.TRANSPARENT);

				resetrightpanel();
				pdfpanel.setVisibility(View.VISIBLE);
				rightrotatevisible();
		        addFilepanel("document");
			}
			else if(v==menu_video){
				removeview();
				menu_document.setBackgroundColor(Color.TRANSPARENT);
				menu_video.setBackgroundColor(blurcolor);
				menu_launch.setBackgroundColor(Color.TRANSPARENT);
				menu_setting.setBackgroundColor(Color.TRANSPARENT);

				resetrightpanel();
				videopanel.setVisibility(View.VISIBLE);
				rightrotatevisible();
		        addFilepanel("video");
			}
			else if(v==menu_launch){
				removeview();
				menu_document.setBackgroundColor(Color.TRANSPARENT);
				menu_video.setBackgroundColor(Color.TRANSPARENT);
				menu_launch.setBackgroundColor(blurcolor);
				menu_setting.setBackgroundColor(Color.TRANSPARENT);

				resetrightpanel();
				webpanel.setVisibility(View.VISIBLE);
				if(new File(Environment.getExternalStorageDirectory()
						+"/GNSS/Assets/launch/launch-"+currentSystem.toLowerCase()+".html").exists()){
					webviewer(
						"file:///"+Environment.getExternalStorageDirectory()+"/GNSS/Assets/launch/"
						,"launch-"+currentSystem.toLowerCase()+".html");
				}
				else
					print("not exist");
				rightrotatevisible();
			}
			else if(v==menu_setting){
				removeview();
				menu_document.setBackgroundColor(Color.TRANSPARENT);
				menu_video.setBackgroundColor(Color.TRANSPARENT);
				menu_launch.setBackgroundColor(Color.TRANSPARENT);
				menu_setting.setBackgroundColor(blurcolor);			

				resetrightpanel();
				GNSSview.setVisibility(View.VISIBLE);
				rightrotatevisible();
				addSettingpanel(px.getSSs());
			}
			
			//display controller
			else if(v==sd_fill_img){
				switchonoff(v);
				vis_fill=!vis_fill;
			}
			else if(v==sd_radius_img){
				switchonoff(v);
				vis_radius=!vis_radius;
			}
			else if(v==sd_satellite_img){
				switchonoff(v);
				
			}
			else if(v==sd_orbit_img){
				switchonoff(v);
			}
		}
	}
	
	public void rotaterightpanel(){
		if(pdfpanel.getVisibility()==View.VISIBLE)
			pdfpanel.setRotation(pdfpanel.getRotation()+90);
		else if(videopanel.getVisibility()==View.VISIBLE)
			videopanel.setRotation(videopanel.getRotation()+90);
		else if(webpanel.getVisibility()==View.VISIBLE)
			webpanel.setRotation(webpanel.getRotation()+90);
	}
	
	public void resetrightpanel(){
		//rightpanel visibility to off
		pdfpanel.setVisibility(View.GONE);
		if(videopanel.isPlaying())
			videopanel.pause();
		videopanel.setVisibility(View.GONE);
		webpanel.setVisibility(View.GONE);
		GNSSview.setVisibility(View.GONE);
		set_control_display.setVisibility(View.GONE);
		if(GNSSview!=null) GNSSview.removeAllViews();
		vis_all = false;
	}
	
	public void rightrotatevisible(){
		//rotate button at rightpanel
		if(pdfpanel.getVisibility()==View.VISIBLE||videopanel.getVisibility()==View.VISIBLE||
			webpanel.getVisibility()==View.VISIBLE)
			rightpanel_rotate.setVisibility(View.VISIBLE);
		else
			rightpanel_rotate.setVisibility(View.GONE);
	}
	
	public void switchonoff(View v){
		if(v.getId()==0){
			v.setBackgroundResource(R.drawable.on);
			v.setId(1);
		}
		else if(v.getId()==1){
			v.setBackgroundResource(R.drawable.off);
			v.setId(0);
		}
	}
	
	public void removeview(){
		selectionpanel.removeAllViews();
		sp_setting.removeAllViews();
	}
	
	//Animation 
	@SuppressWarnings("deprecation")
	public void Expand(){
		
		if(getResources().getConfiguration().orientation==Configuration.ORIENTATION_LANDSCAPE)
		{
			leftpanel.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),
		            R.anim.leftpanel_in_x));
			leftpanel.setX(50);
			
			RelativeLayout.LayoutParams params = 
				new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.MATCH_PARENT,
					RelativeLayout.LayoutParams.MATCH_PARENT);
		    params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
	        rightpanel.setLayoutParams(params);
		    rightpanel.getLayoutParams().width = 
					getWindowManager().getDefaultDisplay().getWidth()-leftpanel.getWidth()-50;
			rightpanel.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),
					R.anim.rightpanel_out_x));
		}
		else if(getResources().getConfiguration().orientation==Configuration.ORIENTATION_PORTRAIT)
		{
			leftpanel.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),
		            R.anim.leftpanel_in_y));
			leftpanel.setY(50);
			
			RelativeLayout.LayoutParams params = 
				new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.MATCH_PARENT,
					RelativeLayout.LayoutParams.MATCH_PARENT);
		    params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
	        rightpanel.setLayoutParams(params);
		    rightpanel.getLayoutParams().height = 
					getWindowManager().getDefaultDisplay().getHeight()-leftpanel.getHeight()-50;
			rightpanel.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),
					R.anim.rightpanel_out_y));
		}
		
		menupanel.setVisibility(View.VISIBLE);
		bottompanel.setVisibility(View.VISIBLE);
		
	}
	
	@SuppressWarnings("deprecation")
	public void Collapse(){
		
		if(getResources().getConfiguration().orientation==Configuration.ORIENTATION_LANDSCAPE)
		{
			leftpanel.setX(-leftpanel.getWidth()+100);
			leftpanel.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),
		            R.anim.leftpanel_out_x));
			
			RelativeLayout.LayoutParams params = 
					new RelativeLayout.LayoutParams(
						RelativeLayout.LayoutParams.MATCH_PARENT,
						RelativeLayout.LayoutParams.MATCH_PARENT);
		    params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		    rightpanel.setLayoutParams(params);
		    rightpanel.getLayoutParams().width = 
					getWindowManager().getDefaultDisplay().getWidth()*90/100+80;
			rightpanel.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),
					R.anim.rightpanel_in_x));
		}
		else if(getResources().getConfiguration().orientation==Configuration.ORIENTATION_PORTRAIT)
		{
//			RelativeLayout.LayoutParams params1 = 
//					new RelativeLayout.LayoutParams(
//						RelativeLayout.LayoutParams.MATCH_PARENT,
//						RelativeLayout.LayoutParams.MATCH_PARENT);
//		    params1.addRule(RelativeLayout.ALIGN_PARENT_TOP);
//		    leftpanel.setLayoutParams(params1);
			leftpanel.setY(-leftpanel.getHeight()+140);
			leftpanel.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),
		            R.anim.leftpanel_out_y));
			
			RelativeLayout.LayoutParams params = 
					new RelativeLayout.LayoutParams(
						RelativeLayout.LayoutParams.MATCH_PARENT,
						RelativeLayout.LayoutParams.MATCH_PARENT);
		    params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		    rightpanel.setLayoutParams(params);
		    rightpanel.getLayoutParams().height = 
					getWindowManager().getDefaultDisplay().getHeight()*90/100+40;
			rightpanel.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),
					R.anim.rightpanel_in_y));
		}
		
		menupanel.setVisibility(View.GONE);
		bottompanel.setVisibility(View.GONE);
		
	}

	String[] files;
	ArrayList<LinearLayout> filebases;
	ArrayList<String> fileintype;
	public void addFilepanel(String type){
		//reset
		files = new String[]{};
		filebases = new ArrayList<LinearLayout>();
		fileintype = new ArrayList<String>();
		
		if(type.equals("video")&&videopanel.isPlaying())
			videopanel.pause();
		
		try {
			AssetManager am = SecondActivity.this.getResources().getAssets();
			files = am.list("");

			//only that type
			for(int i=0;i<files.length;i++){
				try{
					if(files[i].toLowerCase().split("--")[0].equals(type)){
						fileintype.add(files[i]);
					}
				}catch(Exception e){}
			}
			
			FileLayout fl = new FileLayout(
					SecondActivity.this, 
					fileintype,
					filebases, 
					pdfpanel, 
					videopanel);
			
			for(int i=0;i<fileintype.size();i++){
				selectionpanel.addView(fl.plus(i, type, hundred));
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	ControlLayout cl = null;
	public static int currentorbit=0;
	public void addSettingpanel(ArrayList<SatelliteSystem> ss){
		
//		NiftyDialogBuilder dialogBuilder=NiftyDialogBuilder.getInstance(this);
//		dialogBuilderc      
//		    .withTitle("Modal Dialog")
//		    .withMessage("This is a modal Dialog.")
////		    .withEffect(Effectstype.RotateBottom)
//		    .show();
		
		//reset
        vis_fill=false; sd_fill_img.setBackgroundResource(R.drawable.off);
        vis_radius=false; sd_radius_img.setBackgroundResource(R.drawable.off);
        vis_satellite=false; sd_satellite_img.setBackgroundResource(R.drawable.on);
        vis_orbit=false; sd_orbit_img.setBackgroundResource(R.drawable.on);
        GlRenderer.rotatebackY = 0;
        GlRenderer.rotatebackZ= 0;
        GlRenderer.mRotationAngleY=0;
        GlRenderer.mRotationAngleZ=0;
		
		vis_all = true;
		sp_setting.addView(settingpanel);
		
		// Title
		//print(ss.get(0).getTitle()+" "+ss.size());
		set_systemtitle.setText(ss.get(0).getTitle());
		
		for(int i=0;i<ss.size();i++){
			if( ss.get(i).getTitle().toLowerCase()
				.equals(spinner_name.getText().toString().toLowerCase()))
			{
				//RESET
				set_menu.removeAllViews();
				ButtonLayout.bls.clear();//clear [redraw menu]
				ControlLayout.onoffs.clear();//clear [redraw satellite onoff]
				set_control_onoff.removeAllViews();
				
				//Orbits [setting menu] GEO MEO1 MEO2 ...
				for(int j=0;j<ss.get(i).getOrbit().size();j++){
					final OrbitValue orbit = ss.get(i).getOrbit().get(j);
					final int Orbitid = j;
					ButtonLayout bl = new ButtonLayout(SecondActivity.this,j,orbit.getOrbitTitle(),new OnClickListener(){
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							ButtonLayout.resetcolor(v);
							ControlLayout.onoffs.clear();//clear [redraw satellite onoff]
							set_control_onoff.removeAllViews();
							set_control_display.setVisibility(View.VISIBLE);
							
							//keep current orbit
							currentorbit = Orbitid;
							//print(currentorbit+"");
							
							//ALL Satellites GEO_1 GEO_2 ...
							for(int k=0; k<orbit.getSatelliteCount();k++){
								final int Satelliteid = k;
								cl = new ControlLayout(SecondActivity.this,Orbitid,k,orbit,new OnClickListener(){
									@Override
									public void onClick(View v) {
										// TODO Auto-generated method stub
										ControlLayout.switchonoff(v);
										controlsatellite(v, GlRenderer.satellites[0][Orbitid][Satelliteid]);
									}
								},GlRenderer.satellites[0][Orbitid][Satelliteid].visibility);
								set_control_onoff.addView(cl.getView());
							}
							//space
							TextView tv = new TextView(context);
							tv.setText("");
							set_control_onoff.addView(tv);
							
							//Color control [ONCE every click tab GEO MEO1 MEO2 ... ]
							changecolorbutton(sc_display_color,GlRenderer.orbitcolors[0][currentorbit]);
							
						}
					});
					set_menu.addView(bl.getView());
				}

				//Rendering
				if(GNSSview.getChildCount()>0){
					GNSSview.removeAllViews();
				}
				GLVIEW = new GLsatellitesystemView(this, px.getSSs());
				GNSSview.addView(GLVIEW);
				GLVIEW.setOnTouchListener(new SSsimulationListener());
			}
		}
	}
	
	public void changecolorbutton(View v,int color){
		GradientDrawable myGrad = (GradientDrawable)v.getBackground();  
		myGrad.setColor(color);
	}
	
	public void controlsatellite(View v, Quad satellite){
		//Control Visibility of Satellite
		satellite.setvisibility(!satellite.visibility);
	}
	
	int filesAmount = 0;
	public void AssetSize(String subfolder){
		AssetManager assetManager = getAssets();
	    String assets[] = null;
	    try {
	        assets = assetManager.list(subfolder);
	        if (assets.length == 0) {
	            filesAmount++;             
	        } else {
	            for (int i = 0; i < assets.length; ++i) {
	            	AssetSize(subfolder + "/" + assets[i]);
	            }
	        }
	    } catch (IOException ex) {
	        ex.printStackTrace();
	    }
	}
	
	public void webviewer(String URL,String filename){
		webpanel.getSettings().setJavaScriptEnabled(true);
		webpanel.getSettings().setAllowFileAccess(true);
		webpanel.getSettings().setBuiltInZoomControls(true);
		webpanel.setWebChromeClient(new WebChromeClient());
		final ProgressDialog progressDialog = 
			ProgressDialog.show(SecondActivity.this, "Loading","Please wait", true);
			webpanel.setWebViewClient(new WebViewClient() {
		        @Override
		        public void onReceivedError(WebView view, int errorCode,
		                String description, String failingUrl) {
		            // TODO Auto-generated method stub
		        	print(description);
		            super.onReceivedError(view, errorCode, description, failingUrl);
		        }
		        @Override
		        public void onPageFinished(WebView view, String url) {
		            // TODO Auto-generated method stub
		            if (progressDialog.isShowing()) {
		                progressDialog.dismiss();
		            }
		            super.onPageFinished(view, url);
		        }
		    });
	    //*** used to read PDF files from docs.google.com
		//webpanel.loadUrl(URL+filename);
		webpanel.getSettings().setUseWideViewPort(true);
		webpanel.getSettings().setLoadWithOverviewMode(true);
		webpanel.loadDataWithBaseURL(
			URL,
			readFileAsString(Environment.getExternalStorageDirectory()+"/GNSS/Assets/launch/"+filename)
			, "text/html", "UTF-8", null);
	}
	
	public static String readFileAsString(String filePath){
		StringBuffer fileData = new StringBuffer(1000);
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(filePath));
			char[] buf = new char[1024];
			int numRead = 0;
			while ((numRead = reader.read(buf)) != -1) {
				String readData = String.valueOf(buf, 0, numRead);
				fileData.append(readData);
				buf = new char[1024];
			}
			reader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			print(e.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			print(e.toString());
		}
		return fileData.toString();
	}
	
	public void copyfromasset(String assetsfolder){
		
		//create main directory of SDcard assets
		File AssetonSDcard = new File(Environment.getExternalStorageDirectory()+"/GNSS/Assets/"+assetsfolder+"/");
		if(!AssetonSDcard.exists()){
			AssetonSDcard.mkdirs();
			if(!AssetonSDcard.exists())
				print(AssetonSDcard.getAbsolutePath()+"still not Exist");
		}
		
		//explore assets folder
		AssetManager assetManager = getAssets();
	    String assets[] = null;
	    try {
	        assets = assetManager.list(assetsfolder);
	    } catch (IOException ex) {
	        ex.printStackTrace();
	    }

	    //IF there are some update
	    print(AssetonSDcard.list().length+"<"+assets.length+"?");
	    if(AssetonSDcard.list().length<assets.length)
	    {
	    	ProgressDialog pd = new ProgressDialog(context);
	    	pd.setIndeterminate(true);
	    	pd.show();
		    //copy files from folder to sdcard
		    for(String filename : assets) {
		        InputStream in = null;
		        OutputStream out = null;
		        try {
		          in = assetManager.open(assetsfolder+"/"+filename);
		          File outFile = new File(AssetonSDcard, filename);
		          //print(outFile.getAbsolutePath());
		          out = new FileOutputStream(outFile);
		          	  //write file
			          byte[] buffer = new byte[1024];
			          int read;
			          while((read = in.read(buffer)) != -1){
			            out.write(buffer, 0, read);
			          }
		        }
		        catch(IOException e){print(e.toString());}
		        finally {
		            if (in != null) try {in.close();} catch (IOException e) {e.printStackTrace();}
		            if (out != null) try {out.close();} catch (IOException e) {e.printStackTrace();}
		        }  
		    }
		    pd.dismiss();
	    }
	}
	
	
	
	
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
	public static void print(String s){
		Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
	}
	
	public void printUI(final String s){
		runOnUiThread(new Runnable(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
			}
		});
	}

	
//	@Override
//	public 
}
