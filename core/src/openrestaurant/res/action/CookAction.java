package openrestaurant.res.action;

import java.util.Iterator;

import openrestaurant.res.Person;
import openrestaurant.res.Resources;
import openrestaurant.res.item.Note;
import openrestaurant.res.item.Plate;
import openrestaurant.res.item.Plate.Content;

public class CookAction extends Action {

	public CookAction(Resources r) {
		super(r);
		tileX=0;
		tileY=r.logic.tileCountY - 4;
	}
	
	@Override
	public void act(Person person, float delta) {
		if (person.plates.size()>0) {
			boolean hasEmpty = false;
			for (Plate f : person.plates) {
				if (f.content==Content.EMPTY) hasEmpty=true; 
			}
			if (!hasEmpty) {
				person.removeCurrentAction();
				return;
			}
		}
		super.act(person, delta);
	}
	
	@Override
	public void doAction(Person person, double delta) {
		if (person.plates.size()>0) {
			person.txRow=3;
			
			for(Iterator<Plate> it = person.plates.iterator();it.hasNext();) {
				Plate food = it.next();
				if (food.content==Content.EMPTY) {
					it.remove();
				}
			}
		}
		
		if (person.notes.size()>0) {
			person.txRow=3;
			
			Note note = person.notes.get(0);
			note.timeToFood-=delta;
			if (note.timeToFood<0) {
				person.notes.remove(note);
				person.plates.add(new Plate(note.table, Content.FOOD));
			}
		} else {
			person.removeCurrentAction();
		}
		
	}

}
