package Parser.ASTNodes.Conditions.Booleans;

import Exceptions.InvalidFMLException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TrueTest {

    @Test
    void testTrue() throws InvalidFMLException {
        True trueNode = new True();
        trueNode.validate(null);
        trueNode.evaluate(null);
        assertTrue(trueNode.getResult());
    }
}
