package br.usp.ime.jdx.util.filesystem;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FilesystemUtils {

	public FilesystemUtils(){
		
	}

	public static String[] getPathsFromSourceDirRecursively(String sourceDir,
			String globPattern) {

		List<String> paths = new ArrayList<String>();
		
		try{
			PathFinder finder = new PathFinder(globPattern);
	        Files.walkFileTree(Paths.get(sourceDir), finder);
	        paths.addAll(finder.getResults());
		}catch(IOException e){
			e.printStackTrace();
		}
		
		String[] pathsArray = paths.toArray(new String[paths.size()]);
		return pathsArray;
	}

	public static String[] getPathsFromSourceDir(String sourceDir,
			String globPattern) {
		
		List<String> paths = new ArrayList<String>();
		
		Path sourceDirPath = Paths.get(sourceDir);
		try (DirectoryStream<Path> dirStream = 
				Files.newDirectoryStream(sourceDirPath, globPattern)) {
		
			for(Path path : dirStream){
				paths.add(path.toString());
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String[] pathsArray = paths.toArray(new String[paths.size()]);
		return pathsArray;
	}
	
	public static void main(String[] args) {
		String[] paths = FilesystemUtils.getPathsFromSourceDirRecursively(
		  "C:/tmp/tomcat/tomcat/tc7.0.x/trunk/java/org/apache/catalina", 
		  "*.java");
		
		for(String path : paths){
			System.out.println(path);
		}
		
		System.out.println(paths.length);
	}
	
}
