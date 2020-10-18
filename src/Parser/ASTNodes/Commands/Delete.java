package Parser.ASTNodes.Commands;

import Exceptions.FMLExecutionException;
import Exceptions.InvalidFMLException;
import Parser.ASTNodes.Program;
import Parser.ASTNodes.Variables.FileVariable;
import org.apache.commons.io.FileUtils;

import java.io.File;

public class Delete extends Command {

    public Delete(String target) {
        super(target, null);
    }

    /**
     * Deletes the file at the target file path
     */
    @Override
    public void evaluate(Program program) throws FMLExecutionException {
        FileVariable var = program.getFileVariable(targetIdentifier);
        File file = var.getInnerFileObject();
        logger.log(String.format("Deleting %s", var.getAbsolutePath()));

        if (!file.exists()) {
            throw new FMLExecutionException(String.format("Cannot delete %s: the file/folder does not exist", file.getName()));
        }
        try {
            FileUtils.forceDelete(file);
        } catch (Exception e) {
            throw new FMLExecutionException("Failed to delete indicated file,", e);
        }
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
        return String.format("delete %s", targetIdentifier);
    }
}
