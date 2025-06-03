package app;

/**
 * This class represents an operator in the expression tree.
 * 
 * @author Mark Malyshev
 * @author Yusuf Shakhpaz
 * @author Georgia Karwhite
 * @version June 2, 2025
 */
public class OperatorToken extends Token {
    // Constants for operators
    /**
     * Addition character
     */
    public static final char Plus = '+';

    /**
     * Subtraction character
     */
    public static final char Minus = '-';

    /**
     * Multiplication character
     */
    public static final char Mult = '*';

    /**
     * Division character
     */
    public static final char Div = '/';

    /**
     * Opening parenthesis character
     */
    public static final char LeftParen = '(';

    /**
     * Closing parenthesis character
     */
    public static final char RightParen = ')';

    /**
     * The character of this operator
     */
    private final char operatorToken;

    /**
     * Constructor creating a new operator token with the given character
     * @param operatorToken the operation character to build the token with
     */
    public OperatorToken(char operatorToken) {
        this.operatorToken = operatorToken;
    }

    /**
     * Gets the operator character of this token
     * @return character representing the operation performed by this token
     */
    public char getOperatorToken() {
        return operatorToken;
    }
    
    /**
     * Checks if the given character is an operator character
     * @param ch the character to check
     * @return true if the character is an operator, false otherwise
     */
    public static boolean isOperator(char ch) {
        return ch == Plus || ch == Minus || ch == Mult || ch == Div || ch == LeftParen;
    }
    
    /**
     * Gets the priority of the operation, based on the given character.
     * @param op the character operator
     * @return the priority of the operator (larger values being higher priority)
     */
    public static int getPriority(char op) {
        switch (op) {
            case Plus: case Minus: return 0;
            case Mult: case Div: return 1;
            case LeftParen: return 2;
            default: throw new IllegalArgumentException("Invalid operator: " + op);
        }
    }
    
    
    @Override
    public String toString() {
        return Character.toString(operatorToken);
    }

   /**
    * Gives the priority of the operator in this token
    * @return the priority of the operator (larger values being higher priority)
    */
    public int priority() {
        switch (this.operatorToken) {
            case Plus:
                return 0;
            case Minus:
                return 0;
            case Mult:
                return 1;
            case Div:
                return 1;
            case LeftParen:
                return 2;

            default:
                // This case should NEVER happen
                System.out.println("Error in priority.");
                System.exit(0);
                break;
        }
        return 0;
    }
}