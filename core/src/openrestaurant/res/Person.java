package openrestaurant.res;

import java.util.ArrayList;
import java.util.List;

import openrestaurant.res.action.Action;
import openrestaurant.res.action.IdleAction;
import openrestaurant.res.item.Note;
import openrestaurant.res.item.Plate;
import openrestaurant.res.item.Plate.Content;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Person extends Actor {

	public int tileX;
	
	public int tileY;
	
	public int txRow;
	
	public int txCol;
	
	private final TextureRegion[][] images;
	
	private final Resources r;
	
	private List<Action> actions = new ArrayList<Action>();
	
	public List<Note> notes = new ArrayList<Note>();
	
	public List<Plate> plates = new ArrayList<Plate>();
	
	public IdleAction idle;
	
	public Person(Resources r, TextureRegion[][] images) {
		this.r = r;
		this.images = images;
		idle = new IdleAction(r);
	}
	
	public void setPosition(int tileX, int tileY) {
		super.setPosition(tileX * r.logic.tileWidth, tileY * r.logic.tileHeight);
		this.tileX = tileX;
		this.tileY = tileY;
	}
	public void setFinePosition(float x, float y) {
		setPosition(x, y);
		this.tileX = (int) Math.floor(x / r.logic.tileWidth);
		this.tileY = (int) Math.floor(y / r.logic.tileHeight);
	}
	
	public void act(float delta) {
		if (actions.size()>0) {
			Action action = actions.get(0);
			action.act(this, delta);
		} else {
			idle.act(this, delta);
		}
	}
	
	public void add(Action action) {
		actions.add(action);
	}
	public void removeCurrentAction() {
		actions.remove(0);
	}
	
	@Override
		public void draw(Batch spriteBatch, float arg1) {
		TextureRegion tex = images[txRow][txCol];
		spriteBatch.draw(tex, getX(), getY());
		
		//spriteBatch.draw(r.anger, x+5, y+30);
		
		if (notes.size()>0) {
			//r.font10.draw(spriteBatch, "note: "+notes.size(), x, y+35);
			spriteBatch.draw(r.carte, getX() + 5, getY() + 5, 20, 20);
		}
		if (plates.size()>0) {
			
			int dy=0;
			for (Plate f : plates) {
				if (f.content==Content.EMPTY) {
					spriteBatch.draw(r.plate, getX(), getY()+5 + dy);
				} else if (f.content == Content.FOOD) {					
					spriteBatch.draw(r.food, getX(), getY()+5 + dy);					
					r.font17.draw(spriteBatch, ""+f.table.id, getX()+5, getY()+23+dy);
				} else {
					spriteBatch.draw(r.plate, getX(), getY()+5 + dy);
					spriteBatch.draw(r.teapot, getX(), getY()+5 + dy);
				}
				dy-=15;
			}
		}
	}

	@Override
	public Actor hit(float arg0, float arg1, boolean thouchable) {
		return null;
	}
}
