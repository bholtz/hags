package graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.*;

import util.*;

import model.HagsModel;
import model.Map;
import model.Player;

import acm.graphics.*;

public class HagsGraphics implements Direction {

	private static final double MAX_WIDTH = 1300;
	private static final double MAX_HEIGHT = 700;
	private static final int SEP = 2;
	private static final int TITLE_HEIGHT = 23;
	private static final int BLUE_VISION = 16777215 - 10197965;
	private static final int RED_VISION = 16777215 - 13474715;
	private final int squareSize;
	private final int objectSize;

	private GCanvas canvas;
	private Map map;
	private GRect[][] tiles;
	private HashMap<String, GObject> objects;

	/**
	 * Given a HagsModel <b>model</b>, create a new HagsGraphics 
	 * object and display it.
	 * @param model The full initial game model to be displayed.
	 */
	public HagsGraphics(HagsModel model, GCanvas canvas) {
		this.canvas = canvas;
		map = model.getMap();
		squareSize = calculateSquareSize();
		objectSize = (squareSize * 2) / 3;
		tiles = new GRect[map.getWidth()][map.getHeight()];
		createMap();
		createObjects(model);
	}

	private void createObjects(HagsModel model) {
		objects = new HashMap<String, GObject>();
		objects.put("Switch", createSwitch());
		objects.put("Ben", createBen());
		objects.put(model.getPlayerName(1), createPlayer(1));
		objects.put(model.getPlayerName(2), createPlayer(2));
	}

	private GObject createPlayer(int i) {
		GRect player = new GRect(objectSize, objectSize);
		player.setColor(i == 1 ? Color.BLUE : Color.RED);
		player.setFilled(true);
		return player;
	}


	private GObject createBen() {
		GRect ben = new GRect(objectSize, objectSize);
		ben.setFillColor(Color.GRAY);
		ben.setFilled(true);
		return ben;
	}


	private GObject createSwitch() {
		GRect sw = new GRect(objectSize, objectSize);
		sw.setFilled(true);
		sw.setColor(Color.GREEN);
		return sw;
	}


	private void createMap() {
		int numCols = map.getWidth();
		int numRows = map.getHeight();
		for (int col = 0; col < numCols; col++) {
			for (int row = 0; row < numRows; row++) {
				createMapTile(col, row, map.isWall(col, row));
			}
		}
	}

	private int calculateSquareSize() {
		int numCols = map.getWidth();
		int numRows = map.getHeight();
		double xDim = MAX_WIDTH / numCols;
		double yDim = MAX_HEIGHT / numRows;
		return (int) Math.min(xDim, yDim) - SEP;
	}


	public void showWinner(String winner) {
		GLabel label = new GLabel(winner + " wins!");
		label.setFont(new Font("Serif", Font.BOLD, 40));
		double labelX = (canvas.getWidth() - label.getWidth()) / 2;
		double labelY = (canvas.getHeight() - (label.getHeight() + label.getAscent() + label.getDescent())) / 2;
		canvas.removeAll();
		canvas.add(label, labelX, labelY);	
	}

	/**
	 * Displays the current model state. Starts with the background map layout,
	 * then shows all of the positions of the players and objects.
	 * 
	 * @param model The current state of the game, including the positions of the 
	 * players and other game objects.
	 */
	public void display(HagsModel model) {
		resetMapColors();
		displaySwitch(model.getSwitchPos());
		displayPlayers(model.getPlayerStates());
	}

	private void displayPlayers(HashMap<Player, State> playerStates) {
		for (Player player : playerStates.keySet()) {
			placeObject(player.getName(), playerStates.get(player));
		}
	}


	private void displaySwitch(Pos pos) {
		placeObject("Switch", new State(pos, Dir.NONE, false));
	}

	private void placeObject(String name, State state) {
		GObject obj = objects.get(name);
		int offset = (squareSize - objectSize) / 2 + SEP;
		int x = (squareSize + SEP) * state.pos.x + offset;
		int y = (squareSize + SEP) * state.pos.y + offset;
		obj.setLocation(x, y);
		canvas.add(obj);
		if (!name.equals("Ben") && !name.equals("Switch"))
			drawVision(state, state.isActive);	
	}

	private void drawVision(State state, boolean isActive) {
		if (isActive)
			flashlightVision(state);
		
		bleed(state.pos, isActive ? 3 : 6, isActive);
	}


	private void flashlightVision(State state) {
		Pos pos = state.pos;
		int i = 0;
		while (!map.isWall(pos.move(state.facing))) {
			pos = pos.move(state.facing);
			if (i >= 3)
				drawVisionTile(pos, state.isActive);
			i++;
		}
	}

	private void bleed(Pos pos, int i, boolean isActive) {
		// TODO make this less horrible

		
		if (i >= 0) {
			drawVisionTile(pos, isActive);
			for (Dir dir : map.possibleMoves(pos)) {
				if (dir == Dir.NONE) continue;
				Pos nextPos = pos.move(dir);
					bleed(nextPos, i - 1, isActive);
			}
		}
	}


	private void drawVisionTile(Pos pos, boolean isActive) {
		GRect rect = (GRect)tiles[pos.x][pos.y];
		rect.setFilled(true);
		rect.setFillColor(addColor(isActive, rect.getColor()));
	}

	private Color addColor(boolean isActive, Color color) {
		int rgb = color.getRGB();
		rgb -= (isActive ? BLUE_VISION : RED_VISION);
		return new Color(rgb);
	}


	private void resetMapColors() {
		int numCols = map.getWidth();
		int numRows = map.getHeight();
		for (int col = 0; col < numCols; col++) {
			for (int row = 0; row < numRows; row++) {
				resetTileColor(col, row, map.isWall(col, row));
			}
		}
	}

	private void resetTileColor(int col, int row, boolean wall) {
		GRect rect = tiles[col][row];
		if (wall) {
			rect.setFillColor(Color.BLACK);
			rect.setFilled(true);
		}
		else {
			rect.setFillColor(Color.WHITE);
			rect.setFilled(true);
		}		
	}

	/**
	 * Displays a single tile of map.
	 * @param col The column of the map piece being placed
	 * @param row The row of the map piece being placed
	 * @param isWall Whether or not the map piece should be a wall
	 */
	private void createMapTile(int col, int row, boolean isWall) {
		int xPos = col * (squareSize + SEP) + SEP;
		int yPos = row * (squareSize + SEP) + SEP;
		GRect rect = new GRect(xPos, yPos, squareSize, squareSize);
		tiles[col][row] = rect;
		canvas.add(tiles[col][row]);
	}

	/**
	 * Calculates the appropriate window dimensions
	 * @return The width and height of an appropriately sized window for displaying the map
	 */
	public Dimension getDimension() {
		int numCols = map.getWidth();
		int numRows = map.getHeight();
		// Ug, pretend none of this happens
		return new Dimension(numCols * (squareSize + SEP) + SEP + 1, 
				TITLE_HEIGHT + numRows * (squareSize + SEP) + SEP);
	}

}
