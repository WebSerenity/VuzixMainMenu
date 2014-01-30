package com.vuzix.main.menu;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.SurfaceView;

public class DrawViewConnexionBluetooth extends SurfaceView{
	private String TAG_LOCAL = "DrawViewStart - ";
	private boolean fgDebugLocal = false;

    private Paint textPaint = new Paint();
    private Paint textPaintMsg = new Paint();
    private String strInfo = "";
    private String strMsg = "";
    private int screenWidth;
    private int screenHeight;

	public DrawViewConnexionBluetooth(Context context, Point pt) {
		super(context);
		textPaint.setColor(Color.BLUE);
        textPaint.setTextSize(25);
        this.screenWidth = pt.x;
        this.screenHeight = pt.y;

        setWillNotDraw(false);	//obligatoire pour Draw
       
	}
	
	public void setInfo(String info){
		this.strInfo = info;
	}
	
	public void setMessage(String msg){
		this.strMsg = msg;
	}
	
	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		canvas.drawText(strInfo, 10, 25, textPaint);
		
		textPaintMsg.setColor(Color.RED);
        textPaintMsg.setTextSize(25);
        
        canvas.drawText(strMsg, (int)(screenWidth*0.2), (int)(screenHeight*0.3), textPaintMsg);
		
	}

}
