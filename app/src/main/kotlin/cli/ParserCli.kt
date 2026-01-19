package cli


import collections.LookForwardIterator
import parser.ASTNode
import parser.Parser
import tokenizer.ParsedToken
import tokenizer.TokenType
import tokenizer.Tokenizer
import java.io.OutputStream
import java.io.PrintWriter

class ParserCli {
    companion object {
        fun parse(text: String, stdout: OutputStream, stderr: OutputStream) {
            val tok = Tokenizer.from(text)
            val lfi = LookForwardIterator.from(tok) { it.token != TokenType.SPACE && it.token != TokenType.NEWLINE && it.token!=TokenType.COMMENT }
            val ast = Parser(lfi).parseExpr()
            val pw = PrintWriter(stdout)
            ASTNode.printAst(ast, pw,0)
            pw.flush()
        }
    }
}