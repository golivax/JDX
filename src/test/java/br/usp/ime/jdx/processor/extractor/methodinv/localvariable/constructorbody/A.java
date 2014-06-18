package br.usp.ime.jdx.processor.extractor.methodinv.localvariable.constructorbody;

public class A {
	
	//To be tested for method invocations inside constructors
	
	private A(){
		B b = new B();
		b.bar();
	}
	
	
	protected A(String x){
		B b = new B();
		b.bar();
	}
	
	A(String x, String y){
		B b = new B();
		b.bar();
	}
	
	public A(String x, String y, String z){
		B b = new B();
		b.bar();
	}
		
	
}
