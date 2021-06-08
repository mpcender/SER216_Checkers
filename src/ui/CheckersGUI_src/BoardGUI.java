package ui.CheckersGUI_src;

import core.CheckersLogic;
import static javafx.application.Application.STYLESHEET_CASPIAN;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.awt.Point;

/**
 * Board class for In-Between game
 *
 * @author sephi
 */
public class BoardGUI {

    // initialize resource images for game
    private final static Image DARK_CHIP = 
        new Image("file:src/ui/CheckersGUI_src/res/dark_chip.png");
    private final static Image LIGHT_CHIP = 
        new Image("file:src/ui/CheckersGUI_src/res/light_chip.png");
    private final int BOARD_SIZE = 900;
    private final int GRID = 8;

    // Class instances of primary variables
    private Stage primaryStage;
    private Scene scene;
    private GridPane chipPane;
    private GridPane rootPane;
    private VBox footerVBox;
    private char[][] gameBoard;
    private CheckersLogic checkersGame;
    private Button submitButton;
    private boolean computerMoving;

    //String constants for main stage readouts
    private final String welcomeBanner = "Checkers";
    private final String currentPlayer = "Current Player: ";

    // Variables for tracking stage movement
    private double initialX;
    private double initialY;
    
    /**
     * Constructor builds the GUI stage and assigns event handlers
     * @param checkersGame  logic for checkers
     * @param primaryStage  main stage for board and chips
     */
    public BoardGUI(CheckersLogic checkersGame, Stage primaryStage) {
        this.checkersGame = checkersGame;
        this.primaryStage = primaryStage;

        // DRAW CONTAINERS
        this.rootPane = new GridPane();
        this.rootPane = drawStage();
        scene = new Scene(rootPane, BOARD_SIZE, BOARD_SIZE);
    }

    /**
     * Scene Getter
     * @return  scene
     */
    public Scene getScene() {
        return scene;
    }

    /**
     * Initial game state checks required after instantiation
     */
    public void init() {
        submitActionEvent();
        checkComputerMove();
    }

    /**
     * Reset the game on GAME OVER -> Player Continue
     */
    public void resetBoard() {
        new GameComplete(checkersGame);

        // Start new game
        gameBoard = checkersGame.startGame();

        // Draw the stage with current information
        this.rootPane = new GridPane();
        this.rootPane = drawStage();

        // Update the scene
        Scene newScene = new Scene(this.rootPane, BOARD_SIZE, BOARD_SIZE);
        // Make background transparent
        newScene.setFill(null);
        // Set scene and show
        primaryStage.setScene(newScene);
        primaryStage.show();

        checkComputerMove();
    }


    /**
     * Draw the full stage with Grid, Background, Text, and fields
     *
     * @param rootPane  Main GridPane on the Stage
     * @return          Updated GridPane
     */
    private GridPane drawStage() {
        Image board = new Image(
            "file:src/ui/CheckersGUI_src/res/checkersBoard800.png", 
            BOARD_SIZE, BOARD_SIZE, true, true);

        // Set the size and position of the Background
        BackgroundSize myBIsize = new BackgroundSize(
                10, 10, true, true, true, true);
        BackgroundPosition myBIpos = new BackgroundPosition(
                Side.LEFT, 50, true, Side.TOP, 50, true);

        BackgroundImage myBI = new BackgroundImage(board ,
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, myBIpos,
                myBIsize);
        rootPane.setBackground(new Background(myBI));
        rootPane = draw3x3Grid(rootPane, 6.0, 88.0, 6.0, 6.0, 88.0, 6.0);

        // TOP - Create Checkers Banner that allows window movement
        VBox bannerVBox = drawBanner(welcomeBanner);
        bannerVBox.setAlignment(Pos.CENTER);
        dragWindowVbox(bannerVBox);

        // Draw sub-grid for Chips
        chipPane = new GridPane();
        chipPane = drawGrid(chipPane, GRID);
        
        // Footer for showing current active player name.
        footerVBox = drawFooter(currentPlayer);

        // Populate the board with chip objects from checkersLogic
        gameBoard = checkersGame.getCurrentState();
        for (int i = 0; i < gameBoard.length; i++) {
            for (int j = 0; j < gameBoard.length; j++) {
                if (gameBoard[j][i] == 'o') {
                    ImageView chip = chipBuilder(DARK_CHIP, i, j);
                    chipPane.add(chip, i,j);
                }
                if (gameBoard[j][i] == 'x') {
                    ImageView chip = chipBuilder(LIGHT_CHIP, i, j);
                    chipPane.add(chip, i,j);
                }
            }
        }

        rootPane.add(bannerVBox, 0, 0, 3, 1);
        rootPane.add(footerVBox, 0, 2, 3, 1);
        rootPane.add(chipPane, 1, 1);

        // Button to exit program
        submitButton = new Button();
        submitButton.setGraphic(new ImageView("file:src/ui/CheckersGUI_src/res/x.png"));
        submitButton.setStyle("-fx-background-color: transparent;\n");
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(2.0);
        dropShadow.setSpread(1);
        dropShadow.setColor(Color.WHEAT);
        submitButton.setEffect(dropShadow);
        rootPane.add(submitButton, 2, 0);

        //rootPane.setGridLinesVisible(true);
        //chipPane.setGridLinesVisible(true);
   
        return rootPane;
    }

