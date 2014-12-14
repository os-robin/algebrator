package com.algebrator.eq;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;

import com.example.circle.EmilyView;
import com.example.circle.SuperView;

public class AddEquation extends FlexEquation {
	Equation empty;

	@Override
	public Equation copy() {
		Equation result = new AddEquation(this.owner);
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

	public String getDisplay(int pos) {
		if (pos >= 0 && pos < size()) {
			if ( get(pos).negative) {
					return "–";
			}
		}
		return display;
	}

	public AddEquation(SuperView owner) {
		super(owner);
		display = "+";
		myWidth = DEFAULT_SIZE;
		myHeight = DEFAULT_SIZE;

		empty = new NumConstEquation("0", owner);
	}
}
