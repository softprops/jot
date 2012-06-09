
//libraryDependencies <+= sbtVersion(v=>
//  v.split('.') match {
//    case Array("0", "11", "2") =>
//      "org.scala-tools.sbt" %% "scripted-plugin" % v
//    case Array("0", "11", "3") =>
//      "org.scala-sbt" %% "scripted-plugin" % v
//    case e =>
//      Defaults.sbtPluginExtra(
//        "org.scala-sbt" % "scripted-plugin" % v,
//        v,
//        "2.9.2")
//  }
//)

resolvers += Resolver.url("scalasbt", new URL(
  "http://scalasbt.artifactoryonline.com/scalasbt/sbt-plugin-releases"))(
  Resolver.ivyStylePatterns)

addSbtPlugin("com.jsuereth" % "xsbt-gpg-plugin" % "0.6")

resolvers += "coda" at "http://repo.codahale.com"

//addSbtPlugin("me.lessis" % "ls-sbt" % "0.1.1")
