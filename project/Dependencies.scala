import sbt._

object Dependencies {

  val Fs2Version = "3.2.4"
  val Log4CatsVersion = "2.3.1"
  val LogbackVersion = "1.2.3"
  val CatsRetryVersion = "3.1.0"

  val fs2 = Seq(
    "co.fs2" %% "fs2-core" % Fs2Version
  )

  val cats = Seq(
    "com.github.cb372" %% "cats-retry" % CatsRetryVersion
  )

  val logging = Seq(
    "org.typelevel" %% "log4cats-slf4j" % Log4CatsVersion,
    "ch.qos.logback" % "logback-classic" % LogbackVersion
  )
}
