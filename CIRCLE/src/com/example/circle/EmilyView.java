package com.example.circle;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;

public class EmilyView extends SurfaceView implements Runnable,OnTouchListener {

	Thread thread = null;
	SurfaceHolder surfaceHolder;
	volatile boolean running = false;
	
	ArrayList<Button> buttons = new ArrayList<Button>();	//list of our buttons
	
	public EmilyView(Context context) {
		super(context);
		init(context);
	}

	public EmilyView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public EmilyView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context) {
		surfaceHolder = getHolder();
		this.setOnTouchListener(this);
		
		/* modified from Elliott Hughes' "Dalvik Explorer" app
		 * on http://stackoverflow.com/questions/1016896/how-to-get-screen-dimensions
		 * 
		WindowManager w = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display d = w.getDefaultDisplay();
		DisplayMetrics metrics = new DisplayMetrics();
		d.getMetrics(metrics);
		// since SDK_INT = 1;
		int width = metrics.widthPixels;
		int height = metrics.heightPixels;
		// includes window decorations (statusbar bar/menu bar)
		if (Build.VERSION.SDK_INT >= 14 && Build.VERSION.SDK_INT < 17)
		try {
		    width = (Integer) Display.class.getMethod("getRawWidth").invoke(d);
		    height = (Integer) Display.class.getMethod("getRawHeight").invoke(d);
		} catch (Exception ignored) {
		}
		// includes window decorations (statusbar bar/menu bar)
		if (Build.VERSION.SDK_INT >= 17)
		try {
		    Point realSize = new Point();
		    Display.class.getMethod("getRealSize", Point.class).invoke(d, realSize);
		    width = realSize.x;
		    height = realSize.y;
		} catch (Exception ignored) {
		}
		*/
		
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		
		DisplayMetrics metrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(metrics);

		int height = metrics.heightPixels;	//get screen height (dependent on rotation)
		int width = metrics.widthPixels;	//get screen width (dependent on rotation)
		
		/*Display display = wm.getDefaultDisplay();
		
		if (android.os.Build.VERSION.SDK_INT >= 13) {
			Point size = new Point();
			display.getSize(size);
			int width = size.x;
			int height = size.y;
		}
		else {
			int width = display.getWidth();  // deprecated
			int height = display.getHeight();  // deprecated
		}*/
		
		Log.i("height, width", height+", "+width);
		
		TextPaint text = new TextPaint();
		text.setTextSize(30);
		text.setTextAlign(Align.CENTER);
		text.setColor(0xffffffff);
		
		Paint color = new Paint();
		color.setColor(0xff000000+(int)(Math.random()*0xffffff));
		
		for(int i=0; i<10; i++) {
			buttons.add(new Button(i*width/10, (i+1)*width/10, 4*height/6, 5*height/6, color, i+"", text));
			color.setColor(0xff000000+(int)(Math.random()*0xffffff));
		}
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
		for (int i=0;i<buttons.size();i++){
			buttons.get(i).draw(canvas);
		}
	}

	@Override
	public boolean onTouch(View view, MotionEvent event) {

		return true;
	}
}
