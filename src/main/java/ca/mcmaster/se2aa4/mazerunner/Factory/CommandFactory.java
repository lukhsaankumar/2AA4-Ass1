package ca.mcmaster.se2aa4.mazerunner.Factory;

import ca.mcmaster.se2aa4.mazerunner.Commands.MovementCommand;
import ca.mcmaster.se2aa4.mazerunner.Maze;
import ca.mcmaster.se2aa4.mazerunner.MazeExplorer;
import ca.mcmaster.se2aa4.mazerunner.Commands.MoveForwardCommand;
import ca.mcmaster.se2aa4.mazerunner.Commands.TurnLeftCommand;
import ca.mcmaster.se2aa4.mazerunner.Commands.TurnRightCommand;

public class CommandFactory {
    private final MazeExplorer explorer;
    private final Maze maze;

    public CommandFactory(MazeExplorer explorer, Maze maze) {
        this.explorer = explorer;
        this.maze = maze;
    }

    public MovementCommand getCommand(char action) {
        if (action == 'F') {
            return new MoveForwardCommand(explorer, maze);
        } else if (action == 'L') {
            return new TurnLeftCommand(explorer);
        } else if (action == 'R') {
            return new TurnRightCommand(explorer);
        } else {
            return null;
        }
    }
}