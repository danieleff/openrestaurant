package openrestaurant.res.action;

import openrestaurant.res.Person;
import openrestaurant.res.Resources;

public class EnterAction extends Action {

	public EnterAction(Resources r) {
		super(r);
	}

	@Override
	public void act(Person person, float delta) {
		boolean canEnter=true;		
		for (Person other : r.logic.customers) {
			if ((other.tileX==tileX) && (other.tileY==tileY || other.tileY==tileY-1)) {
				canEnter=false;
				break;
			}
		}
		if (canEnter) {
			person.setPosition(tileX, tileY);
			person.removeCurrentAction();
		}
	}
	
}
