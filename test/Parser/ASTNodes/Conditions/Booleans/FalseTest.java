package Parser.ASTNodes.Conditions.Booleans;

import Exceptions.InvalidFMLException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FalseTest {

    @Test
    void testTrue() throws InvalidFMLException {
        False falseNode = new False();
        falseNode.validate(null);
        falseNode.evaluate(null);
        assertFalse(falseNode.getResult());
    }
}
