package app;


public class CellToken extends Token {
    public static final int BadCell = -1;
    private int column;
    private int row;

    // Private constructor for factory methods
    private CellToken(int column, int row) {
        this.column = column;
        this.row = row;
    }

    // Factory method for parsing from string
    public static ParseResult parseFrom(String input, int startIndex) {
        int column = BadCell;
        int row = BadCell;
        int index = startIndex;
        
        // Validate start index
        if (index < 0 || index >= input.length()) {
            return new ParseResult(new CellToken(BadCell, BadCell), index);
        }

        // Skip whitespace
        while (index < input.length() && Character.isWhitespace(input.charAt(index))) {
            index++;
        }

        // Check for capital letters
        if (index >= input.length() || !Character.isUpperCase(input.charAt(index))) {
            return new ParseResult(new CellToken(BadCell, BadCell), index);
        }

        // Parse column letters (A, B, ..., AA, AB, etc.)
        column = input.charAt(index++) - 'A';
        while (index < input.length() && Character.isUpperCase(input.charAt(index))) {
            column = (column + 1) * 26 + (input.charAt(index++) - 'A');
        }

        // Parse row numbers
        if (index >= input.length() || !Character.isDigit(input.charAt(index))) {
            return new ParseResult(new CellToken(BadCell, BadCell), index);
        }

        row = input.charAt(index++) - '0';
        while (index < input.length() && Character.isDigit(input.charAt(index))) {
            row = row * 10 + (input.charAt(index++) - '0');
        }

        return new ParseResult(new CellToken(column, row), index);
    }

    // Getters
    public int getColumn() { return column; }
    public int getRow() { return row; }

    // Helper class for parse result
    public static class ParseResult {
        public final CellToken token;
        public final int nextIndex;

        public ParseResult(CellToken token, int nextIndex) {
            this.token = token;
            this.nextIndex = nextIndex;
        }
    }

    @Override
    public String toString() {
        if (column == BadCell || row == BadCell) return "InvalidCell";
        
        StringBuilder colStr = new StringBuilder();
        int col = column;
        do {
            colStr.insert(0, (char)('A' + (col % 26)));
            col = col / 26 - 1;
        } while (col >= 0);
        
        return colStr.toString() + row;
    }
}