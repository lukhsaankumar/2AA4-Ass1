package ca.mcmaster.se2aa4.mazerunner;

import org.apache.commons.cli.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    private static final Logger logger = LogManager.getLogger();

    public static void main(String[] args) {
        logger.info("** Starting Maze Runner");

        // Define command-line options
        Options options = new Options();
        options.addOption("i", "input", true, "Path to the input maze file");
        options.addOption("p", "path", true, "Path to validate against the maze");

        CommandLineParser parser = new DefaultParser();

        try {
            CommandLine cmd = parser.parse(options, args);
            if (cmd.hasOption("i")) {
                String inputFile = cmd.getOptionValue("i");
                logger.info("Reading the maze from file: " + inputFile);
                char[][] maze = readMaze(inputFile);

                // Display the maze
                logger.info("Parsed Maze as 2D Array:");
                for (char[] row : maze) {
                    System.out.println(Arrays.toString(row));
                }

                // Determine entry and exit points
                int[] entryPoint = findEntryPoint(maze);
                int[] exitPoint = findExitPoint(maze);

                logger.info("Entry Point: " + Arrays.toString(new int[]{entryPoint[0] + 1, entryPoint[1] + 1}));
                logger.info("Exit Point: " + Arrays.toString(new int[]{exitPoint[0] + 1, exitPoint[1] + 1}));

                // Explore maze using the right-hand rule
                MazeExplorer explorer = new MazeExplorer(maze, entryPoint);
                String canonicalPath = explorer.exploreMaze(exitPoint);
                logger.info("Canonical Path: " + canonicalPath);

                // Factorize the path
                String factorizedPath = factorizePath(canonicalPath);
                logger.info("Factorized Path: " + factorizedPath);

                // Validate the provided path if the -p flag is used
                if (cmd.hasOption("p")) {
                    String inputPath = cmd.getOptionValue("p");
                    logger.info("Validating path: " + inputPath);
                    boolean isValid = validatePath(maze, entryPoint, inputPath);

                    if (isValid) {
                        logger.info("The path is VALID.");
                    } else {
                        logger.error("The path is INVALID.");
                    }
                }

            } else {
                logger.error("No input file provided. Use -i to specify the maze file.");
            }
        } catch (ParseException e) {
            logger.error("Failed to parse command-line arguments", e);
        }

        logger.info("** End of MazeRunner");
    }

    private static char[][] readMaze(String filePath) {
        List<char[]> mazeRows = new ArrayList<>();
        int maxRowLength = 0;
    
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
    
            // First pass: Determine the maximum row length
            while ((line = reader.readLine()) != null) {
                maxRowLength = Math.max(maxRowLength, line.length());
            }
    
            reader.close(); // Reset the reader to process the file again
            BufferedReader secondReader = new BufferedReader(new FileReader(filePath));
    
            // Second pass: Read each line and preserve correct spacing
            while ((line = secondReader.readLine()) != null) {
                if (line.isEmpty()) {
                    // Handle empty rows correctly
                    char[] emptyRow = new char[maxRowLength];
                    Arrays.fill(emptyRow, ' '); // Fill empty rows with spaces
                    mazeRows.add(emptyRow);
                } else {
                    char[] row = line.toCharArray();
                    if (row.length < maxRowLength) {
                        row = Arrays.copyOf(row, maxRowLength); 
                        Arrays.fill(row, line.length(), maxRowLength, ' ');  // Fill missing cells with spaces
                    }
                    mazeRows.add(row);
                }
            }
        } catch (IOException e) {
            logger.error("Error reading maze file", e);
        }
    
        return mazeRows.toArray(new char[0][]);
    }        
    
    
    public static int[] findEntryPoint(char[][] maze) {
        for (int row = 0; row < maze.length; row++) {
            if (maze[row][0] == ' ') {
                return new int[]{row, 0};
            }
        }
        throw new IllegalStateException("Maze must have a valid entry point.");
    }

    public static int[] findExitPoint(char[][] maze) {
        int lastColumn = maze[0].length - 1; // Rightmost column
        int lastRow = maze.length - 1; // Bottom row
    
        // Check the rightmost column for an exit
        for (int row = 0; row < maze.length; row++) {
            if (maze[row][lastColumn] == ' ') {
                return new int[]{row, lastColumn};
            }
        }
    
        // Check the bottom row for an exit
        for (int col = 0; col <= lastColumn; col++) {
            if (maze[lastRow][col] == ' ') {
                return new int[]{lastRow, col};
            }
        }
    
        // Optionally, you can add checks for top row or leftmost column if needed
    
        throw new IllegalStateException("Maze must have a valid exit point.");
    }
    
    

    public static String factorizePath(String canonicalPath) {
        if (canonicalPath.isEmpty()) {
            return "";
        }

        StringBuilder factorizedPath = new StringBuilder();
        char currentMove = canonicalPath.charAt(0);
        int count = 0;

        for (char move : canonicalPath.toCharArray()) {
            if (move == currentMove) {
                count++;
            } else {
                factorizedPath.append(count).append(currentMove).append(" ");
                currentMove = move;
                count = 1;
            }
        }
        factorizedPath.append(count).append(currentMove);
        return factorizedPath.toString().trim();
    }

    public static boolean validatePath(char[][] maze, int[] entryPoint, String path) {
        int row = entryPoint[0];
        int col = entryPoint[1];
        int direction = 0; // 0: East, 1: South, 2: West, 3: North
    
        for (char move : path.toCharArray()) {
            switch (move) {
                case 'F':
                    switch (direction) {
                        case 0 -> col++;  // East
                        case 1 -> row++;  // South
                        case 2 -> col--;  // West
                        case 3 -> row--;  // North
                    }
                    break;
                case 'R':
                    direction = (direction + 1) % 4;
                    break;
                case 'L':
                    direction = (direction + 3) % 4;
                    break;
                default:
                    logger.error("Invalid move in path: " + move);
                    return false;
            }
    
            // Check bounds and walls
            if (row < 0 || row >= maze.length || col < 0 || col >= maze[0].length || maze[row][col] == '#') {
                return false;
            }
        }
    
        // Check if we have reached the exit point
        int[] exitPoint = findExitPoint(maze);
        if (row == exitPoint[0] && col == exitPoint[1]) {
            return true; // Successfully reached the exit
        } else {
            return false; // Didn't reach the exit
        }
    }
    
}
