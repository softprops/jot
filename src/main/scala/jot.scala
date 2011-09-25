package jot

import sbt._
import Project.Initialize
import java.io.File

object Plugin extends sbt.Plugin {
  import sbt.Keys._
  import JotKeys._

  object JotKeys {
    val jot = InputKey[Unit]("jot", "Appends an item your jot file")
    val rm = InputKey[Unit]("rm", "Removes an item from your jot file")
    val ls = TaskKey[Unit]("ls", "Enumerates the contents of your jot file")
    val clear = TaskKey[Unit]("clear", "Clears jot file")
    val jotFile = SettingKey[File]("jot-file", "File containing jotted items")
    val jotDirectory = SettingKey[File]("jot-directory", "Directory containing jot file")
    val colors = SettingKey[Boolean]("colors", "Toggles ansii colored output")
  }

  private def lines(f: File): Array[(String, Int)] = IO.read(f) match {
    case "" => Array.empty[(String, Int)]
    case str => str.split("\n\n").zipWithIndex
  }

  private def clearTask: Initialize[Task[Unit]] =
    (streams, jotFile in jot) map {
      (out, jf) =>
        IO.write(jf, "")
        out.log.info("jots cleared")
    }

  private def lsTask: Initialize[Task[Unit]] =
    (streams, jotFile in jot, colors in jot) map {
      (out, jf, clrs) =>
        out.log.info("jottings..")
        IO.touch(jf)
        lines(jf) match {
          case Array() => out.log.info("you have none. try `jot some ideas`")
          case ts => ts.foreach { _ match {
            case(l, n) => out.log.info(
              (if(clrs) "\033[0;36m%s\033[0m) \033[0;37m%s\033[0m" else "%s) %s") format(n, l)
            )
          } }
        }
    }

  def jotSettings: Seq[Setting[_]] =
    jotSettingsIn(Compile) ++ jotSettingsIn(Test)

  def jotSettingsIn(c: Configuration) = inConfig(c)(jotSettings0)

  def jotSettings0: Seq[Setting[_]] = Seq(
    jotDirectory in jot <<= (baseDirectory).identity,
    jotFile in jot <<= (jotDirectory in jot)(_ / ".jot"),
    colors in jot := true,
    jot <<= inputTask { (argsTask: TaskKey[Seq[String]]) =>
      (argsTask, streams, jotFile in jot, colors in jot) map { (args, out, jf, clrs) =>
        args.mkString(" ").trim match {
          case "" => out.log.error("usage: `jot some ideas`")
          case item =>
            IO.touch(jf)
            IO.append(jf, item + "\n\n")
            out.log.info(
              (if(clrs) "jotted, \033[0;37m%s\033[0m" else "jotted, %s") format item
            )
        }
      }
    },
    rm in jot <<= inputTask { (argsTask: TaskKey[Seq[String]]) =>
      (argsTask, streams, jotFile in jot, colors in jot) map { (args, out, jf, clrs) =>
        args match {
          case Seq(num) =>
            IO.touch(jf)
            val lns = lines(jf)
            lns match {
              case Array() => out.log.info("nothing to remove")
              case many => IO.write(jf, many.filter( _ match {
                case (l, n) if(n == num.toInt) =>
                  out.log.info(
                    (if(clrs) "removed, \033[0;37m%s\033[0m" else "removed, %s") format l
                  )
                  false
                case _ => true
              }).map(_._1) match {
                case Array() => ""
                case lns => lns.mkString("","\n\n","\n\n")
              })
            }
          case _ => out.log.error("usage: rm(for jot) <num>")
        }
      }
    },
    ls in jot <<= lsTask,
    clear in jot <<= clearTask
  )
}
