package br.usp.ime.jdx.filter;

import java.util.regex.Pattern;

public class SimpleStringMatcher implements StringMatcher{

	private String regex;
	private Pattern pattern;
	
	public SimpleStringMatcher(String regex){
		this.regex = regex;
		this.pattern = Pattern.compile(regex,Pattern.DOTALL);
	}
		
	public String getRegex(){
		return regex;
	}
	
	public void setRegex(String regex){
		this.regex = regex;
		this.pattern = Pattern.compile(regex,Pattern.DOTALL);
	}

	@Override
	public boolean matches(final String s){
		return pattern.matcher(s).matches();
	}

}