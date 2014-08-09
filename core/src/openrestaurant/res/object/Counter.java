package openrestaurant.res.object;

import java.util.ArrayList;
import java.util.List;

import openrestaurant.Pos;
import openrestaurant.res.Resources;
import openrestaurant.res.action.CounterAction;
import openrestaurant.res.item.Note;
import openrestaurant.res.item.Plate;
import openrestaurant.res.item.Plate.Content;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

public class Counter extends Actor {

	private float hit=0;
	
	private final Resources r;
	
	public int res;

	public final int tileY;

	public final int tileX;
	
	public List<Note> notes = new ArrayList<Note>();
	
	public List<Plate> foods = new ArrayList<Plate>();

	private final Counter touchTarget;

	public final boolean teapotCounter;

	public Counter(Resources r, Counter touchTarget, int tileX, int tileY, boolean teapotCounter) {
		addListener(new ThisListener());
		
		this.r = r;
		this.teapotCounter = teapotCounter;
		if (touchTarget==null) touchTarget=this;
		this.touchTarget = touchTarget;
		this.tileX = tileX;
		this.tileY = tileY;
		setPosition(tileX * r.logic.tileWidth, tileY * r.logic.tileHeight);
	}

	
	@Override
	public void act(float delta) {
		super.act(delta);
		hit-=delta;
	}
	
	@Override
	public void draw(Batch spriteBatch, float arg1) {
		if (hit>0) 
			spriteBatch.setColor((float)Math.sin(hit*6), 1, 1, 1);
		spriteBatch.draw(r.counter, getX(), getY());
		if (hit>0) 
			spriteBatch.setColor(1, 1, 1, 1);
		
		if (notes.size()>0) {
			spriteBatch.draw(r.carte, getX() + 16, getY() + 5 + 24, 20, 20);
		}
		if (foods.size()>0) {
			if (foods.get(0).content==Content.FOOD) {
				spriteBatch.draw(r.food, getX()+16, getY()+24);
				r.font17.draw(spriteBatch, ""+foods.get(0).table.id, getX()+5, getY()+25);
			} else {
				spriteBatch.draw(r.plate, getX()+16, getY()+24);
			}
		}
		
		if (tileX > 2) {
			spriteBatch.draw(r.teapot, getX()+ 16, getY() + r.logic.tileHeight - 10);
		}
	}

	@Override
	public Actor hit(float x, float y, boolean touchable) {
		if (x>0 && 
				x<r.counter.getRegionHeight()*1 &&
				y>0 && 
				y<r.counter.getRegionWidth()
				) {
			return this;			
		}
		return null;
	}
	
	class ThisListener extends InputListener {
		
		@Override
		public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
			return true;
		}
		@Override
		public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
			if (hit(x, y, true)==Counter.this) {
				touchTarget.hit = 0.5f;
				
				CounterAction action = new CounterAction(r, touchTarget, true);
				action.ends.add(new Pos(tileX+2, tileY));
				action.ends.add(new Pos(tileX+2, tileY+1));
				
				if (tileX!=0) {
					action.ends.add(new Pos(tileX-1, tileY));
					action.ends.add(new Pos(tileX-1, tileY+1));
				}
				
				r.logic.player.add(action);
			}
		}
	}

}
