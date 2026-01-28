import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import statement.GlobalEnvironment
import utils.LoxAssertions
import utils.TestRunner


class ClassTest {
    private val testRunner = TestRunner

    @BeforeEach
    fun setUp() {
        GlobalEnvironment.reset()
    }

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

    @Test
    fun `Classes - This - 1`() {
        val myScript = """
            class Calculator {
              add(a, b) {
                return a + b + this.memory;
              }
            }
            
            var calc = Calculator();
            calc.memory = 82;
            print calc.add(92, 1);
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputLineCount(result, 1)
        LoxAssertions.assertOutputEquals(result, "175")
    }

    @Test
    fun `Classes - This - 2`() {
        val myScript = """
            class Animal {
              makeSound() {
                print this.sound;
              }
              identify() {
                print this.species;
              }
            }
            
            var dog = Animal();
            dog.sound = "Woof";
            dog.species = "Dog";
            
            var cat = Animal();
            cat.sound = "Meow";
            cat.species = "Cat";
            
            // Swap methods between instances
            cat.makeSound = dog.makeSound;
            dog.identify = cat.identify;
            
            cat.makeSound();
            dog.identify();
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputLineCount(result, 2)
        LoxAssertions.assertOutputEquals(result, "Woof\r\nCat")
    }

    @Test
    fun `Classes - This - 3`() {
        val myScript = """
            class Wizard {
              getSpellCaster() {
                fun castSpell() {
                  print this;
                  print "Casting spell as " + this.name;
                }
            
                return castSpell;
              }
            }
            
            var wizard = Wizard();
            wizard.name = "Merlin";
            wizard.getSpellCaster()();
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputLineCount(result, 2)
        LoxAssertions.assertOutputEquals(result, "Wizard instance\r\nCasting spell as Merlin")
    }

    @Test
    fun `Classes - Invalid usages of This - 1`() {
        val myScript = """
            fun notAMethod() {
              print this;
            }
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertFailure(result)
        LoxAssertions.assertErrorContains(result, "Can't use 'this' outside of a class.")
    }

    @Test
    fun `Classes - Invalid usages of This - 2`() {
        val myScript = """
            class Person {
              sayName() {
                print this();
              }
            }
            Person().sayName();
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertFailure(result)
        LoxAssertions.assertErrorContains(result, "Can only call functions or classes.")
    }

    @Test
    fun `Classes - Invalid usages of This - 3`() {
        val myScript = """
            class Confused {
              method() {
                fun inner(instance) {
                  var feeling = "confused";
                  print this.feeling;
                }
                return inner;
              }
            }

            var instance = Confused();
            var m = instance.method();
            m(instance);
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertFailure(result)
        LoxAssertions.assertErrorContains(result, "Undefined property 'feeling'.")
    }

    @Test
    fun `Classes - Constructor - return value`() {
        val myScript = """
            class ThingDefault {
              init() {
                this.x = "foo";
                return this;
              }
            }
            var out = ThingDefault();
            print out;
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertFailure(result)
        LoxAssertions.assertErrorContains(result, "Can't return a value from an initializer.")
    }

    @Test
    fun `Classes - Constructor - empty return allowed`() {
        val myScript = """
            class Person {
              init() {
                print "quz";
                return;
              }
            }

            Person();
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputEquals(result, "quz")
    }

    @Test
    fun `Classes - Constructor - 1`() {
        val myScript = """
            class Robot {
              init(model, function) {
                this.model = model;
                this.function = function;
              }
            }
            print Robot("R2-D2", "Astromech").model;
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputLineCount(result, 1)
        LoxAssertions.assertOutputEquals(result, "R2-D2")
    }

    @Test
    fun `Classes - Constructor - 2`() {
        val myScript = """
            class Counter {
              init(startValue) {
                if (startValue < 0) {
                  print "startValue can't be negative";
                  this.count = 0;
                } else {
                  this.count = startValue;
                }
              }
            }

            var instance = Counter(-52);
            print instance.count;
            print instance.init(52).count;
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputLineCount(result, 3)
        LoxAssertions.assertOutputEquals(result, "startValue can't be negative\r\n0\r\n52")
    }

    @Test
    fun `Classes - Constructor - 3`() {
        val myScript = """
            class Vehicle {
              init(type) {
                this.type = type;
              }
            }

            class Car {
              init(make, model) {
                this.make = make;
                this.model = model;
                this.wheels = "four";
              }

              describe() {
                print this.make + " " + this.model +
                " with " + this.wheels + " wheels";
              }
            }

            var vehicle = Vehicle("Generic");
            print "Generic " + vehicle.type;

            var myCar = Car("Toyota", "Corolla");
            myCar.describe();
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputLineCount(result, 2)
        LoxAssertions.assertOutputEquals(result, "Generic Generic\r\nToyota Corolla with four wheels")
    }

    @Test
    fun `Classes - Inheritance - multiple subclasses`() {
        val myScript = """
            class A {}

            // B is a subclass of A
            class B < A {}

            // C is also a subclass of A
            class C < A {}

            print A();
            print B();
            print C();
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputLineCount(result, 3)
        LoxAssertions.assertOutputEquals(result, "A instance\r\nB instance\r\nC instance")
    }

