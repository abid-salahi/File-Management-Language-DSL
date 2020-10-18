package Parser.ASTNodes;

import Exceptions.FMLExecutionException;
import Exceptions.InvalidFMLException;
import Util.Logger;

/**
 * Represents a node in the AST representation of an FML program
 */
public abstract class ASTNode {

    protected static Logger logger = Logger.get();

    /**
     * This flag is set to 'true' by the Validator.
     * DO NOT set this flag outside the Validator.
     */
    private boolean validated = false;

    /**
     * Validates the subtree starting at this Node
     *
     * @throws InvalidFMLException if the subtree is invalid
     */
    public abstract void validate(Program program) throws InvalidFMLException, FMLExecutionException;

    /**
     * Sets the validated flag to true.
     * DO NOT call this outside the Validator.
     */
    public void setValidated() {
        this.validated = true;
    }

    /**
     * @return true if the program has been validated
     */
    public boolean isValidated() {
        return this.validated;
    }

    /**
     * Evaluate the subtree starting at this Node
     */
    public abstract void evaluate(Program program) throws FMLExecutionException;

    public abstract void reset() throws FMLExecutionException;
}
