package EndToEnd;


import Exceptions.FMLExecutionException;
import Main.Main;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import static TestUtils.TestUtils.*;
import static org.junit.jupiter.api.Assertions.*;

public class MainTest {

    private File testFiles;
    private String TEST_SCRIPTS_FOLDER = "test/EndToEnd/TestFMLScripts/ValidScripts";

    @BeforeAll
    static void setupEmptyDir() throws IOException{
        String emptyDirPath = "test/EndToEnd/TestFiles_makeACopy/anEmptyFolder";
        File emptyDir = new File(emptyDirPath);
        if (!emptyDir.exists()){
            createFolderAtPath(emptyDirPath);
        }
    }

    @BeforeEach
    void setup() throws IOException {
        deleteTestFilesCopy();
        testFiles = copyTestFiles();
    }

    @AfterEach
    void teardown() throws IOException {
        deleteTestFilesCopy();
    }

    @Test
    void testEmptyScript() {
        Long lastModified = testFiles.lastModified();
        testScript(getScriptPath("emptyScript.fml"));
        assertEquals(lastModified, testFiles.lastModified());  // directory should remain unmodified
    }

    @Test
    void testDeclarationOnly() {
        Long lastModified = testFiles.lastModified();
        testScript(getScriptPath("declarationOnly.fml"));
        assertEquals(lastModified, testFiles.lastModified());  // directory should remain unmodified
    }

    @Test
    void testListDeclarationOnly() {
        Long lastModified = testFiles.lastModified();
        testScript(getScriptPath("listDeclarationOnly.fml"));
        assertEquals(lastModified, testFiles.lastModified());  // directory should remain unmodified
    }

    @Test
    void testMoveFile() throws IOException {
        testScript(getScriptPath("moveFileToFolder.fml"));
        File newFile = new File(testFiles, "anEmptyFolder/aTestFile.txt");
        File oldFile = new File(testFiles, "aTestFile.txt");
        assertTrue(FileUtils.directoryContains(testFiles,newFile));
        assertFalse(FileUtils.directoryContains(testFiles,oldFile));
    }

    @Test
    void testMoveFolder() throws IOException {
        testScript(getScriptPath("moveFolderToFolder.fml"));
        File newFile = new File(testFiles, "anEmptyFolder/aFilledFolder");
        File oldFile = new File(testFiles, "aFilledFolder");
        assertTrue(FileUtils.directoryContains(testFiles,newFile));
        assertFalse(FileUtils.directoryContains(testFiles,oldFile));
    }

    @Test
    void testCopyFile() throws IOException {
        testScript(getScriptPath("copyFileToFolder.fml"));
        File newFile = new File(testFiles, "anEmptyFolder/aTestFile.txt");
        File oldFile = new File(testFiles, "aTestFile.txt");
        assertTrue(FileUtils.directoryContains(testFiles,newFile));
        assertTrue(FileUtils.directoryContains(testFiles,oldFile));
    }

    @Test
    void testCopyFolder() throws IOException {
        testScript(getScriptPath("copyFolderToFolder.fml"));
        File newFile = new File(testFiles, "anEmptyFolder/aFilledFolder");
        File oldFile = new File(testFiles, "aFilledFolder");
        assertTrue(FileUtils.directoryContains(testFiles,newFile));
        assertTrue(FileUtils.directoryContains(testFiles,oldFile));
    }

    @Test
    void testRenameFile() throws IOException {
        testScript(getScriptPath("renameFile.fml"));
        File newFile = new File(testFiles, "aRenamedTestFile.txt");
        File oldFile = new File(testFiles, "aTestFile.txt");
        assertTrue(FileUtils.directoryContains(testFiles,newFile));
        assertFalse(FileUtils.directoryContains(testFiles,oldFile));
    }

