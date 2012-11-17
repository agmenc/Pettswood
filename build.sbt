name := "pettswood"

organization := "com.github.agmenc"

version := "0.0.12"

crossScalaVersions := Seq("2.9.1", "2.9.2", "2.10.0-M7")

retrieveManaged := true

scalacOptions ++= Seq("-unchecked")

testFrameworks += new TestFramework("org.pettswood.runners.sbt.PettswoodFramework")

// initialCommands in console := "println"

libraryDependencies ++= Seq(
  "net.sourceforge.htmlcleaner" % "htmlcleaner" % "2.2" withSources(),
  "org.specs2" %% "specs2" % "1.11" % "test" withSources(),
  "org.specs2" %% "specs2-scalaz-core" % "6.0.1" % "test" withSources(),
  "junit" % "junit" % "4.8.1" % "test" withSources(),
  "org.mockito" % "mockito-core" % "1.9.0-rc1" % "test" withSources(),
  "org.scala-tools.testing" % "test-interface" % "0.5"
)

resolvers ++= Seq("snapshots-repo" at "https://oss.sonatype.org/content/repositories/snapshots/",
                   "releases-repo"  at "https://oss.sonatype.org/content/groups/scala-tools/",
                   "mvn" at "http://mvnrepository.com/artifact/")

// --------- Publishing -----------------------
publishTo <<= version { v: String =>
  val nexus = "https://oss.sonatype.org/"
  if (v.trim.endsWith("SNAPSHOT")) Some("snapshots" at nexus + "content/repositories/snapshots")
  else Some("releases" at nexus + "service/local/staging/deploy/maven2")
}

publishMavenStyle := true

publishArtifact in Test := false

pomIncludeRepository := { x => false }

pomExtra := (
  <url>https://github.com/agmenc/Pettswood</url>
  <licenses>
    <license>
      <name>GPL version 3 or any later version</name>
      <url>http://www.gnu.org/licenses/gpl.html</url>
      <distribution>repo</distribution>
    </license>
  </licenses>
  <scm>
    <url>https://github.com/agmenc/Pettswood</url>
    <connection>scm:git:git@github.com:agmenc/Pettswood.git</connection>
  </scm>
  <developers>
    <developer>
      <id>agmenc</id>
      <name>Chris Agmen-Smith</name>
      <url>https://github.com/agmenc/Pettswood</url>
    </developer>
  </developers>
)