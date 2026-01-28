import org.junit.jupiter.api.Test
import utils.LoxAssertions
import utils.TestRunner


class ClassTest {
    private val testRunner = TestRunner

    @Test
    fun `Classes - Basic class declaration and instantiation`() {
        val myScript = """
            // Multiple class declarations with empty body
            class Robot {}
            class Wizard {}
            print Robot;
            print Wizard;
            print "Both classes successfully printed";
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputLineCount(result, 3)
        LoxAssertions.assertOutputEquals(result, "Robot\r\nWizard\r\nBoth classes successfully printed")
    }

    @Test
    fun `Classes - Creating instances`() {
        val myScript = """
            class Bagel {}
            var bagel = Bagel();
            print bagel;
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputLineCount(result, 1)
        LoxAssertions.assertOutputEquals(result, "Bagel instance")
    }

    @Test
    fun `Classes - Properties on instances`() {
        val myScript = """
            class Bacon {
              eat() {
                print "Crunch crunch crunch!";
              }
            }
            Bacon().eat();
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputLineCount(result, 1)
        LoxAssertions.assertOutputEquals(result, "Crunch crunch crunch!")
    }
}