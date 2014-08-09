package openrestaurant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import openrestaurant.level.Customer;
import openrestaurant.level.CustomerInfo;
import openrestaurant.level.Group;
import openrestaurant.level.Level;
import openrestaurant.level.LevelList;
import openrestaurant.res.Chair;
import openrestaurant.res.Person;
import openrestaurant.res.Resources;
import openrestaurant.res.action.EnterAction;
import openrestaurant.res.action.TakeSeatAction;
import openrestaurant.res.object.Counter;
import openrestaurant.res.object.Machine;
import openrestaurant.res.object.Table;
import openrestaurant.res.object.Table.Status;

public class Logic {

	public int tileWidth=32;
	
	public int tileHeight=32;
	
	public int tileCountX;
	
	public int tileCountY;
	
	public Person player;
	
	public List<Table> tables = new ArrayList<Table>();
	
	public List<Person> customers = new ArrayList<Person>();
	
	public List<Counter> counters = new ArrayList<Counter>();
	
	public boolean[][] block;
	
	public Random rnd = new Random(System.currentTimeMillis());
	
	public float delta;
	public float time;

	private final Resources r;

	public Level level;
	
	public State state=State.PLAYING;

	private List<Group> groups;

	public LevelList levelList;

	public Machine machine;

	public String[] tutorialText;
	
	public Logic(Resources r) {
		this.r = r;
		tileCountX=16;
		tileCountY=10;
	}
	
	public void loadLevel(String prefix, int id) {
		//TODO Better level ids
		tutorialText=null;
		
		level = r.loadLevel(prefix+"_"+id);
		level.prefix = prefix;
		level.id = id;
		
		r.switchTileset(level.tileset);
		
		tileCountX = level.width;
		tileCountY = level.height;
		
		tables.clear();
		customers.clear();
		counters.clear();
		
		r.gameScreen.clear();
		setState(State.PLAYING);
		
		block = new boolean[tileCountX][tileCountY];
		for (Table t : tables) {
			block[t.tileX][t.tileY]=true;
			block[t.tileX][t.tileY+1]=true;
			block[t.tileX+1][t.tileY]=true;
			block[t.tileX+1][t.tileY+1]=true;
		}
		
		for(int i=0;i<tileCountX;i++) {
			block[i][tileCountY-1]=true;
			block[i][tileCountY-2]=true;
		}
		
		groups = new ArrayList<Group>();
		for (Group[] g : level.groups) {
			ArrayList<Group> subGroup = new ArrayList<Group>();
			for (Group gr : g) {
				for (int i=0;i<gr.count;i++) {
					Group a = new Group(gr);
					if (a.t<0.1) a.t=level.t;
					subGroup.add(a);
				}
			}
			//Collections.shuffle(subGroup);
			groups.addAll(subGroup);
		}
	}
	
	public List<Pos> path(Pos start, List<Pos> ends) {
		Map<Pos, Integer> best = new HashMap<Pos, Integer>();
		Map<Pos, Pos> comeFrom = new HashMap<Pos, Pos>();
		
		Set<Pos> closedSet = new HashSet<Pos>();
		Set<Pos> openSet = new HashSet<Pos>();
		openSet.add(start);
		
		while(!openSet.isEmpty()) {
			Pos current = null;
			for (Pos pos : openSet) {
				if (current==null || pos.score < current.score) {
					current = pos;
				}
			}
			openSet.remove(current);
			
			if (ends.contains(current)) {
				List<Pos> path = new ArrayList<Pos>();
				path.add(current);
				reconstuctPath(comeFrom, current, path);
				return path;
			}
			closedSet.add(current);
			
			for (Pos neighbour : neighbour(current, ends.get(0))) {
				if (closedSet.contains(neighbour)) continue;
				
				Integer s = best.get(neighbour);
				if (s==null || s>=neighbour.score) {
					best.put(neighbour, neighbour.score);
					comeFrom.put(neighbour, current);
				} else {
					neighbour.score = s;
				}
				
				openSet.add(neighbour);
			}
		}
		return null;
	}
	
	private void reconstuctPath(Map<Pos, Pos> comeFrom, Pos current, List<Pos> positions) {
		Pos prev = comeFrom.get(current);
		if (prev!=null) {
			positions.add(0, prev);
			reconstuctPath(comeFrom, prev, positions);
		}
	}

