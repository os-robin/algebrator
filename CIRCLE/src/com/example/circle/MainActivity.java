package com.example.circle;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;

public class MainActivity extends Activity {
	EmilyView emilyview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // If the Android version is lower than Jellybean, use this call to hide
        // the status bar.
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        
        if (Build.VERSION.SDK_INT >= 16) {
        	View decorView = getWindow().getDecorView();
        	// Hide the status bar.
        	int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        	decorView.setSystemUiVisibility(uiOptions);
        	// Remember that you should never show the action bar if the
        	// status bar is hidden, so hide that too if necessary.
        	ActionBar actionBar = getActionBar();
        	actionBar.hide();
        }
        
        emilyview = new EmilyView(this);
        setContentView(emilyview);
        
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    protected void onResume() {
     super.onResume();
     emilyview.onResume();
    }
    
    @Override
    protected void onPause() {
     super.onPause();
     emilyview.onPause();
    }
    
}
