package ca.mcmaster.se2aa4.mazerunner.Solver;

import ca.mcmaster.se2aa4.mazerunner.Maze;
import ca.mcmaster.se2aa4.mazerunner.Path.Path;
import ca.mcmaster.se2aa4.mazerunner.Path.FindPath;

public class ExploreMazeSolver implements MazeSolver {
    private final Maze maze;

    public ExploreMazeSolver(Maze maze) {
        this.maze = maze;
    }

    @Override
    public void solve() {
        FindPath pathFinder = new FindPath(maze);
        Path path = pathFinder.solve();
        System.out.println(path.getFactorizedPath());
    }
}