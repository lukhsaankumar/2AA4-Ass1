package ca.mcmaster.se2aa4.mazerunner;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class MazeExplorerTest {

    @Test
    public void testPathExplorationStraightMaze() {
        char[][] maze = Main.readMaze("examples/straight.maz.txt");
        int[] entry = Main.findEntryPoint(maze);
        int[] exit = Main.findExitPoint(maze);
        MazeExplorer explorer = new MazeExplorer(maze, entry);
        String path = explorer.exploreMaze(exit);
        assertEquals("FFFF", path);
    }

    @Test
    public void testExplorerTurnsAndMoves() {
        char[][] maze = Main.readMaze("examples/rectangle.maz.txt");
        int[] entry = Main.findEntryPoint(maze);
        int[] exit = Main.findExitPoint(maze);
        MazeExplorer explorer = new MazeExplorer(maze, entry);
        String path = explorer.exploreMaze(exit);
        assertTrue(path.contains("L") || path.contains("R"));
    }
}