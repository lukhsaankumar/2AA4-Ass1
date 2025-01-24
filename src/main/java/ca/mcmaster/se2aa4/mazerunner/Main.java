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

        CommandLineParser parser = new DefaultParser();

        try {
            CommandLine cmd = parser.parse(options, args);
            if (cmd.hasOption("i")) {
                String inputFile = cmd.getOptionValue("i");
                logger.info("Reading the maze from file: " + inputFile);
                char[][] maze = readMaze(inputFile);

                // Print the parsed 2D array representation of the maze
                logger.info("Parsed Maze as 2D Array:");
                for (char[] row : maze) {
                    System.out.println(Arrays.toString(row));
                }

                // Determine and display entry and exit points
                int[] entryPoint = findEntryPoint(maze);
                int[] exitPoint = findExitPoint(maze);

                // Adjusting for 1-based indexing for user display
                logger.info("Entry Point: " + Arrays.toString(new int[]{entryPoint[0] + 1, entryPoint[1] + 1}));
                logger.info("Exit Point: " + Arrays.toString(new int[]{exitPoint[0] + 1, exitPoint[1] + 1}));

                // Compute and display the canonical path
                String canonicalPath = findCanonicalPath(maze, entryPoint, exitPoint);
                logger.info("Canonical Path: " + canonicalPath);
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
     *
     * @param filePath Path to the maze file.
     * @return 2D character array representing the maze.
     */
    private static char[][] readMaze(String filePath) {
        List<char[]> mazeRows = new ArrayList<>();
        int maxRowLength = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;

            // First pass: Determine the maximum row length
            while ((line = reader.readLine()) != null) {
                maxRowLength = Math.max(maxRowLength, line.length());
            }

            // Reset the reader to process the file again
            reader.close();
            BufferedReader secondReader = new BufferedReader(new FileReader(filePath));

            // Second pass: Read each line and preserve empty lines
            while ((line = secondReader.readLine()) != null) {
                if (line.isEmpty()) {
                    // Preserve empty rows as spaces
                    char[] emptyRow = new char[maxRowLength];
                    Arrays.fill(emptyRow, ' '); // Fill the row entirely with spaces
                    mazeRows.add(emptyRow);
                } else {
                    // Add the line as a character array
                    char[] row = line.toCharArray();
                    // If row is shorter than maxRowLength, pad it with spaces
                    if (row.length < maxRowLength) {
                        row = Arrays.copyOf(row, maxRowLength);
                    }
                    mazeRows.add(row);
                }
            }
        } catch (IOException e) {
            logger.error("Error reading maze file", e);
        }

        // Convert the list of rows to a 2D character array
        return mazeRows.toArray(new char[0][]);
    }

    /**
     * Finds the entry point of the maze on the leftmost column.
     *
     * @param maze 2D character array representing the maze.
     * @return Array [row, column] of the entry point.
     */
    private static int[] findEntryPoint(char[][] maze) {
        for (int row = 0; row < maze.length; row++) {
            if (maze[row][0] == ' ') { // Check leftmost column
                return new int[]{row, 0};
            }
        }
        throw new IllegalStateException("Maze must have a valid entry point.");
    }

    /**
     * Finds the exit point of the maze on the rightmost column.
     *
     * @param maze 2D character array representing the maze.
     * @return Array [row, column] of the exit point.
     */
    private static int[] findExitPoint(char[][] maze) {
        int lastColumn = maze[0].length - 1; // Rightmost column
        for (int row = 0; row < maze.length; row++) {
            if (maze[row][lastColumn] == ' ') { // Check rightmost column
                return new int[]{row, lastColumn};
            }
        }
        throw new IllegalStateException("Maze must have a valid exit point.");
    }

    /**
     * Finds the canonical path from the entry point to the exit point in a straight maze.
     *
     * @param maze       2D character array representing the maze.
     * @param entryPoint Array [row, column] of the entry point.
     * @param exitPoint  Array [row, column] of the exit point.
     * @return The canonical path as a string (e.g., "FFFF").
     */
    private static String findCanonicalPath(char[][] maze, int[] entryPoint, int[] exitPoint) {
        StringBuilder path = new StringBuilder();

        int currentRow = entryPoint[0];
        int currentCol = entryPoint[1];

        while (currentRow != exitPoint[0] || currentCol != exitPoint[1]) {
            if (currentCol < exitPoint[1]) {
                currentCol++; // Move right
                path.append("F");
            } else if (currentRow < exitPoint[0]) {
                currentRow++; // Move down
                path.append("F");
            } else if (currentRow > exitPoint[0]) {
                currentRow--; // Move up
                path.append("F");
            }
        }

        return path.toString();
    }
}
