import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import statement.GlobalEnvironment
import utils.LoxAssertions
import utils.TestRunner


class ControlFlowTest {
    private val testRunner = TestRunner

    @BeforeEach
    fun setUp() {
        GlobalEnvironment.reset()
    }

    // ==================== If Statements ====================

    @Test
    fun `Control Flow - If - Simple True Condition`() {
        val myScript = """
            var x = 10;
            if (x > 5) {
                print "x is greater than 5";
            }
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "x is greater than 5")
    }

    @Test
    fun `Control Flow - If - Simple False Condition`() {
        val myScript = """
            var x = 3;
            if (x > 5) {
                print "x is greater than 5";
            }
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "")
    }

    @Test
    fun `Control Flow - If - Nil Condition Is Falsy`() {
        val myScript = """
            var x = nil;
            if (x) {
                print "will not print";
            }
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "")
    }

    @Test
    fun `Control Flow - If - Zero Is Falsy`() {
        val myScript = """
            var x = 0;
            if (x) {
                print "will not print";
            }
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "")
    }

    @Test
    fun `Control Flow - If - Empty String Is Falsy`() {
        val myScript = """
            var x = "";
            if (x) {
                print "will not print";
            }
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "")
    }

    @Test
    fun `Control Flow - If - Boolean True`() {
        val myScript = """
            if (true) {
                print "prints";
            }
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "prints")
    }

    @Test
    fun `Control Flow - If - Boolean False`() {
        val myScript = """
            if (false) {
                print "will not print";
            }
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "")
    }

    @Test
    fun `Control Flow - If - Expression As Condition`() {
        val myScript = """
            var a = 3;
            var b = 5;
            if (a + b > 10) {
                print "sum is greater than 10";
            }
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "sum is greater than 10")
    }

    @Test
    fun `Control Flow - If - Single Statement Body`() {
        val myScript = """
            var x = 10;
            if (x > 5) print "single statement";
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "single statement")
    }

    // ==================== If-Else Statements ====================

    @Test
    fun `Control Flow - If Else - True Condition`() {
        val myScript = """
            var x = 10;
            if (x > 5) {
                print "greater";
            } else {
                print "less or equal";
            }
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "greater")
    }

    @Test
    fun `Control Flow - If Else - False Condition`() {
        val myScript = """
            var x = 3;
            if (x > 5) {
                print "greater";
            } else {
                print "less or equal";
            }
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "less or equal")
    }

    @Test
    fun `Control Flow - If Else - Single Statement Bodies`() {
        val myScript = """
            var x = true;
            if (x) print "true" else print "false";
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "true")
    }

    @Test
    fun `Control Flow - If Else - Variable Scope In Then`() {
        val myScript = """
            var x = 1;
            if (true) {
                var y = 2;
                print x + y;
            }
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "3")
    }

    @Test
    fun `Control Flow - If Else - Variable Scope In Else`() {
        val myScript = """
            var x = 1;
            if (false) {
                print "then";
            } else {
                var y = 2;
                print x + y;
            }
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "3")
    }

    // ==================== Else If Statements ====================

    @Test
    fun `Control Flow - Else If - First Branch`() {
        val myScript = """
            var x = 1;
            if (x > 10) {
                print "greater than 10";
            } else if (x > 5) {
                print "greater than 5";
            } else if (x > 0) {
                print "greater than 0";
            }
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "greater than 0")
    }

    @Test
    fun `Control Flow - Else If - Second Branch`() {
        val myScript = """
            var x = 7;
            if (x > 10) {
                print "greater than 10";
            } else if (x > 5) {
                print "greater than 5";
            } else if (x > 0) {
                print "greater than 0";
            }
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "greater than 5")
    }

    @Test
    fun `Control Flow - Else If - Third Branch`() {
        val myScript = """
            var x = 15;
            if (x > 10) {
                print "greater than 10";
            } else if (x > 5) {
                print "greater than 5";
            } else if (x > 0) {
                print "greater than 0";
            }
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "greater than 10")
    }

    @Test
    fun `Control Flow - Else If - No Branch Matches`() {
        val myScript = """
            var x = -5;
            if (x > 10) {
                print "greater than 10";
            } else if (x > 5) {
                print "greater than 5";
            } else if (x > 0) {
                print "greater than 0";
            }
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "")
    }

    @Test
    fun `Control Flow - Else If - With Final Else`() {
        val myScript = """
            var x = -5;
            if (x > 10) {
                print "greater than 10";
            } else if (x > 5) {
                print "greater than 5";
            } else {
                print "less or equal to 5";
            }
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "less or equal to 5")
    }

    @Test
    fun `Control Flow - Else If - Multiple Else Ifs`() {
        val myScript = """
            var x = 3;
            if (x == 1) {
                print "one";
            } else if (x == 2) {
                print "two";
            } else if (x == 3) {
                print "three";
            } else if (x == 4) {
                print "four";
            } else if (x == 5) {
                print "five";
            }
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "three")
    }

    // ==================== Logical And ====================

    @Test
    fun `Control Flow - Logical And - Both True`() {
        val myScript = """
            var a = true;
            var b = true;
            if (a and b) {
                print "both true";
            }
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "both true")
    }

    @Test
    fun `Control Flow - Logical And - First False`() {
        val myScript = """
            var a = false;
            var b = true;
            if (a and b) {
                print "both true";
            }
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "")
    }

    @Test
    fun `Control Flow - Logical And - Second False`() {
        val myScript = """
            var a = true;
            var b = false;
            if (a and b) {
                print "both true";
            }
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "")
    }

    @Test
    fun `Control Flow - Logical And - Both False`() {
        val myScript = """
            var a = false;
            var b = false;
            if (a and b) {
                print "both true";
            }
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "")
    }

    @Test
    fun `Control Flow - Logical And - Short Circuit Returns Left`() {
        val myScript = """
            var a = false;
            var b = true;
            if (a and b) {
                print "both true";
            } else {
                print a;
            }
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "false")
    }

    @Test
    fun `Control Flow - Logical And - Chained And`() {
        val myScript = """
            if (true and true and true) {
                print "all true";
            }
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "all true")
    }

    @Test
    fun `Control Flow - Logical And - With Comparison`() {
        val myScript = """
            var x = 5;
            if (x > 0 and x < 10) {
                print "x is between 0 and 10";
            }
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "x is between 0 and 10")
    }

    // ==================== Logical Or ====================

    @Test
    fun `Control Flow - Logical Or - First True`() {
        val myScript = """
            var a = true;
            var b = false;
            if (a or b) {
                print "at least one true";
            }
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "at least one true")
    }

    @Test
    fun `Control Flow - Logical Or - Second True`() {
        val myScript = """
            var a = false;
            var b = true;
            if (a or b) {
                print "at least one true";
            }
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "at least one true")
    }

    @Test
    fun `Control Flow - Logical Or - Both True`() {
        val myScript = """
            var a = true;
            var b = true;
            if (a or b) {
                print "at least one true";
            }
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "at least one true")
    }

    @Test
    fun `Control Flow - Logical Or - Both False`() {
        val myScript = """
            var a = false;
            var b = false;
            if (a or b) {
                print "at least one true";
            }
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "")
    }

    @Test
    fun `Control Flow - Logical Or - Short Circuit Returns Left`() {
        val myScript = """
            var a = true;
            var b = false;
            if (a or b) {
                print a;
            }
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "true")
    }

    @Test
    fun `Control Flow - Logical Or - Chained Or`() {
        val myScript = """
            if (false or false or true) {
                print "found true";
            }
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "found true")
    }

    @Test
    fun `Control Flow - Logical Or - All False`() {
        val myScript = """
            if (false or false or false) {
                print "found true";
            } else {
                print "all false";
            }
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "all false")
    }

    // ==================== While Loops ====================

    @Test
    fun `Control Flow - While - Simple Counter`() {
        val myScript = """
            var count = 0;
            while (count < 3) {
                print count;
                count = count + 1;
            }
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "0\r\n1\r\n2")
    }

    @Test
    fun `Control Flow - While - Zero Iterations`() {
        val myScript = """
            var count = 5;
            while (count < 3) {
                print "should not print";
            }
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "")
    }

    @Test
    fun `Control Flow - While - Count Down`() {
        val myScript = """
            var count = 3;
            while (count > 0) {
                print count;
                count = count - 1;
            }
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "3\r\n2\r\n1")
    }

    @Test
    fun `Control Flow - While - Nested While Loops`() {
        val myScript = """
            var i = 0;
            while (i < 2) {
                var j = 0;
                while (j < 2) {
                    print i * 10 + j;
                    j = j + 1;
                }
                i = i + 1;
            }
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "0\r\n1\r\n10\r\n11")
    }

    // Note: Lox language does not support 'break' and 'continue' keywords
    // These tests are commented out until the feature is implemented
    /*
    @Test
    fun `Control Flow - While - Break Condition Inside`() {
        val myScript = """
            var count = 0;
            while (true) {
                print count;
                count = count + 1;
                if (count >= 3) {
                    break;
                }
            }
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "0\r\n1\r\n2")
    }

    @Test
    fun `Control Flow - While - Continue Pattern`() {
        val myScript = """
            var i = 0;
            while (i < 5) {
                i = i + 1;
                if (i == 2) {
                    continue;
                }
                print i;
            }
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "1\r\n3\r\n4\r\n5")
    }
    */

    @Test
    fun `Control Flow - While - Single Statement Body`() {
        val myScript = """
            var x = 0;
            while (x < 3) print x = x + 1;
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "1\r\n2\r\n3")
    }

    @Test
    fun `Control Flow - While - Accumulates Value`() {
        val myScript = """
            var sum = 0;
            var i = 1;
            while (i <= 5) {
                sum = sum + i;
                i = i + 1;
            }
            print sum;
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "15")
    }

    // ==================== For Loops ====================

    @Test
    fun `Control Flow - For - Simple Counter`() {
        val myScript = """
            for (var i = 0; i < 3; i = i + 1) {
                print i;
            }
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "0\r\n1\r\n2")
    }

    @Test
    fun `Control Flow - For - Count Down`() {
        val myScript = """
            for (var i = 3; i > 0; i = i - 1) {
                print i;
            }
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "3\r\n2\r\n1")
    }

    @Test
    fun `Control Flow - For - Zero Iterations`() {
        val myScript = """
            for (var i = 5; i < 3; i = i + 1) {
                print i;
            }
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "")
    }

    @Test
    fun `Control Flow - For - Step 2`() {
        val myScript = """
            for (var i = 0; i < 5; i = i + 2) {
                print i;
            }
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "0\r\n2\r\n4")
    }

    @Test
    fun `Control Flow - For - Nested For Loops`() {
        val myScript = """
            for (var i = 0; i < 2; i = i + 1) {
                for (var j = 0; j < 2; j = j + 1) {
                    print i * 10 + j;
                }
            }
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "0\r\n1\r\n10\r\n11")
    }

    @Test
    fun `Control Flow - For - Sum Calculation`() {
        val myScript = """
            var sum = 0;
            for (var i = 1; i <= 5; i = i + 1) {
                sum = sum + i;
            }
            print sum;
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "15")
    }

    @Test
    fun `Control Flow - For - Factorial`() {
        val myScript = """
            var result = 1;
            for (var i = 1; i <= 5; i = i + 1) {
                result = result * i;
            }
            print result;
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "120")
    }

    @Test
    fun `Control Flow - For - Single Statement Body`() {
        val myScript = """
            for (var i = 0; i < 3; i = i + 1) print i;
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "0\r\n1\r\n2")
    }

    @Test
    fun `Control Flow - For - Variable Scope`() {
        val myScript = """
            var outer = "global";
            for (var i = 0; i < 1; i = i + 1) {
                var inner = "for scope";
                print inner;
            }
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "for scope")
    }

    @Test
    fun `Control Flow - For - Variable In Outer Scope`() {
        val myScript = """
            var sum = 0;
            for (var i = 1; i <= 3; i = i + 1) {
                sum = sum + i;
            }
            print sum;
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "6")
    }

    // ==================== Nested If Statements ====================

    @Test
    fun `Control Flow - Nested If - Simple Nesting`() {
        val myScript = """
            var x = 5;
            var y = 10;
            if (x > 0) {
                if (y > 5) {
                    print "both conditions true";
                }
            }
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "both conditions true")
    }

    @Test
    fun `Control Flow - Nested If - Outer False`() {
        val myScript = """
            var x = -1;
            var y = 10;
            if (x > 0) {
                if (y > 5) {
                    print "both conditions true";
                }
            }
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "")
    }

    @Test
    fun `Control Flow - Nested If - Inner False`() {
        val myScript = """
            var x = 5;
            var y = 2;
            if (x > 0) {
                if (y > 5) {
                    print "both conditions true";
                }
            }
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "")
    }

    @Test
    fun `Control Flow - Nested If - With Else`() {
        val myScript = """
            var x = 5;
            var y = 2;
            if (x > 0) {
                if (y > 5) {
                    print "inner true";
                } else {
                    print "inner false";
                }
            } else {
                print "outer false";
            }
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "inner false")
    }

    @Test
    fun `Control Flow - Nested If - Deep Nesting`() {
        val myScript = """
            var a = true;
            var b = true;
            var c = true;
            if (a) {
                if (b) {
                    if (c) {
                        print "all true";
                    }
                }
            }
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "all true")
    }

    @Test
    fun `Control Flow - Nested If - Deep Nesting Break Chain`() {
        val myScript = """
            var a = true;
            var b = true;
            var c = false;
            if (a) {
                if (b) {
                    if (c) {
                        print "all true";
                    }
                }
            }
            print "after";
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "after")
    }

    @Test
    fun `Control Flow - Nested If - Else If Inside If`() {
        val myScript = """
            var x = 3;
            if (x > 0) {
                if (x == 1) {
                    print "one";
                } else if (x == 2) {
                    print "two";
                } else {
                    print "more than two";
                }
            }
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "more than two")
    }

    @Test
    fun `Control Flow - Nested If - In While Loop`() {
        val myScript = """
            var i = 0;
            while (i < 3) {
                if (i == 1) {
                    print "found one";
                }
                i = i + 1;
            }
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "found one")
    }

    @Test
    fun `Control Flow - Nested If - In For Loop`() {
        val myScript = """
            for (var i = 0; i < 3; i = i + 1) {
                if (i == 1) {
                    print "found one";
                }
            }
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "found one")
    }

    // ==================== Complex Control Flow ====================

    @Test
    fun `Control Flow - Complex - If And While`() {
        val myScript = """
            var i = 0;
            while (i < 5) {
                if (i % 2 == 0) {
                    print i;
                }
                i = i + 1;
            }
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "0\r\n2\r\n4")
    }

    @Test
    fun `Control Flow - Complex - If And For`() {
        val myScript = """
            var sum = 0;
            for (var i = 0; i < 10; i = i + 1) {
                if (i % 2 == 0) {
                    sum = sum + i;
                }
            }
            print sum;
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "20")
    }

    @Test
    fun `Control Flow - Complex - While With And Condition`() {
        val myScript = """
            var i = 0;
            while (i < 10 and i < 5) {
                print i;
                i = i + 1;
            }
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "0\r\n1\r\n2\r\n3\r\n4")
    }

    @Test
    fun `Control Flow - Complex - While With Or Condition`() {
        val myScript = """
            var i = 0;
            while (i > 10 or i < 3) {
                print i;
                i = i + 1;
            }
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "0\r\n1\r\n2")
    }

    @Test
    fun `Control Flow - Complex - Nested Loops With If`() {
        val myScript = """
            for (var i = 0; i < 3; i = i + 1) {
                for (var j = 0; j < 3; j = j + 1) {
                    if (i == j) {
                        print "diagonal";
                    } else {
                        print i * 10 + j;
                    }
                }
            }
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Control Flow - Complex - FizzBuzz Pattern`() {
        val myScript = """
            for (var i = 1; i <= 15; i = i + 1) {
                if (i % 3 == 0 and i % 5 == 0) {
                    print "FizzBuzz";
                } else if (i % 3 == 0) {
                    print "Fizz";
                } else if (i % 5 == 0) {
                    print "Buzz";
                } else {
                    print i;
                }
            }
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
    }

    // ==================== Syntactic Errors ====================

    @Test
    fun `Control Flow - Error - Missing Condition Paren`() {
        val myScript = """
            if true {
                print "test";
            }
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertFailure(result)
    }

    @Test
    fun `Control Flow - Error - Missing Opening Brace`() {
        val myScript = """
            if (true)
                print "test";
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertFailure(result)
    }

    @Test
    fun `Control Flow - Error - Missing Closing Brace`() {
        val myScript = """
            if (true) {
                print "test";
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertFailure(result)
    }

    @Test
    fun `Control Flow - Error - While Without Parens`() {
        val myScript = """
            while true {
                print "test";
            }
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertFailure(result)
    }

    @Test
    fun `Control Flow - Error - For Missing Initializer`() {
        val myScript = """
            for (; i < 3; i = i + 1) {
                print i;
            }
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertFailure(result)
    }


    @Test
    fun `Control Flow - Error - Else Without If`() {
        val myScript = """
            else {
                print "test";
            }
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertFailure(result)
    }

    @Test
    fun `Control Flow - Error - Else If Without If`() {
        val myScript = """
            else if (true) {
                print "test";
            }
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertFailure(result)
    }
}
