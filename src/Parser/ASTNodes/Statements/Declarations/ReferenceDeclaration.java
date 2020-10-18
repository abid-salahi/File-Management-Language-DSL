package Parser.ASTNodes.Statements.Declarations;

import Exceptions.FMLExecutionException;
import Exceptions.InvalidFMLException;
import Parser.ASTNodes.Program;

import static Language.FMLGrammar.FILE_VARIABLE_IDENTIFIER;
import static Language.FMLGrammar.LIST_VARIABLE_IDENTIFIER;
import static Util.ObjectUtil.nullOrEqual;

public class ReferenceDeclaration extends DeclarationStatement {

    String leftIdentifier;        // string on the left side of the '='
    String rightIdentifier;       // variable name on the right hand side (i.e. the one that already exists)

    public ReferenceDeclaration(String leftIdentifier, String rightIdentifier) {
        this.leftIdentifier = leftIdentifier;
        this.rightIdentifier = rightIdentifier;
    }

    @Override
    public void validate(Program program) throws InvalidFMLException {
        if (leftIdentifier == null || rightIdentifier == null) {
            throw new InvalidFMLException("Incomplete declaration statement");
        }

        validateIdentifierFormats();
        if (!program.identifierIsDeclared(rightIdentifier)) {
            String message = String.format("Invalid variable declaration. Cannot use %s before declaration", rightIdentifier);
            throw new InvalidFMLException(message);
        }
        program.addIdentifierDeclaration(leftIdentifier);
        this.setValidated();
    }

    @Override
    public void evaluate(Program program) throws FMLExecutionException {
        program.addVariable(leftIdentifier, program.getVariable(rightIdentifier));
    }

    private void validateIdentifierFormats() throws InvalidFMLException {
        boolean leftIdentifierIsList = LIST_VARIABLE_IDENTIFIER.matcher(leftIdentifier).matches();
        boolean leftIdentifierIsFile = FILE_VARIABLE_IDENTIFIER.matcher(leftIdentifier).matches();
        boolean rightIdentifierIsList = LIST_VARIABLE_IDENTIFIER.matcher(rightIdentifier).matches();
        boolean rightIdentifierIsFile = FILE_VARIABLE_IDENTIFIER.matcher(rightIdentifier).matches();


        if (leftIdentifierIsFile == leftIdentifierIsList) {
            throw new InvalidFMLException("Invalid reference declaration. Variable name is invalid: " + leftIdentifier);
        }
        if (rightIdentifierIsFile == rightIdentifierIsList) {
            throw new InvalidFMLException("Invalid reference declaration. Variable name is invalid: " + rightIdentifier);
        }
        if (rightIdentifierIsList ^ leftIdentifierIsList) {
            throw new InvalidFMLException("Invalid reference declaration. Cannot equate list and file variables.");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ReferenceDeclaration)) {
            return false;
        }

        ReferenceDeclaration other = (ReferenceDeclaration) obj;
        return nullOrEqual(this.leftIdentifier, other.leftIdentifier) && nullOrEqual(this.rightIdentifier, other.rightIdentifier);
    }

    @Override
    public void reset() throws FMLExecutionException {
        // Nothing to do here
    }

    @Override
    public String toString() {
        return String.format("%s = %s", leftIdentifier, rightIdentifier);
    }
}
