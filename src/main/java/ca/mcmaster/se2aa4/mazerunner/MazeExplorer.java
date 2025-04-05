package ca.mcmaster.se2aa4.mazerunner;

import java.awt.Point;
import ca.mcmaster.se2aa4.mazerunner.Path.Path;

public class MazeExplorer {
    private Point location;
    private char facing;
    private Path route;

    public MazeExplorer(Point start, char direction) {
        this.location = start;
        this.facing = direction;
        this.route = new Path();
    }

    public void moveForward() {
        if (facing == 'N') location.y--;
        else if (facing == 'E') location.x++;
        else if (facing == 'S') location.y++;
        else location.x--;
        route.addStep("F");
    }

    public void turnLeft() {
        if (facing == 'N') facing = 'W';
        else if (facing == 'W') facing = 'S';
        else if (facing == 'S') facing = 'E';
        else facing = 'N';
        route.addStep("L");
    }

    public void turnRight() {
        if (facing == 'N') facing = 'E';
        else if (facing == 'E') facing = 'S';
        else if (facing == 'S') facing = 'W';
        else facing = 'N';
        route.addStep("R");
    }

    public Point getPosition() {return location; } 

    public char getDirection() { return facing; }

    public Path getPath() { return route;}
}