	private List<Pos> neighbour(Pos current, Pos end) {
		ArrayList<Pos> ret = new ArrayList<Pos>();
		if (current.x>0 && (!block[current.x-1][current.y] || end.equals(new Pos(current.x-1, current.y)) )) {
			Pos p = new Pos(current.x-1, current.y);
			p.score = current.score+1;
			ret.add(p);
		}
		if (current.x<block.length-1 && (!block[current.x+1][current.y] || end.equals(new Pos(current.x+1, current.y)))) {
			Pos p = new Pos(current.x+1, current.y);
			p.score = current.score+1;
			ret.add(p);
		}
		if (current.y>0 && (!block[current.x][current.y-1] || end.equals(new Pos(current.x, current.y-1)))) {
			Pos p = new Pos(current.x, current.y-1);
			p.score = current.score+1;
			ret.add(p);
		}
		if (current.y<block[0].length-1 && (!block[current.x][current.y+1] || end.equals(new Pos(current.x, current.y+1)))) {
			Pos p = new Pos(current.x, current.y+1);
			p.score = current.score+1;
			ret.add(p);
		}
		return ret;
	}

	public void setState(State state) {
		this.state=state;
		r.gameScreen.showDialog(state!=State.PLAYING, state!=State.END);		
	}
	
	public void act(double delta) {
		
		level.timeLeft = 0;
		
		if (groups.size()>0) {
			Group group = groups.get(0);
			group.t-=delta;
			if (group.t<0) {
				groups.remove(0);
				takeTable(group);
			}
			for (Group g : groups) {
				level.timeLeft += g.t;
			}
		} else {
			if (customers.isEmpty()) {
				int stars = 0;
				if (level.star1 > level.heart) stars++;
				if (level.star2 > level.heart) stars++;
				if (level.star3 > level.heart) stars++;
				
				if (level.heart >= r.restaurantGame.database.load(level.prefix+"_"+level.id).heart) {
					r.restaurantGame.database.save(level.prefix+"_"+level.id, level.heart, stars);
				}
				setState(State.END);
			}
		}
		
		if (level.tutorial!=null && level.tutorial==1) {
			doTutorial1();
		}
	}
	
	private String[] tutorialText1 = new String[]{"Wait for the customer", "to sit down"};
	private String[] tutorialText2 = new String[]{"Press the table", "to get the order", "from the customer"};
	private String[] tutorialText3 = new String[]{"Press the table", "on the left", "to give the order", "to the cook"};
	private String[] tutorialText4 = new String[]{"Wait for the cook", "to prepare the food"};
	private String[] tutorialText5 = new String[]{"Press the food", "to get the food"};
	private String[] tutorialText6 = new String[]{"Press the table", "to give the food", "to the customer"};
	private String[] tutorialText7 = new String[]{"Press the table", "to get the money", "from the customer"};
	
	private void doTutorial1() {
		tutorialText = tutorialText7;
	}

	private void takeTable(Group group) {
		List<CustomerInfo> customerInfos = r.groups.get(group.name);
		
		ArrayList<Table> randomTables = new ArrayList<Table>(tables);
		//Collections.shuffle(randomTables);
		
		for (Table table : randomTables) {
			if (table.status!=Table.Status.EMPTY) continue;
			if (table.chairs.size()<customerInfos.size()) continue;
			
			int index=0;
			
			ArrayList<Chair> randomChairs = new ArrayList<Chair>(table.chairs);
			//Collections.shuffle(randomChairs);
			
			for (CustomerInfo customerInfo : customerInfos) {
				List<Customer> custs = r.customers.get(customerInfo.name);
				int total = 0;
				for (Customer c : custs) {
					total += c.p;
				}
				int random = rnd.nextInt(total)+1;
				
				Person customer=null;
				for (Customer customer2 : custs) {
					if (random<=customer2.p) {
						customer = new Person(r, r.customers2.get(customer2.file));
						break;
					}
					random-=customer2.p;
				}
				
				customer.setPosition(3, r.logic.tileCountY - 1);
				EnterAction enter = new EnterAction(r);
				enter.tileX = 3;
				enter.tileY = r.logic.tileCountY - 2;
				customer.add(enter);
				
				Chair chair = randomChairs.get(index);
				TakeSeatAction action = new TakeSeatAction(r, chair);
				action.tileX = chair.tileX;
				action.tileY = chair.tileY;
				customer.add(action);
				chair.reserved=true;
				
				r.gameScreen.ga.addActor(customer);
				
				r.logic.customers.add(customer);
				
				index++;
			}
			table.status = Status.TAKING_SEAT;
			
			table.maxHeart=5;
			
			table.heart=3;
			table.waitOk = 1;
			
			break;
		}
	}


	public static enum State {
		PLAYING,
		PAUSED,
		END
	}
}
