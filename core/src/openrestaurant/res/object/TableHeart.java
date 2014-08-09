package openrestaurant.res.object;

import openrestaurant.res.Resources;
import openrestaurant.res.object.Table.Status;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class TableHeart extends Actor {

	private Resources r;
	private final Table table;

	public TableHeart(Table table) {
		this.table = table;
		r = table.r;
		setPosition(table.getX(), table.getY() - 2f);
	}
	
	@Override
	public void draw(Batch spriteBatch, float arg1) {
		if (table.status!=Status.EMPTY && table.status!=Status.TAKING_SEAT && table.status!=Status.DIRTY) {
			for(int i=0;i<table.maxHeart;i++) {
				if (table.heart < (i+1) - 0.99f) {
					
				} else if (table.heart < (i+1) - 0.75f) {
					spriteBatch.draw(r.heart4, getX() + i * 16, getY() +5, 16, 16);
				} else if (table.heart < (i+1) - 0.5f) {
					spriteBatch.draw(r.heart3, getX()  + i * 16, getY()+5, 16, 16);
				} else if (table.heart < (i+1) - 0.25f) {
					spriteBatch.draw(r.heart2, getX()  + i * 16, getY()+5, 16, 16);
				} else {
					spriteBatch.draw(r.heart1, getX()  + i * 16, getY()+5, 16, 16);
				}
			}
		}
	}

	@Override
	public Actor hit(float arg0, float arg1, boolean touchable) {
		return null;
	}

}
