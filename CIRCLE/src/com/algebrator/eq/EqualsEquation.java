package com.algebrator.eq;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;

import com.example.circle.EmilyView;

public class EqualsEquation extends FixEquation {
	
	public EqualsEquation(EmilyView ev) {
		super(ev);
		display ="=";
		myWidth = DEFAULT_SIZE;
		myHeight = DEFAULT_SIZE;
		// we should probably pass in the paint
		textPaint = new Paint();
		textPaint.setTextSize(30);
		textPaint.setTextAlign(Align.CENTER);
		textPaint.setColor(0xff000000);
	}

	@Override
	public float measureWidth() {
		return horizMeasureWidth();
	}

	@Override
	public void draw(Canvas canvas, float x, float y) {
		horizDraw(canvas,x,y);
		
	}

	@Override
	public float measureHeight() {
		return horizMeasureHeight();
	}

	public int side(Equation equation)  {
		if (get(0).deepContains(equation)){
			return 0;
		}
		if (get(1).deepContains(equation)){
			return 1;
		}
		// else error out
		return -1;
	}

	public static int otherSide(int side) {
		return (side ==1?0:1);
	}

	@Override
	public EquationLoc closestPossible(float x, float y) {
		// TODO Auto-generated method stub
		return null;
	}
	/**  
	 * can't add to the equals equation
	 */
	@Override
	public boolean canInstertAt(int pos, Equation e) {
		return false;
	}
	/**  
	 * can't div the equals equation
	 */
	@Override
	public boolean canDiv(Equation e) {
		return false;
	}
	/**  
	 * can't add to the equals equation
	 */
	@Override
	public boolean canAdd(Equation e) {
		return false;
	}
	/**  
	 * can't multi the equals equation
	 */
	@Override
	public boolean canMulti(Equation e) {
		return false;
	}

	@Override
	public Equation copy() {
		Equation result = new EqualsEquation(this.owner);
		result.display = this.display;
		result.parentheses = this.parentheses;
		// pass selected?

		// copy all the kiddos and set this as their parent
		for (int i = 0; i < this.size(); i++) {
			result.add(this.get(i).copy());
			result.get(i).parent = result;
		}
		return result;
	}

}
