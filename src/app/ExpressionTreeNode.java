package app;

/**
 * This class represents a single node in an ExpressionTree.
 * 
 * @author Mark Malyshev
 * @author Yusuf Shakhpaz
 * @author Georgia Karwhite
 * @version June 2, 2025
 */
public class ExpressionTreeNode {
    /**
     * The token for this node
     */
    public final Token token;

    /**
     * The left child of this node
     */
    public ExpressionTreeNode left;
    
    /**
     * The right child of this node
     */
    public ExpressionTreeNode right;

    /**
     * Constructor for a new node
     * @param token the token for this node
     * @param left the left child of this node
     * @param right the right child of this node
     */
    public ExpressionTreeNode(Token token, ExpressionTreeNode left, ExpressionTreeNode right) {
        this.token = token;
        this.left = left;
        this.right = right;
    }

    /**
     * Checks if this node is a leaf
     * @return true if it is a leaf, false otherwise
     */
    public boolean isLeaf() {
        return left == null && right == null;
    }
}