package Parser.NodeBuilders;

import Exceptions.FMLParsingException;
import Language.FMLGrammar;
import Parser.ASTNodes.Program;
import Parser.ASTNodes.Statements.ForLoop;

import java.util.Deque;

import static Parser.NodeBuilders.BuilderUtils.getBlockTokens;
import static Parser.Parser.parse;

public class ForLoopBuilder {

    public static ForLoop build(Deque<String> forTokens) throws FMLParsingException {

        forTokens.remove(); // ignore "for"

        if (!forTokens.poll().equals("("))
            throw new FMLParsingException("For statement missing opening brackets");

        String loopIdentifier = forTokens.poll();

        if (!forTokens.poll().equalsIgnoreCase(FMLGrammar.IN))
            throw new FMLParsingException("For variables not separated by 'in'.");

        String targetCollectionIdentifier = forTokens.poll();

        if (!targetCollectionIdentifier.endsWith(FMLGrammar.LIST_SYMBOL))
            throw new FMLParsingException("Proper list variable not used.");

        if (!forTokens.poll().equals(")"))
            throw new FMLParsingException("For statement missing closing brackets");

        Deque<String> programTokens = getBlockTokens(forTokens);

        Program forProgram = parse(programTokens);

        return new ForLoop(targetCollectionIdentifier, forProgram, loopIdentifier);
    }
}
