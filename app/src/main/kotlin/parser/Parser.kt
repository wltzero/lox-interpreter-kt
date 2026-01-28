package parser

import collections.LookForwardIterator
import evaluator.LiteralValue
import exception.ParserException
import tokenizer.ParsedToken
import tokenizer.TokenType


sealed class ASTNode {
    sealed class Expr : ASTNode() {
        abstract fun accept(visitor: ExprVisitor<LiteralValue>): LiteralValue

        //二元表达式节点
        class BinaryExp(val op: TokenType, val left: Expr, val right: Expr) : Expr() {
            override fun accept(visitor: ExprVisitor<LiteralValue>): LiteralValue {
                return visitor.visitBinaryExp(this)
            }
        }

        //一元表达式节点
        class UnaryExp(val op: TokenType, val operand: Expr) : Expr() {
            override fun accept(visitor: ExprVisitor<LiteralValue>): LiteralValue {
                return visitor.visitUnaryExp(this)
            }
        }

        //分组/括号表达式节点
        class GroupingExp(val expression: Expr) : Expr() {
            override fun accept(visitor: ExprVisitor<LiteralValue>): LiteralValue {
                return visitor.visitGroupingExp(this)
            }
        }

        class IdentifyExp(val identifier: String, val line: Int = 0) : Expr() {
            override fun accept(visitor: ExprVisitor<LiteralValue>): LiteralValue {
                return visitor.visitIdentifyExp(this)
            }
        }

        class StringLiteral(val string: String) : Expr() {
            override fun accept(visitor: ExprVisitor<LiteralValue>): LiteralValue {
                return visitor.visitStringLiteral(this)
            }
        }

        class BooleanLiteral(val value: Boolean) : Expr() {
            override fun accept(visitor: ExprVisitor<LiteralValue>): LiteralValue {
                return visitor.visitBooleanLiteral(this)
            }
        }

        class NilLiteral : Expr() {
            override fun accept(visitor: ExprVisitor<LiteralValue>): LiteralValue {
                return visitor.visitNilLiteral(this)
            }
        }

        class DoubleLiteral(val number: Double) : Expr() {
            override fun accept(visitor: ExprVisitor<LiteralValue>): LiteralValue {
                return visitor.visiteDoubleLiteral(this)
            }
        }

        class IntegerLiteral(val number: Int) : Expr() {
            override fun accept(visitor: ExprVisitor<LiteralValue>): LiteralValue {
                return visitor.visitIntegerLiteral(this)
            }
        }

        class AssignExp(val name: String, val value: Expr) : Expr() {
            override fun accept(visitor: ExprVisitor<LiteralValue>): LiteralValue {
                return visitor.visitAssignExp(this)
            }
        }

        class CallExp(val callee: Expr, val arguments: List<Expr>) : Expr() {
            override fun accept(visitor: ExprVisitor<LiteralValue>): LiteralValue {
                return visitor.visitCallExp(this)
            }
        }

        class GetExpr(val obj: Expr, val name: String) : Expr() {
            override fun accept(visitor: ExprVisitor<LiteralValue>): LiteralValue {
                return visitor.visitGetExpr(this)
            }
        }

        class SetExpr(val obj: Expr, val name: String, val value: Expr) : Expr() {
            override fun accept(visitor: ExprVisitor<LiteralValue>): LiteralValue {
                return visitor.visitSetExpr(this)
            }
        }

        class ThisExpr : Expr() {
            override fun accept(visitor: ExprVisitor<LiteralValue>): LiteralValue {
                return visitor.visitThisExpr(this)
            }
        }

        class SuperExpr(val method: String) : Expr() {
            override fun accept(visitor: ExprVisitor<LiteralValue>): LiteralValue {
                return visitor.visitSuperExpr(this)
            }
        }

