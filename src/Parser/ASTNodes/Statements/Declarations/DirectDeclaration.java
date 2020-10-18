package Parser.ASTNodes.Statements.Declarations;

import Exceptions.FMLExecutionException;
import Exceptions.InvalidFMLException;
import Parser.ASTNodes.Program;
import Parser.ASTNodes.Statements.Declarations.Paths.FMLPath;
import Parser.ASTNodes.Variables.FileVariable;
import Parser.ASTNodes.Variables.ListVariable;

import static Language.FMLGrammar.FILE_VARIABLE_IDENTIFIER;
import static Language.FMLGrammar.LIST_VARIABLE_IDENTIFIER;
import static Util.ObjectUtil.nullOrEqual;

public class DirectDeclaration extends DeclarationStatement {

    private FMLPath path;
    private String identifier;

    public DirectDeclaration(String identifier, FMLPath path) {
        this.identifier = identifier;
        this.path = path;
    }

    @Override
    public void validate(Program program) throws InvalidFMLException {
        if (path == null || identifier == null) {
            throw new InvalidFMLException("Incomplete declaration statement");
        }
        path.validate(program);
        validateIdentifierFormat();
        program.addIdentifierDeclaration(identifier);
        this.setValidated();
    }

    @Override
    public void evaluate(Program program) throws FMLExecutionException {
        path.evaluate(program);
        boolean identifierIsList = LIST_VARIABLE_IDENTIFIER.matcher(identifier).matches();
        if (identifierIsList) {
            program.addVariable(identifier, new ListVariable(path.getAbsolutePath()));
        } else {
            program.addVariable(identifier, new FileVariable(path.getAbsolutePath()));
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof DirectDeclaration)) {
            return false;
        }

        DirectDeclaration other = (DirectDeclaration) obj;
        return nullOrEqual(this.path, other.path) && nullOrEqual(this.identifier, other.identifier);
    }

    private void validateIdentifierFormat() throws InvalidFMLException {
        boolean identifierIsList = LIST_VARIABLE_IDENTIFIER.matcher(identifier).matches();
        boolean identifierIsFile = FILE_VARIABLE_IDENTIFIER.matcher(identifier).matches();
        if (identifierIsFile == identifierIsList) {
            throw new InvalidFMLException("Invalid declaration. Variable name is invalid: " + identifier);
        }
        if (path.isWildCardPath() && !identifierIsList) {
            throw new InvalidFMLException("Invalid identifier for list variable - must end in []");
        }
    }

    @Override
    public void reset() throws FMLExecutionException {
        path.reset();
    }

    @Override
    public String toString() {
        return String.format("%s = %s", identifier, path.toString());
    }
}
