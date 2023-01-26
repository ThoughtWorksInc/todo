enablePlugins(ScalaJSPlugin)

libraryDependencies += "com.yang-bo" %%% "html" % "3.0.0-M0+68-748a5ab9"

libraryDependencies += "com.thoughtworks.binding" %%% "binding" % "12.1.0+116-c25b3725"

libraryDependencies += "com.thoughtworks.binding" %%% "bindable" % "2.1.3+81-8ac54bf7"

libraryDependencies += "com.thoughtworks.binding" %%% "latestevent" % "2.0.0-M0+2-3c29b239"

libraryDependencies += "com.lihaoyi" %%% "upickle" % "2.0.0"

scalacOptions += "-Ymacro-annotations"

crossPaths := false

crossTarget in fullOptJS := baseDirectory.value

crossTarget in fastOptJS := baseDirectory.value
