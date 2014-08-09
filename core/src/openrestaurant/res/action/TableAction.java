package openrestaurant.res.action;

import java.util.Iterator;

import openrestaurant.res.Chair;
import openrestaurant.res.Person;
import openrestaurant.res.Resources;
import openrestaurant.res.item.Note;
import openrestaurant.res.item.Plate;
import openrestaurant.res.item.Plate.Content;
import openrestaurant.res.object.Table;
import openrestaurant.res.object.Table.Status;

public class TableAction extends Action {

	private final Table table;

	public TableAction(Resources r, Table table) {
		super(r);
		this.table = table;
	}
	
	@Override
	public void doAction(Person person, double delta) {
		if (person.tileX > table.tileX) {
			person.idle.txRow=1;
		} else {
			person.idle.txRow=2;
		}
		
		if (table.status == Status.ORDERING) {
			table.status = Status.WAITING_FOOD;
			table.heart += 0.8f;
			table.waitOk = 0.6f;
			person.notes.add(new Note(r, table));
		} else if (table.status == Status.WAITING_PAY) {
			for (Chair chair : table.chairs) {
				chair.reserved=false;
				if (chair.person!=null) {
					chair.person.add(new ExitAction(r));
					chair.person = null;
				}
			}
			r.logic.level.heart+=table.heart * 100;
			table.status = Status.DIRTY;
		} else if (table.status == Status.DIRTY) {
			table.hasDrink=false;
			if (person.plates.size()<2) {
				person.plates.add(new Plate(table, Content.EMPTY));
				table.status = Status.EMPTY;
			}
		}

		for(Iterator<Plate> it = person.plates.iterator();it.hasNext();) {
			Plate food = it.next();
			if (food.table==table && food.content==Content.FOOD) {
				table.status = Status.EATING;
				table.timeToPay = table.maxHeart * 2 + r.logic.rnd.nextInt(4);
				
				table.heart += 1f;
				table.waitOk = 0.5f;
				it.remove();
			}
			if ((table.status==Status.ORDERING || 
					table.status==Status.EATING || 
					table.status==Status.WAITING_FOOD) &&
				  food.content==Content.DRINK && !table.hasDrink) {
				
				table.hasDrink=true;
				table.heart += 0.4f;
				table.waitOk = 0.4f;
				it.remove();
			}
		}
		
		super.doAction(person, delta);
	}	
	
}
