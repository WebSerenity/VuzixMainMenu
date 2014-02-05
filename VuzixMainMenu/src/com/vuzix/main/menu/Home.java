package com.vuzix.main.menu;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManager.LayoutParams;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.vuzix.tools.Params;

public class Home extends BaseActivity {
	private static String TAG_LOCAL = "Home - ";
	private static boolean fgDebugLocal = true;

	private Timer timer;
	private Intent intent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (Params.TAG_FG_DEBUG && fgDebugLocal){Log.i(Params.TAG_GEN, TAG_LOCAL + "Start Home");};
	}	

	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		event.startTracking();
	      // Capture our buttons' keyDown events so the OS doesn't take them and do something we don't want
		switch (keyCode){
			case KeyEvent.KEYCODE_VOLUME_UP:
				if (Params.TAG_FG_DEBUG && fgDebugLocal){Log.i(Params.TAG_GEN, TAG_LOCAL + "KEYCODE_VOLUME_UP");};
				//dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_UP));
				if (camera != null){
		    		camera.release();
		    		camera = null;
		    	}
				timer.cancel();
				//intent = new Intent(Home.this, ConnexionBluetooth.class);
				intent = new Intent(Home.this, ConnexionBluetooth.class);
				startActivity(intent);
				finish();
				return true;
			case KeyEvent.KEYCODE_VOLUME_DOWN:
				if (Params.TAG_FG_DEBUG && fgDebugLocal){Log.i(Params.TAG_GEN, TAG_LOCAL + "KEYCODE_VOLUME_DOWN");};
				//dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_DOWN));
				if (camera != null){
		    		camera.release();
		    		camera = null;
		    	}
				timer.cancel();
				intent = new Intent(Home.this, Boussole.class);
				startActivity(intent);
				finish();
				return true;
			case KeyEvent.KEYCODE_BACK:
				if (Params.TAG_FG_DEBUG && fgDebugLocal){Log.i(Params.TAG_GEN, TAG_LOCAL + "KEYCODE_BACK");};
				if (camera != null){
		    		camera.release();
		    		camera = null;
		    	}
				timer.cancel();
				intent = new Intent(Home.this, ConnexionBluetooth.class);
				startActivity(intent);
				finish();
				return true;
		}     
		return false;
	      //return super.onKeyDown(keyCode,  event);
	}

	/*
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		if (camera != null){
    		camera.release();
    		camera = null;
    	}
	}
	*/
	
	@Override
	protected void onResume() {
		super.onResume();
		
		activeCamera();
		
		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			  @Override
			  public void run() {
				  //if (Params.TAG_FG_DEBUG && fgDebugLocal){Log.i(Params.TAG_GEN, TAG_LOCAL + "heure = " + getCurrentTimeString());};
				  runThread();
			  }
		}, 1000, 1000);
		
	}
	

	
	private void runThread(){
	     runOnUiThread (new Thread(new Runnable() { 
	         public void run() {
	        	 if (drawViewStart != null){
	        		 drawViewStart.setText(getCurrentTimeString());
	        		 drawViewStart.setScreen(BaseActivity.screenPoint);
	        		 drawViewStart.invalidate();
	        	 }
	         }
	     }));
	}
	
	private String getCurrentTimeString() {
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(date);
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
	            drawViewStart = new DrawViewStart(this, getCurrentTimeString(), BaseActivity.screenPoint);
	            frameLayout.addView(drawViewStart);
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
