enablePlugins(ScalaJSPlugin)

libraryDependencies += "com.yang-bo" %%% "html" % "3.0.0"

libraryDependencies += "com.thoughtworks.binding" %%% "binding" % "12.2.0"

libraryDependencies += "com.thoughtworks.binding" %%% "bindable" % "2.2.0"

libraryDependencies += "com.thoughtworks.binding" %%% "latestevent" % "2.0.0"

libraryDependencies += "com.lihaoyi" %%% "upickle" % "2.0.0"

scalacOptions += "-Ymacro-annotations"

crossPaths := false

crossTarget in fullOptJS := baseDirectory.value

crossTarget in fastOptJS := baseDirectory.value
