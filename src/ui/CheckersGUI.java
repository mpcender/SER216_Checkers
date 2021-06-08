package ui;

import core.CheckersLogic;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.stage.StageStyle;
import ui.CheckersGUI_src.BoardGUI;
import ui.CheckersGUI_src.NewGame;

public class CheckersGUI extends Application  {

    // Player variables
	private static String[] players;

	// Game Board Active Game Variables
	private BoardGUI board;
	private static CheckersLogic checkersGame;
	private static boolean computerPlayer;


    @Override
    public void start(Stage primaryStage) throws Exception {

        // Create popup for user defined game variables
        NewGame newGame = new NewGame();
        computerPlayer = newGame.isComputer();
        players = newGame.getPlayerList();

		// Instantiate checkers game logic
		checkersGame = new CheckersLogic(players, computerPlayer);
		checkersGame.startGame();

		//**********************************************************
		// FX STUFF
		//**********************************************************
		board = new BoardGUI(checkersGame, primaryStage);
        Scene scene = board.getScene();
		primaryStage.initStyle(StageStyle.TRANSPARENT);
        scene.setFill(null);
        
		// Draw the game stage
        primaryStage.setScene(scene);
        primaryStage.show();
		board.init();
    }
    public static void main(String[] args, int i) {
        // Launch the application
        launch(args);
    }
    
}