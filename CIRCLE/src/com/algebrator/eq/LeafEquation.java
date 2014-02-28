package com.algebrator.eq;

import java.util.ArrayList;

import com.example.circle.EmilyView;

import android.graphics.Canvas;
import android.graphics.Point;

public abstract class LeafEquation extends FixEquation {

	public LeafEquation(EmilyView ev) {
		super(ev);
	}

	@Override
	public ArrayList<EquationDis> closest(float x, float y){
		ArrayList<EquationDis> result = new  ArrayList<EquationDis>();
		if (lastPoint.size() ==1){
			Point lastP = lastPoint.get(0);;
			float dis = (float) Math.sqrt((x - lastP.x)*(x - lastP.x) + (y - lastP.y)*(y - lastP.y)); 
			result.add(new EquationDis(this,dis));
		}else{
			//TODO error
			
		}
		return result;
	}
	
	@Override
	public float measureWidth() {
		return singleMeasureWidth();
	}

	@Override
	public float measureHeight() {
		return singleMeasureHeight();
	}

	@Override
	public void draw(Canvas canvas, float x, float y) {
		singleDraw(canvas,x,y);
	}

}
