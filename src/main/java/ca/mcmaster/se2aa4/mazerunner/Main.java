package ca.mcmaster.se2aa4.mazerunner;
import org.apache.commons.cli.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.BufferedReader;
import java.io.FileReader;

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
                readMaze(inputFile);
            } else {
                logger.error("No input file provided. Use -i to specify the maze file.");
            }
        } catch (ParseException e) {
            logger.error("Failed to parse command-line arguments", e);
        } logger.info("** End of MazeRunner");
    }

    private static void readMaze(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                for (int idx = 0; idx < line.length(); idx++) {
                    if (line.charAt(idx) == '#') {
                        System.out.print("WALL ");
                    } else if (line.charAt(idx) == ' ') {
                        System.out.print("PASS ");
                    }
                } System.out.println();
            }
        } catch (Exception e) {
            logger.error("Error reading maze file", e);
        }
    }
}
