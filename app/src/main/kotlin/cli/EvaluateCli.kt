package cli

import collections.LookForwardIterator
import evaluator.EvaluateVisitor
import parser.ASTNode
import parser.Parser
import tokenizer.TokenType
import tokenizer.Tokenizer
import java.io.OutputStream
import java.io.PrintWriter

class EvaluateCli {
    companion object{
        fun evaluate(text: String, stdout: OutputStream, stderr: OutputStream){
            val tok = Tokenizer.from(text)
            val lfi = LookForwardIterator.from(tok) { it.token != TokenType.SPACE && it.token != TokenType.NEWLINE && it.token!=TokenType.COMMENT }
            val ast = Parser(lfi).parseExpr()

            val res = EvaluateVisitor().evaluate(ast).toString()
            println(res)

        }
    }
}