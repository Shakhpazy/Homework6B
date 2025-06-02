package app;

public class LiteralToken extends Token {
    private final int value;

    public LiteralToken(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }
}
