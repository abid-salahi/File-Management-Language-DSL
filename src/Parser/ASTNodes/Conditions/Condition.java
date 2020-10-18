package Parser.ASTNodes.Conditions;

import Exceptions.FMLExecutionException;
import Parser.ASTNodes.ASTNode;

public abstract class Condition extends ASTNode {

    /**
     * Returns the boolean result of the condition after evaluation
     *
     * @return the boolean result of the condition
     */
    public abstract boolean getResult() throws FMLExecutionException;

    public abstract void reset() throws FMLExecutionException;
}
