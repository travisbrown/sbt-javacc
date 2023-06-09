ThisBuild / organization := "dev.travisbrown"
ThisBuild / scalaVersion := "2.12.18"
ThisBuild / githubWorkflowPublishTargetBranches := Nil

val compilerOptions = Seq(
  "-deprecation",
  "-encoding",
  "UTF-8",
  "-feature",
  "-language:existentials",
  "-language:higherKinds",
  "-unchecked",
  "-Ywarn-dead-code",
  "-Ywarn-numeric-widen",
  "-Xfuture",
  "-Yno-adapted-args",
  "-Ywarn-unused-import"
)

val baseSettings = Seq(
  scalacOptions ++= compilerOptions,
  Compile / console / scalacOptions ~= {
    _.filterNot(Set("-Ywarn-unused-import", "-Ywarn-unused:imports"))
  },
  Test / console / scalacOptions ~= {
    _.filterNot(Set("-Ywarn-unused-import", "-Ywarn-unused:imports"))
  }
)

val allSettings = baseSettings ++ publishSettings

lazy val root = project
  .in(file("."))
  .enablePlugins(SbtPlugin)
  .settings(allSettings)
  .settings(
    name := "sbt-javacc",
    libraryDependencies += "net.java.dev.javacc" % "javacc" % "7.0.10"
  )

lazy val publishSettings = Seq(
  releaseCrossBuild := true,
  releasePublishArtifactsAction := PgpKeys.publishSigned.value,
  homepage := Some(url("https://github.com/travisbrown/sbt-javacc")),
  licenses := Seq("Apache 2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0")),
  publishMavenStyle := true,
  Test / publishArtifact := false,
  pomIncludeRepository := { _ => false },
  publishTo := {
    val nexus = "https://oss.sonatype.org/"
    if (isSnapshot.value)
      Some("snapshots".at(nexus + "content/repositories/snapshots"))
    else
      Some("releases".at(nexus + "service/local/staging/deploy/maven2"))
  },
  autoAPIMappings := true,
  apiURL := Some(url("https://github.com/travisbrown/sbt-javacc")),
  scmInfo := Some(
    ScmInfo(
      url("https://github.com/travisbrown/sbt-javacc"),
      "scm:git:git@github.com:travisbrown/sbt-javacc.git"
    )
  ),
  developers := List(
    Developer(
      "travisbrown",
      "Travis Brown",
      "travisrobertbrown@gmail.com",
      url("https://twitter.com/travisbrown")
    )
  )
)
