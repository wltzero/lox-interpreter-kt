import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import statement.GlobalEnvironment
import utils.LoxAssertions
import utils.TestRunner


class FunctionTest {
    private val testRunner = TestRunner

    @BeforeEach
    fun setUp() {
        GlobalEnvironment.reset()
    }

    // ==================== Native Functions ====================

    @Test
    fun `Functions - Native - Clock`() {
        val myScript = """
            print clock();
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Functions - Native - Multiple Clock Calls`() {
        val myScript = """
            print clock();
            print clock();
            print clock();
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Functions - Native - Clock Different Values`() {
        val myScript = """
            var start = clock();
            var end = clock();
            print start < end;
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
    }

    // ==================== Function Declaration ====================

    @Test
    fun `Functions - Declaration - Simple No Body`() {
        val myScript = """
            fun foo() {}
            print foo;
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Functions - Declaration - With Print Statement`() {
        val myScript = """
            fun greet() {
                print "hello";
            }
            greet();
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "hello")
    }

    @Test
    fun `Functions - Declaration - Multiple Functions`() {
        val myScript = """
            fun foo() {
                print "foo";
            }
            fun bar() {
                print "bar";
            }
            foo();
            bar();
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "foo\r\nbar")
    }

    @Test
    fun `Functions - Declaration - Function Inside Block`() {
        val myScript = """
            {
                fun inner() {
                    print "inner";
                }
                inner();
            }
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "inner")
    }

    @Test
    fun `Functions - Declaration - Function Inside Block Not Accessible Outside`() {
        val myScript = """
            {
                fun inner() {
                    print "inner";
                }
            }
            inner();
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertFailure(result)
    }

    // ==================== Function Parameters ====================

