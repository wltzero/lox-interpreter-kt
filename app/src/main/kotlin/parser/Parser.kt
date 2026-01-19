package parser

import collections.LookForwardIterator
import tokenizer.ParsedToken
import tokenizer.TokenType


sealed class ASTNode{
    class BinaryExp(val op: String, val left: ASTNode, val right: ASTNode): ASTNode()
    class UnaryExp(val op: String, val operand: ASTNode): ASTNode()
    class GroupingExp(val expression: ASTNode): ASTNode()
    class IdentifierExp(val identifier: String): ASTNode()
    class StringLiteral(val string: String): ASTNode()
    class BooleanLiteral(val value: Boolean): ASTNode()
    class NilLiteral: ASTNode()
    class NumberExp(val number: Double): ASTNode()
    
    companion object {
        fun printAst(node: ASTNode, writer: java.io.PrintWriter, indent: Int = 0) {
            val prefix = "  ".repeat(indent)
            when (node) {
                is BinaryExp -> {
                    writer.println("${prefix}(${node.op}")
                    printAst(node.left, writer, indent + 1)
                    printAst(node.right, writer, indent + 1)
                    writer.println("${prefix})")
                }
                is UnaryExp -> {
                    writer.println("${prefix}(${node.op}")
                    printAst(node.operand, writer, indent + 1)
                    writer.println("${prefix})")
                }
                is GroupingExp -> {
                    writer.println("${prefix}(group")
                    printAst(node.expression, writer, indent + 1)
                    writer.println("${prefix})")
                }
                is IdentifierExp -> {
                    writer.println("${prefix}${node.identifier}")
                }
                is StringLiteral -> {
                    writer.println("${prefix}\"${node.string}\"")
                }
                is BooleanLiteral -> {
                    writer.println("${prefix}${node.value}")
                }
                is NilLiteral -> {
                    writer.println("${prefix}nil")
                }
                is NumberExp -> {
                    writer.println("${prefix}${node.number}")
                }
            }
        }
    }
}
class Parser(private val iter: LookForwardIterator<ParsedToken>){
    
    // 运算符优先级配置
    private val precedenceMap = mapOf(
        TokenType.OR to 1,
        TokenType.AND to 2,
        TokenType.EQUAL_EQUAL to 3,
        TokenType.BANG_EQUAL to 3,
        TokenType.LESS to 4,
        TokenType.LESS_EQUAL to 4,
        TokenType.GREATER to 4,
        TokenType.GREATER_EQUAL to 4,
        TokenType.PLUS to 5,
        TokenType.MINUS to 5,
        TokenType.STAR to 6,
        TokenType.SLASH to 6
    )
    
    // 运算符名称映射
    private val operatorNames = mapOf(
        TokenType.OR to "or",
        TokenType.AND to "and",
        TokenType.EQUAL_EQUAL to "equal",
        TokenType.BANG_EQUAL to "not_equal",
        TokenType.LESS to "less",
        TokenType.LESS_EQUAL to "less_equal",
        TokenType.GREATER to "greater",
        TokenType.GREATER_EQUAL to "greater_equal",
        TokenType.PLUS to "plus",
        TokenType.MINUS to "minus",
        TokenType.STAR to "mul",
        TokenType.SLASH to "div",
        TokenType.BANG to "bang"
    )
    
    fun parseExpr(): ASTNode {
        return parsePrecedence(0)
    }
    
    /**
     * 处理二元运算符
     * @param minPrecedence 当前允许的最小优先级
     */
    private fun parsePrecedence(minPrecedence: Int): ASTNode {
        var left = parsePrefix()
        
        while (true) {
            // 检查下一个token是否为运算符
            if (!iter.hasNext()) break
            val token = iter.cur().token
            val precedence = precedenceMap[token] ?: break
            
            // 如果优先级不够，停止解析
            if (precedence < minPrecedence) break
            
            // 左结合性：使用 precedence + 1 确保左结合
            val nextPrecedence = precedence + 1
            
            iter.moveNext() // 消费运算符
            val right = parsePrecedence(nextPrecedence)
            val opName = operatorNames[token] ?: token.name.lowercase()
            left = ASTNode.BinaryExp(opName, left, right)
        }
        
        return left
    }
    
    /**
     * 解析前缀表达式（一元运算符和基本元素）
     */
    private fun parsePrefix(): ASTNode {
        if (!iter.hasNext()) {
            throw RuntimeException("Unexpected end of input")
        }
        
        val token = iter.cur().token
        val value = iter.cur().value
        val stringValue = iter.cur().stringValue
        
        return when (token) {
            // 一元运算符
            TokenType.PLUS, TokenType.MINUS, TokenType.BANG -> {
                iter.moveNext()
                val precedence = precedenceMap[token] ?: 0
                val operand = parsePrecedence(precedence)
                val opName = operatorNames[token] ?: token.name.lowercase()
                ASTNode.UnaryExp(opName, operand)
            }
            // 字面量
            TokenType.TRUE -> {
                iter.moveNext()
                ASTNode.BooleanLiteral(true)
            }
            TokenType.FALSE -> {
                iter.moveNext()
                ASTNode.BooleanLiteral(false)
            }
            TokenType.NIL -> {
                iter.moveNext()
                ASTNode.NilLiteral()
            }
            TokenType.NUMBER -> {
                iter.moveNext()
                ASTNode.NumberExp(value as Double)
            }
            TokenType.STRING -> {
                iter.moveNext()
                ASTNode.StringLiteral(stringValue)
            }
            TokenType.IDENTIFIER -> {
                iter.moveNext()
                ASTNode.IdentifierExp(stringValue)
            }
            TokenType.LEFT_PAREN -> {
                iter.moveNext()
                val expr = parseExpr()
                consume(TokenType.RIGHT_PAREN, "Expected ')' after expression")
                ASTNode.GroupingExp(expr)
            }
            else -> {
                throw RuntimeException("Unexpected token: ${token}")
            }
        }
    }
    
    private fun consume(type: TokenType, message: String) {
        if (iter.hasNext() && iter.cur().token == type) {
            iter.moveNext()
        } else {
            throw RuntimeException(message)
        }
    }
}
