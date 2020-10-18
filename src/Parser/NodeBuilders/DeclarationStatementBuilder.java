package Parser.NodeBuilders;

import Exceptions.FMLParsingException;
import Parser.ASTNodes.Statements.Declarations.DeclarationStatement;
import Parser.ASTNodes.Statements.Declarations.DirectDeclaration;
import Parser.ASTNodes.Statements.Declarations.Paths.FMLPath;
import Parser.ASTNodes.Statements.Declarations.ReferenceDeclaration;

import java.util.Deque;

import static Language.FMLGrammar.RIGHT_ARROW;
import static Language.FMLGrammar.USER_DEFINED_STRING_REGEX;
import static Util.StringUtil.removeEscapedQuotes;

public class DeclarationStatementBuilder {

    public static DeclarationStatement build(Deque<String> declarationTokens) throws FMLParsingException {
        String leftIdentifier = declarationTokens.poll();
        declarationTokens.remove(); // ignore "="
        String rightIdentifier = declarationTokens.poll();

        if (rightIdentifier == null) {
            throw new FMLParsingException("Invalid declaration - missing right hand side.");
        }

        FMLPath path = null;
        if (USER_DEFINED_STRING_REGEX.matcher(rightIdentifier).matches()) {                                  // if it's a string
            path = new FMLPath(removeEscapedQuotes(rightIdentifier));
        } else if (!declarationTokens.isEmpty() && declarationTokens.poll().equals(RIGHT_ARROW)) {     // it's a arrow declaration
            String pathString = declarationTokens.poll();
            path = new FMLPath(removeEscapedQuotes(rightIdentifier), pathString);
        }

        if (path != null) {
            return new DirectDeclaration(leftIdentifier, path);
        }
        return new ReferenceDeclaration(leftIdentifier, rightIdentifier);                               // it's a reference declaration
    }
}
