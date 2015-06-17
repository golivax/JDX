package br.usp.ime.jdx.processor.extractor.methodinv.attribute.methodbody;

public class A {

	private B b1;
	
	public void foo(){
		b1 = new B();
		b1.bar();
	}
}