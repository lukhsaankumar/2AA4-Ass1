package ca.mcmaster.se2aa4.mazerunner;

import java.io.*;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import ca.mcmaster.se2aa4.mazerunner.Cells.Cell;
import ca.mcmaster.se2aa4.mazerunner.Factory.CellFactory;

public class Maze {
    private Cell[][] grid;
    private Point start;
    private Point goal;

    public Maze(String filePath) {
        load(filePath);
    }

    public void load(String filePath) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            List<String> content = new ArrayList<>();
            String line;
            int width = 0;

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty() && width > 0) line = " ".repeat(width);
                width = Math.max(width, line.length());
                content.add(line);
            }
            reader.close();

            if (content.isEmpty()) throw new IllegalStateException("Empty maze file");

            int height = content.size();
            grid = new Cell[height][width];

            for (int r = 0; r < height; r++) {
                String curr = content.get(r);
                if (curr.length() < width) curr += " ".repeat(width - curr.length());
                for (int c = 0; c < width; c++) {
                    grid[r][c] = CellFactory.createCell(curr.charAt(c));
                }
            }

            locateEndpoints(content);
        } catch (IOException e) {
            throw new IllegalStateException("Maze read error: " + e.getMessage());
        }
    }

    public void print() {
        for (Cell[] row : grid) {
            for (Cell cell : row) System.out.print(cell);
            System.out.println();
        }
    }

    public Point getEntry() {
        return start;
    }

    public Point getExit() {
        return goal;
    }

    private void locateEndpoints(List<String> lines) {
        int h = lines.size();
        int w = lines.get(0).length();
        start = null;
        goal = null;

        for (int i = 0; i < h; i++) {
            if (lines.get(i).charAt(0) == ' ' || lines.get(i).charAt(0) == 'E') {
                start = new Point(1, i + 1);
                break;
            }
        }

        for (int i = 0; i < h; i++) {
            if (lines.get(i).charAt(w - 1) == ' ' || lines.get(i).charAt(w - 1) == 'X') {
                goal = new Point(w, i + 1);
                break;
            }
        }

        if (start == null || goal == null) {
            throw new IllegalStateException("Missing entry/exit in maze");
        }
    }

    public boolean isPassage(Point pos) {
        int y = pos.y - 1;
        int x = pos.x - 1;
        return y >= 0 && y < grid.length && x >= 0 && x < grid[0].length && grid[y][x].isPassable();
    }

    public int getHeight() {
        return grid.length;
    }

    public int getWidth() {
        return grid[0].length;
    }
}