        interface ExprVisitor<T> {
            fun visitBinaryExp(exp: BinaryExp): T
            fun visitUnaryExp(exp: UnaryExp): T
            fun visitGroupingExp(exp: GroupingExp): T
            fun visitStringLiteral(exp: StringLiteral): T
            fun visitBooleanLiteral(exp: BooleanLiteral): T
            fun visitNilLiteral(exp: NilLiteral): T
            fun visiteDoubleLiteral(exp: DoubleLiteral): T
            fun visitIntegerLiteral(exp: IntegerLiteral): T
            fun visitIdentifyExp(exp: IdentifyExp): T
            fun visitAssignExp(exp: AssignExp): T
            fun visitCallExp(exp: CallExp): T
            fun visitGetExpr(exp: GetExpr): T
            fun visitSetExpr(exp: SetExpr): T
            fun visitThisExpr(exp: ThisExpr): T
            fun visitSuperExpr(exp: SuperExpr): T
        }
    }


    sealed class Stmt : ASTNode() {
        abstract fun accept(visitor: StmtVisitor<LiteralValue>): Any

        class ExpressionStmt(val expression: Expr) : Stmt() {
            override fun accept(visitor: StmtVisitor<LiteralValue>): LiteralValue {
                return visitor.visitExpressionStmt(this)
            }
        }

        class PrintStmt(val expression: Expr) : Stmt() {
            override fun accept(visitor: StmtVisitor<LiteralValue>) {
                visitor.visitPrintStmt(this)
            }
        }

        class VarStmt(val name: String, val expression: Expr) : Stmt() {
            override fun accept(visitor: StmtVisitor<LiteralValue>) {
                return visitor.visitVarStmt(this)
            }
        }

        class BlockStmt(val statements: List<Stmt>) : Stmt() {
            override fun accept(visitor: StmtVisitor<LiteralValue>): Any {
                return visitor.visitBlockStmt(this)
            }
        }

        class IfStmt(
            val condition: Expr,
            val thenBranch: List<Stmt>,
            val elifBranches: List<ElseIfStmt>?,
            val elseBranch: List<Stmt>?
        ) : Stmt() {
            override fun accept(visitor: StmtVisitor<LiteralValue>) {
                return visitor.visitIfStmt(this)
            }
        }

        class ElseIfStmt(val condition: Expr, val thenBranch: List<Stmt>) : Stmt() {
            override fun accept(visitor: StmtVisitor<LiteralValue>): Expr.BooleanLiteral {
                return visitor.visitElseIfStmt(this)
            }
        }

        class WhileStmt(val condition: Expr, val thenBranch: List<Stmt>) : Stmt() {
            override fun accept(visitor: StmtVisitor<LiteralValue>) {
                return visitor.visitWhileStmt(this)
            }
        }

        class ForStmt(
            val initializer: Stmt?, val condition: Expr?, val increment: Expr?, val body: List<Stmt>
        ) : Stmt() {
            override fun accept(visitor: StmtVisitor<LiteralValue>) {
                return visitor.visitForStmt(this)
            }
        }

        class FunctionStmt(val name: String, val parameters: List<String>, val body: List<Stmt>): Stmt() {
            override fun accept(visitor: StmtVisitor<LiteralValue>) {
                return visitor.visitFunStmt(this)
            }
        }

        class ReturnStmt(val value: Expr) : Stmt() {
            override fun accept(visitor: StmtVisitor<LiteralValue>): LiteralValue {
                return visitor.visitReturnStmt(this)
            }
        }

        class ClassStmt(val name: String, val superclass: Expr.IdentifyExp?, val methods: List<FunctionStmt>) : Stmt() {
            override fun accept(visitor: StmtVisitor<LiteralValue>) {
                return visitor.visitClassStmt(this)
            }
        }


