package ca.mcmaster.se2aa4.mazerunner.Factory;

import ca.mcmaster.se2aa4.mazerunner.Solver.ValidatePathSolver;
import ca.mcmaster.se2aa4.mazerunner.Maze;
import ca.mcmaster.se2aa4.mazerunner.Solver.ExploreMazeSolver;
import ca.mcmaster.se2aa4.mazerunner.Solver.MazeSolver;

public class MazeSolverFactory {
    public static MazeSolver createTool(Maze maze, String path) {
        if(path != null && !path.isEmpty()) {
            return new ValidatePathSolver(maze, path);
        } else {
            return new ExploreMazeSolver(maze);
        }
    }
}