package com.algebrator.eq;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;

import com.example.circle.EmilyView;
import com.example.circle.SuperView;

public class EqualsEquation extends FixEquation {
	
	public EqualsEquation(SuperView owner) {
		super(owner);
		display ="=";
		myWidth = DEFAULT_SIZE;
		myHeight = DEFAULT_SIZE;
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

	private int buffer = 10;
	public boolean inBox(float x, float y) {
		float w = measureWidth();
		float h = measureHeight();
		if (x > (this.x + (w/2) + buffer)){
			return false;}
		if (x < (this.x - (w/2) - buffer)){
			return false;}
		if (y > (this.y + (h/2) + buffer)){
			return false;}
		if (y < (this.y - (h/2) - buffer)){
			return false;}
		return true;
	}

}
