import SonatypeKeys._

sonatypeSettings

name := "pettswood"

organization := "com.github.agmenc"

version := "0.0.21"

scalaVersion := "2.11.7"

crossScalaVersions := Seq("2.10.5", "2.11.7")

scalacOptions ++= Seq("-unchecked", "-Yrangepos")

testFrameworks += new TestFramework("org.pettswood.runners.sbt.PettswoodFramework")

// initialCommands in console := "println"

libraryDependencies ++= Seq(
  "net.sourceforge.htmlcleaner" % "htmlcleaner" % "2.8",
  "org.specs2" %% "specs2" % "2.3.11" % "test",
  "junit" % "junit" % "4.8.1" % "test" withSources(),
  "org.mockito" % "mockito-core" % "1.9.0-rc1" % "test",
  "org.scala-tools.testing" % "test-interface" % "0.5"
)

// add scala-xml dependency when needed (for Scala 2.11 and newer)
// this mechanism supports cross-version publishing
libraryDependencies := {
  CrossVersion.partialVersion(scalaVersion.value) match {
    case Some((2, scalaMajor)) if scalaMajor >= 11 => libraryDependencies.value :+ "org.scala-lang.modules" %% "scala-xml" % "1.0.4"
    case _ => libraryDependencies.value
  }
}

// --------- Publishing -----------------------

pomExtra := {
  <url>https://github.com/agmenc/Pettswood</url>
  <licenses>
    <license>
      <name>GPL version 3 or any later version</name>
      <url>http://www.gnu.org/licenses/gpl.html</url>
      <distribution>repo</distribution>
    </license>
  </licenses>
  <scm>
    <url>git@github.com:agmenc/Pettswood</url>
    <developerConnection>scm:git:git@github.com:agmenc/Pettswood.git</developerConnection>
    <connection>scm:git:git@github.com:agmenc/Pettswood.git</connection>
  </scm>
  <developers>
    <developer>
      <id>agmenc</id>
      <name>Chris Agmen-Smith</name>
      <url>https://github.com/agmenc/Pettswood</url>
    </developer>
  </developers>
}
