package ca.mcmaster.se2aa4.mazerunner;

import org.apache.commons.cli.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        // Define command-line options
        Options options = new Options();
        options.addOption("i", "input", true, "Path to the input maze file");
        options.addOption("p", "path", true, "Path to validate against the maze");

        CommandLineParser parser = new DefaultParser();

        try {
            CommandLine cmd = parser.parse(options, args);
            if (cmd.hasOption("i")) {
                String inputFile = cmd.getOptionValue("i");
                char[][] maze = readMaze(inputFile);

                // Determine entry and exit points
                int[] entryPoint = findEntryPoint(maze);
                int[] exitPoint = findExitPoint(maze);

                // Explore maze using the right-hand rule
                MazeExplorer explorer = new MazeExplorer(maze, entryPoint);
                String canonicalPath = explorer.exploreMaze(exitPoint);

                // Factorize the path
                String factorizedPath = factorizePath(canonicalPath);
                System.out.println(factorizedPath);

                // Validate the provided path if the -p flag is used
                if (cmd.hasOption("p")) {
                    String inputPath = cmd.getOptionValue("p");
                    boolean isValid = validatePath(maze, entryPoint, inputPath);

                    if (isValid) {
                        System.out.println("VALID");
                    } else {
                        System.out.println("INVALID");
                    }
                }
            } else {
                System.err.println("No input file provided. Use -i to specify the maze file.");
            }
        } catch (ParseException e) {
            System.err.println("Failed to parse command-line arguments: " + e.getMessage());
        }
    }

    public static char[][] readMaze(String filePath) {
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
            System.err.println("Error reading maze file: " + e.getMessage());
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
                    System.err.println("Invalid move in path: " + move);
                    return false;
            }

            // Check bounds and walls
            if (row < 0 || row >= maze.length || col < 0 || col >= maze[0].length || maze[row][col] == '#') {
                return false;
            }
        }

        // Check if we have reached the exit point
        int[] exitPoint = findExitPoint(maze);
        return row == exitPoint[0] && col == exitPoint[1]; // Successfully reached the exit
    }
}
