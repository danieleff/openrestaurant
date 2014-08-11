package openrestaurant.screen;

import openrestaurant.res.Resources;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class TmxActor extends Actor {
	
	private Resources r;

	public TmxActor(Resources r) {
		this.r = r;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.end();
		OrthogonalTiledMapRenderer renderer = new OrthogonalTiledMapRenderer(r.mainBackground, batch);
		
		renderer.setView((OrthographicCamera) getStage().getCamera());
		renderer.render();
		batch.begin();
	}
}
