package ca.mcmaster.se2aa4.mazerunner;
import org.apache.commons.cli.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class Main {
    private static final Logger logger = LogManager.getLogger();
    public static void main(String[] args) {
        logger.info("** Starting Maze Runner"); // Define command-line options
        Options options = new Options();
        options.addOption("i", "input", true, "Path to the input maze file");
        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options, args);
            if (cmd.hasOption("i")) {
                String inputFile = cmd.getOptionValue("i");
                logger.info("Reading the maze from file: " + inputFile);
                char[][] maze = readMaze(inputFile); 
                // Print the parsed maze as a 2D Array
                logger.info("Parsed Maze as 2D Array");
                for (char[] row : maze) {
                    System.out.println(Arrays.toString(row));
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
    
    
}
