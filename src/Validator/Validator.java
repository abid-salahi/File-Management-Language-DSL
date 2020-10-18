package Validator;

import Exceptions.FMLExecutionException;
import Exceptions.InvalidFMLException;
import Parser.ASTNodes.Program;
import Util.Logger;

/**
 * Validates a .fml program
 */
public class Validator {

    static Logger logger = Logger.get();

    /**
     * Validates the given fml program.
     * - If the program is valid, returns with no action.
     * - If the program is invalid, an exception is thrown with a message describing the problem.
     *
     * @param program the program to check
     * @throws InvalidFMLException if the program is invalid.
     */
    public static void validate(Program program) throws InvalidFMLException {
        logger.log("Starting script validation");
        try {
            program.validate(null);
            program.setValidated();
        } catch (InvalidFMLException | FMLExecutionException e) {
            System.out.println(e.getMessage());
        }
        logger.log("Script validation complete");
    }
}
