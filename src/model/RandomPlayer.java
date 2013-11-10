package model;

import java.util.List;
import java.util.Random;

import util.GameData;

public class RandomPlayer extends HumanPlayer {

	private static int index = 1;
	private String name;
	
	public RandomPlayer() {
		super();
		name = "Random" + index;
		index++;		
	}
	
	@Override
	public Dir takeTurn(GameData data) {
		Random rm = new Random();
		List<Dir> moves = map.possibleMoves(data.pos);
		int move = rm.nextInt(moves.size());
		return moves.get(move);
	}

	@Override
	public String getName() {
		return name;
	}

}
