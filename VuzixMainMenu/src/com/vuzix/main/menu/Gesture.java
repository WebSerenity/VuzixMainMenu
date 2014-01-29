package com.vuzix.main.menu;

import com.vuzix.tools.Params;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManager.LayoutParams;
import android.widget.FrameLayout;
import android.widget.Toast;

public class Gesture extends BaseActivity implements SensorEventListener{
	private static String TAG_LOCAL = "LaunchAppli - ";
	private static boolean fgDebugLocal = true;
	
	private Intent intent;
	private DrawViewGesture drawViewGesture;
	
	private SensorManager sm;
	private Sensor gesture;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if (Params.TAG_FG_DEBUG && fgDebugLocal){Log.i(Params.TAG_GEN, TAG_LOCAL + "Start Gesture");};
		
		sm = (SensorManager) getSystemService(SENSOR_SERVICE);
		gesture = sm.getDefaultSensor(15);
		if (Params.TAG_FG_DEBUG && fgDebugLocal){Log.i(Params.TAG_GEN, TAG_LOCAL + "gesture name = " + gesture.getName());};
		if (Params.TAG_FG_DEBUG && fgDebugLocal){Log.i(Params.TAG_GEN, TAG_LOCAL + "gesture type = " + gesture.getType());};
		if (Params.TAG_FG_DEBUG && fgDebugLocal){Log.i(Params.TAG_GEN, TAG_LOCAL + "gesture vendor = " + gesture.getVendor());};
		if (Params.TAG_FG_DEBUG && fgDebugLocal){Log.i(Params.TAG_GEN, TAG_LOCAL + "gesture version = " + gesture.getVersion());};
		if (Params.TAG_FG_DEBUG && fgDebugLocal){Log.i(Params.TAG_GEN, TAG_LOCAL + "gesture Max Range = " + gesture.getMaximumRange());};
		if (Params.TAG_FG_DEBUG && fgDebugLocal){Log.i(Params.TAG_GEN, TAG_LOCAL + "gesture Min Delay = " + gesture.getMinDelay());};
		if (Params.TAG_FG_DEBUG && fgDebugLocal){Log.i(Params.TAG_GEN, TAG_LOCAL + "gesture Power = " + gesture.getPower());};
		if (Params.TAG_FG_DEBUG && fgDebugLocal){Log.i(Params.TAG_GEN, TAG_LOCAL + "gesture Res = " + gesture.getResolution());};

	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if (Params.TAG_FG_DEBUG && fgDebugLocal){Log.i(Params.TAG_GEN, TAG_LOCAL + "Gesture val : " + (int)event.values[0]);};

		
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
	            drawViewGesture = new DrawViewGesture(this, "");
	            
	            frameLayout.addView(drawViewGesture);
	            
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
				intent = new Intent(Gesture.this, LaunchAppli.class);
				startActivity(intent);
				return true;
			case KeyEvent.KEYCODE_VOLUME_DOWN:
				if (Params.TAG_FG_DEBUG && fgDebugLocal){Log.i(Params.TAG_GEN, TAG_LOCAL + "KEYCODE_VOLUME_DOWN");};
				//dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_DOWN));
				if (camera != null){
		    		camera.release();
		    		camera = null;
		    	}
				
				intent = new Intent(Gesture.this, Home.class);
				startActivity(intent);
				return true;
			case KeyEvent.KEYCODE_BACK:
				if (Params.TAG_FG_DEBUG && fgDebugLocal){Log.i(Params.TAG_GEN, TAG_LOCAL + "KEYCODE_BACK");};
				//dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
				return true;
		}     
		return false;
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		sm.registerListener(this, gesture, SensorManager.SENSOR_DELAY_NORMAL);
		activeCamera();
	}

	@Override
	protected void onPause() {
		super.onPause();
		sm.unregisterListener(this);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		sm.unregisterListener(this);
	}
}
