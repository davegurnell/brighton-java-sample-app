name := "brighton-java-scala-talk"

organization := "underscore.io"

version := "0.0"



// Dependencies:

scalaVersion := "2.10.1"

libraryDependencies += "org.twitter4j" % "twitter4j-core" % "3.0.3"



// Eclipse settings:

EclipseKeys.withSource := true

EclipseKeys.createSrc := EclipseCreateSrc.Default + EclipseCreateSrc.Resource



// Intellij settings:

ideaExcludeFolders += ".idea"

ideaExcludeFolders += ".idea_modules"