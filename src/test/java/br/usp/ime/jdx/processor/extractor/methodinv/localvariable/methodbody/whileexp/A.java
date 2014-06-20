package br.usp.ime.jdx.processor.extractor.methodinv.localvariable.methodbody.whileexp;

import br.usp.ime.jdx.processor.extractor.methodinv.localvariable.methodbody.dowhileexp.B;

public class A {
			
	public void foo(){
		
		B b = new B();
		while(b.isSomething()){
			b.bar();
		}
		
	}
	
}
