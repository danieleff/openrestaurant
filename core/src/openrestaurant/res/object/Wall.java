package openrestaurant.res.object;

import openrestaurant.res.Person;
import openrestaurant.res.Resources;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Wall extends Actor{
	private final Resources r;

	private float doorIsOpen;

	private final int tileX;

	private final int tileY;
	
	public Wall(Resources r, int tileX, int tileY) {
		this.r = r;
		this.tileX = tileX;
		this.tileY = tileY;
		setPosition(tileX * r.logic.tileWidth, tileY * r.logic.tileHeight - 0.5f);
	}
	
	public void act(float delta) {
		doorIsOpen -= delta;
	}
	
	@Override
	public void draw(Batch spriteBatch, float arg1) {
		spriteBatch.draw(r.wallStart, getX(), getY());
		
		for (int i=tileX+1;i<r.logic.tileCountX;i++) {
			if (i==9 && false) {
				
				boolean canEnter=true;
				for (Person other : r.logic.customers) {
					if ((other.tileX==3) && other.tileY==r.logic.tileCountY - 3) {
						canEnter=false;
					}
				}
				if (!canEnter) {
					if (doorIsOpen < 0) {
						r.bell.play();
					}
					doorIsOpen = 0.4f;
				}
				
				if (doorIsOpen > 0) {					
					spriteBatch.draw(r.door2, i * r.logic.tileWidth, getY());
				} else {
					spriteBatch.draw(r.door1, i * r.logic.tileWidth, getY());					
				}
			} else {
				spriteBatch.draw(r.wall, i * r.logic.tileWidth, getY());				
			}
		}
		spriteBatch.draw(r.wallEnd, (r.logic.tileCountX-1) * r.logic.tileWidth, getY());
		
		int meterW = 160;
		int meterX = r.logic.tileCountX * r.logic.tileWidth - 180;
		//int meterX = r.logic.tileCountX * r.logic.tileWidth/2 - meterW/2;
		
		spriteBatch.draw(r.heartMeter2, meterX, getY()+30, meterW, 32);
		
		float red=r.logic.level.heart * meterW / r.logic.level.star3;
		red = Math.min(red, meterW);
		
		spriteBatch.draw(r.heartMeter1, meterX, getY()+30, red, 32);
		
		
		spriteBatch.draw(r.heartMeter3, meterX + meterW * r.logic.level.star1 / r.logic.level.star3 - 16 / 2, getY()+30, 16, 32);
		TextureRegion star1 = r.starGray;
		if (r.logic.level.heart > r.logic.level.star1) {
			star1 = r.star;
		}
		spriteBatch.draw(star1, meterX + meterW * r.logic.level.star1 / r.logic.level.star3 - 24/2, getY()+18, 24, 24);
		
		
		
		spriteBatch.draw(r.heartMeter3, meterX + meterW * r.logic.level.star2 / r.logic.level.star3 - 16 / 2, getY()+30, 16, 32);
		TextureRegion star2 = r.starGray;
		if (r.logic.level.heart > r.logic.level.star2) {
			star2 = r.star;
		}
		spriteBatch.draw(star2, meterX + meterW * r.logic.level.star2 / r.logic.level.star3 - 24/2, getY()+18, 24, 24);
		
		
		spriteBatch.draw(r.heartMeter3, meterX + meterW - 16 / 2, getY()+30, 16, 32);
		TextureRegion star3 = r.starGray;
		if (r.logic.level.heart > r.logic.level.star3) {
			star3 = r.star;
		}
		spriteBatch.draw(star3, meterX + meterW - 24/2, getY()+18, 24, 24);
		
		
		spriteBatch.draw(r.heart1, meterX - 32/2, getY()+35, 32, 32);
		
		r.font10.drawMultiLine(spriteBatch, ""+r.logic.level.heart, meterX - 32/2, getY()+35 + 24, 32, HAlignment.CENTER);

		if (r.logic.level.timeLeft>0.1f) {
			r.font10.drawMultiLine(spriteBatch, ""+"Closing: "+r.logic.level.timeLeft, meterX - 32/2, getY() + 24, 32, HAlignment.LEFT);
		} else {
			r.font10.drawMultiLine(spriteBatch, "Closed", meterX - 32/2, getY() + 24, 32, HAlignment.LEFT);
		}
		
		//spriteBatch.draw(r.pictures, 5 * r.logic.tileWidth, y + r.logic.tileHeight - 5);
		//spriteBatch.draw(r.window, 8 * r.logic.tileWidth, y + 5);
	}

	@Override
	public Actor hit(float x, float y, boolean touchable) {
		
		return null;
	}

}
