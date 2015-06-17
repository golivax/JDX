package br.usp.ime.jdx.processor.extractor;

import org.eclipse.jdt.core.dom.FieldDeclaration;

import br.usp.ime.jdx.entity.system.Method;

public interface FieldDeclarationProcessor {

	void processFieldDeclaration(FieldDeclaration fieldDeclaration, 
			Method clientMethod);
	
}
