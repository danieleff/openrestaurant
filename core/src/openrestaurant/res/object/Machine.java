package openrestaurant.res.object;

import openrestaurant.res.Resources;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Machine extends Actor {

	private final Resources r;
	
	public Machine(Resources r, int tileX, int tileY) {
		this.r = r;
		setPosition(tileX * r.logic.tileWidth, tileY * r.logic.tileHeight + 0.5f);
	}
	
	@Override
	public void draw(Batch spriteBatch, float arg1) {
		spriteBatch.draw(r.machine, getX(), getY());
	}

	@Override
	public Actor hit(float arg0, float arg1, boolean touchable) {
		return null;
	}

	
	
}
