package com.example.circle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PorterDuff.Mode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;

public class ColinView extends SuperView {
	
	public ColinView(Context context) {
		super(context);
		init(context);
	}

	public ColinView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public ColinView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}
	
	protected void init(Context context) {
		
	}

	@Override
	public boolean onTouch(View view, MotionEvent event) {
		return super.onTouch(view, event);
	}
}
