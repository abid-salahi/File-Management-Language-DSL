package Parser.ASTNodes.Commands;

import Exceptions.FMLExecutionException;
import Exceptions.InvalidFMLException;
import Parser.ASTNodes.Program;
import Parser.ASTNodes.Variables.FileVariable;
import org.apache.commons.io.FileUtils;

import java.io.File;

public class Create extends Command {

    public Create(String target) {
        super(target, null);
    }

    /**
     * Creates the target folder
     * Must set target first.
     */
    @Override
    public void evaluate(Program program) throws FMLExecutionException {
        FileVariable var = program.getFileVariable(targetIdentifier);
        File file = var.getInnerFileObject();
        logger.log(String.format("Creating %s", var.getAbsolutePath()));

        try {
            FileUtils.forceMkdir(file);
        } catch (Exception e) {
            throw new FMLExecutionException("Failed to create indicated folder,", e);
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
       return String.format("create %s", targetIdentifier);
    }
}
