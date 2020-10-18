package Parser.ASTNodes.Variables;

import Exceptions.FMLExecutionException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static Util.ObjectUtil.nullOrEqual;

public class FileVariable extends Variable {
    private File innerFileObject;
    private BasicFileAttributes attributes;

    public FileVariable(String absolutePath) throws FMLExecutionException {
        innerFileObject = new File(absolutePath);
        initAttributes();
    }

    /**
     * @return true if this FileVariable Points to a location that exists
     */
    public boolean exists() {
        return Files.exists(innerFileObject.toPath());
    }

    /**
     * @return the reference to the inner File object
     */
    public File getInnerFileObject() {
        return innerFileObject;
    }

    /**
     * @return the reference to the inner File object
     */
    public void setInnerFileObject(File innerFileObject) {
        this.innerFileObject = innerFileObject;
    }

    /**
     * @return the name of the file or folder
     */
    public String getName() {
        return innerFileObject.getName();
    }

    /**
     * Get the extension of the file
     *
     * @return returns extension without the dot ('.'). eg. "txt" for "file.txt"
     * Returns empty string for folders.
     */
    public String getExtension() {
        return FilenameUtils.getExtension(innerFileObject.getName());
    }

    /**
     * @return the size of the file or folder in bytes
     */
    public BigInteger getSize() throws FMLExecutionException {
        if (!Files.exists(innerFileObject.toPath())) {
            throw new FMLExecutionException("Size of file or directory cannot be read since it does not exist");
        }
        return FileUtils.sizeOfAsBigInteger(innerFileObject);
    }

    /**
     * @return the UNIX timestamp for when the file was created
     */
    public BigInteger getTimeCreated() throws FMLExecutionException {
        if (!Files.exists(innerFileObject.toPath())) {
            throw new FMLExecutionException("Time Created of file or directory cannot be read since it does not exist");
        }
        return BigInteger.valueOf(attributes.creationTime().to(TimeUnit.SECONDS));
    }

    /**
     * @return the UNIX timestamp for when the file was last modified
     */
    public BigInteger getTimeModified() throws FMLExecutionException {
        if (!Files.exists(innerFileObject.toPath())) {
            throw new FMLExecutionException("Time Modified of file or directory cannot be read since it does not exist");
        }
        return BigInteger.valueOf(attributes.lastModifiedTime().to(TimeUnit.SECONDS));
    }

    /**
     * Get a reference to the parent folder
     *
     * @return a FileVariable that points to the parent folder.
     * If this folder is the root folder, returns reference to self.
     */
    public FileVariable getParent() throws FMLExecutionException {
        String parent = innerFileObject.getParent();
        return parent == null ? this : new FileVariable(parent);
    }

    /**
     * @return true if and only if this FileVariable points to a location that exists that is also a directory,
     * false otherwise
     */
    public boolean isDirectory() {
        return innerFileObject.isDirectory();
    }

    /**
     * @return true if and only if this FileVariable points to a location that exists that is also a file,
     * false otherwise
     */
    public boolean isFile() {
        return innerFileObject.isFile();
    }

    /**
     * @return the absolute path to this FileVariable
     */
    public String getAbsolutePath() {
        return innerFileObject.getAbsolutePath();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FileVariable)) return false;
        FileVariable that = (FileVariable) o;
        return nullOrEqual(this.getInnerFileObject(), that.getInnerFileObject());
    }

    @Override
    public int hashCode() {
        return Objects.hash(innerFileObject);
    }

    private void initAttributes() throws FMLExecutionException {
        Path p = Paths.get(innerFileObject.getAbsolutePath());
        try {
            if (Files.exists(p)) {
                attributes = Files.readAttributes(p, BasicFileAttributes.class);
            }
        } catch (IOException e) {
            throw new FMLExecutionException("File attributes could not be read");
        }
    }
}
