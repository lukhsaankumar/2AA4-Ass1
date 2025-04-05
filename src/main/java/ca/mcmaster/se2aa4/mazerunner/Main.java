package ca.mcmaster.se2aa4.mazerunner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ca.mcmaster.se2aa4.mazerunner.Factory.MazeSolverFactory;
import ca.mcmaster.se2aa4.mazerunner.Solver.MazeSolver;
import org.apache.commons.cli.*;

public class Main {
    private static final Logger log = LogManager.getLogger();

    public static void main(String[] args) {
        log.info("Maze Runner initialized.");

        Options opts = new Options();
        opts.addOption("i", true, "Input file path");
        opts.addOption("p", true, "Path to check");

        try {
            CommandLine cli = new DefaultParser().parse(opts, args);

            if (!cli.hasOption("i")) {
                log.error("Input file not specified. Use -i <path>");
                System.exit(1);
            }

            String file = cli.getOptionValue("i");
            String route = cli.getOptionValue("p");

            log.info("Loading maze from: {}", file);
            Maze maze = new Maze(file);
            log.info("Maze loaded successfully.");

            MazeSolver solver = MazeSolverFactory.createTool(maze, route);
            log.info("Maze solver initialized. Beginning execution...");
            solver.solve();
        } catch (Exception err) {
            log.error("Unexpected error occurred:", err);
        }

        log.info("Maze Runner finished.");
    }
}
