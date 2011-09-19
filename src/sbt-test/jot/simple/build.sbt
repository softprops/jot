seq(jot.Plugin.options:_*)

InputKey[Unit]("empty") <<= inputTask { (argsTask: TaskKey[Seq[String]]) =>
  (argsTask, streams) map {
    (args, out) =>
      args match {
        case Seq(path) =>
          IO.read(file(path)) match {
            case "" => out.log.debug("File %s was empty" format path)
            case contents => error(
              "Empty should have been empty but instead contained %s" format contents
            )
          }
        case xs => error("usage: empty path")
      }
  }
}

InputKey[Unit]("contents") <<= inputTask { (argsTask: TaskKey[Seq[String]]) =>
  (argsTask, streams) map {
    (args, out) =>
      args match {
        case Seq(given, expected) =>
          if(IO.read(file(given)).equals(IO.read(file(expected)))) out.log.debug(
            "Contents match"
          )
          else error(
            "Contents of (%s)\n%s does not match (%s)\n%s" format(
              given, IO.read(file(given)), expected, IO.read(file(expected))
            )
          )
      }
  }
}

