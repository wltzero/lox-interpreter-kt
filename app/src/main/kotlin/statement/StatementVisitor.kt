package statement

import evaluator.EvaluateVisitor
import evaluator.LiteralValue
import parser.ASTNode

object StatementVisitor : ASTNode.Stmt.StmtVisitor<LiteralValue> {
    fun run(list: List<ASTNode.Stmt>) {
        list.forEach { node ->
            node.accept(this)
        }
    }

    override fun visitExpressionStmt(stmt: ASTNode.Stmt.ExpressionStmt): LiteralValue {
        return EvaluateVisitor.evaluate(stmt.expression)
    }

    override fun visitPrintStmt(stmt: ASTNode.Stmt.PrintStmt) {
        println(EvaluateVisitor.evaluate(stmt.expression))
    }

    override fun visitVarStmt(stmt: ASTNode.Stmt.VarStmt) {
        GlobalEnvironment.define(stmt.name, EvaluateVisitor.evaluate(stmt.expression))
    }

    override fun visitBlockStmt(stmt: ASTNode.Stmt.BlockStmt): Any {
        GlobalEnvironment.pushScope()
        stmt.statements.forEach { node ->
            node.accept(this)
        }
        GlobalEnvironment.popScope()
        return Unit
    }

    override fun visitIfStmt(stmt: ASTNode.Stmt.IfStmt) {
        val literalValue = EvaluateVisitor.evaluate(stmt.condition)
        // 执行if
        if (literalValue.isTruthy()) {
            stmt.thenBranch.forEach { it.accept(this) }
        } else {
            // 执行else if list
            stmt.elifBranches?.forEach { elifStmt ->
                if (elifStmt.accept(this).value) return
            }

            // 执行else
            stmt.elseBranch?.forEach { it.accept(this) }
        }
    }

    override fun visitElseIfStmt(stmt: ASTNode.Stmt.ElseIfStmt): ASTNode.Expr.BooleanLiteral {
        val doElif = EvaluateVisitor.evaluate(stmt.condition)
        if (doElif.isTruthy()) {
            stmt.thenBranch.forEach { it.accept(this) }
            return ASTNode.Expr.BooleanLiteral(true)
        }
        return ASTNode.Expr.BooleanLiteral(false)
    }

    override fun visitWhileStmt(stmt: ASTNode.Stmt.WhileStmt) {
        val condition = stmt.condition
        while (EvaluateVisitor.evaluate(condition).isTruthy()) {
            GlobalEnvironment.pushScope()
            stmt.thenBranch.forEach { it.accept(this) }
            GlobalEnvironment.popScope()
        }
    }

    override fun visitForStmt(stmt: ASTNode.Stmt.ForStmt) {
        stmt.initializer?.accept(this)
        while (stmt.condition?.let { EvaluateVisitor.evaluate(it).isTruthy() } ?: true) {
            GlobalEnvironment.pushScope()
            stmt.body.forEach { it.accept(this) }
            GlobalEnvironment.popScope()
            stmt.increment?.accept(EvaluateVisitor)
        }
    }
}