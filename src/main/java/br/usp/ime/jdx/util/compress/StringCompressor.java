package br.usp.ime.jdx.util.compress;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.io.IOUtils;

public class StringCompressor {

	  public static byte[] compress(String text) {
		    
		  byte[] bytes = new byte[0];
		  
		  GZIPOutputStream gzipos = null;
		  try{			  
			  ByteArrayOutputStream baos = new ByteArrayOutputStream();
			  BufferedOutputStream bos = new BufferedOutputStream(baos);
			  gzipos = new GZIPOutputStream(bos);
			  gzipos.write(text.getBytes());
			  bytes = baos.toByteArray();
		  }catch(Exception e){
			  e.printStackTrace();
		  }finally{
			  IOUtils.closeQuietly(gzipos);
		  }
		  
		  return bytes;
	  }
	  
	  public static String decompress(byte[] text) {
		  
		  String decompressedString = new String();
		  
		  try{
			  
			  GZIPInputStream gzipis = new GZIPInputStream(
					new ByteArrayInputStream(text));
			  
			  decompressedString = IOUtils.toString(gzipis);
		  }catch(Exception e){
			  e.printStackTrace();
		  }
		  
		  return decompressedString;
	  }
	
}
