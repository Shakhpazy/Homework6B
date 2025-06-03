package app;

import java.io.FileWriter;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Stack;
import java.util.Queue;

/**
 * Spreadsheet holds all the functionality for a spreadsheet and manages
 * the data and updates for all the cells.
 * 
 * @author Mark Malyshev
 * @author Yusuf Shakhpaz
 * @author Georgia Karwhite
 * @version June 2, 2025
 */
public class Spreadsheet implements SpreadsheetInterface {

    /**
     * The number of rows in the spreadsheet
     */
    private int theNumberOfRows;

    /**
     * The number of columns in the spreadsheet
     */ 
    private int theNumberOfColumns;

    /** 
     * The cells in the spreadsheet
     */
    private Cell[][] theCells;

    /**
     * The UI for the spreadsheet
     */
    private SpreadsheetUI ui;

    /**
     * A constructor that creates a new spreadsheet with the given dimensions
     * @param theNumberOfRows the height of the spreadsheet
     * @param theNumberOfColumns the width of the spreadsheet
     */
    public Spreadsheet(int theNumberOfRows, int theNumberOfColumns) {
        this.theNumberOfRows = theNumberOfRows;
        this.theNumberOfColumns = theNumberOfColumns;
        theCells = new Cell[theNumberOfRows][theNumberOfColumns];
        initializeCells();
    }

    /**
     * Itializes any cells if this spreadsheet contains pre-entered values/data
     */
    private void initializeCells() {
        for (int i = 0; i < theNumberOfRows; i++) {
            for (int j = 0; j < theNumberOfColumns; j++) {
                theCells[i][j] = new Cell();
                theCells[i][j].setFormula(""); // Explicitly set empty
                theCells[i][j].setValue(0);
            }
        }
    }

    /**
     * Gets the number of rows, or the height, of the spreadsheet
     * @return the number of rows
     */
    public int getNumberOfRows() {
        return theNumberOfRows;
    }

    /**
     * Gets the number of columns, or the width, of the spreadsheet
     * @return the number of columns
     */
    public int getNumberOfColumns() {
        return theNumberOfColumns;
    }

    /**
     * Returns the data as a 2D array of cells
     * @return a 2D array of the cells in this spreadsheet
     */
    public Cell[][] getCells() {
        return theCells;
    }

    /**
     * Gets the cell at the given location
     * @param row the row of the cell
     * @param col the column of the cell
     * @return the cell at the location
     */
    public Cell getCell(int row, int col) {
        if (row >= 0 && row < theNumberOfRows && col >= 0 && col < theNumberOfColumns) {
            return theCells[row][col];
        }
        return null;
    }

    /**
     * Gets the the formula for the cell at the given indices
     * @param row the row index of the cell
     * @param col the column index of the cell
     * @return a String representation fo the cell's formula
     */
    public String getCellFormula(int row, int col) {
        if (row >= 0 && row < theNumberOfRows && col >= 0 && col < theNumberOfColumns) {
            return theCells[row][col].getFormula();
        }
        return null;
    }

    /**
     * Replaces the current UI with a new UI
     * @param ui the new UI to pair the spreadsheet with
     */
    public void setUI(SpreadsheetUI ui) {
        this.ui = ui;
    }

    
    private void notifyUI() {
        if (ui != null) {
            ui.refreshTable();
        }
    }

    /**
     * Sets the cell formula at the given indices
     * @param row the row index of the cell
     * @param col the column index of the cell
     * @param input the new formula
     */
    public void setCellFormula(int row, int col, String input) {
        if (!validateCoordinates(row, col))
            return;

        Cell cell = theCells[row][col];
        cell.setFormula(input);

        try {
            // Try to parse as simple number first
            cell.setValue(Integer.parseInt(input));
            cell.setExpressionTree(null);
        } catch (NumberFormatException e) {
            // Try as expression
            Stack<Token> postfix = convertToPostfix(input);
            if (postfix == null || postfix.isEmpty()) {
                cell.setValue(0);
                cell.setFormula("ERROR: " + input);
            } else {
                ExpressionTree tree = new ExpressionTree();
                tree.buildTree(postfix);
                cell.setExpressionTree(tree);
                cell.evaluate(this); // Evaluate immediately
            }
        }

        evaluateAllCells();
        notifyUI();
    }

    private boolean validateCoordinates(int row, int col) {
        return row >= 0 && row < theNumberOfRows && col >= 0 && col < theNumberOfColumns;
    }

    private Stack<Token> convertToPostfix(String infix) {
        Stack<Token> output = new Stack<>();
        Stack<OperatorToken> operators = new Stack<>();
        int index = 0;

        while (index < infix.length()) {
            char ch = infix.charAt(index);

            // Skip whitespace
            if (Character.isWhitespace(ch)) {
                index++;
                continue;
            }

            if (OperatorToken.isOperator(ch)) {
                // Handle operators
                OperatorToken currentOp = new OperatorToken(ch);
                while (!operators.isEmpty()) {
                    OperatorToken top = operators.peek();
                    if (top.getOperatorToken() != OperatorToken.LeftParen && top.priority() >= currentOp.priority()) {
                        output.push(operators.pop());
                    } else {
                        break;
                    }
                }
                operators.push(currentOp);
                index++;
            } else if (ch == ')') {
                // Handle right parenthesis
                while (!operators.isEmpty()) {
                    OperatorToken op = operators.pop();
                    if (op.getOperatorToken() == OperatorToken.LeftParen) {
                        break;
                    }
                    output.push(op);
                }
                index++;
            } else if (Character.isDigit(ch)) {
                // Parse numbers
                int value = 0;
                while (index < infix.length() && Character.isDigit(infix.charAt(index))) {
                    value = value * 10 + (infix.charAt(index++) - '0');
                }
                output.push(new LiteralToken(value));
            } else if (Character.isUpperCase(ch)) {
                // Parse cell references
                CellToken.ParseResult result = CellToken.parseFrom(infix, index);
                if (result.token.getRow() != CellToken.BadCell) {
                    output.push(result.token);
                    index = result.nextIndex;
                } else {
                    output.clear();
                    return output; // Invalid cell reference
                }
            } else {
                output.clear();
                return output; // Invalid character
            }
        }

        // Add remaining operators
        while (!operators.isEmpty()) {
            output.push(operators.pop());
        }

        return output;
    }

