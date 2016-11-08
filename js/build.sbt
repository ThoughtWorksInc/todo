enablePlugins(ScalaJSPlugin)

libraryDependencies += "com.thoughtworks.binding" %%% "dom" % "10.0.0-M8"

libraryDependencies += "com.thoughtworks.binding" %%% "route" % "10.0.0-M8"

addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)

crossPaths := false

crossTarget in fullOptJS := baseDirectory.value

crossTarget in fastOptJS := baseDirectory.value
