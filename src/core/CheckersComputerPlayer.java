package core;

import java.awt.Point;
import java.util.Random;

/**
 * Checkers computer player is a player object operated by an
 * computer opponent.
 *
 * @author Matt Cummings
 * @version 6/3/2021
 */


public class CheckersComputerPlayer extends Player {

    

    public CheckersComputerPlayer(String name, int id, int chips, boolean isComp) {
        super(name, id, chips, isComp);
        //TODO Auto-generated constructor stub
    }

    /**
     * Prompts the computer player to make a move
     * @param board     Object allows the computer to analyze current game state
     *                  to determine move. (Currently unused)
     * @param allMoves  List of all validated moves computer is capable of.
     * @return          {@code Point[]} with the computers chosen move
     */
    public Point[] makeMove(Board board, Point[][] allMoves){


        return randomMove(allMoves);
    }

    /**
     * Generates random move from list of possible moves
     * @param allMoves  List of all validated moves computer is capable of.
     * @return          {@code Point[]} with the computers randomized move.
     */
    private Point[] randomMove(Point[][] allMoves) {
        Random rand = new Random();
        int i = rand.nextInt(allMoves.length);

        Point[] move = new Point[2];
        move[0] = allMoves[i][0];
        move[1] = allMoves[i][1];

        return move;
    }
    
}
