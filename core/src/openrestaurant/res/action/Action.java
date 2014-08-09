package openrestaurant.res.action;

import java.util.ArrayList;
import java.util.List;

import openrestaurant.Pos;
import openrestaurant.res.Person;
import openrestaurant.res.Resources;


public class Action {

	final Resources r;
	
	public int tileX;
	
	public int tileY;
	
	public ArrayList<Pos> ends=new ArrayList<Pos>();

	private List<Pos> path;
	
	public Action(Resources r) {
		this.r = r;
	}
	
	public void act(Person person, float delta) {
		/*if (person.tileX<1) {
			System.out.println(this+" "+this.getClass().getName());
		}*/
		
		if (path==null || path.size()>0) {
			doMove(person, delta);
		} else {
			doAction(person, delta);
		}
	}
	
	public void doMove(Person person, float delta) {
		float speed = 120;
		
		
		if (path==null) {
			if (ends.size()==0) {
				ends.add(new Pos(tileX, tileY));
			}
			path = r.logic.path(new Pos(person.tileX, person.tileY), ends);
		}
		Pos pos = path.get(0);
		
		if (person.tileX<pos.x) {
			person.setFinePosition(person.getX() + speed*delta, person.getY());
			if (person.tileX>=pos.x) {
				person.setPosition(pos.x, person.tileY); 
			}
			person.txRow = 2;
		} else if (Math.abs(person.getX() - pos.x * r.logic.tileWidth)>0.1f) {
			person.setFinePosition(person.getX() - speed*delta, person.getY());
			if (person.tileX<=pos.x-1) {
				person.setPosition(pos.x, person.tileY); 
			}
			person.txRow = 1;
		} else if (person.tileY<pos.y) {
			person.setFinePosition(person.getX(), person.getY() + speed*delta);
			if (person.tileY>=pos.y) {
				person.setPosition(person.tileX, pos.y); 
			}
			person.txRow = 3;
		} else if (Math.abs(person.getY() - pos.y * r.logic.tileHeight)>0.1f) {
			person.setFinePosition(person.getX(), person.getY() - speed*delta);
			if (person.tileY<=pos.y-1) {
				person.setPosition(person.tileX, pos.y);
			}
			person.txRow = 0;
		}
		
		if (Math.abs(person.getX() - pos.x * r.logic.tileWidth)<0.1f 
				&& Math.abs(person.getY() - pos.y * r.logic.tileHeight)<0.1f) {
			path.remove(0);
		}
		
		person.txCol=(int) ((r.logic.time*7)%4);
	}
	
	public void doAction(Person person, double delta) {
		person.removeCurrentAction();
	}
	
}
