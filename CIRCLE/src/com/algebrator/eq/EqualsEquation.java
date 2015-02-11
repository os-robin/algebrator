package com.algebrator.eq;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.util.Log;

import com.example.circle.Algebrator;
import com.example.circle.EmilyView;
import com.example.circle.SuperView;

import java.util.ArrayList;

public class EqualsEquation extends Equation {

    @Override
    public void integrityCheck() {
        if (size() != 2){
            Log.e("ic", "this should always be size 2");
        }
    }

    public EqualsEquation(SuperView owner) {
		super(owner);
		display = "=";
		myWidth = Algebrator.getAlgebrator().getDefaultSize();
		myHeight = Algebrator.getAlgebrator().getDefaultSize();
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

	@Override
	public Equation copy() {
		Equation result = new EqualsEquation(this.owner);
		result.display = this.display;
		// pass selected?

		// copy all the kiddos and set this as their parent
		for (int i = 0; i < this.size(); i++) {
			result.add(this.get(i).copy());
			result.get(i).parent = result;
		}
		return result;
	}


	public void tryOperator(ArrayList<Equation> eqs){}


    public void drawCentered(Canvas canvas, float x, float y) {
        // we need to figure out where the equals is
        float diffX = +this.measureWidth()/2 - get(0).measureWidth() - myWidth/2;
        super.draw(canvas, x+diffX, y);
    }

    @Override
	public Equation remove(int pos) {
		Equation result = get(pos);
		super.justRemove(get(pos));
		//TODO this is only sort right
		NumConstEquation num = new NumConstEquation(0, owner);
		add(pos,num);
		return result;
	}
	
	@Override
	public void justRemove(Equation equation) {
		int pos = indexOf(equation);
		super.justRemove(equation);
		NumConstEquation num = new NumConstEquation(0, owner);
		add(pos,num);
	}
}
