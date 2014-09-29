package br.usp.ime.jdx.filter;

public class SimpleRegexBuilder {
		
	private String regex = new String();

	public SimpleRegexBuilder(){
		
	}

	public SimpleRegexBuilder startsWith(String s){
		//Replace each dot (.) with a slash and a dot (\.) 
		s = s.replaceAll("\\.", "\\\\.");
		regex += s + ".*";
		return this;
	}
	
	public SimpleRegexBuilder endsWith(String s){
		s = s.replaceAll("\\.", "\\\\.");
		regex += ".*" + s;
		return this;
	}
	
	public SimpleRegexBuilder or(){
		regex += "|";
		return this;
	}
	
	public SimpleRegexBuilder contains(String s){
		regex += ".*" + s + ".*";
		return this;
	}
	
	public String getRegex(){
		return regex;
	}
	
	public String getCaseInsensitiveRegex(){
		return "(?iu)" + regex;
	}

	public static void main(String[] args) {
		SimpleRegexBuilder regexBuilder = new SimpleRegexBuilder();
		regexBuilder.startsWith("initial revision");
		String regex = regexBuilder.getCaseInsensitiveRegex();
		System.out.println(regex);
		SimpleStringMatcher stringMatcher = new SimpleStringMatcher(regex);
		System.out.println(stringMatcher.matches("Initial revision"));
		System.out.println(stringMatcher.matches("Initial revisions"));
		System.out.println(stringMatcher.matches("Initial revision\n"));
		
		String fuckingString = "Initial revision\n";
		System.out.println(fuckingString.trim());
		System.out.println(fuckingString);
	}
}
