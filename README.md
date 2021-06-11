## Checkers Board Game Project

[Click here for a video of the running program](https://recordit.co/SzfD0LFYLY)

This project was an ongoing project for SER216 to build a functional checkers 
game. Each module required the addition of improved functionality including 
computer player and JavaFX GUI.

The project was built in VS Code. 
  - The JavaFX Library contained in this project is configured in the launch.json file under the .vscode dir. The project must be opened to the root folder to run properly. Otherwise modify the "configurations" --> "vmArgs" directory PATH to match the JavaFX lib location within this project on your computer.

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

