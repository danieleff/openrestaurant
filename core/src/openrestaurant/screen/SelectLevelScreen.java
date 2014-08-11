package openrestaurant.screen;

import java.util.HashMap;
import java.util.Map;

import openrestaurant.res.Resources;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class SelectLevelScreen extends ScreenWithStages {

	private Stage ui;

	private final Resources r;

	private Label title;
	
	private Label[] heartLabels=new Label[30];
	
	private Map<Integer, Group> dayGroups=new HashMap<Integer, Group>();

	public SelectLevelScreen(final Resources r) {
		this.r = r;
		//TODO background
		ui = new Stage(new FitViewport(800, 600));
		addStage(ui);

		TmxActor ma = new TmxActor(r);
		
		ui.addActor(ma);
		
		Window table = new Window("Test", r.skin);
		table.setMovable(false);
		table.setResizable(false);
		
		table.row().colspan(7);
		title = new Label("January", r.skin);
		title.setText("January");
		table.add(title);
		
		
		String[] days = new String[]{"Mon", "Tue", "Wen", "Thu", "Fri", "Sat", "Sun"};
		
		table.row().width(100).height(40).pad(2).padBottom(6);
		for(int i=0;i<7;i++) {
			Label label = new Label(days[i], r.skin);
			label.setText(days[i]);
			label.setAlignment(Align.top);
			table.add(label);
		}
		
		int day = 1;
		for(int i=0;i<2;i++) {
			table.row().width(100).height(120).pad(2);
			for(int j=0;j<7;j++) {
				Group s = new Group();
				dayGroups.put(day, s);
				
				TextButton b = new TextButton(""+day, r.skin);
				
				b.setText(""+day);
				b.getLabel().setAlignment(Align.top);
				b.setFillParent(true);
				
				final int x = day;
				b.addListener(new ClickListener() {
					@Override
					public void clicked(InputEvent event, float cx, float cy) {
						r.logic.loadLevel(r.logic.levelList.prefix, x);
						r.restaurantGame.transitTo(r.gameScreen);
					}
				});
				
				s.addActor(b);
				
				for(int starCount=0;starCount<3;starCount++) {
					Image star = new Image(r.starGray);
					star.setScaling(Scaling.fit);
					star.setName("star_"+day+"_"+starCount);
					
					star.setWidth(28);
					star.setHeight(28);
					star.setX(5 + starCount * 34);
					star.setY(30);
					s.addActor(star);
				}
				
				Image h = new Image(r.heart1);
				h.setScaling(Scaling.fit);
				h.setX(5);
				h.setY(5);
				h.setWidth(20);
				h.setHeight(20);
				s.addActor(h);
				
				Label l = new Label("1234", r.skin, "font-17", (Color)null);
				heartLabels[day]=l;
				l.setX(0);
				//l.y=20;
				l.setWidth(100);
				l.setAlignment(Align.bottom | Align.right);
				//l.setFillParent(true);
				s.addActor(l);
				
				table.add(s);
				day++;
			}
		}
		
		table.row().colspan(7).pad(5).fill(true, false);
		TextButton back = new TextButton("Back", r.skin);
		back.align(Align.center);
		back.setText("Back");
		
		back.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				r.restaurantGame.transitTo(r.mainScreen);
			}
		});
		
		table.add(back);
		
		table.pack();
		
		table.setX(ui.getWidth()/2 - table.getWidth()/2);
		table.setY(ui.getHeight()/2 - table.getHeight()/2);
		
		ui.addActor(table);
	}

	private Actor findActor(String name) {
		int len = ui.getActors().size;
		for(int i=0; i<len; i++){
		    Actor a = ui.getActors().get(i);
		    if(a.getName()!=null && a.getName().equals(name)){
		        return a;
		    }
		}
		return null;
	}
	
	@Override
	public void show() {
		title.setText(r.logic.levelList.name);
		
		for(int i=1;i<=2*7;i++) {
			Actor actor = dayGroups.get(i);
			if (i <= r.logic.levelList.numLevels) {
				
				if (r.restaurantGame.database!=null) {
					/*Score stars = r.restaurantGame.database.load(""+r.logic.levelList.prefix+"_"+i);
					
					heartLabels[i].setText(""+stars.heart);
					
					Image img1 = (Image)findActor("star_"+i+"_0");
					//img1.setRegion(stars.stars<1?r.starGray:r.star);
					
					Image img2 = (Image)findActor("star_"+i+"_1");
					//img2.setRegion(stars.stars<1?r.starGray:r.star);
					
					Image img3 = (Image)findActor("star_"+i+"_2");
					//img3.setRegion(stars.stars<1?r.starGray:r.star);
					 * */
				}
				
				actor.setVisible(true);
			} else {
				actor.setVisible(false);
			}
		}
		
		InputMultiplexer input = new InputMultiplexer();
		input.addProcessor(new InputAdapter() {
			public boolean keyDown(int keycode) {
				if (keycode==Keys.BACK || keycode==Keys.ESCAPE || keycode==Keys.BACKSPACE) {
					r.restaurantGame.transitTo(r.mainScreen);
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
		
		ui.act(delta);
		ui.draw();
	}
	
	@Override
	public void dispose() {
		
	}

	@Override
	public void hide() {
		
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resize(int x, int y) {
		ui.getViewport().update(x, y, true);
	}

	@Override
	public void resume() {
		
	}

}
