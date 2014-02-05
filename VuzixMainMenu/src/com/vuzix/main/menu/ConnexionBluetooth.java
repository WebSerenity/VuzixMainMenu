package com.vuzix.main.menu;

import com.vuzix.tools.Params;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

public class ConnexionBluetooth extends BaseActivity{
	private static String TAG_LOCAL = "ConnexionBluetooth - ";
	private static boolean fgDebugLocal = true;
	
	private DrawViewConnexionBluetooth drawViewConnexionBluetooth;
	private Intent intent;
	
	private static BluetoothAdapter bluetoothAdapter = null;
	private static BluetoothChatService chatService = null;
	
	private Intent intentListBluetooth;
	//private EditText etMsg;
	//private Button btSend;
	//private ListView lvInput;
	//private ArrayAdapter<String> messageAdapter;
	
	private String connectedDeviceName = null;
	private int statut = 0;
	
	 @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (Params.TAG_FG_DEBUG && fgDebugLocal){Log.i(Params.TAG_GEN, TAG_LOCAL + "Start ConnexionBluetooth");};
		
		
		
	}
	 
	 @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		 if (Params.TAG_FG_DEBUG && fgDebugLocal){Log.i(Params.TAG_GEN, TAG_LOCAL + "resultCode = " + resultCode);};
			if (Params.TAG_FG_DEBUG && fgDebugLocal){Log.i(Params.TAG_GEN, TAG_LOCAL + "requestCode = " + requestCode);};
			
			if (requestCode == DISCOVERABLE){	//retour du ACTION_REQUEST_DISCOVERABLE
				if (resultCode == 0){	//Mode Discovery refuse
					if (Params.TAG_FG_DEBUG && fgDebugLocal){Log.i(Params.TAG_GEN, TAG_LOCAL + "Mode Discovery Refuse");};
					Toast.makeText(context, context.getResources().getString(R.string.bt_discovery_no), Toast.LENGTH_LONG).show();
				}
				if (resultCode == 300){	//Mode Discovery accepte
					if (Params.TAG_FG_DEBUG && fgDebugLocal){Log.i(Params.TAG_GEN, TAG_LOCAL + "Mode Discovery Accepte");};
					Toast.makeText(context, context.getResources().getString(R.string.bt_discovery_ok), Toast.LENGTH_LONG).show();
					
				}
				/*
				if (!bluetoothAdapter.getName().equalsIgnoreCase(Params.BT_REF)) {
					intentListBluetooth = new Intent(this, DeviceBluetoothList.class);
					setStatus("Discovery ...");
					startActivityForResult(intentListBluetooth, REQUEST_CONNECT_DEVICE_SECURE);
				}
				*/
		        return;
			}
			
			if (requestCode == REQUEST_CONNECT_DEVICE_SECURE){	//Retour du Mode Discovery SECURE 
				if (resultCode == Activity.RESULT_OK){
					if (Params.TAG_FG_DEBUG && fgDebugLocal){Log.i(Params.TAG_GEN, TAG_LOCAL + "Device selection = " + intent.getStringExtra(EXTRA_DEVICE_ADDRESS));};
					connectDevice(intent, true);
				}else{
				            
				}
				return;
			}
			
			if (requestCode == REQUEST_CONNECT_DEVICE_INSECURE){	//Retour du Mode Discovery INSECURE 
				if (resultCode == Activity.RESULT_OK){
					if (Params.TAG_FG_DEBUG && fgDebugLocal){Log.i(Params.TAG_GEN, TAG_LOCAL + "Device selection = " + intent.getStringExtra(EXTRA_DEVICE_ADDRESS));};
					connectDevice(intent, false);
				}else{
					
				}
				return;
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
				chatService.stop();
				intent = new Intent(ConnexionBluetooth.this, GPS.class);
				startActivity(intent);
				finish();
				return true;
			case KeyEvent.KEYCODE_VOLUME_DOWN:
				if (Params.TAG_FG_DEBUG && fgDebugLocal){Log.i(Params.TAG_GEN, TAG_LOCAL + "KEYCODE_VOLUME_DOWN");};
				
				if (camera != null){
		    		camera.release();
		    		camera = null;
		    	}
				chatService.stop();
				intent = new Intent(ConnexionBluetooth.this, Home.class);
				startActivity(intent);
				finish();
				return true;
			case KeyEvent.KEYCODE_BACK:
				if (Params.TAG_FG_DEBUG && fgDebugLocal){Log.i(Params.TAG_GEN, TAG_LOCAL + "KEYCODE_BACK");};
				if (camera != null){
		    		camera.release();
		    		camera = null;
		    	}
				chatService.stop();
				intent = new Intent(ConnexionBluetooth.this, GPS.class);
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
		
		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		chatService = new BluetoothChatService(context, handler);
		
		if (bluetoothAdapter == null) {
            Toast.makeText(context, context.getResources().getString(R.string.bt_not_available), Toast.LENGTH_LONG).show();
            finish();
            return;
        }
		
		if (statut != BluetoothChatService.STATE_CONNECTED){
			Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            //startActivityForResult(discoverableIntent, DISCOVERABLE);
        }
		chatService.start();
		
		/*
		if (chatService != null) {
            if (chatService.getState() == BluetoothChatService.STATE_NONE) {
            	chatService.start();
            }
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
	            drawViewConnexionBluetooth = new DrawViewConnexionBluetooth(this,screenPoint);
	            drawViewConnexionBluetooth.setInfo("Bluetooth - listenning ...");
	            frameLayout.addView(drawViewConnexionBluetooth);
	            
	            // Set the layout as the apps content view 
	            setContentView(frameLayout);
	        }
	        else {
	        	Toast toast = Toast.makeText(context,  "Unable to find camera. Closing.", Toast.LENGTH_SHORT);
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
	 
	 private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case MESSAGE_STATE_CHANGE:
            	if (Params.TAG_FG_DEBUG && fgDebugLocal){Log.i(Params.TAG_GEN, TAG_LOCAL + "MESSAGE_STATE_CHANGE: " + msg.arg1);};
                
                switch (msg.arg1) {
                case BluetoothChatService.STATE_CONNECTED:
                	if (Params.TAG_FG_DEBUG && fgDebugLocal){Log.i(Params.TAG_GEN, TAG_LOCAL + "Connected");};
                	//setStatus(context.getResources().getString(R.string.bt_connected));
                	statut = BluetoothChatService.STATE_CONNECTED;
                	drawViewConnexionBluetooth.setInfo("Bluetooth - Connected to " + connectedDeviceName);
                	drawViewConnexionBluetooth.invalidate();
                    break;
                case BluetoothChatService.STATE_CONNECTING:
                    //setStatus(context.getResources().getString(R.string.bt_connecting));
                    if (Params.TAG_FG_DEBUG && fgDebugLocal){Log.i(Params.TAG_GEN, TAG_LOCAL + "Connecting");};
                    statut = BluetoothChatService.STATE_CONNECTING;
                    drawViewConnexionBluetooth.setInfo("Bluetooth - Connecteing ...");
                	drawViewConnexionBluetooth.invalidate();
                    break;
                case BluetoothChatService.STATE_LISTEN:
                	if (Params.TAG_FG_DEBUG && fgDebugLocal){Log.i(Params.TAG_GEN, TAG_LOCAL + "listenning");};
                	//setStatus(context.getResources().getString(R.string.bt_listenning));
                	statut = BluetoothChatService.STATE_LISTEN;
                	drawViewConnexionBluetooth.setInfo("Bluetooth - Listenning ...");
                	drawViewConnexionBluetooth.invalidate();
                    break;
                case BluetoothChatService.STATE_NONE:
                	if (Params.TAG_FG_DEBUG && fgDebugLocal){Log.i(Params.TAG_GEN, TAG_LOCAL + "None");};
                	//setStatus(context.getResources().getString(R.string.bt_connected_no));
                	statut = BluetoothChatService.STATE_NONE;
                	drawViewConnexionBluetooth.setInfo("Bluetooth - Stand by");
                	drawViewConnexionBluetooth.invalidate();
                    break;
                }
                break;
            case MESSAGE_WRITE:
                byte[] writeBuf = (byte[]) msg.obj;
                String writeMessage = new String(writeBuf);
                if (Params.TAG_FG_DEBUG && fgDebugLocal){Log.i(Params.TAG_GEN, TAG_LOCAL + "MESSAGE_WRITE: " + writeMessage);};
                //messageAdapter.add("Me:  " + writeMessage);
                break;
            case MESSAGE_READ:
            	String readMessage = "";
                byte[] readBuf = (byte[]) msg.obj;
                byte byteEntete = readBuf[0];
                String refSend = Character.toString ((char) byteEntete);
                if (Params.TAG_FG_DEBUG && fgDebugLocal){Log.i(Params.TAG_GEN, TAG_LOCAL + "MESSAGE_READ - byteEntete: " + refSend);};
                if (refSend.equalsIgnoreCase(Params.REP_DATA)){
                	readMessage = "Data received";
                }else{
	                readMessage = new String(readBuf, 0, msg.arg1);
	                if (Params.TAG_FG_DEBUG && fgDebugLocal){Log.i(Params.TAG_GEN, TAG_LOCAL + "MESSAGE_READ: " + readMessage);};
	                String strRep = readMessage.substring(0, 1);
	                if (strRep.equalsIgnoreCase(Params.REP_MESSAGE)){
	                	readMessage = "Msg:" + readMessage.substring(1);
	                }
	                if (strRep.equalsIgnoreCase(Params.REP_SMS)){
	                	readMessage = "SMS:" + readMessage.substring(1);
	                }
	                if (strRep.equalsIgnoreCase(Params.REP_NOTIF)){
	                	readMessage = "Notif:" + readMessage.substring(1);
	                }
                }
                
                drawViewConnexionBluetooth.setMessage(readMessage);
            	drawViewConnexionBluetooth.invalidate();
                //messageAdapter.add(connectedDeviceName +":  " + readMessage);
                break;
            case MESSAGE_DEVICE_NAME:
            	connectedDeviceName = msg.getData().getString(DEVICE_NAME);
            	if (Params.TAG_FG_DEBUG && fgDebugLocal){Log.i(Params.TAG_GEN, TAG_LOCAL + "DEVICE_NAME = " + connectedDeviceName);};
                
            	drawViewConnexionBluetooth.setInfo("Bluetooth - Connected to " + connectedDeviceName);
            	drawViewConnexionBluetooth.invalidate();//setStatus(context.getResources().getString(R.string.bt_connected_to) + " " + connectedDeviceName);
                //Toast.makeText(context, context.getResources().getString(R.string.bt_connected_to) + connectedDeviceName, Toast.LENGTH_SHORT).show();
                break;
            case MESSAGE_TOAST:
                //Toast.makeText(context, msg.getData().getString(TOAST),Toast.LENGTH_SHORT).show();
                break;
            }
        }
    };
	/*    
    private void setStatus(String info){
		//this.setTitle(getResources().getString(R.string.app_name) + " - " + info);
	}
    */
    private void connectDevice(Intent data, boolean secure) {
        // Get the device MAC address
        String address = data.getExtras().getString(BaseActivity.EXTRA_DEVICE_ADDRESS);
        // Get the BluetoothDevice object
        BluetoothDevice device = bluetoothAdapter.getRemoteDevice(address);
        // Attempt to connect to the device
        chatService.connect(device, secure);
    }

}
