package app;

public interface SpreadsheetInterface {

    /**
     * Gets the total number of rows in the spreadsheet
     * @return the number of rows
     */
    public int getNumberOfRows();

    /**
     * Gets the total number of columns in the spreadsheet
     * @return the number of columns
     */
    public int getNumberOfColumns();

    /**
     * Gets the entire grid of cells in the spreadsheet
     * @return a 2D array of Cell objects
     */
    public Cell[][] getCells();

    /**
     * Gets a specific cell at the given row and column
     * @param row the row index (0-based)
     * @param col the column index (0-based)
     * @return the Cell at the specified position
     */
    public Cell getCell(int row, int col);

    /**
     * Gets the formula stored in a specific cell
     * @param row the row index (0-based)
     * @param col the column index (0-based)
     * @return the formula as a String
     */
    public String getCellFormula(int row, int col);

    /**
     * Sets the UI component for this spreadsheet
     * @param ui the SpreadsheetUI instance to be notified of changes
     */
    public void setUI(SpreadsheetUI ui);

    /**
     * Sets the formula for a specific cell
     * @param row the row index (0-based)
     * @param col the column index (0-based)
     * @param formula the new formula to set
     */
    public void setCellFormula(int row, int col, String formula);
    
    // Clear the spreadsheet
    public void clear();

    // Save the spreadsheet
    public void save();

}
