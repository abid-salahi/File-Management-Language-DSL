package Parser.ASTNodes.Conditions.Terms;

import Exceptions.FMLExecutionException;
import Parser.ASTNodes.ASTNode;

import java.math.BigDecimal;

public abstract class Term extends ASTNode {

    public boolean getBooleanValue() throws UnsupportedOperationException, FMLExecutionException {
        throw new UnsupportedOperationException("Term does not support boolean values");
    }

    public String getStringValue() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Term does not support string values");
    }

    public BigDecimal getNumericValue() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Term does not support numeric values");
    }
}
