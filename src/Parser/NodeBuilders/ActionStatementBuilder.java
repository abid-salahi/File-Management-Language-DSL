package Parser.NodeBuilders;

import Exceptions.FMLParsingException;
import Language.FMLGrammar.CommandEnum;
import Parser.ASTNodes.Commands.*;
import Parser.ASTNodes.Statements.ActionStatement;

import static Language.FMLGrammar.USER_DEFINED_STRING_REGEX;
import static Util.StringUtil.removeEscapedQuotes;

public class ActionStatementBuilder {

    public static ActionStatement build(String commandString, String targetIdentifier, String destination) throws FMLParsingException {
        boolean destinationIsString = destination != null && USER_DEFINED_STRING_REGEX.matcher(destination).matches();
        if (destinationIsString) {
            destination = removeEscapedQuotes(destination);
        }
        Command command = getCommand(commandString, targetIdentifier, destination);
        return new ActionStatement(command, targetIdentifier, destination, destinationIsString);
    }

    private static Command getCommand(String commandString, String target, String dest) throws FMLParsingException {
        CommandEnum commandEnum = CommandEnum.fromString(commandString);
        if (commandEnum == null) {
            throw new FMLParsingException(commandString + " is not a valid command");
        }
        switch (commandEnum) {
            case COPY:
                return new Copy(target, dest);
            case MOVE:
                return new Move(target, dest);
            case DELETE:
                return new Delete(target);
            case RENAME:
                return new Rename(target, dest);
            case CREATE:
                return new Create(target);
            case COMPRESS:
                return new Compress(target);
        }
        throw new FMLParsingException(commandString + " is not a valid command");
    }
}
