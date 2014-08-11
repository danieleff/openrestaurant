package openrestaurant.screen;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;

public abstract class ScreenWithStages implements Screen {
	
	List<Stage> stages=new ArrayList<Stage>();
	
	public boolean clear=true;
	
	protected void addStage(Stage stage) {
		stages.add(stage);
	}
	
	protected void clearStages() {
		stages.clear();
	}
	
	public void clearActions() {
		for (Stage stage : stages) {
			stage.getRoot().clearActions();
		}
	}
	
	public List<Stage> getStages() {
		return stages;
	}
	
	@Override
	public void render(float delta) {
		if (clear) {
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			Gdx.gl.glClearColor(0, 0, 0, 1);
		}
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
	
}
