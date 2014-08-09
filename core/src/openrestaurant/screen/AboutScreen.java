package openrestaurant.screen;

import openrestaurant.res.Resources;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class AboutScreen implements Screen {

	private float height = 480f;
	private Stage ui;
	private final Resources r;
	private Image image;

	public AboutScreen(Resources r) {
		this.r = r;
		
	}
	@Override
	public void show() {		
		ui = new Stage(new FitViewport(height*Gdx.graphics.getWidth()/Gdx.graphics.getHeight(), height));
		
		InputMultiplexer input = new InputMultiplexer();
		input.addProcessor(new InputAdapter() {
			public boolean keyDown(int keycode) {
				if (keycode==Keys.BACK || keycode==Keys.ESCAPE || keycode==Keys.BACKSPACE) {
					r.restaurantGame.setScreen(r.mainScreen);
					return true;
				}
				return super.keyDown(keycode);
			}
		});
		input.addProcessor(ui);
		Gdx.input.setInputProcessor(input);
		
		
		image = new Image(r.main);
		image.setBounds(0, -340, 512*1.6f, 512*1.6f);
		ui.addActor(image);
		
		Window table = new Window("About", r.skin);
		table.defaults().spaceBottom(10).padLeft(40).padTop(10);
		
		table.row().align(Align.left);		
		table.add(new Label("Version", r.skin));
		table.add(new Label("0.1", r.skin));
		
		table.row().align(Align.left);		
		table.add(new Label("Created by:", r.skin));
		table.add(new Label("danieleff@gmail.com", r.skin));
		
		table.row().colspan(2);
		table.add(new Label("Credits", r.skin));
		
		table.row().align(Align.left);		
		table.add(new Label("Graphics:", r.skin));
		table.add(new Label("Sithjester", r.skin));
		
		table.pack();		

		table.setPosition(ui.getWidth()/2 - table.getWidth()/2, ui.getHeight()/2 - table.getHeight()/2);
		
		ui.addActor(table);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClearColor(0.5f, 0.6f, 0.6f, 1);
		
		ui.act(delta);
		ui.draw();
	}
	
	@Override
	public void dispose() {
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
	public void resize(int arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}


}

