package Parser.ASTNodes;

import Exceptions.FMLExecutionException;
import Exceptions.InvalidFMLException;
import Parser.ASTNodes.Statements.Statement;
import Parser.ASTNodes.Variables.FileVariable;
import Parser.ASTNodes.Variables.ListVariable;
import Parser.ASTNodes.Variables.Variable;

import java.util.*;

import static Util.ObjectUtil.nullOrEqual;

/**
 * Represents an FML program in AST format
 */
public class Program extends ASTNode {

    private List<Statement> statements;
    private Set<String> identifiers;                 // identifiers declared. Used at validation time.
    private Map<String, Variable> variables;         // Map<identifier, Variable object>. Used at evaluation time.
    private Program parent;
    private boolean terminate = false;

    private Integer line;

    /**
     * Initialize the program object
     *
     * @param statements statements that form this program
     */
    public Program(List<Statement> statements) {
        this.identifiers = new HashSet<>();
        this.variables = new HashMap<>();
        this.statements = statements;
    }

    public Integer getLine() {
        return line;
    }

    public void setLine(Integer line) {
        this.line = line;
    }

    /**
     * Get the statements of this program
     *
     * @return a list of statements in order
     */
    public List<Statement> getStatements() {
        return this.statements;
    }

    /**
     * Adds the identifier to the set of declared identifiers
     *
     * @param identifier variable identifier/name to add
     */
    public void addIdentifierDeclaration(String identifier) {
        this.identifiers.add(identifier);
    }

    /**
     * @return true if the given identifier is already declared
     */
    public boolean identifierIsDeclared(String identifier) {
        return this.identifiers.contains(identifier) || (parent != null && parent.identifierIsDeclared(identifier));
    }

    /**
     * Get the variables within the top-level scope of this program
     * as a map of Variable
     *
     * @return a map of the variables in this programs outer scope
     */
    public Map<String, Variable> getVariables() {
        return variables;
    }

    /**
     * Get the variable associated with the given name/identifier.
     *
     * @param identifier identifier associated with variable
     * @return the associated Variable object
     * @throws FMLExecutionException if there is no variable with the given identifier
     */
    public Variable getVariable(String identifier) throws FMLExecutionException {
        if (variables.containsKey(identifier)) {
            return variables.get(identifier);
        } else if (parent != null) {
            return parent.getVariable(identifier);
        }
        throw new FMLExecutionException("Variable " + identifier + " accessed before initialization");
    }

    /**
     * Get the FileVariable associated with the given name/identifier
     *
     * @param identifier identifier associated with the variable
     * @return the associated FileVariable object
     * @throws FMLExecutionException if there is no variable, or variable is not FileVariable
     */
    public FileVariable getFileVariable(String identifier) throws FMLExecutionException {
        Variable var = getVariable(identifier);
        if (!(var instanceof FileVariable)) {
            throw new FMLExecutionException("Expected " + identifier + " to be file/folder variable");
        }
        return (FileVariable) var;
    }

    /**
     * Get the ListVariable associated with the given name/identifier
     *
     * @param identifier identifier associated with the variable
     * @return the associated ListVariable object
     * @throws FMLExecutionException if there is no variable, or variable is not ListVariable
     */
    public ListVariable getListVariable(String identifier) throws FMLExecutionException {
        Variable var = getVariable(identifier);
        if (!(var instanceof ListVariable)) {
            throw new FMLExecutionException("Expected " + identifier + " to be list variable");
        }
        return (ListVariable) var;
    }

    /**
     * Add a variable to this program's outer scope.
     * If a variable with the same identifier already exists, it will be replaced.
     *
     * @param identifier the identifier used to declare the variable
     * @param variable   variable to add to scope
     */
    public void addVariable(String identifier, Variable variable) {
        variables.put(identifier, variable);
    }

    /**
     * Validates this program.
     *
     * @throws InvalidFMLException if the program is found to be invalid
     */
    @Override
    public void validate(Program program) throws InvalidFMLException, FMLExecutionException {
        this.parent = program;
        line = parent == null ? 0 : parent.getLine();
        for (Statement s : this.statements) {
            line++;
            logger.log(s.toString(), line);
            s.validate(this);
        }
        if (parent != null) {
            parent.setLine(line);
        }
        this.setValidated();
    }

    /**
     * Evaluates the program by evaluating all the statements that
     * are within this program in order.
     */
    @Override
    public void evaluate(Program program) throws FMLExecutionException {
        this.parent = program;
        line = parent == null ? 0 : parent.getLine();
        for (Statement s : this.statements) {
            line++;
            logger.log(s.toString(), line);
            if (terminate) {
                break;
            }
            s.evaluate(this);
        }
        if (parent != null) {
            parent.setLine(line);
        }
    }

    /**
     * Resets the program runtime state
     */
    public void reset() {
        this.variables.clear();
        // TODO: reset all statements
    }

    public void terminate() {
        this.terminate = true;
        if (parent != null) parent.terminate();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Program)) {
            return false;
        }
        Program other = (Program) obj;
        return nullOrEqual(this.statements, other.statements);
    }
}
