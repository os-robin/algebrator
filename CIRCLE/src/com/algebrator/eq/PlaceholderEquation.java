package com.algebrator.eq;

import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.util.Log;

import com.example.circle.Algebrator;
import com.example.circle.EmilyView;
import com.example.circle.SuperView;

public class PlaceholderEquation extends LeafEquation {
	
	public PlaceholderEquation(SuperView owner) {
		super(owner);
		display = "|";
		myWidth =0;
		myHeight = Algebrator.getAlgebrator().getDefaultSize();

	}
	
	@Override
	public Equation copy() {
		Log.e("copy", "this should prolly not be called");
		
		Equation result = new PlaceholderEquation(this.owner);
		result.display = this.display;

		return result;
	}

    @Override
    protected float privateMeasureWidth() {
        return 0;
    }

    @Override
    protected Paint getPaint(){
        Paint p = new Paint(super.getPaint());
        long now = System.currentTimeMillis()/4;
         now = now % 360;
        int alpha =(int)((1+Math.sin(Math.toRadians(now)))*127.5);
        p.setAlpha(alpha);
        return p;
    }
	
}
