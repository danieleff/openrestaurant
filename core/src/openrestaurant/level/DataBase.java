package openrestaurant.level;

public interface DataBase {

	public void save(String level, int heart, int stars);
	
	public Score load(String level);
	
	public static class Score {
		public int heart;
		public int stars;
	}
	
}
