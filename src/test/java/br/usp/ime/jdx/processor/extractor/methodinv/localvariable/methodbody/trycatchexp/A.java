package br.usp.ime.jdx.processor.extractor.methodinv.localvariable.methodbody.trycatchexp;

public class A {
			
	public void foo(){
				
		B b = new B();
		try{
			b.bar();
		}catch(Exception e){
			b.bar();
		}finally{
			b.bar();
		}
		
	}
	
}
