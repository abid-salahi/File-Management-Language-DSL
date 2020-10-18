
package Parser.ASTNodes.Commands;

import Exceptions.FMLExecutionException;
import Exceptions.InvalidFMLException;
import Parser.ASTNodes.Program;
import Parser.ASTNodes.Variables.FileVariable;

import java.io.File;

public class Rename extends Command {

    public Rename(String target, String destination) {
        super(target, destination);
    }

    /**
     * Renames the target file to the destination (i.e. the new name).
     * Must set target and destination first.
     */
    @Override
    public void evaluate(Program program) throws FMLExecutionException {
        try {
            FileVariable targetFileVariable = program.getFileVariable(this.targetIdentifier);
            File targetFileObject = targetFileVariable.getInnerFileObject();
            File parentFileObject = targetFileVariable.getParent().getInnerFileObject();
            File destinationFileObject = new File(parentFileObject, destination);
            logger.log(String.format("Renaming %s to %s", targetFileObject.getAbsolutePath(), destination));
            if (!targetFileObject.renameTo(destinationFileObject)) {
                throw new FMLExecutionException("Rename API execution error");
            }
            targetFileVariable.setInnerFileObject(destinationFileObject);
        } catch (Exception e) {
            throw new FMLExecutionException("Failed to rename file from " + this.targetIdentifier + " to " + this.destination, e);
        }
    }

    @Override
    public void validate(Program program) throws InvalidFMLException {
        if (targetIdentifier == null) {
            throw new InvalidFMLException("Invalid Command structure: Target identifier is null");
        } else if (!program.identifierIsDeclared(targetIdentifier)) {
            throw new InvalidFMLException("Invalid Command Structure: Target is not declared");
        } else if (destination == null) {
            throw new InvalidFMLException("Invalid Command Structure: Destination is null");
        }
    }

    @Override
    public boolean equals(Object obj) {
        return (this.getClass().isInstance(obj) && super.equals(obj));
    }

    @Override
    public String toString() {
        return "rename " + targetIdentifier + " to " + destination;
    }
}
