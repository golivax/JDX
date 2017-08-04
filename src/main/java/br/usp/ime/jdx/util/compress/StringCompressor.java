package br.usp.ime.jdx.util.compress;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.io.IOUtils;

/**
 * Not being used now, but might be useful in the future 
 * (such as to compress source code). 
 * Based on: https://gist.github.com/yfnick/227e0c12957a329ad138 
 * @author Gustavo Ansaldi Oliva {@link goliva@ime.usp.br}
 */
public class StringCompressor {

	  public static byte[] compress(String text) {
		  
		  byte[] b = null;
		  
		  try{
			  ByteArrayOutputStream os = new ByteArrayOutputStream(text.length());
			  GZIPOutputStream gos = new GZIPOutputStream(os);
			  gos.write(text.getBytes());
			  gos.close();
			  b = os.toByteArray();
			  os.close();
		  }catch(Exception e){
			  e.printStackTrace();
		  }
		  return b;
		  
	  }
	  
	  /**
	  public static String decompress(byte[] text) {

		  String s = null;
		  
		  try{
			  final int BUFFER_SIZE = 32;
			  ByteArrayInputStream is = new ByteArrayInputStream(text);
			  GZIPInputStream gis = new GZIPInputStream(is, BUFFER_SIZE);
			  StringBuilder string = new StringBuilder();
			  byte[] data = new byte[BUFFER_SIZE];
			  int bytesRead;
			  while ((bytesRead = gis.read(data)) != -1) {
				  string.append(new String(data, 0, bytesRead));
			  }
			  gis.close();
			  is.close();  
			  s = string.toString();
		  }catch(Exception e){
			  e.printStackTrace();
		  }
		  
		  return s;
	  }
	  */
	  
	  public static String decompress(byte[] text) {
		  
		  String s = null;
		  
		  try{
			  ByteArrayInputStream bis = new ByteArrayInputStream(text);
		      GZIPInputStream gis = new GZIPInputStream(bis);
		      byte[] bytes = IOUtils.toByteArray(gis);
		      s = new String(bytes, StandardCharsets.UTF_8);
		  }catch(Exception e){
			  e.printStackTrace();
		  }
		  
		  return s;
	  }
	
}
