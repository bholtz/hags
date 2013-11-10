package model;

import util.*;

public abstract class HagsObject implements Direction {
	protected Pos pos;
	
	public Pos getPos() {
		return pos;
	}
	
	/**
	 * @return the x position of the object
	 */
	public int getX() {
		return pos.x;
	}

	/**
	 * @return the y position of the object
	 */
	public int getY() {
		return pos.y;
	}
	
	/**
	 * 
	 * @return the name of this object (e.g. "Ben" or "SuperAwesomeCoolAI" or "Switch")
	 */
	public abstract String getName();
}
