package com.example.circle;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint.Align;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;

public class EmilyView extends SurfaceView implements Runnable, OnTouchListener {

	Thread thread = null;
	SurfaceHolder surfaceHolder;
	volatile boolean running = false;

	ArrayList<Button> buttons = new ArrayList<Button>(); // list of our buttons
	ArrayList<Heart> hearts = new ArrayList<Heart>();

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

		/*
		 * modified from Elliott Hughes' "Dalvik Explorer" app on
		 * http://stackoverflow
		 * .com/questions/1016896/how-to-get-screen-dimensions
		 * 
		 * WindowManager w = (WindowManager)
		 * context.getSystemService(Context.WINDOW_SERVICE); Display d =
		 * w.getDefaultDisplay(); DisplayMetrics metrics = new DisplayMetrics();
		 * d.getMetrics(metrics); // since SDK_INT = 1; int width =
		 * metrics.widthPixels; int height = metrics.heightPixels; // includes
		 * window decorations (statusbar bar/menu bar) if (Build.VERSION.SDK_INT
		 * >= 14 && Build.VERSION.SDK_INT < 17) try { width = (Integer)
		 * Display.class.getMethod("getRawWidth").invoke(d); height = (Integer)
		 * Display.class.getMethod("getRawHeight").invoke(d); } catch (Exception
		 * ignored) { } // includes window decorations (statusbar bar/menu bar)
		 * if (Build.VERSION.SDK_INT >= 17) try { Point realSize = new Point();
		 * Display.class.getMethod("getRealSize", Point.class).invoke(d,
		 * realSize); width = realSize.x; height = realSize.y; } catch
		 * (Exception ignored) { }
		 */

