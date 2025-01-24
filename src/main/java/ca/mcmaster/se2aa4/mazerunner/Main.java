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
        logger.info("** Starting Maze Runner"); // Define command-line options
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
                logger.info("Parsed Maze as 2D Array:"); 
                
                // Print the parsed 2D array representation of the maze
                for (char[] row : maze) { System.out.println(Arrays.toString(row)); }
                
                // Determine and display entry and exit points
                int[] entryPoint = findEntryPoint(maze);
                int[] exitPoint = findExitPoint(maze);

                // Adjusting for 1-based indexing for user display
                logger.info("Entry Point: " + Arrays.toString(new int[]{entryPoint[0] + 1, entryPoint[1] + 1}));
                logger.info("Exit Point: " + Arrays.toString(new int[]{exitPoint[0] + 1, exitPoint[1] + 1}));

                // Compute the canonical path
                String canonicalPath = findCanonicalPath(maze, entryPoint, exitPoint);
                logger.info("Canonical Path: " + canonicalPath);

                // Compute the factorized path
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

    /**
     * Reads a maze from a file and parses it into a 2D character array.
     */
    private static char[][] readMaze(String filePath) {
        List<char[]> mazeRows = new ArrayList<>();
        int maxRowLength = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = reader.readLine()) != null) {
                maxRowLength = Math.max(maxRowLength, line.length());
            }

            reader.close();
            BufferedReader secondReader = new BufferedReader(new FileReader(filePath));

            while ((line = secondReader.readLine()) != null) {
                if (line.isEmpty()) {
                    char[] emptyRow = new char[maxRowLength];
                    Arrays.fill(emptyRow, ' ');
                    mazeRows.add(emptyRow);
                } else {
                    char[] row = line.toCharArray();
                    if (row.length < maxRowLength) {
                        row = Arrays.copyOf(row, maxRowLength);
                    }
                    mazeRows.add(row);
                }
            }
        } catch (IOException e) {
            logger.error("Error reading maze file", e);
        }

        return mazeRows.toArray(new char[0][]);
    }

    /**
     * Finds the entry point of the maze on the leftmost column.
     */
    private static int[] findEntryPoint(char[][] maze) {
        for (int row = 0; row < maze.length; row++) {
            if (maze[row][0] == ' ') {
                return new int[]{row, 0};
            }
        }
        throw new IllegalStateException("Maze must have a valid entry point.");
    }

    /**
     * Finds the exit point of the maze on the rightmost column.
     */
    private static int[] findExitPoint(char[][] maze) {
        int lastColumn = maze[0].length - 1;
        for (int row = 0; row < maze.length; row++) {
            if (maze[row][lastColumn] == ' ') {
                return new int[]{row, lastColumn};
            }
        }
        throw new IllegalStateException("Maze must have a valid exit point.");
    }

    /**
     * Finds the canonical path from the entry point to the exit point in a straight maze.
     */
    private static String findCanonicalPath(char[][] maze, int[] entryPoint, int[] exitPoint) {
        StringBuilder path = new StringBuilder();

        int currentRow = entryPoint[0];
        int currentCol = entryPoint[1];

        while (currentRow != exitPoint[0] || currentCol != exitPoint[1]) {
            if (currentCol < exitPoint[1]) {
                currentCol++;
                path.append("F");
            } else if (currentRow < exitPoint[0]) {
                currentRow++;
                path.append("F");
            } else if (currentRow > exitPoint[0]) {
                currentRow--;
                path.append("F");
            }
        }

        return path.toString();
    }

    /**
     * Converts a canonical path into its factorized form.
     */
    private static String factorizePath(String canonicalPath) {
        if (canonicalPath.isEmpty()) {
            return "";
        }

        StringBuilder factorizedPath = new StringBuilder();
        char currentChar = canonicalPath.charAt(0);
        int count = 0;

        for (char c : canonicalPath.toCharArray()) {
            if (c == currentChar) {
                count++;
            } else {
                factorizedPath.append(count).append(currentChar).append(" ");
                currentChar = c;
                count = 1;
            }
        }

        factorizedPath.append(count).append(currentChar);

        return factorizedPath.toString().trim();
    }

    /**
     * Validates a path against the maze.
     */
    private static boolean validatePath(char[][] maze, int[] entryPoint, String path) {
        int row = entryPoint[0];
        int col = entryPoint[1];
        int direction = 0; // 0: East, 1: South, 2: West, 3: North

        for (char move : path.toCharArray()) {
            switch (move) {
                case 'F': // Move forward
                    switch (direction) {
                        case 0 -> col++;
                        case 1 -> row++;
                        case 2 -> col--;
                        case 3 -> row--;
                    }
                    break;
                case 'R': // Turn right
                    direction = (direction + 1) % 4;
                    break;
                case 'L': // Turn left
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

        return true;
    }
}