        interface StmtVisitor<T> {
            fun visitExpressionStmt(stmt: ExpressionStmt): T
            fun visitPrintStmt(stmt: PrintStmt)
            fun visitVarStmt(stmt: VarStmt)
            fun visitBlockStmt(stmt: BlockStmt): Any
            fun visitIfStmt(stmt: IfStmt)
            fun visitElseIfStmt(stmt: ElseIfStmt): Expr.BooleanLiteral
            fun visitWhileStmt(stmt: WhileStmt)
            fun visitForStmt(stmt: ForStmt)
            fun visitFunStmt(stmt: FunctionStmt)
            fun visitReturnStmt(stmt: ReturnStmt): LiteralValue
            fun visitClassStmt(stmt: ClassStmt)
        }
    }


    companion object {
        fun printAst(node: ASTNode, writer: java.io.PrintWriter, indent: Int = 0) {
            val prefix = " ".repeat(indent)
            when (node) {
                is Expr.BinaryExp -> {
                    writer.print("${prefix}(${node.op.symbol}")
                    printAst(node.left, writer, 1)
                    printAst(node.right, writer, 1)
                    writer.print(")")
                }

                is Expr.UnaryExp -> {
                    writer.print("${prefix}(${node.op.symbol}")
                    printAst(node.operand, writer, 1)
                    writer.print(")")
                }

                is Expr.GroupingExp -> {
                    writer.print("${prefix}(group")
                    printAst(node.expression, writer, 1)
                    writer.print(")")
                }

                is Expr.StringLiteral -> {
                    writer.print("${prefix}${node.string}")
                }

                is Expr.BooleanLiteral -> {
                    writer.print("${prefix}${node.value}")
                }

                is Expr.NilLiteral -> {
                    writer.print("${prefix}nil")
                }

                is Expr.DoubleLiteral -> {
                    writer.print("${prefix}${node.number}")
                }

                is Expr.IntegerLiteral -> {
                    writer.print("${prefix}${node.number.toDouble()}")
                }

                is Stmt.ExpressionStmt -> {
                    writer.print("${prefix}(expression-stmt")
                    printAst(node.expression, writer, indent + 1)
                    writer.print(")")
                }

                is Stmt.PrintStmt -> {
                    writer.print("${prefix}(print-stmt")
                    printAst(node.expression, writer, indent + 1)
                    writer.print(")")
                }

                is Stmt.VarStmt -> {
                    writer.print("${prefix}(var-stmt ${node.name}")
                    printAst(node.expression, writer, indent + 1)
                    writer.print(")")
                }

                is Stmt.BlockStmt -> {
                    writer.print("${prefix}(block")
                    node.statements.forEach { stmt ->
                        writer.println()
                        printAst(stmt, writer, indent + 1)
                    }
                    writer.print(")")
                }

                else -> {}
            }
        }
    }
}

class Parser(private val iter: LookForwardIterator<ParsedToken>) {
    // 运算符优先级配置
    private val binaryPrecedenceMap = mapOf(
        TokenType.EQUAL to 0,
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
        TokenType.PLUS to 7, TokenType.BANG to 7
    )

    // 运算符结合性（左结合）
    private val isLeftAssociative = setOf(
        TokenType.PLUS, TokenType.MINUS, TokenType.STAR, TokenType.SLASH, TokenType.MOD, TokenType.AND, TokenType.OR, TokenType.EQUAL_EQUAL, TokenType.BANG_EQUAL, TokenType.LESS, TokenType.LESS_EQUAL, TokenType.GREATER, TokenType.GREATER_EQUAL
    )

    fun parseExpr(): ASTNode.Expr {
        return parsePrecedence(0)
    }

    fun parseStmts(): List<ASTNode.Stmt> {
        val list = mutableListOf<ASTNode.Stmt>()
        // 遇到EOF或者右花括号后结束块解析
        while (iter.hasNext() && iter.cur().token != TokenType.RIGHT_BRACE && iter.cur().token != TokenType.EOF) {
            list.add(parseStatement())
        }
        return list
    }

