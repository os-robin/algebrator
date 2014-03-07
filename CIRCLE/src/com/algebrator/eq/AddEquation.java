package com.algebrator.eq;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;

import com.example.circle.EmilyView;

public class AddEquation extends FlexEquation {
	Equation empty;
	
	public AddEquation(EmilyView ev){
		super(ev);
		display ="+";
		myWidth = DEFAULT_SIZE;
		myHeight = DEFAULT_SIZE;
		textPaint = new Paint();
		textPaint.setTextSize(30);
		textPaint.setTextAlign(Align.CENTER);
		textPaint.setColor(0xffffffff);
		empty= new NumConstEquation("0",ev);
	}
	
	void operate(int pos1, int pos2){
		// 
	}
	
	void operate(){
		
	}
	
	boolean canOperate(){
		return false;
	}

	public float measureHeight() {
		return horizMeasureHeight();
	}

	public float measureWidth() {
		return horizMeasureWidth();
	}
	
	@Override
	public void draw(Canvas canvas, float x, float y) {
		horizDraw(canvas,x,y);		
	}

	@Override
	public boolean canInsert(Equation eq) {
		//if eq is contained in this its ok
		if (contains(eq)){
			return true;
		}
		// if this and eq are both Add elements of something
		// and they are both on the same side of that something
		Equation lcc= lowestCommonContainer(eq);
		if (lcc instanceof AddEquation){
			if (((AddEquation) lcc).addContain(this) 
					&&((AddEquation) lcc).addContain(this)){
				return true;
			}
		}
		//if eq is on the otherside
		if (lcc instanceof EqualsEquation){
			// figure out what side this is on
			int mySide = ((EqualsEquation)lcc).side(this);
			// if eq is the top of the other side
			if (lcc.get(EqualsEquation.otherSide(mySide)).equals(eq)){
				return true;
			}
			// if the top of the other side is a add equation
			if (lcc.get(EqualsEquation.otherSide(mySide)) instanceof AddEquation){//TODO or ()
				// if the top of the otherside AddContains eq it can be inserted
				if (((AddEquation) lcc.get(EqualsEquation.otherSide(mySide))).addContain(eq)){
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean addContain(Equation equation){
		Equation current = equation;
		if (equation.equals(this)){
			return true;
		}
		while (true){
			if (current.parent.equals(this)){
				return true;
			}else if (current.parent instanceof AddEquation){ //TODO or () 
				current = current.parent;
			}else{
				return false;
			}
		}
	}
}
