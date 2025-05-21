package app;

public class Cell {
    private String formula;
    private int value;

    public Cell() {
        formula = "";
        value = 0;
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

    public void clear() {
        formula = "";
        value = 0;
    }
}
