package Parser.ASTNodes.Statements;

import Exceptions.FMLExecutionException;
import Exceptions.InvalidFMLException;
import Parser.ASTNodes.Commands.Command;
import Parser.ASTNodes.Program;

import static Util.ObjectUtil.nullOrEqual;

/**
 * A statement that performs a file system action
 */
public class ActionStatement extends Statement {

    /* COMMAND TARGET (PREPOSITION DESTINATION)? */

    /**
     * The command associated with the action
     */
    private Command command;

    /**
     * File on which action will take place
     */
    private String targetIdentifier;     // initialized at parse time. Get the actual FileVariable at runtime.

    /**
     * Either a file or string destination for the action.
     * One of these will be null.
     */
    private String stringDestination;
    private String fileDestinationIdentifier;     // initialized at parse time. Get the actual FileVariable at runtime.

    public ActionStatement(Command command, String targetIdentifier, String destination, boolean destinationIsString) {
        this.command = command;
        this.targetIdentifier = targetIdentifier;
        if (destinationIsString) {
            this.stringDestination = destination;
        } else {
            this.fileDestinationIdentifier = destination;
        }
    }

    @Override
    public void validate(Program program) throws InvalidFMLException, FMLExecutionException {
        command.validate(program);
        this.setValidated();
    }

    /**
     * Performs the action associated with this action statement
     */
    @Override
    public void evaluate(Program program) throws FMLExecutionException {
        command.evaluate(program);
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ActionStatement)) {
            return false;
        }

        ActionStatement other = (ActionStatement) object;
        return nullOrEqual(this.command, other.command)
                && nullOrEqual(this.targetIdentifier, other.targetIdentifier)
                && nullOrEqual(this.fileDestinationIdentifier, other.fileDestinationIdentifier)
                && nullOrEqual(this.stringDestination, other.stringDestination);
    }

    @Override
    public void reset() throws FMLExecutionException {
        command.reset();
    }

    @Override
    public String toString() {
        return command.toString();
    }
}
