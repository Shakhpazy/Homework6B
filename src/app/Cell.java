package app;

public class Cell {
    private String formula;
    private int value;
    private ExpressionTree expressionTree;  // Add this field


    public Cell() {
        formula = "";
        value = 0;
        expressionTree = null;

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
    
    public void setExpressionTree(ExpressionTree tree) {
        this.expressionTree = tree;
    }
    
    public int evaluate(Spreadsheet spreadsheet) {
        if (expressionTree != null) {
            this.value = expressionTree.evaluate(spreadsheet);
            System.out.println("Evaluated formula '" + formula + "' = " + value);
        }
        return this.value;
    }
    
    public void clear() {
        formula = "";
        value = 0;
    }
}
