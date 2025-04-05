package ca.mcmaster.se2aa4.mazerunner;

import ca.mcmaster.se2aa4.mazerunner.Path.FindPath;
import ca.mcmaster.se2aa4.mazerunner.Path.Path;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MazeExplorerTest {

    @Test
    public void testPathExplorationStraightMaze() {
        Maze maze = new Maze("examples/straight.maz.txt");
        FindPath solver = new FindPath(maze);
        Path path = solver.solve();
        assertEquals("R L F R L F R L F R L F", path.getFactorizedPath());
    }

    @Test
    public void testExplorerTurnsAndMoves() {
        Maze maze = new Maze("examples/rectangle.maz.txt");
        FindPath solver = new FindPath(maze);
        Path path = solver.solve();
        assertTrue(path.getFactorizedPath().contains("L") || path.getFactorizedPath().contains("R"));
    }
}