    private ExpressionTree buildExpressionTree(Stack<Token> postfix) {
        ExpressionTree tree = new ExpressionTree();
        
        // Make a copy of the stack to avoid modifying the original
        Stack<Token> postfixCopy = new Stack<>();
        postfixCopy.addAll(postfix);
        
        tree.buildTree(postfixCopy);
        return tree;
    }

   

    private void evaluateAllCells() {
        // 1. Build dependency graph
        // 2. Perform topological sort
        Queue<Cell> aQ = topologicalSort();

        // 3. Evaluate cells in order
        while (aQ.size() > 0) {
            aQ.remove().evaluate(this);
        }
    }

    /**
     * Sets the value of the cell at the given indices
     * @param row the row index of the cell
     * @param col the column index of the cell
     * @param inputValue the new value
     */
    public void setCellValue(int row, int col, String inputValue) {
        if (row >= 0 && row < theNumberOfRows && col >= 0 && col < theNumberOfColumns) {
            int inputIntValue = Integer.parseInt(inputValue);
            theCells[row][col].setValue(inputIntValue);
            notifyUI();
        }
    }

    /**
     * Clears all the cells in the spreadsheet
     */
    public void clear() {
        for (int i = 0; i < theNumberOfRows; i++) {
            for (int j = 0; j < theNumberOfColumns; j++) {
                theCells[i][j].clear();
            }
        }
        notifyUI();
    }

    /**
     * Saves the current spreadsheet data to spreadsheet.txt
     */
    public void save() {
        try {
            FileWriter writer = new FileWriter("spreadsheet.txt");
            writer.write(theNumberOfRows + "," + theNumberOfColumns + "\n");
        
            for (int i = 0; i < theNumberOfRows; i++) {
                for (int j = 0; j < theNumberOfColumns; j++) {
                    Cell cell = theCells[i][j];
                    String formula = cell.getFormula();
                    int value = cell.getValue();
                    writer.write(i + "," + j + "," + formula + "," + value + "\n");
                }
            }
            writer.close();
            System.out.println("Spreadsheet saved to spreadsheet.txt");
        } catch (java.io.IOException e) {
            System.out.println("Error saving spreadsheet: " + e.getMessage());
        }
    }


    private LinkedList<LinkedList<Cell>> makeGraph() {
        LinkedList<LinkedList<Cell>> graph = new LinkedList<LinkedList<Cell>>();
        for (int r = 0; r < theNumberOfRows; r++) {
            for (int c = 0; c < theNumberOfColumns; c++) {
                LinkedList<Cell> edges = new LinkedList<Cell>();
                edges.add(getCell(r, c));
                //for all of the cell tokens in the expression tree, add edges
                for (Cell cell : getCell(r, c).getCellDependencies()) {
                    edges.add(cell);
                }
            }
        }
        return graph;
    }

    private LinkedList<CellNums> calcIndegree(LinkedList<LinkedList<Cell>> graph) {
        LinkedList<CellNums> topNumbers = new LinkedList<CellNums>();
        for (LinkedList<Cell> deps : graph) {
            CellNums nums = new CellNums(deps.remove());
            nums.myNumber = deps.size();
        }
        return topNumbers;
    }

    private Queue<Cell> topologicalSort() {
        //add item to queue when its indegree is zero
        Queue<Cell> theQ = new LinkedList<Cell>();
        LinkedList<LinkedList<Cell>> graph = makeGraph();
        LinkedList<CellNums> indegrees = calcIndegree(graph);
        
        for (int i = 0; i < graph.size(); i ++) {
            Cell current = findNewIndegreeZero(indegrees);
            //adjust counts for adjacent cells
            for (LinkedList<Cell> dependencies : graph) {
                Iterator<Cell> walker = dependencies.iterator();
                Cell node = walker.next(); //first one is the "parent" cell
                while (walker.hasNext()) {
                    Cell c = walker.next();
                    if (c.equals(current)) {
                        indegrees.get(indegrees.indexOf(c)).myNumber = indegrees.get(indegrees.indexOf(c)).myNumber - 1;
                    }
                }
            }
            theQ.add(current);
        }
        return theQ;
    }

    private Cell findNewIndegreeZero(LinkedList<CellNums> theIndegrees) {
        boolean okay = true;
        Iterator<CellNums> walker = theIndegrees.iterator();
        Cell c = null;
        CellNums current = null;
        while (c == null && walker.hasNext()) {
            current = walker.next();
            if (current.myNumber == 0) {
                c = current.myCell;
                theIndegrees.remove(current);
            }
        }
        return c;
    }

    private class CellNums {
        public final Cell myCell;
        public int myNumber;

        public CellNums (Cell theCell) {
            myCell = theCell;
            myNumber = 0;
        }

        @Override
        public boolean equals(Object o) {
            return myCell.equals(((CellNums) o).myCell);
        }
    }
}
