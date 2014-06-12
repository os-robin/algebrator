package com.example.circle;

import com.algebrator.eq.NumConstEquation;
import com.algebrator.eq.PlaceholderEquation;
import com.algebrator.eq.VarEquation;

public class DeleteAction extends Action {

	public EmilyView emilyView;
	public String num;

	public DeleteAction(EmilyView emilyView) {
		super();
		this.emilyView = emilyView;
	}

	@Override
	public void act() {
		if (emilyView.selected instanceof NumConstEquation) {
			if (((NumConstEquation) emilyView.selected).display.length() != 0) {
				((NumConstEquation) emilyView.selected).display = (String) ((NumConstEquation) emilyView.selected).display
						.subSequence(0,
								((NumConstEquation) emilyView.selected).display
										.length() - 1);
			}
			if (((NumConstEquation) emilyView.selected).display.length() == 0) {
				PlaceholderEquation temp = new PlaceholderEquation(emilyView);
				int at = emilyView.selected.parent
						.lastIndexOf(emilyView.selected);
				emilyView.selected.parent.set(at, temp);
				temp.parent = emilyView.selected.parent;
				temp.setSelected(true);
			}
		}
		
		if (emilyView.selected instanceof VarEquation) {
			VarEquation oldEq = (VarEquation) emilyView.selected;
			// TODO replace
		}
	}
}
