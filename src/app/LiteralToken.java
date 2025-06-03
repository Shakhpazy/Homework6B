package app;

/**
 * This class represents a numerical value that is part of a formula of a cell.
 * 
 * @author Mark Malyshev
 * @author Yusuf Shakhpaz
 * @author Georgia Karwhite
 * @version June 2, 2025
 */
public class LiteralToken extends Token {
    /**
     * The value of the literal token
     */
    private final int value;

    /**
     * Creates a LiteralToken with the given value
     * @param value the int value of the token
     */
    public LiteralToken(int value) {
        this.value = value;
    }

    /**
     * Gets the value that this token represents
     * @return the value of this token
     */
    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }
}
