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
<<<<<<< HEAD
    cache
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here
=======
    cache,
    "com.google.code.gson" % "gson" % "2.2.4",
    "mysql" % "mysql-connector-java" % "5.1.25"
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here,
>>>>>>> f9bbff977c083469bdbf3bb2961ba03e91204cae
  )

}
