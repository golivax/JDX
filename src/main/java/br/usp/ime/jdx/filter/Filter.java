package br.usp.ime.jdx.filter;

import java.util.regex.Pattern;

public class Filter {

	private String regex;
	private final Pattern pattern;
	
	public Filter(String regex){
		this.regex = regex;
		this.pattern = Pattern.compile(regex);
	}
	
	public boolean matches(final String s){
		return pattern.matcher(s).matches();
	}
	
	public String getRegex(){
		return regex;
	}

}