package com.vuzix.main.menu;

import java.util.ArrayList;
import java.util.List;

import com.vuzix.modeles.Appli;
import com.vuzix.tools.Params;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

public class LaunchAppli extends BaseActivity{
	private static String TAG_LOCAL = "LaunchAppli - ";
	private static boolean fgDebugLocal = true;
	
	private Intent intent;
	private ListView lvAppli;
	private ArrayList<Appli> listAppli = new ArrayList<Appli>();
	private AppliAdapter appliAdapter;
	
	private int posRef = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		if (Params.TAG_FG_DEBUG && fgDebugLocal){Log.i(Params.TAG_GEN, TAG_LOCAL + "start LaunchAppli");};
		final PackageManager pm = getPackageManager();
		
		//appliAdapter = new AppliAdapter(context, R.layout.ligne_appli, listAppli);
		//get a list of installed apps.       
	    List<PackageInfo> packs = getPackageManager().getInstalledPackages(0);
	    
	    listAppli.clear();
	    //appliAdapter.clear();
	    for(int Cpt=0; Cpt<packs.size(); Cpt++) {
	        PackageInfo packageInfo = packs.get(Cpt);
	        String nameAppli = packageInfo.applicationInfo.loadLabel(getPackageManager()).toString();
	        //if (Params.TAG_FG_DEBUG && fgDebugLocal){Log.i(Params.TAG_GEN, TAG_LOCAL + "appname = " + nameAppli);};
	        String namePack = packageInfo.packageName;
	        //if (Params.TAG_FG_DEBUG && fgDebugLocal){Log.i(Params.TAG_GEN, TAG_LOCAL + "package = " + namePack);};
	        //if (!namePack.contains("android") && !namePack.contains("mediatek") && !namePack.contains("google")){
	        if (!namePack.contains("android") && !namePack.contains("mediatek")){
	        	//if (!appliExclue(namePack)){
	        		//listInfo.add(nameAppli);
	        		Appli appli = new Appli(nameAppli, namePack);
	        		listAppli.add(appli);
	        		//appliAdapter.add(appli);
	        		
	        	//}
	        }
	    }	
	    appliAdapter = new AppliAdapter(context, R.layout.ligne_appli, listAppli);
		
	}
	private boolean appliExclue(String strPack){
		for (int Cpt = 0; Cpt < Params.APP_EXCEPTION.length; Cpt++){
			if (strPack.equalsIgnoreCase(Params.APP_EXCEPTION[Cpt])){
				return true;
			}
		}
		return false;
	}
	
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		event.startTracking();
	      // Capture our buttons' keyDown events so the OS doesn't take them and do something we don't want
		switch (keyCode){
			case KeyEvent.KEYCODE_VOLUME_UP:
				if (Params.TAG_FG_DEBUG && fgDebugLocal){Log.i(Params.TAG_GEN, TAG_LOCAL + "KEYCODE_VOLUME_UP");};
				if (Params.TAG_FG_DEBUG && fgDebugLocal){Log.i(Params.TAG_GEN, TAG_LOCAL + "posRef = " + posRef);};
				if (posRef == 0){
					if (camera != null){
			    		camera.release();
			    		camera = null;
			    	}
					intent = new Intent(LaunchAppli.this, GPS.class);
					startActivity(intent);
					finish();
				}else{
					posRef--;
					Appli appli = appliAdapter.getItem(posRef);
					appliAdapter.setSelectedState(posRef);
					appliAdapter.notifyDataSetChanged();
					
					
					//lvAppli.setBackgroundColor(getResources().getColor(R.color.bleu));
				}
		
				return true;
			case KeyEvent.KEYCODE_VOLUME_DOWN:
				if (Params.TAG_FG_DEBUG && fgDebugLocal){Log.i(Params.TAG_GEN, TAG_LOCAL + "KEYCODE_VOLUME_DOWN");};
				if (Params.TAG_FG_DEBUG && fgDebugLocal){Log.i(Params.TAG_GEN, TAG_LOCAL + "posRef = " + posRef);};
				//attention l orde des if est important pour les bornes
				if (posRef >= lvAppli.getCount()){
					if (camera != null){
			    		camera.release();
			    		camera = null;
			    	}
					intent = new Intent(LaunchAppli.this, ConnexionBluetooth.class);
					startActivity(intent);
					finish();
				}
				if (posRef <= lvAppli.getCount() - 1){
					Appli appli = appliAdapter.getItem(posRef);
					appliAdapter.setSelectedState(posRef);
					appliAdapter.notifyDataSetChanged();
					posRef++;
				}
				
				
				
				return true;
			case KeyEvent.KEYCODE_BACK:
				if (Params.TAG_FG_DEBUG && fgDebugLocal){Log.i(Params.TAG_GEN, TAG_LOCAL + "KEYCODE_BACK");};
				if (camera != null){
		    		camera.release();
		    		camera = null;
		    	}
				intent = new Intent(LaunchAppli.this, GPS.class);
				startActivity(intent);
				finish();
				return true;
		}     
		return false;
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		activeCamera();
	}
	
	private void activeCamera(){
    	camera = getCameraInstance();
        try {
	        if (camera != null){
	        	frameLayout = new FrameLayout(this);
	        	frameLayout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
	            
	            // Create a new camera view and add it to the layout
	        	preview = new Preview(this,camera);
	            frameLayout.addView(preview);
	            
	            lvAppli = new ListView(this);
	            lvAppli.setPadding(100, 10, 10, 10);
	            lvAppli.setAdapter(appliAdapter);
	            if (Params.TAG_FG_DEBUG && fgDebugLocal){Log.i(Params.TAG_GEN, TAG_LOCAL + "nbr = " + lvAppli.getCount());};
	            
	            frameLayout.addView(lvAppli);
	            setContentView(frameLayout);
	        }
	        else {
	        	Toast toast = Toast.makeText(getApplicationContext(),  getResources().getString(R.string.error_camera_instance), Toast.LENGTH_SHORT);
	        	toast.show();
	        	finish();
	        }
        } catch (Exception e) {
        	if (Params.TAG_FG_DEBUG && fgDebugLocal){Log.i(Params.TAG_GEN, TAG_LOCAL + "Load error = " + e.getMessage());};
        	if (camera != null){
        		camera.release();
        		camera = null;
        	}
		}
    }
}
