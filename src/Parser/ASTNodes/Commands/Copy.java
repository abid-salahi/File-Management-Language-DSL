package Parser.ASTNodes.Commands;

import Exceptions.FMLExecutionException;
import Exceptions.InvalidFMLException;
import Parser.ASTNodes.Program;
import Parser.ASTNodes.Variables.FileVariable;
import org.apache.commons.io.FileUtils;

import java.io.File;

public class Copy extends Command {

    public Copy(String target, String destination) {
        super(target, destination);
    }

    /**
     * Copies the target file to the destination path.
     * Must set target and destination first.
     */
    @Override
    public void evaluate(Program program) throws FMLExecutionException {
        FileVariable targetVar = program.getFileVariable(this.targetIdentifier);
        FileVariable destFolderVar = program.getFileVariable(this.destination);
        logger.log(String.format("Copying %s to %s", targetVar.getAbsolutePath(), destFolderVar.getAbsolutePath()));

        if (!destFolderVar.isDirectory()) {
            throw new FMLExecutionException("Destination folder cannot be a type other than directory. Please select an appropriate directory.");
        }

        File targetFile = targetVar.getInnerFileObject();
        File destFold = destFolderVar.getInnerFileObject();
        try {
            if (targetFile.isFile()) {
                FileUtils.copyFileToDirectory(targetFile, destFold);
            } else if (targetFile.isDirectory()) {
                FileUtils.copyDirectoryToDirectory(targetFile, destFold);
            } else {
                throw new FMLExecutionException("target file cannot be recognized as a valid file type.");
            }

        } catch (Exception e) {
            throw new FMLExecutionException("API failed to execute copy " + this.targetIdentifier + " to " + this.destination, e);
        }
    }

    @Override
    public void validate(Program program) throws InvalidFMLException, FMLExecutionException {
        if (!(this.targetIdentifier != null &&
                this.destination != null &&
                program.identifierIsDeclared(this.targetIdentifier) &&
                program.identifierIsDeclared(this.destination))) {
            throw new InvalidFMLException("Invalid Copy Command structure! Cannot validate copy" + this.targetIdentifier + " to " + this.destination);
        }
    }

    @Override
    public boolean equals(Object obj) {
        return (this.getClass().isInstance(obj) && super.equals(obj));
    }

    @Override
    public String toString() {
        return "copy " + targetIdentifier + " to " + destination;
    }
}
