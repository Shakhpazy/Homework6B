package app;

public class OperatorToken extends Token {
    // Constants for operators
    public static final char Plus = '+';
    public static final char Minus = '-';
    public static final char Mult = '*';
    public static final char Div = '/';
    public static final char LeftParen = '(';
    public static final char RightParen = ')';

    private final char operatorToken;

    public OperatorToken(char operatorToken) {
        this.operatorToken = operatorToken;
    }

    public char getOperatorToken() {
        return operatorToken;
    }
    
    public static boolean isOperator(char ch) {
        return ch == Plus || ch == Minus || ch == Mult || ch == Div || ch == LeftParen;
    }

    
    //not sure we need this part
    int priority () {
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
}