package ca.mcmaster.se2aa4.mazerunner;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import ca.mcmaster.se2aa4.mazerunner.Path.Path;

public class MainTest {

    @Test
    public void testFactorizeCustomPattern() {
        Path path = new Path();
        path.addStep("F");
        path.addStep("R");
        path.addStep("R");
        path.addStep("R");
        path.addStep("L");
        path.addStep("F");
        path.addStep("F");
        assertEquals("F 3R L 2F", path.getFactorizedPath());
    }

    @Test
    public void testFactorizeSingleMove() {
        Path path = new Path();
        path.addStep("R");
        assertEquals("R", path.getFactorizedPath());
    }

    @Test
    public void testEmptyPath() {
        Path path = new Path();
        assertEquals("", path.getFactorizedPath());
    }

}
