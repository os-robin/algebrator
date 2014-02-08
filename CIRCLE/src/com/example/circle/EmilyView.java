package com.example.circle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class EmilyView extends SurfaceView implements Runnable {

	Thread thread = null;
	SurfaceHolder surfaceHolder;
	volatile boolean running = false;
	
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
	
	int x=0;
	int y=0;
	
	private void myDraw(Canvas canvas) {
		Paint paint = new Paint();
		paint.setColor(0xff000000+x*0x100);
		canvas.drawRect(x++, y++, x+100, y+100, paint);
	}
}
