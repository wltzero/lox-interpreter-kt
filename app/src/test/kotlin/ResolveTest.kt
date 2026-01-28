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
        LoxAssertions.assertOutputEquals(result, "global\r\nglobal")
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
        LoxAssertions.assertOutputEquals(result, "1\r\n2\r\n3")
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


}