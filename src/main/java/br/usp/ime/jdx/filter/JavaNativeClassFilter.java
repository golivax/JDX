package br.usp.ime.jdx.filter;

public class JavaNativeClassFilter extends Filter{

	public JavaNativeClassFilter() {
		super("(java|javax|org\\.ietf\\.jgss|org\\.omg|" +
				"org\\.w3c|org\\.xml)\\..*");		
	}
	
	public static void main(String[] args) {
		JavaNativeClassFilter filter = 
				new JavaNativeClassFilter();
		
		System.out.println(filter.matches("java.util.Vector.java"));
		System.out.println(filter.matches("java.io.File"));
		System.out.println(filter.matches("java.lang.StringBuffer"));
		System.out.println(filter.matches("javax.xml.parsers.SAXParserFactory"));
		System.out.println(filter.matches("org.ietf.jgss.GSSContext"));
		System.out.println(filter.matches("org.omg.CORBA.CurrentOperations"));
		System.out.println(filter.matches("org.w3c.dom.CharacterData"));
		System.out.println(filter.matches("org.xml.sax.Attributes"));
		
	}
}
