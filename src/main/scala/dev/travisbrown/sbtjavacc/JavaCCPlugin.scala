package dev.travisbrown.sbtjavacc

import org.javacc.parser.Main
import sbt._
import Keys._

object JavaCCPlugin extends AutoPlugin {
  object autoImport {
    val javacc = taskKey[Unit]("Generate JavaCC parsers")
    val javaccStatic = settingKey[Boolean]("Generate static JavaCC parser")
    val javaccSource = settingKey[File]("JavaCC source directory")
    val javaccOutput = settingKey[File]("JavaCC output directory")
  }

  import autoImport._

  override lazy val projectSettings = inConfig(Compile)(
    Seq(
      javaccStatic := false,
      javaccSource := baseDirectory.value / "src" / "main" / "javacc",
      javaccOutput := baseDirectory.value / "target" / "javacc",
      javacc := run(javaccSource.value, javaccOutput.value, javaccStatic.value),
      (Compile / unmanagedSourceDirectories) += javaccOutput.value,
      compileOrder := CompileOrder.JavaThenScala
    )
  )

  def run(sourceDirectory: File, output: File, isStatic: Boolean): Unit = {
    val sourceFinder = sourceDirectory * "*.jj"

    sourceFinder.get.foreach { source =>
      val args = Array[String](
        s"-STATIC=$isStatic",
        s"-OUTPUT_DIRECTORY=${output.getAbsolutePath}",
        source.getAbsolutePath
      )
      Main.mainProgram(args)
    }
  }
}
