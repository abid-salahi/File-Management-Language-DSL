package Parser.ASTNodes.Commands;

import Exceptions.FMLExecutionException;
import Exceptions.InvalidFMLException;
import Parser.ASTNodes.Program;
import Parser.ASTNodes.Variables.FileVariable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Compress extends Command {

    public Compress(String target) {
        super(target, null);
    }

    /**
     * Compresses the target folder/file
     * Must set target first.
     */
    @Override
    public void evaluate(Program program) throws FMLExecutionException {
        FileVariable fileVariable = program.getFileVariable(targetIdentifier);
        File file = fileVariable.getInnerFileObject();
        logger.log("Compressing " + file.getAbsolutePath());

        try {
            String compressedFileName = file.getName().concat(".zip");
            String fileToWriteTo = new File(file.getParentFile(), compressedFileName).getAbsolutePath();
            FileOutputStream fos = new FileOutputStream(fileToWriteTo);
            ZipOutputStream zos = new ZipOutputStream(fos);
            if (!file.isDirectory()) {
                zos.putNextEntry(new ZipEntry(file.getName()));

                byte[] bytes = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
                zos.write(bytes,0, bytes.length);
                zos.closeEntry();
                zos.close();
            } else {
                zipFile(file, file.getName(), zos);
                zos.close();
                fos.close();
            }

        } catch (Exception e) {
            throw new FMLExecutionException("Failed to compress indicated file,", e);
        }
    }

    private void zipFile(File fileToZip, String fileName, ZipOutputStream zipOut) throws IOException {
        if (fileToZip.isHidden()) {
            return;
        }
        if (fileToZip.isDirectory()) {
            if (fileName.endsWith("/")) {
                zipOut.putNextEntry(new ZipEntry(fileName));
                zipOut.closeEntry();
            } else {
                zipOut.putNextEntry(new ZipEntry(fileName + "/"));
                zipOut.closeEntry();
            }
            File[] children = fileToZip.listFiles();
            for (File childFile : children) {
                zipFile(childFile, fileName + "/" + childFile.getName(), zipOut);
            }
            return;
        }
        FileInputStream fis = new FileInputStream(fileToZip);
        ZipEntry zipEntry = new ZipEntry(fileName);
        zipOut.putNextEntry(zipEntry);
        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zipOut.write(bytes, 0, length);
        }
        fis.close();
    }

    @Override
    public void validate(Program program) throws InvalidFMLException {
        if (targetIdentifier == null) {
            throw new InvalidFMLException("Invalid Command structure: Target identifier is null");
        } else if (!program.identifierIsDeclared(targetIdentifier)) {
            throw new InvalidFMLException("Invalid Command Structure: Target is not declared");
        }
    }

    @Override
    public boolean equals(Object obj) {
        return (this.getClass().isInstance(obj) && super.equals(obj));
    }

    @Override
    public String toString() {
        return "compress " + targetIdentifier;
    }
}
