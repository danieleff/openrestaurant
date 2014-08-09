package openrestaurant.res.object;

import java.util.ArrayList;
import java.util.List;

import openrestaurant.Pos;
import openrestaurant.res.Chair;
import openrestaurant.res.Person;
import openrestaurant.res.Resources;
import openrestaurant.res.action.TableAction;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

public class Table extends Actor {

	private float hit=0;
	
	public float timeToPay;
	
	final Resources r;
	
	public final List<Chair> chairs = new ArrayList<Chair>();

	public final int tileX;

	public final int tileY;

	public final int id;
	
	public Status status = Status.EMPTY;
	
	public float maxHeart;
	
	public float heart;
	
	public float waitOk;

	public boolean hasDrink;

	public Table(Resources r, int tileX, int tileY, int id) {
		this.r = r;
		this.tileX = tileX;
		this.tileY = tileY;
		this.id = id;
		setPosition(tileX * r.logic.tileWidth, tileY * r.logic.tileHeight + 0.5f);
		
		addListener(new ThisListener());
	}
	
	public void addChair(Chair chair) {
		chairs.add(chair);
	}
	
	@Override
	public void draw(Batch spriteBatch, float arg1) {
		if (hit>0) { 
			spriteBatch.setColor((float)Math.sin(hit*6), 1, 1, 1);
		}
		
		spriteBatch.draw(r.table, getX() , getY() -3);
		if (hit>0) { 
			spriteBatch.setColor(1, 1, 1, 1);
		}
		
		if (status == Status.ORDERING) {
			spriteBatch.draw(r.carte, getX() + 20,getY() + 30, 20, 20);
		}
		if (status == Status.EATING) {
			spriteBatch.draw(r.food, getX() +15, getY()+30);
		} 
		if (status==Status.WAITING_PAY) {
			spriteBatch.draw(r.plate, getX() +15, getY()+30);
		} 
		if (status==Status.DIRTY) {
			spriteBatch.draw(r.plate, getX() +15, getY()+30);
		}
		
		if (hasDrink) {
			spriteBatch.draw(r.cup, getX() +25, getY()+30);
		}
		
		r.font17.draw(spriteBatch, id+" "/*+String.format("%.2f", heart)*/, getX()+24, getY()+55);
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		hit-=delta;
		
		if (status == Status.EATING) {
			timeToPay-=delta;
			if (timeToPay<0) {
				status = Status.WAITING_PAY;
			}
		}
		
		if (status == Status.ORDERING || status == Status.WAITING_FOOD || status ==Status.WAITING_PAY) {
			float speed = 0.1f;
			if (waitOk>0) {
				waitOk-=delta * speed;
			} else {
				heart = (float) Math.max(0, heart - delta * speed);
			}
		}
		
	}

	@Override
	public Actor hit(float x, float y, boolean touchable) {
		if (x>0 && x<r.table.getRegionHeight() &&
				y>0 && y<r.table.getRegionWidth()
				) {
			return this;			
		}
		return null;
	}
	
	private class ThisListener extends InputListener {
		
		@Override
		public boolean touchDown(InputEvent e, float x, float y, int pointer, int button) {
			return true;
		}
		@Override
		public void touchUp(InputEvent e, float x, float y, int pointer, int button) {
			if (hit(x, y, true)==Table.this) {
				hit = 0.5f;
				
				TableAction action = new TableAction(r, Table.this);
				action.ends.add(new Pos(tileX-1, tileY));
				action.ends.add(new Pos(tileX-1, tileY+1));
				action.ends.add(new Pos(tileX+2, tileY));
				action.ends.add(new Pos(tileX+2, tileY+1));
				r.logic.player.add(action);
			}
		}
	}
	
	public static enum Status {
		EMPTY,
		TAKING_SEAT,
		ORDERING,
		WAITING_FOOD,
		EATING,
		WAITING_PAY,
		DIRTY,
	}

	public void seatTaken(Chair chair, Person person) {
		boolean allSeatsTaken = true; 
		for (Chair c : chairs) {
			if (c.reserved && c.person==null) {
				allSeatsTaken = false;
			}
		}
		if (allSeatsTaken) {
			status = Status.ORDERING;
		}
	}

}
