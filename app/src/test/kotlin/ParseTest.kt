import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import statement.GlobalEnvironment
import utils.LoxAssertions
import utils.TestRunner


class ParseTest {
    private val testRunner = TestRunner

    @BeforeEach
    fun setUp() {
        GlobalEnvironment.reset()
    }

    // ==================== Literal Expressions ====================

    @Test
    fun `Parsing - Literal - Integer`() {
        val myScript = """
            42;
        """.trimIndent()

        val result = testRunner.parse(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Parsing - Literal - Double`() {
        val myScript = """
            3.14;
        """.trimIndent()

        val result = testRunner.parse(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Parsing - Literal - String`() {
        val myScript = """
            "hello";
        """.trimIndent()

        val result = testRunner.parse(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Parsing - Literal - Boolean True`() {
        val myScript = """
            true;
        """.trimIndent()

        val result = testRunner.parse(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Parsing - Literal - Boolean False`() {
        val myScript = """
            false;
        """.trimIndent()

        val result = testRunner.parse(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Parsing - Literal - Nil`() {
        val myScript = """
            nil;
        """.trimIndent()

        val result = testRunner.parse(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Parsing - Literal - Negative Number`() {
        val myScript = """
            -42;
        """.trimIndent()

        val result = testRunner.parse(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Parsing - Literal - Negative Decimal`() {
        val myScript = """
            -3.14;
        """.trimIndent()

        val result = testRunner.parse(myScript)

        LoxAssertions.assertSuccess(result)
    }

    // ==================== Identifier Expressions ====================

    @Test
    fun `Parsing - Identifier - Simple`() {
        val myScript = """
            x;
        """.trimIndent()

        val result = testRunner.parse(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Parsing - Identifier - With Underscore`() {
        val myScript = """
            my_variable;
        """.trimIndent()

        val result = testRunner.parse(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Parsing - Identifier - With Number In Middle`() {
        val myScript = """
            var2;
        """.trimIndent()

        val result = testRunner.parse(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Parsing - Identifier - Single Letter`() {
        val myScript = """
            a;
        """.trimIndent()

        val result = testRunner.parse(myScript)

        LoxAssertions.assertSuccess(result)
    }

    // ==================== Grouping Expressions ====================

    @Test
    fun `Parsing - Grouping - Simple`() {
        val myScript = """
            (1 + 2);
        """.trimIndent()

        val result = testRunner.parse(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Parsing - Grouping - Nested`() {
        val myScript = """
            ((1));
        """.trimIndent()

        val result = testRunner.parse(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Parsing - Grouping - Complex Expression`() {
        val myScript = """
            (1 + 2) * (3 - 4);
        """.trimIndent()

        val result = testRunner.parse(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Parsing - Grouping - Identifier In Group`() {
        val myScript = """
            (x);
        """.trimIndent()

        val result = testRunner.parse(myScript)

        LoxAssertions.assertSuccess(result)
    }

    // ==================== Unary Expressions ====================

    @Test
    fun `Parsing - Unary - Negation`() {
        val myScript = """
            -x;
        """.trimIndent()

        val result = testRunner.parse(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Parsing - Unary - Not`() {
        val myScript = """
            !true;
        """.trimIndent()

        val result = testRunner.parse(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Parsing - Unary - Double Negation`() {
        val myScript = """
            !!true;
        """.trimIndent()

        val result = testRunner.parse(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Parsing - Unary - Negative Literal`() {
        val myScript = """
            -123;
        """.trimIndent()

        val result = testRunner.parse(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Parsing - Unary - Not False`() {
        val myScript = """
            !false;
        """.trimIndent()

        val result = testRunner.parse(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Parsing - Unary - Negation Of Group`() {
        val myScript = """
            -(1 + 2);
        """.trimIndent()

        val result = testRunner.parse(myScript)

        LoxAssertions.assertSuccess(result)
    }

    // ==================== Binary Arithmetic Expressions ====================

    @Test
    fun `Parsing - Binary - Addition`() {
        val myScript = """
            1 + 2;
        """.trimIndent()

        val result = testRunner.parse(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Parsing - Binary - Subtraction`() {
        val myScript = """
            5 - 3;
        """.trimIndent()

        val result = testRunner.parse(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Parsing - Binary - Multiplication`() {
        val myScript = """
            2 * 3;
        """.trimIndent()

        val result = testRunner.parse(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Parsing - Binary - Division`() {
        val myScript = """
            6 / 2;
        """.trimIndent()

        val result = testRunner.parse(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Parsing - Binary - Multiple Operators`() {
        val myScript = """
            1 + 2 - 3 * 4 / 5;
        """.trimIndent()

        val result = testRunner.parse(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Parsing - Binary - Chained Addition`() {
        val myScript = """
            1 + 2 + 3;
        """.trimIndent()

        val result = testRunner.parse(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Parsing - Binary - Mixed Operators`() {
        val myScript = """
            10 + 5 * 2 - 8 / 4;
        """.trimIndent()

        val result = testRunner.parse(myScript)

        LoxAssertions.assertSuccess(result)
    }

    // ==================== Binary Comparison Expressions ====================

    @Test
    fun `Parsing - Comparison - Less`() {
        val myScript = """
            1 < 2;
        """.trimIndent()

        val result = testRunner.parse(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Parsing - Comparison - Less Equal`() {
        val myScript = """
            1 <= 2;
        """.trimIndent()

        val result = testRunner.parse(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Parsing - Comparison - Greater`() {
        val myScript = """
            2 > 1;
        """.trimIndent()

        val result = testRunner.parse(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Parsing - Comparison - Greater Equal`() {
        val myScript = """
            2 >= 1;
        """.trimIndent()

        val result = testRunner.parse(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Parsing - Comparison - Equality`() {
        val myScript = """
            1 == 1;
        """.trimIndent()

        val result = testRunner.parse(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Parsing - Comparison - Not Equal`() {
        val myScript = """
            1 != 2;
        """.trimIndent()

        val result = testRunner.parse(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Parsing - Comparison - With Arithmetic`() {
        val myScript = """
            1 + 2 < 3 * 4;
        """.trimIndent()

        val result = testRunner.parse(myScript)

        LoxAssertions.assertSuccess(result)
    }

    // ==================== Logical Expressions ====================

    @Test
    fun `Parsing - Logical - And`() {
        val myScript = """
            true and false;
        """.trimIndent()

        val result = testRunner.parse(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Parsing - Logical - Or`() {
        val myScript = """
            true or false;
        """.trimIndent()

        val result = testRunner.parse(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Parsing - Logical - Not`() {
        val myScript = """
            not true;
        """.trimIndent()

        val result = testRunner.parse(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Parsing - Logical - Complex And Or`() {
        val myScript = """
            true and false or true;
        """.trimIndent()

        val result = testRunner.parse(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Parsing - Logical - With Comparison`() {
        val myScript = """
            x > 0 and x < 10;
        """.trimIndent()

        val result = testRunner.parse(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Parsing - Logical - Multiple And`() {
        val myScript = """
            a and b and c;
        """.trimIndent()

        val result = testRunner.parse(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Parsing - Logical - Multiple Or`() {
        val myScript = """
            a or b or c;
        """.trimIndent()

        val result = testRunner.parse(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Parsing - Logical - And Or Combination`() {
        val myScript = """
            a or b and c;
        """.trimIndent()

        val result = testRunner.parse(myScript)

        LoxAssertions.assertSuccess(result)
    }

    // ==================== Assignment Expressions ====================

    @Test
    fun `Parsing - Assignment - Simple Variable`() {
        val myScript = """
            x = 42;
        """.trimIndent()

        val result = testRunner.parse(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Parsing - Assignment - With Expression`() {
        val myScript = """
            x = 1 + 2;
        """.trimIndent()

        val result = testRunner.parse(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Parsing - Assignment - Chained`() {
        val myScript = """
            a = b = 10;
        """.trimIndent()

        val result = testRunner.parse(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Parsing - Assignment - String Value`() {
        val myScript = """
            name = "hello";
        """.trimIndent()

        val result = testRunner.parse(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Parsing - Assignment - Boolean Value`() {
        val myScript = """
            flag = true;
        """.trimIndent()

        val result = testRunner.parse(myScript)

        LoxAssertions.assertSuccess(result)
    }

    // ==================== Call Expressions ====================

    @Test
    fun `Parsing - Call - Simple Function`() {
        val myScript = """
            foo();
        """.trimIndent()

        val result = testRunner.parse(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Parsing - Call - With Arguments`() {
        val myScript = """
            foo(1, 2, 3);
        """.trimIndent()

        val result = testRunner.parse(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Parsing - Call - No Arguments`() {
        val myScript = """
            bar();
        """.trimIndent()

        val result = testRunner.parse(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Parsing - Call - Nested`() {
        val myScript = """
            foo(bar());
        """.trimIndent()

        val result = testRunner.parse(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Parsing - Call - With Expression Arguments`() {
        val myScript = """
            add(1 + 2, 3 * 4);
        """.trimIndent()

        val result = testRunner.parse(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Parsing - Call - On Call Result`() {
        val myScript = """
            foo()();
        """.trimIndent()

        val result = testRunner.parse(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Parsing - Call - Method Call On Object`() {
        val myScript = """
            obj.method();
        """.trimIndent()

        val result = testRunner.parse(myScript)

        LoxAssertions.assertSuccess(result)
    }

    // ==================== Property Access Expressions ====================

    @Test
    fun `Parsing - Property - Get Simple`() {
        val myScript = """
            obj.property;
        """.trimIndent()

        val result = testRunner.parse(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Parsing - Property - Get After Call`() {
        val myScript = """
            foo().bar;
        """.trimIndent()

        val result = testRunner.parse(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Parsing - Property - Get Nested`() {
        val myScript = """
            obj.a.b.c;
        """.trimIndent()

        val result = testRunner.parse(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Parsing - Property - Set`() {
        val myScript = """
            obj.property = 42;
        """.trimIndent()

        val result = testRunner.parse(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Parsing - Property - Set With Expression`() {
        val myScript = """
            obj.x = 1 + 2;
        """.trimIndent()

        val result = testRunner.parse(myScript)

        LoxAssertions.assertSuccess(result)
    }

    // ==================== Complex Expressions ====================

    @Test
    fun `Parsing - Complex - Arithmetic With Parentheses`() {
        val myScript = """
            (1 + 2) * (3 - 4) / (5 + 6);
        """.trimIndent()

        val result = testRunner.parse(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Parsing - Complex - Mixed Operators`() {
        val myScript = """
            1 + 2 * 3 - 4 / 5 > 6 and 7 < 8 or 9 == 10;
        """.trimIndent()

        val result = testRunner.parse(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Parsing - Complex - Function Call In Expression`() {
        val myScript = """
            foo() + bar() * baz();
        """.trimIndent()

        val result = testRunner.parse(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Parsing - Complex - Property Access In Expression`() {
        val myScript = """
            obj.a + obj.b * obj.c;
        """.trimIndent()

        val result = testRunner.parse(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Parsing - Complex - Deeply Nested`() {
        val myScript = """
            ((((1 + 2) * (3 - 4)) + 5) * 6);
        """.trimIndent()

        val result = testRunner.parse(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Parsing - Complex - All Operator Types`() {
        val myScript = """
            a + b - c * d / e < f and g > h or i == j;
        """.trimIndent()

        val result = testRunner.parse(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Parsing - Complex - Unary And Binary Mixed`() {
        val myScript = """
            -a + b * -c;
        """.trimIndent()

        val result = testRunner.parse(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Parsing - Complex - Not And Comparison`() {
        val myScript = """
            !(a < b);
        """.trimIndent()

        val result = testRunner.parse(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Parsing - Complex - Ternary Like`() {
        val myScript = """
            a and b or c;
        """.trimIndent()

        val result = testRunner.parse(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Parsing - Complex - Assignment In Expression`() {
        val myScript = """
            x = y = z = 10;
        """.trimIndent()

        val result = testRunner.parse(myScript)

        LoxAssertions.assertSuccess(result)
    }

    // ==================== Error Cases ====================

    @Test
    fun `Parsing - Error - Unterminated Paren`() {
        val myScript = """
            (1 + 2;
        """.trimIndent()

        val result = testRunner.parse(myScript)

        LoxAssertions.assertFailure(result)
    }

    @Test
    fun `Parsing - Error - Missing Operand Right`() {
        val myScript = """
            1 +;
        """.trimIndent()

        val result = testRunner.parse(myScript)

        LoxAssertions.assertFailure(result)
    }

    @Test
    fun `Parsing - Error - Unexpected Token`() {
        val myScript = """
            1 + 2 3;
        """.trimIndent()

        val result = testRunner.parse(myScript)

        LoxAssertions.assertFailure(result)
    }
}
