package com.example.circle.Actions;

import com.algebrator.eq.PlaceholderEquation;
import com.example.circle.Actions.Action;
import com.example.circle.EmilyView;

/**
 * Created by Colin on 2/2/2015.
 */
public class LeftAction extends Action {
    public LeftAction(EmilyView emilyView) {
        super(emilyView);
    }
    @Override
    public void act() {
        if (emilyView.selected instanceof PlaceholderEquation) {
            tryMoveLeft();
        }
    }
}
