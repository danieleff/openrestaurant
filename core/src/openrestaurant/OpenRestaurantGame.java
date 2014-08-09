package openrestaurant;

import openrestaurant.level.DataBase;
import openrestaurant.res.Resources;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

public class OpenRestaurantGame extends Game {
	
	public Resources r;
	public DataBase database;

	public OpenRestaurantGame(DataBase database) {
		this.database = database;
	}
	
	@Override
	public void create() {
		Gdx.input.setCatchBackKey(true);
		
		r = new Resources(this);
		setScreen(r.mainScreen);
	}
	
}
