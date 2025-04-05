package ca.mcmaster.se2aa4.mazerunner.Cells;

public class Wall extends Cell {
    @Override
    public boolean isPassable() { return false; }

    @Override
    public String toString() { return "#"; }
}