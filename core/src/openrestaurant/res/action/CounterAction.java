package openrestaurant.res.action;

import java.util.Iterator;

import openrestaurant.res.Person;
import openrestaurant.res.Resources;
import openrestaurant.res.item.Note;
import openrestaurant.res.item.Plate;
import openrestaurant.res.item.Plate.Content;
import openrestaurant.res.object.Counter;

public class CounterAction extends Action {

	private final Counter counter;
	private final boolean maid;

	public CounterAction(Resources r, Counter counter, boolean maid) {
		super(r);
		this.counter = counter;
		this.maid = maid;
	}
	
	@SuppressWarnings("unused")
	@Override
	public void doAction(Person person, double delta) {
		if (maid) {
			person.idle.txRow=1;
			
			if (!counter.teapotCounter) {
				for (Note note : person.notes) {
					counter.notes.add((Note) note);
				}
				person.notes.clear();
	
				if (person.plates.size()>0) {
					
					for(Iterator<Plate> it = person.plates.iterator();it.hasNext();) {
						Plate f = it.next();
						if (f.content==Content.EMPTY) {
							counter.foods.add(f);
							it.remove();
						}
					}				
				}
				
				for(Iterator<Plate> it = counter.foods.iterator();it.hasNext();) {
					if (person.plates.size()>=2) break;
					
					Plate food = it.next();
					
					if (food.content==Content.FOOD) {
						person.plates.add(food);
						it.remove();
					}
				}
			} else {
				Plate drink = null;
				for (Plate f : person.plates) {
					if (f.content==Content.DRINK) {
						drink = f;
						break;
					}
				}
				if (drink!=null) {
					person.plates.remove(drink);
				} else {
					if (person.plates.size()<2) {
						person.plates.add(new Plate(null, Content.DRINK));
					}
				}
			}
		} else {
			if (!counter.teapotCounter) {
				if (counter.foods.size()==0 && person.plates.size()>0) {
					Plate food = person.plates.get(0);
					counter.foods.add(food);
					person.plates.remove(food);
				}
				
				for(Iterator<Plate> it = counter.foods.iterator();it.hasNext();) {
					Plate food = it.next();
					
					if (food.content==Content.EMPTY) {
						person.plates.add(food);
						it.remove();
					}
				}
				
				for (Note note : counter.notes) {
					person.notes.add(note);
					
					CookAction cook = new CookAction(r);
					person.add(cook);
				}
				counter.notes.clear();
			}
		}
		super.doAction(person, delta);
	}	
	
}
