package br.usp.ime.jdx.processor.extractor.methodinv.localvariable.innerclassbody;

public class A {
	
	//To be tested for method invocations inside inner classes
	//(i.e. non-static nested class)
	class I1{
		
		public I1(){
			B b = new B();
			b.bar();	
		}
		
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
		
		//another inner class
		class I2{
			public void foo(){
				B b = new B();
				b.bar();
			}
		}
		
	}	
		
	
}
