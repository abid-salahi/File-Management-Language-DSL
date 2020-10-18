package Parser.ASTNodes.Statements;

import Exceptions.FMLExecutionException;
import Exceptions.InvalidFMLException;
import Parser.ASTNodes.Program;
import Parser.ASTNodes.Variables.FileVariable;
import Parser.ASTNodes.Variables.ListVariable;

import java.util.List;

import static Util.ObjectUtil.nullOrEqual;

public class ForLoop extends Statement {

    /**
     * The list of file paths to execute the loop for.
     */
    private final String targetCollectionIdentifier;

    /**
     * The program within the loop
     */
    private final Program loopProgram;

    /**
     * The identifier used in the iterator for each element of the list
     */
    private final String loopIdentifier; // inject this in program on each iteration with different value

    public ForLoop(String targetCollectionIdentifier, Program loopProgram, String loopIdentifier) {
        this.targetCollectionIdentifier = targetCollectionIdentifier;
        this.loopProgram = loopProgram;
        this.loopIdentifier = loopIdentifier;
    }

    @Override
    public void evaluate(Program program) throws FMLExecutionException {
        ListVariable targetCollection = program.getListVariable(targetCollectionIdentifier);
        List<FileVariable> fileVars = targetCollection.getInnerList();

        for (FileVariable fv : fileVars) {
            try {
                // inject the current file variable into the loop program so it can be identified
                loopProgram.addVariable(this.loopIdentifier, fv);
                // execute loop program, including updated value for the file variable
                loopProgram.evaluate(program);
                // cleanup
                loopProgram.reset();
            } catch (FMLExecutionException e) {
                throw new FMLExecutionException("For loop failed to execute program for file variable" + fv.getName(), e);
            }
        }
    }

    @Override
    public void validate(Program program) throws InvalidFMLException, FMLExecutionException {
        if (targetCollectionIdentifier == null) {
            throw new FMLExecutionException("Invalid for loop: target is null");
        }
        if (loopProgram == null) {
            throw new FMLExecutionException("Invalid for loop: inner program is null");
        }
        if (loopIdentifier == null) {
            throw new FMLExecutionException("Invalid for loop: loop variable is null");
        }
        if (!program.identifierIsDeclared(targetCollectionIdentifier)) {
            throw new FMLExecutionException("Cannot use variable before declaration: " + targetCollectionIdentifier);
        }
        loopProgram.addIdentifierDeclaration(loopIdentifier);
        loopProgram.validate(program);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ForLoop)) {
            return false;
        }
        ForLoop other = (ForLoop) obj;
        return nullOrEqual(this.loopIdentifier, other.loopIdentifier)
                && nullOrEqual(this.targetCollectionIdentifier, other.targetCollectionIdentifier)
                && nullOrEqual(this.loopProgram, other.loopProgram);
    }

    @Override
    public void reset() throws FMLExecutionException {
        loopProgram.reset();
    }

    @Override
    public String toString() {
        return "for loop";
    }
}
