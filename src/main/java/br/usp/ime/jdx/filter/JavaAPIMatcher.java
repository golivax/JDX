package br.usp.ime.jdx.filter;

import java.util.EnumSet;

import org.apache.commons.lang3.StringUtils;


public class JavaAPIMatcher implements StringMatcher{

	private SimpleStringMatcher simpleStringMatcher;
	
	public JavaAPIMatcher() {
	
		SimpleRegexBuilder regexBuilder = new SimpleRegexBuilder();
		for(JavaNativePackage e : EnumSet.allOf(JavaNativePackage.class)){
			regexBuilder.startsWith(e.getPackageName() + ".").or();
		}
		
		String regex = regexBuilder.getRegex();		
		this.simpleStringMatcher = new SimpleStringMatcher(regex);
	}
	
	public void allowClassesIn(JavaNativePackage javaEnum){
		String pkgName = javaEnum.getPackageName();
		//Replace each dot (.) with a slash and a dot (\.) 
		String pkgRegex = pkgName.replaceAll("\\.", "\\\\.") + "\\..*";
		String regex = simpleStringMatcher.getRegex();
		regex = StringUtils.remove(regex, pkgRegex);
		simpleStringMatcher.setRegex(regex);
	}

	@Override
	public boolean matches(String s) {
		return simpleStringMatcher.matches(s);
	}
	
	public String getRegex(){
		return simpleStringMatcher.getRegex();
	}
	
	public static void main(String[] args) {
		
		//TODO: Transform this into a unit test
		JavaAPIMatcher filter = new JavaAPIMatcher();		
		System.out.println(filter.getRegex());
		
		System.out.println("Should be true");
		
		System.out.println(filter.matches("java.util.Vector"));
		System.out.println(filter.matches("java.io.Closeable"));
		System.out.println(filter.matches("java.lang.String"));
		System.out.println(filter.matches("javax.xml.parsers.SAXParserFactory"));
		System.out.println(filter.matches("org.ietf.jgss.GSSContext"));
		System.out.println(filter.matches("org.omg.CORBA.CurrentOperations"));
		System.out.println(filter.matches("org.w3c.dom.CharacterData"));
		System.out.println(filter.matches("org.xml.sax.Attributes"));
		System.out.println(filter.matches("javax.servlet.AsyncListener"));
		
		System.out.println("Should be false");
		
		System.out.println(filter.matches("java.utila.Vector"));
		
		filter.allowClassesIn(JavaNativePackage.JAVAX_SERVLET);
		
		System.out.println("Should be true");

		System.out.println(filter.matches("java.util.Vector"));
		System.out.println(filter.matches("java.io.Closeable"));
		System.out.println(filter.matches("java.lang.String"));
		System.out.println(filter.matches("javax.xml.parsers.SAXParserFactory"));
		System.out.println(filter.matches("org.ietf.jgss.GSSContext"));
		System.out.println(filter.matches("org.omg.CORBA.CurrentOperations"));
		System.out.println(filter.matches("org.w3c.dom.CharacterData"));
		System.out.println(filter.matches("org.xml.sax.Attributes"));
		
		System.out.println("Should be false");
		System.out.println(filter.matches("javax.servlet.AsyncListener"));
	}
	
}
