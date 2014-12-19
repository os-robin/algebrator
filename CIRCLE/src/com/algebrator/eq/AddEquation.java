package com.algebrator.eq;

import java.util.ArrayList;
import java.util.HashSet;

import android.util.Log;

import com.example.circle.SuperView;

public class AddEquation extends Operation {
	Equation empty;

	@Override
	public Equation copy() {
		Equation result = new AddEquation(this.owner);
		result.display = this.getDisplay(-1);
		result.parentheses = this.parentheses;
		// pass selected?

		// copy all the kiddos and set this as their parent
		for (int i = 0; i < this.size(); i++) {
			result.add(this.get(i).copy());
			result.get(i).parent = result;
		}
		return result;
	}

	public String getDisplay(int pos) {
		if (pos >= 0 && pos < size()) {
			if (get(pos).negative) {
				return "–";
			}
		}
		return display;
	}

	public AddEquation(SuperView owner) {
		super(owner);
		display = "+";
		myWidth = DEFAULT_SIZE;
		myHeight = DEFAULT_SIZE;

		empty = new NumConstEquation("0", owner);
	}

	public void tryOperator(Equation a, Equation b) {
		if (indexOf(a) != Math.min(indexOf(a), indexOf(b))) {
			Log.e("", "noo good");
		}

		while (a instanceof AddEquation) {
			a = a.get(a.size() - 1);
		}
		while (b instanceof AddEquation) {
			b = b.get(b.size() - 1);
		}
		Equation result = null;
		if (a instanceof NumConstEquation && b instanceof NumConstEquation) {
			NumConstEquation aa = (NumConstEquation) a;
			NumConstEquation bb = (NumConstEquation) b;
			double newValue = aa.getValue() + bb.getValue();
			result = new NumConstEquation(Math.abs(newValue) + "", owner);
			if (newValue < 0) {
				result.negative = true;
			}

		} else {
			HashSet<CountData> counts = new HashSet<CountData>();

			for (Equation e : new Equation[] { a, b }) {
				updateCounts(e, counts);
			}
			result = new MultiEquation(owner);
			// if there is only one term #*keys...
			if (counts.size() == 1) {
				CountData cd = (CountData) counts.toArray()[0];
				result.add(new NumConstEquation(cd.value + "", owner));
				for (Equation eq : cd.key) {
					result.add(eq);
				}
			} else {
				// there must be 2
				CountData cd1 = (CountData) counts.toArray()[0];
				CountData cd2 = (CountData) counts.toArray()[1];
				HashSet<Equation> common = cd1.common(cd2);
				// if there is multiple and they have no commonality - cancel
				if (common.size() == 0) {
					return;
					// if they have commonality write (X+Y)*COMMON
				} else {
					cd1.rem = cd1.remainder(common);
					cd2.rem = cd2.remainder(common);
					result = new MultiEquation(owner);
					ArrayList<CountData> x = new ArrayList<CountData>();
					x.add(cd1);
					x.add(cd2);
					AddEquation add = new AddEquation(owner);
					for (CountData cd : x) {
						if (cd.rem.size() == 0) {
							NumConstEquation num = new NumConstEquation(cd.value
									+ "", owner);
							add.add(num);
						} else {
							MultiEquation me = new MultiEquation(owner);
							if (cd.value != 1) {
								NumConstEquation num = new NumConstEquation(
										cd.value + "", owner);
								me.add(num);
							}
							for (Equation e : cd.rem) {
								me.add(e);
							}
							add.add(me);
						}
					}
					result.add(add);
					// now we add common:
					if (common.size() == 1) {
						result.add((Equation) common.toArray()[0]);
					} else {
						MultiEquation me = new MultiEquation(owner);
						for (Equation e : common) {
							me.add(e);
						}
						result.add(me);
					}
				}
			}

		}
		add(indexOf(b), result);
		a.remove();
		b.remove();
	}

	private CountData updateCounts(Equation e, HashSet<CountData> counts) {
		CountData cd = new CountData();
		updateKey(e, cd);

		boolean hasMatch = false;
		for (CountData countData : counts) {
			if (countData.matches(cd)) {
				hasMatch = true;
				countData.value += cd.value;
			}
		}
		if (!hasMatch) {
			counts.add(cd);
		}
		return cd;

	}

	private void updateKey(Equation e, CountData cd) {
		if (e instanceof MultiEquation) {
			for (Equation ee : e) {
				updateKey(ee, cd);
			}
		} else if (e instanceof LeafEquation) {
			if (e instanceof NumConstEquation) {
				NumConstEquation ee = (NumConstEquation) e;
				cd.value *= ee.getValue();
			} else {
				cd.key.add(e);
			}
		} else if (e instanceof DivEquation) {
			Equation ee = e.copy();
			ee.set(0, new NumConstEquation("1", owner));
			cd.key.add(ee);
			updateKey(e.get(0), cd);
		}
	}
}

class CountData {
	public HashSet<Equation> rem;
	public Double value = 1.0;
	public HashSet<Equation> key = new HashSet<Equation>();

	public CountData() {
	}

	/**
	 * things in key that are not in common
	 * 
	 * @param common
	 * @return
	 */
	public HashSet<Equation> remainder(HashSet<Equation> common) {
		HashSet<Equation> result = new HashSet<Equation>();
		for (Equation e : key) {
			boolean pass = true;
			for (Equation e2 : common) {
				if (e.same(e2)) {
					pass = false;
					break;
				}
			}
			if (pass) {
				result.add(e);
			}
		}
		return result;
	}

	/**
	 * things in this.key also in cd2.key
	 * 
	 * @param common
	 * @return
	 */
	public HashSet<Equation> common(CountData cd2) {
		HashSet<Equation> result = new HashSet<Equation>();
		for (Equation e : key) {
			for (Equation e2 : cd2.key) {
				if (e.same(e2)) {
					result.add(e);
					break;
				}
			}
		}
		return result;
	}

	public boolean matches(CountData cd) {
		// if everything in this is the same as something in the other
		for (Equation e : key) {
			boolean any = false;
			for (Equation ee : cd.key) {
				if (ee.same(e)) {
					any = true;
					break;
				}
			}
			if (!any) {
				return false;
			}
		}
		return true;
	}
}