    private fun parseStatement(): ASTNode.Stmt {
        return when (iter.cur().token) {
            TokenType.PRINT -> {
                iter.moveNext()
                val expr = parseExpr()
                consume(TokenType.SEMICOLON, "Expect ';' after value")
                ASTNode.Stmt.PrintStmt(expr)
            }

            TokenType.VAR -> {
                iter.moveNext()
                while (iter.cur().token != TokenType.IDENTIFIER) {
                    iter.moveNext()
                }
                val name = iter.cur().stringValue
                iter.moveNext()
                if (iter.hasNext() && iter.cur().token == TokenType.EQUAL) {
                    iter.moveNext()
                    val e = parseExpr()
                    consume(TokenType.SEMICOLON, "Expect ';' after value")
                    ASTNode.Stmt.VarStmt(name, e)
                } else {
                    consume(TokenType.SEMICOLON, "Expect ';' after variable declaration")
                    ASTNode.Stmt.VarStmt(name, ASTNode.Expr.NilLiteral())
                }
            }

            TokenType.LEFT_BRACE -> {
                // 仅处理代表作用域的括号
                iter.moveNext()
                val statements = parseStmts()
                consume(TokenType.RIGHT_BRACE, "Expect '}' after block")
                ASTNode.Stmt.BlockStmt(statements)
            }

            TokenType.IF -> {
                // if 分支
                iter.moveNext()
                consume(TokenType.LEFT_PAREN, "Expect '(' after 'if'")
                val condition = parseExpr()
                consume(TokenType.RIGHT_PAREN, "Expect ')' after 'if'")

                // 解析 then 分支
                val thenBranch: List<ASTNode.Stmt>
                if (iter.cur().token == TokenType.LEFT_BRACE) {
                    iter.moveNext()
                    val thenStatements = parseStmts()
                    consume(TokenType.RIGHT_BRACE, "Expect '}' after if block")
                    thenBranch = thenStatements
                } else {
                    // 单语句 then 分支
                    val stmt = parseStatement()
                    thenBranch = listOf(stmt)
                }

                // else if or else 分支
                val elifBranches = mutableListOf<ASTNode.Stmt.ElseIfStmt>()
                var elseBranch: List<ASTNode.Stmt>? = null

                while (iter.hasNext() && iter.cur().token == TokenType.ELSE) {
                    iter.moveNext()
                    if (iter.hasNext() && iter.cur().token == TokenType.IF) {
                        // else if
                        iter.moveNext()
                        consume(TokenType.LEFT_PAREN, "Expect '(' after 'else if'")
                        val elifCondition = parseExpr()
                        consume(TokenType.RIGHT_PAREN, "Expect ')' after 'else if'")

                        if (iter.cur().token == TokenType.LEFT_BRACE) {
                            iter.moveNext()
                            val elifStatements = parseStmts()
                            consume(TokenType.RIGHT_BRACE, "Expect '}' after else-if block")
                            elifBranches.add(ASTNode.Stmt.ElseIfStmt(elifCondition, elifStatements))
                        } else {
                            val elifStmt = parseStatement()
                            elifBranches.add(ASTNode.Stmt.ElseIfStmt(elifCondition, listOf(elifStmt)))
                        }
                    } else {
                        // else block
                        if (iter.cur().token == TokenType.LEFT_BRACE) {
                            iter.moveNext()
                            val elseStatements = parseStmts()
                            consume(TokenType.RIGHT_BRACE, "Expect '}' after else block")
                            elseBranch = elseStatements
                        } else {
                            val elseStmt = parseStatement()
                            elseBranch = listOf(elseStmt)
                        }
                        return ASTNode.Stmt.IfStmt(condition, thenBranch, elifBranches, elseBranch)
                    }
                }
                // only if and else if branches
                ASTNode.Stmt.IfStmt(condition, thenBranch, elifBranches, elseBranch)
            }

            TokenType.WHILE -> {
                iter.moveNext()
                consume(TokenType.LEFT_PAREN, "Expect '(' after 'if'")
                val condition = parseExpr()
                consume(TokenType.RIGHT_PAREN, "Expect ')' after 'if'")
                var thenBranch: List<ASTNode.Stmt>

                if (iter.cur().token == TokenType.LEFT_BRACE) {
                    consume(TokenType.LEFT_BRACE, "Expect '{' after while block")
                    thenBranch = parseStmts()
                    consume(TokenType.RIGHT_BRACE, "Expect '}' after while block")
                } else {
                    thenBranch = listOf(parseStatement())
                }
                ASTNode.Stmt.WhileStmt(condition, thenBranch)
            }

            TokenType.FOR -> {
                iter.moveNext()
                consume(TokenType.LEFT_PAREN, "Expect '(' after 'for'")

                // 处理 initializer
                val initializer = if (iter.cur().token == TokenType.SEMICOLON) {
                    iter.moveNext()
                    null
                } else {
                    val init = parseStatement()
                    if (init is ASTNode.Stmt.BlockStmt) {
                        throw ParserException("Expect expression.")
                    }
                    init
                }

                // 处理 condition
                val condition = if (iter.cur().token == TokenType.SEMICOLON) {
                    iter.moveNext()
                    null
                } else {
                    val cond = parseExpr()
                    consume(TokenType.SEMICOLON, "Expect ';' after condition")
                    cond
                }

                // 处理 increment
                val increment = if (iter.cur().token == TokenType.RIGHT_PAREN) {
                    iter.moveNext()
                    null
                } else {
                    val inc = parseExpr()
                    consume(TokenType.RIGHT_PAREN, "Expect ')' after 'for'")
                    inc
                }

                var bodyStmt: List<ASTNode.Stmt>

                if (iter.cur().token == TokenType.LEFT_BRACE) {
                    consume(TokenType.LEFT_BRACE, "Expect '{' after for loop")
                    bodyStmt = parseStmts()
                    consume(TokenType.RIGHT_BRACE, "Expect '}' after for block")
                } else {
                    val body = parseStatement()
                    if (body is ASTNode.Stmt.VarStmt) {
                        throw ParserException("Expect expression.")
                    }
                    bodyStmt = listOf(body)
                }
                ASTNode.Stmt.ForStmt(initializer, condition, increment, bodyStmt)
            }

            TokenType.FUN -> {
                iter.moveNext()
                val functionName = iter.cur().stringValue
                iter.moveNext()
                consume(TokenType.LEFT_PAREN, "Expect '(' after function name")
                val params = mutableListOf<String>()
                while (iter.cur().token != TokenType.RIGHT_PAREN) {
                    params.add(iter.cur().stringValue)
                    consume(TokenType.IDENTIFIER, "Expect parameter name")
                    if (iter.cur().token != TokenType.RIGHT_PAREN) {
                        consume(TokenType.COMMA, "Expect ',' between parameters")
                    }
                }
                consume(TokenType.RIGHT_PAREN, "Expect ')' after parameters")
                consume(TokenType.LEFT_BRACE, "Expect '{' before function body")
                val body = parseStmts()
                consume(TokenType.RIGHT_BRACE, "Expect '}' after function body")
                ASTNode.Stmt.FunctionStmt(functionName, params, body)
            }

            TokenType.RETURN -> {
                iter.moveNext()
                val value = if (iter.cur().token == TokenType.SEMICOLON) {
                    ASTNode.Expr.NilLiteral()
                } else {
                    parseExpr()
                }
                consume(TokenType.SEMICOLON, "Expect ';' after return statement")
                ASTNode.Stmt.ReturnStmt(value)
            }

            TokenType.CLASS -> {
                iter.moveNext()
                val className = iter.cur().stringValue
                consume(TokenType.IDENTIFIER, "Expect class name.")

                var superclass: ASTNode.Expr.IdentifyExp? = null
                if (iter.hasNext() && iter.cur().token == TokenType.LESS) {
                    iter.moveNext()
                    val superclassName = iter.cur().stringValue
                    val superclassLine = iter.cur().line
                    consume(TokenType.IDENTIFIER, "Expect superclass name.")
                    superclass = ASTNode.Expr.IdentifyExp(superclassName, superclassLine)
                }

                consume(TokenType.LEFT_BRACE, "Expect '{' before class body.")

                val methods = mutableListOf<ASTNode.Stmt.FunctionStmt>()
                while (iter.cur().token != TokenType.RIGHT_BRACE && iter.hasNext()) {
                    if (iter.cur().token == TokenType.FUN) {
                        iter.moveNext()
                        val method = parseFunction("method")
                        methods.add(method)
                    } else if (iter.cur().token == TokenType.IDENTIFIER) {
                        val method = parseFunction("method")
                        methods.add(method)
                    } else {
                        iter.moveNext()
                    }
                }
                consume(TokenType.RIGHT_BRACE, "Expect '}' after class body.")

                ASTNode.Stmt.ClassStmt(className, superclass, methods)
            }

            else -> {
                // 处理单行表达式
                val expr = parseExpr()
                consume(TokenType.SEMICOLON, "Expect ';' after expression")
                ASTNode.Stmt.ExpressionStmt(expr)
            }
        }
    }


