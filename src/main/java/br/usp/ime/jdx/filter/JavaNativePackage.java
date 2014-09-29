package br.usp.ime.jdx.filter;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum JavaNativePackage {
	
	//JAVA EE 6 high-level packages
	JAVAX_ANNOTATION("javax.annotation"),
	JAVAX_DECORATOR("javax.decorator"),
	JAVAX_EJB("javax.ejb"),
	JAVAX_EL("javax.el"),
	JAVAX_ENTERPRISE("javax.enterprise"),
	JAVAX_FACES("javax.faces"),
	JAVAX_INJECT("javax.inject"),
	JAVAX_INTERCEPTOR("javax.interceptor"),
	JAVAX_JMS("javax.jms"),
	JAVAX_JWS("javax.jws"),
	JAVAX_MAIL("javax.mail"),
	JAVAX_MANAGEMENT("javax.management"),
	JAVAX_PERSISTENCE("javax.persistence"),
	JAVAX_RESOURCE("javax.resource"),
	JAVAX_SECURITY("javax.security"),
	JAVAX_SERVLET("javax.servlet"),
	JAVAX_TRANSACTION("javax.transaction"),
	JAVAX_VALIDATION("javax.validation"),
	JAVAX_WS("javax.ws"),
	JAVAX_XML("javax.xml"),	
	
	//JAVA SE 7 high-level packages
	JAVA_APPLET("java.applet"),
	JAVA_AWT("java.awt"),
	JAVA_BEANS("java.beans"),
	JAVA_IO("java.io"),
	JAVA_LANG("java.lang"),
	JAVA_MATH("java.math"),
	JAVA_NET("java.net"),
	JAVA_NIO("java.nio"),
	JAVA_RMI("java.rmi"),
	JAVA_SECURITY("java.security"),
	JAVA_SQL("java.sql"),
	JAVA_TEXT("java.text"),
	JAVA_UTIL("java.util"),
	JAVAX_ACCESSIBILITY("javax.accessibility"),
	JAVAX_ACTIVATION("javax.activation"),
	JAVAX_ACTIVITY("javax.activity"),
	//JAVAX_ANNOTATION("java.ACCESSIBILITY"), --> Already included in JAVA EE
	JAVAX_CRYPTO("javax.crypto"),
	JAVAX_IMAGEIO("javax.imageio"),
	//JAVAX_JWS("javax.jws"), --> Already included in JAVA EE
	JAVAX_LANG("javax.imageio"),
	//JAVAX_MANAGEMENT("javax.management"), --> Already included in JAVA EE
	JAVAX_NAMING("javax.naming"),
	JAVAX_NET("javax.net"),
	JAVAX_PRINT("javax.print"),
	JAVAX_RMI("javax.rmi"),
	JAVAX_SCRIPT("javax.script"),
	//JAVAX_SECURITY("javax.security"), --> Already included in JAVA EE
	JAVAX_SOUND("javax.sound"),
	JAVAX_SQL("javax.sql"),
	JAVAX_SWING("javax.swing"),
	JAVAX_TOOLS("javax.tools"),
	//JAVAX_TRANSACTION("javax.transaction"), --> Already included in JAVA EE
	//JAVAX_XML("javax.xml"), --> Already included in JAVA EE
	ORG_IETF("org.ietf"),
	ORG_OMG("org.omg"),
	ORG_W3C("org.w3c"),
	ORG_XML_SAX("org.xml.sax"),
	;
	
	
	private static final Map<String,JavaNativePackage> lookup = 
			new HashMap<String,JavaNativePackage>();

	static {
	 for(JavaNativePackage e : EnumSet.allOf(JavaNativePackage.class))
	     lookup.put(e.getPackageName(), e);
	}
	
	private String packageName;
	
	private JavaNativePackage(String packageName){
		this.packageName = packageName;
	}

	public String getPackageName(){
		return packageName;
	}
	
	public static JavaNativePackage get(String pkgName){
		return lookup.get(pkgName);
	}
	
}
