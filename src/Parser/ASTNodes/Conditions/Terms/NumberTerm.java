package Parser.ASTNodes.Conditions.Terms;

import Exceptions.FMLExecutionException;
import Exceptions.InvalidFMLException;
import Language.FMLGrammar.FileSizeUnit;
import Parser.ASTNodes.Program;

import java.math.BigDecimal;

import static Util.ObjectUtil.nullOrEqual;

public class NumberTerm extends Term {

    private BigDecimal value;
    private FileSizeUnit unit; // may be null

    private BigDecimal evaluatedValue;

    public NumberTerm(BigDecimal value) {
        this.value = value;
    }

    public NumberTerm(BigDecimal value, FileSizeUnit unit) {
        this.value = value;
        this.unit = unit;
    }

    @Override
    public BigDecimal getNumericValue() {
        return evaluatedValue;
    }

    @Override
    public void validate(Program program) throws InvalidFMLException {
        if (value == null) {
            throw new InvalidFMLException("Number Term is incomplete");
        }
    }

    @Override
    public void evaluate(Program program) {
        evaluatedValue = value;
        if (unit != null) {
            evaluatedValue = evaluatedValue.multiply(unit.getMultiplier());
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof NumberTerm)) {
            return false;
        }
        NumberTerm other = (NumberTerm) obj;
        return nullOrEqual(this.value, other.value) && nullOrEqual(this.unit, other.unit);
    }

    @Override
    public void reset() throws FMLExecutionException {
        evaluatedValue = null;
    }
}
