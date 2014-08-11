package openrestaurant;

import openrestaurant.level.DataBase;
import openrestaurant.res.Resources;
import openrestaurant.screen.ScreenWithStages;
import openrestaurant.screen.TransitionScreen;
import openrestaurant.screen.TransitionScreen.TransitionType;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

public class OpenRestaurantGame extends Game {
	
	public Resources r;
	public DataBase database;
	private TransitionScreen transitionScreen;

	public OpenRestaurantGame(DataBase database) {
		this.database = database;
	}
	
	@Override
	public void create() {
		Gdx.input.setCatchBackKey(true);
		
		r = new Resources(this);
		setScreen(r.mainScreen);
		
		transitionScreen = new TransitionScreen(this);
	}
	
	public void transitTo(ScreenWithStages to) {
		transitionScreen.fadeScreens(TransitionType.ROTATE_OUT, to);
	}
	
}
