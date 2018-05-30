name := "quickstart"

version := "1.0"

libraryDependencies += "com.twitter" %% "finagle-http" % "18.5.0"
libraryDependencies += "com.twitter" %% "finagle-redis" % "18.5.0"
libraryDependencies += "io.spray" %%  "spray-json" % "1.3.3"
libraryDependencies += "com.twitter" %% "twitter-server" % "18.5.0"
libraryDependencies += "com.twitter" %% "finatra-http" % "18.5.0"
libraryDependencies += "com.twitter" %% "inject-core" % "18.5.0"  % "test" classifier "tests"
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.3"
libraryDependencies += "com.typesafe" % "config" % "1.3.2"