    // 栈元素数据类，用于保存解析状态
    private data class ParseFrame(
        val left: ASTNode.Expr, val operator: TokenType, val minPrecedence: Int
    )

    /**
     * 使用栈实现的Pratt解析器
     * @param minPrecedence 当前允许的最小优先级
     */
    private fun parsePrecedence(minPrecedence: Int): ASTNode.Expr {
        val stack = mutableListOf<ParseFrame>()
        var current = parsePrefix()

        while (true) {
            if (!iter.hasNext()) break
            if (iter.cur().token == TokenType.SEMICOLON || iter.cur().token == TokenType.COMMA) {
                break
            }
            
            // 处理函数调用：(expr)(args)
            if (iter.cur().token == TokenType.LEFT_PAREN) {
                iter.moveNext()
                val args = mutableListOf<ASTNode.Expr>()
                while (iter.cur().token != TokenType.RIGHT_PAREN) {
                    args.add(parseExpr())
                    if (iter.cur().token != TokenType.RIGHT_PAREN) {
                        consume(TokenType.COMMA, "Expect ',' between arguments")
                    }
                }
                consume(TokenType.RIGHT_PAREN, "Expect ')' after arguments")
                current = ASTNode.Expr.CallExp(current, args)
                continue
            }

            // 处理属性访问：obj.property
            if (iter.cur().token == TokenType.DOT) {
                iter.moveNext()
                val propertyName = iter.cur().stringValue
                consume(TokenType.IDENTIFIER, "Expect property name after '.'")
                current = ASTNode.Expr.GetExpr(current, propertyName)
                continue
            }

            // 处理属性赋值：obj.property = value
            if (iter.cur().token == TokenType.EQUAL && current is ASTNode.Expr.GetExpr) {
                iter.moveNext()
                val value = parsePrecedence(0)
                current = ASTNode.Expr.SetExpr(current.obj, current.name, value)
                continue
            }

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
                current = ASTNode.Expr.BinaryExp(frame.operator, frame.left, current)
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
                current = ASTNode.Expr.BinaryExp(frame.operator, frame.left, current)
            }
        }

