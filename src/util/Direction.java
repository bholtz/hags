package util;

public interface Direction {
	/**
	 * This enum provides all of the possible moves for each player. Each 
	 * of the directions has a built in dx and dy value that can be added to
	 * a player's position to move them.
	 */
	public static enum Dir {
		SOUTH (0, 1),
		WEST (-1, 0),
		NORTH (0, -1),
		EAST (1, 0),
		NONE (0, 0);
		public final int dx, dy;
		Dir(int x, int y) {
			dx = x;
			dy = y;
		}
	}
}
