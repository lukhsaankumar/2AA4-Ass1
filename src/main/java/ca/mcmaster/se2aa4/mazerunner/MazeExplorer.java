package ca.mcmaster.se2aa4.mazerunner;

import java.util.ArrayList;
import java.util.List;

public class MazeExplorer {
    private final char[][] maze;
    private int currentRow;
    private int currentCol;
    private int direction; // 0: East, 1: South, 2: West, 3: North
    private final List<String> path; // Store path as instructions (F, R, L)

    public MazeExplorer(char[][] maze, int[] entryPoint) {
        this.maze = maze;
        this.currentRow = entryPoint[0];
        this.currentCol = entryPoint[1];
        this.direction = 0; // Assume we start facing East
        this.path = new ArrayList<>();
    }

    /**
     * Implements the right-hand rule to find a path to the exit.
     * @param exitPoint The exit point of the maze.
     * @return The canonical path as a string.
     */
    public String exploreMaze(int[] exitPoint) {
        StringBuilder canonicalPath = new StringBuilder();
        
        while (currentRow != exitPoint[0] || currentCol != exitPoint[1]) {
            if (canMoveRight()) {
                turnRight();
                canonicalPath.append("R");
                moveForward();
                canonicalPath.append("F");
            } else if (canMoveForward()) {
                moveForward();
                canonicalPath.append("F");
            } else {
                turnLeft();
                canonicalPath.append("L");
            }
        }
        return canonicalPath.toString();
    }

    private boolean canMoveForward() {
        int[] nextPos = getNextPosition();
        return isInBounds(nextPos[0], nextPos[1]) && maze[nextPos[0]][nextPos[1]] != '#';
    }

    private boolean canMoveRight() {
        int originalDirection = direction;
        turnRight();
        int[] nextPos = getNextPosition();
        turnLeft(); // Revert direction after checking
        direction = originalDirection;
        return isInBounds(nextPos[0], nextPos[1]) && maze[nextPos[0]][nextPos[1]] != '#';
    }

    private void turnRight() {
        direction = (direction + 1) % 4;
    }

    private void turnLeft() {
        direction = (direction + 3) % 4;
    }

    private void moveForward() {
        int[] nextPos = getNextPosition();
        currentRow = nextPos[0];
        currentCol = nextPos[1];
    }

    private int[] getNextPosition() {
        switch (direction) {
            case 0: return new int[]{currentRow, currentCol + 1}; // East
            case 1: return new int[]{currentRow + 1, currentCol}; // South
            case 2: return new int[]{currentRow, currentCol - 1}; // West
            case 3: return new int[]{currentRow - 1, currentCol}; // North
            default: throw new IllegalStateException("Invalid direction");
        }
    }

    private boolean isInBounds(int row, int col) {
        return row >= 0 && row < maze.length && col >= 0 && col < maze[0].length;
    }
}
