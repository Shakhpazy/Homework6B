package app;

public class Spreadsheet implements SpreadsheetInterface{

    private int theNumberOfRows;
    private int theNumberOfColumns;
    private Cell[][] theCells;
    private SpreadsheetUI ui;
    
    public Spreadsheet(int theNumberOfRows, int theNumberOfColumns) {
        this.theNumberOfRows = theNumberOfRows;
        this.theNumberOfColumns = theNumberOfColumns;
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

    public void setUI(SpreadsheetUI ui) {
        this.ui = ui;
    }

    private void notifyUI() {
        if (ui != null) {
            ui.refreshTable();
        }
    }

    public void setCellFormula(int row, int col, String formula) {
        if (row >= 0 && row < theNumberOfRows && col >= 0 && col < theNumberOfColumns) {
            theCells[row][col].setFormula(formula);
            notifyUI();
        }
    }

    public void clear() {
        for (int i = 0; i < theNumberOfRows; i++) {
            for (int j = 0; j < theNumberOfColumns; j++) {
                theCells[i][j].clear();
            }
        }
        notifyUI();
    }

    public void save() {
        return;
    }

}
