
http://agmenc.github.com/Pettswood/

*Current features*:
* Mixins allow explicit inclusion of the desired fixture
* Single and multi-row tables
* Nested tables (and nested nested tables, etc)
* Integrates with sbt, using the test-interface. Runs Pettswood tests when you run the "test" task.

*Usage*:
* Hosting on scala-tools is pending, so I'm afraid you'll need to clone it
* Add this to your build.sbt:
> "testFrameworks += new TestFramework("org.pettswood.runners.PettswoodFramework")"
* Create a test. Use "What is Pettswood.html" as a starting point.
* Copy the pettswood.css to the target directory (yes, I know I need to automate that)
* Write the Concepts to interpret the test. Use the code snippets in "What is Pettswood.html" as a starting point.

*Planned features* (and volunteers wanted):
* Includes, so that snippets of setup/tear-down can be commonalised
* New Concepts (test fixtures), to help with common tasks (web, DB, etc)
* Runners for other build environments (Ant, Maven)
* IDE integration (IntelliJ, Eclipse)
* WizzyWig (WYSIWYG) integration, so that tests can be edited in-browser
* New test creator, using WizzyWig.
* Other stuff I haven't thought of yet
