package br.usp.ime.jdx.processor.extractor.methodinv.localvariable.localclassbody;

public class A {
	
	public void foo(){
		
		class LocalClass{
			
			public void bar(){
				B b = new B();
				b.bar();
			}
		}
		
		LocalClass lc = new LocalClass();
		lc.bar();
	}
	
}
