package ca.ubc.ece.salt.sdjsb.ast;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.mozilla.javascript.Node;
import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.AstRoot;
import org.mozilla.javascript.ast.Block;
import org.mozilla.javascript.ast.CatchClause;
import org.mozilla.javascript.ast.DoLoop;
import org.mozilla.javascript.ast.ForInLoop;
import org.mozilla.javascript.ast.ForLoop;
import org.mozilla.javascript.ast.FunctionNode;
import org.mozilla.javascript.ast.IfStatement;
import org.mozilla.javascript.ast.Scope;
import org.mozilla.javascript.ast.SwitchCase;
import org.mozilla.javascript.ast.SwitchStatement;
import org.mozilla.javascript.ast.TryStatement;
import org.mozilla.javascript.ast.WhileLoop;
import org.mozilla.javascript.ast.WithStatement;

public abstract class StatementPreProcessor implements PreProcessor {
	
	@Override
	public void process(AstRoot root) {
		
		/* Call the processStatement method on all statements. */
		this.processStatementsSwitch(root);
		
	}
	
	/**
	 * Processes a statement an returns a new statement to insert in its
	 * place.
	 * @param node The statement to process.
	 * @return The new statement to replace the old statement.
	 */
	protected abstract AstNode processStatement(AstNode node);

	/**
	 * Calls the abstract process method on each child statement and replaces
	 * the statements with the new statements generated by the abstract process
	 * method.
	 * @param node The node with child statements.
	 */
	private void processStatements(AstRoot node) {
		
		/* Get the list of children. We can't directly iterate over the 
		 * children of the node because we also need to operate on them. */
		List<AstNode> statements = new LinkedList<AstNode>();
		for(Node child : node) {
			statements.add((AstNode) child);
		}
		
		/* Process and replace the statements. */
		for(AstNode statement : statements) {
            AstNode newStatement = this.processStatement(statement);
            if(newStatement != null) node.replaceChild(statement, newStatement);
		}

		/* Process the sub-statements. */
		for(Node child : node) {
			this.processStatementsSwitch((AstNode)child);
		}

	}

	/**
	 * Calls the abstract process method on each child statement and replaces
	 * the statements with the new statements generated by the abstract process
	 * method.
	 * @param node The node with child statements.
	 */
	private void processStatements(Block node) {

		/* Get the list of children. We can't directly iterate over the 
		 * children of the node because we also need to operate on them. */
		List<AstNode> statements = new LinkedList<AstNode>();
		for(Node child : node) {
			statements.add((AstNode) child);
		}
		
		/* Process and replace the statements. */
		for(AstNode statement : statements) {
            AstNode newStatement = this.processStatement(statement);
            if(newStatement != null) node.replaceChild(statement, newStatement);
		}

		/* Process the sub-statements. */
		for(Node child : node) {
			this.processStatementsSwitch((AstNode)child);
		}

	}

	/**
	 * Calls the abstract process method on each child statement and replaces
	 * the statements with the new statements generated by the abstract process
	 * method.
	 * @param node The node with child statements.
	 */
	private void processStatements(Scope node) {

		/* Get the list of children. We can't directly iterate over the 
		 * children of the node because we also need to operate on them. */
		List<AstNode> statements = new LinkedList<AstNode>();
		for(Node child : node) {
			statements.add((AstNode) child);
		}
		
		/* Process and replace the statements. */
		for(AstNode statement : statements) {
            AstNode newStatement = this.processStatement(statement);
            if(newStatement != null) node.replaceChild(statement, newStatement);
		}

		/* Process the sub-statements. */
		for(Node child : node) {
			this.processStatementsSwitch((AstNode)child);
		}

	}

	/**
	 * Calls the abstract process method on each child statement.
	 * @param node The node with child statements.
	 */
	private void processStatements(FunctionNode node) {

		if(node.getBody() instanceof Block)
            this.processStatementsSwitch(node.getBody());
		else {
			AstNode newStatement = this.processStatement(node.getBody());
			if(newStatement != null) node.setBody(newStatement);
		}

	}

	/**
	 * Calls the abstract process method on each child statement.
	 * @param node The node with child statements.
	 */
	private void processStatements(IfStatement node) {
		
		if(node.getThenPart() instanceof Scope) {
            this.processStatementsSwitch(node.getThenPart());
		} else {
			AstNode newStatement = this.processStatement(node.getThenPart());
			if(newStatement != null) node.setThenPart(newStatement);
		}

		if(node.getElsePart() instanceof Scope) {
            this.processStatementsSwitch(node.getElsePart());
		} else {
			AstNode newStatement = this.processStatement(node.getElsePart());
			if(newStatement != null) node.setElsePart(newStatement);
		}

	}

	/**
	 * Calls the abstract process method on each child statement.
	 * @param node The node with child statements.
	 */
	private void processStatements(WhileLoop node) {
		
		if(node.getBody() instanceof Scope) {
            this.processStatementsSwitch(node.getBody());
		} else {
			AstNode newStatement = this.processStatement(node.getBody());
			if(newStatement != null) node.setBody(newStatement);
		}

	}