    @Test
    void testRenameFolder() throws IOException {
        testScript(getScriptPath("renameFolder.fml"));
        File newFile = new File(testFiles, "aRenamedFilledFolder");
        File oldFile = new File(testFiles, "aFilledFolder");
        assertTrue(FileUtils.directoryContains(testFiles,newFile));
        assertFalse(FileUtils.directoryContains(testFiles,oldFile));
    }

    @Test
    void testDeleteFile() throws IOException {
        testScript(getScriptPath("deleteFile.fml"));
        File oldFile = new File(testFiles, "aTestFile.txt");
        assertFalse(FileUtils.directoryContains(testFiles,oldFile));
    }

    @Test
    void testDeleteFolder2() throws IOException {
        testScript(getScriptPath("deleteFolders.fml"));
        File oldFolder1 = new File(testFiles, "aFilledFolder");
        File oldFolder2 = new File(testFiles, "anEmptyFolder");
        assertFalse(FileUtils.directoryContains(testFiles,oldFolder1));
        assertFalse(FileUtils.directoryContains(testFiles,oldFolder2));
    }

    @Test
    void testCreateFolder() throws IOException{
        testScript(getScriptPath("createFolder2.fml"));
        File newFolder1 = new File(testFiles, "newlyCreatedFolder");
        assertTrue(FileUtils.directoryContains(testFiles,newFolder1));
    }

    @Test
    void testCompressFolder() throws IOException{
        testScript(getScriptPath("compressFolder.fml"));
        File compfolder = new File(testFiles, "aFilledFolder.zip");
        assertTrue(FileUtils.directoryContains(testFiles,compfolder));
    }

    @Test
    void testCompressFile() throws IOException{
        testScript(getScriptPath("compressFile.fml"));
        File newFolder1 = new File(testFiles, "aTestFile.txt.zip");
        assertTrue(FileUtils.directoryContains(testFiles,newFolder1));
    }

    @Test
    void testMultiAction() throws IOException {
        testScript(getScriptPath("multiaction.fml"));
        File aFilledFolder = new File(testFiles, "aFilledFolder");
        assertFalse(FileUtils.directoryContains(testFiles, aFilledFolder));
        assertFalse(FileUtils.directoryContains(testFiles, new File(testFiles, "anEmptyFolder")));

        File notAnEmptyFolder = new File(testFiles, "notAnEmptyFolder");
        aFilledFolder = new File(notAnEmptyFolder, "aFilledFolder");
        assertTrue(FileUtils.directoryContains(testFiles, notAnEmptyFolder));
        assertTrue(FileUtils.directoryContains(notAnEmptyFolder, aFilledFolder));

        assertFalse(FileUtils.directoryContains(aFilledFolder, new File(aFilledFolder, "testFile1.txt")));
        assertFalse(FileUtils.directoryContains(aFilledFolder, new File(aFilledFolder, "test.txt")));
        assertTrue(FileUtils.directoryContains(testFiles, new File(testFiles, "test.txt")));
    }

    //If cond tests
    @Test
    void testIfCondAlwaysTrue() throws IOException{
        testScript(getScriptPath("ifCondAlwaysTrue.fml"));
        File compfolder = new File(testFiles, "aFilledFolder.zip");
        assertTrue(FileUtils.directoryContains(testFiles,compfolder));
    }

    @Test
    void testIfCondAlwaysFalse() throws IOException{
        testScript(getScriptPath("ifCondAlwaysFalse.fml"));
        File compfolder = new File(testFiles, "aTestFile.txt");
        assertTrue(FileUtils.directoryContains(testFiles,compfolder));
    }

