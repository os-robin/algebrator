package com.algebrator.eq;

import java.util.ArrayList;

import com.example.circle.BigDaddy;
import com.example.circle.EmilyView;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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
		// not tested		
		float totalWidth= myWidth+textPaint.measureText(display)-textPaint.measureText(display.subSequence(0, 1)+"");
		
		if (parenthesis){
			//TODO test
			Paint p = new Paint(textPaint);
			p.setTextSize(measureHeight());
			totalWidth += p.measureText("()");
		}
		return totalWidth;
	}

	@Override
	public float measureHeight() {
		float totalHeight= myHeight;
		
		if (parenthesis){
			totalHeight += PARN_HEIGHT_ADDITION;
		}
		return totalHeight;
	}

	@Override
	public void draw(Canvas canvas, float x, float y) {
		lastPoint =new ArrayList<Point>();
		Paint temp = textPaint;
		if (selected){
			temp = new Paint(textPaint);
			temp.setColor(Color.GREEN);
			
		}
		if (parenthesis){
			drawParenthesis(canvas,x,y,temp);
		} 
		canvas.drawText(display, x, y, temp);
		
		Point point = new Point();
		point.x =(int) x;
		point.y = (int) y;
		lastPoint.add(point);
	}
	
	public EquationLoc closestPossible(float x, float y){
		return null;
	}
	
	
}
