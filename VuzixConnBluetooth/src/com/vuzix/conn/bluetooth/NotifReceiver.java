package com.vuzix.conn.bluetooth;

import com.vuzix.tools.Params;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class NotifReceiver extends BroadcastReceiver {
	private String TAG_LOCAL = "NotificationCatcherReceiver - ";
	private boolean fgDebugLocal = true;
	
	private String message = ""; 
	
	@Override
	public void onReceive(Context context, Intent intent) {
		if (Params.TAG_FG_DEBUG && fgDebugLocal){Log.i(Params.TAG_GEN, TAG_LOCAL + "notificationCatcherReceiver - Received message");};
    	if (Params.TAG_FG_DEBUG && fgDebugLocal){Log.i(Params.TAG_GEN, TAG_LOCAL + "notificationCatcherReceiver - intent.getAction() :: " + intent.getAction());}; 
    	if (Params.TAG_FG_DEBUG && fgDebugLocal){Log.i(Params.TAG_GEN, TAG_LOCAL + "notificationCatcherReceiver - EXTRA_PACKAGE) :: " + intent.getStringExtra(SendNotificationService.EXTRA_PACKAGE));};
    	if (Params.TAG_FG_DEBUG && fgDebugLocal){Log.i(Params.TAG_GEN, TAG_LOCAL + "notificationCatcherReceiver - EXTRA_MESSAGE) :: " + intent.getStringExtra(SendNotificationService.EXTRA_MESSAGE));};
        
    	intent = new Intent(context, SendNotification.class);
        intent.putExtra("notification", message);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
        context.startActivity(intent);
		
	}
}
