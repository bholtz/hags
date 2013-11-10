package model;

import util.GameData;

public class DummyPlayer extends HumanPlayer {
			
	private static int index = 1;
	private String name;
	
	public DummyPlayer() {
		super();
		name = "Dummy" + index;
		index++;		
	}
	
	@Override
	public Dir takeTurn(GameData data) {
		return Dir.NONE;
	}

	@Override
	public String getName() {
		return name;
	}

}
