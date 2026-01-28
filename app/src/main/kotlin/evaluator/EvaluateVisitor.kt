package evaluator

import exception.EvaluateException
import exception.ReturnException
import parser.ASTNode
import statement.Environment
import statement.GlobalEnvironment
import statement.StatementVisitor
import tokenizer.TokenType


object EvaluateVisitor : ASTNode.Expr.ExprVisitor<LiteralValue> {
    private val locals: MutableMap<ASTNode.Expr, Int> = mutableMapOf()

    fun evaluate(node: ASTNode.Expr): LiteralValue {
        return node.accept(this)
    }

    fun setLocals(localsMap: Map<ASTNode.Expr, Int>) {
        locals.clear()
        locals.putAll(localsMap)
    }


    override fun visitBinaryExp(exp: ASTNode.Expr.BinaryExp): LiteralValue {
        val leftValue = exp.left.accept(this)
        val rightValue = exp.right.accept(this)

        return when (exp.op) {
            TokenType.PLUS -> handlePlus(leftValue, rightValue)
            TokenType.MINUS -> handleMinus(leftValue, rightValue)
            TokenType.STAR -> handleStar(leftValue, rightValue)
            TokenType.SLASH -> handleSlash(leftValue, rightValue)

            TokenType.MOD -> LiteralValue.DoubleLiteralValue(leftValue.asDouble() % rightValue.asDouble())

            TokenType.LESS -> LiteralValue.BooleanLiteralValue(leftValue.asDouble() < rightValue.asDouble())
            TokenType.LESS_EQUAL -> LiteralValue.BooleanLiteralValue(leftValue.asDouble() <= rightValue.asDouble())
            TokenType.GREATER -> LiteralValue.BooleanLiteralValue(leftValue.asDouble() > rightValue.asDouble())
            TokenType.GREATER_EQUAL -> LiteralValue.BooleanLiteralValue(leftValue.asDouble() >= rightValue.asDouble())
            TokenType.EQUAL_EQUAL -> LiteralValue.BooleanLiteralValue(areValuesEqual(leftValue, rightValue))
            TokenType.BANG_EQUAL -> LiteralValue.BooleanLiteralValue(!areValuesEqual(leftValue, rightValue))

            TokenType.AND -> handleAnd(exp.left, exp.right)
            TokenType.OR -> handleOr(exp.left, exp.right)
            else -> throw EvaluateException("Unsupported binary operator: ${exp.op}")
        }
    }

    private fun handlePlus(left: LiteralValue, right: LiteralValue): LiteralValue {
        return when {
            left is LiteralValue.DoubleLiteralValue && right is LiteralValue.DoubleLiteralValue -> {
                LiteralValue.DoubleLiteralValue(left.value + right.value)
            }

            left is LiteralValue.IntegerLiteralValue && right is LiteralValue.IntegerLiteralValue -> {
                LiteralValue.IntegerLiteralValue(left.value + right.value)
            }

            left is LiteralValue.IntegerLiteralValue && right is LiteralValue.DoubleLiteralValue -> {
                LiteralValue.DoubleLiteralValue(left.value + right.value)
            }

            left is LiteralValue.DoubleLiteralValue && right is LiteralValue.IntegerLiteralValue -> {
                LiteralValue.DoubleLiteralValue(left.value + right.value)
            }

            left is LiteralValue.StringLiteralValue && right is LiteralValue.StringLiteralValue -> {
                LiteralValue.StringLiteralValue(left.value + right.value)
            }

            else -> throw EvaluateException("Operands must be two numbers or two strings.")
        }
    }


    private fun handleMinus(left: LiteralValue, right: LiteralValue): LiteralValue {
        return when {
            left is LiteralValue.DoubleLiteralValue && right is LiteralValue.DoubleLiteralValue -> {
                LiteralValue.DoubleLiteralValue(left.value - right.value)
            }

            left is LiteralValue.IntegerLiteralValue && right is LiteralValue.IntegerLiteralValue -> {
                LiteralValue.IntegerLiteralValue(left.value - right.value)
            }

            left is LiteralValue.IntegerLiteralValue && right is LiteralValue.DoubleLiteralValue -> {
                LiteralValue.DoubleLiteralValue(left.value - right.value)
            }

            left is LiteralValue.DoubleLiteralValue && right is LiteralValue.IntegerLiteralValue -> {
                LiteralValue.DoubleLiteralValue(left.value - right.value)
            }

            else -> throw EvaluateException("Operands must be numbers.")
        }
    }

