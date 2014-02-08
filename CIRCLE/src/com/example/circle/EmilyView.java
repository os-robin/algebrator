package com.example.circle;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;

public class EmilyView extends SurfaceView implements Runnable,OnTouchListener {

	Thread thread = null;
	SurfaceHolder surfaceHolder;
	volatile boolean running = false;
	
	ArrayList<Square> squares = new ArrayList<Square>();
	
	public EmilyView(Context context) {
		super(context);
		init();
	}

	public EmilyView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public EmilyView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		surfaceHolder = getHolder();
		this.setOnTouchListener(this);
	};


	public void onResume() {
		running = true;
		thread = new Thread(this);
		thread.start();
	}

	public void onPause() {
		boolean retry = true;
		running = false;
		while (retry) {
			try {
				thread.join();
				retry = false;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void run() {
		while (running) {
			if (surfaceHolder.getSurface().isValid()) {
				Canvas canvas = surfaceHolder.lockCanvas();
				myDraw(canvas);
				surfaceHolder.unlockCanvasAndPost(canvas);
			}
		}
	}
	

	
	private void myDraw(Canvas canvas) {
		Log.i("what is the size of squares?",squares.size()+"");
		for (int i=0;i<squares.size();i++){
			Log.i("i",""+i);
			squares.get(i).draw(canvas);
		}
		
		
	}

	@Override
	public boolean onTouch(View view, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_UP){
			Paint paint = new Paint();
			paint.setColor((int)(0xff000000+Math.random()*0x00ffffff));
			squares.add(new Square(event.getX()-10,event.getX()+10,event.getY()-10,event.getY()+10,paint));
		}
		return true;
	}
}
