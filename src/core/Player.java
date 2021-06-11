package core;


/**
 * Player defines the player of any game and tracks name, id, game pieces,
 * wins, losses, and draws.
 *
 * @author Matt Cummings
 * @version 5/25/2021
 */

public class Player {

//-----------------------------------------------------------------------------
//						 		CLASS VARIABLES
//-----------------------------------------------------------------------------
	// The user defined name of the player
	private final String NAME;
	// The system defined unique ID of the player
	private final int ID;
	// The number of chips the player has on the board
	private int chips;
	// which pieces the player is assigned to
	private char chipType;
	// number of wins
	private int wins;
	// number of losses
	private int losses;
	// number of draws
	private int draws;
	// is computer
	private boolean isComputer;

//-----------------------------------------------------------------------------
//						 		CONSTRUCTOR
//-----------------------------------------------------------------------------
	/**
	 * {@code Player} Constructor
	 * @param name		The user defined name of the player
	 * @param id		The system defined {@code ID} of the players
	 * @param chips		The number of chips the player has on the board
	 * @param chipType	The {@code char} chip type 'x' or 'o'
	 * @param isComp	{@code true} if the player is a computer
	 */
	public Player(String name, int id, int chips, char chipType, boolean isComp){
		this.NAME = name;
		this.ID = id;
		this.chips = chips;
		this.chipType = chipType;
		this.isComputer = isComp;
	}

//-----------------------------------------------------------------------------
//						 		PUBLIC METHODS
//-----------------------------------------------------------------------------
	/**
	 * Returns the user defined name of the {@code Player}
	 * @return	The user defined name of this {@code Player} object
	 */
	public String getName() {
		return this.NAME;
	}

	/**
	 * Compares the {@code ID} of this {@code Player} to {@code other}
	 * @param other		{@code Player} object to be compared
	 * @return			{@code true} if Player {@code ID}'s match
	 */
	public boolean equals(Player other) {
		return this.ID == other.getID();
	}

	/**
	 * Get the ID number of a player
	 * @return the unique {@code ID} of the player
	 */
	public int getID() {
		return this.ID;
	}

	/**
	 * Get the number of chips aa player currently owns
	 * @return 	the number of {@code chips} a player has left
	 */
	public int getChips() {
		return chips;
	}

	/**
	 * Removes a single chip from the players {@code chips} count
	 */
	public void loseChip() {
		chips--;
	}

	
	/**
	 * Resets the player chip counts on game complete
	 * @param chips	Number of new chips to assign to the player
	 */
	public void resetChips(int chips) {
		this.chips = chips;
	}

	/**
	 * Provides the chip type assigned to the player
	 * @return chip type that the player controlls
	 */
	public char getChipType() {
		return this.chipType;
	}

	/**
	 * Modifies the chip type assigned to the player
	 * @param chipType chip type the player controlls
	 */
	public void setChipType(char chipType) {
		this.chipType = chipType;
	}


	/**
	 * Simple getter for returning player stats
	 * @return list of wins, losses, and draws
	 */
	public int[] getStats(){
		int[] winLoss = {wins, losses, draws};
		return winLoss;
	}

	/**
	 * Simple setter for win count
	 */
	public void setWin(){
		this.wins++;
	}

	/**
	 * Simple setter for loss count
	 */
	public void setLoss(){
		this.losses++;
	}

	/**
	 * Simple setter for draw count
	 */
	public void setDraw(){
		this.draws++;
	}


	/**
	 * Tells if the player is a computer player
	 * @return	{@code true} if player is a computer
	 */
	public boolean isComputer() {
		return this.isComputer;
	}

}
