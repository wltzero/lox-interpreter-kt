import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import statement.GlobalEnvironment
import utils.LoxAssertions
import utils.TestRunner


class EvaluateTest {
    private val testRunner = TestRunner

    @BeforeEach
    fun setUp() {
        GlobalEnvironment.reset()
    }

    // ==================== Literal Evaluation ====================

    @Test
    fun `Evaluating - Literal - Integer`() {
        val myScript = """
            42;
        """.trimIndent()

        val result = testRunner.evaluate(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "42")
    }

    @Test
    fun `Evaluating - Literal - Double`() {
        val myScript = """
            3.14;
        """.trimIndent()

        val result = testRunner.evaluate(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Evaluating - Literal - String`() {
        val myScript = """
            "hello";
        """.trimIndent()

        val result = testRunner.evaluate(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "hello")
    }

    @Test
    fun `Evaluating - Literal - Boolean True`() {
        val myScript = """
            true;
        """.trimIndent()

        val result = testRunner.evaluate(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "true")
    }

    @Test
    fun `Evaluating - Literal - Boolean False`() {
        val myScript = """
            false;
        """.trimIndent()

        val result = testRunner.evaluate(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "false")
    }

    @Test
    fun `Evaluating - Literal - Nil`() {
        val myScript = """
            nil;
        """.trimIndent()

        val result = testRunner.evaluate(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "nil")
    }

    @Test
    fun `Evaluating - Literal - Negative Integer`() {
        val myScript = """
            -42;
        """.trimIndent()

        val result = testRunner.evaluate(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "-42")
    }

    @Test
    fun `Evaluating - Literal - Negative Double`() {
        val myScript = """
            -3.14;
        """.trimIndent()

        val result = testRunner.evaluate(myScript)

        LoxAssertions.assertSuccess(result)
    }

    // ==================== Identifier Evaluation ====================

    @Test
    fun `Evaluating - Identifier - Undefined Variable`() {
        val myScript = """
            unknown;
        """.trimIndent()

        val result = testRunner.evaluate(myScript)

        LoxAssertions.assertFailure(result)
    }


    // ==================== Binary Arithmetic ====================

    @Test
    fun `Evaluating - Arithmetic - Addition Integers`() {
        val myScript = """
            1 + 2;
        """.trimIndent()

        val result = testRunner.evaluate(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "3")
    }

    @Test
    fun `Evaluating - Arithmetic - Addition Doubles`() {
        val myScript = """
            1.5 + 2.5;
        """.trimIndent()

        val result = testRunner.evaluate(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Evaluating - Arithmetic - Addition Mixed`() {
        val myScript = """
            1 + 2.5;
        """.trimIndent()

        val result = testRunner.evaluate(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Evaluating - Arithmetic - Addition Strings`() {
        val myScript = """
            "hello" + " " + "world";
        """.trimIndent()

        val result = testRunner.evaluate(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "hello world")
    }

    @Test
    fun `Evaluating - Arithmetic - Subtraction Integers`() {
        val myScript = """
            10 - 3;
        """.trimIndent()

        val result = testRunner.evaluate(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "7")
    }

    @Test
    fun `Evaluating - Arithmetic - Subtraction Doubles`() {
        val myScript = """
            5.5 - 2.2;
        """.trimIndent()

        val result = testRunner.evaluate(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Evaluating - Arithmetic - Multiplication Integers`() {
        val myScript = """
            4 * 5;
        """.trimIndent()

        val result = testRunner.evaluate(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "20")
    }

    @Test
    fun `Evaluating - Arithmetic - Multiplication Doubles`() {
        val myScript = """
            2.5 * 4;
        """.trimIndent()

        val result = testRunner.evaluate(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Evaluating - Arithmetic - Division Integers`() {
        val myScript = """
            20 / 4;
        """.trimIndent()

        val result = testRunner.evaluate(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "5")
    }

    @Test
    fun `Evaluating - Arithmetic - Division Doubles`() {
        val myScript = """
            10 / 4;
        """.trimIndent()

        val result = testRunner.evaluate(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Evaluating - Arithmetic - Modulo`() {
        val myScript = """
            10 % 3;
        """.trimIndent()

        val result = testRunner.evaluate(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "1")
    }

    @Test
    fun `Evaluating - Arithmetic - Division By Zero`() {
        val myScript = """
            10 / 0;
        """.trimIndent()

        val result = testRunner.evaluate(myScript)

        LoxAssertions.assertFailure(result)
        LoxAssertions.assertErrorContains(result, "Division by zero")
    }

    @Test
    fun `Evaluating - Arithmetic - Complex Expression`() {
        val myScript = """
            (1 + 2) * (3 - 4) / 2;
        """.trimIndent()

        val result = testRunner.evaluate(myScript)

        LoxAssertions.assertSuccess(result)
    }

    // ==================== Comparison Operations ====================

    @Test
    fun `Evaluating - Comparison - Less Than`() {
        val myScript = """
            1 < 2;
        """.trimIndent()

        val result = testRunner.evaluate(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "true")
    }

    @Test
    fun `Evaluating - Comparison - Less Equal`() {
        val myScript = """
            2 <= 2;
        """.trimIndent()

        val result = testRunner.evaluate(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "true")
    }

    @Test
    fun `Evaluating - Comparison - Greater Than`() {
        val myScript = """
            3 > 2;
        """.trimIndent()

        val result = testRunner.evaluate(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "true")
    }

    @Test
    fun `Evaluating - Comparison - Greater Equal`() {
        val myScript = """
            2 >= 2;
        """.trimIndent()

        val result = testRunner.evaluate(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "true")
    }

    @Test
    fun `Evaluating - Comparison - Equal Equal`() {
        val myScript = """
            1 == 1;
        """.trimIndent()

        val result = testRunner.evaluate(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "true")
    }

    @Test
    fun `Evaluating - Comparison - Not Equal`() {
        val myScript = """
            1 != 2;
        """.trimIndent()

        val result = testRunner.evaluate(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "true")
    }

    @Test
    fun `Evaluating - Comparison - String Equality`() {
        val myScript = """
            "hello" == "hello";
        """.trimIndent()

        val result = testRunner.evaluate(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "true")
    }

    @Test
    fun `Evaluating - Comparison - Boolean Equality`() {
        val myScript = """
            true == true;
        """.trimIndent()

        val result = testRunner.evaluate(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "true")
    }

    @Test
    fun `Evaluating - Comparison - Nil Equality`() {
        val myScript = """
            nil == nil;
        """.trimIndent()

        val result = testRunner.evaluate(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "true")
    }

    // ==================== Logical Operations ====================

    @Test
    fun `Evaluating - Logical - And True And True`() {
        val myScript = """
            true and true;
        """.trimIndent()

        val result = testRunner.evaluate(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "true")
    }

    @Test
    fun `Evaluating - Logical - And True And False`() {
        val myScript = """
            true and false;
        """.trimIndent()

        val result = testRunner.evaluate(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "false")
    }

    @Test
    fun `Evaluating - Logical - Or False Or True`() {
        val myScript = """
            false or true;
        """.trimIndent()

        val result = testRunner.evaluate(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "true")
    }

    @Test
    fun `Evaluating - Logical - Or False Or False`() {
        val myScript = """
            false or false;
        """.trimIndent()

        val result = testRunner.evaluate(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "false")
    }

    @Test
    fun `Evaluating - Logical - Not True`() {
        val myScript = """
            !true;
        """.trimIndent()

        val result = testRunner.evaluate(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "false")
    }

    @Test
    fun `Evaluating - Logical - Not False`() {
        val myScript = """
            !false;
        """.trimIndent()

        val result = testRunner.evaluate(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "true")
    }

    @Test
    fun `Evaluating - Logical - Not Nil`() {
        val myScript = """
            !nil;
        """.trimIndent()

        val result = testRunner.evaluate(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "true")
    }

    @Test
    fun `Evaluating - Logical - Not Number`() {
        val myScript = """
            !0;
        """.trimIndent()

        val result = testRunner.evaluate(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "false")
    }

    @Test
    fun `Evaluating - Logical - Complex And Or`() {
        val myScript = """
            true and false or true;
        """.trimIndent()

        val result = testRunner.evaluate(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "true")
    }

    @Test
    fun `Evaluating - Logical - Short Circuit And`() {
        val myScript = """
            false and "should not evaluate";
        """.trimIndent()

        val result = testRunner.evaluate(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "false")
    }

    @Test
    fun `Evaluating - Logical - Short Circuit Or`() {
        val myScript = """
            true or "should not evaluate";
        """.trimIndent()

        val result = testRunner.evaluate(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "true")
    }

    // ==================== Unary Operations ====================

    @Test
    fun `Evaluating - Unary - Negative Number`() {
        val myScript = """
            -10;
        """.trimIndent()

        val result = testRunner.evaluate(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "-10")
    }

    @Test
    fun `Evaluating - Unary - Double Negative`() {
        val myScript = """
            --10;
        """.trimIndent()

        val result = testRunner.evaluate(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "10")
    }

    @Test
    fun `Evaluating - Unary - Positive Number`() {
        val myScript = """
            +10;
        """.trimIndent()

        val result = testRunner.evaluate(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "10")
    }

    @Test
    fun `Evaluating - Unary - Negation Of Expression`() {
        val myScript = """
            -(5 + 3);
        """.trimIndent()

        val result = testRunner.evaluate(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "-8")
    }

    // ==================== Grouping ====================

    @Test
    fun `Evaluating - Grouping - Simple`() {
        val myScript = """
            (1 + 2);
        """.trimIndent()

        val result = testRunner.evaluate(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "3")
    }

    @Test
    fun `Evaluating - Grouping - Nested`() {
        val myScript = """
            ((10));
        """.trimIndent()

        val result = testRunner.evaluate(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "10")
    }

    @Test
    fun `Evaluating - Grouping - Change Precedence`() {
        val myScript = """
            (1 + 2) * 3;
        """.trimIndent()

        val result = testRunner.evaluate(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "9")
    }

    // ==================== Mixed Type Errors ====================

    @Test
    fun `Evaluating - Error - String Plus Number`() {
        val myScript = """
            "hello" + 5;
        """.trimIndent()

        val result = testRunner.evaluate(myScript)

        LoxAssertions.assertFailure(result)
        LoxAssertions.assertErrorContains(result, "Operands must be two numbers or two strings")
    }

    @Test
    fun `Evaluating - Error - Number Minus String`() {
        val myScript = """
            5 - "hello";
        """.trimIndent()

        val result = testRunner.evaluate(myScript)

        LoxAssertions.assertFailure(result)
        LoxAssertions.assertErrorContains(result, "Operands must be numbers")
    }

    @Test
    fun `Evaluating - Error - Unary Minus String`() {
        val myScript = """
            -"hello";
        """.trimIndent()

        val result = testRunner.evaluate(myScript)

        LoxAssertions.assertFailure(result)
        LoxAssertions.assertErrorContains(result, "Operand must be a number")
    }

    @Test
    fun `Evaluating - Error - Compare String And Number`() {
        val myScript = """
            "5" < 5;
        """.trimIndent()

        val result = testRunner.evaluate(myScript)

        LoxAssertions.assertFailure(result)
    }

    // ==================== Complex Expressions ====================

    @Test
    fun `Evaluating - Complex - Arithmetic And Comparison`() {
        val myScript = """
            1 + 2 < 3 * 4;
        """.trimIndent()

        val result = testRunner.evaluate(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "true")
    }

    @Test
    fun `Evaluating - Complex - All Operators`() {
        val myScript = """
            1 + 2 * 3 - 4 / 2 > 5 and 6 < 7 or 8 == 8;
        """.trimIndent()

        val result = testRunner.evaluate(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "true")
    }

    @Test
    fun `Evaluating - Complex - Nested Grouping`() {
        val myScript = """
            ((1 + 2) * (3 - 4)) + 5;
        """.trimIndent()

        val result = testRunner.evaluate(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "2")
    }

    @Test
    fun `Evaluating - Complex - Long Chain`() {
        val myScript = """
            1 + 2 + 3 + 4 + 5;
        """.trimIndent()

        val result = testRunner.evaluate(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "15")
    }

    @Test
    fun `Evaluating - Complex - Multiple Not`() {
        val myScript = """
            !!!true;
        """.trimIndent()

        val result = testRunner.evaluate(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "false")
    }

    @Test
    fun `Evaluating - Complex - Logical With Comparison`() {
        val myScript = """
            (1 < 10) and (2 < 10);
        """.trimIndent()

        val result = testRunner.evaluate(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "true")
    }

    @Test
    fun `Evaluating - Complex - String Concatenation`() {
        val myScript = """
            "hello" + " " + "world" + "!";
        """.trimIndent()

        val result = testRunner.evaluate(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "hello world!")
    }

    @Test
    fun `Evaluating - Complex - Truthy Falsey Values`() {
        val myScript = """
            nil and true;
        """.trimIndent()

        val result = testRunner.evaluate(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "nil")
    }
}
