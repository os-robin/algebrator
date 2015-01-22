package com.algebrator.eq;

import java.util.ArrayList;
import java.util.HashSet;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;

import com.example.circle.Actions.BinaryAction;
import com.example.circle.Algebrator;
import com.example.circle.SuperView;

public class DivEquation extends Operation implements MultiDivSuperEquation, BinaryEquation {
	private final int BUFFER = 15;

    @Override
    public void integrityCheck(){
        if (size() != 2){
            Log.e("ic","this should be size 2");
        }
    }

	@Override
	public Equation copy() {
		Equation result = new DivEquation(this.owner);
		result.display = this.getDisplay(-1);
		// pass selected?

		// copy all the kiddos and set this as their parent
		for (int i = 0; i < this.size(); i++) {
			result.add(this.get(i).copy());
			result.get(i).parent = result;
		}
		return result;
	}

	@Override
	public boolean onTop(Equation eq) {
		if (equals(eq)) {
			return true;
		}
		if (eq.equals(get(0))) {
			return true;
		}
		if (eq.equals(get(1))) {
			return false;
		}
		if (get(0).deepContains(eq)) {
			if (get(0) instanceof MultiDivSuperEquation) {
				MultiDivSuperEquation next = (MultiDivSuperEquation) get(0);
				return next.onTop(eq);
			}
		}
		if (get(1).deepContains(eq)) {
			if (get(1) instanceof MultiDivSuperEquation) {
				MultiDivSuperEquation next = (MultiDivSuperEquation) get(1);
				return !next.onTop(eq);
			}
		}
		Log.e("123","onTop for something this does not contain ");
		return false;
	}

	public DivEquation(SuperView owner) {
		super(owner);
        display = "/";


		myWidth = Algebrator.getAlgebrator().DEFAULT_SIZE;
		myHeight = Algebrator.getAlgebrator().DEFAULT_SIZE;
	}

	@Override
	public float measureWidth() {
		float maxWidth = myWidth;

		for (int i = 0; i < size(); i++) {
			if (get(i).measureWidth() > maxWidth) {
				maxWidth = get(i).measureWidth();
			}
		}
		if (parenthesis()) {
			maxWidth += PARN_WIDTH_ADDITION;
		}

		return maxWidth + BUFFER;
	}

	@Override
	public void privateDraw(Canvas canvas, float x, float y) {
		drawBkgBox(canvas, x, y);
		lastPoint = new ArrayList<Point>();
		float totalHieght = measureHeight();
		float currentY = -(totalHieght / 2) + y;
		Paint temp = getPaint();
		if (parenthesis()) {
			drawParentheses(canvas, x, y, temp);
			currentY += PARN_HEIGHT_ADDITION / 2;
		}

		for (int i = 0; i < size(); i++) {
			get(i).draw(canvas, x, currentY + get(i).measureHeightUpper() );
			currentY += get(i).measureHeight();
			if (i != size() - 1) {
				Point point = new Point();
				point.x = (int) x;
				point.y = (int) (currentY + (myHeight) / 2);
				temp.setStrokeWidth(3);
				int halfwidth = (int) ((measureWidth() - (2 * BUFFER)) / 2);
				canvas.drawLine(point.x - halfwidth, point.y, point.x
						+ halfwidth, point.y, temp);

				lastPoint.add(point);
				currentY += myHeight;
			}
		}
	}

	@Override
	public float measureHeight() {
		float totalHeight = myHeight;

		for (int i = 0; i < size(); i++) {
			totalHeight += get(i).measureHeight();
		}
		if (parenthesis()) {
			totalHeight += PARN_HEIGHT_ADDITION;
		}
		return totalHeight;
	}

    @Override
    public float measureHeightUpper(){
        return measureHeight()/2;
    }

    @Override
    public float measureHeightLower(){
        return measureHeight()/2;
    }

	@Override
         public boolean remove(Object e) {
        if (e instanceof Equation && this.contains(e)) {
            remove(indexOf(e));
            return true;
        }
        return false;
    }

    @Override
    public Equation remove(int pos) {
        if (pos == 0) {
            Equation result = get(0);
            this.get(0).replace(new NumConstEquation(1, owner));
            return result;
        } else if (pos == 1) {
            this.replace(get(0));
            return get(1);
        }
        return null;
    }
	
	@Override
	public boolean same(Equation eq){
		if (!(eq instanceof DivEquation))
			return false;
		DivEquation e = (DivEquation)eq;
		return get(0).same(e.get(0)) && get(1).same(e.get(1));
	}
	
	public void tryOperator(ArrayList<Equation> eqs) {

        Equation a = get(0);
        Equation b = get(1);
        //it should cancel any commonalties
        //if you have .../1 it should handle that
        //if youhave 0/... needs to handle that too
        // (6 + 5) / x -> 6/x + 5/x
        //

        Equation result =Operations.divide(a,b);

        replace(result);

	}

    private MultiCountData common(MultiCountData top, MultiCountData bot) {
        MultiCountData result = new MultiCountData();
        for (Equation b:top.key){
            for (Equation a:bot.key){
                if (a.same(b)){
                    result.key.add(a);
                }
            }
        }
        result.value =bot.value;
        return result;
    }

    private double gcd(double a, double b){
        while (b > 0)
        {
            double temp = b;
            b = a % b; // % is remainder
            a = temp;
        }
        return a;
    }

    private double lcm(double a, double b)
    {
        return a * (b / gcd(a, b));
    }

}
