package openrestaurant.res.action;

import openrestaurant.res.Person;
import openrestaurant.res.Resources;

public class ExitAction extends Action {

	public ExitAction(Resources r) {
		super(r);
		tileX = 3;
		tileY = r.logic.tileCountY - 3;
	}

	@Override
	public void doAction(Person person, double delta) {
		r.logic.customers.remove(person);
		person.remove();
		super.doAction(person, delta);
	}
	
}
