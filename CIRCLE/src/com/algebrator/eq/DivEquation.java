package com.algebrator.eq;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;

import com.example.circle.EmilyView;

public class DivEquation extends MultiDivSuperEquation {
	
	@Override
	public Equation copy() {
		Equation result = new DivEquation(this.owner);
		result.display = this.getDisplay(-1);
		result.parentheses = this.parentheses;
		// pass selected?

		// copy all the kiddos and set this as their parent
		for (int i = 0; i < this.size(); i++) {
			result.add(this.get(i).copy());
			result.get(i).parent = result;
		}
		return result;
	}
	
	@Override
	public boolean onTop(Equation eq){
		if  (eq.equals(get(0))){
			return true;
		}
		if (eq.equals(get(1))){
			return false;
		}
		if (get(0).deepContains(eq)){
			if (get(0) instanceof MultiDivSuperEquation){
				MultiDivSuperEquation next = (MultiDivSuperEquation)get(0);
				return next.onTop(eq);
			}
		}
		if (get(1).deepContains(eq)){
			if (get(1) instanceof MultiDivSuperEquation){
				MultiDivSuperEquation next = (MultiDivSuperEquation)get(1);
				return !next.onTop(eq);
			}
		}
		return false;
	}
	
	public DivEquation(EmilyView ev){
		super(ev);
		display ="/";
		myWidth = DEFAULT_SIZE;
		myHeight = DEFAULT_SIZE;
		textPaint = new Paint();
		textPaint.setTextSize(30);
		textPaint.setTextAlign(Align.CENTER);
		textPaint.setColor(0xffffffff);
	}

	@Override
	public float measureWidth() {
		return vertMeasureWidth();
	}

	@Override
	public void draw(Canvas canvas, float x, float y) {
		vertDraw(canvas,x,y);
	}

	@Override
	public float measureHeight() {
		return vertMeasureHeight();
	}
	
	@Override
	public Equation remove(int pos){
		if (pos ==0){
			Equation result = get(0);
			set(0, new NumConstEquation("1",owner));
			return result;
		}else if (pos ==1){
			parent.set(parent.indexOf(this), get(0));
			parent = null;
			set(0, new NumConstEquation("1",owner));
			return this;
		}
		return parent;
	}

	@Override
	public EquationLoc closestPossible(float x, float y) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public boolean canInstertAt(int pos, Equation e) {
		if (pos ==0){
			return canMulti(e);
		}
		else if (pos ==1){
			return canDiv(e);
		}
		return false;
	}
	
	@Override
	public Equation set(int pos, Equation e){
		//TODO looking at canInsertAt, I am going to need to reqrite this
		return set(pos,e);
	}
}
