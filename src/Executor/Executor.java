package Executor;

import Exceptions.FMLExecutionException;
import Parser.ASTNodes.Program;
import Util.Logger;

/**
 * Executes an FML program. Any actions described by the FML script are triggered here.
 */
public class Executor {

    static Logger logger = Logger.get();

    /**
     * Executes the given program. Runtime exceptions may be thrown.
     *
     * @param program the valid program to execute.
     * @throws IllegalArgumentException if the program has not been validated
     */
    public static void execute(Program program) throws FMLExecutionException {
        if (!program.isValidated()) {
            throw new IllegalArgumentException("The program has not been validated. Will not execute.");
        }
        logger.log("Starting script execution");
        try {
            program.evaluate(null);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}