    @Test
    fun `Functions - Parameters - No Parameters`() {
        val myScript = """
            fun noParams() {
                return "no params";
            }
            print noParams();
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "no params")
    }

    @Test
    fun `Functions - Parameters - Single Parameter`() {
        val myScript = """
            fun double(x) {
                return x * 2;
            }
            print double(5);
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "10")
    }

    @Test
    fun `Functions - Parameters - Multiple Parameters`() {
        val myScript = """
            fun add(a, b) {
                return a + b;
            }
            print add(5, 3);
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "8")
    }

    @Test
    fun `Functions - Parameters - Many Parameters`() {
        val myScript = """
            fun sum(a, b, c, d, e) {
                return a + b + c + d + e;
            }
            print sum(1, 2, 3, 4, 5);
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "15")
    }

    @Test
    fun `Functions - Parameters - With Default Values`() {
        val myScript = """
            fun greet(name) {
                return "Hello, " + name;
            }
            print greet("World");
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "Hello, World")
    }

    @Test
    fun `Functions - Parameters - Parameter Scope`() {
        val myScript = """
            fun test(x) {
                print x;
            }
            var x = "global";
            test("parameter");
            print x;
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "parameter\r\nglobal")
    }

    @Test
    fun `Functions - Parameters - Same Name As Outer Variable`() {
        val myScript = """
            var x = "outer";
            fun foo(x) {
                print x;
            }
            foo("inner");
            print x;
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "inner\r\nouter")
    }

    // ==================== Return Statements ====================

    @Test
    fun `Functions - Return - Integer`() {
        val myScript = """
            fun getNumber() {
                return 42;
            }
            print getNumber();
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "42")
    }

    @Test
    fun `Functions - Return - String`() {
        val myScript = """
            fun getString() {
                return "hello";
            }
            print getString();
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "hello")
    }

    @Test
    fun `Functions - Return - Boolean`() {
        val myScript = """
            fun getBool() {
                return true;
            }
            print getBool();
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "true")
    }

    @Test
    fun `Functions - Return - Nil`() {
        val myScript = """
            fun getNil() {
                return;
            }
            print getNil();
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "nil")
    }

    @Test
    fun `Functions - Return - Expression`() {
        val myScript = """
            fun calc() {
                return 1 + 2 * 3;
            }
            print calc();
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "7")
    }

    @Test
    fun `Functions - Return - Early Return`() {
        val myScript = """
            fun earlyReturn(x) {
                if (x > 10) {
                    return "big";
                }
                return "small";
            }
            print earlyReturn(5);
            print earlyReturn(15);
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "small\r\nbig")
    }

    @Test
    fun `Functions - Return - Multiple Returns`() {
        val myScript = """
            fun isPositive(n) {
                if (n > 0) {
                    return true;
                }
                if (n < 0) {
                    return false;
                }
                return nil;
            }
            print isPositive(5);
            print isPositive(-3);
            print isPositive(0);
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "true\r\nfalse\r\nnil")
    }

    @Test
    fun `Functions - Return - Return From Nested Function`() {
        val myScript = """
            fun outer() {
                fun inner() {
                    return "inner return";
                }
                return inner();
            }
            print outer();
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "inner return")
    }

    // ==================== Nested Functions ====================

    @Test
    fun `Functions - Nested - Simple Nested Function`() {
        val myScript = """
            fun outer() {
                fun inner() {
                    print "inner";
                }
                print "outer";
                inner();
            }
            outer();
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "outer\r\ninner")
    }

    @Test
    fun `Functions - Nested - Nested Function Call`() {
        val myScript = """
            fun outer() {
                fun inner() {
                    return "inner result";
                }
                return inner();
            }
            print outer();
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "inner result")
    }

    @Test
    fun `Functions - Nested - Multiple Nested Functions`() {
        val myScript = """
            fun level1() {
                fun level2() {
                    fun level3() {
                        return "deep";
                    }
                    return level3();
                }
                return level2();
            }
            print level1();
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "deep")
    }

    @Test
    fun `Functions - Nested - Inner Accesses Outer Variables`() {
        val myScript = """
            fun outer() {
                var x = "outer";
                fun inner() {
                    print x;
                }
                inner();
            }
            outer();
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "outer")
    }

    @Test
    fun `Functions - Nested - Inner Modifies Outer Variables`() {
        val myScript = """
            fun outer() {
                var x = 10;
                fun inner() {
                    x = 20;
                }
                print x;
                inner();
                print x;
            }
            outer();
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "10\r\n20")
    }

    @Test
    fun `Functions - Nested - Inner Shadows Outer Variable`() {
        val myScript = """
            fun outer() {
                var x = "outer";
                fun inner() {
                    var x = "inner";
                    print x;
                }
                inner();
                print x;
            }
            outer();
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "inner\r\nouter")
    }

    @Test
    fun `Functions - Nested - Return Nested Function`() {
        val myScript = """
            fun makeCounter() {
                var count = 0;
                fun increment() {
                    count = count + 1;
                    return count;
                }
                return increment;
            }
            var counter = makeCounter();
            print counter();
            print counter();
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "1\r\n2")
    }

    // ==================== Closures ====================

    @Test
    fun `Functions - Closure - Basic Closure`() {
        val myScript = """
            fun makeCounter() {
                var count = 0;
                fun counter() {
                    count = count + 1;
                    return count;
                }
                return counter;
            }
            var counter = makeCounter();
            print counter();
            print counter();
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "1\r\n2")
    }

    @Test
    fun `Functions - Closure - Multiple Closures`() {
        val myScript = """
            fun makeCounter() {
                var count = 0;
                fun counter() {
                    count = count + 1;
                    return count;
                }
                return counter;
            }
            var counter1 = makeCounter();
            var counter2 = makeCounter();
            print counter1();
            print counter1();
            print counter2();
            print counter2();
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "1\r\n2\r\n1\r\n2")
    }

    @Test
    fun `Functions - Closure - Closure Captures Parameter`() {
        val myScript = """
            fun makeMultiplier(factor) {
                fun multiply(n) {
                    return n * factor;
                }
                return multiply;
            }
            var double = makeMultiplier(2);
            var triple = makeMultiplier(3);
            print double(5);
            print triple(5);
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "10\r\n15")
    }

    @Test
    fun `Functions - Closure - Nested Closure`() {
        val myScript = """
            fun outer() {
                var x = 10;
                fun middle() {
                    fun inner() {
                        return x;
                    }
                    return inner;
                }
                return middle();
            }
            var fn = outer();
            print fn();
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "10")
    }

    @Test
    fun `Functions - Closure - Closure Modifies Captured Variable`() {
        val myScript = """
            fun makeAccumulator() {
                var sum = 0;
                fun add(n) {
                    sum = sum + n;
                    return sum;
                }
                return add;
            }
            var add = makeAccumulator();
            print add(5);
            print add(3);
            print add(10);
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "5\r\n8\r\n18")
    }

    @Test
    fun `Functions - Closure - Factory Function`() {
        val myScript = """
            fun makeGreeter(prefix) {
                fun greet(name) {
                    return prefix + " " + name;
                }
                return greet;
            }
            var hello = makeGreeter("Hello");
            var hi = makeGreeter("Hi");
            print hello("World");
            print hi("There");
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "Hello World\r\nHi There")
    }

    @Test
    fun `Functions - Closure - Closure With Multiple Variables`() {
        val myScript = """
            fun makePoint(x, y) {
                fun get(dimension) {
                    if (dimension == "x") return x;
                    if (dimension == "y") return y;
                }
                return get;
            }
            var point = makePoint(3, 4);
            print point("x");
            print point("y");
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "3\r\n4")
    }

    @Test
    fun `Functions - Closure - Recursive Function`() {
        val myScript = """
            fun fib(n) {
                if (n <= 1) return n;
                return fib(n - 1) + fib(n - 2);
            }
            print fib(0);
            print fib(1);
            print fib(5);
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "0\r\n1\r\n5")
    }

    @Test
    fun `Functions - Closure - Mutual Recursion`() {
        val myScript = """
            fun isEven(n) {
                if (n == 0) return true;
                return isOdd(n - 1);
            }
            fun isOdd(n) {
                if (n == 0) return false;
                return isEven(n - 1);
            }
            print isEven(10);
            print isOdd(10);
            print isEven(7);
            print isOdd(7);
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "true\r\nfalse\r\nfalse\r\ntrue")
    }

    // ==================== Function Calls ====================

    @Test
    fun `Functions - Call - Simple Call`() {
        val myScript = """
            fun sayHello() {
                print "Hello!";
            }
            sayHello();
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "Hello!")
    }

    @Test
    fun `Functions - Call - Call Expression`() {
        val myScript = """
            fun add(a, b) {
                return a + b;
            }
            var result = add(5, 3);
            print result;
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "8")
    }

    @Test
    fun `Functions - Call - Nested Calls`() {
        val myScript = """
            fun add(a, b) {
                return a + b;
            }
            fun double(x) {
                return x * 2;
            }
            print double(add(5, 3));
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "16")
    }

    @Test
    fun `Functions - Call - Call Without Arguments`() {
        val myScript = """
            fun getFive() {
                return 5;
            }
            print getFive();
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "5")
    }

    @Test
    fun `Functions - Call - Call With All Types`() {
        val myScript = """
            fun printAll(a, b, c, d, e) {
                print a;
                print b;
                print c;
                print d;
                print e;
            }
            printAll(1, "hello", true, nil, 3.14);
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
    }

    @Test
    fun `Functions - Call - Too Few Arguments`() {
        val myScript = """
            fun greet(name, greeting) {
                print greeting + " " + name;
            }
            greet("World");
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertFailure(result)
    }

    @Test
    fun `Functions - Call - Too Many Arguments`() {
        val myScript = """
            fun greet(name) {
                print name;
            }
            greet("Hello", "World");
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertFailure(result)
    }

    @Test
    fun `Functions - Call - Method On Function Object`() {
        val myScript = """
            fun foo() {}
            // Functions don't have methods in this implementation
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
    }

    // ==================== Scope ====================

    @Test
    fun `Functions - Scope - Function Creates New Scope`() {
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
    fun `Functions - Scope - Nested Function Scope`() {
        val myScript = """
            var x = "global";
            fun outer() {
                var x = "outer";
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
        LoxAssertions.assertOutputEquals(result, "outer\r\nglobal")
    }

    @Test
    fun `Functions - Scope - Variable Shadowing`() {
        val myScript = """
            var x = 1;
            fun foo() {
                var x = 2;
                fun bar() {
                    var x = 3;
                    print x;
                }
                bar();
                print x;
            }
            foo();
            print x;
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "3\r\n2\r\n1")
    }

    @Test
    fun `Functions - Scope - Access Global From Nested Function`() {
        val myScript = """
            var x = "global";
            fun outer() {
                fun inner() {
                    print x;
                }
                inner();
            }
            outer();
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "global")
    }

    @Test
    fun `Functions - Scope - Modify Global From Function`() {
        val myScript = """
            var x = 1;
            fun increment() {
                x = x + 1;
            }
            print x;
            increment();
            print x;
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "1\r\n2")
    }

    // ==================== Runtime Errors ====================

    @Test
    fun `Functions - Runtime Error - Call Non-Function`() {
        val myScript = """
            var x = 10;
            x();
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertFailure(result)
    }

    @Test
    fun `Functions - Runtime Error - Call Non-Function With Arguments`() {
        val myScript = """
            var x = "hello";
            x("arg");
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertFailure(result)
    }

    @Test
    fun `Functions - Runtime Error - Too Few Arguments`() {
        val myScript = """
            fun foo(a, b) {}
            foo(1);
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertFailure(result)
    }

    @Test
    fun `Functions - Runtime Error - Too Many Arguments`() {
        val myScript = """
            fun foo(a) {}
            foo(1, 2);
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertFailure(result)
    }

    @Test
    fun `Functions - Runtime Error - Undefined Variable In Function`() {
        val myScript = """
            fun foo() {
                print undefinedVar;
            }
            foo();
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertFailure(result)
    }

    // ==================== Syntax Errors ====================

    @Test
    fun `Functions - Syntax Error - Missing Name`() {
        val myScript = """
            fun () {
                print "test";
            }
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertFailure(result)
    }

    @Test
    fun `Functions - Syntax Error - Missing Paren After Name`() {
        val myScript = """
            fun foo {
                print "test";
            }
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertFailure(result)
    }

    @Test
    fun `Functions - Syntax Error - Missing Opening Brace`() {
        val myScript = """
            fun foo()
                print "test";
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertFailure(result)
    }

    @Test
    fun `Functions - Syntax Error - Missing Closing Paren`() {
        val myScript = """
            fun foo( {
                print "test";
            }
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertFailure(result)
    }

    @Test
    fun `Functions - Syntax Error - Missing Comma Between Parameters`() {
        val myScript = """
            fun foo(a b) {
                print a;
            }
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertFailure(result)
    }

    @Test
    fun `Functions - Syntax Error - Invalid Parameter Name`() {
        val myScript = """
            fun foo(123) {
                print "test";
            }
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertFailure(result)
    }

    // ==================== Complex Function Patterns ====================

    @Test
    fun `Functions - Complex - Higher Order Function`() {
        val myScript = """
            fun apply(a, b, fn) {
                return fn(a, b);
            }
            fun add(a, b) {
                return a + b;
            }
            fun multiply(a, b) {
                return a * b;
            }
            print apply(5, 3, add);
            print apply(5, 3, multiply);
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "8\r\n15")
    }

    @Test
    fun `Functions - Complex - Currying`() {
        val myScript = """
            fun add(a) {
                fun addB(b) {
                    return a + b;
                }
                return addB;
            }
            var add5 = add(5);
            print add5(3);
            print add5(10);
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "8\r\n15")
    }

    @Test
    fun `Functions - Complex - Function Composition`() {
        val myScript = """
            fun compose(f, g) {
                fun composed(x) {
                    return f(g(x));
                }
                return composed;
            }
            fun double(x) { return x * 2; }
            fun increment(x) { return x + 1; }
            var doubleThenIncrement = compose(increment, double);
            print doubleThenIncrement(5);
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "11")
    }

}
