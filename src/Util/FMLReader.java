package Util;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Reads a .fml file from disk
 */
public class FMLReader {

    /**
     * Validates the path to the .fml file and returns the contents of the file as a string
     *
     * @param path path to the .fml script
     * @return the contents of the script as a string
     * @throws IllegalArgumentException if the path does not exist, or if the path does not point to .fml file
     * @throws IOException              if there is an error reading the file from disk
     */
    public static String read(String path) throws IllegalArgumentException, IOException {
        try {
            File script = new File(path);
            if (!script.exists()) {
                throw new IllegalArgumentException("File does not exist");
            }
            if (!script.isFile()) {
                throw new IllegalArgumentException("Path is not a file");
            }
            if (!FilenameUtils.getExtension(script.getName()).equals("fml")) {
                throw new IllegalArgumentException("File is not a .fml file");
            }
            return Files.readString(Paths.get(script.getAbsolutePath()));
        } catch (IOException e) {
            throw new IOException("Failed to read .fml file at " + path, e);
        }
    }
}
