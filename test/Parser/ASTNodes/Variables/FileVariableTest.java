package Parser.ASTNodes.Variables;

import Exceptions.FMLExecutionException;
import Parser.ASTNodes.Program;
import Parser.ASTNodes.Statements.Declarations.Paths.FMLPath;
import org.junit.jupiter.api.*;

import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class FileVariableTest {
    private static FileVariable file;
    private static FileVariable directory;
    private static String filePathStr;
    private static String directoryPathStr;
    private static Program program;

    private static FileVariable nonexistentDirectory;
    private static String nonexistentDirectoryPathStr;
    private static FileVariable nonexistentFile;
    private static String nonexistentFilePathStr;

    private static BigInteger dirCreateTime = BigInteger.valueOf(1602448779);
    private static BigInteger fileCreatetime=BigInteger.valueOf(1602449349);

    private static BigInteger dirModTime = BigInteger.valueOf(1602449291);
    private static BigInteger fileModtime = BigInteger.valueOf(1602449355);

    @BeforeAll
    static void setup(){
        program = new Program(new ArrayList<>());
        directoryPathStr = "test\\resources\\FileTestData\\Jokes";
        FMLPath dirpath = new FMLPath(directoryPathStr);

        filePathStr = "test\\resources\\FileTestData\\KindleShakespare.mobi";
        FMLPath filpath = new FMLPath(filePathStr);

        nonexistentDirectoryPathStr = "test\\resources\\FileTestData\\Jokes\\Memes";
        FMLPath nonexistdirpath = new FMLPath(nonexistentDirectoryPathStr);

        nonexistentFilePathStr = "test\\resources\\FileTestData\\KindleShakespare.epub";
        FMLPath nonexistfilepath = new FMLPath(nonexistentFilePathStr);

        try {
            dirpath.evaluate(program);
            filpath.evaluate(program);
            nonexistdirpath.evaluate(program);
            nonexistfilepath.evaluate(program);

            file = new FileVariable(filpath.getAbsolutePath());
            directory = new FileVariable(dirpath.getAbsolutePath());
            nonexistentFile = new FileVariable(nonexistfilepath.getAbsolutePath());
            nonexistentDirectory = new FileVariable(nonexistdirpath.getAbsolutePath());
        }catch (FMLExecutionException e){
            System.err.println("Something went wrong in FileVariableTest Setup");
        }
    }

    @Test
    void testIsDirectory(){
        assertTrue(directory.isDirectory());
        assertFalse(file.isDirectory());

        assertFalse(nonexistentDirectory.isDirectory());
        assertFalse(nonexistentFile.isDirectory());
    }

    @Test
    void testIsFile(){
        assertFalse(directory.isFile());
        assertTrue(file.isFile());

        assertFalse(nonexistentDirectory.isFile());
        assertFalse(nonexistentFile.isFile());
    }

    @Test
    void testExists(){
        assertTrue(directory.exists());
        assertTrue(file.exists());

        assertFalse(nonexistentDirectory.exists());
        assertFalse(nonexistentFile.exists());
    }

    @Test
    void testName(){
        assertEquals("Jokes", directory.getName());
        assertEquals("KindleShakespare.mobi", file.getName());

        assertEquals("Memes", nonexistentDirectory.getName());
        assertEquals("KindleShakespare.epub",nonexistentFile.getName());
    }

    @Test
    void testExtension(){
        assertEquals("mobi", file.getExtension());
        assertEquals("", directory.getExtension());

        assertEquals("epub", nonexistentFile.getExtension());
        assertEquals("", nonexistentDirectory.getExtension());
    }

    @Test
    void testValidSize(){
        try {
            // TODO (Investigate): Directory size appears to change when the test files go through git, commenting out this case for now
            //assertEquals(new BigInteger("5503895"), directory.getSize() );
            assertEquals(new BigInteger("11081290"), file.getSize() );
        } catch (FMLExecutionException e) {
            fail("Expected no exception to be thrown when calling getSize() for a valid file or directory");
        }
    }

    @Test
    void testInvalidSize(){
        FMLExecutionException fileE = assertThrows(FMLExecutionException.class,
                ()->nonexistentFile.getSize());
        FMLExecutionException dirE = assertThrows(FMLExecutionException.class,
                ()->nonexistentDirectory.getSize());

        assertEquals("Size of file or directory cannot be read since it does not exist",fileE.getMessage());
        assertEquals("Size of file or directory cannot be read since it does not exist",dirE.getMessage());
    }

//    TODO: find a way to make these tests work. The created and modified times change when cloned from git.
//    @Test
//    public void testTimeCreated(){
//        try {
//            assertEquals(dirCreateTime, directory.getTimeCreated());
//            assertEquals(fileCreatetime, file.getTimeCreated());
//        } catch (FMLExecutionException e) {
//            fail("Expected no exception to be thrown when calling getTimeCreated() for a valid file or directory");
//        }
//    }

    @Test
    void testInvalidTimeCreated(){
        FMLExecutionException fileE = assertThrows(FMLExecutionException.class,
                ()->nonexistentFile.getTimeCreated());
        FMLExecutionException dirE = assertThrows(FMLExecutionException.class,
                ()->nonexistentDirectory.getTimeCreated());

        assertEquals("Time Created of file or directory cannot be read since it does not exist",fileE.getMessage());
        assertEquals("Time Created of file or directory cannot be read since it does not exist",dirE.getMessage());
    }

//    @Test
//    public void testTimeLastModified(){
//        try {
//            assertEquals(dirModTime, directory.getTimeModified());
//            assertEquals(fileModtime, file.getTimeModified());
//        } catch (FMLExecutionException e) {
//            fail("Expected no exception to be thrown when calling getTimeModified() for a valid file or directory");
//        }
//    }

    @Test
    void testInvalidTimeLastModified(){
        FMLExecutionException fileE = assertThrows(FMLExecutionException.class,
                ()->nonexistentFile.getTimeModified());
        FMLExecutionException dirE = assertThrows(FMLExecutionException.class,
                ()->nonexistentDirectory.getTimeModified());

        assertEquals("Time Modified of file or directory cannot be read since it does not exist",fileE.getMessage());
        assertEquals("Time Modified of file or directory cannot be read since it does not exist",dirE.getMessage());
    }

    @Test
    void testParent(){
        int lastslash = filePathStr.lastIndexOf('\\');
        FMLPath filep = new FMLPath(filePathStr.substring(0,lastslash));

        lastslash = directoryPathStr.lastIndexOf('\\');
        FMLPath dirp = new FMLPath(directoryPathStr.substring(0,lastslash));

        lastslash = nonexistentFilePathStr.lastIndexOf('\\');
        FMLPath invalidfilep = new FMLPath(nonexistentFilePathStr.substring(0,lastslash));

        lastslash = nonexistentDirectoryPathStr.lastIndexOf('\\');
        FMLPath invaliddirp = new FMLPath(nonexistentDirectoryPathStr.substring(0,lastslash));

        try {
            filep.evaluate(program);
            dirp.evaluate(program);
            invalidfilep.evaluate(program);
            invaliddirp.evaluate(program);

            FileVariable expectedFileParent = new FileVariable(filep.getAbsolutePath());
            FileVariable expectedDirParent = new FileVariable(dirp.getAbsolutePath());
            FileVariable expectedInvalidFileParent = new FileVariable(invalidfilep.getAbsolutePath());
            FileVariable expectedInvalidDirParent = new FileVariable(invaliddirp.getAbsolutePath());

            assertEquals(expectedFileParent, file.getParent());
            assertEquals(expectedDirParent, directory.getParent());
            assertEquals(expectedInvalidFileParent, nonexistentFile.getParent());
            assertEquals(expectedInvalidDirParent, nonexistentDirectory.getParent());
        }catch (FMLExecutionException e){
            fail("Unexpected exception in FileVariableTest.testParent");
        }
    }

    @Test
    void testRootParent(){
        String rootPathStr = "\\Users";
        FMLPath dirpath = new FMLPath(rootPathStr);
        try {
            dirpath.evaluate(program);
            FileVariable fileVar = new FileVariable(dirpath.getAbsolutePath());
            FileVariable root = fileVar.getParent();
            assertEquals(root, root.getParent());
        } catch (FMLExecutionException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testAbsolutePath(){
        String fileAbsPath = file.getAbsolutePath();
        String expectedFileAbsPath = new File(fileAbsPath).getAbsolutePath();
        assertEquals(expectedFileAbsPath, fileAbsPath);

        String dirAbsPath = directory.getAbsolutePath();
        String expectedDirAbsPath = new File(directoryPathStr).getAbsolutePath();
        assertEquals(expectedDirAbsPath, dirAbsPath);
    }
}
