package com.vuzix.main.menu;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.SurfaceView;

public class DrawViewGPS extends SurfaceView{
	private String TAG_LOCAL = "DrawGPS - ";
	private boolean fgDebugLocal = false;
	
	private String strLat = "";
	private String strLong = "";
	private boolean fgActif = false;
	private String strActif = "";
	private Context context;
	private Paint textPaint = new Paint();
	private int screenWidth;
    private int screenHeight;
	
	public DrawViewGPS(Context context, String strLat, String strLong, boolean actif) {
		super(context);
		this.context = context;
		this.strLat = strLat;
		this.strLong = strLong;
		this.fgActif = actif;
		
		textPaint.setColor(Color.BLUE);
        textPaint.setTextSize(25);

        setWillNotDraw(false);
	}
	
	public void setLat(String strLat){
		this.strLat = strLat;
	}
	
	public void setLong(String strLong){
		this.strLong = strLong;
	}
	
	public void setActif(boolean actif){
		this.fgActif = actif;
	}
	
	public void setScreen(Point pt){
		this.screenWidth = pt.x;
        this.screenHeight = pt.y;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		//super.onDraw(canvas);
		float[] taillelettres = {0};
		float tailleTexte = 0;
		
		//etat du GPS
		if (fgActif){
			strActif = context.getResources().getString(R.string.gps_actif);
		}else{
			strActif = context.getResources().getString(R.string.gps_inactif);
		}
        canvas.drawText(strActif, 10, 25, textPaint);
		
        if (fgActif){
        	textPaint.setColor(Color.RED);
        	canvas.drawText(strLat, screenWidth/3, screenHeight/2- 50, textPaint);
        	canvas.drawText(strLong, screenWidth/3, screenHeight/2 +50, textPaint);
        }
        
	}
	

}
