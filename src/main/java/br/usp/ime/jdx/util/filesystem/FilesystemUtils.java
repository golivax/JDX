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
	
	public static String[] getPathsFromSourceDir(String projectDir, String sourceDir, 
			String globPattern, boolean recursive) throws IOException{
		
		String finalPath = getPath(projectDir, sourceDir);
		
		if(!Files.exists(Paths.get(finalPath))){
			throw new NoSuchFileException(finalPath);
		}
		
		if(recursive){
			return getPathsFromSourceDirRecursively(finalPath, globPattern);
		}
		else{
			return getPathsFromSourceDir(finalPath, globPattern);
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
	
	public static String getPath(String dir, String subDir) {
		String finalPath = dir;
		if(!subDir.equals("") || !subDir.equals("./")) {
			finalPath = finalPath + "/" + subDir;
		}
		return finalPath;
	}	
		
}