	/**
	 * Calls the abstract process method on each child statement.
	 * @param node The node with child statements.
	 */
	private void processStatements(DoLoop node) {
		
		if(node.getBody() instanceof Scope) {
            this.processStatementsSwitch(node.getBody());
		} else {
			AstNode newStatement = this.processStatement(node.getBody());
			if(newStatement != null) node.setBody(newStatement);
		}

	}

	/**
	 * Calls the abstract process method on each child statement.
	 * @param node The node with child statements.
	 */
	private void processStatements(ForLoop node) {
		
		if(node.getBody() instanceof Scope) {
            this.processStatementsSwitch(node.getBody());
		} else {
			AstNode newStatement = this.processStatement(node.getBody());
			if(newStatement != null) node.setBody(newStatement);
		}

	}

	/**
	 * Calls the abstract process method on each child statement.
	 * @param node The node with child statements.
	 */
	private void processStatements(ForInLoop node) {
		
		if(node.getBody() instanceof Scope) {
            this.processStatementsSwitch(node.getBody());
		} else {
			AstNode newStatement = this.processStatement(node.getBody());
			if(newStatement != null) node.setBody(newStatement);
		}

	}

	/**
	 * Calls the abstract process method on each child statement.
	 * @param node The node with child statements.
	 */
	private void processStatements(SwitchStatement node) {
		
		for(SwitchCase switchCase : node.getCases()) {
			this.processStatementsSwitch(switchCase);
		}

	}

	/**
	 * Calls the abstract process method on each child statement.
	 * @param node The node with child statements.
	 */
	private void processStatements(SwitchCase node) {
		
		List<Pair<Integer, AstNode>> newStatements = new LinkedList<Pair<Integer, AstNode>>();
		
		for(AstNode statement : node.getStatements()) {
			AstNode newStatement = this.processStatement(statement);
			
			if(newStatement != null) {
                int i = node.getStatements().indexOf(statement);
                newStatements.add(Pair.of(i, newStatement));
			}
		}

		for(Pair<Integer, AstNode> pair : newStatements) {
			node.getStatements().set(pair.getLeft(), pair.getRight());
		}

	}

	/**
	 * Calls the abstract process method on each child statement.
	 * @param node The node with child statements.
	 */
	private void processStatements(WithStatement node) {
		
		if(node.getStatement() instanceof Block) {
            this.processStatementsSwitch(node.getStatement());
		} else {
			AstNode newStatement = this.processStatement(node.getStatement());
			if(newStatement != null) node.setStatement(newStatement);
		}

	}

	/**
	 * Calls the abstract process method on each child statement.
	 * @param node The node with child statements.
	 */
	private void processStatements(TryStatement node) {
		
		if(node.getTryBlock() instanceof Block) {
            this.processStatementsSwitch(node.getTryBlock());
		} else {
			AstNode newStatement = this.processStatement(node.getTryBlock());
			if(newStatement != null) node.setTryBlock(newStatement);
		}

		if(node.getFinallyBlock() instanceof Block) {
            this.processStatementsSwitch(node.getFinallyBlock());
		} else {
			AstNode newStatement = this.processStatement(node.getFinallyBlock());
			if(newStatement != null) node.setFinallyBlock(newStatement);
		}

		for(AstNode statement : node.getCatchClauses()) {
			this.processStatementsSwitch(statement);
		}

	}

	/**
	 * Calls the abstract process method on each child statement.
	 * @param node The node with child statements.
	 */
	private void processStatements(CatchClause node) {
        this.processStatementsSwitch(node.getBody());
	}
	
	/**
	 * Calls the appropriate build method for the node type.
	 */
	private void processStatementsSwitch(AstNode node) {
		
		if (node instanceof AstRoot) {
			this.processStatements((AstRoot)node);
		} else if (node instanceof Block) {
			this.processStatements((Block)node);
		} else if (node instanceof FunctionNode) {
			this.processStatements((FunctionNode) node);
		} else if (node instanceof IfStatement) {
			this.processStatements((IfStatement) node);
		} else if (node instanceof WhileLoop) {
			this.processStatements((WhileLoop) node);
		} else if (node instanceof DoLoop) {
			this.processStatements((DoLoop) node);
		} else if (node instanceof ForLoop) {
			this.processStatements((ForLoop) node);
		} else if (node instanceof ForInLoop) {
			this.processStatements((ForInLoop) node);
		} else if (node instanceof SwitchStatement) {
			this.processStatements((SwitchStatement) node);
		} else if (node instanceof SwitchCase) {
			this.processStatements((SwitchCase) node);
		} else if (node instanceof WithStatement) {
			this.processStatements((WithStatement) node);
		} else if (node instanceof TryStatement) {
			this.processStatements((TryStatement) node);
		} else if (node instanceof CatchClause) {
			this.processStatements((CatchClause) node);
		} else if (node instanceof Scope) {
			this.processStatements((Scope)node);
		}

	}

}
