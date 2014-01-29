package com.vuzix.main.menu;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.hardware.Camera;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.vuzix.tools.Params;

public class BaseActivity extends Activity {
	private String TAG_LOCAL = "BaseActivity - ";
	private boolean fgDebugLocal = true;
	
	public static String strPackage = "";
	protected static Point screenPoint;
	protected static int screenWidth;
	protected static int screenHeight;
	
	private PowerManager pm;
	private WakeLock wavelock;
	
	protected Preview preview;
	protected Camera camera; 
	protected FrameLayout frameLayout;
	protected DrawViewStart drawViewStart;
	protected Context context;
	
	protected ArrayList<String> listClass = new ArrayList<String>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		if (Params.TAG_FG_DEBUG && fgDebugLocal){Log.i(Params.TAG_GEN, TAG_LOCAL + "Start BaseActivity");};
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        Display display = getWindowManager().getDefaultDisplay();
        screenPoint = new Point();
        display.getSize(screenPoint); 
        screenWidth = screenPoint.x;
        screenHeight = screenPoint.y;
        if (Params.TAG_FG_DEBUG && fgDebugLocal){Log.i(Params.TAG_GEN, TAG_LOCAL + "screenWidth" + screenWidth);};
        if (Params.TAG_FG_DEBUG && fgDebugLocal){Log.i(Params.TAG_GEN, TAG_LOCAL + "screenHeight" + screenHeight);};
		
		strPackage = getApplicationContext().getPackageName();
		
		pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        
        wavelock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "ISI_WAVELOCK");
        wavelock.acquire();
        
        context = getApplicationContext();
        
        
	}

	
	protected Camera getCameraInstance(){
        Camera c = null;
        
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
        	if (Params.TAG_FG_DEBUG && fgDebugLocal){Log.i(Params.TAG_GEN, TAG_LOCAL + "Camera error = " + e.getMessage());};
        	Toast toast = Toast.makeText(getApplicationContext(),  getResources().getString(R.string.error_camera_instance), Toast.LENGTH_SHORT);
        	toast.show();
        	finish();
        	if (c != null){
            	c.release();
            	c = null;
            }
        }
        return c;
    }
	
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		if (camera != null){
    		camera.release();
    		camera = null;
    	}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if (!wavelock.isHeld()){
    		wavelock.acquire();
    	}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (camera != null){
    		camera.release();
    		camera = null;
    	}
		if (wavelock.isHeld()){
    		wavelock.release();
    	}

	}
	
	@Override
	protected void onPause() {
		super.onPause();
		if (camera != null){
    		camera.release();
    		camera = null;
    	}
		if (wavelock.isHeld()){
    		wavelock.release();
    	}
	}
	
	

}
