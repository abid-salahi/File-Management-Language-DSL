package Parser.ASTNodes.Conditions.Terms;

import Exceptions.InvalidFMLException;
import Parser.ASTNodes.Program;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class StringTermTest {
    private StringTerm valid = new StringTerm("hello");
    private StringTerm emptyvalid = new StringTerm("");
    private StringTerm invalid = new StringTerm(null);

    @Test
    void testGetStringValue(){
        assertEquals("hello", valid.getStringValue());
        assertEquals("", emptyvalid.getStringValue());
    }

    @Test
    void testValidate(){
        Program p = new Program(new ArrayList<>());
        InvalidFMLException fileEx = assertThrows(InvalidFMLException.class,
                ()->invalid.validate(p));

        try {
            valid.validate(p);
            emptyvalid.validate(p);
        } catch (InvalidFMLException e) {
            fail("Did not expect an exception to be thrown when validating String Term");
        }
    }
}
