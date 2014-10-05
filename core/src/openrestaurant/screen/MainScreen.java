package openrestaurant.screen;

import openrestaurant.level.LevelList;
import openrestaurant.res.Resources;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class MainScreen extends ScreenWithStages {

	private Stage ui;
	
	private Resources r;	
	
	public MainScreen(Resources r) {
		this.r = r;
		createUI();
	}
	
	public void show() {
		InputMultiplexer input = new InputMultiplexer();
		input.addProcessor(new InputAdapter() {
			public boolean keyDown(int keycode) {
				if (keycode==Keys.BACK || keycode==Keys.ESCAPE || keycode==Keys.BACKSPACE) {
					Gdx.app.exit();
					return true;
				}
				return super.keyDown(keycode);
			}
		});
		input.addProcessor(ui);
		Gdx.input.setInputProcessor(input);
	}
	@Override
	public void render(float delta) {
		super.render(delta);
		/*
		mapRenderer.setView((OrthographicCamera)ui.getViewport().getCamera());
		mapRenderer.render();
				*/
		ui.act(delta);
		ui.draw();
	}
	
	private void createUI() {
		clearStages();
		ui = new Stage(new FitViewport(800, 600));
		addStage(ui);
		
		TmxActor ma = new TmxActor(r);
		ui.addActor(ma);
		
		
		Table table = new Table(r.skin);
		table.defaults().spaceBottom(10).padLeft(10).padTop(10);
		table.row().fill().expandX();

		addLevelList(table, new LevelList("Tutorial", "tut", 1));
		addLevelList(table, new LevelList("Beta test missions 1", "b1", 10));
		
		addLevelList(table, new LevelList("Tiled .tmx test", "tmx", 1));
		
		//addLevelList(table, new LevelList("Beta test missions 2", "b2", 1));
		
		table.row();		
		/*TextButton b = new TextButton("About", r.skin, "large");
		b.getStyle().font=r.skin.getFont("font_test");
		table.add(b).fill();
		
		b.addListener(new ClickListener() {
			
			@Override
			public void clicked(InputEvent event, float x, float y) {
				r.restaurantGame.transitTo(r.aboutScreen);
			}
		});*/
		
		table.pack();
		
		table.setX(ui.getWidth() - table.getWidth() - 5);
		table.setY(ui.getHeight() - table.getHeight());
		
		ui.addActor(table);
				
	}

	private void addLevelList(Table table, final LevelList levelList) {
		TextButton button = new TextButton(levelList.name, r.skin, "large");
		table.row();
		table.add(button).fill();
		
		button.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				r.logic.levelList = levelList;
				r.click.play();
				r.restaurantGame.transitTo(r.selectLevelScreen);
			}
		});
	}

	@Override
	public void resize(int x, int y) {
		if (ui!=null) ui.getViewport().update(x, y, true);
	}
}
