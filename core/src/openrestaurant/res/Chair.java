package openrestaurant.res;

import openrestaurant.res.object.Table;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Chair extends Actor {

	private Resources r;
	
	private final boolean up;

	public boolean reserved;
	
	public Person person;

	public final int tileX;

	public final int tileY;

	private final Table table;
	
	public Chair(Resources r, Table table, int tileX, int tileY, boolean up) {
		this.r = r;
		this.table = table;
		this.tileX = tileX;
		this.tileY = tileY;
		this.up = up;
		this.setPosition(tileX * r.logic.tileWidth, tileY * r.logic.tileHeight);
		if (up) {
			this.setY( this.getY()-0.5f);			
		} else {
			this.setY( this.getY()+0.5f);
		}
	}
	
	@Override
	public void draw(Batch spriteBatch, float arg1) {
		boolean sitting = false;
		if (person!=null) {
			sitting = true;
		}
		if (up) {
			if (sitting) {
				person.idle.txRow=3;
				spriteBatch.draw(r.chairUp2, getX(), getY());
			} else {
				spriteBatch.draw(r.chairUp, getX(), getY());
			}
		} else {
			spriteBatch.draw(r.chairDown, getX(), getY());
		}
	}

	@Override
	public Actor hit(float arg0, float arg1, boolean touchable) {
		return null;
	}
	public void take(Person person) {
		this.person = person;
		table.seatTaken(this, person);
	}

	
	
}
