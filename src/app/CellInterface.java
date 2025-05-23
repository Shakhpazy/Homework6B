package app;

public interface CellInterface {
    /**
     * Gets the formula stored in this cell
     * @return the formula as a String
     */
    public String getFormula();

    /**
     * Gets the current value of this cell
     * @return the numeric value of the cell
     */
    public int getValue();

    /**
     * Sets the numeric value of this cell
     * @param value the new value to set
     */
    public void setValue(int value);

    /**
     * Sets the formula for this cell
     * @param formula the new formula to set
     */
    public void setFormula(String formula);

    /**
     * Clears the cell by resetting both formula and value
     */
    public void clear();
}
