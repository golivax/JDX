package br.usp.ime.jdx.processor.extractor.methodinv.localvariable.methodbody.forexp;

public class A {
			
	public void foo(){
		B b = new B();
		for (int i = 0; i < b.getValue(); i++){
			b.bar();
		}
	}
	
}
