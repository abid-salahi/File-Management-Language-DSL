package TestUtils;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class TestUtils {

    private static String ORIGINAL_TEST_FILES_PATH = "test/EndToEnd/TestFiles_makeACopy";
    private static String COPIED_TEST_FILES_PATH = "test/EndToEnd/TestFiles";

    public static File copyTestFiles() throws IOException {
        File originalTestFiles = new File(ORIGINAL_TEST_FILES_PATH);
        File copy = new File(COPIED_TEST_FILES_PATH);
        FileUtils.copyDirectory(originalTestFiles, copy);
        return copy;
    }

    public static void deleteTestFilesCopy() throws IOException {
        File copy = new File(COPIED_TEST_FILES_PATH);
        if (copy.exists()) {
            FileUtils.forceDelete(copy);
        }
    }

    public static void createFileWithPath(String path) throws IOException {
        File file = new File(path);
        FileUtils.touch(file);
    }

    public static void deleteFileWithPath(String path) throws IOException {
        File file = new File(path);
        FileUtils.forceDelete(file);
    }

    public static void createFolderAtPath(String path) throws IOException {
        File file = new File(path);
        FileUtils.forceMkdir(file);
    }
    public static void verifyFileExists(String path) throws FileNotFoundException{
        File file = new File(path);
        if (!file.exists()) {
            throw new FileNotFoundException("File at path: " + path + " is not found.");
        }
    }
}
