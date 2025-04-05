package ca.mcmaster.se2aa4.mazerunner.Commands;

import java.awt.Point;
import java.util.Map;
import ca.mcmaster.se2aa4.mazerunner.MazeExplorer;
import ca.mcmaster.se2aa4.mazerunner.Maze;

public class MoveForwardCommand implements MovementCommand {
    private final MazeExplorer explorer;
    private final Maze maze;

    private static final Map<Character, Point> directionDelta = Map.of(
        'N', new Point(0, -1),
        'E', new Point(1, 0),
        'S', new Point(0, 1),
        'W', new Point(-1, 0)
    );

    public MoveForwardCommand(MazeExplorer explorer, Maze maze) {
        this.explorer = explorer;
        this.maze = maze;
    }

    public boolean execute() {
        Point position = explorer.getPosition();
        Point offset = directionDelta.get(explorer.getDirection());
        if (offset == null) return false;
        Point nextPosition = new Point(position.x + offset.x, position.y + offset.y);
        int rowIndex = nextPosition.y - 1;
        int colIndex = nextPosition.x - 1;
        if (rowIndex < 0 || rowIndex >= maze.getHeight() || colIndex < 0 || colIndex >= maze.getWidth()) return false;
        if (!maze.isPassage(nextPosition)) return false;
        explorer.moveForward();
        return true;
    }
}
