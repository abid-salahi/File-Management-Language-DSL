package Parser.ASTNodes.Conditions;

import Exceptions.FMLExecutionException;
import Exceptions.InvalidFMLException;
import Parser.ASTNodes.Program;

import java.util.Collections;
import java.util.List;

import static Util.ObjectUtil.nullOrEqual;

public class AndCondition extends Condition {

    private final List<Negation> innerConditions;
    private Boolean andResult;

    public AndCondition(List<Negation> innerConditions) {
        this.andResult = null;
        this.innerConditions = innerConditions;
    }

    public AndCondition(Negation innerCondition) {
        this(Collections.singletonList(innerCondition));
    }

    @Override
    public void evaluate(Program program) throws FMLExecutionException {
        for (Negation neg : this.innerConditions) {
            neg.evaluate(program);
            if (!neg.getResult()) {
                this.andResult = false;
                return;
            }
        }
        this.andResult = true;
    }

    @Override
    public void validate(Program program) throws InvalidFMLException, FMLExecutionException {
        if (this.innerConditions == null || this.innerConditions.size() < 1) {
            throw new InvalidFMLException("OrCondition requires at least one inner condition.");
        }
        for (Negation neg : this.innerConditions) {
            neg.validate(program);
        }
    }

    @Override
    public boolean getResult() throws FMLExecutionException {
        if (this.andResult == null) {
            throw new FMLExecutionException("Cannot access result of AndCondition before calling evaluate()");
        }
        return this.andResult;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof AndCondition)) {
            return false;
        }
        AndCondition other = (AndCondition) obj;
        return nullOrEqual(this.innerConditions, other.innerConditions);
    }

    @Override
    public void reset() throws FMLExecutionException {
        this.andResult = null;
        for (Negation n: this.innerConditions) {
            n.reset();
        }
    }
}
