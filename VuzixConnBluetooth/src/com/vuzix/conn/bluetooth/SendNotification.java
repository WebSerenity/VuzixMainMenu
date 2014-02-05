package com.vuzix.conn.bluetooth;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import com.vuzix.tools.Params;

public class SendNotification extends BaseActivity{
	private String TAG_LOCAL = "SendNotification - ";
	private boolean fgDebugLocal = true;
	private Intent intent;
	private String strNotification = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (Params.TAG_FG_DEBUG && fgDebugLocal){Log.i(Params.TAG_GEN, TAG_LOCAL + "SendNotification");};
		
		intent = getIntent();
		strNotification = intent.getStringExtra("notification");
		if (Params.TAG_FG_DEBUG && fgDebugLocal){Log.i(Params.TAG_GEN, TAG_LOCAL + "strNotification = " + strNotification);};
		strNotification = Params.REP_NOTIF + strNotification;
		Home.sendMessage(strNotification);
		finish();
	}
	

	


}
