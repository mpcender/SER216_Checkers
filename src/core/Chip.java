package core;


/**
 * Chip is a generic class that stores the valid movesets of a game piece 
 * and the pieces owner.
 *
 * @author Matt Cummings
 * @version 5/25/2021
 */

import java.awt.Point;

public class Chip {

//-----------------------------------------------------------------------------
//						 		CLASS VARIABLES
//-----------------------------------------------------------------------------
	// Player object that owns the chip object
	private final Player OWNER;

	private final char TYPE;

	// an array of Point objects that store valid move offsets from current chip placement.
	private Point[] VALID_MOVE_SET;
	// an array of Point objects that store extended move offsets from current chip placement.
	private Point[] EXTENDED_MOVE_SET;

	private Point[] ALL_MOVES;
	private int NUM_MOVES;
	private boolean isKing;

//-----------------------------------------------------------------------------
//						 		CONSTRUCTOR
//-----------------------------------------------------------------------------
	/**
	 * Chip Constructor stores its own valid movesets and it owner
	 * @param validMoveSet		An array of all poosible standard valid move 
	 * 							offsets calculated from the chips origin
	 * @param extendedMoveSet	An array of all poosible extended valid move 
	 * 							offsets calculated from the chips origin. 
	 * 							Used for special circumstance moves (i.e. 
	 * 							Checkers: chip jumps chip)
	 * @param owner				{@code Player} object which controlls this 
	 * 							{@code Chip}
	 * @param type				chip type (char value)
	 */
	public Chip(Point[] validMoveSet, Point[] extendedMoveSet, Player owner, char type){
		this.VALID_MOVE_SET = validMoveSet;
		this.EXTENDED_MOVE_SET = extendedMoveSet;
		this.OWNER = owner;
		this.TYPE = type;
		this.NUM_MOVES = validMoveSet.length + extendedMoveSet.length;
		this.isKing = false;

		// collate movesets
		collateMoves();
	}


//-----------------------------------------------------------------------------
//						 		PUBLIC METHODS
//-----------------------------------------------------------------------------
	/**
	 * Returns the owner of Chip object
	 * @return	{@code Player} object that owns this {@code Chip}
	 */
	public Player getOwner() {
		return this.OWNER;
	}

	/**
	 * Returns the piece type of the {@code Chip}
	 * @return	returns the {@code type} of {@code Chip}
	 */
	public char getType() {
		return this.TYPE;
	}

	/**
	 * Returns the number of moves this piece can possibly perform.
	 * @return	the number of different moves the piece can possibly make
	 */
	public int getNumMoves() {
		return this.NUM_MOVES;
	}

	/**
	 * Returns a list of all moves the piece can make
	 * @return	the list of all possible moves of the chip
	 */
	public Point[] getMoveSets(){
		return ALL_MOVES;
	}
	
	/**
	 * Updates chip to King and modifies chip with new King moveset
	 * @param valid		New valid king moves
	 * @param extended	New extended king moves
	 */
	public void setKing(Point[] valid, Point[] extended) {
		this.VALID_MOVE_SET = valid;
		this.EXTENDED_MOVE_SET = extended;
		collateMoves();
		this.isKing = true;
	}

	/**
	 * Returns the chips King status
	 * @return	{@code true} if chip is King
	 */
	public boolean isKing(){
		return this.isKing;
	}

	/**
	 * Determines if the provided offset is included in the chips valid 
	 * movesets.
	 * 
	 * @param offset 	The 'to' location or distance from point of origin
	 * @param ext		{@code true} to evaluate {@code EXTENDED_MOVE_SET}, 
	 * 					{@code false} to evaluate {@code VALID_MOVE_SET}.
	 * @return			{@code true} if the move destination is included in 
	 * 					the chip objects valid moveset
	 */
	public boolean isValidMove(Point offset, boolean ext) {

		// if extended moveset return true if move matches any extended move
		if (ext){
			for (Point p : EXTENDED_MOVE_SET){
				if ( p.equals(offset) ) {
					return true;
				}
			}
			return false;
		} 
		// if standard moveset return true if move matches any valid move
		else {
			for (Point p : VALID_MOVE_SET){
				if ( p.equals(offset) ) {
					return true;
				}
			}
			return false;
		}
		
	}

//-----------------------------------------------------------------------------
//						 		PRIVATE METHODS
//-----------------------------------------------------------------------------
	/**
	 * Method combines given movesets for valid chip move evaluations
	 * Standard Valid moves at front, extended moves at back.
	 */
	private void collateMoves() {
		int len = VALID_MOVE_SET.length + EXTENDED_MOVE_SET.length;
		Point[] allMoves = new Point[len];
		for (int i = 0; i < VALID_MOVE_SET.length; i++) {
			allMoves[i] = VALID_MOVE_SET[i];
		}
		for (int i = VALID_MOVE_SET.length; i < len; i++) {
			allMoves[i] = EXTENDED_MOVE_SET[i-VALID_MOVE_SET.length];
		}
		this.ALL_MOVES = allMoves;
	}
}
