package ca.mcmaster.se2aa4.mazerunner;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class CheckPathTest {

    @Test
    public void testCheckValidPath() {
        char[][] maze = Main.readMaze("examples/straight.maz.txt");
        int[] entry = Main.findEntryPoint(maze);
        assertTrue(Main.validatePath(maze, entry, "FFFF"));
    }

    @Test
    public void testCheckInvalidPath() {
        char[][] maze = Main.readMaze("examples/small.maz.txt");
        int[] entry = Main.findEntryPoint(maze);
        assertFalse(Main.validatePath(maze, entry, "FFFFF"));
    }

    @Test
    public void testCheckOutOfBoundsPath() {
        char[][] maze = Main.readMaze("examples/small.maz.txt");
        int[] entry = Main.findEntryPoint(maze);
        assertFalse(Main.validatePath(maze, entry, "LLFF"));
    }

    @Test
    public void testCheckWallCollision() {
        char[][] maze = Main.readMaze("examples/small.maz.txt");
        int[] entry = Main.findEntryPoint(maze);
        assertFalse(Main.validatePath(maze, entry, "LF"));
    }

    @Test
    public void testPathEndsNotAtExit() {
        char[][] maze = Main.readMaze("examples/straight.maz.txt");
        int[] entry = Main.findEntryPoint(maze);
        assertFalse(Main.validatePath(maze, entry, "FF"));
    }
}