    @Test
    void testNumericIfCond() throws IOException{
        testScript(getScriptPath("ifNumericCond.fml"));
        File delFolder = new File(testFiles, "anEmptyFolder");
        assertFalse(FileUtils.directoryContains(testFiles,delFolder));

        File copiedFile = new File(testFiles, "aFilledFolder/aTestFile.txt");
        assertTrue(FileUtils.directoryContains(testFiles, copiedFile));

        File nondeletedFile = new File(testFiles, "aTestFile.txt");
        assertTrue(FileUtils.directoryContains(testFiles, nondeletedFile));

        File compressedFile = new File(testFiles, "aTestFile.txt.zip");
        assertTrue(FileUtils.directoryContains(testFiles,compressedFile));

        File deletedfile = new File(testFiles, "aFilledFolder/testfile1.txt");
        assertFalse(FileUtils.directoryContains(testFiles, deletedfile));
    }

    @Test
    void testStringIfCond() throws IOException{
        testScript(getScriptPath("ifStringCond.fml"));

        File renameFolder =new File(testFiles, "aRenamedEmptyFolder");
        File original =new File(testFiles, "anEmptyFolder");
        assertTrue(FileUtils.directoryContains(testFiles, renameFolder));
        assertFalse(FileUtils.directoryContains(testFiles, original));

        File copiedFile = new File(testFiles, "aFilledFolder/aTestFile.txt");
        assertTrue(FileUtils.directoryContains(testFiles, copiedFile));

        File notcompressedFile = new File(testFiles, "aTestFile.txt.zip");
        assertFalse(FileUtils.directoryContains(testFiles, notcompressedFile));

        File deletedFile = new File(testFiles, "aTestFile.txt");
        assertFalse(FileUtils.directoryContains(testFiles, deletedFile));
    }

    @Test
    void testBooleanIfCond() throws IOException{
        testScript(getScriptPath("ifBooleanCond.fml"));

        File renameFolder =new File(testFiles, "aRenamedBooleanFolder");
        File original =new File(testFiles, "anEmptyFolder");
        assertTrue(FileUtils.directoryContains(testFiles, renameFolder));
        assertFalse(FileUtils.directoryContains(testFiles, original));

        File compressedFolder = new File(testFiles, "aFilledFolder.zip");
        assertTrue(FileUtils.directoryContains(testFiles, compressedFolder));

        File undeletedFolder  = new File(testFiles, "aFilledFolder");
        assertTrue(FileUtils.directoryContains(testFiles, undeletedFolder));
    }

    // Not sure if this functionality will be implemented
//    @Test
//    void testParentIfCond() throws IOException{
//
//    }

    @Test
    void testNestedIfCond() throws IOException{
        testScript(getScriptPath("ifCondNested.fml"));
        File movedFile =new File(testFiles, "anEmptyFolder/aTestFile.txt");
        File original =new File(testFiles, "aTestFile.txt");
        assertTrue(FileUtils.directoryContains(testFiles, movedFile));
        assertFalse(FileUtils.directoryContains(testFiles, original));
    }

    @Test
    void testIfElseCond() throws IOException{
        testScript(getScriptPath("ifElseCond.fml"));
        File folderFile =new File(testFiles, "anEmptyFolder/aTestFile.txt");
        File fakeFolder = new File(testFiles,"aNewFilledFolder");
        assertTrue(FileUtils.directoryContains(testFiles, folderFile));
        assertFalse(FileUtils.directoryContains(testFiles, fakeFolder));
    }

    @Test
    void testIfOrCond()throws IOException {
        testScript(getScriptPath("ifOrCond.fml"));

        File newlyCreatedFolder = new File(testFiles, "newlyCreatedFolder");
        assertTrue(FileUtils.directoryContains(testFiles,newlyCreatedFolder));

        File compressedFile = new File(testFiles, "aTestfile.txt.zip");
        assertTrue(FileUtils.directoryContains(testFiles, compressedFile));

        File movedFile = new File(testFiles, "anEmptyFolder/aTestfile.txt");
        assertTrue(FileUtils.directoryContains(testFiles, movedFile));

        File nondeletedFile = new File(testFiles, "aFilledFolder");
        assertTrue(FileUtils.directoryContains(testFiles, nondeletedFile));
    }

