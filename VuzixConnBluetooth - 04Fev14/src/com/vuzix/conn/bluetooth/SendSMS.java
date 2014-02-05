package com.vuzix.conn.bluetooth;

import com.vuzix.tools.Params;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class SendSMS extends BaseActivity{
	private String TAG_LOCAL = "SendSMS - ";
	private boolean fgDebugLocal = true;
	private Intent intent;
	private String strSMS = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (Params.TAG_FG_DEBUG && fgDebugLocal){Log.i(Params.TAG_GEN, TAG_LOCAL + "SendSMS");};
		
		intent = getIntent();
		strSMS = intent.getStringExtra("sms");
		if (Params.TAG_FG_DEBUG && fgDebugLocal){Log.i(Params.TAG_GEN, TAG_LOCAL + "strSMS = " + strSMS);};
		Home.sendMessage(strSMS);
		finish();
	}

}
