package br.usp.ime.jdx.processor.extractor.methodinv.localvariable.methodbody.misc.multiplecalls;

public class A {
		
	//To be tested for method invocations inside methods 
	//(excluding constructors) 
	
	public void foo(){
		B b = new B();
		b.bar();
		b.bar();
	}	
	
	public void anotherFoo(B b){
		b.bar();
		b.bar();
	}	
}
