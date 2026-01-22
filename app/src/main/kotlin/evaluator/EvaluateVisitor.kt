package evaluator

import parser.ASTNode
import statement.GlobalEnvironment
import tokenizer.TokenType

class EvaluateException(message: String): RuntimeException(message)

object EvaluateVisitor : ASTNode.Expr.ExprVisitor<LiteralValue> {
    fun evaluate(node: ASTNode.Expr): LiteralValue {
        return node.accept(this)
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

            TokenType.AND -> LiteralValue.BooleanLiteralValue(leftValue.asBoolean() && rightValue.asBoolean())
            TokenType.OR -> LiteralValue.BooleanLiteralValue(leftValue.asBoolean() || rightValue.asBoolean())
            TokenType.EQUAL ->{
                val rightValue = exp.right.accept(this)
                if (exp.left is ASTNode.Expr.IdentifyExp) {
                    val varName = exp.left.identifier
                    GlobalEnvironment.assign(varName, rightValue)
                }
                rightValue
            }
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
                if (left.value % right.value == 0) {
                    LiteralValue.IntegerLiteralValue(left.value / right.value)
                } else {
                    LiteralValue.DoubleLiteralValue(left.value.toDouble() / right.value.toDouble())
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
        return GlobalEnvironment.get(exp.identifier).value
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