package com.algebrator.eq;

import java.util.ArrayList;
import java.util.HashSet;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;

import com.example.circle.Algebrator;
import com.example.circle.EmilyView;
import com.example.circle.SuperView;

public abstract class LeafEquation extends Equation {

    @Override
    public void integrityCheck(){
        if (size() != 0){
            Log.e("ic","this should be size 0");
        }
    }


	public LeafEquation(SuperView owner) {
		super(owner);
		myHeight = Algebrator.getAlgebrator().DEFAULT_SIZE;
		myWidth = Algebrator.getAlgebrator().DEFAULT_SIZE;
	}
	
	@Override
	public String getDisplay(int pos){
	    return display;
	}
	
	@Override
	public HashSet<Equation> on(float x, float y){
		//Log.i("yo,yo",x+","+y);

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
	public ArrayList<EquationDis> closest(DragEquation dragging){
		ArrayList<EquationDis> result = new  ArrayList<EquationDis>();
		result.add(new EquationDis(this,dragging, EquationDis.Side.left));
        result.add(new EquationDis(this,dragging,EquationDis.Side.right));
        result.add(new EquationDis(this,dragging,EquationDis.Side.top));
        result.add(new EquationDis(this,dragging,EquationDis.Side.bottom));
		return result;
	}

    @Override
    public ArrayList<EquationDis> closest(float x, float y){
        ArrayList<EquationDis> result = new  ArrayList<EquationDis>();
        result.add(new EquationDis(this,x,y, EquationDis.Side.left));
        result.add(new EquationDis(this,x,y,EquationDis.Side.right));
        result.add(new EquationDis(this,x,y,EquationDis.Side.top));
        result.add(new EquationDis(this,x,y,EquationDis.Side.bottom));
        return result;
    }
	
	@Override
	public float measureWidth() {
		// not tested
		float totalWidth= Math.max(myWidth,textPaint.measureText(display)); //-textPaint.measureText(display.subSequence(0, 1)+"")
		
		if (parenthesis()){
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
		
		if (parenthesis()){
			totalHeight += PARN_HEIGHT_ADDITION;
		}
		return totalHeight;
	}

	@Override
	public void privateDraw(Canvas canvas, float x, float y) {
		drawBkgBox(canvas, x, y);
		lastPoint =new ArrayList<Point>();
		Paint temp = getPaint();
		if (parenthesis()){
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
	
	public void tryOperator(ArrayList< Equation> yos){}
}
