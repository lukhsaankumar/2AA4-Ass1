package ca.mcmaster.se2aa4.mazerunner.Commands;

import ca.mcmaster.se2aa4.mazerunner.MazeExplorer;

public class TurnRightCommand implements MovementCommand {
    private final MazeExplorer explorer;

    public TurnRightCommand(MazeExplorer explorer) { this.explorer = explorer; }

    @Override
    public boolean execute() {
        explorer.turnRight();
        return true;
    }
}
