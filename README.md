[Documentation and Guides]: http://agmenc.github.com/Pettswood/index.html
[Project Wiki]: https://github.com/agmenc/Pettswood/wiki
[Pettswood]: https://github.com/agmenc/pettswood#readme
[WizzyWig]: http://www.agmen-smith.co.uk/wizzywig/wizzyWigBoot.js
[Pettswood Ecosystem]: https://github.com/agmenc/pettswood-ecosystem#readme

## Pettswood

[Pettswood] runs executable documentation: human-readable specs written by users, that run against your code as tests. This page is the quick-start guide. You may also be interested in:
* The full [Documentation and Guides]
* [WizzyWig] - an experimental in-browser script for editing HTML files
* The [Project Wiki]

### Quick Start

Add the library and test framework

```scala
libraryDependencies += "org.pettswood" %% "pettswood" % "0.0.12" % "test" withSources()

testFrameworks += new TestFramework("org.pettswood.runners.sbt.PettswoodFramework")
```

Create an integration hook anywhere in src/test/scala

```
import org.pettswood.runners.sbt.SbtIntegrationHook

class PettswoodSbt extends SbtIntegrationHook
```

### Create a test specification

Tests go in the resources directory:

```
[project root]/src/test/resources/Your test here.html
[project root]/src/test/resources/someFeature/Another test here.html
```

Copy this into [project root]/src/test/resources/MyFirstTest.html:

```html
<!DOCTYPE HTML>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
    <title>My First Pettswood Test</title>
    <link rel="stylesheet" type="text/css" href="css/pettswood.css"/>
    <script type="text/javascript" src="javascript/jquery-1.7.2.min.js"></script>
    <script type="text/javascript" src="javascript/pettswood.js"></script>
</head>
<body>
<h1>My First Pettswood Test</h1>
<table class="mixins">
    <tr>
        <td class="names">Mixins</td>
        <td>MyFirstMixin</td>
    </tr>
</table>
<table>
    <tr>
        <td class="fixture">Hello</td>
        <td>World</td>
    </tr>
</table>
</body>
</html>
```

### Tell Pettswood how to process your test

A *concept* bridges from Pettswood to your domain. Let's start with something trivial:
```scala
import org.pettswood.{Result, Concept}

class HelloWorld extends Concept {
  protected def cell(text: String) = Result.given(text, "World")
}
```

A *mixin* is a library of concepts:

```scala
import org.pettswood.specification.concepts.HelloWorld
import org.pettswood.{DomainBridge, Mixin}

class MyFirstMixin(domain: DomainBridge) extends Mixin(domain) {
  domain.learn("Hello", () => new HelloWorld())
}
```

### Run the test
#### In sbt
Run all tests (including Pettswood):

```
test
```

To run just the Pettswood tests, run the integration hook you created earlier:

```
test-only path.to.PettswoodSbt
```

#### From the IDE

Use SingleRunner class to run tests from your testing framework:

```scala
class SingleRunnerSpec extends Specification {
  "Frameworks such as Specs2 can run your test" in {
    SingleRunner("src/test/resources/AdvancedTopics.html").overallPass must beTrue
  }
}
```

Or use a mini-app:

```scala
object SingleTestRunner extends App {
  SingleRunner("src/test/resources/AdvancedTopics.html")
}
```

### Look at the test results

Either *click on the title*, or navigate to:

```
[project root]/target/pettswood/someFeature/Your test here.html
```

At this point, try putting something other than "World" in the second cell of the table, and re-run the test.