    private fun handleStar(left: LiteralValue, right: LiteralValue): LiteralValue {
        return when {
            left is LiteralValue.DoubleLiteralValue && right is LiteralValue.DoubleLiteralValue -> {
                LiteralValue.DoubleLiteralValue(left.value * right.value)
            }

            left is LiteralValue.IntegerLiteralValue && right is LiteralValue.IntegerLiteralValue -> {
                LiteralValue.IntegerLiteralValue(left.value * right.value)
            }

            left is LiteralValue.IntegerLiteralValue && right is LiteralValue.DoubleLiteralValue -> {
                LiteralValue.DoubleLiteralValue(left.value * right.value)
            }

            left is LiteralValue.DoubleLiteralValue && right is LiteralValue.IntegerLiteralValue -> {
                LiteralValue.DoubleLiteralValue(left.value * right.value)
            }

            else -> throw EvaluateException("Operands must be numbers.")
        }
    }

    private fun handleSlash(left: LiteralValue, right: LiteralValue): LiteralValue {
        return when {
            right is LiteralValue.DoubleLiteralValue && right.value == 0.0 -> throw EvaluateException("Division by zero")
            right is LiteralValue.IntegerLiteralValue && right.value == 0 -> throw EvaluateException("Division by zero")

            left is LiteralValue.DoubleLiteralValue && right is LiteralValue.DoubleLiteralValue -> {
                LiteralValue.DoubleLiteralValue(left.value / right.value)
            }

            left is LiteralValue.IntegerLiteralValue && right is LiteralValue.IntegerLiteralValue -> {
                val result = left.value.toDouble() / right.value.toDouble()
                if (result == result.toLong().toDouble()) {
                    LiteralValue.IntegerLiteralValue(result.toInt())
                } else {
                    LiteralValue.DoubleLiteralValue(result)
                }
            }

            left is LiteralValue.IntegerLiteralValue && right is LiteralValue.DoubleLiteralValue -> {
                LiteralValue.DoubleLiteralValue(left.value / right.value)
            }

            left is LiteralValue.DoubleLiteralValue && right is LiteralValue.IntegerLiteralValue -> {
                LiteralValue.DoubleLiteralValue(left.value / right.value)
            }

            else -> throw EvaluateException("Operands must be numbers.")
        }
    }

    private fun handleOr(leftExpr: ASTNode.Expr, rightExpr: ASTNode.Expr): LiteralValue {
        val leftValue = leftExpr.accept(this)
        if (leftValue.isTruthy()) {
            return leftValue
        }
        return rightExpr.accept(this)
    }

    private fun handleAnd(leftExpr: ASTNode.Expr, rightExpr: ASTNode.Expr): LiteralValue {
        val leftValue = leftExpr.accept(this)
        if (leftValue.isFalsy()) {
            return leftValue
        }
        return rightExpr.accept(this)
    }

    override fun visitUnaryExp(exp: ASTNode.Expr.UnaryExp): LiteralValue {
        val operandValue = exp.operand.accept(this)
        return when (exp.op) {
            TokenType.MINUS -> when (operandValue) {
                is LiteralValue.DoubleLiteralValue -> LiteralValue.DoubleLiteralValue(-operandValue.value)
                is LiteralValue.IntegerLiteralValue -> LiteralValue.IntegerLiteralValue(-operandValue.value)
                else -> throw EvaluateException("Operand must be a number.")
            }

            TokenType.PLUS -> operandValue
            TokenType.BANG -> when (operandValue) {
                is LiteralValue.BooleanLiteralValue -> LiteralValue.BooleanLiteralValue(!operandValue.value)
                is LiteralValue.NilLiteralValue -> LiteralValue.BooleanLiteralValue(true)
                is LiteralValue.IntegerLiteralValue -> LiteralValue.BooleanLiteralValue(false)
                is LiteralValue.DoubleLiteralValue -> LiteralValue.BooleanLiteralValue(false)
                else -> throw EvaluateException("Unsupported operand type for unary bang: ${operandValue::class.simpleName}")
            }

            else -> throw EvaluateException("Unsupported unary operator: ${exp.op}")
        }
    }

    override fun visitGroupingExp(exp: ASTNode.Expr.GroupingExp): LiteralValue {
        return exp.expression.accept(this)
    }

    override fun visitStringLiteral(exp: ASTNode.Expr.StringLiteral): LiteralValue {
        return LiteralValue.StringLiteralValue(exp.string)
    }

    override fun visitBooleanLiteral(exp: ASTNode.Expr.BooleanLiteral): LiteralValue {
        return LiteralValue.BooleanLiteralValue(exp.value)
    }

    override fun visitNilLiteral(exp: ASTNode.Expr.NilLiteral): LiteralValue {
        return LiteralValue.NilLiteralValue
    }

    override fun visiteDoubleLiteral(exp: ASTNode.Expr.DoubleLiteral): LiteralValue {
        return LiteralValue.DoubleLiteralValue(exp.number)
    }

    override fun visitIntegerLiteral(exp: ASTNode.Expr.IntegerLiteral): LiteralValue {
        return LiteralValue.IntegerLiteralValue(exp.number)
    }

