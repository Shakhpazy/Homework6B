package app;

import java.util.LinkedList;
import java.util.Stack;

/**
 * ExpressionTree is designed to manage the order of operations, and
 * evaluates the values.
 * 
 * @author Mark Malyshev
 * @author Yusuf Shakhpaz
 * @author Georgia Karwhite
 * @version June 2, 2025
 */
public class ExpressionTree {
    /**
     * The root of the expression tree
     */
    private ExpressionTreeNode root;
    
    /**
     * A list of the cells that are part of the expression
     */
    private LinkedList<Cell> myCellRefs = new LinkedList<Cell>();

    /**
     * Builds an ExpressionTree based on the given postfix notation
     * @param postfix a Stack of Tokens that represents the postfix notation
     */
    public void buildTree(Stack<Token> postfix) {
        Stack<ExpressionTreeNode> stack = new Stack<>();
        
        // We need to process the postfix in reverse order
        Stack<Token> reversed = new Stack<>();
        while (!postfix.isEmpty()) {
            reversed.push(postfix.pop());
        }

        while (!reversed.isEmpty()) {
            Token token = reversed.pop();
            
            if (token instanceof OperatorToken) {
                // Operators need two operands
                if (stack.size() < 2) {
                    System.out.println("Error: Not enough operands for operator " + token);
                    this.root = null;
                    return;
                }
                ExpressionTreeNode right = stack.pop();
                ExpressionTreeNode left = stack.pop();
                stack.push(new ExpressionTreeNode(token, left, right));
            } else {
                // Numbers and cell references are leaves
                stack.push(new ExpressionTreeNode(token, null, null));
            }
        }
        
        this.root = stack.isEmpty() ? null : stack.pop();
    }

    /**
     * Evaluates the expression
     * @param spreadsheet the spreadsheet this expression tree is part of
     * @return the int result from evaluating the expression tree
     */
    public int evaluate(Spreadsheet spreadsheet) {
        if (root == null) {
            System.out.println("Error: Empty expression tree");
            return 0;
        }
        return evaluateNode(root, spreadsheet);
    }

    /**
     * Gets the list of cell dependencies
     * @return a LinkedList of the cells that are used in this tree
     */
    public LinkedList<Cell> getCellDependencies() {
        return myCellRefs;
    }

    
    private int evaluateNode(ExpressionTreeNode node, Spreadsheet spreadsheet) {
        if (node == null) return 0;
        
        if (node.token instanceof LiteralToken) {
            return ((LiteralToken) node.token).getValue();
        }
        else if (node.token instanceof CellToken) {
            CellToken cellRef = (CellToken) node.token;
            Cell cell = spreadsheet.getCell(cellRef.getRow(), cellRef.getColumn());
            if (cell == null) {
                System.out.println("Error: Invalid cell reference " + cellRef);
                return 0;
            } else {
                myCellRefs.add(cell);
            }
            return cell.getValue();
        }
        else if (node.token instanceof OperatorToken) {
            int left = evaluateNode(node.left, spreadsheet);
            int right = evaluateNode(node.right, spreadsheet);
            char op = ((OperatorToken) node.token).getOperatorToken();
            
            System.out.println("Calculating: " + left + " " + op + " " + right);
            
            switch (op) {
                case '+': return left + right;
                case '-': return left - right;
                case '*': return left * right;
                case '/': return right != 0 ? left / right : 0;
                default: 
                    System.out.println("Error: Unknown operator " + op);
                    return 0;
            }
        }
        
        System.out.println("Error: Unknown token type");
        return 0;
    }
}