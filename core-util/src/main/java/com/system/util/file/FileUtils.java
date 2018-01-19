package com.system.util.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * The <class>FileUtils</class> defines
 * utility methods for manipulating
 * files
 *
 * @author Andrew
 */
public class FileUtils {

    /**
     * Recursively create all folders defined in the provided path.
     * <p>
     * This path is in the form of 'C:\\Directory2\\Sub2\\Sub-Sub2'
     *
     * @param folderPath - the folder path to completely create
     * @return the Path object that represents this newly created path
     */
    public static Path createFolderDirectoriesRecursively(String folderPath) {
        Path path = Paths.get(folderPath);
        //Create folders only if they don't all already exist
        if (!Files.exists(path)) {
            try {
                return Files.createDirectories(path);
            } catch (IOException e) {
                //fail to create directory
                e.printStackTrace();
            }
        }
        return null;
    }

}
