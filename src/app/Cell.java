package app;

import java.util.LinkedList;

/**
 * The Cell class is used in the spreadsheet as the main hub of information and
 * functionality for individual cells.
 * 
 * @author Mark Malyshev
 * @author Yusuf Shakhpaz
 * @author Georgia Karwhite
 * @version June 2, 2025
 */
public class Cell {
    /**
     * The formula of the cell
     */
    private String formula;

    /**
     * The value of the cell
     */
    private int value;

    /**
     * The expression tree of the cell
     */
    private ExpressionTree expressionTree; // Add this field

    /**
     * Constructor for a spreadsheet cell, with default values
     */
    public Cell() {
        formula = "";
        value = 0;
        expressionTree = null;

    }

    /**
     * Getter for the formula in the cell
     * @return the formula stored in the cell
     */
    public String getFormula() {
        return formula;
    }

    /**
     * The value stored in the cell after the formula has been evaluated
     * @return the value of the cell
     */
    public int getValue() {
        return value;
    }

    /**
     * Gets the expression tree of the cell
     * @return
     */
    public ExpressionTree getExpressionTree() {
        return expressionTree;
    }

    /**
     * Sets the value of the cell
     * @param value the new int value for the cell to hold
     */
    public void setValue(int value) {

        this.value = value;
    }

    /**
     * Sets the formula for the cell
     * @param formula the new String formula for the cell
     */
    public void setFormula(String formula) {
        this.formula = formula;
    }

    /**
     * Sets the expression tree for the cell
     * @param tree an expression tree to manage the order of operations in the cell
     */
    public void setExpressionTree(ExpressionTree tree) {
        this.expressionTree = tree;
    }

    /**
     * Evaluates the expression in the cell
     * @param spreadsheet the spreadsheet in which this cell belongs
     * @return int value of the cell
     */
    public int evaluate(Spreadsheet spreadsheet) {
        if (expressionTree != null) {
            this.value = expressionTree.evaluate(spreadsheet);
            System.out.println("Evaluated formula '" + formula + "' = " + value);
        }
        return this.value;
    }

    /**
     * Clears the contents of the cell
     */
    public void clear() {
        formula = "";
        value = 0;
    }

    /**
     * Gets a list of the cells used in the calculation of this cell's value
     * @return a list of the depencies of this cell's value
     */
    public LinkedList<Cell> getCellDependencies() {
        if (expressionTree == null) {
            return new LinkedList<Cell>();
        } else {
            return expressionTree.getCellDependencies();
        }
    }
}
