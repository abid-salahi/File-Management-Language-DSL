package Parser.ASTNodes.Statements;

import Exceptions.FMLExecutionException;
import Exceptions.InvalidFMLException;
import Parser.ASTNodes.Program;
import org.apache.commons.lang3.NotImplementedException;

public class ReturnStatement extends Statement {

    @Override
    public void evaluate(Program program) {
        program.terminate();
    }

    @Override
    public void validate(Program program) throws InvalidFMLException {
        // Nothing to do here
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof ReturnStatement; // all return statements are equal
    }

    @Override
    public void reset() throws FMLExecutionException {
        // nothing to do here
    }

    @Override
    public String toString() {
        return "return";
    }
}
