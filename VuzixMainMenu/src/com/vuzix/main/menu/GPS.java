package com.vuzix.main.menu;

import java.text.DecimalFormat;

import com.vuzix.tools.Params;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManager.LayoutParams;
import android.widget.FrameLayout;
import android.widget.Toast;

public class GPS extends BaseActivity implements LocationListener{
	private static String TAG_LOCAL = "GPS - ";
	private static boolean fgDebugLocal = true;
	
	private Intent intent;
	private LocationManager locationManager;
	private boolean fgGPSActif = false;
	private DrawViewGPS drawViewGPS;
	private String strLat = "Lat : Non définie";
	private String strLong = "Long : Non définie";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (Params.TAG_FG_DEBUG && fgDebugLocal){Log.i(Params.TAG_GEN, TAG_LOCAL + "Start GPS");};
		
		locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
        	if (Params.TAG_FG_DEBUG && fgDebugLocal){Log.i(Params.TAG_GEN, TAG_LOCAL + "GPS - OK");};
        	//Toast.makeText(getApplicationContext(), "GPS actif", Toast.LENGTH_LONG).show();
        	fgGPSActif = true;
        }else{
        	if (Params.TAG_FG_DEBUG && fgDebugLocal){Log.i(Params.TAG_GEN, TAG_LOCAL + "GPS - NO");};
        	//Toast.makeText(getApplicationContext(), "GPS inactif", Toast.LENGTH_LONG).show();
        	fgGPSActif = false;
        	//Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            //startActivityForResult(intent, 1000);
        }
        if (fgGPSActif){
        	locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }
		
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		event.startTracking();
	      // Capture our buttons' keyDown events so the OS doesn't take them and do something we don't want
		switch (keyCode){
			case KeyEvent.KEYCODE_VOLUME_UP:
				if (Params.TAG_FG_DEBUG && fgDebugLocal){Log.i(Params.TAG_GEN, TAG_LOCAL + "KEYCODE_VOLUME_UP");};
				if (camera != null){
		    		camera.release();
		    		camera = null;
		    	}
				intent = new Intent(GPS.this, Boussole.class);
				startActivity(intent);
				finish();
				return true;
			case KeyEvent.KEYCODE_VOLUME_DOWN:
				if (Params.TAG_FG_DEBUG && fgDebugLocal){Log.i(Params.TAG_GEN, TAG_LOCAL + "KEYCODE_VOLUME_DOWN");};
				
				if (camera != null){
		    		camera.release();
		    		camera = null;
		    	}
				intent = new Intent(GPS.this, ConnexionBluetooth.class);
				startActivity(intent);
				finish();
				return true;
			case KeyEvent.KEYCODE_BACK:
				if (Params.TAG_FG_DEBUG && fgDebugLocal){Log.i(Params.TAG_GEN, TAG_LOCAL + "KEYCODE_BACK");};
				if (camera != null){
		    		camera.release();
		    		camera = null;
		    	}
				intent = new Intent(GPS.this, Boussole.class);
				startActivity(intent);
				finish();
				return true;
		}     
		return false;
	      //return super.onKeyDown(keyCode,  event);
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
	            
	            // Create a new draw view and add it to the layout
	            drawViewGPS = new DrawViewGPS(this, "", "", fgGPSActif);
	            drawViewGPS.setScreen(screenPoint);
	            drawViewGPS.setLat(strLat);
	            drawViewGPS.setLong(strLong);
	            frameLayout.addView(drawViewGPS);
	            
	            // Set the layout as the apps content view 
	            setContentView(frameLayout);
	        }
	        else {
	        	Toast toast = Toast.makeText(getApplicationContext(),  "Unable to find camera. Closing.", Toast.LENGTH_SHORT);
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

	@Override
	public void onLocationChanged(Location loc) {
		if (fgGPSActif){
			double latitude = -1000;
			double longitude = -1000;
			
			latitude = loc.getLatitude();
			longitude = loc.getLongitude();
			//if (Params.TAG_FG_DEBUG && fgDebugLocal){Log.i(Params.TAG_GEN, TAG_LOCAL + "latitude = " + latitude);};
			//if (Params.TAG_FG_DEBUG && fgDebugLocal){Log.i(Params.TAG_GEN, TAG_LOCAL + "longitude = " + longitude);};
		
			DecimalFormat dec = new DecimalFormat("#.0000");
			drawViewGPS.setLat("Lat : " + dec.format(latitude) + "°");
			drawViewGPS.setLong("Long : " + dec.format(longitude) + "°");
			drawViewGPS.invalidate();
		}
		
	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}

}
