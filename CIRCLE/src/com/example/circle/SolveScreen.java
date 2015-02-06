package com.example.circle;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

/**
 * Created by Colin on 2/6/2015.
 */
public class SolveScreen extends SuperScreen {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myView = Algebrator.getAlgebrator().solveView;

        lookAt(myView);
    }
}
