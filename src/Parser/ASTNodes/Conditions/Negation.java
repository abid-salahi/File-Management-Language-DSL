package Parser.ASTNodes.Conditions;

import Exceptions.FMLExecutionException;
import Exceptions.InvalidFMLException;
import Parser.ASTNodes.Conditions.Booleans.FMLBoolean;
import Parser.ASTNodes.Program;

import static Util.ObjectUtil.nullOrEqual;

public class Negation extends Condition {

    /**
     * Only one of these will be non-null.
     */
    private Negation innerNegation = null;
    private FMLBoolean bool = null;
    private Boolean negResult = null;

    public Negation(Negation innerNegation) {
        this.innerNegation = innerNegation;
    }

    public Negation(FMLBoolean bool) {
        this.bool = bool;
    }

    @Override
    public void evaluate(Program program) throws FMLExecutionException {
        if (this.innerNegation != null) {
            this.innerNegation.evaluate(program);
            this.negResult = !this.innerNegation.getResult();
        } else if (this.bool != null) {
            this.bool.evaluate(program);
            this.negResult = this.bool.getResult();
        } else {
            throw new FMLExecutionException("Exactly one of innerNegation or FMLBoolean must be a non-null object.");
        }
    }

    @Override
    public void validate(Program program) throws InvalidFMLException, FMLExecutionException {
        if (innerNegation == null && bool == null) {
            throw new InvalidFMLException("Negation and FMLBoolean cannot both be set to null.");
        }
        if (innerNegation != null && bool != null) {
            throw new InvalidFMLException("Exactly one of innerNegation or FMLBoolean must be a non-null object.");
        }

        if (innerNegation != null) {
            innerNegation.validate(program);
        } else {
            bool.validate(program);
        }
    }

    @Override
    public boolean getResult() throws FMLExecutionException {
        /*
         * If innerNegation is not null, then return the negated value of
         * innerNegation.getResult(); If bool is not null, then return the result of
         * bool.getResult();
         */
        if (this.negResult == null) {
            throw new FMLExecutionException("Cannot access result of negCondition before calling evaluate()");
        }
        return this.negResult;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Negation)) {
            return false;
        }
        Negation other = (Negation) obj;
        return nullOrEqual(this.innerNegation, other.innerNegation) && nullOrEqual(this.bool, other.bool);
    }

    @Override
    public void reset() throws FMLExecutionException {
        if (innerNegation != null) {
            innerNegation.reset();
        }
        if (bool != null) {
            bool.reset();
        }
        negResult = null;
    }
}
