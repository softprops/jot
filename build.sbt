sbtPlugin := true

organization := "me.lessis"

name := "jot"

version <<= sbtVersion { v =>
  if(v.startsWith("0.11") || v.startsWith("0.12") || v.startsWith("0.13")) "0.1.0"
  else error("unsupported version of sbt %s" format v)
}

sbtVersion in Global := "0.13.0-Beta2"

scalaVersion in Global := "2.10.2-RC2"

homepage := Some(url("https://github.com/softprops/jot/"))

description := "Sbt interface for storing local project notes"

scalacOptions += "-deprecation"

seq(ScriptedPlugin.scriptedSettings: _*)

seq(lsSettings :_*)

(LsKeys.tags in LsKeys.lsync) := Seq("sbt")

licenses <++= (version)(v => Seq("MIT" -> url(
  "https://github.com/softprops/jot/blob/%s/LICENSE".format(v))))

publishTo := Some(Classpaths.sbtPluginReleases)

publishMavenStyle := false

publishArtifact in Test := false

pomExtra := (
  <scm>
    <url>git@github.com:softprops/jot.git</url>
    <connection>scm:git:git@github.com:softprops/jot.git</connection>
  </scm>
  <developers>
    <developer>
      <id>softprops</id>
      <name>Doug Tangren</name>
      <url>https://github.com/softprops</url>
    </developer>
  </developers>
)
