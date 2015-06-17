package br.usp.ime.jdx.processor.extractor;

import org.eclipse.jdt.core.dom.Expression;

import br.usp.ime.jdx.entity.system.Method;

public interface ExpressionProcessor {

	void processExpression(Expression expression,Method clientMethod);
	
}
