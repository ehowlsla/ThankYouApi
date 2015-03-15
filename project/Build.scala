import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "ThankYouApi"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
    javaCore,
    javaJdbc,
    javaEbean,
    cache,
    "com.google.code.gson" % "gson" % "2.2.4",
    "com.typesafe.akka" % "akka-actor_2.10"      % "2.1.2",
    "mysql" % "mysql-connector-java" % "5.1.25"
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here,
  )

}
