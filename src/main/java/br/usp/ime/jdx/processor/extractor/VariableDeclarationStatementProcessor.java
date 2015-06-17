package br.usp.ime.jdx.processor.extractor;

import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

import br.usp.ime.jdx.entity.system.Method;

public interface VariableDeclarationStatementProcessor {

	void processVariableDeclarationStatement(
			VariableDeclarationStatement variableDeclarationSt, 
			Method clientMethod);
	
}
