enablePlugins(ScalaJSPlugin)

libraryDependencies += "org.lrng.binding" %%% "html" % "1.0.3"

libraryDependencies += "com.thoughtworks.binding" %%% "route" % "12.0.0"

libraryDependencies += "com.lihaoyi" %%% "upickle" % "1.4.3"

scalacOptions += "-Ymacro-annotations"

crossPaths := false

crossTarget in fullOptJS := baseDirectory.value

crossTarget in fastOptJS := baseDirectory.value
