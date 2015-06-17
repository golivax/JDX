package br.usp.ime.jdx.processor.extractor;

import org.eclipse.jdt.core.dom.ConstructorInvocation;

import br.usp.ime.jdx.entity.system.Method;

public interface ConstructorInvocationProcessor {

	void processConstructorInvocation(
			ConstructorInvocation constructorInvocation, Method clientMethod);
}
