package br.usp.ime.jdx.processor.extractor.methodinv.localvariable.methodbody;

public class A {
		
	//To be tested for method invocations inside methods 
	//(excluding constructors) 
	
	private void fooPrivate(){
		B b = new B();
		b.bar();
	}
	
	protected void fooProtected(){
		B b = new B();
		b.bar();		
	}
	
	void fooDefault(){
		B b = new B();
		b.bar();			
	}
	
	public void foo(){
		B b = new B();
		b.bar();
	}
	
	static void fooStatic(){
		B b = new B();
		b.bar();
	}		
	
}
