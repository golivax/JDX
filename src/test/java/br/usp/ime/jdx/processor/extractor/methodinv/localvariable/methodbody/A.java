package br.usp.ime.jdx.processor.extractor.methodinv.localvariable.methodbody;

public class A {
		
	private C c;
	
	//To be tested for method invocations inside methods 
	//(excluding constructors) 
	
	private void fooPrivate(){
		B b = new B();
		b.bar();
		boolean x = b.isSomething();
		boolean y = !b.isSomething();
		c.foo();
	}
	
	protected void fooProtected(){
		B b = new B();
		b.bar();
		boolean x = b.isSomething();
		boolean y = !b.isSomething();		
	}
	
	void fooDefault(){
		B b = new B();
		b.bar();
		boolean x = b.isSomething();
		boolean y = !b.isSomething();		
	}
	
	public void foo(){
		B b = new B();
		b.bar();
		boolean x = b.isSomething();
		boolean y = !b.isSomething();
	}
	
	static void fooStatic(){
		B b = new B();
		b.bar();
		boolean x = b.isSomething();
		boolean y = !b.isSomething();
	}		
	
}
