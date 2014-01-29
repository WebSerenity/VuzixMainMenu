package com.vuzix.conn.bluetooth;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import com.vuzix.tools.Params;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Home extends BaseActivity {
	private String TAG_LOCAL = "Home - ";
	private boolean fgDebugLocal = true;
	
	private Intent intentListBluetooth;
	private EditText etMsg;
	private Button btSend;
	private ListView lvInput;
	private ArrayAdapter<String> messageAdapter;
	
	private String connectedDeviceName = null;
	private int statut = 0;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (Params.TAG_FG_DEBUG && fgDebugLocal){Log.i(Params.TAG_GEN, TAG_LOCAL + "Start Home");};
		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		chatService = new BluetoothChatService(context, mHandler);
		
		if (bluetoothAdapter == null) {
            Toast.makeText(this, context.getResources().getString(R.string.bt_not_available), Toast.LENGTH_LONG).show();
            finish();
            return;
        }
		/*
		if (bluetoothAdapter.getName().equalsIgnoreCase(Params.BT_REF)){
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
		*/
		setContentView(R.layout.activity_home);
		
		etMsg = (EditText)findViewById(R.id.etMsg);
		lvInput = (ListView)findViewById(R.id.lvInput);
		btSend = (Button)findViewById(R.id.btSend);
		btSend.setVisibility(View.INVISIBLE);
		etMsg.setVisibility(View.INVISIBLE);
		
        
        if (!bluetoothAdapter.isEnabled()) {
			bluetoothAdapter.enable();
			Toast.makeText(getApplicationContext(), getResources().getString(R.string.bt_init_active), Toast.LENGTH_LONG).show();
		}
        
        
        if (statut != BluetoothChatService.STATE_CONNECTED){
			Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivityForResult(discoverableIntent, DISCOVERABLE);
        }
        
        btSend.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				//byte[] byteArray = getScreenshot();
				//sendMessage(byteArray);
				String strMsg = etMsg.getText().toString();
				if (Params.TAG_FG_DEBUG && fgDebugLocal){Log.i(Params.TAG_GEN, TAG_LOCAL + "strMsg = " + strMsg);};
				if (strMsg.length() > 0){
					sendMessage(strMsg);
				}
			}
		});
        
        messageAdapter = new ArrayAdapter<String>(this, R.layout.message);
        lvInput = (ListView) findViewById(R.id.lvInput);
        lvInput.setAdapter(messageAdapter);
        etMsg = (EditText) findViewById(R.id.etMsg);
        
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if (Params.TAG_FG_DEBUG && fgDebugLocal){Log.i(Params.TAG_GEN, TAG_LOCAL + "onResume");};
		
		if (chatService != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if (chatService.getState() == BluetoothChatService.STATE_NONE) {
              // Start the Bluetooth chat services
            	chatService.start();
            }
        }
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
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
			if (!bluetoothAdapter.getName().equalsIgnoreCase(Params.BT_REF)) {
				intentListBluetooth = new Intent(this, DeviceBluetoothList.class);
				setStatus("Discovery ...");
				startActivityForResult(intentListBluetooth, REQUEST_CONNECT_DEVICE_SECURE);
			}
	        return;
		}
		if (requestCode == REQUEST_CONNECT_DEVICE_SECURE){	//Retour du Mode Discovery SECURE 
			if (resultCode == Activity.RESULT_OK){
				if (Params.TAG_FG_DEBUG && fgDebugLocal){Log.i(Params.TAG_GEN, TAG_LOCAL + "Device selection = " + intent.getStringExtra(EXTRA_DEVICE_ADDRESS));};
				connectDevice(intent, true);
			}else{
				intentListBluetooth = new Intent(this, DeviceBluetoothList.class);
	            startActivityForResult(intentListBluetooth, REQUEST_CONNECT_DEVICE_INSECURE);
	            
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
	
	private void connectDevice(Intent data, boolean secure) {
        // Get the device MAC address
        String address = data.getExtras().getString(BaseActivity.EXTRA_DEVICE_ADDRESS);
        // Get the BluetoothDevice object
        BluetoothDevice device = bluetoothAdapter.getRemoteDevice(address);
        // Attempt to connect to the device
        chatService.connect(device, secure);
    }
	
	private void setStatus(String info){
		this.setTitle(getResources().getString(R.string.app_name) + " - " + info);
	}
	
	private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case MESSAGE_STATE_CHANGE:
            	if (Params.TAG_FG_DEBUG && fgDebugLocal){Log.i(Params.TAG_GEN, TAG_LOCAL + "MESSAGE_STATE_CHANGE: " + msg.arg1);};
                
                switch (msg.arg1) {
                case BluetoothChatService.STATE_CONNECTED:
                	setStatus(getResources().getString(R.string.bt_connected));
                	statut = BluetoothChatService.STATE_CONNECTED;
                	if (!bluetoothAdapter.getName().equalsIgnoreCase(Params.BT_REF)){
                		btSend.setVisibility(View.VISIBLE);
                		etMsg.setVisibility(View.VISIBLE);
                	}
                    break;
                case BluetoothChatService.STATE_CONNECTING:
                    setStatus(getResources().getString(R.string.bt_connecting));
                    statut = BluetoothChatService.STATE_CONNECTING;
                    break;
                case BluetoothChatService.STATE_LISTEN:
                	setStatus(getResources().getString(R.string.bt_listenning));
                	statut = BluetoothChatService.STATE_LISTEN;
                    break;
                case BluetoothChatService.STATE_NONE:
                	setStatus(getResources().getString(R.string.bt_connected_no));
                	statut = BluetoothChatService.STATE_NONE;
                    break;
                }
                break;
            case MESSAGE_WRITE:
                byte[] writeBuf = (byte[]) msg.obj;
                String writeMessage = new String(writeBuf);
                if (Params.TAG_FG_DEBUG && fgDebugLocal){Log.i(Params.TAG_GEN, TAG_LOCAL + "MESSAGE_WRITE: " + writeMessage);};
                messageAdapter.add("Me:  " + writeMessage);
                break;
            case MESSAGE_READ:
                byte[] readBuf = (byte[]) msg.obj;
                String readMessage = new String(readBuf, 0, msg.arg1);
                if (Params.TAG_FG_DEBUG && fgDebugLocal){Log.i(Params.TAG_GEN, TAG_LOCAL + "MESSAGE_READ: " + readMessage);};
                messageAdapter.add(connectedDeviceName +":  " + readMessage);
                break;
            case MESSAGE_DEVICE_NAME:
            	connectedDeviceName = msg.getData().getString(DEVICE_NAME);
                setStatus(getResources().getString(R.string.bt_connected_to) + " " + connectedDeviceName);
                Toast.makeText(getApplicationContext(), context.getResources().getString(R.string.bt_connected_to) + connectedDeviceName, Toast.LENGTH_SHORT).show();
                break;
            case MESSAGE_TOAST:
                Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST),Toast.LENGTH_SHORT).show();
                break;
            }
        }
    };
	
    
    private void sendMessage(String message) {
        // Check that we're actually connected before trying anything
        if (chatService.getState() != BluetoothChatService.STATE_CONNECTED) {
            Toast.makeText(context, getResources().getString(R.string.bt_connected_no), Toast.LENGTH_SHORT).show();
            return;
        }

        // Check that there's actually something to send
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothChatService to write
            byte[] send = message.getBytes();
            chatService.write(send);
            etMsg.setText("");
        }
    }
    
    private void sendMessage(byte[] byteArray) {
        // Check that we're actually connected before trying anything
        if (chatService.getState() != BluetoothChatService.STATE_CONNECTED) {
            Toast.makeText(context, getResources().getString(R.string.bt_connected_no), Toast.LENGTH_SHORT).show();
            return;
        }

        // Check that there's actually something to send
        if (byteArray.length > 0) {
            // Get the message bytes and tell the BluetoothChatService to write
            //byte[] send = message.getBytes();
            chatService.write(byteArray);

            // Reset out string buffer to zero and clear the edit text field
            //strBufferOut.setLength(0);
            //etMsg.setText(strBufferOut);
        }
    }
    
    private byte[] getScreenshot(){
    	byte[] byteArray = null;
    	// image naming and path  to include sd card  appending name you choose for file
    	String mPath = Environment.getExternalStorageDirectory().toString() + "/Download/test.jpg";  
    	if (Params.TAG_FG_DEBUG && fgDebugLocal){Log.i(Params.TAG_GEN, TAG_LOCAL + "mPath = " + mPath);};

    	// create bitmap screen capture
    	Bitmap bitmap;
    	
    	View v1 = getWindow().getDecorView().getRootView();
    	v1.setDrawingCacheEnabled(true);
    	bitmap = Bitmap.createBitmap(v1.getDrawingCache());
    	if (Params.TAG_FG_DEBUG && fgDebugLocal){Log.i(Params.TAG_GEN, TAG_LOCAL + "bitmap.x = " + bitmap.getWidth());};
    	v1.setDrawingCacheEnabled(false);

    	
    	OutputStream fout = null;
    	File imageFile = new File(mPath);

    	try {
    	    fout = new FileOutputStream(imageFile);
    	    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fout);
    	    fout.flush();
    	    fout.close();
    	    
    	    ByteArrayOutputStream stream = new ByteArrayOutputStream();
    	    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
    	    byteArray = stream.toByteArray();
    	    if (Params.TAG_FG_DEBUG && fgDebugLocal){Log.i(Params.TAG_GEN, TAG_LOCAL + "byteArray size  " + byteArray.length);};

    	} catch (FileNotFoundException e) {
    		if (Params.TAG_FG_DEBUG && fgDebugLocal){Log.i(Params.TAG_GEN, TAG_LOCAL + "getScreenshot FileNotFoundException :  " + e.getMessage());};
    	} catch (IOException e) {
    		if (Params.TAG_FG_DEBUG && fgDebugLocal){Log.i(Params.TAG_GEN, TAG_LOCAL + "getScreenshot IOException :  " + e.getMessage());};
    	}
    	return byteArray;
    }
  
    
    /*
    private TextView.OnEditorActionListener mWriteListener = new TextView.OnEditorActionListener() {
        public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
            // If the action is a key-up event on the return key, send the message
            if (actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_UP) {
                String message = view.getText().toString();
                sendMessage(message);
            }
            if (Params.TAG_FG_DEBUG && fgDebugLocal){Log.i(Params.TAG_GEN, TAG_LOCAL + "END onEditorAction");};
            return true;
        }
    };
    */

}
