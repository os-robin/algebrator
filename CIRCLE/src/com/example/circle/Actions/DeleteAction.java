package com.example.circle.Actions;

import com.algebrator.eq.EqualsEquation;
import com.algebrator.eq.Equation;
import com.algebrator.eq.NumConstEquation;
import com.algebrator.eq.PlaceholderEquation;
import com.example.circle.EmilyView;

public class DeleteAction extends Action {

	public String num;

	public DeleteAction(EmilyView emilyView) {
		super(emilyView);
	}

	@Override
	public void act() {
		Equation target = emilyView.selected;
		if (emilyView.selected instanceof NumConstEquation) {
			if (((NumConstEquation) emilyView.selected).getDisplay(-1).length() != 0) {
				((NumConstEquation) emilyView.selected).setDisplay( (String) ((NumConstEquation) emilyView.selected).getDisplay(-1)
						.subSequence(0,
								((NumConstEquation) emilyView.selected).getDisplay(-1)
										.length() - 1));
			}
			if (((NumConstEquation) emilyView.selected).getDisplay(-1).length() == 0) {
				PlaceholderEquation temp = new PlaceholderEquation(emilyView);
				int at = emilyView.selected.parent
						.lastIndexOf(emilyView.selected);
				emilyView.selected.parent.set(at, temp);
				temp.parent = emilyView.selected.parent;
				temp.setSelected(true);
			}
		}
		
		else if (emilyView.selected instanceof PlaceholderEquation){
			emilyView.selected.remove();
			if (target.isSelected()){
				target.setSelected(false);
			}
		}
		
		else if (!(emilyView.selected instanceof EqualsEquation)){
			Equation oldEq =  emilyView.selected;
			PlaceholderEquation temp = new PlaceholderEquation(emilyView);
			oldEq.replace(temp);
			if (target.isSelected()){
				target.setSelected(false);
			}
		}
		
		
		//else if (emilyView.selected instanceof EqualsEquation){
		//	emilyView.selected.remove();
		//}
	}
}
