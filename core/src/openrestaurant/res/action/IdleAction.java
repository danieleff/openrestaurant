package openrestaurant.res.action;

import openrestaurant.res.Person;
import openrestaurant.res.Resources;

public class IdleAction extends Action {

	public int txRow = 0;
	
	private float idleTime=0;
	
	public IdleAction(Resources r) {
		super(r);
	}
	
	public void act(Person p, float delta) {
		p.txRow = txRow;
		idleTime-=delta;
		if (idleTime<0) {
			idleTime = 3+r.logic.rnd.nextFloat()*3;
			p.txCol = r.logic.rnd.nextInt(4);
		}
	}

}