    /**
     * 
     * Creates chip object to be added to chip GridPane
     * @param img   Image source for the chip object
     * @param x     x location in GridPane
     * @param y     y location in GridPane
     * @return      Chip object
     */
    private ImageView chipBuilder(Image img, int x, int y) {
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(10.0);
        dropShadow.setOffsetX(4.0);
        dropShadow.setOffsetY(4.0); 
        ImageView chip = new ImageView(img);
        chip.setEffect(dropShadow);
        chip.setFitHeight(60);
        chip.setFitWidth(60);
        chip.setX(x);
        chip.setY(y);
        dragChip(chip);
        return chip;
    }

    
    /**
     * Draws Adjustable 3x3 grid for stage
     *
     * @param pane      GridPane to be built
     * @param Row_1     Percent height of row 1
     * @param Row_2     Percent height of row 2
     * @param Row_3     Percent height of row 3
     * @param Col_1     Percent width of column 1
     * @param Col_2     Percent width of column 2
     * @param Col_3     Percent width of column 3
     * @return          modified GridPane
     */
    private GridPane draw3x3Grid(GridPane pane,
            double Row_1, double Row_2, double Row_3,
            double Col_1, double Col_2, double Col_3) {
        // Make grids a set size with set row/col
        final int numCols = 3;
        final int numRows = 3;

        for (int i = 0; i < numCols; i++) {
            ColumnConstraints colConst = new ColumnConstraints();
            if (i == 0) {
                colConst.setPercentWidth(Col_1);
            } else if (i == 1) {
                colConst.setPercentWidth(Col_2);
            } else {
                colConst.setPercentWidth(Col_3);
            }
            pane.getColumnConstraints().add(colConst);
        }
        for (int i = 0; i < numRows; i++) {
            RowConstraints rowConst = new RowConstraints();
            if (i == 0) {
                rowConst.setPercentHeight(Row_1);
            } else if (i == 1) {
                rowConst.setPercentHeight(Row_2);
            } else {
                rowConst.setPercentHeight(Row_3);
            }
            pane.getRowConstraints().add(rowConst);
        }
        return pane;
    }


    /**
     * Draw n*n size gridpane
     * @param pane  GridPane to be built
     * @param size  number of rows and columns
     * @return      modified GridPane
     */
    private GridPane drawGrid(GridPane pane, int size) {
        // Make grids a set size with set row/col
        for (int i = 0; i < size; i++) {
            ColumnConstraints colConst = new ColumnConstraints();
            RowConstraints rowConst = new RowConstraints();
            colConst.setPercentWidth(100/size+2);
            colConst.setHalignment(HPos.CENTER);
            rowConst.setPercentHeight(100/size+2);
            rowConst.setValignment(VPos.CENTER);
            pane.getColumnConstraints().add(colConst);
            pane.getRowConstraints().add(rowConst);
        }
        return pane;
    }


    /**
     * Draw Top welcome banner with checkers text
     * @param banner    text to display
     * @return          vbox of the created banner
     */
    private VBox drawBanner(String banner) {
        // Generate labels to display
        Label bannerLabel = new Label(banner);
        Image board = new Image("file:src/ui/CheckersGUI_src/res/checkersBoard800.png", 800, 800, true, true);

        bannerLabel.setTextFill(new ImagePattern(board,
            BOARD_SIZE, 0, board.getWidth(), board.getHeight(), false));
        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.BEIGE);
        dropShadow.setRadius(20.0);
        dropShadow.setSpread(0.4);
        bannerLabel.setEffect(dropShadow);
        // Set text styles
        bannerLabel.setFont(Font.font(STYLESHEET_CASPIAN, FontWeight.BOLD, 40));

