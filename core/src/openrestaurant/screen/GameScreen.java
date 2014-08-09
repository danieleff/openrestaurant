package openrestaurant.screen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import openrestaurant.Logic.State;
import openrestaurant.res.Chair;
import openrestaurant.res.GameArea;
import openrestaurant.res.Person;
import openrestaurant.res.Resources;
import openrestaurant.res.TableConfig;
import openrestaurant.res.action.WaitNoteAction;
import openrestaurant.res.object.Counter;
import openrestaurant.res.object.Floor;
import openrestaurant.res.object.Machine;
import openrestaurant.res.object.Table;
import openrestaurant.res.object.TableHeart;
import openrestaurant.res.object.Wall;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class GameScreen implements Screen {

	private final Resources r;
	private Stage stage;
	public GameArea ga;
	private Stage ui;
	private Window dialog;
	private TextButton cont;
	private TextButton back;
	private Label dialogLabel;
	private Image star1;
	private Image star2;
	private Image star3;
	private com.badlogic.gdx.scenes.scene2d.ui.Table highscores;
	private Runnable loader;
	
	private float gameTime;
	private float currentTime;
	
	float fixStep = 1/60f;

	public GameScreen(final Resources r) {
		this.r = r;
		//TODO Person Movement
		//TODO tutorial text
		
		stage = new Stage(new FitViewport(320 * Gdx.graphics.getWidth() / Gdx.graphics.getHeight(), 320));
		stage.getCamera().position.set(stage.getWidth()/2, stage.getHeight()/2, 0);

		ui = new Stage(new FitViewport(480 * Gdx.graphics.getWidth() / Gdx.graphics.getHeight(),480));
		
		ga = new GameArea();
		ga.resize(new Rectangle(0, 0, stage.getWidth(), stage.getHeight()), 
				new Rectangle(0, 0, r.logic.tileCountX*r.logic.tileWidth, r.logic.tileCountY*r.logic.tileHeight));
		
		back = new TextButton("Back", r.skin);
		back.setPosition(ga.getX()*1.5f + 20, ga.getY()*1.5f + ga.getHeight()*1.5f - 50);
		back.setText("Back");
		back.pack();
		back.setVisible(true);
		back.addListener(new ClickListener() {
			
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (r.logic.state==State.PLAYING) {
					r.logic.setState(State.PAUSED);
				}
			}
		});
		ui.addActor(back);
		
		createDialog();
	}
	
	public void clear() {
		ga.clear();
		
		ga.resize(new Rectangle(0, 0, stage.getWidth(), stage.getHeight()), 
				new Rectangle(0, 0, r.logic.tileCountX*r.logic.tileWidth, r.logic.tileCountY*r.logic.tileHeight));
		
		Floor floor = new Floor(r);
		
		ga.addActor(floor);
		
		Person cook = new Person(r, r.baker);
		cook.setPosition(0, 3);
		ga.addActor(cook);
		cook.idle = new WaitNoteAction(r);
		cook.idle.txRow=2;
		
		r.logic.player = new Person(r, r.maid);
		r.logic.player.setPosition(3, 1);
		r.logic.player.idle.txRow=2;
		ga.addActor(r.logic.player);
		int id=1;
		
		List<TableConfig> list = r.tableConfigs.get(r.logic.level.tables);

		for (TableConfig tableConfig : list) {
			Table table = new Table(r, tableConfig.x, tableConfig.y, id);
			ga.addActor(table);
			
			ga.addActor(new TableHeart(table));
			
			
			ArrayList<Integer> cs = new ArrayList<Integer>(Arrays.asList(new Integer[]{1, 2, 3, 4}));
			//Collections.shuffle(cs);
			
			if (cs.indexOf(1)<tableConfig.c) {
				Chair chair1 = new Chair(r, table, tableConfig.x, tableConfig.y+1, false);
				ga.addActor(chair1);
				table.addChair(chair1);
			}
			
			if (cs.indexOf(2)<tableConfig.c) {
				Chair chair2 = new Chair(r, table, tableConfig.x+1, tableConfig.y+1, false);
				ga.addActor(chair2);
				table.addChair(chair2);
			}
			
			if (cs.indexOf(3)<tableConfig.c) {
				Chair chair3 = new Chair(r, table, tableConfig.x, tableConfig.y, true);
				ga.addActor(chair3);
				table.addChair(chair3);
			}
			
			if (cs.indexOf(4)<tableConfig.c) {
				Chair chair4 = new Chair(r, table, tableConfig.x+1, tableConfig.y, true);
				ga.addActor(chair4);	
				table.addChair(chair4);
			}
			
			r.logic.tables.add(table);
			
			id++;
		}
		
		
		Wall wall = new Wall(r, 7, r.logic.tileCountY-2);
		ga.addActor(wall);
/*
		Counter counter1 = new Counter(r, null, 1, r.logic.tileCountY - 3, 0);
		ga.addActor(counter1);
		r.logic.counters.add(counter1);
		*/
		for(int i=r.logic.tileCountY - 2;i>=0;i-=2) {
			Counter counter2 = new Counter(r, null, 1, i, false);
			ga.addActor(counter2);
			r.logic.counters.add(counter2);
		}
		
		Counter counter3 = new Counter(r, null, 4, r.logic.tileCountY - 2, true);
		ga.addActor(counter3);
		r.logic.counters.add(counter3);
		
		/*
		Counter counter3 = new Counter(r, null, 1, 0, 2);
		ga.addActor(counter3);
		r.logic.counters.add(counter3);
		*/
		
		r.logic.machine = new Machine(r, 0, r.logic.tileCountY - 3);
		ga.addActor(r.logic.machine);
		
		stage.addActor(ga);
	}
	
	private void createDialog() {
		dialog = new Window("Game Paused", r.skin);
		dialog.setTouchable(Touchable.disabled);
		dialog.setVisible(false);
				
		/*highscores = new com.badlogic.gdx.scenes.scene2d.ui.tablelayout.Table(r.skin);
		FlickScrollPane fs = new FlickScrollPane(highscores);

		Label label = new Label(r.skin);
		label.setText("Loading");
		label.width = 140;
		highscores.add(label);
		fs.pack();
		dialog.add(fs).width(250).fill().align(Align.TOP);
		
		*/
		dialog.row().colspan(3);
		dialogLabel = new Label("x", r.skin);
		dialogLabel.setText("x");
		dialog.add(dialogLabel);
		
		
		dialog.row().width(64).height(64).space(50);
		star1 = new Image(r.starGray);
		star1.setScaling(Scaling.none);
		star1.setAlign(Align.center);
		
		dialog.add(star1);
		star2 = new Image(r.starGray);
		star2.setScaling(Scaling.none);
		star2.setAlign(Align.center);
		dialog.add(star2);
		star3 = new Image(r.starGray);
		star3.setScaling(Scaling.none);
		star3.setAlign(Align.center);
		dialog.add(star3);
		
		dialog.row().width(150).space(50);
		dialog.pad(10).padTop(50);
		
		TextButton back = new TextButton("Back", r.skin);		
		dialog.add(back);
		
		cont = new TextButton("Continue", r.skin);		
		dialog.add(cont);
		
		TextButton restart = new TextButton("Restart", r.skin);		
		dialog.add(restart);
		
		back.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				r.restaurantGame.setScreen(r.selectLevelScreen);
			}
		});
		
		restart.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				r.logic.loadLevel(r.logic.level.prefix, r.logic.level.id);
				r.logic.setState(State.PLAYING);
			}
		});
		

		cont.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				r.logic.setState(State.PLAYING);
			}
		});
		
		dialog.pack();
		dialog.setX(ui.getWidth()/2 - dialog.getWidth()/2);
		dialog.setY(ui.getHeight()/2 - dialog.getHeight()/2);
		
        ui.addActor(dialog);
	}
	
	private void loadHighscores() {/*
		if (loader!=null) return;
		
		loader = new Runnable() {
			public void run() {
				try {
					URL url = new URL("http://danieleff.appspot.com/restaurant?level=1_1");
					BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
					String row;
					List<String> rows = new ArrayList<String>();
					rows.add("Weekly Top 100");
					
					while((row=reader.readLine())!=null) {
						rows.add(row);
					}
					for(int i=0;i<30;i++) {
						rows.add(""+i);
					}
					
					setHighscores(rows);
					
					reader.close();
				} catch (IOException e) {
					List<String> rows = new ArrayList<String>();
					rows.add("Cannot connect to server");
					setHighscores(rows);
				}
			}
		};
		new Thread(loader).start();		*/
	}

	private void setHighscores(final List<String> rows) {
		/*
		Gdx.app.postRunnable(new Runnable() {
		     public void run() {
		    	 highscores.clear();
		         for (String row : rows) {
		        	 int index = row.indexOf(",");
		        	 if (index!=-1) {
		        		 String first = row.substring(0, index);
		        		 String second = row.substring(index+1);
		        		 
			        	 highscores.row().padRight(20);
			        	 Label label = new Label(first, r.skin);
			        	 label.setText(first);
			        	 highscores.add(label);
			        	 
			        	 Label label2 = new Label(second, r.skin);
			        	 label2.setText(second);
			        	 highscores.add(label2);
		        	 } else {
		        		 highscores.row().colspan(2);
			        	 Label label = new Label(row, r.skin);
			        	 label.setText(row);
			        	 highscores.add(label);
		        	 }
				}
		     }
		 });*/
	}
	
	public void showDialog(boolean show, boolean theEnd) {
		/*if (show) {
			loadHighscores();
		}*/
		
		dialog.setVisible(show);
		dialog.setTouchable(show?Touchable.enabled:Touchable.disabled);
		ga.setTouchable((!show)?Touchable.enabled:Touchable.disabled);
		
		cont.setVisible(theEnd);
		
		if (!theEnd) {
			dialog.setTitle("Restaurant Closed");
			
			int stars = 0;
			if (r.logic.level.star1 > r.logic.level.heart) stars++;
			if (r.logic.level.star2 > r.logic.level.heart) stars++;
			if (r.logic.level.star3 > r.logic.level.heart) stars++;
			
			star1.setVisible(true);
			star2.setVisible(true);
			star3.setVisible(true);
			
			/*
			star1.setRegion(stars<1?r.starGray:r.star);
			star2.setRegion(stars<2?r.starGray:r.star);
			star3.setRegion(stars<3?r.starGray:r.star);
			*/
			
			if (stars == 0) {
				dialogLabel.setText("You need to work harder!");
			} else if (stars == 1) {
				dialogLabel.setText("You can do better!");
			} else if (stars == 2) {
				dialogLabel.setText("Well done!");
			} else {
				dialogLabel.setText("You are the best!");
			}
			
		} else {
			star1.setVisible(false);
			star2.setVisible(false);
			star3.setVisible(false);
			
			dialog.setTitle("Game Paused");
			dialogLabel.setText("Please select");
		}
	}
	
	@Override
	public void render(float delta) {
		currentTime += delta;
		if (gameTime < currentTime - 1000) {
			gameTime = currentTime;
		}
		while(gameTime < currentTime) {
			gameTime+=fixStep;
			renderFixstep();
		}
	}
	
	private void renderFixstep() {
		r.logic.delta = Math.min(fixStep, 1);
		r.logic.time += r.logic.delta;
		
		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        if (r.logic.state==State.PLAYING) {
        	r.logic.act(fixStep);
        	stage.act(fixStep);
        }
		stage.draw();
		
		ui.act(fixStep);
		ui.draw();
	}

	@Override
	public void show() {
		InputMultiplexer im = new InputMultiplexer();
		
		im.addProcessor(new InputAdapter() {
			public boolean keyDown(int keycode) {
				if (keycode==Keys.BACK || keycode==Keys.ESCAPE || keycode==Keys.BACKSPACE) {
					if (r.logic.state==State.PLAYING) {
						r.logic.setState(State.PAUSED);
					} else {
						r.restaurantGame.setScreen(r.selectLevelScreen);
					}
					return true;
				}
				return super.keyDown(keycode);
			}
		});
		
		im.addProcessor(ui);
		im.addProcessor(stage);
		Gdx.input.setInputProcessor(im);
	}

	@Override
	public void hide() {
		
	}
	
	@Override
	public void dispose() {
		
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
