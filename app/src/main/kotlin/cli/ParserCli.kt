package cli


import collections.LookForwardIterator
import exception.ParserException
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
            // 检查是否还有未消费的 token（除了分号和 EOF）
            while (lfi.hasNext() && lfi.cur().token != TokenType.EOF) {
                if (lfi.cur().token == TokenType.SEMICOLON) {
                    lfi.moveNext()
                    continue
                }
                throw ParserException("Unexpected token: ${lfi.cur().token}")
            }
            val pw = PrintWriter(stdout)
            ASTNode.printAst(ast, pw,0)
            pw.flush()
        }
    }
}