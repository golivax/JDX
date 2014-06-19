package br.usp.ime.jdx.processor.extractor.methodinv.localvariable.methodbody.ifthenelseexp;

public class A {
			
	public void foo(){
		B b = new B();
		if (b.isSomething()){
			b.bar();
		}
		else if(b.isAnotherThing()){
			b.bar();
		}
		else{
			b.bar();
		}
	}
	
}
