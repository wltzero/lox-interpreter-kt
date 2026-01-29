import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import statement.GlobalEnvironment
import utils.LoxAssertions
import utils.TestRunner


class StatementTest {
    private val testRunner = TestRunner

    @BeforeEach
    fun setUp() {
        GlobalEnvironment.reset()
    }

    // ==================== Print Statements ====================

    @Test
    fun `Statements - Print - Integer`() {
        val myScript = """
            print 42;
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "42")
    }

    @Test
    fun `Statements - Print - Double`() {
        val myScript = """
            print 3.14;
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Statements - Print - String`() {
        val myScript = """
            print "hello world";
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "hello world")
    }

    @Test
    fun `Statements - Print - Boolean True`() {
        val myScript = """
            print true;
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "true")
    }

    @Test
    fun `Statements - Print - Boolean False`() {
        val myScript = """
            print false;
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "false")
    }

    @Test
    fun `Statements - Print - Nil`() {
        val myScript = """
            print nil;
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "nil")
    }

    @Test
    fun `Statements - Print - Expression`() {
        val myScript = """
            print 1 + 2;
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "3")
    }

    @Test
    fun `Statements - Print - Variable`() {
        val myScript = """
            var x = 100;
            print x;
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "100")
    }

    @Test
    fun `Statements - Print - Multiple Prints`() {
        val myScript = """
            print "first";
            print "second";
            print "third";
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "first\r\nsecond\r\nthird")
    }

    @Test
    fun `Statements - Print - Concatenation`() {
        val myScript = """
            var name = "world";
            print "hello " + name;
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "hello world")
    }

    @Test
    fun `Statements - Print - Empty String`() {
        val myScript = """
            var empty = "";
            print empty;
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "")
    }

    // ==================== Variable Declaration ====================

    @Test
    fun `Statements - Variable - Declaration Simple`() {
        val myScript = """
            var x = 10;
            print x;
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "10")
    }

    @Test
    fun `Statements - Variable - Declaration Without Initializer`() {
        val myScript = """
            var x;
            print x;
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "nil")
    }

    @Test
    fun `Statements - Variable - Declaration With Expression`() {
        val myScript = """
            var x = 5 + 3 * 2;
            print x;
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "11")
    }

    @Test
    fun `Statements - Variable - Declaration With String`() {
        val myScript = """
            var greeting = "hello";
            print greeting;
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "hello")
    }

    @Test
    fun `Statements - Variable - Declaration With Boolean`() {
        val myScript = """
            var flag = true;
            print flag;
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "true")
    }

    @Test
    fun `Statements - Variable - Multiple Declarations`() {
        val myScript = """
            var a = 1;
            var b = 2;
            var c = 3;
            print a;
            print b;
            print c;
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "1\r\n2\r\n3")
    }

    @Test
    fun `Statements - Variable - Declaration Using Other Variable`() {
        val myScript = """
            var a = 10;
            var b = a;
            print b;
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "10")
    }

    @Test
    fun `Statements - Variable - Declaration With Function Call`() {
        val myScript = """
            fun getValue() {
                return 42;
            }
            var x = getValue();
            print x;
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "42")
    }

    // ==================== Variable Assignment ====================

    @Test
    fun `Statements - Variable - Assignment Simple`() {
        val myScript = """
            var x = 10;
            x = 20;
            print x;
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "20")
    }

    @Test
    fun `Statements - Variable - Assignment Changes Type`() {
        val myScript = """
            var x = 10;
            x = "hello";
            print x;
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "hello")
    }

    @Test
    fun `Statements - Variable - Assignment With Expression`() {
        val myScript = """
            var x = 10;
            x = x + 5;
            print x;
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "15")
    }

    @Test
    fun `Statements - Variable - Multiple Assignments`() {
        val myScript = """
            var a = 1;
            var b = 2;
            a = b = 3;
            print a;
            print b;
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "3\r\n3")
    }

    @Test
    fun `Statements - Variable - Assignment Undefined Variable`() {
        val myScript = """
            x = 10;
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertFailure(result)
    }

    // ==================== Variable Re-declaration ====================

    @Test
    fun `Statements - Variable - Redeclaration In Same Scope`() {
        val myScript = """
            var x = 10;
            var x = 20;
            print x;
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "20")
    }

    @Test
    fun `Statements - Variable - Redeclaration Changes Type`() {
        val myScript = """
            var x = 10;
            var x = "hello";
            print x;
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "hello")
    }

    @Test
    fun `Statements - Variable - Redeclaration Preserves Value`() {
        val myScript = """
            var x = 10;
            var x = 20;
            var x = 30;
            print x;
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "30")
    }

    // ==================== Block Statements ====================

    @Test
    fun `Statements - Block - Simple Block`() {
        val myScript = """
            {
                print "inside block";
            }
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "inside block")
    }

    @Test
    fun `Statements - Block - Multiple Statements`() {
        val myScript = """
            {
                var x = 10;
                var y = 20;
                print x + y;
            }
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "30")
    }

    @Test
    fun `Statements - Block - Nested Blocks`() {
        val myScript = """
            {
                print "outer";
                {
                    print "inner";
                }
            }
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "outer\r\ninner")
    }

    @Test
    fun `Statements - Block - Deeply Nested`() {
        val myScript = """
            {
                {
                    {
                        print "deep";
                    }
                }
            }
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "deep")
    }

    // ==================== Scope ====================

    @Test
    fun `Statements - Scope - Global Variable Accessible Inside Block`() {
        val myScript = """
            var x = 10;
            {
                print x;
            }
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "10")
    }

    @Test
    fun `Statements - Scope - Block Variable Not Accessible Outside`() {
        val myScript = """
            {
                var x = 10;
            }
            print x;
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertFailure(result)
    }

    @Test
    fun `Statements - Scope - Inner Block Variable Shadows Outer`() {
        val myScript = """
            var x = "outer";
            {
                var x = "inner";
                print x;
            }
            print x;
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "inner\r\nouter")
    }

    @Test
    fun `Statements - Scope - Assignment To Outer Variable In Block`() {
        val myScript = """
            var x = 10;
            {
                x = 20;
            }
            print x;
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "20")
    }

    @Test
    fun `Statements - Scope - Multiple Levels Of Shadowing`() {
        val myScript = """
            var x = "global";
            {
                var x = "outer";
                {
                    var x = "inner";
                    print x;
                }
                print x;
            }
            print x;
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "inner\r\nouter\r\nglobal")
    }

    @Test
    fun `Statements - Scope - Same Variable Name Different Types`() {
        val myScript = """
            var x = 10;
            {
                var x = "hello";
                print x;
            }
            print x;
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "hello\r\n10")
    }

    @Test
    fun `Statements - Scope - Function Creates New Scope`() {
        val myScript = """
            var x = "global";
            fun foo() {
                var x = "local";
                print x;
            }
            foo();
            print x;
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "local\r\nglobal")
    }

    @Test
    fun `Statements - Scope - Function Can Access Outer Variables`() {
        val myScript = """
            var x = 10;
            fun foo() {
                print x;
            }
            foo();
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "10")
    }

    @Test
    fun `Statements - Scope - Function Can Modify Outer Variables`() {
        val myScript = """
            var x = 10;
            fun foo() {
                x = 20;
            }
            foo();
            print x;
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "20")
    }

    @Test
    fun `Statements - Scope - Nested Function Scope`() {
        val myScript = """
            var x = "outer";
            fun outer() {
                var x = "middle";
                fun inner() {
                    print x;
                }
                inner();
            }
            outer();
            print x;
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "middle\r\nouter")
    }

    // ==================== Mixed Statements ====================

    @Test
    fun `Statements - Mixed - Print And Variables`() {
        val myScript = """
            var a = 5;
            var b = 10;
            print "a =";
            print a;
            print "b =";
            print b;
            print "a + b =";
            print a + b;
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "a =\r\n5\r\nb =\r\n10\r\na + b =\r\n15")
    }

    @Test
    fun `Statements - Mixed - Blocks And Variables`() {
        val myScript = """
            var x = 100;
            {
                var y = 200;
                print "inner:";
                print x + y;
            }
            print "outer:";
            print x;
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "inner:\r\n300\r\nouter:\r\n100")
    }

    @Test
    fun `Statements - Mixed - Multiple Blocks With Same Variable Name`() {
        val myScript = """
            {
                var x = 1;
                print x;
            }
            {
                var x = 2;
                print x;
            }
            {
                var x = 3;
                print x;
            }
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "1\r\n2\r\n3")
    }

    @Test
    fun `Statements - Mixed - Complex Scoping`() {
        val myScript = """
            var a = "A";
            var b = "B";
            var c = "C";
            {
                var a = "a";
                var b = "b";
                print a;
                print b;
                print c;
            }
            print a;
            print b;
            print c;
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "a\r\nb\r\nC\r\nA\r\nB\r\nC")
    }

    // ==================== Edge Cases ====================

    @Test
    fun `Statements - Edge - Empty Block`() {
        val myScript = """
            {}
            print "after empty block";
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "after empty block")
    }

    @Test
    fun `Statements - Edge - Variable Declaration After Use In Block`() {
        val myScript = """
            {
                print x;
                var x = 10;
            }
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertFailure(result)
    }

    @Test
    fun `Statements - Edge - Multiple Variables Same Name In Nested Blocks`() {
        val myScript = """
            var x = 1;
            {
                var x = 2;
                {
                    var x = 3;
                    print x;
                }
                print x;
            }
            print x;
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "3\r\n2\r\n1")
    }

    @Test
    fun `Statements - Edge - Assignment In Nested Scope Affects Outer`() {
        val myScript = """
            var x = 1;
            {
                x = 2;
                {
                    x = 3;
                }
            }
            print x;
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "3")
    }

    @Test
    fun `Statements - Edge - Shadowing After Assignment`() {
        val myScript = """
            var x = 10;
            {
                print x;
                var x = 20;
                print x;
            }
            print x;
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Statements - Edge - Print Nil Variable`() {
        val myScript = """
            var x;
            print x;
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "nil")
    }

    @Test
    fun `Statements - Edge - Reassign Nil To Value`() {
        val myScript = """
            var x;
            print x;
            x = 42;
            print x;
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "nil\r\n42")
    }
}
