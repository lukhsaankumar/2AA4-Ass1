package ca.mcmaster.se2aa4.mazerunner.Path;

import java.awt.Point;
import ca.mcmaster.se2aa4.mazerunner.Maze;
import ca.mcmaster.se2aa4.mazerunner.MazeExplorer;

public class FindPath {
    private final Maze grid;
    private final MazeExplorer scout;

    public FindPath(Maze grid) {
        this.grid = grid;
        this.scout = new MazeExplorer(grid.getEntry(), 'E');
    }

    public Path solve() {
        while (!scout.getPosition().equals(grid.getExit())) {
            scout.turnRight();
            Point tryRight = next(scout.getPosition(), scout.getDirection());

            if (canAdvance(tryRight)) {
                scout.moveForward();
            } else {
                scout.turnLeft();
                Point tryAhead = next(scout.getPosition(), scout.getDirection());

                if (canAdvance(tryAhead)) {
                    scout.moveForward();
                } else {
                    scout.turnLeft();
                }
            }
        }
        return scout.getPath();
    }

    private Point next(Point current, char dir) {
        Point move = new Point(current.x, current.y);
        if (dir == 'N') move.y--;
        else if (dir == 'E') move.x++;
        else if (dir == 'S') move.y++;
        else move.x--;
        return move;
    }

    private boolean canAdvance(Point spot) {
        int r = spot.y - 1;
        int c = spot.x - 1;
        if (r < 0 || r >= grid.getHeight() || c < 0 || c >= grid.getWidth()) return false;
        return grid.isPassage(spot);
    }
}
