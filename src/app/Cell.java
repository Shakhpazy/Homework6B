package app;

public class Cell {
    private String formula;
    private int value;

    public Cell() {
        formula = "";
        value = 10;
    }

    public String getFormula() {
        return formula;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }
}
