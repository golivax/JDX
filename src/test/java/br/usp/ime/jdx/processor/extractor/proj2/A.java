package br.usp.ime.jdx.processor.extractor.proj2;

public class A {
	
	//Constructor tests
	
	private A(){
		B b1 = new B();
		B b2 = new B();
	}
	
	
	protected A(String x){
		B b1 = new B();
		B b2 = new B();
	}
	
	A(String x, String y){
		B b1 = new B();
		B b2 = new B();
	}
	
	public A(String x, String y, String z){
		B b1 = new B();
		B b2 = new B();
	}
	
	//Method tests
	
	private void fooPrivate(){
		B b1 = new B();
		B b2 = new B();	
	}
	
	protected void fooProtected(){
		B b1 = new B();
		B b2 = new B();		
	}
	
	void fooDefault(){
		B b1 = new B();
		B b2 = new B();			
	}
	
	public void foo(){
		B b1 = new B();
		B b2 = new B();
	}
	
	static void fooStatic(){
		B b1 = new B();
		B b2 = new B();
	}
	

	//Inner class test
	
	class I1{
		
		private void fooPrivate(){
			B b1 = new B();
			B b2 = new B();	
		}
		
		protected void fooProtected(){
			B b1 = new B();
			B b2 = new B();		
		}
		
		void fooDefault(){
			B b1 = new B();
			B b2 = new B();			
		}
		
		public void foo(){
			B b1 = new B();
			B b2 = new B();
		}
		
		class I2{
			public void foo(){
				B b1 = new B();
				B b2 = new B();
			}
		}
		
	}
	

		
		/**
		int i = 0;
		
		while(i < 10){
			b.bar();
		}
		
		while(x.equals(y)){
			b.bar();
		}
		
		do{
			b.bar();
		}while(i < 10);
		
		if(x.equals(y)){
			b.bar();
		}
		else if(!x.equals(y)){
			b.bar();
		}
		else if(x.length() < y.length()){
			b.bar();
		}
		else{
			b.bar();
		}
		*/
		
	
}
