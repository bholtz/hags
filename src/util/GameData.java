package util;

import java.util.*;

import util.Direction.Dir;
import model.*;
import model.Map;

/**
 * The game data class is a bundle of information that is available 
 * to a player at a given time. For example, if a player is active, 
 * denoted by A, with inactive player I, switch S, and Ben B, <br>
 * <code>
 * +----------+    <br>
 * |.I........|    <br>
 * +-----.+--.|    <br>
 * |.S....|...|    <br>
 * +------+...|    <br>
 * |..B.A.....|    <br>
 * +----------+    <br>
 * </code>
 * it would receive a GameData instance that lets it know that the 
 * inactive player is 10 steps from the switch and that Ben is in 
 * location (3,5).
 */
public class GameData {
	/** 
	 * This is the distance in number of steps from the opponent 
	 * to the switch. If the opponent is currently active, then this 
	 * value is -1.
	 * */
	public final int oppDistToSwitch;
	
	/**
	 * Maintains a list of players visible 
	 */
	public final HashMap<String, State> visiblePlayers;
	
	public final Pos pos;
	
	public final Pos sw; 
	
	public final Pos inactivePlayer;
	
	public final int turnsRemaining;
	
	public GameData(HagsModel model, Player player) {
		HashMap<Player, State> states = model.getPlayerStates();
		Map map = model.getMap();
		State state = states.get(player);
		this.pos = state.pos;
		HashMap<String, State> visible = new HashMap<String, State>();
		if (player.getName().equals("Ben")) {
			this.sw = null;
			this.oppDistToSwitch = -1;
			this.inactivePlayer = null;
		}
		else if (player == model.getActive()) {
			Pos inactivePos = states.get(model.getInactive()).pos;
			this.oppDistToSwitch = dist(inactivePos, model.getSwitchPos(), map);
			this.sw = null;
			for (Player plyr : states.keySet()) {
				if (plyr != player) {
					if (dist(pos, states.get(plyr).pos, map) < 7)
						visible.put(plyr.getName(), states.get(plyr));
					if (inDirectSight(states.get(plyr).pos, state, map))
						visible.put(plyr.getName(), states.get(plyr));
				}
			}
			if (model.inactivePosAvailable()) {
				this.inactivePlayer = inactivePos;
			}
			else {
				this.inactivePlayer = null;
			}
		}
		// Case player is inactive
		else { 
			this.inactivePlayer = null;
			this.oppDistToSwitch = -1;
			this.sw = model.getSwitchPos();
			for (Player plyr : states.keySet()) {
				if (plyr != player) {
					if (dist(pos, states.get(plyr).pos, map) < 4)
						visible.put(plyr.getName(), states.get(plyr));
				}
			}
		}
		this.visiblePlayers = visible;
		this.turnsRemaining = model.getTurnsRemaining();
	}
	
	private boolean inDirectSight(Pos other, State player, Map map) {
		Pos pos = player.pos.move(Dir.NONE);
		while (!map.isWall(pos)) {
			if (other.equals(pos))
				return true;
			pos = pos.move(player.facing);
		}
		return false;
	}

	private int dist(Pos start, Pos end, Map map) {
		Queue<List<Pos>> frontier = new LinkedList<List<Pos>>();
		Set<Pos> explored = new HashSet<Pos>();
		List<Pos> startPath = new ArrayList<Pos>();
		startPath.add(start);
		frontier.add(startPath);
		while (true) {
			List<Pos> cur = frontier.remove();
			Pos curEnd = cur.get(cur.size() - 1);
			if (curEnd.equals(end)) {
				return cur.size() - 1;
			}
			if (explored.contains(curEnd)) continue;
			for (Dir dir : map.possibleMoves(curEnd)) {
				if (dir == Dir.NONE) continue;
				Pos next = curEnd.move(dir);
				if (!explored.contains(next)) {
					List<Pos> nextPath = new ArrayList<Pos>(cur);
					nextPath.add(next);
					frontier.add(nextPath);
				}
			}
			explored.add(curEnd);
		}
	}
}

