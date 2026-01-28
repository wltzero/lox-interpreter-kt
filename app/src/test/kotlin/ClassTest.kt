import org.junit.jupiter.api.Test
import utils.LoxAssertions
import utils.TestRunner


class ClassTest {
    private val testRunner = TestRunner

    @Test
    fun `Classes - Declearation - 1`() {
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
    fun `Classes - Declearation - 2`() {
        val myScript = """
            {
              // Class declaration inside blocks should work
              class Dinosaur {}
              print "Inside block: Dinosaur exists";
              print Dinosaur;
            }
            print "Accessing out-of-scope class:";
            print Dinosaur; // expect runtime error
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertFailure(result)
        LoxAssertions.assertErrorContains(result, "Undefined variable 'Dinosaur'")
    }

    @Test
    fun `Classes - Declearation - 3`() {
        val myScript = """
            // Class declaration inside function should work
            fun foo() {
              class Superhero {}
              print "Class declared inside function";
              print Superhero;
            }
            
            foo();
            print "Function called successfully";
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputLineCount(result, 3)
        LoxAssertions.assertOutputEquals(result, "Class declared inside function\r\nSuperhero\r\nFunction called successfully")
    }

    @Test
    fun `Classes - Instance - 1`() {
        val myScript = """
            // Creating multiple instances of the same class
            class Robot {}
            var r1 = Robot();
            var r2 = Robot();
            
            print "Created multiple robots:";
            print r1;
            print r2;
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputLineCount(result, 3)
        LoxAssertions.assertOutputEquals(result, "Created multiple robots:\r\nRobot instance\r\nRobot instance")
    }

    @Test
    fun `Classes - Instance - 2`() {
        val myScript = """
            class Wizard {}
            class Dragon {}
            
            // Instantiating classes in a function should work
            fun createCharacters() {
              var merlin = Wizard();
              var smaug = Dragon();
              print "Characters created in fantasy world:";
              print merlin;
              print smaug;
              return merlin;
            }
            
            var mainCharacter = createCharacters();
            // An instance of a class should be truthy
            if (mainCharacter) {
              print "The main character is:";
              print mainCharacter;
            } else {
              print "Failed to create a main character.";
            }
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputLineCount(result, 5)
        LoxAssertions.assertOutputEquals(result, "Characters created in fantasy world:\r\nWizard instance\r\nDragon instance\r\nThe main character is:\r\nWizard instance")
    }

    @Test
    fun `Classes - Instance - 3`() {
        val myScript = """
            class Superhero {}
            
            var count = 0;
            while (count < 3) {
              var hero = Superhero();
              print "Hero created:";
              print hero;
              count = count + 1;
            }
            
            print "All heroes created!";
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputLineCount(result, 7)
        LoxAssertions.assertOutputEquals(result, "Hero created:\r\nSuperhero instance\r\nHero created:\r\nSuperhero instance\r\nHero created:\r\nSuperhero instance\r\nAll heroes created!")
    }

    @Test
    fun `Classes - Getter & Setter - 1`() {
        val myScript = """
            // Multiple properties and conditional access
            class Robot {}
            var r2d2 = Robot();
            
            r2d2.model = "Astromech";
            r2d2.operational = false;
            
            if (r2d2.operational) {
              print r2d2.model;
              r2d2.mission = "Navigate hyperspace";
              print r2d2.mission;
            }
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
    }
    @Test
    fun `Classes - Getter & Setter - 2`() {
        val myScript = """
            // Multiple instances with properties
            class Superhero {}
            var batman = Superhero();
            var superman = Superhero();
            
            batman.name = "Batman";
            batman.called = 18;
            
            superman.name = "Superman";
            superman.called = 66;
            
            print "Times " + superman.name + " was called: ";
            print superman.called;
            print "Times " + batman.name + " was called: ";
            print batman.called;
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputLineCount(result, 4)
        LoxAssertions.assertOutputEquals(result, "Times Superman was called: \r\n66\r\nTimes Batman was called: \r\n18")
    }
    @Test
    fun `Classes - Getter & Setter - 3`() {
        val myScript = """
            // Property manipulation in functions
            class Wizard {}
            var gandalf = Wizard();
            
            gandalf.color = "Grey";
            gandalf.power = nil;
            print gandalf.color;
            
            fun promote(wizard) {
              wizard.color = "White";
              if (true) {
                wizard.power = 100;
              } else {
                wizard.power = 0;
              }
            }
            
            promote(gandalf);
            print gandalf.color;
            print gandalf.power;
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputLineCount(result, 3)
        LoxAssertions.assertOutputEquals(result, "Grey\r\nWhite\r\n100")
    }

    @Test
    fun `Classes - Instance Method - 1`() {
        val myScript = """
            {
              class Foo {
                returnSelf() {
                  return Foo;
                }
              }

              print Foo().returnSelf();  // expect: Foo
            }
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputLineCount(result, 1)
        LoxAssertions.assertOutputEquals(result, "Foo")
    }

    @Test
    fun `Classes - Instance Method - 2`() {
        val myScript = """
            class Wizard {
              castSpell(spell) {
                print "Casting a magical spell: " + spell;
              }
            }
            
            class Dragon {
              breatheFire(fire, intensity) {
                print "Breathing " + fire + " with intensity: "
                + intensity;
              }
            }
            
            // Methods on different class instances
            var merlin = Wizard();
            var smaug = Dragon();
            
            // Conditional method calling
            if (true) {
              var action = merlin.castSpell;
              action("Fireball");
            } else {
              var action = smaug.breatheFire;
              action("Fire", "100");
            }
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputLineCount(result, 1)
        LoxAssertions.assertOutputEquals(result, "Casting a magical spell: Fireball")
    }

    @Test
    fun `Classes - Instance Method - 3`() {
        val myScript = """
            class Superhero {
              useSpecialPower(hero) {
                print "Using power: " + hero.specialPower;
              }
            
              hasSpecialPower(hero) {
                return hero.specialPower;
              }
            
              giveSpecialPower(hero, power) {
                hero.specialPower = power;
              }
            }
            
            // Methods in functions
            fun performHeroics(hero, superheroClass) {
              if (superheroClass.hasSpecialPower(hero)) {
                superheroClass.useSpecialPower(hero);
              } else {
                print "No special power available";
              }
            }
            
            var superman = Superhero();
            var heroClass = Superhero();
            
            if (true) {
              heroClass.giveSpecialPower(superman, "Flight");
            } else {
              heroClass.giveSpecialPower(superman, "Strength");
            }
            
            performHeroics(superman, heroClass);
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputLineCount(result, 1)
        LoxAssertions.assertOutputEquals(result, "Using power: Flight")
    }
}