package ca.ubc.ece.salt.gumtree.ast;

import java.io.InvalidClassException;

/**
 * {@link ClassifiedASTNode} is an interface for applying AST differencing
 * classifications (i.e. inserted, removed, moved and updated) to the ASTNode
 * of the base language.
 * 
 * <p>Because GumTree uses many languages, it produces a common Tree node for
 * each AST node parsed from a language. The tree nodes contain all the
 * information and functionality needed for GumTree, but a lot of language
 * and parser specific features of the AST nodes are lost in the conversion
 * from AST node to Tree node. It is therefore unsuitable to use Tree nodes
 * for CFG production.</p>
 * 
 * <p>The solution to this problem is to map the change classifications from
 * the Tree nodes back to the AST nodes. ClassifiedASTNode enables this by
 * providing a language-independent interface. To make a specific parser's
 * AST node's classifiable, the AST node class should be modified to implement
 * this interface. For example, the class {@code AstNode} in the Mozilla Rhino
 * parser for JavaScript is modified to support this interface.</p>
 * 
 * @author qhanam
 *
 */
public interface ClassifiedASTNode {

    /**
     * @param changeType The change applied to this node from AST differencing.
     */
    void setChangeType(ChangeType changeType);
    
    /**
     * @return The change applied to this node from AST differencing.
     */
    public ChangeType getChangeType();
    
    /**
     * @param node The source or destination node to map this node to.
     */
    void map(ClassifiedASTNode node) throws InvalidClassException;
    
    /**
     * @return The source or destination node that maps to this node or null
     * 		   if this node does not have a mapping (which is the case for
     * 		   inserted and removed nodes).
     */
    ClassifiedASTNode getMapping();

    /** The change type from AST differencing. **/
    public enum ChangeType {
    	INSERTED,
    	REMOVED,
    	UPDATED,
    	MOVED,
    	UNCHANGED
    }

}
