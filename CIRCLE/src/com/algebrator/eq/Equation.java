package com.algebrator.eq;
import java.util.ArrayList;
import java.util.Collections;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;

import com.example.circle.BigDaddy;
import com.example.circle.EmilyView;

abstract public class Equation extends ArrayList<Equation>{
	protected static final int DEFAULT_SIZE = 50;
	protected static final float PARN_HEIGHT_ADDITION = 10;
	public Equation parent;
	public String display;
	Paint textPaint;
	EmilyView owner;
	public boolean parentheses;
	protected boolean selected = false;
	
	
	public Equation(EmilyView ev){
		owner =ev;
	}
	
	// we could template this in C++ can we in java?
	/**
	 * makes a copy the entire equation tree below and including this node
	 * good for showing work
	 * @return
	 */
	public abstract Equation copy();

	
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		if (selected){
			if (owner.selected != null){
				owner.selected.setSelected(false);
			}
			owner.selected =this;
		}else if (owner.selected.equals(this)){
			owner.selected = null;
		}
		this.selected = selected;
	}

	ArrayList<Point> lastPoint =new ArrayList<Point>();
	protected int myWidth;
	protected int myHeight;
	
	@Override
	public boolean add(Equation equation) {
		boolean result = super.add(equation);
		equation.parent =this;
		return result;
	}
	
	
	abstract public EquationLoc closestPossible(float x, float y);
	public boolean canAdd(Equation eq){
		if (eq.equals(this)){
			return false;
		}
		Equation lcc = lowestCommonContainer(eq);
		if (lcc.addContain(this) && lcc.addContain(eq)){
			return true;
		}
		return false;
	}
	public boolean canMulti(Equation eq){
		if (eq.equals(this)){
			return false;
		}
		Equation lcc = lowestCommonContainer(eq);
		if (lcc.DivMultiContain(this) && lcc.DivMultiContain(eq)){
			// if these are on different side
			if (lcc instanceof EqualsEquation){
				// we need them to have different topnesses
				EqualsEquation top = (EqualsEquation) lcc;
				MultiDivSuperEquation myTop= (MultiDivSuperEquation)top.get(top.side(this));
				MultiDivSuperEquation eqTop= (MultiDivSuperEquation)top.get(top.side(eq));
				return myTop.onTop(this) != eqTop.onTop(eq);
			}else{
				// if they are on the same side they need the same topness
				MultiDivSuperEquation lccmdse = (MultiDivSuperEquation) lcc;
				return lccmdse.onTop(this) == lccmdse.onTop(eq);
			}
		}
		return false;
	}
	abstract public boolean canInstertAt(int pos, Equation e);
	public boolean canDiv(Equation eq){
		if (eq.equals(this)){
			return false;
		}
		Equation lcc = lowestCommonContainer(eq);
		if (lcc.DivMultiContain(this) && lcc.DivMultiContain(eq)){
			// if these are on different side
			if (lcc instanceof EqualsEquation){
				// we need them to have the same topnesses
				EqualsEquation top = (EqualsEquation) lcc;
				MultiDivSuperEquation myTop= (MultiDivSuperEquation)top.get(top.side(this));
				MultiDivSuperEquation eqTop= (MultiDivSuperEquation)top.get(top.side(eq));
				return myTop.onTop(this) == eqTop.onTop(eq);
			}else{
				// if they are on the same side they to have different topness
				MultiDivSuperEquation lccmdse = (MultiDivSuperEquation) lcc;
				return lccmdse.onTop(this) != lccmdse.onTop(eq);
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public ArrayList<EquationDis> closest(float x, float y){
		ArrayList<EquationDis> result= new ArrayList<EquationDis>();
		for (int i=0;i<size();i++){
			result.addAll(get(i).closest(x, y));
		}
		Collections.sort(result);
		return result;
	}
	
	public abstract boolean isFlex();
	public abstract float measureWidth();
	/**
	 * x,y is the center of the equation to be drawn
	 */
	public abstract void draw(Canvas canvas,float x, float y);
	
	public boolean deepContains(Equation equation){
		for (int i=0;i<size();i++){
			if (get(i).deepContains(equation)){
				return true;
			}
		}
		return false;
	}
	
	public abstract float measureHeight();
	
	public boolean on(float x, float y){
		Log.i("yo,yo",x+","+y);
		boolean result =false;
		for (int i=0;i<lastPoint.size();i++){
			//TODO should be measureHeight and width??
			if (x < lastPoint.get(i).x + myWidth/2 
					&& x > lastPoint.get(i).x - myWidth/2 
					&& y < lastPoint.get(i).y + myHeight/2 
					&& y > lastPoint.get(i).y - myHeight/2){
				result = true;
				setSelected(true);
			}
		}
		return result;
	}
	
	public boolean onAny(float x, float y){
		boolean result =on(x,y);
		if (result){return result;}
		for (int i=0;i<size();i++){
			result =get(i).on(x,y);
			if (result){return result;}
		}
		return result;
	}

 	protected void horizDraw(Canvas canvas, float x, float y) {
 		lastPoint =new ArrayList<Point>();
		float totalWidth = measureWidth();
		float currentX=0;
		Paint temp = textPaint;
		if (selected){
			temp = new Paint(textPaint);
			temp.setColor(Color.GREEN);
			
		}
		if (parentheses){
			drawParentheses(canvas,x,y,temp);
			temp.setTextSize(measureHeight());
			totalWidth -= temp.measureText("()");
		} 
		for (int i=0;i<size();i++){
			float currentWidth = get(i).measureWidth();
			get(i).draw(canvas, x - (totalWidth/2) + currentX + (currentWidth/2),y);
			currentX+=get(i).measureWidth();
			if (i != size()-1){
				canvas.drawText(display, x - (totalWidth/2) + currentX + (myWidth/2), y, temp);
				Point point = new Point();
				point.x =(int)( x - (totalWidth/2) + currentX + (myWidth/2));
				point.y = (int)  ( y);
				lastPoint.add(point);
				currentX+=myWidth;
			}
		}
	}
	
 	protected float horizMeasureHeight() {
		float totalHeight = myHeight;
		
		for(int i = 0; i<size(); i++) {
			if(get(i).measureHeight() > totalHeight) {
				totalHeight = get(i).measureHeight();
			}
		}
		if (parentheses){
			totalHeight += PARN_HEIGHT_ADDITION;
		}
		
		return totalHeight;
	}

 	protected float horizMeasureWidth() {
		float totalWidth = 0;
		
		for(int i = 0; i<size(); i++) {
			totalWidth += get(i).measureWidth();
		}
		totalWidth += (size()-1)*myWidth;
		
		if (parentheses){
			//TODO test
			Paint p = new Paint();
			p.setTextSize(measureHeight());
			totalWidth += p.measureText("()");
		}
		
		return totalWidth;
	}
	
 	protected void vertDraw(Canvas canvas, float x, float y) {
		lastPoint =new ArrayList<Point>();
		float totalHieght = measureWidth();
		float currentY=0;
		Paint temp = textPaint;
		if (selected){
			temp = new Paint(textPaint);
			temp.setColor(Color.GREEN);
			
		}
		if (parentheses){
			drawParentheses(canvas,x,y,temp);
			totalHieght = totalHieght-PARN_HEIGHT_ADDITION;
		} 
		for (int i=0;i<size();i++){
			float currentHieght = get(i).measureHeight();
			get(i).draw(canvas, x,y - (totalHieght/2) + currentY + (currentHieght/2));
			currentY+=currentHieght;
			if (i != size()-1){
				canvas.drawText(display, x, y - (totalHieght/2) + currentY + (myWidth/2), temp);
				Point point = new Point();
				point.x =(int) x;
				point.y = (int)  (y - (totalHieght/2) + currentY + (myWidth/2));
				lastPoint.add(point);
				currentY+=myHeight;
			}
		}
	}
	
 	protected float vertMeasureHeight() {
		float totalHeight = myHeight;
		
		for(int i = 0; i<size(); i++) {
			totalHeight = get(i).measureHeight();
		}
		if (parentheses){
			totalHeight += PARN_HEIGHT_ADDITION;
		}
		totalHeight += (size()-1)*myHeight;
		return totalHeight;
	}

 	protected float vertMeasureWidth() {
		float maxWidth = 0;
		
		for(int i = 0; i<size(); i++) {
			if(get(i).measureHeight() > maxWidth) {
				maxWidth = get(i).measureHeight();
			}
		}
		if (parentheses){
			//TODO test
			Paint p = new Paint();
			p.setTextSize(measureHeight());
			maxWidth += p.measureText("()");
		}
		
		return maxWidth;
	}
	
	public Equation lowestCommonContainer(Equation eq) {
		// TODO slow
		Equation at = parent;
		while (true){
			if (at.deepContains(eq)){ 
				return at;
			}else{
				at = at.parent;
			}
		}
	}
	
	protected void drawParentheses(Canvas canvas ,float x, float y, Paint temp) {
		Paint ptemp = new Paint(temp);
		ptemp.setTextSize(measureHeight());
		canvas.drawText("(", x - (measureWidth()/2)+(ptemp.measureText("(")/2), y, ptemp);
		canvas.drawText(")", x + (measureWidth()/2)+(ptemp.measureText("(")/2), y, ptemp);		
	}
	
	public boolean addContain(Equation equation){
		Equation current = equation;
		if (equation.equals(this)){
			if (this instanceof AddEquation || this instanceof EqualsEquation){
				return true;
			}else{
				return false;
			}
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
	
	/**
	 * check to see if all the generations between an equation and this are divEquations or MultiEquations
	 * equations are considered to contain themselves
	 * @param equation
	 * @return
	 */
	public boolean DivMultiContain(Equation equation){
		Equation current = equation;
		if (equation.equals(this)){
			if (this instanceof MultiDivSuperEquation || this instanceof EqualsEquation){
				return true;
			}else{
				return false;
			}
		}
		while (true){
			if (current.parent.equals(this)){
				return true;
			}else if (current.parent instanceof MultiDivSuperEquation){
				current = current.parent;
			}else{
				return false;
			}
		}
	}
	
	@Override
	public boolean equals(Object x){
		return this==x;
	}
}
