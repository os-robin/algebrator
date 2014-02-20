package com.algebrator.eq;
import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Paint;

abstract public class Equation extends ArrayList<Equation>{
	public static final int DEFAULT_SIZE = 50;
	Equation parent;
	String display;
	Paint textPaint; 
	protected int myWidth;
	protected int myHeight;
	public abstract boolean isFlex();
	public abstract float measureWidth();
	/**
	 * x,y is the center of the equation to be drawn
	 */
	public abstract void draw(Canvas canvas,float x, float y);
	public abstract float measureHeight();
}