package ca.mcmaster.se2aa4.mazerunner;

import ca.mcmaster.se2aa4.mazerunner.Path.PathChecker;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CheckPathTest {

    @Test
    public void testCheckValidPath() {
        Maze maze = new Maze("examples/straight.maz.txt");
        PathChecker checker = new PathChecker(maze);
        assertTrue(checker.checkPath("4F"));
    }

    @Test
    public void testCheckInvalidPath() {
        Maze maze = new Maze("examples/small.maz.txt");
        PathChecker checker = new PathChecker(maze);
        assertFalse(checker.checkPath("5F"));
    }

    @Test
    public void testCheckOutOfBoundsPath() {
        Maze maze = new Maze("examples/small.maz.txt");
        PathChecker checker = new PathChecker(maze);
        assertFalse(checker.checkPath("2L2F"));
    }

    @Test
    public void testCheckWallCollision() {
        Maze maze = new Maze("examples/small.maz.txt");
        PathChecker checker = new PathChecker(maze);
        assertFalse(checker.checkPath("LF"));
    }

    @Test
    public void testPathEndsNotAtExit() {
        Maze maze = new Maze("examples/straight.maz.txt");
        PathChecker checker = new PathChecker(maze);
        assertFalse(checker.checkPath("2F"));
    }
}