		// Get the height of the whole display
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);

		DisplayMetrics metrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(metrics);

		int screenheight = metrics.heightPixels; // get screen height (dependent
													// on rotation)
		int screenwidth = metrics.widthPixels; // get screen width (dependent on
												// rotation)

		Log.i("screenheight, screenwidth", screenheight + "," + screenwidth);

		// Get the heights of status, title, decorations, etc.
		Window win = ((Activity) context).getWindow();
		Rect rect = new Rect();

		win.getDecorView().getWindowVisibleDisplayFrame(rect);
		int statusBarHeight = rect.top; // Get the height of the status bar
		int contentViewTop = win.findViewById(Window.ID_ANDROID_CONTENT)
				.getTop(); // Get height occupied by decoration contents
		int titleBarHeight = contentViewTop - statusBarHeight;

		Log.i("statusBarHeight, contentViewTop, titleBarHeight",
				statusBarHeight + "," + contentViewTop + "," + titleBarHeight);

		// Get navigation bar height
		int navBarHeight = 0;
		/*
		 * Resources resources = context.getResources(); int resourceId =
		 * resources.getIdentifier("navigation_bar_height", "dimen", "android");
		 * if (resourceId > 0) { navBarHeight =
		 * resources.getDimensionPixelSize(resourceId); }
		 * 
		 * Log.i("navBarHeight", navBarHeight+"");
		 */

		// Calculate actual height
		int height = screenheight
				- (titleBarHeight + statusBarHeight + navBarHeight);
		int width = screenwidth;

		Log.i("actual height, width", height + ", " + width);
		
		hearts.add(new Heart(width/2, height/4, 500, 0x00000000));
		
		TextPaint text = new TextPaint();
		text.setTextSize(30);
		text.setTextAlign(Align.CENTER);
		text.setColor(0xffffffff);

		/*
		for (int i = 0; i < 5; i++) {
			buttons.add(new Button(i*width/11, (i+1)*width/11, 4*height/6, 5*height/6,
					0xff000000+(int)(Math.random()*0xffffff), i+1+"", text));
		}

		buttons.add(new Button(5*width/11, 6*width/11, 4*height/6, 5*height/6, 0xff000000+(int)(Math.random()*0xffffff), "( )", text));
		buttons.add(new Button(6*width/11, 7*width/11, 4*height/6, 5*height/6, 0xff000000+(int)(Math.random()*0xffffff), "*", text));
		buttons.add(new Button(7*width/11, 8*width/11, 4*height/6, 5*height/6, 0xff000000+(int)(Math.random()*0xffffff), "+", text));
		buttons.add(new Button(8*width/11, 9*width/11, 4*height/6, 5*height/6, 0xff000000+(int)(Math.random()*0xffffff), "x", text));
		buttons.add(new Button(9*width/11, 10*width/11, 4*height/6, 5*height/6, 0xff000000+(int)(Math.random()*0xffffff), "y", text));
		buttons.add(new Button(10*width/11, 11*width/11, 4*height/6, 5*height/6, 0xff000000+(int)(Math.random()*0xffffff), "VAR", text));

		for (int i = 0; i < 4; i++) {
			buttons.add(new Button((int)((i+0.5)*width/11), (int) ((i+1.5)*width/11), 5*height/6, height,
					0xff000000+(int)(Math.random()*0xffffff), i+6+"", text));
		}

		buttons.add(new Button((int) ((4 + 0.5) * width / 11), (int) ((5 + 0.5)
				* width / 11), 5 * height / 6, height, 0xff000000+(int)(Math.random()*0xffffff), "0", text));
		buttons.add(new Button((int) ((5 + 0.5) * width / 11), (int) ((6 + 0.5)
				* width / 11), 5 * height / 6, height, 0xff000000+(int)(Math.random()*0xffffff), ".", text));
		buttons.add(new Button((int) ((6 + 0.5) * width / 11), (int) ((7 + 0.5)
				* width / 11), 5 * height / 6, height, 0xff000000+(int)(Math.random()*0xffffff), "/", text));
		buttons.add(new Button((int) ((7 + 0.5) * width / 11), (int) ((8 + 0.5)
				* width / 11), 5 * height / 6, height, 0xff000000+(int)(Math.random()*0xffffff), "-", text));
		buttons.add(new Button((int) ((8 + 0.5) * width / 11), (int) ((9 + 0.5)
				* width / 11), 5 * height / 6, height, 0xff000000+(int)(Math.random()*0xffffff), "MORE", text));
		buttons.add(new Button((int) ((9 + 0.5) * width / 11), (int) ((10 + 0.5)
				* width / 11), 5 * height / 6, height, 0xff000000+(int)(Math.random()*0xffffff), "DEL", text));
		*/

		buttons.add(new Button(0*width/11, 1*width/11, 4*height/6, 5*height/6, 0xff000000+(int)(Math.random()*0xffffff), "W", text));
		buttons.add(new Button(1*width/11, 2*width/11, 4*height/6, 5*height/6, 0xff000000+(int)(Math.random()*0xffffff), "I", text));
		buttons.add(new Button(2*width/11, 3*width/11, 4*height/6, 5*height/6, 0xff000000+(int)(Math.random()*0xffffff), "L", text));
		buttons.add(new Button(3*width/11, 4*width/11, 4*height/6, 5*height/6, 0xff000000+(int)(Math.random()*0xffffff), "L", text));
		buttons.add(new Button(4*width/11, 5*width/11, 4*height/6, 5*height/6, 0xff000000+(int)(Math.random()*0xffffff), "Y", text));
		buttons.add(new Button(5*width/11, 6*width/11, 4*height/6, 5*height/6, 0xff000000+(int)(Math.random()*0xffffff), "O", text));
		buttons.add(new Button(6*width/11, 7*width/11, 4*height/6, 5*height/6, 0xff000000+(int)(Math.random()*0xffffff), "U", text));
		buttons.add(new Button(7*width/11, 8*width/11, 4*height/6, 5*height/6, 0xff000000+(int)(Math.random()*0xffffff), "B", text));
		buttons.add(new Button(8*width/11, 9*width/11, 4*height/6, 5*height/6, 0xff000000+(int)(Math.random()*0xffffff), "E", text));
		buttons.add(new Button(9*width/11, 10*width/11, 4*height/6, 5*height/6, 0xff000000+(int)(Math.random()*0xffffff), "M", text));
		buttons.add(new Button(10*width/11, 11*width/11, 4*height/6, 5*height/6, 0xff000000+(int)(Math.random()*0xffffff), "Y", text));
		buttons.add(new Button((int)(0.5*width/11), (int)(1.5*width/11), 5*height/6, 6*height/6, 0xff000000+(int)(Math.random()*0xffffff), "V", text));
		buttons.add(new Button((int)(1.5*width/11), (int)(2.5*width/11), 5*height/6, 6*height/6, 0xff000000+(int)(Math.random()*0xffffff), "A", text));
		buttons.add(new Button((int)(2.5*width/11), (int)(3.5*width/11), 5*height/6, 6*height/6, 0xff000000+(int)(Math.random()*0xffffff), "L", text));
		buttons.add(new Button((int)(3.5*width/11), (int)(4.5*width/11), 5*height/6, 6*height/6, 0xff000000+(int)(Math.random()*0xffffff), "E", text));
		buttons.add(new Button((int)(4.5*width/11), (int)(5.5*width/11), 5*height/6, 6*height/6, 0xff000000+(int)(Math.random()*0xffffff), "N", text));
		buttons.add(new Button((int)(5.5*width/11), (int)(6.5*width/11), 5*height/6, 6*height/6, 0xff000000+(int)(Math.random()*0xffffff), "T", text));
		buttons.add(new Button((int)(6.5*width/11), (int)(7.5*width/11), 5*height/6, 6*height/6, 0xff000000+(int)(Math.random()*0xffffff), "I", text));
		buttons.add(new Button((int)(7.5*width/11), (int)(8.5*width/11), 5*height/6, 6*height/6, 0xff000000+(int)(Math.random()*0xffffff), "N", text));
		buttons.add(new Button((int)(8.5*width/11), (int)(9.5*width/11), 5*height/6, 6*height/6, 0xff000000+(int)(Math.random()*0xffffff), "E", text));
		buttons.add(new Button((int)(9.5*width/11), (int)(10.5*width/11), 5*height/6, 6*height/6, 0xff000000+(int)(Math.random()*0xffffff), "?", text));
		
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
		canvas.drawColor(0, Mode.CLEAR);
		for (int i = 0; i < buttons.size(); i++) {
			int currentColor = buttons.get(i).bgpaint.getColor();
			buttons.get(i).draw(canvas);
			buttons.get(i).bgpaint.setColor((int) Math.ceil((currentColor + buttons.get(i).color)/2));
		}
		
		for (int i = 0; i < hearts.size(); i++) {
			hearts.get(0).draw(canvas);
		}
				
	}

	@Override
	public boolean onTouch(View view, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_UP) {

			for (int i = 0; i < buttons.size(); i++) {
				buttons.get(i).click(event);
								
			}
			
		}

		return true;
	}
}
