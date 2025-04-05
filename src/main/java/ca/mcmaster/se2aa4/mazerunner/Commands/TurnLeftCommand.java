package ca.mcmaster.se2aa4.mazerunner.Commands;

import ca.mcmaster.se2aa4.mazerunner.MazeExplorer;

public class TurnLeftCommand implements MovementCommand {
    private final MazeExplorer explorer;

    public TurnLeftCommand(MazeExplorer explorer) { this.explorer = explorer; }

    @Override
    public boolean execute() {
        explorer.turnLeft();
        return true;
    }
}