        // 处理栈中剩余的所有表达式
        while (stack.isNotEmpty()) {
            val frame = stack.removeLast()
            current = ASTNode.Expr.BinaryExp(frame.operator, frame.left, current)
        }

        return current
    }

    /**
     * 解析前缀表达式（一元运算符和基本元素）
     */
    private fun parsePrefix(): ASTNode.Expr {
        if (!iter.hasNext()) {
            throw ParserException("Unexpected end of input")
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
                ASTNode.Expr.UnaryExp(token, operand)
            }
            // 字面量
            TokenType.TRUE -> {
                iter.moveNext()
                ASTNode.Expr.BooleanLiteral(true)
            }

            TokenType.FALSE -> {
                iter.moveNext()
                ASTNode.Expr.BooleanLiteral(false)
            }

            TokenType.NIL -> {
                iter.moveNext()
                ASTNode.Expr.NilLiteral()
            }

            TokenType.NUMBER -> {
                iter.moveNext()
                if ((value as String).contains('.')) {
                    ASTNode.Expr.DoubleLiteral(value.toDouble())
                } else {
                    ASTNode.Expr.IntegerLiteral(value.toInt())
                }
            }

            TokenType.STRING -> {
                iter.moveNext()
                ASTNode.Expr.StringLiteral(stringValue.removeSurrounding("\""))
            }

            TokenType.LEFT_PAREN -> {
                iter.moveNext()
                val expr = parseExpr()
                consume(TokenType.RIGHT_PAREN, "Expected ')' after expression")
                ASTNode.Expr.GroupingExp(expr)
            }

            TokenType.LEFT_BRACE -> {
                iter.moveNext()
                throw ParserException("Expect expression.")
            }

            TokenType.IDENTIFIER -> {
                iter.moveNext()
                if (iter.hasNext() && iter.cur().token == TokenType.EQUAL) {
                    iter.moveNext()
                    val value = parseExpr()
                    ASTNode.Expr.AssignExp(stringValue, value)
                } else if (iter.hasNext() && iter.cur().token == TokenType.LEFT_PAREN) {
                    iter.moveNext()
                    val args = mutableListOf<ASTNode.Expr>()
                    while (iter.cur().token != TokenType.RIGHT_PAREN) {
                        args.add(parseExpr())
                        if (iter.cur().token != TokenType.RIGHT_PAREN) {
                            consume(TokenType.COMMA, "Expect ',' between arguments")
                        }
                    }
                    consume(TokenType.RIGHT_PAREN, "Expect ')' after arguments")
                    ASTNode.Expr.CallExp(ASTNode.Expr.IdentifyExp(stringValue), args)
                } else {
                    ASTNode.Expr.IdentifyExp(stringValue)
                }
            }

            TokenType.THIS -> {
                iter.moveNext()
                ASTNode.Expr.ThisExpr()
            }

            TokenType.SUPER -> {
                iter.moveNext()
                consume(TokenType.DOT, "Expect '.' after 'super'.")
                val methodName = iter.cur().stringValue
                consume(TokenType.IDENTIFIER, "Expect superclass method name.")
                ASTNode.Expr.SuperExpr(methodName)
            }

            TokenType.STRING_UNTERMINATED -> {
                iter.moveNext()
                throw ParserException("Unterminated string")
            }

            TokenType.UNEXPECTED_CHAR -> {
                iter.moveNext()
                throw ParserException("Unexpected character")
            }

            else -> {
                throw ParserException("Unexpected token: ${token}")
            }
        }
    }

    /**
     * 消费指定类型的token，如果当前token不是指定类型，则抛出异常
     * @param [type]
     * @param [message]
     */
    private fun consume(type: TokenType, message: String) {
        if (iter.hasNext() && iter.cur().token == type) {
            iter.moveNext()
        } else {
            throw ParserException(message)
        }
    }

    private fun parseFunction(kind: String): ASTNode.Stmt.FunctionStmt {
        val functionName = iter.cur().stringValue
        iter.moveNext()
        consume(TokenType.LEFT_PAREN, "Expect '(' after $kind name")
        val params = mutableListOf<String>()
        while (iter.cur().token != TokenType.RIGHT_PAREN) {
            params.add(iter.cur().stringValue)
            consume(TokenType.IDENTIFIER, "Expect parameter name")
            if (iter.cur().token != TokenType.RIGHT_PAREN) {
                consume(TokenType.COMMA, "Expect ',' between parameters")
            }
        }
        consume(TokenType.RIGHT_PAREN, "Expect ')' after parameters")
        consume(TokenType.LEFT_BRACE, "Expect '{' before $kind body")
        val body = parseStmts()
        consume(TokenType.RIGHT_BRACE, "Expect '}' after $kind body")
        return ASTNode.Stmt.FunctionStmt(functionName, params, body)
    }
}
