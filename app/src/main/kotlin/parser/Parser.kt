package parser

import collections.LookForwardIterator
import tokenizer.ParsedToken
import tokenizer.TokenType


sealed class ASTNode{
    class BinaryExp(val op: TokenType, val left: ASTNode, val right: ASTNode): ASTNode()
    class UnaryExp(val op: TokenType, val operand: ASTNode): ASTNode()
    class GroupingExp(val expression: ASTNode): ASTNode()
    class IdentifierExp(val identifier: String): ASTNode()
    class StringLiteral(val string: String): ASTNode()
    class BooleanLiteral(val value: Boolean): ASTNode()
    class NilLiteral: ASTNode()
    class NumberExp(val number: Double): ASTNode()
    
    companion object {
        fun printAst(node: ASTNode, writer: java.io.PrintWriter, indent: Int = 0) {
            val prefix = " ".repeat(indent)
            when (node) {
                is BinaryExp -> {
                    writer.print("${prefix}(${node.op.symbol}")
                    printAst(node.left, writer, 1)
                    printAst(node.right, writer, 1)
                    writer.print(")")
                }
                is UnaryExp -> {
                    writer.print("${prefix}(${node.op.symbol}")
                    printAst(node.operand, writer, 1)
                    writer.print(")")
                }
                is GroupingExp -> {
                    writer.print("${prefix}(group")
                    printAst(node.expression, writer, 1)
                    writer.print(")")
                }
                is IdentifierExp -> {
                    writer.print("${prefix}${node.identifier}")
                }
                is StringLiteral -> {
                    writer.print("${prefix}${node.string}")
                }
                is BooleanLiteral -> {
                    writer.print("${prefix}${node.value}")
                }
                is NilLiteral -> {
                    writer.print("${prefix}nil")
                }
                is NumberExp -> {
                    writer.print("${prefix}${node.number}")
                }
            }
        }
    }
}
class Parser(private val iter: LookForwardIterator<ParsedToken>){
    
    // 运算符优先级配置
    private val binaryPrecedenceMap = mapOf(
        TokenType.OR to 1,
        TokenType.AND to 2,
        TokenType.EQUAL_EQUAL to 3,
        TokenType.BANG_EQUAL to 3,
        TokenType.LESS to 4,
        TokenType.LESS_EQUAL to 4,
        TokenType.GREATER to 4,
        TokenType.GREATER_EQUAL to 4,
        TokenType.PLUS to 5,
        TokenType.MINUS to 5,       // 中缀减号优先级5
        TokenType.STAR to 6,        // 乘除优先级6
        TokenType.SLASH to 6,
        TokenType.MOD to 6,
    )
    // 一元运算符优先级
    private val unaryPrecedenceMap = mapOf(
        TokenType.MINUS to 7,       // 前缀负号优先级7（高于乘除）
        TokenType.PLUS to 7,
        TokenType.BANG to 7
    )

    // 运算符结合性（左结合）
    private val isLeftAssociative = setOf(
        TokenType.PLUS, TokenType.MINUS,
        TokenType.STAR, TokenType.SLASH, TokenType.MOD,
        TokenType.AND, TokenType.OR
    )
    fun parseExpr(): ASTNode {
        return parsePrecedence(0)
    }
    
    // 栈元素数据类，用于保存解析状态
    private data class ParseFrame(
        val left: ASTNode,
        val operator: TokenType,
        val minPrecedence: Int
    )
    
    /**
     * 使用栈实现的Pratt解析器
     * @param minPrecedence 当前允许的最小优先级
     */
    private fun parsePrecedence(minPrecedence: Int): ASTNode {
        val stack = mutableListOf<ParseFrame>()
        var current = parsePrefix()
        
        while (true) {
            // 检查下一个token是否为运算符
            if (!iter.hasNext()) break
            val token = iter.cur().token
            val precedence = binaryPrecedenceMap[token] ?: break
            
            // 如果优先级不够，需要回退到上一层
            if (precedence < minPrecedence) break

            // 入栈前先处理栈中可合并的表达式
            while (stack.isNotEmpty()) {
                val frame = stack.last()
                val framePrecedence = binaryPrecedenceMap[frame.operator] ?: 0

                // 左结合：当前优先级 <= 栈帧优先级 才合并
                // 右结合：当前优先级 < 栈帧优先级 才合并
                val shouldCollapse = if (isLeftAssociative.contains(frame.operator)) {
                    precedence <= framePrecedence
                } else {
                    precedence < framePrecedence
                }

                if (!shouldCollapse) break

                // 弹出栈帧并合并表达式
                stack.removeLast()
                current = ASTNode.BinaryExp(frame.operator, frame.left, current)
            }
            // 入栈
            stack.add(ParseFrame(current, token, minPrecedence))
            
            // 消费运算符并解析右操作数
            iter.moveNext()
            current = parsePrefix()
            
            // 处理栈中可以合并的表达式
            while (stack.isNotEmpty()) {
                val frame = stack.last()
                val framePrecedence = binaryPrecedenceMap[frame.operator] ?: 0
                
                // 计算下一优先级
                val nextPrecedence = framePrecedence + 1
                
                // 优先级足够，则退出
                if (precedence < nextPrecedence) break
                
                // 优先级不足够，弹出栈帧并合并表达式
                stack.removeLast()
                current = ASTNode.BinaryExp(token, frame.left, current)
            }
        }
        
        // 处理栈中剩余的所有表达式
        while (stack.isNotEmpty()) {
            val frame = stack.removeLast()
            current = ASTNode.BinaryExp(frame.operator, frame.left, current)
        }
        
        return current
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
                val precedence = unaryPrecedenceMap[token] ?: 0
                val operand = parsePrecedence(precedence)
                ASTNode.UnaryExp(token, operand)
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
                ASTNode.StringLiteral(stringValue.removeSurrounding("\""))
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
