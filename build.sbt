name := "VikaVersion"
 
version := "1.0" 
      
lazy val `vikaversion` = (project in file(".")).enablePlugins(PlayScala)

      
resolvers += "Akka Snapshot Repository" at "https://repo.akka.io/snapshots/"
      
scalaVersion := "2.13.5"

libraryDependencies ++= Seq(
  jdbc ,
  ehcache ,
  ws ,
  specs2 % Test ,
  guice ,
  "com.typesafe.play" %% "play-slick" % "5.0.0",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.3.3",
  "com.typesafe.slick" %% "slick-codegen" % "3.3.3",
  "org.postgresql" % "postgresql" % "42.2.23",
  "org.mindrot" % "jbcrypt" % "0.4",
)