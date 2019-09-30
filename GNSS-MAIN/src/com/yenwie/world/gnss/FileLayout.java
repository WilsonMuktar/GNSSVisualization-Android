package com.yenwie.world.gnss;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Color;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.gnss.main.R;
import com.joanzapata.pdfview.PDFView;
import com.joanzapata.pdfview.util.FileUtils;
import com.warnyul.android.widget.FastVideoView;

public class FileLayout {

	Activity activity;
	PDFView pdfpanel;
	final FastVideoView videopanel;
	ArrayList<String> fileintype;
	ArrayList<LinearLayout> filebases;
	public FileLayout(
			Activity act, 
			ArrayList<String> fileintype, 
			ArrayList<LinearLayout> filebases,
			PDFView pdfpanel, 
			final FastVideoView videopanel){
		activity = act;
		this.fileintype = fileintype;
		this.filebases = filebases;
		this.pdfpanel = pdfpanel;
		this.videopanel = videopanel;
	}
	
	public View plus(int i,String type, final int[] hundred) throws IOException{
		View inf = activity.getLayoutInflater().inflate(R.layout.file_layout, null);
		final LinearLayout filebase = (LinearLayout)inf.findViewById(R.id.Filebase);
		ImageView filelogo = (ImageView)inf.findViewById(R.id.file_typelogo);
		TextView filetitle = (TextView)inf.findViewById(R.id.file_title);
		TextView fileinfo  = (TextView)inf.findViewById(R.id.file_info);
		
		if(type.equals("document"))
			filelogo.setBackgroundResource(R.drawable.icon_pdf);
		else if(type.equals("video"))
			filelogo.setBackgroundResource(R.drawable.icon_video);
			
		filetitle.setText(fileintype.get(i).replace(type+"--", ""));
//		File f = FileUtils.fileFromAsset(activity.getApplicationContext(), fileintype.get(i));
		fileinfo.setText("");//(f.getTotalSpace()/(1024*1024*1024))+"MB");
		inf.setLeft(10);
		
		filebase.setId(i);
		filebases.add(filebase);
		
		if(type.equals("document"))
			filebase.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					for(int j=0;j<filebases.size();j++){
						if(j==v.getId())
							filebases.get(j).setBackgroundColor(Color.argb(50, 255, 255, 255));
						else
							filebases.get(j).setBackgroundColor(Color.TRANSPARENT);
					}
					
					
			        pdfpanel.fromAsset(fileintype.get(v.getId()))
				        .pages(hundred)
				        .defaultPage(1)
				        .showMinimap(false)
				        .enableSwipe(true)
				        .onDraw(null)//onDrawListener())
				        .onLoad(null)//onLoadCompleteListener)
				        .onPageChange(null)//onPageChangeListener)
				        .load();
				}
			});
		else if(type.equals("video"))
			filebase.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					for(int j=0;j<filebases.size();j++){
						if(j==v.getId())
							filebases.get(j).setBackgroundColor(Color.argb(50, 255, 255, 255));
						else
							filebases.get(j).setBackgroundColor(Color.TRANSPARENT);
					}
					
					File videoFile;
					try {
						videoFile = FileUtils.fileFromAsset(activity.getApplicationContext(), fileintype.get(v.getId()));
		                videopanel.setVideoURI(Uri.fromFile(videoFile));  
		                videopanel.requestFocus();
		                videopanel.start();
		            } catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
	
		return inf;
	}
}