    @Test
    fun `Classes - Inheritance - multi-level inheritance`() {
        val myScript = """
            class Vehicle {}

            // Car is a subclass of Vehicle
            class Car < Vehicle {}

            // Sedan is a subclass of Car
            class Sedan < Car {}

            print Vehicle();
            print Car();
            print Sedan();

            {
              // Truck is a subclass of Vehicle
              class Truck < Vehicle {}
              print Truck();
            }
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputLineCount(result, 4)
        LoxAssertions.assertOutputEquals(result, "Vehicle instance\r\nCar instance\r\nSedan instance\r\nTruck instance")
    }

    @Test
    fun `Classes - Inheritance - method inheritance`() {
        val myScript = """
            class Root {
              getName() {
                print "Root class";
              }
            }

            class Parent < Root {
              parentMethod() {
                print "Method defined in Parent";
              }
            }

            class Child < Parent {
              childMethod() {
                print "Method defined in Child";
              }
            }

            var root = Root();
            var parent = Parent();
            var child = Child();

            // Root methods are available to all
            root.getName();
            parent.getName();
            child.getName();

            // Parent methods are available to Parent and Child
            parent.parentMethod();
            child.parentMethod();

            // Child methods are only available to Child
            child.childMethod();
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputLineCount(result, 6)
        LoxAssertions.assertOutputEquals(result, "Root class\r\nRoot class\r\nRoot class\r\nMethod defined in Parent\r\nMethod defined in Parent\r\nMethod defined in Child")
    }

    @Test
    fun `Classes - Inheritance - constructor inheritance`() {
        val myScript = """
            class Foo {
              init() {
                this.secret = 42;
              }
            }

            // Bar is a subclass of Foo
            class Bar < Foo {}

            // Baz is a subclass of Bar
            class Baz < Bar {}

            var baz = Baz();

            // Baz should inherit the constructor from Foo
            // which should set the secret value to 42
            print baz.secret;
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputLineCount(result, 1)
        LoxAssertions.assertOutputEquals(result, "42")
    }

    @Test
    fun `Classes - Inheritance - constructor override`() {
        val myScript = """
            class Base {
              init(a) {
                this.a = a;
              }
            }

            // Constructors can also be overridden
            class Derived < Base {
              init(a, b) {
                this.a = a;
                this.b = b;
              }
            }

            var derived = Derived(89, 32);
            print derived.a;
            print derived.b;
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputLineCount(result, 2)
        LoxAssertions.assertOutputEquals(result, "89\r\n32")
    }

    @Test
    fun `Classes - Inheritance - method override with this`() {
        val myScript = """
            class Animal {
              speak() {
                return "Animal speaks";
              }

              makeSound() {
                return "Generic sound";
              }

              communicate() {
                return this.speak() + " : " + this.makeSound();
              }
            }

            class Dog < Animal {
              speak() {
                return "Dog speaks";
              }

              makeSound() {
                return "Woof";
              }
            }

            class Puppy < Dog {
              speak() {
                return "Puppy speaks";
              }
            }

            var animal = Animal();
            var dog = Dog();
            var puppy = Puppy();

            print animal.communicate();
            print dog.communicate();
            print puppy.communicate();
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputLineCount(result, 3)
        LoxAssertions.assertOutputEquals(result, "Animal speaks : Generic sound\r\nDog speaks : Woof\r\nPuppy speaks : Woof")
    }

    @Test
    fun `Classes - Inheritance - superclass must be class - function`() {
        val myScript = """
            fun A() {}

            // A class can only inherit from a class.
            class B < A {} // expect runtime error

            print A();
            print B();
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertFailure(result, 70)
        LoxAssertions.assertErrorContains(result, "Superclass must be a class")
    }

    @Test
    fun `Classes - Inheritance - superclass must be class - variable`() {
        val myScript = """
            var A = "class";

            // A class can only inherit from a class
            class B < A {} // expect runtime error

            print B();
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertFailure(result, 70)
        LoxAssertions.assertErrorContains(result, "Superclass must be a class")
    }

    @Test
    fun `Classes - Inheritance - self inheritance`() {
        val myScript = """
            // A class can't inherit from itself.
            class Foo < Foo {} // expect compile error
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertFailure(result, 65)
        LoxAssertions.assertErrorContains(result, "A class can't inherit from itself.")
    }

    @Test
    fun `Classes - Inheritance - super keyword`() {
        val myScript = """
            class Base {
              method() {
                print "Base.method()";
              }
            }

            class Parent < Base {
              method() {
                super.method();
              }
            }

            class Child < Parent {
              method() {
                super.method();
              }
            }

            var parent = Parent();
            parent.method();
            var child = Child();
            child.method();
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertSuccess(result)
        LoxAssertions.assertOutputLineCount(result, 2)
        LoxAssertions.assertOutputEquals(result, "Base.method()\r\nBase.method()")
    }

    @Test
    fun `Classes - Invalid usages of super - outside class`() {
        val myScript = """
            // super can't be used outside of a class
            super.notEvenInAClass(); // expect compile error
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertFailure(result, 65)
        LoxAssertions.assertErrorContains(result, "Can't use 'super' outside of a class.")
    }

    @Test
    fun `Classes - Invalid usages of super - no dot`() {
        val myScript = """
            class A {}

            class B < A {
              method() {
                // super must be followed by `.`
                // and an expression
                super; // expect compile error
              }
            }
        """.trimIndent()

        val result = testRunner.run(myScript)

        LoxAssertions.assertFailure(result, 65)
        LoxAssertions.assertErrorContains(result, "Expect '.' after 'super'.")
    }
}