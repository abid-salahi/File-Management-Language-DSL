package Parser.ASTNodes.Commands;

import Parser.ASTNodes.ASTNode;
import Util.Logger;

import static Util.ObjectUtil.nullOrEqual;

public abstract class Command extends ASTNode {

    protected String targetIdentifier;
    protected String destination;

    public Command(String targetIdentifier, String destination) {
        this.targetIdentifier = targetIdentifier;
        this.destination = destination;
    }

    public String getTargetIdentifier() {
        return targetIdentifier;
    }

    public String getDestination() {
        return destination;
    }

    @Override
    public boolean equals(Object obj) {
        if (!this.getClass().isInstance(obj)) {
            return false;
        }
        Command other = (Command) obj;
        return nullOrEqual(this.targetIdentifier, other.targetIdentifier)
                && nullOrEqual(this.destination, other.destination);
    }

    @Override
    public void reset() {
        // nothing to do here
    }
}
