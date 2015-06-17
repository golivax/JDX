package br.usp.ime.jdx.processor.extractor;

import org.eclipse.jdt.core.dom.ImportDeclaration;

import br.usp.ime.jdx.entity.system.CompUnit;

public interface ImportDeclarationProcessor {

	void processImportDeclaration(ImportDeclaration importDeclaration, 
			CompUnit clientCompilationUnit);
}
