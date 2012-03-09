sbtPlugin := true

organization := "me.lessis"

name := "jot"

version <<= sbtVersion { v =>
  if(v.startsWith("0.11")) "0.1.0"
  else error("unsupported version of sbt %s" format v)
}

homepage := Some(url("https://github.com/softprops/jot/"))

description := "Sbt interface for storing local project notes"

scalacOptions += "-deprecation"

seq(ScriptedPlugin.scriptedSettings: _*)

seq(lsSettings :_*)

(LsKeys.tags in LsKeys.lsync) := Seq("sbt")

licenses <++= (version)(v => Seq("MIT" -> url(
  "https://github.com/softprops/jot/blob/%s/LICENSE".format(v))))

publishTo := Some(Resolver.url("sbt-plugin-releases", url(
  "http://scalasbt.artifactoryonline.com/scalasbt/sbt-plugin-releases/"
))(Resolver.ivyStylePatterns))

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
