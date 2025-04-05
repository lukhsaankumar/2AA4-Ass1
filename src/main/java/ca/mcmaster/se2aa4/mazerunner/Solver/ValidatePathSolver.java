package ca.mcmaster.se2aa4.mazerunner.Solver;

import ca.mcmaster.se2aa4.mazerunner.Path.PathChecker;
import ca.mcmaster.se2aa4.mazerunner.Maze;

public class ValidatePathSolver implements MazeSolver {
    private final Maze maze;
    private final String path;

    public ValidatePathSolver(Maze maze, String path) {
        this.maze = maze;
        this.path = path;
    }   

    @Override
    public void solve() {
        PathChecker pathChecker = new PathChecker(maze);
        boolean isValid = pathChecker.checkPath(path);
        if (isValid) {
            System.out.println("Path is valid");
        } else {
            System.out.println("Path is invalid");
        }
    }
}