package openrestaurant.res.action;

import openrestaurant.res.Chair;
import openrestaurant.res.Person;
import openrestaurant.res.Resources;

public class TakeSeatAction extends Action {

	private final Chair chair;

	public TakeSeatAction(Resources r, Chair chair) {
		super(r);
		this.chair = chair;
	}
	
	@Override
	public void doAction(Person person, double delta) {
		chair.take(person);
		super.doAction(person, delta);
	}

}
