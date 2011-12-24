sbtPlugin := true

organization := "me.lessis"

name := "jot"

version <<= sbtVersion { v =>
  if(v.startsWith("0.11")) "0.1.0"
  else error("unsupported version of sbt %s" format v)
}

scalacOptions += "-deprecation"

seq(ScriptedPlugin.scriptedSettings: _*)

publishTo := Some("Scala Tools Nexus" at "http://nexus.scala-tools.org/content/repositories/releases/")
    
credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")

seq(lsSettings :_*)

homepage := Some(url("https://github.com/softprops/jot/"))

description := "Sbt interface for storing local project notes"

(LsKeys.tags in LsKeys.lsync) := Seq("sbt")
