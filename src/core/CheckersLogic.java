package core;


/**
 * Checkers Game Logic
 *
 * @author Matt Cummings
 * @version 5/25/2021
 */

import java.awt.Point;
import java.util.ArrayDeque;
import java.util.Random;

public class CheckersLogic {

//-----------------------------------------------------------------------------
//						 		CLASS VARIABLES
//-----------------------------------------------------------------------------
	// 
	private static final int PLAYER_START_CHIPS = 12;
	private static final int MAX_PLAYERS = 2;
	private static final int SIZE = 8;

	// Move Sets of Checkers Pieces
	private static final Point[] PLAIN_X = { new Point(-1,-1), new Point(-1,1) };
	private static final Point[] PLAIN_EXT_X = { new Point(-2,-2), new Point(-2,2) };
	private static final Point[] PLAIN_O = { new Point(1,-1), new Point(1,1) };
	private static final Point[] PLAIN_EXT_O = { new Point(2,-2), new Point(2,2) };
	
	// All available piece types
	private static final char[] PIECE_TYPES = { 'x', 'o' };
	// Aggregation of all piece types and their associated movesets
	private static final Point[][][] MOVESETS = 
		{ { PLAIN_X, PLAIN_EXT_X }, { PLAIN_O, PLAIN_EXT_O } };

	// checkers logic board object
	private Board gameBoard;

	//players participating in the game
	private Player[] players;

	// current active player
	private int activePlayer;

	// point that stores the coordinates of chips being attacked
	private Point[] attackPoints;

	// An initial state checkers board
	private static final char[][] INIT_BOARD = 
		{ { ' ' , 'o' , ' ', 'o', ' ', 'o', ' ', 'o'} ,
		  { 'o' , ' ' , 'o', ' ', 'o', ' ', 'o', ' '} ,
		  { ' ' , 'o' , ' ', 'o', ' ', 'o', ' ', 'o'} ,
		  { ' ' , ' ' , ' ', ' ', ' ', ' ', ' ', ' '} ,
		  { ' ' , ' ' , ' ', ' ', ' ', ' ', ' ', ' '} ,
		  { 'x' , ' ' , 'x', ' ', 'x', ' ', 'x', ' '} ,
		  { ' ' , 'x' , ' ', 'x', ' ', 'x', ' ', 'x'} ,
		  { 'x' , ' ' , 'x', ' ', 'x', ' ', 'x', ' '} ,};


	/*
	private static final char[][] TEST_BOARD = 
		  { { ' ' , 'o' , ' ', 'o', ' ', 'o', ' ', 'o'} ,
			{ 'o' , ' ' , 'o', ' ', 'o', ' ', 'o', ' '} ,
			{ ' ' , 'o' , ' ', 'o', ' ', 'o', ' ', ' '} ,
			{ ' ' , ' ' , 'x', ' ', 'x', ' ', 'x', ' '} ,
			{ ' ' , 'x' , ' ', 'x', ' ', 'x', ' ', 'x'} ,
			{ ' ' , ' ' , 'x', ' ', 'x', ' ', 'x', ' '} ,
			{ ' ' , ' ' , ' ', ' ', ' ', ' ', ' ', ' '} ,
			{ ' ' , ' ' , ' ', ' ', ' ', ' ', ' ', ' '} ,};
	*/


//-----------------------------------------------------------------------------
//						 		CONSTRUCTOR
//-----------------------------------------------------------------------------
	/**
	 * CheckersLogic handles and executes the game logic of checkers
	 */
	public CheckersLogic(String[] playerName, boolean computerOpponent) {
		this.players = new Player[MAX_PLAYERS];

		// check if opponent is computer or human
		if (computerOpponent) {
			Random rand = new Random();
			int i = rand.nextInt(2);

			//System.out.println("start comp: " + computerOpponent + " rand: " + i);
			// Coin toss who goes first
			if (i < 1){ 
				this.players[0] = 
					new Player(playerName[0], 0, PLAYER_START_CHIPS, PIECE_TYPES[0], false);
				this.players[1] = 
					new CheckersComputerPlayer(playerName[1], 1, PLAYER_START_CHIPS, PIECE_TYPES[1], true);
			} else {
				this.players[0] = 
					new CheckersComputerPlayer(playerName[1], 0, PLAYER_START_CHIPS, PIECE_TYPES[0], true);
				this.players[1] = 
					new Player(playerName[0], 1, PLAYER_START_CHIPS, PIECE_TYPES[1], false);
			}
		} else {
			// Initialize players
			for (int i = 0; i < MAX_PLAYERS; i++) {
				this.players[i] = 
					new Player(playerName[i], i+1, PLAYER_START_CHIPS, PIECE_TYPES[i], false);
			}
		}
	}

//-----------------------------------------------------------------------------
//						 		PUBLIC METHODS
//-----------------------------------------------------------------------------
	/**
	 * returns the current active player in the game
	 * @return 	returns the active players name identifier
	 */
	public String getPlayer() {
		return players[activePlayer].getName();
	}

