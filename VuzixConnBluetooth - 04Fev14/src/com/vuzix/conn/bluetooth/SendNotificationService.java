package com.vuzix.conn.bluetooth;

import java.util.List;

import com.vuzix.tools.Params;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.Notification;
import android.content.Intent;
import android.os.Build;
import android.os.Parcelable;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

public class SendNotificationService extends AccessibilityService{
	private String TAG_LOCAL = "SendNotificationService - ";
	private boolean fgDebugLocal = true;
	
	
	private final AccessibilityServiceInfo accessibilityServiceInfo = new AccessibilityServiceInfo();
	
	public static final String EXTRA_MESSAGE = "extra_message"; 
    public static final String EXTRA_PACKAGE = "extra_package";
    //public static final String ACTION_CATCH_TOAST = "com.mytest.accessibility.CATCH_TOAST"; 
    public static final String ACTION_CATCH_NOTIFICATION = "com.mytest.accessibility.CATCH";
	
	
	private Intent intent;

	@Override
	public void onAccessibilityEvent(AccessibilityEvent event) {
		if (Params.TAG_FG_DEBUG && fgDebugLocal){Log.i(Params.TAG_GEN, TAG_LOCAL + "Notification start");};
		if (Params.TAG_FG_DEBUG && fgDebugLocal){Log.i(Params.TAG_GEN, TAG_LOCAL + "event.type = " + event.getEventType());};
		final int eventType = event.getEventType(); 
        if (eventType == AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED) {
        	if (Params.TAG_FG_DEBUG && fgDebugLocal){Log.i(Params.TAG_GEN, TAG_LOCAL + "event.type = NOTIFICATION");};
            final String sourcePackageName = (String)event.getPackageName();
            Parcelable parcelable = event.getParcelableData();
             
            if (parcelable instanceof Notification) {
            	
                List<CharSequence> messages = event.getText();
                if (messages.size() > 0) {
                    final String notificationMsg = (String) messages.get(0);   
                    if (Params.TAG_FG_DEBUG && fgDebugLocal){Log.i(Params.TAG_GEN, TAG_LOCAL + "Broadcasting for " + ACTION_CATCH_NOTIFICATION);};
                    
                    try {
                        intent = new Intent(SendNotificationService.this, NotifReceiver.class);
                        if (Params.TAG_FG_DEBUG && fgDebugLocal){Log.i(Params.TAG_GEN, TAG_LOCAL + "notificationMsg " + notificationMsg);};
                        intent.putExtra(EXTRA_PACKAGE, sourcePackageName);
                        intent.putExtra(EXTRA_MESSAGE, notificationMsg);
                        SendNotificationService.this.getApplicationContext().sendBroadcast(intent);
                    } catch (Exception e) {
                    	if (Params.TAG_FG_DEBUG && fgDebugLocal){Log.i(Params.TAG_GEN, TAG_LOCAL + "Notification error = " + e.getMessage());};
                    }
                } else {
                	if (Params.TAG_FG_DEBUG && fgDebugLocal){Log.i(Params.TAG_GEN, TAG_LOCAL + "Notification Message is empty. Can not broadcast");};
                }
            } 
        } else {
        	if (Params.TAG_FG_DEBUG && fgDebugLocal){Log.i(Params.TAG_GEN, TAG_LOCAL + "Got un-handled Event");};
        }
		
	}
	
	@Override
	protected void onServiceConnected() {
		// Set the type of events that this service wants to listen to.  
        //Others won't be passed to this service.
		accessibilityServiceInfo.eventTypes = AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED;
 
        // If you only want this service to work with specific applications, set their
        // package names here.  Otherwise, when the service is activated, it will listen
        // to events from all applications.
        //info.packageNames = new String[]
                //{"com.appone.totest.accessibility", "com.apptwo.totest.accessibility"};
 
        // Set the type of feedback your service will provide.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
        	accessibilityServiceInfo.feedbackType = AccessibilityServiceInfo.FEEDBACK_ALL_MASK;
        } else {
        	accessibilityServiceInfo.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
        }       
 
        // Default services are invoked only if no package-specific ones are present
        // for the type of AccessibilityEvent generated.  This service *is*
        // application-specific, so the flag isn't necessary.  If this was a
        // general-purpose service, it would be worth considering setting the
        // DEFAULT flag.
 
        // info.flags = AccessibilityServiceInfo.DEFAULT;
 
        accessibilityServiceInfo.notificationTimeout = 100;
 
        this.setServiceInfo(accessibilityServiceInfo);
	}

	@Override
	public void onInterrupt() {
		// TODO Auto-generated method stub
		
	}

}
