name := "tryLWJGL"

version := "0.1"

scalaVersion := "2.13.0"

libraryDependencies ++= {
  val version = "3.2.3"
  val os = "windows" // TODO: Change to "linux" or "mac" if necessary

  Seq(
    "lwjgl",
    "lwjgl-glfw",
    "lwjgl-opengl"
    // TODO: Add more modules here
  ).flatMap {
    module => {
      Seq(
        "org.lwjgl" % module % version,
        "org.lwjgl" % module % version classifier s"natives-$os"
      )
    }
  }
}