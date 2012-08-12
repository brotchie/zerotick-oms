name := "zerotick-oms"

version := "1.0"

scalaVersion := "2.9.2"

resolvers += "Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies += "com.typesafe.akka" % "akka-actor" % "2.0.2"

libraryDependencies += "com.ib" % "jtsclient" % "9.67"

libraryDependencies += "com.typesafe.akka" % "akka-zeromq" % "2.0.2"
