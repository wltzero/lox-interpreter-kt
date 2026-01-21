package statement

import evaluator.EvaluateVisitor
import evaluator.Value
import parser.ASTNode

object StatementVisitor: ASTNode.Stmt.StmtVisitor<Value> {
    fun run(list: List<ASTNode.Stmt>){
        list.forEach { node->
            node.accept(this)
        }
    }
    override fun visitExpressionStmt(stmt: ASTNode.Stmt.ExpressionStmt): Value {
        return EvaluateVisitor.evaluate(stmt.expression)
    }

    override fun visitPrintStmt(stmt: ASTNode.Stmt.PrintStmt){
        println(EvaluateVisitor.evaluate(stmt.expression))
    }

}