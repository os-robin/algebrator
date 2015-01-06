package com.algebrator.eq;

import java.util.ArrayList;
import java.util.HashSet;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;

import com.example.circle.Algebrator;
import com.example.circle.SuperView;

public class DivEquation extends Operation implements MultiDivSuperEquation {
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
		if (parentheses) {
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
		if (parentheses) {
			drawParentheses(canvas, x, y, temp);
			currentY += PARN_HEIGHT_ADDITION / 2;
		}

		for (int i = 0; i < size(); i++) {
			float currentHieght = get(i).measureHeight();
			get(i).draw(canvas, x, currentY + (currentHieght / 2));
			currentY += currentHieght;
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
		if (parentheses) {
			totalHeight += PARN_HEIGHT_ADDITION;
		}
		return totalHeight;
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

        Equation a = eqs.get(0);
        Equation b = eqs.get(1);
        //it should cancel any commonalties
        //if you have .../1 it should handle that
        //if youhave 0/... needs to handle that too
        // (6 + 5) / x -> 6/x + 5/x
        //

        Equation result =null;
        if (get(0) instanceof AddEquation){
            result = new AddEquation(owner);
            for (Equation e:get(0)){
                // figure out what is common
                MultiCountData top = new MultiCountData(e);
                MultiCountData bot = new MultiCountData(get(1));
                MultiCountData common = common(top,bot);
                top.removeCommon(common);
                bot.removeCommon(common);
                if (!(bot.getEquation(owner) instanceof NumConstEquation && ((NumConstEquation) bot.getEquation(owner)).getValue() == 1)){
                    Equation inner = new DivEquation(owner);
                    inner.add(top.getEquation(owner));
                    inner.add(bot.getEquation(owner));
                    result.add(inner);
                }else if (!(top.getEquation(owner) instanceof NumConstEquation && ((NumConstEquation) top.getEquation(owner)).getValue() == 0 )){
                    result.add(top.getEquation(owner));
                }
            }
            if (result.size() == 1){
                result = result.get(0);
            }
        }else {
            // figure out what is common
            MultiCountData top = new MultiCountData(get(0));
            MultiCountData bot = new MultiCountData(get(1));
            MultiCountData common = common(top,bot);
            top.removeCommon(common);
            bot.removeCommon(common);

            if (!(sortaNumber(bot.getEquation(owner)) && Math.abs(getValue(bot.getEquation(owner)))  == 1)){
                Equation inner = new DivEquation(owner);
                inner.add(top.getEquation(owner));
                inner.add(bot.getEquation(owner));
                result = inner;
            }else{
                if (getValue(bot.getEquation(owner))== -1){
                    if (top.getEquation(owner) instanceof MinusEquation){
                        result = top.getEquation(owner).get(0);
                    }else{
                        Equation inner = top.getEquation(owner);
                        result = new MinusEquation(owner);
                        result.add(inner);
                    }
                }else {
                    result = top.getEquation(owner);
                }
            }
        }
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
