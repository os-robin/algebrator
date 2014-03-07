package com.algebrator.eq;

import java.util.ArrayList;

import com.example.circle.BigDaddy;
import com.example.circle.EmilyView;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Paint.Align;

public abstract class LeafEquation extends FixEquation {

	public LeafEquation(EmilyView ev) {
		super(ev);

		myWidth = DEFAULT_SIZE;
		myHeight = DEFAULT_SIZE;
		textPaint = new Paint();
		textPaint.setTextSize(BigDaddy.TEXT_SIZE);
		textPaint.setTextAlign(Align.CENTER);
		textPaint.setColor(0xffffffff);
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
		textPaint.measureText(display);
		
		float totalWidth= myWidth+textPaint.measureText(display)-textPaint.measureText(display.subSequence(0, 1)+"");
		
		if (parenthesis){
			//TODO test
			Paint p = new Paint();
			p.setTextSize(BigDaddy.TEXT_SIZE);
			p.setTextSize(measureHeight());
			totalWidth += p.measureText("()");
		}
		return totalWidth;
	}

	@Override
	public float measureHeight() {
		return myHeight;
	}

	@Override
	public void draw(Canvas canvas, float x, float y) {
		lastPoint =new ArrayList<Point>();
		if (!this.isSelected()){
			textPaint.measureText(display);
			canvas.drawText(display, x, y, textPaint);
		}else{
			Paint temp = new Paint();
			temp.setTextSize(BigDaddy.TEXT_SIZE);
			temp.measureText(display);
			temp.setTextAlign(Align.CENTER);
			temp.setColor(Color.GREEN);
			canvas.drawText(display, x, y, temp);
		}
		Point point = new Point();
		point.x =(int) x;
		point.y = (int) y;
		lastPoint.add(point);
	}

}
