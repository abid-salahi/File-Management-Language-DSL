package Parser.ASTNodes.Conditions.Terms;

import Exceptions.FMLExecutionException;
import Exceptions.InvalidFMLException;
import Parser.ASTNodes.Program;

import static Util.ObjectUtil.nullOrEqual;

public class StringTerm extends Term {

    private String value;

    public StringTerm(String value) {
        this.value = value;
    }

    @Override
    public String getStringValue() {
        return this.value;
    }

    @Override
    public void validate(Program program) throws InvalidFMLException {
        if (value == null) {
            throw new InvalidFMLException("String Term is null");
        }
    }

    @Override
    public void evaluate(Program program) {
        // nothing to do here
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof StringTerm)) {
            return false;
        }
        StringTerm other = (StringTerm) obj;
        return nullOrEqual(this.value, other.value);
    }

    @Override
    public void reset() throws FMLExecutionException {
        // nothing to do here
    }
}
