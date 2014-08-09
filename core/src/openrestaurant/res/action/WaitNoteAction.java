package openrestaurant.res.action;

import openrestaurant.res.Person;
import openrestaurant.res.Resources;
import openrestaurant.res.item.Plate;
import openrestaurant.res.item.Plate.Content;
import openrestaurant.res.object.Counter;

public class WaitNoteAction extends IdleAction {

	public WaitNoteAction(Resources r) {
		super(r);
	}
	
	@Override
	public void act(Person p, float delta) {
		if (p.plates.size()>0) {
			boolean has=false;
			for (Plate f : p.plates) {
				if (f.content==Content.FOOD) has=true;
			}
			if (has) {
				for (Counter counter : r.logic.counters) {
					if (counter.teapotCounter) continue;
					
					if (counter.foods.size()==0) {
						
						CounterAction action = new CounterAction(r, counter, false);
						action.tileX=counter.tileX-1;
						action.tileY=counter.tileY;
						p.add(action);
						
						return;
					}
				}
			}
			for (Plate f : p.plates) {
				if (f.content==Content.EMPTY) {
					p.add(new CookAction(r));
					break;
				}
			}
			
		} else if (p.notes.size()>0) {
			p.add(new CookAction(r));
		}
		
		for (Counter counter : r.logic.counters) {
			if (counter.notes.size()>0) {
				CounterAction action = new CounterAction(r, counter, false);
				action.tileX=counter.tileX-1;
				action.tileY=counter.tileY;
				p.add(action);
				
				break;
			}
			if (counter.foods.size()>0) {
				for (Plate f : counter.foods) {
					if (f.content==Content.EMPTY) {
						CounterAction action = new CounterAction(r, counter, false);
						action.tileX=counter.tileX-1;
						action.tileY=counter.tileY;
						p.add(action);
						break;
					}
				}
			}
		}
		super.act(p, delta);
	}
	

}
