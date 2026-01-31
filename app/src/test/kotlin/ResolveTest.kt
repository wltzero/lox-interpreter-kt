import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import statement.GlobalEnvironment
import utils.LoxAssertions
import utils.TestRunner

class ResolveTest {

    private val testRunner = TestRunner

    @BeforeEach
    fun setUp() {
        GlobalEnvironment.reset()
    }

    @Test
    fun `Resolving & Binding - 1`() {
        val myScript = """
            fun global() {
              print "global";
            }
            
            {
              fun f() {
                global();
              }
            
              f();
            
              fun global() {
                print "local";
              }
            
              f();
            }
        """.trimIndent()

        val result = testRunner.run(myScript)

        // 使用断言验证结果
        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputLineCount(result, 2)
        LoxAssertions.assertOutputEquals(result, "global\r\nlocal")
    }

    @Test
    fun `Resolving & Binding - 2`() {
        val myScript = """
            var x = "global";
            
            fun outer() {
              var x = "outer";
            
              fun middle() {
                fun inner() {
                  print x;
                }
            
                inner();
            
                var x = "middle";
            
                inner();
              }
            
              middle();
            }
            
            outer();
        """.trimIndent()

        val result = testRunner.run(myScript)

        // 使用断言验证结果
        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputLineCount(result, 2)
        LoxAssertions.assertOutputEquals(result, "outer\r\nouter")
    }


    @Test
    fun `Resolving & Binding - 3`() {
        val myScript = """
            var count = 0;
            
            {
              fun makeCounter() {
                fun counter() {
                  count = count + 1;
                  print count;
                }
                return counter;
              }
            
              var counter1 = makeCounter();
              counter1();
              counter1();
            
              var count = 0;
            
              counter1();
            }
        """.trimIndent()

        val result = testRunner.run(myScript)

        // 使用断言验证结果
        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputLineCount(result, 3)
        LoxAssertions.assertOutputEquals(result, "1\r\n2\r\n1")
    }


    @Test
    fun `Resolving & Binding - 4`() {
        val myScript = """
            // This program uses for loops and block scopes
            // to print the updates to the same variable
            var baz = "after";
            {
              var baz = "before";
            
              for (var baz = 0; baz < 1; baz = baz + 1) {
                print baz;
                var baz = -1;
                print baz;
              }
            }
            
            {
              for (var baz = 0; baz > 0; baz = baz + 1) {}
            
              var baz = "after";
              print baz;
            
              for (baz = 0; baz < 1; baz = baz + 1) {
                print baz;
              }
            }
        """.trimIndent()

        val result = testRunner.run(myScript)

        // 使用断言验证结果
        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputLineCount(result, 4)
        LoxAssertions.assertOutputEquals(result, "0\r\n-1\r\nafter\r\n0")
    }

    // ==================== Error: Local Variable in Own Initializer ====================

    @Test
    fun `Resolving & Binding - Error - Local Variable In Own Initializer Simple`() {
        val myScript = """
            var a = "outer";
            {
              var a = a;
            }
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertFailure(result)
        LoxAssertions.assertErrorContains(result, "Can't read local variable in its own initializer")
    }

    @Test
    fun `Resolving & Binding - Error - Local Variable In Own Initializer With Function`() {
        val myScript = """
            fun returnArg(arg) {
              return arg;
            }

            var b = "global";
            {
              var a = "first";
              var b = returnArg(b);
              print b;
            }

            var b = b + " updated";
            print b;
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertFailure(result)
        LoxAssertions.assertErrorContains(result, "Can't read local variable in its own initializer")
    }

    @Test
    fun `Resolving & Binding - Error - Local Variable In Own Initializer Nested Function`() {
        val myScript = """
            fun outer() {
              var a = "outer";

              fun inner() {
                var a = a;
                print a;
              }

              inner();
            }

            outer();
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertFailure(result)
        LoxAssertions.assertErrorContains(result, "Can't read local variable in its own initializer")
    }

    // ==================== Error: Return From Top-Level ====================

    @Test
    fun `Resolving & Binding - Error - Return From Top-Level In Control Flow`() {
        val myScript = """
            fun foo() {
              if (true) {
                return "early return";
              }

              for (var i = 0; i < 10; i = i + 1) {
                return "loop return";
              }
            }

            if (true) {
              return "conditional return";
            }
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertFailure(result)
        LoxAssertions.assertErrorContains(result, "Can't return from top-level code")
    }

    @Test
    fun `Resolving & Binding - Error - Return From Block At Top-Level`() {
        val myScript = """
            {
              return "not allowed in a block either";
            }

            fun allowed() {
              if (true) {
                return "this is fine";
              }
              return;
            }
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertFailure(result)
        LoxAssertions.assertErrorContains(result, "Can't return from top-level code")
    }

    @Test
    fun `Resolving & Binding - Error - Return From Nested Function In Top-Level If`() {
        val myScript = """
            fun outer() {
              fun inner() {
                return "ok";
              }

              return "also ok";
            }

            if (true) {
              fun nested() {
                return;
              }

              return "not ok";
            }
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertFailure(result)
        LoxAssertions.assertErrorContains(result, "Can't return from top-level code")
    }

}