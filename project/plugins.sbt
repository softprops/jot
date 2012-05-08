libraryDependencies <+= sbtVersion("org.scala-sbt" %% "scripted-plugin" % _)

resolvers += Resolver.url("scalasbt", new URL(
  "http://scalasbt.artifactoryonline.com/scalasbt/sbt-plugin-releases"))(
  Resolver.ivyStylePatterns)

addSbtPlugin("com.jsuereth" % "xsbt-gpg-plugin" % "0.6")

resolvers += "coda" at "http://repo.codahale.com"

addSbtPlugin("me.lessis" % "ls-sbt" % "0.1.1")