	/**
	 * returns the current active player chip type
	 * @return 	returns the active players chip type
	 */
	public char getPlayerChip() {
		return players[activePlayer].getChipType();
	}

	
	/** 
	 * @return String[]
	 */
	public String[] getPLayerNames() {
		String[] playerStr = new String[2];
		playerStr[0] = this.players[0].getName();
		playerStr[1] = this.players[1].getName();
		return playerStr;
	}


	/**
	 * Returns the current state of the game board
	 * @return current game board state of type {@code char[][]}
	 */
	public char[][] getCurrentState(){
		return this.gameBoard.getCurrentState();
	}


	
	/** 
	 * @return char[][]
	 */
	/*
	 * Generates newgame state, creates players, builds new chips with 
	 * appropriate movesets, and maps to the board.
	 * 
	 * @param playerName		Human player name input
	 * @param computerOpponent	if {@code true} computer opponent active
	 * @return					return the current state of the board
	 */
	public char[][] startGame() {
		
		// Initialize Board with Chip types, positions, and owners.
		this.gameBoard = new Board(INIT_BOARD, PIECE_TYPES, 
									MOVESETS, this.players);

		// game always starts on 'dark' or 'X'
		this.activePlayer = 0;
		// fist move is never attack
		this.attackPoints = new Point[0];

		return this.gameBoard.getCurrentState();
	}

	
	/**
	 * Move takes in a string from the player, reformats, and analyzes. 
	 * @param move	The user input indicating the location of the piece to be 
	 * 				moved and the desired destination.
	 * @return 		returns converted {@code Point[]} if valid move input. 
	 * 				returns {@code null} if invalid move input. 
	 */
	public Point[] validateMove(String move) {
		// Ensure user input is correctly formatted and convert to coordinate
		Point[] movePoints = convertInput(move);
		if (movePoints == null) { 
			return null; 
		}
	
		// Validate user input is a legal move 
		if (!isValidMove(movePoints[0], movePoints[1])) {
			return null;
		}

		return movePoints;
	}


	/**
	 * Executes the players move to the board, toggles active player and 
	 * clears active attack points.
	 * @param movePoints
	 */
	public void confirmMove(Point[] movePoints) {
		// Record user move and update all relevant game states
		recordMove(movePoints[0], movePoints[1]);
		// switch to inactive player
		toggleActivePlayer();
		// clear attack points
		attackPoints = new Point[0];
	}


	/**
	 * method to check if game has reached a checkers specific end game state.
	 * @return	{@code true} if end game state detected
	 */
	public boolean isGameComplete() {
		if (players[activePlayer].getChips() == 0){ 
			recordPlayerStats();
			return true; 
		}
		return !canPlayerMove();			
	}


	/**
	 * Returns the stats of both players for display
	 * @return Current win, loss, and draws for players
	 */
	public int[][] getPlayerStats(){
		int[][] playerStats = {players[0].getStats(), players[1].getStats()};
		return playerStats;
	}


	/**
	 * Determines if current player is a computer
	 * @return	{@code true} if current player is the computer
	 */
	public boolean isComputerTurn() {
		return players[activePlayer].isComputer();
	}


