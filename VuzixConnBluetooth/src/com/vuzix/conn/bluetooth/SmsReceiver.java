package com.vuzix.conn.bluetooth;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

import com.vuzix.tools.Params;

public class SmsReceiver extends BroadcastReceiver {
	private String TAG_LOCAL = "IncomingSms - ";
	private boolean fgDebugLocal = true;
   
	private final SmsManager sms = SmsManager.getDefault();
    
	public void onReceive(Context context, Intent intent) {
		if (Params.TAG_FG_DEBUG && fgDebugLocal){Log.i(Params.TAG_GEN, TAG_LOCAL + "IncomingSms");};
		final Bundle bundle = intent.getExtras();
		try {
			if (bundle != null) {
				final Object[] pdusObj = (Object[]) bundle.get("pdus");
                
				for (int i = 0; i < pdusObj.length; i++) {
                   SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                   String phoneNumber = currentMessage.getDisplayOriginatingAddress();
                    
                   String senderNum = phoneNumber;
                   String message = currentMessage.getDisplayMessageBody();
                   
                   if (Params.TAG_FG_DEBUG && fgDebugLocal){Log.i(Params.TAG_GEN, TAG_LOCAL + "senderNum: "+ senderNum + "; message: " + message);};
                  
                   intent = new Intent(context, SendSMS.class);
                   intent.putExtra("sms", message);
                   intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
                   context.startActivity(intent);
                    
               } // end for loop
             } // bundle is null

       } catch (Exception e) {
    	   if (Params.TAG_FG_DEBUG && fgDebugLocal){Log.i(Params.TAG_GEN, TAG_LOCAL + "IncomingSms error : " + e.getMessage());};
            
       }
   }    
}