        // Add to new vbox
        VBox vbox = new VBox(bannerLabel);

        // Set vbox properties and style
        vbox.setStyle("-fx-background-color: transparent");
        vbox.setAlignment(Pos.CENTER);

        // Return updated banner
        return vbox;
    }

    /**
     * Draw footer with name of the current active player
     * @param player    
     * @return          
     */
    private VBox drawFooter(String player) {
        // Generate labels to display
        Label playerLabel = new Label(player + checkersGame.getPlayer());
        
        // Set text styles
        playerLabel.setFont(Font.font(STYLESHEET_CASPIAN, FontWeight.BOLD, 20));
        
        playerLabel.setTextFill(Color.BEIGE);
        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.BLACK);
        dropShadow.setRadius(5.0);
        dropShadow.setSpread(0.4);
        dropShadow.setOffsetX(3.0);
        dropShadow.setOffsetY(3.0); 
        playerLabel.setEffect(dropShadow);
        // Add to new vbox
        VBox vbox = new VBox(playerLabel);

        // Set vbox properties and style
        vbox.setStyle("-fx-background-color: transparent");
        vbox.setAlignment(Pos.CENTER);

        // Return updated banner
        return vbox;
    }

    /**
     * Promt computer move and update UI state
     */
    private void checkComputerMove() {
        
        if (checkersGame.isComputerTurn()) {
            
            computerMoving = true;
            Point[] move = checkersGame.computerMove();

            // Get node at index of move origin
            Node node = getNodeByRowColumnIndex(move[0].x,move[0].y,chipPane);
            // Build new chip at location of move destination
            ImageView newChip = chipBuilder(((ImageView) node).getImage(), move[1].y,move[1].x);
            // Remove origin chip
            chipPane.getChildren().remove(node);
            // Place destination chip
            chipPane.add(newChip , move[1].y, move[1].x); 
            // Update board with chip moves
            checkGameState();

            if (checkersGame.isGameComplete()) {
                resetBoard();
            }
		}
        computerMoving = false;
        
    }

    //-----------------------------------------------------------------------
    //    
    //    EVENT HANDLERS
    //    
    //-----------------------------------------------------------------------
    
    /**
     * Allows user to exit the program
     */
    private void submitActionEvent() {
        submitButton.setOnAction(event -> {
            Platform.exit();
        });
    }

    /**
     * Allow click drag on the main stage window
     * @param vbox  Window to move
     */
    private void dragWindowVbox(final VBox vbox) {

        vbox.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {
                if (me.getButton() != MouseButton.MIDDLE) {
                    initialX = me.getSceneX();
                    initialY = me.getSceneY();
                }
            }
        });

        vbox.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {
                if (me.getButton() != MouseButton.MIDDLE) {
                    vbox.getScene().getWindow().setX(me.getScreenX() - initialX);
                    vbox.getScene().getWindow().setY(me.getScreenY() - initialY);
                }
            }
        });
    }

    
    /**
     * Allow click drag movement for Chip objects
     * @param chip Node to be moved
     */
    private void dragChip(final ImageView chip) {

        // All Handlers:
        // computerMoving disables chip transitions while computer is moving
        // Player can only move chips owned
        chip.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {
                if (me.getButton() != MouseButton.MIDDLE && !computerMoving) {
                    // Disallow movement of chips player doesnt own
                    if (checkersGame.getPlayerChip() == 'x' 
                        && chip.getImage().equals(LIGHT_CHIP)) {
                        handlePress(me, chip);
                    }
                    if (checkersGame.getPlayerChip() == 'o' 
                        && chip.getImage().equals(DARK_CHIP)) {
                        handlePress(me, chip);
                    }
                }
            }
        });

        chip.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {
                if (me.getButton() != MouseButton.MIDDLE && !computerMoving) {
                    if (checkersGame.getPlayerChip() == 'x' 
                        && chip.getImage().equals(LIGHT_CHIP)) {
                        chip.setTranslateX(me.getSceneX() - initialX);
                        chip.setTranslateY(me.getSceneY() - initialY);
                    }
                    if (checkersGame.getPlayerChip() == 'o' 
                        && chip.getImage().equals(DARK_CHIP)) {
                        chip.setTranslateX(me.getSceneX() - initialX);
                        chip.setTranslateY(me.getSceneY() - initialY);
                    }
                }
                
            }
        });

        chip.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {
                if (me.getButton() != MouseButton.MIDDLE && !computerMoving) {
                    if (checkersGame.getPlayerChip() == 'x' 
                        && chip.getImage().equals(LIGHT_CHIP)) {
                            handleRelease(me, chip);
                    }
                    if (checkersGame.getPlayerChip() == 'o' 
                        && chip.getImage().equals(DARK_CHIP)) {
                            handleRelease(me, chip);
                    }
                }
            }
        });

    }

    /**
     * Helper method for Chip mouse pressed handler
     * @param me    Mouse event to resolve
     * @param chip  Chip object event Target
     */
    private void handlePress(MouseEvent me, ImageView chip) {
        // Store inital coordinates in scene
        initialX = me.getSceneX();
        initialY = me.getSceneY();
    }

    /**
     * Helper method for Chip mouse released handler
     * @param me    Mouse event to resolve
     * @param chip  Chip object event Target
     */
    private void handleRelease(MouseEvent me, ImageView chip) {
        // Calculate new position
        double mod = (BOARD_SIZE-(BOARD_SIZE*(2.0/15.0)))/8;
        int x = (int)Math.round(me.getSceneX()/mod)-1;
        int y = (int)Math.round(me.getSceneY()/mod)-1;

        // Build string to pass to logic
        String input = convertMove((int)chip.getX(), (int)chip.getY(), x, y);

        // Validate move with checkers Logic
        Point[] move = checkersGame.validateMove(input);

        // If move invalid, return chip to its initial position
        if (move == null) {
            ImageView newChip = chipBuilder(chip.getImage(), 
                                (int)chip.getX(), (int)chip.getY());
            chipPane.getChildren().remove(chip);
            chipPane.add(newChip, (int)chip.getX(), (int)chip.getY());
        }
        // update game state
        else {
            ImageView newChip = chipBuilder(chip.getImage(), x, y);
            chipPane.getChildren().remove(chip);
            chipPane.add(newChip, x, y);
            checkersGame.confirmMove(move);

            checkGameState();

            if (checkersGame.isGameComplete()) {
                resetBoard();
            }

            checkComputerMove();
        }
    }

    /**
     * Generates String console-input equivalent of numeric array move locations
     * @param x1    Origin x
     * @param y1    Origin y
     * @param x2    Destination x
     * @param y2    Destination y
     * @return      String with int char output.
     */
    private String convertMove(int x1, int y1, int x2, int y2) {
        String input = (char)(y1+49) + "" + (char)(x1+97) + "-" + 
                        (char)(y2+49) + "" + (char)(x2+97) ;

        return input;
    }

    /**
     * Helper Method for checkComputerMove to locate chip node in gridPane
     * @param row       
     * @param column    
     * @param gridPane  
     * @return          
     */ 
    public Node getNodeByRowColumnIndex(final int row, final int column, GridPane gridPane) {
        Node result = null;
        ObservableList<Node> childrens = gridPane.getChildren();
    
        for (Node node : childrens) {
            if(gridPane.getRowIndex(node) == row && gridPane.getColumnIndex(node) == column) {
                result = node;
                break;
            }
        }
        return result;
    }

    /**
     * Update current player label and update if chip attack occured
     */
    private void checkGameState() {
        // Update player label
        this.rootPane.getChildren().remove(this.footerVBox);
        this.footerVBox = drawFooter(currentPlayer);
        rootPane.add(this.footerVBox, 0, 2, 3, 1);

        // Updated stat from checkers game logic
        char[][] newState = checkersGame.getCurrentState();
        Integer[] attack = null;
        
        for (int i = 0; i < GRID; i++) {
            for (int j = 0; j < GRID; j++) {
                if (gameBoard[i][j] == checkersGame.getPlayerChip() 
                    && newState[i][j] == ' ') {
                    attack = new Integer[2];
                    attack[0] = i;
                    attack[1] = j;
                    //System.out.println("ATTACK " + i + " , " + j);
                }
            }
        }
        int i=0;
        if (attack != null) {
            while (i < chipPane.getChildren().size()) {
                if (chipPane.getChildren().get(i) instanceof ImageView ) {
                    int x = (int)((ImageView) chipPane.getChildren().get(i)).getX();
                    int y = (int)((ImageView) chipPane.getChildren().get(i)).getY();
                    //System.out.println(x + " , " + y);
                    if (attack[0] == y && attack[1] == x) {
                        //System.out.println(attack[0] + " : " + x + " , " + attack[1] + " : " + y);
                        chipPane.getChildren().remove(chipPane.getChildren().get(i));
                    }
                }
                i++;
            }
            
        }
        gameBoard = checkersGame.getCurrentState();
    }

}
