package com.vuzix.main.menu;

import java.util.ArrayList;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
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
	
	protected static BluetoothAdapter bluetoothAdapter = null;
	protected static BluetoothChatService chatService = null;
	
	protected static final int DISCOVERABLE = 200;
	//protected static final int MODE_DISCOVERY = 100;
	
	protected static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
	protected static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
	
	protected static String EXTRA_DEVICE_ADDRESS = "device_address";
	
	
	protected static final int MESSAGE_STATE_CHANGE = 1;
	protected static final int MESSAGE_READ = 2;
	protected static final int MESSAGE_WRITE = 3;
	protected static final int MESSAGE_DEVICE_NAME = 4;
	protected static final int MESSAGE_TOAST = 5;
	
	protected static String DEVICE_NAME = "device_name";
	protected static String TOAST = "toast";
	
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
