package com.vuzix.main.menu;

import com.vuzix.tools.Params;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {
	private static String TAG_LOCAL = "BootReceiver - ";
	private static boolean fgDebugLocal = true;

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		intent = new Intent(context, Home.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
		context.startActivity(intent);
		if (Params.TAG_FG_DEBUG && fgDebugLocal){Log.i(Params.TAG_GEN, TAG_LOCAL + "Boot AutoStart démarré." );};
	}

}
