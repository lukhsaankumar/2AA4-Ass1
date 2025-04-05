package ca.mcmaster.se2aa4.mazerunner.Path;

import ca.mcmaster.se2aa4.mazerunner.Maze;
import ca.mcmaster.se2aa4.mazerunner.MazeExplorer;
import ca.mcmaster.se2aa4.mazerunner.Commands.MovementCommand;
import ca.mcmaster.se2aa4.mazerunner.Factory.CommandFactory;

public class PathChecker {
    private final CommandFactory factory;
    private final MazeExplorer walker;
    private final Maze grid;

    public PathChecker(Maze grid) {
        this.grid = grid;
        this.walker = new MazeExplorer(grid.getEntry(), 'E');
        this.factory = new CommandFactory(walker, grid);
    }

    public boolean checkPath(String route) {
        String decoded = expandPath(route);
        for (char step : decoded.toCharArray()) {
            MovementCommand cmd = factory.getCommand(step);
            if (cmd == null || !cmd.execute()) return false;
        }
        return walker.getPosition().equals(grid.getExit());
    }

    private String expandPath(String route) {
        StringBuilder result = new StringBuilder();
        String[] segments = route.trim().split("\\s+");
        for (String seg : segments) {
            int i = 0;
            while (i < seg.length() && Character.isDigit(seg.charAt(i))) i++;
            int repeat = (i == 0) ? 1 : Integer.parseInt(seg.substring(0, i));
            char move = seg.charAt(i);
            result.append(String.valueOf(move).repeat(repeat));
        }
        return result.toString();
    }
}
