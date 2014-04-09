package br.usp.ime.jdx.util.filesystem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import br.usp.ime.jdx.filter.Filter;

public class FilesystemUtils {

	public static String[] getPathsFromFilesRecursively(
			String rootDir, Filter filter){
		
		List<String> paths = 
				getPathFromFilesRecursively(new File(rootDir), filter);
		
		String[] pathsArray = paths.toArray(new String[paths.size()]);
		
		return pathsArray;		
	}
			
	
	public static List<String> getPathFromFilesRecursively(
			File rootDir, Filter filter) {
		
		List<String> paths = new ArrayList<String>();
		
		try {
			File[] filesAndDirs = rootDir.listFiles();
			for (File file : filesAndDirs) {
				//If it is a directory, we get all the files in there
				if (file.isDirectory()) {
					paths.addAll(getPathFromFilesRecursively(file, filter));
				} 
				//If it is a file and matches the filter, then we get it
				else if (file.isFile() && filter.matches(file.getPath())) {						
					paths.add(file.getPath());
				}
			}
		} catch (Throwable e) {
			System.err.println("getPathFromFilesRecursively error in " + 
				rootDir);
			
			e.printStackTrace();
		}
		
		return paths;
	}
	
}
