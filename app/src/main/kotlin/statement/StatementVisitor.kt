package statement

import evaluator.EvaluateVisitor
import evaluator.LiteralValue
import parser.ASTNode

object StatementVisitor: ASTNode.Stmt.StmtVisitor<LiteralValue> {
    fun run(list: List<ASTNode.Stmt>){
        list.forEach { node->
            node.accept(this)
        }
    }
    override fun visitExpressionStmt(stmt: ASTNode.Stmt.ExpressionStmt): LiteralValue {
        return EvaluateVisitor.evaluate(stmt.expression)
    }

    override fun visitPrintStmt(stmt: ASTNode.Stmt.PrintStmt){
        println(EvaluateVisitor.evaluate(stmt.expression))
    }

    override fun visitVarStmt(stmt: ASTNode.Stmt.VarStmt) {
        GlobalEnvironment.set(stmt.name, EvaluateVisitor.evaluate(stmt.expression))
    }

    override fun visitBlockStmt(stmt: ASTNode.Stmt.BlockStmt): Any {
        stmt.statements.forEach { node ->
            node.accept(this)
        }
        return Unit
    }

}