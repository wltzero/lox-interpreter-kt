import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import statement.GlobalEnvironment
import utils.LoxAssertions
import utils.TestRunner


class TokenTest {
    private val testRunner = TestRunner

    @BeforeEach
    fun setUp() {
        GlobalEnvironment.reset()
    }

    // ==================== Single Character Tokens ====================

    @Test
    fun `Scanning - Single Character - Plus`() {
        val myScript = """
            1 + 2;
        """.trimIndent()

        val result = testRunner.tokenize(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Scanning - Single Character - Minus`() {
        val myScript = """
            5 - 3;
        """.trimIndent()

        val result = testRunner.tokenize(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Scanning - Single Character - Star`() {
        val myScript = """
            2 * 3;
        """.trimIndent()

        val result = testRunner.tokenize(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Scanning - Single Character - Slash`() {
        val myScript = """
            6 / 2;
        """.trimIndent()

        val result = testRunner.tokenize(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Scanning - Single Character - Left Paren`() {
        val myScript = """
            (1 + 2);
        """.trimIndent()

        val result = testRunner.tokenize(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Scanning - Single Character - Right Paren`() {
        val myScript = """
            (1 + 2);
        """.trimIndent()

        val result = testRunner.tokenize(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Scanning - Single Character - Left Brace`() {
        val myScript = """
            class Foo {}
        """.trimIndent()

        val result = testRunner.tokenize(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Scanning - Single Character - Right Brace`() {
        val myScript = """
            class Foo {}
        """.trimIndent()

        val result = testRunner.tokenize(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Scanning - Single Character - Semicolon`() {
        val myScript = """
            print "hello";
        """.trimIndent()

        val result = testRunner.tokenize(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Scanning - Single Character - Comma`() {
        val myScript = """
            var a = 1, b = 2;
            print a;
            print b;
        """.trimIndent()

        val result = testRunner.tokenize(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Scanning - Single Character - Dot`() {
        val myScript = """
            class Foo {
                bar() {}
            }
            var f = Foo();
            f.bar();
        """.trimIndent()

        val result = testRunner.tokenize(myScript)

        LoxAssertions.assertSuccess(result)
    }

    // ==================== Two Character Tokens ====================

    @Test
    fun `Scanning - Two Character - Bang Equal`() {
        val myScript = """
            print 1 != 2;
        """.trimIndent()

        val result = testRunner.tokenize(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Scanning - Two Character - Bang`() {
        val myScript = """
            var x = true;
            print !x;
        """.trimIndent()

        val result = testRunner.tokenize(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Scanning - Two Character - Equal Equal`() {
        val myScript = """
            print 1 == 1;
            print 1 == 2;
        """.trimIndent()

        val result = testRunner.tokenize(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Scanning - Two Character - Equal`() {
        val myScript = """
            var x = 42;
            print x;
        """.trimIndent()

        val result = testRunner.tokenize(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Scanning - Two Character - Less Equal`() {
        val myScript = """
            print 1 <= 2;
            print 2 <= 2;
        """.trimIndent()

        val result = testRunner.tokenize(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Scanning - Two Character - Less`() {
        val myScript = """
            print 1 < 2;
            print 2 < 2;
        """.trimIndent()

        val result = testRunner.tokenize(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Scanning - Two Character - Greater Equal`() {
        val myScript = """
            print 2 >= 1;
            print 2 >= 2;
        """.trimIndent()

        val result = testRunner.tokenize(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Scanning - Two Character - Greater`() {
        val myScript = """
            print 2 > 1;
            print 2 > 2;
        """.trimIndent()

        val result = testRunner.tokenize(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Scanning - Two Character - Slash Slash Comment`() {
        val myScript = """
            // This is a comment
            print "hello";
        """.trimIndent()

        val result = testRunner.tokenize(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Scanning - Two Character - Multi Line Comment`() {
        val myScript = """
            /* This is a
               multi-line
               comment */
            print "test";
        """.trimIndent()

        val result = testRunner.tokenize(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Scanning - Two Character - Multi Line Comment Nested`() {
        val myScript = """
            /* outer /* inner */ end */
            print "test";
        """.trimIndent()

        val result = testRunner.tokenize(myScript)

        LoxAssertions.assertSuccess(result)
    }

    // ==================== Keywords ====================

    @Test
    fun `Scanning - Keyword - And`() {
        val myScript = """
            var a = true and true;
            var b = true and false;
            print a;
            print b;
        """.trimIndent()

        val result = testRunner.tokenize(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Scanning - Keyword - Class`() {
        val myScript = """
            class Foo {}
            print Foo;
        """.trimIndent()

        val result = testRunner.tokenize(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Scanning - Keyword - Else`() {
        val myScript = """
            var x = true;
            if (x) {
                print "true";
            } else {
                print "false";
            }
        """.trimIndent()

        val result = testRunner.tokenize(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Scanning - Keyword - False`() {
        val myScript = """
            print false;
        """.trimIndent()

        val result = testRunner.tokenize(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Scanning - Keyword - For`() {
        val myScript = """
            var sum = 0;
            for (var i = 0; i < 3; i = i + 1) {
                sum = sum + i;
            }
            print sum;
        """.trimIndent()

        val result = testRunner.tokenize(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Scanning - Keyword - Fun`() {
        val myScript = """
            fun foo() {
                print "hello";
            }
            foo();
        """.trimIndent()

        val result = testRunner.tokenize(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Scanning - Keyword - If`() {
        val myScript = """
            var x = 10;
            if (x > 5) {
                print "big";
            }
        """.trimIndent()

        val result = testRunner.tokenize(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Scanning - Keyword - Nil`() {
        val myScript = """
            var x = nil;
            print x;
        """.trimIndent()

        val result = testRunner.tokenize(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Scanning - Keyword - Or`() {
        val myScript = """
            var a = false or true;
            var b = false or false;
            print a;
            print b;
        """.trimIndent()

        val result = testRunner.tokenize(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Scanning - Keyword - Print`() {
        val myScript = """
            print "hello world";
        """.trimIndent()

        val result = testRunner.tokenize(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Scanning - Keyword - Return`() {
        val myScript = """
            fun foo() {
                return 42;
            }
            print foo();
        """.trimIndent()

        val result = testRunner.tokenize(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Scanning - Keyword - Super`() {
        val myScript = """
            class A {
                foo() {
                    return "A";
                }
            }
            class B < A {
                foo() {
                    return super.foo() + " B";
                }
            }
            print B().foo();
        """.trimIndent()

        val result = testRunner.tokenize(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Scanning - Keyword - This`() {
        val myScript = """
            class Foo {
                bar() {
                    return this;
                }
            }
            print Foo().bar();
        """.trimIndent()

        val result = testRunner.tokenize(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Scanning - Keyword - True`() {
        val myScript = """
            print true;
        """.trimIndent()

        val result = testRunner.tokenize(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Scanning - Keyword - Var`() {
        val myScript = """
            var x = 100;
            print x;
        """.trimIndent()

        val result = testRunner.tokenize(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Scanning - Keyword - While`() {
        val myScript = """
            var i = 0;
            while (i < 3) {
                print i;
                i = i + 1;
            }
        """.trimIndent()

        val result = testRunner.tokenize(myScript)

        LoxAssertions.assertSuccess(result)
    }

    // ==================== Identifiers and Reserved Words ====================

    @Test
    fun `Scanning - Identifier - Simple`() {
        val myScript = """
            var myVariable = 42;
            print myVariable;
        """.trimIndent()

        val result = testRunner.tokenize(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Scanning - Identifier - With Underscore`() {
        val myScript = """
            var my_variable = 42;
            print my_variable;
        """.trimIndent()

        val result = testRunner.tokenize(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Scanning - Identifier - With Number In Middle`() {
        val myScript = """
            var var2 = 42;
            print var2;
        """.trimIndent()

        val result = testRunner.tokenize(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Scanning - Identifier - Starting With Uppercase`() {
        val myScript = """
            var MyClass = 42;
            print MyClass;
        """.trimIndent()

        val result = testRunner.tokenize(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Scanning - Reserved Word - Andlike`() {
        val myScript = """
            var android = 42;
            print android;
        """.trimIndent()

        val result = testRunner.tokenize(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Scanning - Reserved Word - Classlike`() {
        val myScript = """
            var myClass = 42;
            print myClass;
        """.trimIndent()

        val result = testRunner.tokenize(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Scanning - Reserved Word - Iflike`() {
        val myScript = """
            var office = 42;
            print office;
        """.trimIndent()

        val result = testRunner.tokenize(myScript)

        LoxAssertions.assertSuccess(result)
    }

    // ==================== String Literals ====================

    @Test
    fun `Scanning - String - Empty`() {
        val myScript = """
            var empty = "";
            print empty;
        """.trimIndent()

        val result = testRunner.tokenize(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Scanning - String - Simple`() {
        val myScript = """
            var greeting = "hello";
            print greeting;
        """.trimIndent()

        val result = testRunner.tokenize(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Scanning - String - With Spaces`() {
        val myScript = """
            var sentence = "hello world";
            print sentence;
        """.trimIndent()

        val result = testRunner.tokenize(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Scanning - String - With Numbers`() {
        val myScript = """
            var version = "version 1.0";
            print version;
        """.trimIndent()

        val result = testRunner.tokenize(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Scanning - String - With Special Characters`() {
        val myScript = """
            var special = "hello!@#$%^&*()";
            print special;
        """.trimIndent()

        val result = testRunner.tokenize(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Scanning - String - With Escape Newline`() {
        val myScript = """
            var multi = "line1\nline2";
            print multi;
        """.trimIndent()

        val result = testRunner.tokenize(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Scanning - String - With Escape Tab`() {
        val myScript = """
            var tabbed = "col1\tcol2";
            print tabbed;
        """.trimIndent()

        val result = testRunner.tokenize(myScript)

        LoxAssertions.assertSuccess(result)
    }


    @Test
    fun `Scanning - String - With Escape Backslash`() {
        val myScript = """
            var backslash = "path\\to\\file";
            print backslash;
        """.trimIndent()

        val result = testRunner.tokenize(myScript)

        LoxAssertions.assertSuccess(result)
    }

    // ==================== Number Literals ====================

    @Test
    fun `Scanning - Number - Integer`() {
        val myScript = """
            print 42;
        """.trimIndent()

        val result = testRunner.tokenize(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Scanning - Number - Zero`() {
        val myScript = """
            print 0;
        """.trimIndent()

        val result = testRunner.tokenize(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Scanning - Number - Decimal`() {
        val myScript = """
            print 3.14;
        """.trimIndent()

        val result = testRunner.tokenize(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Scanning - Number - Decimal Leading Zero`() {
        val myScript = """
            print 0.5;
        """.trimIndent()

        val result = testRunner.tokenize(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Scanning - Number - Decimal No Integer Part`() {
        val myScript = """
            print .5;
        """.trimIndent()

        val result = testRunner.tokenize(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Scanning - Number - Large Number`() {
        val myScript = """
            print 1234567890;
        """.trimIndent()

        val result = testRunner.tokenize(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Scanning - Number - Negative`() {
        val myScript = """
            print -42;
        """.trimIndent()

        val result = testRunner.tokenize(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Scanning - Number - Negative Decimal`() {
        val myScript = """
            print -3.14;
        """.trimIndent()

        val result = testRunner.tokenize(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Scanning - Number - In Expression`() {
        val myScript = """
            print (10 + 5) * 2;
        """.trimIndent()

        val result = testRunner.tokenize(myScript)

        LoxAssertions.assertSuccess(result)
    }

    // ==================== Whitespace Handling ====================

    @Test
    fun `Scanning - Whitespace - Spaces`() {
        val myScript = """
            var   x   =   42;
            print   x;
        """.trimIndent()

        val result = testRunner.tokenize(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Scanning - Whitespace - Tabs`() {
        val myScript = """
            var	x	=	42;
            print	x;
        """.trimIndent()

        val result = testRunner.tokenize(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Scanning - Whitespace - Newlines`() {
        val myScript = """
            var x = 42;

            print x;
        """.trimIndent()

        val result = testRunner.tokenize(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Scanning - Whitespace - Mixed`() {
        val myScript = """
            var   x   =   42;

            print   x;
        """.trimIndent()

        val result = testRunner.tokenize(myScript)

        LoxAssertions.assertSuccess(result)
    }

    // ==================== Error Cases ====================

    @Test
    fun `Scanning - Error - Unterminated String`() {
        val myScript = """
            var x = "unterminated;
        """.trimIndent()

        val result = testRunner.tokenize(myScript)

        LoxAssertions.assertFailure(result)
        LoxAssertions.assertErrorContains(result, "Unterminated string")
    }

    @Test
    fun `Scanning - Error - Invalid Character`() {
        val myScript = """
            var x = @;
        """.trimIndent()

        val result = testRunner.tokenize(myScript)

        LoxAssertions.assertFailure(result)
        LoxAssertions.assertErrorContains(result, "Unexpected character")
    }

    @Test
    fun `Scanning - Error - Invalid Character After Number`() {
        val myScript = """
            var x = 123@;
        """.trimIndent()

        val result = testRunner.tokenize(myScript)

        LoxAssertions.assertFailure(result)
        LoxAssertions.assertErrorContains(result, "Unexpected character")
    }

    @Test
    fun `Scanning - Error - Missing Right Paren After Print`() {
        val myScript = """
            print "hello";
        """.trimIndent()

        val result = testRunner.tokenize(myScript)

        LoxAssertions.assertSuccess(result)
    }

    // ==================== Combined Tests ====================

    @Test
    fun `Scanning - Combined - Complex Expression`() {
        val myScript = """
            var a = 10;
            var b = 20;
            var c = a + b * 2;
            print c;
        """.trimIndent()

        val result = testRunner.tokenize(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Scanning - Combined - All Operators`() {
        val myScript = """
            var a = (1 + 2) * (3 - 4) / 5;
            print a;
        """.trimIndent()

        val result = testRunner.tokenize(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Scanning - Combined - Comparison Operators`() {
        val myScript = """
            print 1 < 2;
            print 2 > 1;
            print 1 == 1;
            print 1 != 2;
            print 2 <= 2;
            print 2 >= 2;
        """.trimIndent()

        val result = testRunner.tokenize(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Scanning - Combined - Logical Operators`() {
        val myScript = """
            print true and true;
            print true and false;
            print false or true;
            print false or false;
            print not true;
            print not false;
        """.trimIndent()

        val result = testRunner.tokenize(myScript)

        LoxAssertions.assertSuccess(result)
    }
}
