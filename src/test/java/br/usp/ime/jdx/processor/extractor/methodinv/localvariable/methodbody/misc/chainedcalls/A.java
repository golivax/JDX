package br.usp.ime.jdx.processor.extractor.methodinv.localvariable.methodbody.misc.chainedcalls;

public class A {
			
	public void foo(){
		B b = new B();
		b.getC().bar();
	}	
	
}
