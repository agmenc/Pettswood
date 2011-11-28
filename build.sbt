name := "pettswood"

organization := "org.pettswood"

version := "0.0.1"

scalaVersion := "2.9.1"

retrieveManaged := true

testFrameworks += new TestFramework("org.pettswood.runners.PettswoodFramework")

// initialCommands in console := "println"

libraryDependencies ++= Seq(
  "org.specs2" %% "specs2" % "1.6.1" % "test" withSources(),
  "org.specs2" %% "specs2-scalaz-core" % "6.0.1" % "test" withSources(),
  "junit" % "junit" % "4.8.1" % "test" withSources(),
  "org.mockito" % "mockito-core" % "1.9.0-rc1" % "test" withSources(),
  "org.scala-tools.testing" % "test-interface" % "0.5"
)

resolvers ++= Seq("snapshots" at "http://scala-tools.org/repo-snapshots",
                    "releases"  at "http://scala-tools.org/repo-releases")

// --------- Publishing -----------------------
publishTo := Some(Resolver.file("file", new File("releases")))

// disable publishing the main API jar - may need to re-enable this for some of the bureaucracy-heavy hosting sites
publishArtifact in (Compile, packageDoc) := false


