package Parser.ASTNodes.Statements.Declarations.Paths;

import Exceptions.FMLExecutionException;
import Exceptions.InvalidFMLException;
import Parser.ASTNodes.ASTNode;
import Parser.ASTNodes.Program;
import Parser.ASTNodes.Variables.FileVariable;

import java.nio.file.Path;
import java.nio.file.Paths;

import static Language.FMLGrammar.WILDCARD_PATH;
import static Util.ObjectUtil.nullOrEqual;
import static Util.StringUtil.removeEscapedQuotes;

public class FMLPath extends ASTNode {

    /**
     * Absolute or relative path
     */
    String path;

    /**
     * FMLPath is relative to this file variable
     */
    String rootVarIdentifier;    // may be null

    /**
     * Built at evaluation time
     */
    String absolutePath;

    public FMLPath(String path) {
        this.path = path;
    }

    public FMLPath(String rootVariableIdentifier, String relativePath) {
        this.rootVarIdentifier = rootVariableIdentifier;
        this.path = removeEscapedQuotes(relativePath);
    }

    /**
     * @return true if this path contains a wildcard
     */
    public boolean isWildCardPath() {
        return WILDCARD_PATH.matcher(this.path).matches();
    }

    /**
     * @return the absolute path represented by this FMLPath object
     */
    public String getAbsolutePath() {
        if (this.absolutePath == null) {
            throw new IllegalStateException("Cannot access absolute path before calling evaluate");
        }
        return this.absolutePath;
    }

    @Override
    public void validate(Program program) throws InvalidFMLException {
        if (path == null) {
            throw new InvalidFMLException("Incomplete path declaration");
        }
        if (rootVarIdentifier != null && !program.identifierIsDeclared(rootVarIdentifier)) {
            String message = String.format("Cannot use variable %s before declaration", rootVarIdentifier);
            throw new InvalidFMLException(message);
        }
        this.setValidated();
    }

    @Override
    public void evaluate(Program program) throws FMLExecutionException {
        String rootPath = null;
        if (rootVarIdentifier != null) {
            FileVariable fileRoot = program.getFileVariable(rootVarIdentifier);
            if (!fileRoot.isDirectory()) {
                throw new FMLExecutionException("The root of a relative path cannot be a file.");
            }
            rootPath = fileRoot.getAbsolutePath();
        }
        try {
            this.absolutePath = buildAbsPath(rootPath, this.path);
        } catch (Exception e) {
            throw new FMLExecutionException("Failed to build path", e);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof FMLPath)) {
            return false;
        }
        FMLPath other = (FMLPath) obj;
        return nullOrEqual(this.path, other.path)
                && nullOrEqual(this.rootVarIdentifier, other.rootVarIdentifier)
                && nullOrEqual(this.absolutePath, other.absolutePath);
    }

    @Override
    public void reset() throws FMLExecutionException {
        absolutePath = null;
    }

    private String buildAbsPath(String rootPath, String relPath) {
        // "*" is an illegal path character so we need to replace it
        // to be able to use java.nio.Path to join the paths and then put it back
        String asteriskReplacement = "ASTERISK_SYMBOL";
        relPath = relPath.replaceAll("\\*", asteriskReplacement);

        Path nioPath = rootPath == null ? Paths.get(relPath) : Paths.get(rootPath, relPath);
        return nioPath.toString().replaceAll(asteriskReplacement, "*");
    }

    @Override
    public String toString() {
        if (rootVarIdentifier == null) return this.path;
        return String.format("%s -> %s", rootVarIdentifier, this.path);
    }
}
