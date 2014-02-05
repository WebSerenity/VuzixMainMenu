package com.vuzix.main.menu;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.SurfaceView;

public class DrawViewGesture extends SurfaceView{
	private String TAG_LOCAL = "DrawViewGesture - ";
	private boolean fgDebugLocal = false;
	
	private String strInfo = "";

	private Context context;
	private Paint textPaint = new Paint();
	private int screenWidth;
    private int screenHeight;
    
    public DrawViewGesture(Context context, String str) {
		super(context);
		this.context = context;
		this.strInfo = str;
		
		textPaint.setColor(Color.BLUE);
        textPaint.setTextSize(25);

        setWillNotDraw(false);
	}
    
    public void setScreen(Point pt){
		this.screenWidth = pt.x;
        this.screenHeight = pt.y;
	}
    
    @Override
    protected void onDraw(Canvas canvas) {
    	// TODO Auto-generated method stub
    	//super.onDraw(canvas);
    	canvas.drawText("Gesture", 10, 25, textPaint);
    }

}
