package ca.mcmaster.se2aa4.mazerunner;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class MainTest {

    @Test
    public void testFactorizeSimplePath() {
        String path = "FFFRFF";
        String expected = "3F 1R 2F";
        assertEquals(expected, Main.factorizePath(path));
    }

    @Test
    public void testFactorizeSingleMove() {
        assertEquals("1F", Main.factorizePath("F"));
    }

    @Test
    public void testFactorizeEmptyPath() {
        assertEquals("", Main.factorizePath(""));
    }
}
