package br.usp.ime.jdx.processor.extractor.methodinv.nodep;

public class A extends B implements C{

	private B b1;
	private B b2 = null;
	
	public A(B b){
		b = null;
		b1 = null;
		b2 = null;
		B b3;
		B b4 = null;
	}
	
	public void foo(B b){
		b = null;
		b1 = null;
		b2 = null;
		B b3;
		B b4 = null;
	}
	
	public B bar(){
		B b = null;
		return b;
	}

	@Override
	public void foo() {
		
	}
	
}
