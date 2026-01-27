package cli

import collections.LookForwardIterator
import evaluator.EvaluateVisitor
import parser.Parser
import resolver.Resolver
import statement.StatementVisitor
import tokenizer.TokenType
import tokenizer.Tokenizer
import java.io.OutputStream

class StatementCli {
    companion object{
        fun doRun(text: String, stdout: OutputStream, stderr: OutputStream){
            val tok = Tokenizer.from(text)
            val lfi = LookForwardIterator.from(tok) { it.token != TokenType.SPACE && it.token != TokenType.NEWLINE && it.token!=TokenType.COMMENT }
            val stmts = Parser(lfi).parseStmts()

            val resolver = Resolver()
            resolver.resolve(stmts)
            EvaluateVisitor.setLocals(resolver.getLocals())

            StatementVisitor.run(stmts)
        }
    }
}
