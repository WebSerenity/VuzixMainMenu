package com.vuzix.main.menu;

import java.text.DecimalFormat;
import java.util.ArrayList;

import com.vuzix.modeles.PtsCardinaux;
import com.vuzix.tools.Params;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.FrameLayout;
import android.widget.Toast;


public class Boussole extends BaseActivity implements SensorEventListener{
	private static String TAG_LOCAL = "Boussole - ";
	private static boolean fgDebugLocal = true;
	
	private Preview preview;
	private Camera camera; 
	private DrawViewBoussole drawViewBoussole;
	private FrameLayout frameLayout;
	
	private SensorManager sensorManager;
	private Sensor orientation;
	
	private String strAzimut = "";
	private PtsCardinaux ptsCardinaux;
	private int azimut = 0;
	private ArrayList<PtsCardinaux> listPts = new ArrayList<PtsCardinaux>();
	private Intent intent;
	

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Params.TAG_FG_DEBUG && fgDebugLocal){Log.i(Params.TAG_GEN, TAG_LOCAL + "Start Boussole");};
        
        ptsCardinaux = new PtsCardinaux(0, 0, 350, 360, "Nord", "N");
        listPts.add(ptsCardinaux);
        ptsCardinaux = new PtsCardinaux(1, 0, 0, 10, "Nord", "N");
        listPts.add(ptsCardinaux);
        ptsCardinaux = new PtsCardinaux(2, 22, 12, 24, "NNE", "NNE");
        listPts.add(ptsCardinaux);
        ptsCardinaux = new PtsCardinaux(3, 45, 35, 55, "NE", "NE");
        listPts.add(ptsCardinaux);
        ptsCardinaux = new PtsCardinaux(4, 67, 57, 77, "ENE", "ENE");
        listPts.add(ptsCardinaux);
        
        
        ptsCardinaux = new PtsCardinaux(5, 90, 80, 110, "Est", "E");
        listPts.add(ptsCardinaux);
        ptsCardinaux = new PtsCardinaux(6, 112, 102, 122, "ESE", "ESE");
        listPts.add(ptsCardinaux);
        ptsCardinaux = new PtsCardinaux(7, 135, 125, 145, "SE", "SE");
        listPts.add(ptsCardinaux);
        ptsCardinaux = new PtsCardinaux(8, 155, 145, 135, "SE", "SE");
        listPts.add(ptsCardinaux);
        
        
        ptsCardinaux = new PtsCardinaux(9, 180, 170, 190, "Sud", "S");
        listPts.add(ptsCardinaux);
        ptsCardinaux = new PtsCardinaux(10, 202, 192, 212, "SSO", "SSO");
        listPts.add(ptsCardinaux);
        ptsCardinaux = new PtsCardinaux(11, 225, 215, 235, "SO", "SO");
        listPts.add(ptsCardinaux);
        ptsCardinaux = new PtsCardinaux(12, 247, 237, 257, "OSO", "OSO");
        listPts.add(ptsCardinaux);
        
        ptsCardinaux = new PtsCardinaux(13, 270, 260, 280, "Ouest", "O");
        listPts.add(ptsCardinaux);
        ptsCardinaux = new PtsCardinaux(14, 292, 282, 302, "ONO", "ONO");
        listPts.add(ptsCardinaux); 
        ptsCardinaux = new PtsCardinaux(15, 315, 305, 325, "NO", "NO");
        listPts.add(ptsCardinaux);  
        ptsCardinaux = new PtsCardinaux(16, 337, 327, 347, "NNO", "NNO");
        listPts.add(ptsCardinaux);   
        
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        orientation = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        
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
				intent = new Intent(Boussole.this, Home.class);
				startActivity(intent);
				return true;
			case KeyEvent.KEYCODE_VOLUME_DOWN:
				if (Params.TAG_FG_DEBUG && fgDebugLocal){Log.i(Params.TAG_GEN, TAG_LOCAL + "KEYCODE_VOLUME_DOWN");};
				if (camera != null){
		    		camera.release();
		    		camera = null;
		    	}
				intent = new Intent(Boussole.this, GPS.class);
				startActivity(intent);
				return true;
			case KeyEvent.KEYCODE_BACK:
				
				return true;
		}     
		return false;
	      //return super.onKeyDown(keyCode,  event);
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	sensorManager.registerListener(this, orientation, SensorManager.SENSOR_DELAY_NORMAL);
    	activeCamera();
    }
    
    @Override
    protected void onPause() {
    	super.onPause();
    	sensorManager.unregisterListener(this);
    	/*
    	if (camera != null){
    		camera.release();
    		camera = null;
    	}
    	*/
    }

    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	sensorManager.unregisterListener(this);
    	/*
    	if (camera != null){
    		camera.release();
    		camera = null;
    	}
    	*/
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
	            drawViewBoussole = new DrawViewBoussole(this, "", screenPoint);
	            drawViewBoussole.setScreen(screenPoint);
	            drawViewBoussole.setAzimut(0);
	            drawViewBoussole.setPtsCardinaux(null);
	            frameLayout.addView(drawViewBoussole);
	            
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
	public void onBackPressed() {
		super.onBackPressed();
		sensorManager.unregisterListener(this);
		/*
		if (camera != null){
    		camera.release();
    		camera = null;
    	}
    	*/
	}
	

	@Override
	public void onSensorChanged(SensorEvent event) {
		float azimuth_angle = event.values[0];
		azimut = (int)azimuth_angle;
		//if (Params.TAG_FG_DEBUG && fgDebugLocal){Log.i(Params.TAG_GEN, TAG_LOCAL + "azimut = " + azimut);};
		
		drawViewBoussole.setAzimut(azimut);
		
	    int pts = getPtsCardinaux(azimut);
	    //if (Params.TAG_FG_DEBUG && fgDebugLocal){Log.i(Params.TAG_GEN, TAG_LOCAL + "pts = " + pts);};
	    if (pts > -1){
	    	drawViewBoussole.setPtsCardinaux(listPts.get(pts));
	    }
		drawViewBoussole.invalidate();
    	
	}

	/*
	private void startView(){
		if (drawViewStart != null){
			drawViewBoussole.setAzimut(azimut);
			//drawViewBoussole.setText(strAzimut);
			drawViewBoussole.setScreen(screenPoint);
			drawViewBoussole.invalidate();
   	 	}
	}
	*/
	
	/*
	private void runThread(){
	     runOnUiThread (new Thread(new Runnable() { 
	         public void run() {
	        	 if (drawViewStart != null){
	 				drawViewBoussole.setAzimut(azimut);
	 				drawViewBoussole.setText(strAzimut);
	 				drawViewBoussole.setScreen(screenPoint);
	 				drawViewBoussole.invalidate();
	        	 }
	         }
	     }));
	}
	*/


	
	private int getPtsCardinaux(int azimut){
		for (int Cpt = 0; Cpt < listPts.size(); Cpt++){
			PtsCardinaux ptsCardinaux = listPts.get(Cpt);
			int min = ptsCardinaux.getMin();
			int max = ptsCardinaux.getMax();
			if (azimut >= min && azimut <= max){
				return Cpt;
			}
		}
		return -1;
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		
	}


}
