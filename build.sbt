val scala3Version = "3.0.2"

val v = new {
  val scalaCheckEffect = "1.0.3"
  val munitCatsEffect = "1.0.6"
}

lazy val root = project
  .in(file("."))
  .settings(
    name := "model-based-testing",
    version := "0.1.0-SNAPSHOT",
    scalaVersion := scala3Version,
    scalacOptions ++= Seq("-rewrite", "-indent"),
    libraryDependencies ++= Seq(
      "org.typelevel" %% "munit-cats-effect-3" % v.munitCatsEffect,
      "org.typelevel" %% "scalacheck-effect" % v.scalaCheckEffect,
      "org.typelevel" %% "scalacheck-effect-munit" % v.scalaCheckEffect
    ).map(_ % Test),
    Global / onChangedBuildSource := ReloadOnSourceChanges
  )
