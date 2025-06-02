package app;

import java.util.Stack;

public class Spreadsheet implements SpreadsheetInterface {

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

    // mainly used for if the spreadsheet contains pre-entered values/data
    private void initializeCells() {
        for (int i = 0; i < theNumberOfRows; i++) {
            for (int j = 0; j < theNumberOfColumns; j++) {
                theCells[i][j] = new Cell();
                theCells[i][j].setFormula(""); // Explicitly set empty
                theCells[i][j].setValue(0);
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
        // 3. Evaluate cells in order
    }

    public void setCellValue(int row, int col, String inputValue) {
        if (row >= 0 && row < theNumberOfRows && col >= 0 && col < theNumberOfColumns) {
            int inputIntValue = Integer.parseInt(inputValue);
            theCells[row][col].setValue(inputIntValue);
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
