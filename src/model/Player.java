package model;

import util.GameData;

public abstract class Player extends HagsObject {

	protected Map map;
	
	public void registerMap(Map map) {
		this.map = map;
	}
	
	/**
	 * 
	 * @param dir the Direction in which the player is to be moved
	 */
	protected void move(Dir dir) {
		pos = pos.move(dir);
	}

	public abstract Dir takeTurn(GameData data);
}
