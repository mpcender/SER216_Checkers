
package ui.CheckersGUI_src;

import static javafx.application.Application.STYLESHEET_CASPIAN;

import core.CheckersLogic;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author sephi
 */
public class GameComplete {

    private Stage dialog;
    private String[] playerList;
    private CheckersLogic checkersGame;

    
    public GameComplete(CheckersLogic checkersGame) {
        this.checkersGame = checkersGame;
        this.playerList = checkersGame.getPLayerNames();
        gameCompletePopup();
    }

    private void gameCompletePopup() {
        dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);

        // Draw setup Stage
        GridPane gpane = drawStage();
        Scene dialogScene = new Scene(gpane, 400, 350);

        dialog.setScene(dialogScene);
        dialog.showAndWait();
    }

    
    /** 
     * @return GridPane
     */
    private GridPane drawStage() {

        int[][] records = checkersGame.getPlayerStats();

        Button playAgain = new Button("Play Again");
        playAgain.setAlignment(Pos.CENTER_LEFT);
        Button exit = new Button("Exit");
        exit.setAlignment(Pos.CENTER_RIGHT);
        newGameClick(playAgain);
        exitClick(exit);

        Label gameOver = new Label("GAME OVER");
        gameOver.setFont(Font.font(STYLESHEET_CASPIAN, 30));
        gameOver.setAlignment(Pos.CENTER);

        Label win = setStyle("Wins:");
        Label loss = setStyle("Loss:");
        Label draw = setStyle("Draw:");
        Label winP1 =  setStyle(records[0][0]+"");
        Label lossP1 = setStyle(records[0][1]+"");
        Label drawP1 = setStyle(records[0][2]+"");
        Label  winP2 = setStyle(records[1][0]+"");
        Label lossP2 = setStyle(records[1][1]+"");
        Label drawP2 = setStyle(records[1][2]+"");

        Label playerLabell = setStyle(playerList[0]);
        Label playerLabel2 = setStyle(playerList[1]);


        GridPane gpane = new GridPane();
        gpane = draw3x6Grid(gpane, 15, 15, 15, 15, 15, 15, 30, 30, 30, 10, 20);
        gpane.add(gameOver, 0, 0 , 6, 1);

        gpane.add(playerLabell, 1, 1);
        gpane.add(playerLabel2, 2, 1);

        gpane.add(win, 0, 2);
        gpane.add(loss, 0, 3);
        gpane.add(draw, 0, 4);

        
        gpane.add(winP1, 1, 2);
        gpane.add(lossP1, 1, 3);
        gpane.add(drawP1, 1, 4);

        gpane.add(winP2, 2, 2);
        gpane.add(lossP2, 2, 3);
        gpane.add(drawP2, 2, 4);

        gpane.add(playAgain, 0, 6, 2,1);
        gpane.add(exit, 3, 6,4,3);

        GridPane gfinal = new GridPane();
        gfinal.add(gpane, 0, 0);

        String cssLayout = "-fx-border-color: black;\n"
                + "-fx-border-insets: 5;\n"
                + "-fx-border-width: 3;\n";
        gfinal.setStyle(cssLayout);
        gfinal.setBackground(new Background(new BackgroundFill(
                Color.BEIGE, CornerRadii.EMPTY, Insets.EMPTY)));
        gfinal.setAlignment(Pos.CENTER);

        return gfinal;
    }

    
    /** 
     * @param text
     * @return Label
     */
    private Label setStyle(String text) {
        Label label = new Label(text);
        label.setFont(Font.font(STYLESHEET_CASPIAN, FontWeight.BOLD,16));
        label.setAlignment(Pos.CENTER);
        return label;
    }

    
    /** 
     * @return GridPane
     */
    /*
	private void printStats() {

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
    */

    private GridPane draw3x6Grid(GridPane pane,
            double Row_1, double Row_2, double Row_3,
            double Row_4, double Row_5, double Row_6,
            double Col_1, double Col_2, double Col_3,
            double Col_4, double Col_5) {
        // Make grids a set size with set row/col
        final int numRows = 6;
        final int numCols = 5;
        for (int i = 0; i < numCols; i++) {
            ColumnConstraints colConst = new ColumnConstraints();
            if (i == 0) {
                colConst.setPercentWidth(Col_1);
            } else if (i == 1) {
                colConst.setPercentWidth(Col_2);
            } else if (i == 2) {
                colConst.setPercentWidth(Col_3);
            } else if (i == 3) {
                colConst.setPercentWidth(Col_4);
            } else {
                colConst.setPercentWidth(Col_5);
            }
            pane.getColumnConstraints().add(colConst);
        }
        for (int i = 0; i < numRows; i++) {
            RowConstraints rowConst = new RowConstraints();
            if (i == 0) {
                rowConst.setPercentHeight(Row_1);
            } else if (i == 1) {
                rowConst.setPercentHeight(Row_2);
            } else if (i == 2) {
                rowConst.setPercentHeight(Row_3);
            } else if (i == 3) {
                rowConst.setPercentHeight(Row_4);
            } else if (i == 4) {
                rowConst.setPercentHeight(Row_5);
            } else {
                rowConst.setPercentHeight(Row_6);
            }
            pane.getRowConstraints().add(rowConst);
        }
        return pane;
    }
    
    
    /** 
     * @param button
     */
    private void newGameClick(Button button){
        button.setOnAction(event -> {
            dialog.close();
            return;

        });
    }
    
    /** 
     * @param button
     */
    private void exitClick(Button button){
        button.setOnAction(event -> {
            Platform.exit();
            dialog.close();
            return;

        });
    }

}
