package Main;

import Executor.Executor;
import Language.FMLGrammar;
import Parser.ASTNodes.Program;
import Parser.Parser;
import Tokenizer.Tokenizer;
import Util.FMLReader;
import Util.Logger;
import Validator.Validator;

import java.util.Queue;

/**
 * Entry-point to the File Management Language compiler and executor
 */
public class Main {

    static Logger logger = Logger.get();
    /**
     * Takes the path to a .fml file as an argument, parses it, and executes the FML script
     *
     * @param args path to .fml file to execute
     */
    public static void main(String[] args) {
        try {
            Logger.setStage(Logger.Stage.LOADING);
            String path = getFilePath(args);
            String data = FMLReader.read(path);
            logger.logSeparator();

            Logger.setStage(Logger.Stage.TOKENIZING);
            Queue<String> tokens = Tokenizer.getTokens(data, FMLGrammar.SEPARATORS);
            logger.logSeparator();

            Logger.setStage(Logger.Stage.PARSING);
            Program program = Parser.parse(tokens);
            logger.logSeparator();

            Logger.setStage(Logger.Stage.VALIDATING);
            Validator.validate(program);
            logger.logSeparator();

            Logger.setStage(Logger.Stage.EVALUATING);
            Executor.execute(program);
            logger.log("Done!");
        } catch (Exception e) {
            logger.log("Failed to run script: " + e.getMessage());
        }
    }

    /**
     * Validates the arguments and returns the path to the .fml file parsed from the arguments
     *
     * @param args args provided by user
     * @return path to .fml as provided by the user
     * @throws IllegalArgumentException if the provided args are invalid
     */
    private static String getFilePath(String[] args) throws IllegalArgumentException {
        String path = null;
        if (args.length == 1) {
            path = args[0];
            Logger.setEnabled(false);
        } else if (args.length == 2) {
            String flag = args[0];
            path = args[1];
            if (!flag.equals("-v")) {
                throw new IllegalArgumentException("Unexpected first argument: " + flag);
            }
            Logger.setEnabled(true);
        }
        logger.log("Parsing path to FML script");

        if (path != null) {
            logger.log("Identified path to script as " + path);
            return path;
        }
        throw new IllegalArgumentException("Invalid number of arguments - expected 1, found " + args.length);
    }
}
