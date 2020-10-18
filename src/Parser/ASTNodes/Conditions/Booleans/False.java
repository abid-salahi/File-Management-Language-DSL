package Parser.ASTNodes.Conditions.Booleans;

import Exceptions.InvalidFMLException;
import Parser.ASTNodes.Program;

public class False extends FMLBoolean {

    @Override
    public void evaluate(Program program) {
        // Nothing to do here
    }

    @Override
    public void validate(Program program) throws InvalidFMLException {
        // Nothing to do here
    }

    @Override
    public boolean getResult() {
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof False);
    }

    @Override
    public void reset() {
        // nothing to do here
    }
}
