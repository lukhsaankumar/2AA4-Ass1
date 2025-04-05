package ca.mcmaster.se2aa4.mazerunner.Cells;

public class Open extends Cell {
    @Override
    public boolean isPassable() { return true; }

    @Override
    public String toString() { return " "; }
}