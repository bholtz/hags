package model;

import java.util.List;

import util.*;

public class Ben extends Player {
	
	private Dir lastDir;
	
	public Ben() {
		super();
		lastDir = Dir.WEST;
	}
	
	@Override
	public Dir takeTurn(GameData data) {
		Pos pos = data.pos;
		List<Dir> moves = map.possibleMoves(pos);
		moves.remove(Dir.NONE);
		if (!moves.contains(lastDir)) {
			lastDir = moves.get((int)(Math.random() * moves.size()));
		}
		return lastDir;
	}

	@Override
	public String getName() {
		return "Ben";
	}
}
