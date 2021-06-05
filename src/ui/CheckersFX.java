package ui;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.stage.StageStyle;

public class CheckersFX extends Application {

    //private Board board;

    @Override
    public void start(Stage primaryStage) {

        /*
        // Create popup for user defined game variables
        NewGame newGame = new NewGame();

        // Pull relevant variables from new game creation screen
        Person[] playerList = newGame.getPlayerList();
        House house = newGame.getHouse();
        house.setNumOfPlayers(newGame.getNumOfPlayers());

        // Create new board and house with user game setup info
        board = new Board(playerList, house, primaryStage);
        house.setBoard(board);
        Scene scene = board.getScene();

        //---------------------------------------------------------------------
        //GRID LINE AND STAGE VISIBILITY SETTINGS
        //---------------------------------------------------------------------
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        scene.setFill(null);

        // Draw the game stage
        primaryStage.setScene(scene);
        primaryStage.show();
        */
    }

    public static void main(String[] args) {
        // Launch the application
        launch(args);
    }

}