	/**
	 * Propts computer player to make move
	 * @return	{@code char[][]} with updated board state after computer move
	 */
	public Point[] computerMove() {
		Point[] compMove = null;
		// if player is not computer throw error
		try {
			CheckersComputerPlayer comp = (CheckersComputerPlayer)players[activePlayer];
			
			Point[][] moves;
			// Get all possible moves for computer
			Point[][] possibleMoves = gameBoard.getPossibleMoves(comp);
			ArrayDeque<Integer> validMoveIndex = new ArrayDeque<Integer>();
			// Validate moves
			for (int i = 0 ; i < possibleMoves.length; i++) {
				if (isValidMove(possibleMoves[i][0], possibleMoves[i][1])){
					validMoveIndex.add(i);
				}
				attackPoints = null;
			}
			// Transfer valid moves to array to pass to computer
			moves = new Point[validMoveIndex.size()][2];
			int j = 0;
			do {
				int i = validMoveIndex.remove();
				moves[j][0] = possibleMoves[i][0];
				moves[j++][1] = possibleMoves[i][1];
			}
			while (!validMoveIndex.isEmpty());
			
			compMove = comp.makeMove(gameBoard, moves);
			isAttack(compMove[0], compMove[1]);

			// give computer moveset and board, submit move.
			confirmMove(compMove);
			
		} 
		catch(ClassCastException e) {
			e.printStackTrace();
			System.out.println("Player object is not a computer player");
		} catch(IndexOutOfBoundsException e) {
			e.printStackTrace();
			System.out.println("Computer move array access out of bounds");
		} 
		
		return compMove;
	}

//-----------------------------------------------------------------------------
//						 		PRIVATE METHODS
//-----------------------------------------------------------------------------
	/**
	 * convert string to Point coordinates e.g., 3a-4b --> (2,0),(3,1)
	 * @param playerMove	The user input indicating the location of the piece 
	 * 						to be moved and the desired destination.
	 * @return				Converted user input in the form of {@code Point[]}
	 */
	private Point[] convertInput(String playerMove) {
		// Inital input check to ensure valid number of characters
		if (playerMove.length() != 5) {
			System.out.println("Invalid number of characters");
			return null;
		} 
		// validation to ensure correct hyphen usage
		if (playerMove.contains("--")|| playerMove.contains("---") ||
			playerMove.contains("----") || playerMove.contains("-----")){
			System.out.println("Too many hyphens!");
			return null;
		}
		
		// Split user input at hyphen for conversion
		String[] moves = playerMove.toLowerCase().split("-");
		
		// calidation to ensure hyphen seperates 2 distinct values.
		if (moves[0].length() != 2 || moves[1].length() != 2){
			System.out.println("Must include hyphen (-) between positions");
			return null;
		}

		// Convert user inputs to coordinates (x,y) format
		int fromRow = (moves[0].toCharArray()[0]-1)-48;
		int fromCol = (moves[0].toCharArray()[1]-49)-48;
		int toRow = (moves[1].toCharArray()[0]-1)-48;
		int toCol = (moves[1].toCharArray()[1]-49)-48;


		// Secondary validation to ensure input values fit board range
		if (fromRow < 0 || fromRow >= SIZE || fromCol < 0 || fromCol >= SIZE ||
			toRow < 0 || toRow >= SIZE || toCol < 0 || toCol >= SIZE)  {
			System.out.println("Input out of range of the board");
			return null;
		}

		Point from = new Point(fromRow, fromCol);
		Point to = new Point(toRow, toCol);

		Point[] convertedMoves = {from, to};

		return convertedMoves;
	}


	/**
	 * Logic specific for valid move checked against the board boundaries, 
	 * checkers interaction rules (attacks), and the individual chips valid 
	 * moveset.
	 * @param from	Origin
	 * @param to	Destination
	 * @return		{@code true} if move is valid
	 */
	private boolean isValidMove(Point from, Point to) {
		
		boolean initialCheck = 
			
			// to check valid on board, computer check (redundancy for human)
			gameBoard.isValidMoveBoard(from, to) 
				&&
			// check to ensure the origin location is valid
			isOriginValid(from) 
			  	&&
			// Check to ensure the destination is not blocked
			isDestinationValid(to);

		if (initialCheck) {
			// Check if valid Chip move OR Chip attack
			// isAttack decouples logic from single-space valid moveset 
			if (isAttack(from, to) || gameBoard.isValidMoveChip(from, to, false)) {
				return true;
			}
		}
		return false;
	}
	

	/**
	 * Record the players move, adds additional movepoint if attacking
	 * @param from	Origin
	 * @param to	Destination
	 */
	private void recordMove(Point from, Point to) {
		// Default number of move coordinates
		int stateSize = 2;
		
		// If attack, add number of attack points to array length
		if (attackPoints != null) { 
			stateSize+=attackPoints.length; 
		}
		// Generate new array and copy default points
		Point[] stateChange = new Point[stateSize];
		stateChange[0] = new Point(from);
		stateChange[1] = new Point(to);

		// add additional attack points if applicable
		if (attackPoints != null) {
			for (int i = 2; i < attackPoints.length+2; i++){
				stateChange[i] = attackPoints[0];
			}
		}
		// Execute
		gameBoard.updateState(stateChange);
	}


