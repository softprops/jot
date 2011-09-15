package jot

import sbt._
import java.io.File

object Keys {
  val add = InputKey[Unit]("add", "Appends a thought to your jot file")
  val rm = InputKey[Unit]("rm", "Removes a thought from your jot file")
  val ls = InputKey[Unit]("ls", "Lists your jotted thoughts")
  val jotFile = SettingKey[File]("jot-file", "File containing thoughts")
  val jotDirectory = SettingKey[File]("jot-directory", "Directory containing jot file")
}

object Plugin extends sbt.Plugin {
  import sbt.Keys._
  import jot.Keys._

  val Jot = config("jot")

  def options: Seq[Setting[_]] = inConfig(Jot)(Seq(
    jotDirectory <<= (baseDirectory).identity,
    jotFile <<= (jotDirectory)(_ / ".jot"),
    add <<= inputTask { (argsTask: TaskKey[Seq[String]]) =>
      (argsTask, streams, jotFile) map { (args, out, jf) =>
        out.log.info("add thought '%s'" format args.mkString(" "))
      }
    },
    rm <<= inputTask { (argsTask: TaskKey[Seq[String]]) =>
      (argsTask, streams, jotFile) map { (args, out, jf) =>
        args match {
          case Seq(num) =>
            out.log.info("rm thought %s" format num)
        }
      }
    },
    ls <<= inputTask { (argsTask: TaskKey[Seq[String]]) =>
      (argsTask, streams, jotFile) map { (args, out, jf) =>
        out.log.info("ls ...")
      }
    }
  ))
}
