package app;

public class Spreadsheet {

    private int theNumberOfRows;
    private int theNumberOfColumns;
    private Cell[][] theCells;
    
    public Spreadsheet() {
        theNumberOfRows = 20;
        theNumberOfColumns = 20;
        theCells = new Cell[theNumberOfRows][theNumberOfColumns];
        initializeCells();
    }

    //mainly used for if the spreadsheet contains pre-entered values/data
    private void initializeCells() {
        for (int i = 0; i < theNumberOfRows; i++) {
            for (int j = 0; j < theNumberOfColumns; j++) {
                theCells[i][j] = new Cell();
            }
        }
    }

    public int getNumberOfRows() {
        return theNumberOfRows;
    }

    public int getNumberOfColumns() {
        return theNumberOfColumns;
    }

    public Cell[][] getCells() {
        return theCells;
    }

    public Cell getCell(int row, int col) {
        if (row >= 0 && row < theNumberOfRows && col >= 0 && col < theNumberOfColumns) {
            return theCells[row][col];
        }
        return null;
    }

    public String getCellFormula(int row, int col) {
        if (row >= 0 && row < theNumberOfRows && col >= 0 && col < theNumberOfColumns) {
            return theCells[row][col].getFormula();
        }
        return null;
    }

    public void setCellFormula(int row, int col, String formula) {
        if (row >= 0 && row < theNumberOfRows && col >= 0 && col < theNumberOfColumns) {
            theCells[row][col].setFormula(formula);
        }
    }

}
