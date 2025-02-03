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

            } else {
                logger.error("No input file provided. Use -i to specify the maze file.");
            }
        } catch (ParseException e) {
            logger.error("Failed to parse command-line arguments", e);
        }

        logger.info("** End of Maze Runner");
    }

    public static char[][] readMaze(String filePath) {
        List<char[]> mazeRows = new ArrayList<>();
    
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) { // Skip empty lines
                    mazeRows.add(line.toCharArray());
                }
            }
        } catch (IOException e) {
            logger.error("Error reading maze file: " + e.getMessage());
        }
    
        if (mazeRows.isEmpty()) {
            throw new IllegalStateException("The maze is empty or could not be read.");
        }
    
        return mazeRows.toArray(new char[0][]);
    }

    public static int[] findEntryPoint(char[][] maze) {
        for (int row = 0; row < maze.length; row++) {
            if (maze[row][0] == ' ') { // Check the leftmost column
                return new int[]{row, 0};
            }
        }
        throw new IllegalStateException("Maze must have a valid entry point.");
    }
    

    public static int[] findExitPoint(char[][] maze) {
        int lastColumn = maze[0].length - 1; // Check rightmost column
        for (int row = 0; row < maze.length; row++) {
            if (maze[row][lastColumn] == ' ') {
                return new int[]{row, lastColumn};
            }
        }
        throw new IllegalStateException("Maze must have a valid exit point.");
    }
    
    public static String factorizePath(String canonicalPath) {
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
}
