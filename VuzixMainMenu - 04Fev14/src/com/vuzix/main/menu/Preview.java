package com.vuzix.main.menu;

import java.io.IOException;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class Preview extends SurfaceView implements SurfaceHolder.Callback {
	private SurfaceHolder mHolder;
    private Camera mCamera;

	public Preview(Context context, Camera camera) {
		super(context);
		mCamera = camera;
		mHolder = getHolder();
        mHolder.addCallback(this);
        //type de la ssurface 
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		 if (mHolder.getSurface() == null){
	          // preview surface does not exist
          return;
        }

        // stop preview before making changes
        try {
        	if (mCamera != null){
        		mCamera.stopPreview();
        	}
        } catch (Exception e){
        	Log.d("ISI", "Error starting camera preview: " + e.getMessage());
        	mCamera = null;
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here

        // start preview with new settings
        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
            //tryDrawing(holder);

        } catch (Exception e){
            Log.d("ISI", "Error starting camera preview: " + e.getMessage());
            if (mCamera != null){
            	mCamera.stopPreview();
            	mCamera = null;
            }
        }
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		try {
			if (mCamera != null){
				mCamera.setPreviewDisplay(holder);
				mCamera.startPreview();
			}
            //tryDrawing(holder);
        } catch (IOException e) {
            Log.d("ISI", "Error setting camera preview: " + e.getMessage());
            mCamera.stopPreview();
            mCamera = null;
        }
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
		if (mCamera != null){
			//mCamera.stopPreview();
			mCamera = null;
		}
		
	}
	
	/*

	@Override
	public void run() {
		// TODO Auto-generated method stub
		if(mHolder.getSurface().isValid()){
		     Canvas canvas = mHolder.lockCanvas();
		     Paint paint = new Paint(); 
		     paint.setColor(Color.RED); 
		     paint.setStyle(Style.FILL); 
		     canvas.drawPaint(paint); 

		     paint.setColor(Color.BLACK); 
		     paint.setTextSize(20); 
		     canvas.drawText("Some Text", 50, 75, paint); 
		     mHolder.unlockCanvasAndPost(canvas);
		}else{
			Log.i("ISI", "Pas de canvas...");
		}
		
	}
	*/

}
