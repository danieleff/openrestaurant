package openrestaurant.res;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import openrestaurant.Logic;
import openrestaurant.OpenRestaurantGame;
import openrestaurant.level.Customer;
import openrestaurant.level.CustomerInfo;
import openrestaurant.level.Level;
import openrestaurant.screen.AboutScreen;
import openrestaurant.screen.GameScreen;
import openrestaurant.screen.MainScreen;
import openrestaurant.screen.SelectLevelScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

public class Resources {

	public MainScreen mainScreen;
	public GameScreen gameScreen;
	
	public Logic logic;
	
	public BitmapFont font10;
	public BitmapFont font17;
	public BitmapFont font24;
	public BitmapFont font32;

	public TextureRegion[][] maid;
	public Texture floor;
	public TextureRegion table;
	public TextureRegion chairDown;
	public TextureRegion chairUp2;
	public final OpenRestaurantGame restaurantGame;
	public TextureRegion chairUp;
	public TextureRegion wall;
	public TextureRegion wallStart;
	public TextureRegion wallEnd;
	
	public TextureRegion counter;
	public TextureRegion[][] tremel;
	public TextureRegion[][] guy;
	public TextureRegion[][] lina;
	public TextureRegion[][] sailormoon;
	public Skin skin;
	public TextureRegion[][] baker;
	public TextureRegion wallDoor;
	public TextureRegion machine;
	public TextureRegion[][] steampunk_f1;
	public TextureRegion[][] zelgadisgreywords;
	public TextureRegion food;
	public TextureRegion pictures;
	public TextureRegion window;
	
	public Map<String, List<Customer>> customers = new HashMap<String, List<Customer>>();
	
	public Map<String, TextureRegion[][]> customers2 = new HashMap<String, TextureRegion[][]>();
	
	public Map<String, List<CustomerInfo>> groups = new HashMap<String, List<CustomerInfo>>();
	
	public Map<String, List<TableConfig>> tableConfigs = new HashMap<String, List<TableConfig>>();
	
	private Json json;
	private JsonReader jr;
	public TextureRegion plate;
	public Texture main;
	public SelectLevelScreen selectLevelScreen;
	public TextureRegion star;
	public TextureRegion starGray;
	public TextureRegion heartMeter1;
	public TextureRegion heartMeter2;
	public TextureRegion heartMeter3;
	public TextureRegion heart1;
	public TextureRegion heart2;
	public TextureRegion heart3;
	public TextureRegion heart4;
	public TextureRegion door1;
	public TextureRegion door2;
	public AtlasRegion carte;
	private TextureAtlas atlas;
	private Texture floorBrown;
	private Texture floorWhite;
	public Sound bell;
	private AtlasRegion counterTable;
	public AtlasRegion teapot;
	public AtlasRegion cup;
	public AboutScreen aboutScreen;
	private TmxMapLoader tmxMapLoader;
	public TiledMap mainBackground;
	public Sound click;
	
	public static String datadir="data/";
	
	public Resources(OpenRestaurantGame restaurantGame) {
		this.restaurantGame = restaurantGame;
		
		click = Gdx.audio.newSound(Gdx.files.internal("sound/switch2.ogg"));
		
		tmxMapLoader = new TmxMapLoader();
		
		mainBackground = tmxMapLoader.load("main.tmx");
		
		logic = new Logic(this);
		
		json = new Json();
		jr = new JsonReader();
		
		//skin = new Skin(Gdx.files.internal(datadir+"uiskin.png"), new TextureAtlas(Gdx.files.internal(datadir+"uiskin.json")));
		skin = new Skin(Gdx.files.internal("data/uiskin.json"));
		
		skin.getFont("default-font").getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		skin.getFont("font_test").getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		/*ObjectSet<Texture> ts = skin.getAtlas().getTextures();
		for (Texture texture : ts) {
			texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		}
		*/
		loadFonts();
		
		loadJson();
		
		atlas = new TextureAtlas(Gdx.files.internal(datadir+"pack"));
		
		for (java.util.Map.Entry<String, List<Customer>> e : customers.entrySet()) {
			for (Customer c : e.getValue()) {
				if (!customers2.containsKey(c.file)) {
					TextureRegion[][] c2 = loadChar(atlas, c.file);
					customers2.put(c.file, c2);
				}
			}
		}
		
		maid = loadChar(atlas, "waitress");
		tremel = loadChar(atlas, "tremel");
		guy = loadChar(atlas, "modernguy02");
		baker = loadChar(atlas, "baker");
		lina = loadChar(atlas, "linainverse");
		sailormoon = loadChar(atlas, "sailormoon");
		steampunk_f1 = loadChar(atlas, "steampunkf1");
		zelgadisgreywords = loadChar(atlas, "zelgadisgreywords");
		
		floorBrown = new Texture(Gdx.files.internal(datadir+"floor-brown.png"));
		floorBrown.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		floorBrown.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		floorWhite = new Texture(Gdx.files.internal(datadir+"floor-white.png"));
		floorWhite.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		floorWhite.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		main = new Texture(Gdx.files.internal("main.png"));
		main.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		star = atlas.findRegion("star");
		starGray = atlas.findRegion("starGray");
		
		
		heart1 = atlas.findRegion("heart1");
		heart2 = atlas.findRegion("heart2");
		heart3 = atlas.findRegion("heart3");
		heart4 = atlas.findRegion("heart4");
		
		heartMeter1 = atlas.findRegion("heartMeter1");
		heartMeter2 = atlas.findRegion("heartMeter2");
		heartMeter3 = atlas.findRegion("heartMeter3");

		carte = atlas.findRegion("carte");
		
		bell = Gdx.audio.newSound(Gdx.files.internal(datadir+"bell.mp3"));
		
		mainScreen = new MainScreen(this);
		selectLevelScreen = new SelectLevelScreen(this);
		gameScreen = new GameScreen(this);
		aboutScreen = new AboutScreen(this);
	}
	
