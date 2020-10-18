package Parser.ASTNodes.Statements;

import Exceptions.FMLExecutionException;
import Exceptions.InvalidFMLException;
import Parser.ASTNodes.Conditions.OrCondition;
import Parser.ASTNodes.Program;

import static Util.ObjectUtil.nullOrEqual;

public class IfStatement extends Statement {

    private OrCondition condition;
    private Program innerProgram;
    private Program elseProgram;    // may be null (no else block)

    public IfStatement(OrCondition condition, Program innerProgram, Program elseProgram) {
        this.condition = condition;
        this.innerProgram = innerProgram;
        this.elseProgram = elseProgram;
    }

    public IfStatement(OrCondition condition, Program innerProgram) {
        this.condition = condition;
        this.innerProgram = innerProgram;
    }

    /**
     * Evaluates the condition. If condition is true, then runs innerProgram, otherwise runs elseProgram
     */
    @Override
    public void evaluate(Program program) throws FMLExecutionException {
        condition.evaluate(program);
        if (condition.getResult()) {
            innerProgram.evaluate(program);
        } else if (elseProgram != null) {
            elseProgram.evaluate(program);
        }
        condition.reset();
    }

    @Override
    public void validate(Program program) throws InvalidFMLException, FMLExecutionException {
        if (condition == null || innerProgram == null) {
            throw new InvalidFMLException("Invalid if-statement");
        }
        condition.validate(program);
        innerProgram.validate(program);
        if (elseProgram != null) {
            elseProgram.validate(program);
        }
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof IfStatement)) {
            return false;
        }
        IfStatement otherIf = (IfStatement) other;
        return nullOrEqual(this.condition, otherIf.condition)
                && nullOrEqual(this.innerProgram, otherIf.innerProgram)
                && nullOrEqual(this.elseProgram, otherIf.elseProgram);
    }

    @Override
    public void reset() throws FMLExecutionException {
        condition.reset();
        innerProgram.reset();
        if (elseProgram != null) {
            elseProgram.reset();
        }
    }

    @Override
    public String toString() {
        return "if condition";
    }
}
