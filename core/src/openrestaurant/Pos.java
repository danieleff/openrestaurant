package openrestaurant;

public class Pos implements Comparable<Pos> {
	
	public final int x;
	
	public final int y;

	public int score;
	
	public Pos(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public String toString() {
		return x+","+y+" "+score;
	}
	
	@Override
	public boolean equals(Object other) {
		return ((Pos)other).x==x && ((Pos)other).y==y;
	}
	
	@Override
	public int hashCode() {
		return new Integer(x+y).hashCode();
	}

	@Override
	public int compareTo(Pos o) {
		return score-o.score;
	}
	
}