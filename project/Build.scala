import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "iitrpr"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    "org.mongodb" % "casbah_2.10" % "2.5.0",
    "com.novus" %% "salat-core" % "1.9.2-SNAPSHOT",
    "com.novus" %% "salat-util" % "1.9.2-SNAPSHOT",
    "se.radley" %% "play-plugins-salat" % "1.2-SNAPSHOT"
    
  )



   val main = play.Project(appName, appVersion, appDependencies).settings(
     resolvers ++= Seq(
      "sonatype repository" at "http://oss.sonatype.org/content/repositories/snapshots",
      "typesafe artefactory" at "http://typesafe.artifactoryonline.com/typesafe/repo",
      "jgit-repository" at "http://download.eclipse.org/jgit/maven")
  )

}
