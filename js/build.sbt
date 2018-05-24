enablePlugins(ScalaJSPlugin)

libraryDependencies += "com.thoughtworks.binding" %%% "dom" % "11.0.1"

libraryDependencies += "com.thoughtworks.binding" %%% "route" % "11.0.1"

addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)

crossPaths := false

crossTarget in fullOptJS := baseDirectory.value

crossTarget in fastOptJS := baseDirectory.value

scalaJSLinkerConfig ~= {
  _.withESFeatures(_.withUseECMAScript2015(true))
}
