package com.algebrator.eq;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;

import com.example.circle.EmilyView;

public class AddEquation extends FlexEquation {
	Equation empty;
	
	@Override
	public Equation copy() {
		Equation result = new AddEquation(this.owner);
		result.display = this.display;
		result.parenthesis = this.parenthesis;
		// pass selected?

		// copy all the kiddos and set this as their parent
		for (int i = 0; i < this.size(); i++) {
			result.add(this.get(i).copy());
			result.get(i).parent = result;
		}
		return result;
	}
	
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
		// TODO - if the elements at pos1 and know there values
		// what if they are both x ... or one is 3 x and the other is 2 x
		// or x + 5 and x + 7
	}
	
	void operate(){
		//TODO
	}
	
	boolean canOperate(){
		// TODO
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
	public boolean canInstertAt(int pos, Equation eq) {
		return canAdd(eq);
	}

	@Override
	public EquationLoc closestPossible(float x, float y) {
		// TODO Auto-generated method stub
		return null;
	}
}
