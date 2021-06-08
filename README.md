## Checkers Board Game Project

This project was an ongoing project for SER216 to build a functional checkers 
game. Each module required the addition of improved functionality including 
computer player and JavaFX GUI.

The project was built in VS Code. 
  - To connect to the JavaFX Library contained in this project access the launch.json file under the .vscode dir. Modify the "configurations" --> "vmArgs" directory path to the JavaFX lib to match the location of this project on your computer.

## Folder Structure

The workspace contains three primary folders

- `src`: the folder to maintain sources
  - `core`: Game logic and components
  - `ui`: User interface modules
    - `CheckersGUI_src`: JavaFX UI objects for Checkers menu and game
      - `res`: png and jpg sources for checkers objects
  - `docs`: Generated Javadoc
- `lib`: the folder to maintain dependencies
  - `javafx-sdk-11.0.2`: JavaFX dependencies

