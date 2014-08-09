package openrestaurant.res.item;

import openrestaurant.res.object.Table;

public class Plate extends Item {

	public final Table table;
	public final Content content;

	public Plate(Table table, Content content) {
		this.table = table;
		this.content = content;
		
	}
	
	public static enum Content {
		EMPTY,
		FOOD,
		DRINK,
	}
	
}
