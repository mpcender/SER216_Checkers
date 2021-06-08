package ui;

/**
 * Checkers UI class to run checkers game
 *
 * @author Matt Cummings
 * @version 5/25/2021
 */

//import java.awt.Point;
import java.util.Scanner;
//import java.util.concurrent.TimeUnit;

import core.CheckersLogic;
import java.awt.Point;

public class CheckersTextConsole {

//-----------------------------------------------------------------------------
//						 		CLASS VARIABLES
//-----------------------------------------------------------------------------
	// Game Board UI Variables
	private static char o = '\u24DE';
	private static char x = '\u24E7';
	//private static char nul = ' ';
	private static char lin = '\u2015';
	/*
	private static final char[][] emptyBoard = 
		{ { nul ,  o  , nul ,  o  , nul ,  o  , nul ,  o  } ,
		  {  o  , nul ,  o  , nul ,  o  , nul ,  o  , nul } ,
		  { nul ,  o  , nul ,  o  , nul ,  o  , nul ,  o  } ,
		  { nul , nul , nul , nul , nul , nul , nul , nul } ,
		  { nul , nul , nul , nul , nul , nul , nul , nul } ,
		  {  x  , nul ,  x  , nul ,  x  , nul ,  x  , nul } ,
		  { nul ,  x  , nul ,  x  , nul ,  x  , nul ,  x  } ,
		  {  x  , nul ,  x  , nul ,  x  , nul ,  x  , nul } ,};
	*/

	// Player variables
	private static String[] players = new String[2];

	// Game Board Active Game Variables
	private static char[][] gameBoard;
	private static CheckersLogic checkersGame;
	private static Scanner userIn;
	private static boolean gameActive;
	private static boolean computerPlayer;
	private static boolean keepPlaying;


//-----------------------------------------------------------------------------
//						 		MAIN METHOD
//-----------------------------------------------------------------------------
	public static void main(String[] args) {
		boolean gui = textOrGui();

		if (gui) {
			CheckersGUI.main(args, 1);
		}else {
			keepPlaying = true;

			// Set player names (modify to allow user input?)
			computerPlayer = playAgainstComputer();
			if (computerPlayer) { 
				players[0] = "Player"; players[1] = "Computer";
			} else {
				players[0] = "Player X"; players[1] = "Player O";
			}

			// Instantiate checkers game logic
			checkersGame = new CheckersLogic(players, computerPlayer);

			while (keepPlaying) {

				gameBoard = checkersGame.startGame();


				gameActive = true;
				printBoard(gameBoard);

				System.out.print("\nBegin Game. ");
				userIn = new Scanner(System.in);

				while (gameActive) {
					// Get user input from active user
					getUserInput();
					// Check if game is completed
					gameActive = !checkersGame.isGameComplete();
				}

				printStats();
				playAgain();
			}
		}
		
		
	}

//-----------------------------------------------------------------------------
//						 		UI METHODS
//-----------------------------------------------------------------------------
	/**
	 * Prompts and accepts user input
	 */
	private static void getUserInput(){
		

		//System.out.println("comp: " + computerPlayer + " - isTurn: "+ checkersGame.isComputerTurn());
		// Short circuit if local computer variable false
		if (computerPlayer && checkersGame.isComputerTurn()) {
			System.out.println("Computer (" + 
			checkersGame.getPlayerChip() + ") moving BEEP BOOP BOOP");
			// Timout for 3 sec on comp move to make output more human readable
			/*
			try {
				TimeUnit.SECONDS.sleep(3);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
			*/
			// update game board with computer move
			checkersGame.computerMove();
			gameBoard = checkersGame.getCurrentState();
		} else {
			// Promt player for input
			System.out.println(checkersGame.getPlayer() + " (" + 
			checkersGame.getPlayerChip() + ") – your turn." + 
			"\nChoose a cell position of piece to be moved and the new " +
			"position. e.g., 3a-4b ");

			Point[] movePoints = null;
			String userMove;
			// Continue to accept input until a valid input is provided
			while (movePoints == null) {
				// Get input from the user
				userMove = userIn.nextLine();
				// Validate input
				movePoints = checkersGame.validateMove(userMove);
			}
			checkersGame.confirmMove(movePoints);

			// Update game board state to the user
			gameBoard = checkersGame.getCurrentState();
		}
		printBoard(gameBoard);
	}

	/**
	 * Method prints a char[][] as a board stylized with custom characters
	 * @param board		an array with Chip locations
	 */
	private static void printBoard(char[][] board) {
		char col = 'a';
		printLine();

		for (int i = 0; i < board.length; i++) {
			System.out.print((i+1) + "    ");
			for (int j = 0; j < board[0].length; j++) {
				System.out.print("| ");
				if (board[i][j] == 'o') {
					System.out.print(o);
				} else if (board[i][j] == 'x') {
					System.out.print(x);
				} else {
					System.out.print(board[i][j]);
				}
				System.out.print("  ");
			}
			System.out.println("|");
			printLine();
		}
		System.out.print("\n     ");
		for (int i = 0; i < board.length; i++) {
			System.out.print("  " + col + "  ");
			col++;
		}
		System.out.print("\n");
	}

	/**
	 * Helper method for {@code printBoard()} to print horizontal line
	 */
	private static void printLine(){ 
		System.out.print("     ");
		for (int i = 0; i < 14; i++) {
			System.out.print(lin);
			System.out.print("  ");
		}
		System.out.print("\n");
	}

	/**
	 * 
	 */
	private static void printStats() {

		int[][] records = checkersGame.getPlayerStats();
		printLine();
		System.out.print("\n          G   A   M   E     O   V   E   R\n\n");
		printLine();
		System.out.print("\n");
		System.out.print("     " + players[0] + 
						"\n          Win : " + records[0][0] +
						"\n          Loss: " + records[0][1] +
						"\n          Draw: " + records[0][2]);
		System.out.print("\n");
		System.out.print("     " + players[1] + 
						"\n          Win : " + records[1][0] +
						"\n          Loss: " + records[1][1]+
						"\n          Draw: " + records[1][2]);
						System.out.print("\n\n");
		printLine();
	}
	
	/**
	 * 
	 */
	private static void playAgain() {
		System.out.print("Would you like to play again? (Y/N): ");

		String userReply = validateYesNo();

		if (userReply.contains("Y")){
			keepPlaying = true;
		} else {
			keepPlaying = false;
			System.out.println("Thanks for Playing!!");
		}		
	}

	/**
	 * 
	 * @return
	 */
	private static boolean playAgainstComputer() {
		System.out.print("Would you like to against the computer (Y/N): ");
		String userReply = validateYesNo();
		return userReply.contains("Y");
	}

	/**
	 * 
	 * @return
	 */
	private static String validateYesNo() {
		userIn = new Scanner(System.in);
		String userReply = userIn.nextLine();

		// Continue to accept input until a valid input is provided
		while(!(userReply.contains("N") ^ userReply.contains("Y"))) {
			System.out.print("Please input a valid response (Y,N)");
			userReply = userIn.nextLine();
		}
		return userReply;
	}


	/**
	 * 
	 */
	private static boolean textOrGui() {
		System.out.print("GUI or console-based UI? (GUI/CON): ");

		userIn = new Scanner(System.in);
		String userReply = userIn.nextLine();

		// Continue to accept input until a valid input is provided
		while(!(userReply.contains("GUI") ^ userReply.contains("CON"))) {
			System.out.print("Please input a valid response (GUI/CON)");
			userReply = userIn.nextLine();
		}

		if (userReply.contains("GUI")){
			return true;
		} else {
			return false;
		}		
	}
}
