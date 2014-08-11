package openrestaurant.res.object;

import openrestaurant.res.Resources;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Floor extends Actor {

	private final Resources r;

	public Floor(Resources r) {
		this.r = r;
		
		setBounds(0, 0, r.logic.tileCountX * r.logic.tileWidth, r.logic.tileCountY * r.logic.tileHeight);
	}
	
	@Override
	public void draw(Batch spriteBatch, float arg1) {
		spriteBatch.draw(r.floor, 
				0, 0, 
				(int)getWidth(), (int)getHeight(), 
				1/64f, 1/64f, 
				r.logic.tileCountX - 1/64f, (r.logic.tileCountY) - 1/64f);
		
		if (r.logic.tutorialText!=null) {
			int y=4;
			for (String text : r.logic.tutorialText) {
				r.font17.drawMultiLine(spriteBatch, text, r.logic.tileWidth * 8, r.logic.tileHeight * y, r.logic.tileWidth * 5, HAlignment.CENTER);
				y-=1;
			}
		}	
	}

	@Override
	public Actor hit(float arg0, float arg1, boolean touchable) {
		return null;
	}

}
