package controller;

import teamAG.ComputerPlayer;
import model.*;
import graphics.*;

import DeadBeef.AstarPlayer;
import acm.program.GraphicsProgram;

@SuppressWarnings("serial")
public class HagsController extends GraphicsProgram {

	
	private static final double DISPLAY_PAUSE = 20;
	private HagsModel model;
	private HagsGraphics graphics;
	private HumanPlayer playerOne, playerTwo;
	
	public void init() {
		setTitle("HAGS!");
		playerTwo = new RaymondPlayer();
		playerOne = new AstarPlayer();
		model = new HagsModel(playerOne, playerTwo);
		graphics = new HagsGraphics(model, this.getGCanvas());
		this.resize(graphics.getDimension());
	}

	/**
	 * This is the main loop in which Hags is played. 
	 * Each player and NPC has a turn. The turn generates 
	 * updates to <b>model</b>, which are then sent to <b>graphics</b> to be 
	 * displayed. The loop runs until one player wins.
	 */
	public void run() {
		String winner;
		while (true) {
			graphics.display(model);
			pause(DISPLAY_PAUSE);
			winner = model.getWinner();
			if (winner != null) break;
			model.nextTurn();
			pause(DISPLAY_PAUSE);
		}
		graphics.showWinner(winner);
	}

	
	public static void main(String[] args) {
		(new HagsController()).start();
	}
}
