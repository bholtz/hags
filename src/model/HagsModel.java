package model;

import java.io.*;
import java.util.*;

import util.*;

/**
 * This class manages all of backend of hags. It  
 * 
 * @author Ben Holtz
 */
public class HagsModel implements Direction {
	
	public static final int NUM_PLAYERS = 3;
	public static final int MAX_TURNS = 1000;
	public static final String MAP_FILE = "apartment.map";

	private Map map;
	private List<Player> players;
	private HashMap<Player, State> playerStates;
	private HumanPlayer p1, p2;
	private HumanPlayer active, inactive;
	private Ben ben;
	private Pos switchPos;
	
	/** Keeps track of which turn number is currently happening. 
	 * Taking <code>turn</code> mod <code>NUM_PLAYERS</code> gives us the index of the
	 * player whose turn it is currently. */
	private int turn;
	private boolean inactivePosAvailable;
	
	/**
	 * Creates a model object with instantiated players and map.
	 */
	public HagsModel(HumanPlayer playerOne, HumanPlayer playerTwo) {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(MAP_FILE));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		map = new Map(br);
		players = new ArrayList<Player>();
		playerStates = new HashMap<Player, State>();
		p1 = playerOne;
		p2 = playerTwo;
		ben = new Ben();
		players.add(ben);
		players.add(p1);
		players.add(p2);
		active = p1;
		inactive = p2;
		for (Player player : players) {
			player.registerMap(map);
			Pos pos = null;
			try {
				pos = getPos(br.readLine());
			} catch (IOException e) {
				e.printStackTrace();
			}
			playerStates.put(player, new State(pos, Dir.NORTH, player == p1));
		}
		try {
			switchPos = getPos(br.readLine());
		} catch (IOException e) {
			e.printStackTrace();
		}
		players.add(ben);
		inactivePosAvailable = false;
		turn = 0;
	}
		
	
	private Pos getPos(String line) {
		String[] pos = line.split(" ");
		return new Pos(Integer.parseInt(pos[0]), Integer.parseInt(pos[1]));
	}


	/**
	 * Decides if the game has ended and returns the name of the
	 * winner if it has.
	 * @return 	the name of the winning player if the game state is now terminal i.e. 
	 * 			Ben has caught a player, the active player has caught 
	 * 			the inactive player, or time has run out <br>
	 * 			<code>null</code> if the game is not yet in a terminal state
	 */
	public String getWinner() {
		// Check if Ben has caught a player
		if (caughtByBen(p1))
			return p2.getName();
		if (caughtByBen(p2))
			return p1.getName();
		
		// Check if time has run out
		if (turn == MAX_TURNS)
			return active.getName();
		
		if (canTag(active, inactive))
			return active.getName();
		
		return null;
	}
	
	private boolean canTag(HumanPlayer active, HumanPlayer inactive) {
		State activeState = playerStates.get(active);
		Pos inactivePos = playerStates.get(inactive).pos;
		Pos temp = activeState.pos;
		while (!map.isWall(temp)) {
			if (temp.equals(inactivePos))
				return true;
			temp = temp.move(activeState.facing);
		}
		return false;
	}


	private boolean caughtByBen(HumanPlayer player) {
		Pos pPos = playerStates.get(player).pos;
		Pos bPos = playerStates.get(ben).pos;
		return bPos.equals(pPos);
	}


	public String getPlayerName(int i) {
		return players.get(i).getName();
	}
	
	public void nextTurn() {
		Player player = players.get(turn % (NUM_PLAYERS + 1));
		Dir dir;
		try {
			dir = player.takeTurn(new GameData(this, player));
		} catch (Exception e) {
			dir = Dir.NONE;
		}
		if (!map.possibleMoves(playerStates.get(player).pos).contains(dir))
			dir = Dir.NONE;
		playerStates.put(player, playerStates.get(player).move(dir));
		if (player == inactive) {
			if (playerStates.get(player).pos.equals(switchPos)) {
				switchRoles();
				switchPos = map.randomSpot();
			}
		}
		inactivePosAvailable = false;
		turn++;
	}
	
	private void switchRoles() {
		HumanPlayer temp = inactive;
		inactive = active;
		active = temp;
		playerStates.get(inactive).switchRole();
		playerStates.get(active).switchRole();
		inactivePosAvailable = true;
	}


	public Map getMap() {
		return map;
	}


	public Pos getSwitchPos() {
		return switchPos;
	}

	public boolean inactivePosAvailable() {
		return this.inactivePosAvailable;
	}

	public HashMap<Player, State> getPlayerStates() {
		return playerStates;
	}

	
	public HumanPlayer getActive() {
		return active;
	}
	
	public HumanPlayer getInactive() {
		return inactive;
	}


	public int getTurnsRemaining() {
		return (MAX_TURNS - turn) / (NUM_PLAYERS + 1);
	}
}
