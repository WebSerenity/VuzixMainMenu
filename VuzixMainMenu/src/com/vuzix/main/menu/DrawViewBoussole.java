package com.vuzix.main.menu;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;
import android.view.SurfaceView;

import com.vuzix.modeles.PtsCardinaux;
import com.vuzix.tools.Params;

public class DrawViewBoussole extends SurfaceView{
	private String TAG_LOCAL = "DrawViewBoussole - ";
	private boolean fgDebugLocal = false;

    private Paint textPaint = new Paint();
    private Paint ptsPaint = new Paint();
    private String str;
    private int screenWidth;
    private int screenHeight;
    private int azimut = 0;
    private PtsCardinaux ptsCardinaux = null;
    
	public DrawViewBoussole(Context context, String str, Point pt) {
		super(context);
		// Create out paint to use for drawing
        textPaint.setARGB(255, 0, 0, 255);
        textPaint.setTextSize(25);

        setWillNotDraw(false);
        this.str = str;
        this.screenWidth = pt.x;
        this.screenHeight = pt.y;

	}
	
	public void setPtsCardinaux(PtsCardinaux ptsCardinaux){
		this.ptsCardinaux = ptsCardinaux;
	}
	
	public void setText(String str){
		this.str = str;
	}
	
	public void setScreen(Point pt){
		this.screenWidth = pt.x;
        this.screenHeight = pt.y;
	}
	
	public void setAzimut(int azimut){
		this.azimut = azimut;
	}
	
	@Override
    protected void onDraw(Canvas canvas){
		float[] taillelettres = {0};
		float tailleTexte = 0;
		int delta = 0;
		
		
        //Dessin de la ligne
        Paint paintLine = new Paint();
        paintLine.setColor(Color.RED);
        paintLine.setStrokeWidth(5);
        canvas.drawLine(0, screenHeight/2,screenWidth, screenHeight/2, paintLine);
        
        //Dessin des textes
        if (Params.TAG_FG_DEBUG && fgDebugLocal){Log.i(Params.TAG_GEN, TAG_LOCAL + "azimut = " + azimut);};
        String strAzimut = String.valueOf(azimut + "°");
        taillelettres = new float[strAzimut.length()];
        textPaint.getTextWidths(strAzimut, 0, strAzimut.length(), taillelettres);
        tailleTexte = 0;
        for (int Cpt = 0; Cpt < taillelettres.length; Cpt++){
        	tailleTexte = tailleTexte + taillelettres[Cpt];
        }
    	delta = (int) (tailleTexte/2);       
        canvas.drawText(strAzimut, screenWidth/2 - delta, 25, textPaint);
        
        int azimutMin = azimut - Params.BOUSSOLE_VIEW/2;
        String strAzimutMin = String.valueOf(azimutMin + "°");      
        canvas.drawText(strAzimutMin, 10, 25, textPaint);
        
    	int azimutMax = azimut + Params.BOUSSOLE_VIEW/2;
        String strAzimutMax = String.valueOf(azimutMax + "°"); 
        taillelettres = new float[strAzimutMax.length()];
        textPaint.getTextWidths(strAzimutMax, 0, strAzimutMax.length(), taillelettres);
        tailleTexte = 0;
        for (int Cpt = 0; Cpt < taillelettres.length; Cpt++){
        	tailleTexte = tailleTexte + taillelettres[Cpt];
        }
    	delta = (int) (tailleTexte); 
        canvas.drawText(strAzimutMax, screenWidth - delta - 10, 25, textPaint);
        
        //Dessin des traits verticaux de la boussole
        int deltaRep = 0;
        int reste = azimut % 10;
        if (Params.TAG_FG_DEBUG && fgDebugLocal){Log.i(Params.TAG_GEN, TAG_LOCAL + "reste = " + reste);};
        if (reste != 0){
        	deltaRep = reste;
        }
        int step = screenWidth /Params.BOUSSOLE_VIEW;
        for (int Cpt = 0; Cpt < 2*Params.BOUSSOLE_VIEW; Cpt++){
        	paintLine.setColor(Color.RED);
        	int posRep = deltaRep + Cpt * step;
        	int height = 25;
        	canvas.drawLine(posRep, screenHeight/2,posRep, screenHeight/2 - height, paintLine);
        	
        }
    	
        

        
        if (ptsCardinaux != null && azimut >= ptsCardinaux.getMin() && azimut <= ptsCardinaux.getMax()){
        	ptsPaint.setColor(Color.RED);
        	ptsPaint.setTextSize(60);
         	
        	int posPts = (ptsCardinaux.getMax() - azimut) * step;
        	if (posPts < 0){
        		posPts = 0;
        	}
        	//if (Params.TAG_FG_DEBUG && fgDebugLocal){Log.i(Params.TAG_GEN, TAG_LOCAL + "posPts = " + posPts);};
        	
        	taillelettres = new float[ptsCardinaux.getAff().length()];
            textPaint.getTextWidths(ptsCardinaux.getAff(), 0, ptsCardinaux.getAff().length(), taillelettres);
            tailleTexte = 0;
            for (int Cpt = 0; Cpt < taillelettres.length; Cpt++){
            	tailleTexte = tailleTexte + taillelettres[Cpt];
            }
        	delta = (int) (tailleTexte/2);
        	canvas.drawLine(posPts + deltaRep, screenHeight/2,posPts + deltaRep, screenHeight/2 - 50, paintLine);
        	
        	canvas.drawText(ptsCardinaux.getAff(), posPts, screenHeight/2 + 60, ptsPaint);
        }
        
        
	}
}
