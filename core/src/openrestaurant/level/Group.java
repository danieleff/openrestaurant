package openrestaurant.level;

public class Group {

	public float t;
	
	public int count = 1;
	
	public String name;
	
	public Group() {}
	
	public Group(Group other) { 
		t=other.t;
		name=other.name;
	}
	
}
