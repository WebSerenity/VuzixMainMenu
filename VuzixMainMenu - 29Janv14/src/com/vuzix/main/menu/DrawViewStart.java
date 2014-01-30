package com.vuzix.main.menu;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.SurfaceView;

public class DrawViewStart extends SurfaceView{
	private String TAG_LOCAL = "DrawViewStart - ";
	private boolean fgDebugLocal = false;

    private Paint textPaint = new Paint();
    private String str;
    private int screenWidth;
    private int screenHeight;
	
	public DrawViewStart(Context context, String str, Point pt) {
		super(context);
        textPaint.setColor(Color.RED);
        textPaint.setTextSize(72);

        setWillNotDraw(false);	//obligatoire pour Draw
        this.str = str;
        this.screenWidth = pt.x;
        this.screenHeight = pt.y;

	}
	
	public void setText(String str){
		this.str = str;
	}
	
	public void setScreen(Point pt){
		this.screenHeight = pt.y;
		this.screenWidth = pt.x;
	}

	@Override
    protected void onDraw(Canvas canvas){
		float[] taillelettres = new float[str.length()];
        textPaint.getTextWidths(str, 0, str.length(), taillelettres);
        float tailleTexte = 0;
        for (int Cpt = 0; Cpt < taillelettres.length; Cpt++){
        	tailleTexte = tailleTexte + taillelettres[Cpt];
        }
        canvas.drawText(str, screenWidth/2 - (int)tailleTexte/2, screenHeight/2, textPaint);
         
        
	}
}
