package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import util.*;

public class KeyboardPlayer extends HumanPlayer implements Direction {

	private static int index = 1;
	private String name;

	public KeyboardPlayer() {
		super();
		name = "Keyboard" + index;
		index++;		
	}

	@Override
	public Dir takeTurn(GameData data) {
		System.out.println("Enter a move:");
		Dir dir = null;
		while (true) {
			try{
				BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
				String s = bufferRead.readLine();
				if (s.equals("w"))
					dir = Dir.NORTH;
				else if (s.equals("a"))
					dir = Dir.WEST;
				else if (s.equals("s"))
					dir = Dir.SOUTH;
				else if (s.equals("d"))
					dir = Dir.EAST;
				else
					dir = Dir.NONE;
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
			if (map.possibleMoves(data.pos).contains(dir))
				return dir;
			System.out.println("Try a different move.");
		}
	}

	@Override
	public String getName() {
		return name;
	}

}
