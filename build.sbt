import SonatypeKeys._

sonatypeSettings

name := "pettswood"

organization := "com.github.agmenc"

version := "0.0.19"

scalaVersion := "2.10.4"

crossScalaVersions := Seq("2.10.4")

scalacOptions ++= Seq("-unchecked", "-Yrangepos")

testFrameworks += new TestFramework("org.pettswood.runners.sbt.PettswoodFramework")

// initialCommands in console := "println"

libraryDependencies ++= Seq(
  "net.sourceforge.htmlcleaner" % "htmlcleaner" % "2.2",
  "org.specs2" %% "specs2" % "2.3.11" % "test",
  "junit" % "junit" % "4.8.1" % "test" withSources(),
  "org.mockito" % "mockito-core" % "1.9.0-rc1" % "test",
  "org.scala-tools.testing" % "test-interface" % "0.5"
)

resolvers ++= Seq("snapshots-repo" at "https://oss.sonatype.org/content/repositories/snapshots/",
                   "releases-repo"  at "https://oss.sonatype.org/content/groups/scala-tools/",
                   "mvn" at "http://mvnrepository.com/artifact/")

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
