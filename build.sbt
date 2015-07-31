lazy val root = (project in file(".")).
  settings(
    name := "bcrypt-test",
    version := "1.0",
    scalaVersion := "2.11.0"
  )

libraryDependencies ++= Seq(
  "org.springframework.security" % "spring-security-crypto" % "4.0.1.RELEASE"
)
