package openrestaurant.res.item;

import openrestaurant.res.Resources;
import openrestaurant.res.object.Table;

public class Note extends Item {

	public final Table table;
	
	public float timeToFood;

	public Note(Resources r, Table table) {
		this.table = table;
		timeToFood=table.maxHeart;
	}

}
