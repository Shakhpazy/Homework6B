package app;


public class ExpressionTreeNode {
    public final Token token;
    public ExpressionTreeNode left;
    public ExpressionTreeNode right;

    public ExpressionTreeNode(Token token, ExpressionTreeNode left, ExpressionTreeNode right) {
        this.token = token;
        this.left = left;
        this.right = right;
    }

    public boolean isLeaf() {
        return left == null && right == null;
    }
}