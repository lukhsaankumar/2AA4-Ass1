package ca.mcmaster.se2aa4.mazerunner.Path;

import java.util.ArrayList;
import java.util.List;

public class Path {
    private List<String> path;

    public Path() { this.path = new ArrayList<>(); }

    public void addStep(String step) { path.add(step); }

    public List<String> getPath() { return path; }

    public String getFactorizedPath() {
        if (path.isEmpty()) return "";
        StringBuilder compressed = new StringBuilder();
        String move = path.get(0);
        int streak = 1;
        for (int i = 1; i < path.size(); i++) {
            if (path.get(i).equals(move)) {
                streak++;
            } else {
                if (streak > 1) compressed.append(streak);
                compressed.append(move).append(" ");
                move = path.get(i);
                streak = 1;
            }
        }
        if (streak > 1) compressed.append(streak);
        compressed.append(move);
        return compressed.toString().trim();
    }    
}