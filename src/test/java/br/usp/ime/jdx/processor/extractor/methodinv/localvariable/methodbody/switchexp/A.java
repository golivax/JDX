package br.usp.ime.jdx.processor.extractor.methodinv.localvariable.methodbody.switchexp;

public class A {
			
	public void foo(){
		
		B b = new B();
		
		switch(b.getSomeValue()){
			case 1: 	b.bar();
						break;
			case 3: 	b.bar();
						break;
			default:	b.bar();
		}
		
	}
	
}
