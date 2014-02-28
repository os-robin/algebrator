package com.algebrator.eq;
import java.util.ArrayList;
import java.util.Collections;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;

abstract public class Equation extends ArrayList<Equation>{
	public static final int DEFAULT_SIZE = 50;
	Equation parent;
	String display;
	Paint textPaint; 
	boolean selected = false;
	ArrayList<Point> lastPoint =new ArrayList<Point>();
	protected int myWidth;
	protected int myHeight;
	
	@Override
	public boolean add(Equation equation) {
		boolean result = super.add(equation);
		equation.parent =this;
		return result;
	}
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public ArrayList<EquationDis> closest(float x, float y){
		ArrayList<EquationDis> result= new ArrayList<EquationDis>();
		for (int i=0;i<size();i++){
			result.addAll(get(i).closest(x, y));
		}
		Collections.sort(result);
		return result;
	}
	
	public abstract boolean isFlex();
	public abstract float measureWidth();
	/**
	 * x,y is the center of the equation to be drawn
	 */
	public abstract void draw(Canvas canvas,float x, float y);
	
	public boolean deepContains(Equation equation){
		for (int i=0;i<size();i++){
			if (get(i).deepContains(equation)){
				return true;
			}
		}
		return false;
	}
	
	public abstract float measureHeight();
	public boolean on(float x, float y){
		Log.i("yo,yo",x+","+y);
		boolean result =false;
		for (int i=0;i<lastPoint.size();i++){
			if (x < lastPoint.get(i).x + myWidth/2 
					&& x > lastPoint.get(i).x - myWidth/2 
					&& y < lastPoint.get(i).y + myHeight/2 
					&& y > lastPoint.get(i).y - myHeight/2){
				result = true;
				selected =true;
			}
		}
		return result;
	}
	
	public boolean onAny(float x, float y){
		boolean result =on(x,y);
		if (result){return result;}
		for (int i=0;i<size();i++){
			result =get(i).on(x,y);
			if (result){return result;}
		}
		return result;
	}

 	public void horizDraw(Canvas canvas, float x, float y) {
 		lastPoint =new ArrayList<Point>();
		float totalWidth = measureWidth();
		float currentX=0;
		for (int i=0;i<size();i++){
			float currentWidth = get(i).measureWidth();
			get(i).draw(canvas, x - (totalWidth/2) + currentX + (currentWidth/2),y);
			currentX+=get(i).measureWidth();
			if (i != size()-1){
				canvas.drawText(display, x - (totalWidth/2) + currentX + (myWidth/2), y, textPaint);
				Point point = new Point();
				point.x =(int)( x - (totalWidth/2) + currentX + (myWidth/2));
				point.y = (int)  ( y);
				lastPoint.add(point);
				currentX+=myWidth;
			}
		}
	}
	
	public float horizMeasureHeight() {
		float maxHeight = myHeight;
		
		for(int i = 0; i<size(); i++) {
			if(get(i).measureHeight() > maxHeight) {
				maxHeight = get(i).measureHeight();
			}
		}
		
		return maxHeight;
	}

	public float horizMeasureWidth() {
		float totalWidth = 0;
		
		for(int i = 0; i<size(); i++) {
			totalWidth += get(i).measureWidth();
		}
		totalWidth += (size()-1)*myWidth;
		return totalWidth;
	}
	
	public void vertDraw(Canvas canvas, float x, float y) {
		lastPoint =new ArrayList<Point>();
		float totalHieght = measureWidth();
		float currentY=0;
		for (int i=0;i<size();i++){
			float currentHieght = get(i).measureHeight();
			get(i).draw(canvas, x,y - (totalHieght/2) + currentY + (currentHieght/2));
			currentY+=currentHieght;
			if (i != size()-1){
				canvas.drawText(display, x, y - (totalHieght/2) + currentY + (myWidth/2), textPaint);
				Point point = new Point();
				point.x =(int) x;
				point.y = (int)  (y - (totalHieght/2) + currentY + (myWidth/2));
				lastPoint.add(point);
				currentY+=myHeight;
			}
		}
	}
	
	public float vertMeasureHeight() {
		float totalHeight = myHeight;
		
		for(int i = 0; i<size(); i++) {
			totalHeight = get(i).measureHeight();
		}
		totalHeight += (size()-1)*myHeight;
		return totalHeight;
	}

	public float vertMeasureWidth() {
		float maxWidth = 0;
		
		for(int i = 0; i<size(); i++) {
			if(get(i).measureHeight() > maxWidth) {
				maxWidth = get(i).measureHeight();
			}
		}
		
		return maxWidth;
	}

	public float singleMeasureWidth() {
		return myWidth;
	}

	public float singleMeasureHeight() {
		return myHeight;
	}

	public void singleDraw(Canvas canvas, float x, float y) {
		lastPoint =new ArrayList<Point>();
		if (!selected){
			canvas.drawText(display, x, y, textPaint);
		}else{
			Paint temp = new Paint();
			temp.setColor(Color.GREEN);
			canvas.drawText(display, x, y, temp);
		}
		Point point = new Point();
		point.x =(int) x;
		point.y = (int) y;
		lastPoint.add(point);
		
	}
}