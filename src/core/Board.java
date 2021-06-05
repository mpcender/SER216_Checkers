package core;


/**
 * Board is an n*n board that tracks size of the space and the current 
 * state of chip objects.
 *
 * @author Matt Cummings
 * @version 5/25/2021
 */

import java.awt.Point;

public class Board {

//-----------------------------------------------------------------------------
//						 		CLASS VARIABLES
//-----------------------------------------------------------------------------

	private int size;
	private Chip[][] state;


//-----------------------------------------------------------------------------
//						 		CONSTRUCTOR
//-----------------------------------------------------------------------------
	/**
	 * Board Class Constructor
	 * @param state  	game board where each char depicts {@code Chip type}
	 * @param types		list of {@code Chip type}s available on the board
	 * @param ruleset	valid movesets of corresponding {@code type} passed as 
	 * 					a Point object calculated as an offset from Chip origin.
	 * @param players	list of players participating to assign to chips
	 */
	public Board(
				char[][] state, 
				char[] types, 
				Point[][][] ruleset, 
				Player[] players) {

		this.size = state.length;
		this.state = new Chip[this.size][this.size];

		for (int i = 0; i < this.size; i++) {
			for (int j = 0; j < this.size; j++) {
				for (int k = 0; k < types.length; k++) {
					// if current board node contains char chip type
					if (state[i][j] == types[k]){ 
						this.state[i][j] = 
							new Chip(ruleset[k][0], ruleset[k][1], 
									 players[k], types[k]);
					} 
				}
			}
		}
	}

//-----------------------------------------------------------------------------
//						 		PUBLIC METHODS
//-----------------------------------------------------------------------------
	/**
	 * Updates the state of the board with player move and removed enemy
	 * 
	 * @param stateChange	stateChange[0] = Origin
	 * 						stateChange[1] = destination
	 * 						stateChange[2]...[n] = enemies to remove
	 * @return				updated char[][] of the board state
	 */
	public char[][] updateState(Point[] stateChange) {

		//Move Chip to Destination
		state[stateChange[1].x][stateChange[1].y] = 
			state[stateChange[0].x][stateChange[0].y];
		//Remove Chip from Origin
		state[stateChange[0].x][stateChange[0].y] = null;

		if (stateChange.length > 2) {
			for (int i = 2; i < stateChange.length; i++) {
				state[stateChange[i].x][stateChange[i].y].getOwner().loseChip();
		 		state[stateChange[i].x][stateChange[i].y] = null;
			}
		} 
		return getCurrentState();
	}

	/**
	 * Determine if the chip placement is a valid move
	 * @param from	Origin of the piece to move
	 * @param to	Destination of the piece to move
	 * @param ext	is piece using extended moveset
	 * @return		returns {@code true} if move is within valid chip moveset
	 */
	public boolean isValidMoveChip(Point from, Point to, boolean ext) {
		Point offset = new Point(to.x-from.x, to.y-from.y);
		boolean isValid = false;
		try {
			isValid = state[from.x][from.y].isValidMove(offset, ext);
		} catch(NullPointerException e){
			return false;
		}
		return isValid;
	}

	/**
	 * Simple method to return the owner of a chip on the board
	 * @param origin	Coordinates of the origin of the chip
	 * @return			returns the owner of the designated chip
	 */
	public Player getChipOwner(Point origin) {
		return state[origin.x][origin.y].getOwner();
		
	}

	/**
	 * Simple method to determine if the place on the board is a Chip
	 * @param origin	Coordinates of the origin of the chip
	 * @return			{@code true} if the coordinate location contains a 
	 * 					{@code Chip} object
	 */
	public boolean isChip(Point origin) {
		return state[origin.x][origin.y] != null;
	}

	/**
	 * Convert board chip array to char array for return to game logic processor
	 * @return	Return {@code char[][]} of the current board state
	 */
	public char[][] getCurrentState() {
		char[][] currentState = new char[this.size][this.size];
		for (int i = 0; i < this.size; i++) {
			for (int j = 0; j < this.size; j++) {
				if (this.state[i][j] != null) {
					currentState[i][j] = this.state[i][j].getType();
				} else {
					currentState[i][j] = ' ';
				}
			}
		}
		return currentState;
	}


	/**
	 * Returns all possible moves that can be made by player
	 * @param activePlayer	The player chips to be examined
	 * @return				Array of Points containing all possible moves where 
	 * 						{@code Point[]} is the point pair including the 
	 * 						{@code Point[n][0]}: Origin and {@code Point[n][1]}: 
	 * 						Destination points. 
	 */
	public Point[][] getPossibleMoves(Player activePlayer){
		int size = activePlayer.getChips();
		int count = 0;
		// Array list of all moves Point[from][to]
		Point[][] possibleMoves = new Point[size*8][8];
		// check every node on the board
		for (int i = 0; i < this.size; i++) {
			for (int j = 0; j < this.size; j++) {
				// if the node contains a chip...
				if (this.state[i][j] != null) {
					// match chip to owner and return possible moves
					if (this.state[i][j].getOwner().equals(activePlayer)){
						Point from = new Point(i,j);
						Point[] to = this.state[i][j].getMoveSets();

						for (int k = 0; k < to.length ; k++){
							possibleMoves[count][0] = from;
							possibleMoves[count][1] = 
								new Point(from.x+to[k].x,from.y+to[k].y);
							count++;
						}
					}
				}
			}
		}
		return possibleMoves;
	}

	/**
	 * Check if the origin and destination are on the board
	 * @param from	Origin
	 * @param to	Destination
	 * @return		{@code true} if move is valid
	 */
	
	public boolean isValidMoveBoard(Point from, Point to) {
		boolean isValid = false;
		try {
			isValid = 	from.x >= 0 && from.x < state[0].length &&
						to.x >= 0 && to.x < state[0].length &&
						from.y >= 0 && from.y < state.length &&
						to.y >= 0 && to.y < state.length;	
		} catch(NullPointerException e){
			return false;
		}
		if(!isValid) { return false; }
		return true;
	}
	
}
