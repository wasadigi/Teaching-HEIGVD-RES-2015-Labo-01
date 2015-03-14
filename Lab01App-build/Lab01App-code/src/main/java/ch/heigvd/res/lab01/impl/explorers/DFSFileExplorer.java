package ch.heigvd.res.lab01.impl.explorers;

import ch.heigvd.res.lab01.interfaces.IFileExplorer;
import ch.heigvd.res.lab01.interfaces.IFileVisitor;
import java.io.File;
import java.util.LinkedList;

/**
 * This implementation of the IFileExplorer interface performs a depth-first
 * exploration of the file system and invokes the visitor for every encountered
 * node (file and directory). When the explorer reaches a directory, it visits all
 * files in the directory and then moves into the subdirectories.
 * 
 * @author Olivier Liechti
 */
public class DFSFileExplorer implements IFileExplorer {

  @Override
  public void explore(File rootDirectory, IFileVisitor vistor) {
		/* visits current directory */
    vistor.visit(rootDirectory);
    
    /* tests whether current directory has files */
    if (rootDirectory.listFiles() == null)
    	return;

    /* stores subdirectories for recursive calls */
    LinkedList<File> subdirectories = new LinkedList<File>();
    
    /* browses current directory files i.e. files and subdirectories:
     *  - files are visited
     *  - subdirectories are stored
     */
    for (File file : rootDirectory.listFiles())
    	if (file.isDirectory())
        subdirectories.add(file);
      else
        vistor.visit(file);

    /* explores subdirectories */
    for (File subdirectory : subdirectories)
        explore(subdirectory, vistor);  
    }

}
