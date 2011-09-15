package jot

import sbt._
import Project.Initialize
import java.io.File

object Keys {
  val down = InputKey[Unit]("down", "Appends a thought to your jot file")
  val rm = InputKey[Unit]("rm", "Removes a thought from your jot file")
  val ls = TaskKey[Unit]("ls", "Lists your jotted thoughts")
  val clear = TaskKey[Unit]("clear", "Clears all jotted thoughts")
  val jotFile = SettingKey[File]("jot-file", "File containing thoughts")
  val jotDirectory = SettingKey[File]("jot-directory", "Directory containing jot file")
}

object Plugin extends sbt.Plugin {
  import sbt.Keys._
  import jot.Keys._

  val Jot = config("jot")

  private def lines(f: File): Array[(String, Int)] = IO.read(f) match {
    case "" => Array.empty[(String, Int)]
    case str => str.split("\n\n").zipWithIndex
  }

  private def clearTask: Initialize[Task[Unit]] =
    (streams, jotFile) map {
      (out, jf) =>
        IO.write(jf, "")
        out.log.info("jots cleared")
    }

  private def lsTask: Initialize[Task[Unit]] =
    (streams, jotFile) map {
      (out, jf) =>
        out.log.info("thoughts..")
        IO.touch(jf)
        lines(jf) match {
          case Array() => out.log.info("you have none")
          case ts => ts.foreach { _ match {
            case(l, n) => out.log.info("%s) %s" format(n, l))
          } }
        }
    }

  def options: Seq[Setting[_]] = inConfig(Jot)(Seq(
    jotDirectory <<= (baseDirectory).identity,
    jotFile <<= (jotDirectory)(_ / ".jot"),
    down <<= inputTask { (argsTask: TaskKey[Seq[String]]) =>
      (argsTask, streams, jotFile) map { (args, out, jf) =>
        val thought = args.mkString(" ")
        IO.touch(jf)
        IO.append(jf, thought + "\n\n")
        out.log.info("jotted down thought, %s" format thought)
      }
    },
    rm <<= inputTask { (argsTask: TaskKey[Seq[String]]) =>
      (argsTask, streams, jotFile) map { (args, out, jf) =>
        args match {
          case Seq(num) =>
            IO.touch(jf)
            val lns = lines(jf)
            lns match {
              case Array() => out.log.info("no thoughts to remove")
              case many => IO.write(jf, many.filter( _ match {
                case (l, n) if(n == num.toInt) =>
                  out.log.info("removed the thought, %s" format l)
                  false
                case _ => true
              }).map(_._1)
                .mkString("","\n\n","\n\n")
              )
            }
        }
      }
    },
    ls <<= lsTask,
    clear <<= clearTask
  ))
}
