/*
 * Copyright 2011 Jean-Rémy Falleri
 *
 * This file is part of Praxis.
 * Praxis is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Praxis is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Praxis.  If not, see <http://www.gnu.org/licenses/>.
 */

package fr.labri.gumtree.gen.js;

import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

import org.mozilla.javascript.Parser;
import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.AstRoot;

import ca.ubc.ece.salt.sdjsb.ast.ConditionalPreProcessor;
import ca.ubc.ece.salt.sdjsb.ast.ShortCircuitPreProcessor;
import ca.ubc.ece.salt.sdjsb.ast.VarPreProcessor;
import fr.labri.gumtree.io.TreeGenerator;
import fr.labri.gumtree.tree.Tree;

public class RhinoTreeGenerator extends TreeGenerator {

	private Map<AstNode, Tree> treeNodeMap;

	@Override
	public Tree generate(String source, String file, boolean preProcess) {
		Parser p = new Parser();
        AstRoot root = p.parse(source, file, 1);

        if(preProcess) {
			/* Expand variable initializers. */
			VarPreProcessor varPreProcessor = new VarPreProcessor();
			varPreProcessor.process(root);

			/* Expand the ternary operators. */
			ConditionalPreProcessor conditionalPreProcessor = new ConditionalPreProcessor();
			conditionalPreProcessor.process(root);

			/* Expand short circuit operators. */
			ShortCircuitPreProcessor shortCircuitPreProcessor = new ShortCircuitPreProcessor();
			shortCircuitPreProcessor.process(root);
        }

        System.out.println(root.toSource());

        /* Build the GumTree tree. */
        RhinoTreeVisitor visitor = new RhinoTreeVisitor(root);
        root.visit(visitor);
        this.treeNodeMap = visitor.getTreeNodeMap();
        return visitor.getTree();
	}

	@Override
	public Tree generate(String file, boolean preProcess) {
		Parser p = new Parser();
		try {
			AstRoot root = p.parse(new FileReader(file), file, 1);

			if(preProcess) {
				/* Expand variable initializers. */
				VarPreProcessor varPreProcessor = new VarPreProcessor();
				varPreProcessor.process(root);

				/* Expand the ternary operators. */
				ConditionalPreProcessor conditionalPreProcessor = new ConditionalPreProcessor();
				conditionalPreProcessor.process(root);

				/* Expand short circuit operators. */
				ShortCircuitPreProcessor shortCircuitPreProcessor = new ShortCircuitPreProcessor();
				shortCircuitPreProcessor.process(root);
			}

            System.out.println(root.toSource());

            /* Build the GumTree tree. */
			RhinoTreeVisitor visitor = new RhinoTreeVisitor(root);
			root.visit(visitor);
			this.treeNodeMap = visitor.getTreeNodeMap();
			return visitor.getTree();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * This provides clients with efficient access to Tree nodes if they have
	 * AstNodes. For example, a client may be  working with AstNodes instead
	 * of the Tree nodes (AstNodes provide more data and functionality). This
	 * will allow them to get the GumTree data for each AstNode without having
	 * to perform an inefficient search.
	 * @return The map of each AstNode to the Tree node that it created.
	 */
	public Map<AstNode, Tree> getTreeNodeMap() {
		return this.treeNodeMap;
	}

	@Override
	public boolean handleFile(String file) {
		return file.toLowerCase().endsWith(".js");
	}

	@Override
	public String getName() {
		return "js-rhino";
	}

}
