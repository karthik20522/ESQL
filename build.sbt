name := "ESQL"

version := "1.0"

scalaVersion := "2.11.4"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

libraryDependencies ++= {
  Seq(
	"org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.2",
	"org.elasticsearch" % "elasticsearch" % "1.3.2",
    "org.scalatest" % "scalatest_2.11" % "2.2.1" % "test",
    "org.mockito" % "mockito-core" % "1.10.8"
  )
}