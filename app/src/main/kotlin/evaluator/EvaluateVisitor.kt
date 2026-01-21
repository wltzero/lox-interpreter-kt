package evaluator

import parser.ASTNode
import tokenizer.TokenType

class EvaluateException(message: String): RuntimeException(message)

object EvaluateVisitor : ASTNode.Expr.ExprVisitor<Value> {
    fun evaluate(node: ASTNode.Expr): Value {
        return node.accept(this)
    }


    override fun visitBinaryExp(exp: ASTNode.Expr.BinaryExp): Value {
        val leftValue = exp.left.accept(this)
        val rightValue = exp.right.accept(this)

        return when (exp.op) {
            TokenType.PLUS -> handlePlus(leftValue, rightValue)
            TokenType.MINUS -> handleMinus(leftValue, rightValue)
            TokenType.STAR -> handleStar(leftValue, rightValue)
            TokenType.SLASH -> handleSlash(leftValue, rightValue)

            TokenType.MOD -> Value.DoubleValue(leftValue.asDouble() % rightValue.asDouble())

            TokenType.LESS -> Value.BooleanValue(leftValue.asDouble() < rightValue.asDouble())
            TokenType.LESS_EQUAL -> Value.BooleanValue(leftValue.asDouble() <= rightValue.asDouble())
            TokenType.GREATER -> Value.BooleanValue(leftValue.asDouble() > rightValue.asDouble())
            TokenType.GREATER_EQUAL -> Value.BooleanValue(leftValue.asDouble() >= rightValue.asDouble())
            TokenType.EQUAL_EQUAL -> Value.BooleanValue(areValuesEqual(leftValue, rightValue))
            TokenType.BANG_EQUAL -> Value.BooleanValue(!areValuesEqual(leftValue, rightValue))

            TokenType.AND -> Value.BooleanValue(leftValue.asBoolean() && rightValue.asBoolean())
            TokenType.OR -> Value.BooleanValue(leftValue.asBoolean() || rightValue.asBoolean())

            else -> throw EvaluateException("Unsupported binary operator: ${exp.op}")
        }
    }

    private fun handlePlus(left: Value, right: Value): Value {
        return when {
            left is Value.DoubleValue && right is Value.DoubleValue -> {
                Value.DoubleValue(left.value + right.value)
            }

            left is Value.IntegerValue && right is Value.IntegerValue -> {
                Value.IntegerValue(left.value + right.value)
            }

            left is Value.IntegerValue && right is Value.DoubleValue -> {
                Value.DoubleValue(left.value + right.value)
            }

            left is Value.DoubleValue && right is Value.IntegerValue -> {
                Value.DoubleValue(left.value + right.value)
            }

            left is Value.StringValue && right is Value.StringValue -> {
                Value.StringValue(left.value + right.value)
            }

            else -> throw EvaluateException("Operands must be two numbers or two strings.")
        }
    }


    private fun handleMinus(left: Value, right: Value): Value {
        return when {
            left is Value.DoubleValue && right is Value.DoubleValue -> {
                Value.DoubleValue(left.value - right.value)
            }

            left is Value.IntegerValue && right is Value.IntegerValue -> {
                Value.IntegerValue(left.value - right.value)
            }

            left is Value.IntegerValue && right is Value.DoubleValue -> {
                Value.DoubleValue(left.value - right.value)
            }

            left is Value.DoubleValue && right is Value.IntegerValue -> {
                Value.DoubleValue(left.value - right.value)
            }

            else -> throw EvaluateException("Operands must be numbers.")
        }
    }

    private fun handleStar(left: Value, right: Value): Value {
        return when {
            left is Value.DoubleValue && right is Value.DoubleValue -> {
                Value.DoubleValue(left.value * right.value)
            }

            left is Value.IntegerValue && right is Value.IntegerValue -> {
                Value.IntegerValue(left.value * right.value)
            }

            left is Value.IntegerValue && right is Value.DoubleValue -> {
                Value.DoubleValue(left.value * right.value)
            }

            left is Value.DoubleValue && right is Value.IntegerValue -> {
                Value.DoubleValue(left.value * right.value)
            }

            else -> throw EvaluateException("Operands must be numbers.")
        }
    }

    private fun handleSlash(left: Value, right: Value): Value {
        return when {
            right is Value.DoubleValue && right.value == 0.0 -> throw EvaluateException("Division by zero")
            right is Value.IntegerValue && right.value == 0 -> throw EvaluateException("Division by zero")

            left is Value.DoubleValue && right is Value.DoubleValue -> {
                Value.DoubleValue(left.value / right.value)
            }

            left is Value.IntegerValue && right is Value.IntegerValue -> {
                if (left.value % right.value == 0) {
                    Value.IntegerValue(left.value / right.value)
                } else {
                    Value.DoubleValue(left.value.toDouble() / right.value.toDouble())
                }
            }

            left is Value.IntegerValue && right is Value.DoubleValue -> {
                Value.DoubleValue(left.value / right.value)
            }

            left is Value.DoubleValue && right is Value.IntegerValue -> {
                Value.DoubleValue(left.value / right.value)
            }

            else -> throw EvaluateException("Operands must be numbers.")
        }
    }

    override fun visitUnaryExp(exp: ASTNode.Expr.UnaryExp): Value {
        val operandValue = exp.operand.accept(this)
        return when (exp.op) {
            TokenType.MINUS -> when (operandValue) {
                is Value.DoubleValue -> Value.DoubleValue(-operandValue.value)
                is Value.IntegerValue -> Value.IntegerValue(-operandValue.value)
                else -> throw EvaluateException("Operand must be a number.")
            }

            TokenType.PLUS -> operandValue
            TokenType.BANG -> when (operandValue) {
                is Value.BooleanValue -> Value.BooleanValue(!operandValue.value)
                is Value.NilValue -> Value.BooleanValue(true)
                is Value.IntegerValue -> Value.BooleanValue(false)
                is Value.DoubleValue -> Value.BooleanValue(false)
                else -> throw EvaluateException("Unsupported operand type for unary bang: ${operandValue::class.simpleName}")
            }

            else -> throw EvaluateException("Unsupported unary operator: ${exp.op}")
        }
    }

    override fun visitGroupingExp(exp: ASTNode.Expr.GroupingExp): Value {
        return exp.expression.accept(this)
    }

    override fun visitStringLiteral(exp: ASTNode.Expr.StringLiteral): Value {
        return Value.StringValue(exp.string)
    }

    override fun visitBooleanLiteral(exp: ASTNode.Expr.BooleanLiteral): Value {
        return Value.BooleanValue(exp.value)
    }

    override fun visitNilLiteral(exp: ASTNode.Expr.NilLiteral): Value {
        return Value.NilValue
    }

    override fun visiteDoubleLiteral(exp: ASTNode.Expr.DoubleLiteral): Value {
        return Value.DoubleValue(exp.number)
    }

    override fun visitIntegerLiteral(exp: ASTNode.Expr.IntegerLiteral): Value {
        return Value.IntegerValue(exp.number)
    }

    // 辅助方法：判断两个 Value 是否相等
    private fun areValuesEqual(a: Value, b: Value): Boolean {
        return when {
            a is Value.NilValue && b is Value.NilValue -> true
            a is Value.DoubleValue && b is Value.DoubleValue -> a.value == b.value
            a is Value.IntegerValue && b is Value.IntegerValue -> a.value == b.value
            a is Value.BooleanValue && b is Value.BooleanValue -> a.value == b.value
            a is Value.StringValue && b is Value.StringValue -> a.value == b.value
            else -> false
        }
    }

}