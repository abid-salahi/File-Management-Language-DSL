package Parser.ASTNodes.Commands;

import Exceptions.FMLExecutionException;
import Exceptions.InvalidFMLException;
import Parser.ASTNodes.Program;

public class Move extends Command {

    public Move(String target, String destination) {
        super(target, destination);
    }

    /**
     * Moves the target file to the destination path.
     * Must set target and destination first.
     */
    @Override
    public void evaluate(Program program) throws FMLExecutionException {
        logger.log("Executing Move as Copy + Delete");
        try {
            Copy cp = new Copy(this.targetIdentifier, this.destination);
            Delete del = new Delete(this.targetIdentifier);
            cp.evaluate(program);
            del.evaluate(program);
        } catch (FMLExecutionException e) {
            throw new FMLExecutionException("API failed to execute move " + this.targetIdentifier + " to " + this.destination, e);
        }
    }

    @Override
    public void validate(Program program) throws InvalidFMLException, FMLExecutionException {
        if (!(this.targetIdentifier != null &&
                this.destination != null &&
                program.identifierIsDeclared(this.targetIdentifier) &&
                program.identifierIsDeclared(this.destination))) {
            throw new InvalidFMLException("Invalid Move Command structure! Cannot validate move" + this.targetIdentifier + " to " + this.destination);
        }
    }

    @Override
    public boolean equals(Object obj) {
        return (this.getClass().isInstance(obj) && super.equals(obj));
    }

    @Override
    public String toString() {
        return "move " + targetIdentifier + " to " + destination;
    }
}
