package br.usp.ime.jdx.util.filesystem;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

public class FilesystemUtils {

	public FilesystemUtils(){
		
	}
	
	public static String[] getPathsFromSourceDirs(List<String> sourceDirs,
			String globPattern, boolean recursive) throws IOException{
		
		String[] paths = new String[0];
		
		for(String sourceDir : sourceDirs){
			String[] filePathsFromDir = 
					getPathsFromSourceDir(sourceDir, globPattern, recursive);
			
			paths = ArrayUtils.addAll(paths, filePathsFromDir);			
		}
		
		return paths;
	}
	
	public static String[] getPathsFromSourceDir(String sourceDir, 
			String globPattern, boolean recursive) throws IOException{
		
		if(!Files.exists(Paths.get(sourceDir))){
			throw new NoSuchFileException(sourceDir);
		}
		
		if(recursive){
			return getPathsFromSourceDirRecursively(sourceDir, globPattern);
		}
		else{
			return getPathsFromSourceDir(sourceDir, globPattern);
		}
	}

	private static String[] getPathsFromSourceDirRecursively(String sourceDir,
			String globPattern) throws IOException {

		List<String> paths = new ArrayList<String>();
		
		FileFinder finder = new FileFinder(globPattern);
	    Files.walkFileTree(Paths.get(sourceDir), finder);
	    paths.addAll(finder.getResults());
	    		
		String[] pathsArray = paths.toArray(new String[paths.size()]);
		return pathsArray;
	}

	private static String[] getPathsFromSourceDir(String sourceDir,
			String globPattern) throws IOException{
		
		List<String> paths = new ArrayList<String>();
		
		Path sourceDirPath = Paths.get(sourceDir);
		try (DirectoryStream<Path> dirStream = 
				Files.newDirectoryStream(sourceDirPath, globPattern)) {
		
			for(Path path : dirStream){
				paths.add(path.toString());
			}
			
		}
		
		String[] pathsArray = paths.toArray(new String[paths.size()]);
		return pathsArray;
	}
	
	public static void main(String[] args) {
		
		try{
		
			String[] paths = FilesystemUtils.getPathsFromSourceDir(
			  "C:/tmp/tomcat/tomcat/tc7.0.x/trunk/java/org/apache/catalina", 
			  "*.java", true);
			
			for(String path : paths){
				System.out.println(path);
			}
			
			System.out.println(paths.length);
			
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
}