    @Test
    void testIfAndCond()throws IOException {
        testScript(getScriptPath("ifAndCond.fml"));

        File newlyCreatedFolder = new File(testFiles, "newlyCreatedFolder");
        assertTrue(FileUtils.directoryContains(testFiles,newlyCreatedFolder));

        File compressedFile = new File(testFiles, "aTestfile.txt.zip");
        assertFalse(FileUtils.directoryContains(testFiles, compressedFile));

        File movedFile = new File(testFiles, "anEmptyFolder/aTestfile.txt");
        assertFalse(FileUtils.directoryContains(testFiles, movedFile));

        File nondeletedFile = new File(testFiles, "aFilledFolder");
        assertTrue(FileUtils.directoryContains(testFiles, nondeletedFile));
    }

    @Test
    void testIfNegationCond() throws IOException{

    }

    @Test
    void testIfAndComboCond() throws IOException{
        testScript(getScriptPath("ifAndOrcond.fml"));

        File nondeleted = new File(testFiles, "aTestfile.txt");
        assertTrue(FileUtils.directoryContains(testFiles,nondeleted));

        File deleted = new File(testFiles, "aFilledFolder/testfile1.txt");
        assertFalse(FileUtils.directoryContains(testFiles, deleted));
    }

    @Test
    void testMoveFilesForLoop() throws IOException {
        testScript(getScriptPath("moveFilesToFolderForLoop.fml"));
        File oldFile1 = new File(testFiles, "aFilledFolder/testfile1.txt");
        File oldFile2 = new File(testFiles, "aFilledFolder/testfile2.txt");
        File newFile1 = new File(testFiles, "anEmptyFolder/testfile1.txt");
        File newFile2 = new File(testFiles, "anEmptyFolder/testfile2.txt");
        assertTrue(FileUtils.directoryContains(testFiles,newFile1));
        assertTrue(FileUtils.directoryContains(testFiles,newFile2));
        assertFalse(FileUtils.directoryContains(testFiles,oldFile1));
        assertFalse(FileUtils.directoryContains(testFiles,oldFile2));
    }

    @Test
    void testMoveFilesForLoopIf() throws IOException {
        testScript(getScriptPath("moveFilesToFolderForLoopIf.fml"));
        File oldFile1 = new File(testFiles, "aFilledFolder/testfile1.txt");
        File oldFile2 = new File(testFiles, "aFilledFolder/testfile2.txt");
        File newFile1 = new File(testFiles, "anEmptyFolder/testfile1.txt");
        File newFile2 = new File(testFiles, "anEmptyFolder/testfile2.txt");
        assertTrue(FileUtils.directoryContains(testFiles,newFile1));
        assertTrue(FileUtils.directoryContains(testFiles,oldFile2));
        assertTrue(FileUtils.directoryContains(testFiles,oldFile1));
        assertFalse(FileUtils.directoryContains(testFiles,newFile2));
    }

    @Test
    void testMoveFilesForLoopReturn() throws IOException {
        testScript(getScriptPath("returnInForLoop.fml"));
        File oldFile1 = new File(testFiles, "aFilledFolder/testfile1.txt");
        File oldFile2 = new File(testFiles, "aFilledFolder/testfile2.txt");
        File newFile1 = new File(testFiles, "anEmptyFolder/testfile1.txt");
        File newFile2 = new File(testFiles, "anEmptyFolder/testfile2.txt");
        assertTrue(FileUtils.directoryContains(testFiles,newFile1));
        assertTrue(FileUtils.directoryContains(testFiles,oldFile2));
        assertFalse(FileUtils.directoryContains(testFiles,oldFile1));
        assertFalse(FileUtils.directoryContains(testFiles,newFile2));
    }

    private void testScript(String scriptPath) {
        Main.main(new String[]{scriptPath});
    }

    private String getScriptPath(String filename) {
        return Paths.get(TEST_SCRIPTS_FOLDER, filename).toString();
    }
}