	public void switchTileset(String name) {
		if (name.equals("brown")) {
			floor = floorBrown;
		} else {
			floor = floorWhite;
		}
		
		table  = atlas.findRegion(name+"-table");
		
		chairDown  = atlas.findRegion(name+"-chairDown");
		chairUp  = atlas.findRegion(name+"-chairUp");
		chairUp2  = atlas.findRegion(name+"-chairUp2");
		
		wallStart  = atlas.findRegion(name+"-wallStart");
		wall  = atlas.findRegion(name+"-wall");
		wallDoor  = atlas.findRegion(name+"-wallDoor");
		wallEnd  = atlas.findRegion(name+"-wallEnd");
		machine  = atlas.findRegion(name+"-machine");
		
		door1 = atlas.findRegion(name+"-door1");
		door2 = atlas.findRegion(name+"-door2");
		
		food = atlas.findRegion(name+"-food");
		plate = atlas.findRegion(name+"-plate");
		
		pictures = atlas.findRegion(name+"-pictures");
		window = atlas.findRegion(name+"-window");
		
		counter = atlas.findRegion(name+"-counterTable");
		//counterTable = atlas.findRegion(name+"-counterTable");
		
		teapot = atlas.findRegion(name+"-teapot");
		cup = atlas.findRegion(name+"-cup");
	}
	
	private TextureRegion[][] loadChar(TextureAtlas atlas, String name) {
		TextureRegion[][] ret=new TextureRegion[4][4];
		for(int x=0;x<4;x++) {
			for (int y=0;y<4;y++) {
				AtlasRegion r = atlas.findRegion(name+x+y);
				if (r==null) throw new Error("Character not found: "+name+x+y);
				ret[x][y] = r;
			}
		}
		return ret;
	}

	private void loadFonts() {
		font10 = new BitmapFont(Gdx.files.internal(datadir+"font_10.fnt"), Gdx.files.internal(datadir+"font_10.png"), false);
		font17 = new BitmapFont(Gdx.files.internal(datadir+"font_17.fnt"), Gdx.files.internal(datadir+"font_17.png"), false);
		font24 = new BitmapFont(Gdx.files.internal(datadir+"font_24.fnt"), Gdx.files.internal(datadir+"font_24.png"), false);
		font32 = new BitmapFont(Gdx.files.internal(datadir+"font_32.fnt"), Gdx.files.internal(datadir+"font_32.png"), false);		
	}

	public Level loadLevel(String levelName) {
		//TODO 2 x 10 levels
		
		Level ret = json.fromJson(Level.class, Gdx.files.internal(datadir+"level/level_"+levelName+".json"));
		ret.name = levelName;
		return ret;
	}

	private void loadJson() {
		JsonValue c = jr.parse(Gdx.files.internal(datadir+"level/customers.json"));
		for (JsonValue e : c.iterator()) {
			ArrayList<Customer> chars = new ArrayList<Customer>();
			for (JsonValue object : e.iterator()) {
				chars.add(json.readValue(Customer.class, object));
			}
			customers.put(e.name, chars);
		}
		
		JsonValue g = jr.parse(Gdx.files.internal(datadir+"level/groups.json"));
		for (JsonValue e : g.iterator()) {
			ArrayList<CustomerInfo> chars = new ArrayList<CustomerInfo>();
			for (JsonValue object : e.iterator()) {
				chars.add(json.readValue(CustomerInfo.class, object));				
			}
			groups.put(e.name, chars);
		}
		
		JsonValue t = jr.parse(Gdx.files.internal(datadir+"level/tables.json"));
		for (JsonValue e : t.iterator()) {
			ArrayList<TableConfig> configs = new ArrayList<TableConfig>();
			for (JsonValue object : e.iterator()) {
				configs.add(json.readValue(TableConfig.class, object));
			}
			tableConfigs.put(e.name, configs);
		}
	}
	
}