    override fun visitIdentifyExp(exp: ASTNode.Expr.IdentifyExp): LiteralValue {
        val distance = locals[exp]
        return if (distance != null) {
            GlobalEnvironment.getAt(distance, exp.identifier).value
        } else {
            GlobalEnvironment.getGlobal(exp.identifier).value
        }
    }

    override fun visitAssignExp(exp: ASTNode.Expr.AssignExp): LiteralValue {
        val value = exp.value.accept(this)
        val distance = locals[exp]
        if (distance != null) {
            GlobalEnvironment.assignAt(distance, exp.name, value)
        } else {
            GlobalEnvironment.assignGlobal(exp.name, value)
        }
        return value
    }

    override fun visitCallExp(exp: ASTNode.Expr.CallExp): LiteralValue {
        val calleeValue = exp.callee.accept(this)
        val arguments = exp.arguments.map { it.accept(this) }
        return when (val literal = calleeValue) {
            is LiteralValue.FunctionLiteralValue -> {
                if (arguments.size != literal.parameters.size) {
                    throw EvaluateException("Expected ${literal.parameters.size} arguments but got ${arguments.size}.")
                }

                literal.capturedEnvironment?.let { GlobalEnvironment.pushScopeWith(it) }
                for (i in literal.parameters.indices) {
                    GlobalEnvironment.define(literal.parameters[i], arguments[i])
                }

                try {
                    StatementVisitor.run(literal.body)
                } catch (res: ReturnException) {
                    GlobalEnvironment.popScope()
                    return res.value
                }
                GlobalEnvironment.popScope()
                LiteralValue.NilLiteralValue
            }
            is LiteralValue.NativeFunctionLiteralValue ->{
                if (arguments.size != literal.parameters.size) {
                    throw EvaluateException("Expected ${literal.parameters.size} arguments but got ${arguments.size}.")
                }
                val res = literal.function.invoke(arguments)
                res
            }
            is LiteralValue.ClassLiteralValue -> {
                val instance = LiteralValue.InstanceLiteralValue(literal)
                literal.methods["init"]?.let { initializer ->
                    GlobalEnvironment.pushScope()
                    GlobalEnvironment.define("this", instance)
                    try {
                        StatementVisitor.run(initializer.body)
                    } catch (res: ReturnException) {
                        GlobalEnvironment.popScope()
                        return res.value
                    }
                    GlobalEnvironment.popScope()
                }
                instance
            }
            else -> throw EvaluateException("Can only call functions or classes.")
        }
    }

    override fun visitGetExpr(exp: ASTNode.Expr.GetExpr): LiteralValue {
        val obj = exp.obj.accept(this)
        return when (val literal = obj) {
            is LiteralValue.InstanceLiteralValue -> {
                if (literal.fields.containsKey(exp.name)) {
                    literal.fields[exp.name]!!
                } else if (literal.klass.methods.containsKey(exp.name)) {
                    // Return a bound method
                    val method = literal.klass.methods[exp.name]!!
                    val capturedEnv = method.capturedEnvironment
                    val boundEnv = statement.Environment(capturedEnv)
                    boundEnv.set("this", literal)
                    LiteralValue.FunctionLiteralValue(method.name, method.parameters, method.body, boundEnv)
                } else {
                    literal.fields[exp.name] = LiteralValue.NilLiteralValue
                    LiteralValue.NilLiteralValue
                }
            }
            else -> throw EvaluateException("Only instances have properties.")
        }
    }

    override fun visitSetExpr(exp: ASTNode.Expr.SetExpr): LiteralValue {
        val obj = exp.obj.accept(this)
        val value = exp.value.accept(this)
        return when (val literal = obj) {
            is LiteralValue.InstanceLiteralValue -> {
                literal.fields[exp.name] = value
                value
            }
            else -> throw EvaluateException("Only instances have fields.")
        }
    }

    override fun visitThisExpr(exp: ASTNode.Expr.ThisExpr): LiteralValue {
        val distance = locals[exp]
        return if (distance != null) {
            GlobalEnvironment.getAt(distance, "this").value
        } else {
            GlobalEnvironment.getGlobal("this").value
        }
    }

    // 辅助方法：判断两个 Value 是否相等
    private fun areValuesEqual(a: LiteralValue, b: LiteralValue): Boolean {
        return when {
            a is LiteralValue.NilLiteralValue && b is LiteralValue.NilLiteralValue -> true
            a is LiteralValue.DoubleLiteralValue && b is LiteralValue.DoubleLiteralValue -> a.value == b.value
            a is LiteralValue.IntegerLiteralValue && b is LiteralValue.IntegerLiteralValue -> a.value == b.value
            a is LiteralValue.BooleanLiteralValue && b is LiteralValue.BooleanLiteralValue -> a.value == b.value
            a is LiteralValue.StringLiteralValue && b is LiteralValue.StringLiteralValue -> a.value == b.value
            else -> false
        }
    }

}
