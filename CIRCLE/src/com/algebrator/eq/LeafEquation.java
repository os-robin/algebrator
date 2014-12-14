package com.algebrator.eq;

import java.util.ArrayList;
import java.util.HashSet;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;

import com.example.circle.EmilyView;
import com.example.circle.SuperView;

public abstract class LeafEquation extends FixEquation {

	public LeafEquation(SuperView owner) {
		super(owner);
		myHeight = DEFAULT_SIZE;
		myWidth = DEFAULT_SIZE;
	}
	
	@Override
	public String getDisplay(int pos){
		if (parent instanceof AddEquation && parent.indexOf(this) != 0){
			return display;
		}
		if (pos != -1){
			return display;
		}
		return (negative?"-":"")+display;
	}
	
	@Override
	public HashSet<Equation> on(float x, float y){
		Log.i("yo,yo",x+","+y);

		HashSet<Equation> result = new HashSet<Equation>();
		
		
		for (int i=0;i<lastPoint.size();i++){
			//TODO should be measureHeight and width??
			if (x < lastPoint.get(i).x + measureWidth()/2 
					&& x > lastPoint.get(i).x - measureWidth()/2 
					&& y < lastPoint.get(i).y + measureHeight()/2 
					&& y > lastPoint.get(i).y - measureHeight()/2){
				result.add(this);
			}
		}
		return result;
	}


	@Override
	public ArrayList<EquationDis> closest(float x, float y){
		ArrayList<EquationDis> result = new  ArrayList<EquationDis>();
		result.add(new EquationDis(this,x,y));
		return result;
	}
	
	@Override
	public float measureWidth() {
		// not tested
		float totalWidth= myWidth+textPaint.measureText(display)-textPaint.measureText(display.subSequence(0, 1)+"");
		
		if (parentheses){
			totalWidth += PARN_WIDTH_ADDITION;
		}
		return totalWidth;
	}

	@Override
	public float measureHeight() {
//		Rect out =  new Rect();
//		textPaint.getTextBounds(display, 0, display.length(),out);
//		float totalHeight= out.height();
		
		float totalHeight= myHeight;
		
		if (parentheses){
			totalHeight += PARN_HEIGHT_ADDITION;
		}
		return totalHeight;
	}

	@Override
	public void privateDraw(Canvas canvas, float x, float y) {
		drawBkgBox(canvas, x, y);
		lastPoint =new ArrayList<Point>();
		Paint temp = getPaint();
		if (parentheses){
			drawParentheses(canvas,x,y,temp);
		} 
		Rect out =  new Rect();
		textPaint.getTextBounds(display, 0, display.length(),out);
		float h= out.height();
		float w= out.width();
		canvas.drawText(getDisplay(-1), x, y+ (h/2), temp);
		
		Point point = new Point();
		point.x =(int) x;
		point.y = (int) y;
		lastPoint.add(point);
	}
	
	public  boolean tryAddLeft(DragEquation dragging){
		if (CanAdd(dragging)){
			if (side() != dragging.demo.side()){
				dragging.demo.negative = !dragging.demo.negative;
			}
			dragging.demo.remove();
			if (parent instanceof AddEquation){
				int myIndex = parent.indexOf(this);
				if (dragging.demo.x < x){
					parent.add(myIndex+1,dragging.demo);
				}else{
					parent.add(myIndex,dragging.demo);
				}
				
			}else{
				Equation oldEq = this;
				AddEquation newEq = new AddEquation(owner);
				if (oldEq.parentheses){
					oldEq.parentheses = false;
					newEq.parentheses = true;
				}
				oldEq.replace(newEq);
				newEq.add(dragging.demo);
				newEq.add(oldEq);
			}
			return true;
		}
		return false;
	}

	private boolean CanAdd(DragEquation dragging) {
		// if these are in the same add block
		if (parent.addContain(dragging.demo) || dragging.demo.addContain(this)){
			return true;
		}
		// if they are only both only adds away form equals
		if (getEquals().addContain(dragging.demo) && dragging.demo.getEquals().addContain(this)){
			return true;
		}
		return false;
	}

	
	private boolean tryAdd(DragEquation dragging,boolean right){
		if (CanAdd(dragging)){
			if (side() != dragging.demo.side()){
				dragging.demo.negative = !dragging.demo.negative;
			}
			dragging.demo.remove();
			if (parent instanceof AddEquation){
				int myIndex = parent.indexOf(this);
				if (dragging.demo.x < x){
					parent.add(myIndex+1,dragging.demo);
				}else{
					parent.add(myIndex,dragging.demo);
				}
			}else{
				Equation oldEq = this;
				AddEquation newEq = new AddEquation(owner);
				if (oldEq.parentheses){
					oldEq.parentheses = false;
					newEq.parentheses = true;
				}
				oldEq.replace(newEq);
				if (right){
					newEq.add(dragging.demo);
					newEq.add(oldEq);
				}else{
					newEq.add(oldEq);
					newEq.add(dragging.demo);
				}
			}
			return true;
		}
		return false;
	}
	
	public  boolean tryAddRight(DragEquation dragging){
		return tryAdd(dragging,false);
	}

	public  boolean tryDiv(DragEquation dragging){
		return false;
	}

	public  boolean tryMultiRight(DragEquation dragging){
		return tryAdd(dragging,true);
	}

	// TODO
	private boolean CanMulti(DragEquation dragging) {
		return false;
		// if these are in the same multi block
		//if (parent.DivMultiContain(dragging.demo) || dragging.demo.DivMultiContain(this)){
		//		return true;
		//}
		//		// if they are only both only adds away form equals
		//if (getEquals().DivMultiContain(dragging.demo) && dragging.demo.getEquals().DivMultiContain(this)){
		//		return true;
		//}
		//return false;
	}

	public  boolean tryMultiLeft(DragEquation dragging){
		return false;
	}
}
