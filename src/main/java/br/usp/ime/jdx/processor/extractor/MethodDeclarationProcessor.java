package br.usp.ime.jdx.processor.extractor;

import org.eclipse.jdt.core.dom.MethodDeclaration;

import br.usp.ime.jdx.entity.system.Method;

public interface MethodDeclarationProcessor {

	void processMethodDeclaration(MethodDeclaration methodDeclaration, 
			Method clientMethod);
}
