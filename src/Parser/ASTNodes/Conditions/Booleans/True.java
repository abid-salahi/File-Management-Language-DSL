package Parser.ASTNodes.Conditions.Booleans;

import Exceptions.InvalidFMLException;
import Parser.ASTNodes.Conditions.Negation;
import Parser.ASTNodes.Program;

public class True extends FMLBoolean {

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
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof True);
    }

    @Override
    public void reset() {
        // Nothing to do here
    }
}
