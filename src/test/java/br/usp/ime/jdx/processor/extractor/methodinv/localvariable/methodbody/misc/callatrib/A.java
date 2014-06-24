package br.usp.ime.jdx.processor.extractor.methodinv.localvariable.methodbody.misc.callatrib;

public class A {
			
	public void foo(){
		B b = new B();
		C c = b.getC();
		c.bar();
	}	
	
}
