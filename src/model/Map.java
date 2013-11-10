package model;

import java.io.*;
import java.util.*;

import util.*;

/**
 * This class defines the map on which the game is played.
 * It keeps track of the static wall layout and the dynamic 
 * position of players, Ben, and the switch.
 */
public class Map implements Direction {
	/**
	 * Holds the static wall structure of the map. 
	 * True indicates the presence of
	 * wall, and false indicates the absence of a wall 
	 * (free space).
	 */
	private boolean[][] map;
	
	private List<Pos> openings;
	
	/**
	 * Stores the dimensions of the map. Note that the 
	 * surrounding walls are included in the dimension,
	 * so a map with a single open space would actually 
	 * have width 3 and height 3.
	 */
	private int width, height;
	
	public static void main(String[] args) {
		Map map = new Map(30,15);
		map.printMap();
	}
	
	private Map(int w, int h) {
		map = new boolean[w][h];
		width = w;
		height = h;
		openings = new ArrayList<Pos>();
		addWalls();
		findOpenings();
		randomize();
	}
	
	private void randomize() {
		Random r = new Random();
		while (!isOK()) {
			Pos next = openings.get(r.nextInt(openings.size()));
			map[next.x][next.y] = true;
			openings.remove(next);
		}
	}

	private boolean isOK() {
		int totalMoves = 0;
		int maxMoves = 0;
		for (Pos pos : openings) {
			for (Dir dir : Dir.values()) {
				if (dir == Dir.NONE) continue;
				int moves = numVisible(pos, dir);
				totalMoves += moves;
				if (moves > maxMoves)
					maxMoves = moves;
			}
		}
		return maxMoves < 10 && (totalMoves / openings.size()) < 8;
	}

	private int numVisible(Pos pos, Dir dir) {
		int moves = 0;
		Pos temp = pos.move(dir);
		while (!isWall(temp)) {
			moves++;
			temp = temp.move(dir);
		}
		return moves;
	}

	private void findOpenings() {
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				if (!map[i][j])
					openings.add(new Pos(i, j));
			}
		}
	}

	private void addWalls() {
		for (int i = 0; i < width; i++) {
			map[i][0] = true;
			map[i][height - 1] = true;
		}
		for (int i = 0; i < height; i++) {
			map[0][i] = true;
			map[width-1][i] = true;
		}
	}

	public Map(BufferedReader br) {
		openings = new ArrayList<Pos>();
		try {
			width = Integer.parseInt(br.readLine());
			height = Integer.parseInt(br.readLine());
			map  = new boolean[width][height];
			for (int y = 0; y < height; y++) {
				String line = br.readLine();
				for (int x = 0; x < width; x++) {
					char ch = line.charAt(x);
					map[x][y] = (ch == 'W');
					if (!map[x][y]) {
						openings.add(new Pos(x,y));
					}
				}
			}
		} 
		catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	/**
	 * Query the map for whether or not some position is a wall
	 * @param x The x position of the query
	 * @param y The y position of the query
	 * @return {@code true} if the specified location is a wall, and {@code false} otherwise
	 */
	public boolean isWall(int x, int y) {
		return map[x][y];
	}
	
	public boolean isWall(Pos pos) {
		return isWall(pos.x, pos.y);
	}
	
	public Pos randomSpot() {
		Random rm = new Random();
		return openings.get(rm.nextInt(openings.size()));
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public List<Dir> possibleMoves(Pos pos) {
		assert !isWall(pos);
		List<Dir> dirs = new ArrayList<Dir>();
		for (Dir dir : Dir.values()) {
			if (!isWall(pos.move(dir)))
				dirs.add(dir);
		}
		return dirs;
	}

	private void printMap() {
		System.out.println(width);
		System.out.println(height);
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				System.out.print((map[x][y] ? 'W' : ' '));
			}
			System.out.println();
		}
	}
	
}
