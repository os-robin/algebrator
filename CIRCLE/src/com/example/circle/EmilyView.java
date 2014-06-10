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

import com.algebrator.eq.EqualsEquation;
import com.algebrator.eq.Equation;
import com.algebrator.eq.PlaceholderEquation;

public class EmilyView extends SurfaceView implements Runnable, OnTouchListener {
	public Equation selected;
	Thread thread = null;
	SurfaceHolder surfaceHolder;
	volatile boolean running = false;

/**
 	* 	list of our buttons
 */
	ArrayList<Button> buttons = new ArrayList<Button>();
	/**
	 * list of our passed in variables
	 */
	ArrayList<Button> vars = new ArrayList<Button>();
	ArrayList<String> varList = new ArrayList<String>();
	EqualsEquation stupid;
	
	int width;
	int height;
	
	TextPaint text = new TextPaint();
	
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
		stupid = new EqualsEquation(this);
		Equation x = new PlaceholderEquation(this);
		Equation y = new PlaceholderEquation(this);
		Log.e("zoom?",(x==y)+"");

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
		height = screenheight
				- (titleBarHeight + statusBarHeight + navBarHeight);
		width = screenwidth;

		Log.i("actual height, width", height + ", " + width);
		
		
		text.setTextSize(30);
		text.setTextAlign(Align.CENTER);
		text.setColor(0xffffffff);


		for (int i = 0; i < 5; i++) {
			buttons.add(new Button(i*width/11, (i+1)*width/11, 4*height/6, 5*height/6,
					0xff000000+(int)(Math.random()*0xffffff), i+1+"", text));
			buttons.get(i).myAction = new NumberAction(this, i+1+"");
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
			buttons.get(i+11).myAction = new NumberAction(this, i+6+"");
		}
		
		Button delete= new Button((int) ((9 + 0.5) * width / 11), (int) ((10 + 0.5)
				* width / 11), 5 * height / 6, height, 0xff000000+(int)(Math.random()*0xffffff), "DEL", text);
		delete.myAction = new DeleteAction(this);
		

		buttons.add(new Button((int) ((4 + 0.5) * width / 11), (int) ((5 + 0.5)
				* width / 11), 5 * height / 6, height, 0xff000000+(int)(Math.random()*0xffffff), "0", text));
		buttons.add(new Button((int) ((5 + 0.5) * width / 11), (int) ((6 + 0.5)
				* width / 11), 5 * height / 6, height, 0xff000000+(int)(Math.random()*0xffffff), ".", text));
		buttons.add(new Button((int) ((6 + 0.5) * width / 11), (int) ((7 + 0.5)
				* width / 11), 5 * height / 6, height, 0xff000000+(int)(Math.random()*0xffffff), "÷", text));
		buttons.add(new Button((int) ((7 + 0.5) * width / 11), (int) ((8 + 0.5)
				* width / 11), 5 * height / 6, height, 0xff000000+(int)(Math.random()*0xffffff), "-", text));
		buttons.add(new Button((int) ((8 + 0.5) * width / 11), (int) ((9 + 0.5)
				* width / 11), 5 * height / 6, height, 0xff000000+(int)(Math.random()*0xffffff), "MORE", text));
		buttons.add(delete);
		
		/*
		AddEquation add1 = new AddEquation();
		add1.add(new NumConstEquation("3"));
		add1.add(new NumConstEquation("4"));
		AddEquation add2 = new AddEquation();
		add2.add(new NumConstEquation("2"));
		add2.add(new NumConstEquation("5"));
		AddEquation add3 = new AddEquation();
		add3.add(new NumConstEquation("1"));
		add3.add(new NumConstEquation("6"));
		stupid.add(add1);
		stupid.add(add2);
		stupid.add(add3); */
		
		PlaceholderEquation empty1 = new PlaceholderEquation(this);
		empty1.setSelected(true);
		PlaceholderEquation empty2 = new PlaceholderEquation(this);
		stupid.add(empty1);
		stupid.add(empty2);
		
		
		
		buttons.get(10).myAction = new Action(){

			@Override
			public void act() {
				varList.add("A"); varList.add("B"); varList.add("C");
				if(vars.size()<3) {
					vars.add(new Button(width*(10-vars.size())/11, width*(11-vars.size())/11, 3*height/6, 4*height/6, 0xff000000+(int)(Math.random()*0xffffff), varList.get(vars.size()), text));
				}
				
			}
			
		};
		
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
			buttons.get(i).draw(canvas);
		}
		
		for (int i = 0; i < vars.size(); i++) {
			vars.get(i).draw(canvas);
		}
		
		stupid.draw(canvas, width/2, height/2);
		
	}

	@Override
	public boolean onTouch(View view, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_UP) {

			for (int i = 0; i < buttons.size(); i++) {
				buttons.get(i).click(event);
								
			}
			
			for (int i = 0; i < vars.size(); i++) {
				vars.get(i).click(event);
			}
			stupid.onAny(event.getX(), event.getY());
		}

		return true;
	}
}
