package com.example.circle;

import android.os.Bundle;

/**
 * Created by Colin on 2/6/2015.
 */
public class WriteScreen
        extends SuperScreen {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myView =  new EmilyView(this);

        Algebrator.getAlgebrator().solveView = myView;

        lookAt(myView);
    }
}
