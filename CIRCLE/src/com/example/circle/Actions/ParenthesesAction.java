package com.example.circle.Actions;

import com.example.circle.EmilyView;

public class ParenthesesAction extends Action {

    public ParenthesesAction(EmilyView emilyView) {
        super(emilyView);
    }

    @Override
    public void act() {
        emilyView.selected.parentheses = !emilyView.selected.parentheses;
    }

}
