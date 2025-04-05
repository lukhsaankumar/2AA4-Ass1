package ca.mcmaster.se2aa4.mazerunner.Factory;

import ca.mcmaster.se2aa4.mazerunner.Cells.Open;
import ca.mcmaster.se2aa4.mazerunner.Cells.Cell;
import ca.mcmaster.se2aa4.mazerunner.Cells.Wall;

public class CellFactory {
    public static Cell createCell(char symbol){
        if(symbol == '#'){
            return new Wall();
        } else {
            return new Open();
        }
    }
}