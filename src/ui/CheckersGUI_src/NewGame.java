
package ui.CheckersGUI_src;

import static javafx.application.Application.STYLESHEET_CASPIAN;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Creates New Game screen to setup players
 * @author Matt Cummings
 */
public class NewGame {

    private Stage dialog;
    private String[] playerList = new String[2];
    private boolean isComputer;

    private Label playerLabell = new Label("Player 1: ");
    private Label playerLabel2 = new Label("Player 2: ");

    private TextField playerName1 = new TextField("Player 1");
    private TextField playerName2 = new TextField("Player 2");

    public NewGame() {
        newGamePopup();
    }

    
    /** 
     * Check if computer is active
     * @return boolean indicating computer participation
     */
    public boolean isComputer(){
        return isComputer;
    }

    
    /** 
     * Get the ordered list of players in the match
     * @return String[] with player names
     */
    public String[] getPlayerList() {
        return playerList;
    }

    /**
     * Method to draw the popup GUI
     */
    private void newGamePopup() {
        dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);

        // Draw setup Stage
        GridPane gpane = drawStage();
        Scene dialogScene = new Scene(gpane, 350, 250);

        dialog.setScene(dialogScene);
        dialog.showAndWait();
    }

    
    /** 
     * Generate sub-components of the popup window
     * @return GridPane containing the sub-components
     */
    private GridPane drawStage() {
        playerLabell.setFont(Font.font(STYLESHEET_CASPIAN, 16));
        playerLabel2.setFont(Font.font(STYLESHEET_CASPIAN, 16));

        Button start = new Button("Start Game!");
        
        
        start.setAlignment(Pos.CENTER);

        MenuButton menuButton = new MenuButton("Start Game");
        MenuItem human = new MenuItem("Human vs Human");
        MenuItem computer = new MenuItem("Human vs Computer");
        human.setId("human");
        computer.setId("computer");
        
        startGameClick(human);
        startGameClick(computer);
        menuButton.getItems().addAll(human, computer);
        

        GridPane gpane = new GridPane();
        gpane = draw3x6Grid(gpane, 15, 15, 15, 15, 15, 15, 30, 30, 30, 10, 20);
        gpane.add(playerLabell, 1, 0);
        gpane.add(playerLabel2, 1, 1);

        gpane.add(playerName1, 2, 0);
        gpane.add(playerName2, 2, 1);


        GridPane gfinal = new GridPane();
        gfinal.add(gpane, 0, 0);
        //gfinal.add(start, 0, 2);
        gfinal.add(menuButton, 0, 2);
        gfinal.getChildren().get(1).setTranslateX(135);
        gfinal.getChildren().get(1).setScaleX(1.5);
        gfinal.getChildren().get(1).setScaleY(1.5);

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
     * Generate a 3*6 gridPane 
     * @param pane      gridPane to be augmented
     * @param Row_1     Percent height
     * @param Row_2     Percent height
     * @param Row_3     Percent height
     * @param Row_4     Percent height
     * @param Row_5     Percent height
     * @param Row_6     Percent height
     * @param Col_1     Percent width
     * @param Col_2     Percent width
     * @param Col_3     Percent width
     * @param Col_4     Percent width
     * @param Col_5     Percent width
     * @return          GridPane with updated row / rol setup
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
     * Button handler to proceed to active game board
     * @param selection The button to set action on
     */
    private void startGameClick(MenuItem selection){
        selection.setOnAction(event -> {
            String id = ((MenuItem)event.getSource()).getId();
            if (id.equals("computer")){
                this.isComputer = true;
                this.playerList[0] = this.playerName1.getText();
                this.playerList[1] = "AI: " + this.playerName2.getText();
            } else if (id.equals("human")) {
                this.isComputer = false;
                this.playerList[0] = playerName1.getText();
                this.playerList[1] = playerName2.getText();

            } 
            dialog.close();
            return;

        });
    }

}
