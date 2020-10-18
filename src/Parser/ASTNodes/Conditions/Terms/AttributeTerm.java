package Parser.ASTNodes.Conditions.Terms;

import Exceptions.FMLExecutionException;
import Exceptions.InvalidFMLException;
import Language.FMLGrammar.AttributeName;
import Parser.ASTNodes.Program;
import Parser.ASTNodes.Variables.FileVariable;

import java.math.BigDecimal;
import java.math.BigInteger;

import static Util.ObjectUtil.nullOrEqual;

public class AttributeTerm extends Term {

    private String fileVarIdentifier;
    private AttributeName attributeName;
    private Object result;

    public AttributeTerm(String fileVarIdentifier, AttributeName attributeName) {
        this.fileVarIdentifier = fileVarIdentifier;
        this.attributeName = attributeName;
    }

    public Object getResult() {
        return result;
    }

    @Override
    public boolean getBooleanValue() throws UnsupportedOperationException {
        if (attributeName == AttributeName.IS_DIRECTORY ||attributeName == AttributeName.IS_FILE){
            return (boolean)result;
        }
        throw new UnsupportedOperationException(
                String.format("Expected an attributeName of either IS_FILE " +
                        "or IS_DIRECTORY, received one of type: %s", attributeName));

    }

    @Override
    public String getStringValue() throws UnsupportedOperationException {
        if (attributeName == AttributeName.NAME ||attributeName == AttributeName.EXTENSION ){
            return (String) result;
        }
        throw new UnsupportedOperationException(
                String.format("Expected an attributeName of either NAME " +
                        "or NAME, received one of type: %s", attributeName));
    }

    @Override
    public BigDecimal getNumericValue() throws UnsupportedOperationException {
        if (attributeName == AttributeName.CREATED ||attributeName == AttributeName.MODIFIED ||
                attributeName == AttributeName.SIZE     ) {
            return new BigDecimal((BigInteger)result);
        }
        throw new UnsupportedOperationException(
                String.format("Expected an attributeName of either CREATED, " +
                        "MODIFIED or SIZE; received one of type: %s", attributeName));
    }

    public FileVariable getParentValue() throws UnsupportedOperationException {
        if (attributeName == AttributeName.PARENT){
            return (FileVariable) result;
        }
        throw new UnsupportedOperationException(String.format("Expected an attributeName of PARENT," +
                " received one of type: %s", attributeName));

    }

    @Override
    public void validate(Program program) throws InvalidFMLException {
        if (attributeName == null){
            throw new InvalidFMLException("Attribute is null");
        } else if (fileVarIdentifier == null){
            throw new InvalidFMLException("File is null");
        } else if (!program.identifierIsDeclared(fileVarIdentifier)){
            throw new InvalidFMLException("File variable identifier is not declared in program");
        }
    }

    @Override
    public void evaluate(Program program) throws FMLExecutionException {
        FileVariable file = program.getFileVariable(fileVarIdentifier);
        switch (attributeName){
            case CREATED:
                result = file.getTimeCreated();
                break;
            case EXTENSION:
                result = file.getExtension();
                break;
            case IS_DIRECTORY:
                result = file.isDirectory();
                break;
            case IS_FILE:
                result = file.isFile();
                break;
            case MODIFIED:
                result = file.getTimeModified();
                break;
            case NAME:
                result = file.getName();
                break;
            case PARENT:
                result = file.getParent();
                break;
            case SIZE:
                result = file.getSize();
                break;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof AttributeTerm)) {
            return false;
        }
        AttributeTerm other = (AttributeTerm) obj;
        return nullOrEqual(this.fileVarIdentifier, other.fileVarIdentifier)
                && nullOrEqual(this.attributeName, other.attributeName);
    }

    @Override
    public void reset() {
        result = null;
    }
}