	/**
	 * swaps the current activePlayer with inactive player
	 */
	private void toggleActivePlayer() {
		if (activePlayer == 0) {
			activePlayer = 1;
		} else {
			activePlayer = 0;
		}
	}

	/**
	 * Determines end game state if player can no longer make a valid move
	 * @return	{@code true} possible valid move exists
	 */
	private boolean canPlayerMove(){
		Point[][] chipPossibleMoves = gameBoard.getPossibleMoves(players[activePlayer]);
		for (int i = 0 ; i < chipPossibleMoves.length; i++) {
			if (isValidMove(chipPossibleMoves[i][0], chipPossibleMoves[i][1])){

				/*
				System.out.println("Valid Move at: (" + 
				chipPossibleMoves[i][0].x + "," + chipPossibleMoves[i][0].y + 
				") , (" + chipPossibleMoves[i][1].x + "," + 
				chipPossibleMoves[i][1].y + ")");
				*/

				// Reset attack points for final TRUE validation check
				attackPoints = new Point[0];
				return true;
			}
			// Reset attack points for EACH validation check
			attackPoints = new Point[0];
		}
		recordPlayerStats();
		return false;
	}


//-----------------------------------------------------------------------------
//						 		HELPER METHODS
//-----------------------------------------------------------------------------
	/**
	 * Helper method for {@code isValidMove()} to determine whether the players 
	 * origin input point is valid.
	 * @param from	origin of the chip being moved
	 * @return		{@code true} if the origin contains a {@code Chip} and that 
	 * 				{@code Chip} is owned by the active {@code Player}
	 */
	private boolean isOriginValid(Point from) {
		
		if (!gameBoard.isChip(from) ) { 
			return false; 
		} 
		if (!gameBoard.getChipOwner(from).equals(players[activePlayer])) {
			return false;
		}
		return true;
	}

	/**
	 * Helper method for {@code isValidMove()} to ensure destination is not 
	 * blocked
	 * @param to 	point of the chips destination
	 * @return		{@code true} if destination is empty
	 */
	private boolean isDestinationValid(Point to) {
		return !gameBoard.isChip(to);
	}


	// TODO allow for multiple jump attack
	/**
	 * Helper method for {@code isValidMove()} determines if the intented 
	 * player move is an attack move against an opponent chip.

	 * @param from	Origin
	 * @param to	Destination
	 * @return		{@code true} if the move will attack opponent Chip
	 */
	private boolean isAttack(Point from, Point to) {
		Point offset = new Point(to.x-from.x, to.y-from.y);
		// Terminate attack check if offset distance prohibits attack
		if (Math.abs(offset.x) < 2 || Math.abs(offset.y) < 2) { return false; }

		// Calculate the midPoint location of the enemy chip
		Point middleChipPoint = 
			new Point(from.x+(offset.x/2),from.y+(offset.y/2));
		
		// Check for enemy chip at midPoint
		boolean doesEnemyExist;
		try {
			doesEnemyExist = !gameBoard.
				getChipOwner(middleChipPoint).
					equals(players[activePlayer]);
		} catch (NullPointerException e) {
			doesEnemyExist = false;
		}

		// Check if the move is a valid extended move for the Chip
	 	if (gameBoard.isValidMoveChip(from, to, true) && doesEnemyExist) {
				attackPoints = new Point[1];
				attackPoints[0] = middleChipPoint;
				return true;
		}
		return false;
	}


	/**
	 *  records the win loss draw metrics for each player
	 */
	private void recordPlayerStats(){

		// If players both have the same number of chips - DRAW
		if (players[0].getChips() == players[1].getChips()) {
			players[0].setDraw();
			players[1].setDraw();
		} 
		// Player with the most chips left wins the game
		else if (players[0].getChips() > players[1].getChips()) {
			players[0].setWin();
			players[1].setLoss();
		} else if (players[1].getChips() > players[0].getChips()) {
			players[0].setLoss();
			players[1].setWin();
		}

		// reset chips for player
		players[0].resetChips(PLAYER_START_CHIPS);
		players[1].resetChips(PLAYER_START_CHIPS);
	}
}
