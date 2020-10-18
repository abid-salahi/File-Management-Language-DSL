package Parser.ASTNodes.Conditions;

import Exceptions.FMLExecutionException;
import Exceptions.InvalidFMLException;
import Parser.ASTNodes.Program;
import org.mockito.internal.matchers.And;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static Util.ObjectUtil.nullOrEqual;

public class OrCondition extends Condition {

    private final List<AndCondition> innerConditions;
    private Boolean orResult;

    public OrCondition(List<AndCondition> innerConditions) {
        this.orResult = null;
        this.innerConditions = innerConditions;
    }

    public OrCondition(AndCondition innerCondition) {
        this(Collections.singletonList(innerCondition));
    }

    @Override
    public void evaluate(Program program) throws FMLExecutionException {
        for (AndCondition andCond : this.innerConditions) {
            andCond.evaluate(program);
            if (andCond.getResult()) {
                this.orResult = true;
                return;
            }
        }
        this.orResult = false;
    }

    @Override
    public void validate(Program program) throws InvalidFMLException, FMLExecutionException {
        if (this.innerConditions == null || this.innerConditions.size() < 1) {
            throw new InvalidFMLException("AndCondition requires at least one inner condition.");
        }
        for (AndCondition and : this.innerConditions) {
            and.validate(program);
        }
    }

    @Override
    public boolean getResult() throws FMLExecutionException {
        if (this.orResult == null) {
            throw new FMLExecutionException("Cannot access result of OrCondition before calling evaluate()");
        }
        return this.orResult;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof OrCondition)) {
            return false;
        }
        OrCondition other = (OrCondition) obj;
        return nullOrEqual(this.innerConditions, other.innerConditions);
    }

    @Override
    public void reset() throws FMLExecutionException {
        this.orResult = null;
        for (AndCondition a: this.innerConditions) {
            a.reset();
        }
    }
}
