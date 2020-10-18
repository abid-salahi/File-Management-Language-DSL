package Parser.ASTNodes.Conditions.Booleans;

import Exceptions.FMLExecutionException;
import Exceptions.InvalidFMLException;
import Parser.ASTNodes.Conditions.Condition;
import Parser.ASTNodes.Program;

import static Util.ObjectUtil.nullOrEqual;

/**
 * This is just a wrapper for Condition. We need this because
 * of the circular dependency Condition -> Negation -> Boolean -> Condition
 */
public class BooleanCondition extends FMLBoolean {

    Condition condition;

    public BooleanCondition(Condition condition) {
        this.condition = condition;
    }

    @Override
    public boolean getResult() throws FMLExecutionException {
        return condition.getResult();
    }

    @Override
    public void validate(Program program) throws InvalidFMLException, FMLExecutionException {
        condition.validate(program);
    }

    @Override
    public void evaluate(Program program) throws FMLExecutionException {
        condition.evaluate(program);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof BooleanCondition)) {
            return false;
        }
        BooleanCondition other = (BooleanCondition) obj;
        return nullOrEqual(this.condition, other.condition);
    }

    @Override
    public void reset() throws FMLExecutionException {
        condition.reset();
    }
}
