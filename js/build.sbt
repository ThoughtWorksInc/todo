enablePlugins(ScalaJSPlugin)

libraryDependencies += "com.thoughtworks.binding" %%% "dom" % "11.8.1"

libraryDependencies += "com.thoughtworks.binding" %%% "route" % "11.8.1"

addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.1" cross CrossVersion.full)

crossPaths := false

crossTarget in fullOptJS := baseDirectory.value

crossTarget in fastOptJS := baseDirectory.value
