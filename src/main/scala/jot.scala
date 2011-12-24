package jot

object Plugin extends sbt.Plugin {
  import sbt._
  import sbt.Project.Initialize
  import sbt.Keys._
  import JotKeys.{ jot => j, _ }
  import java.io.File

  object JotKeys {
    val jot = InputKey[Unit]("jot", "Appends an item your jot file")
    val drop = InputKey[Unit](key("drop"), "Removes an item from your jot file")
    val cat = TaskKey[Unit](key("cat"), "Enumerates the contents of your jot file")
    val jotFile = SettingKey[File](key("file"), "File containing jotted items")
    val colors = SettingKey[Boolean]("colors", "Toggles ansii colored output")

    private def key(name: String) = "jot-%s" format name
  }

  private def lines(f: File): Array[(String, Int)] =
    IO.read(f) match {
      case "" => Array.empty[(String, Int)]
      case str => str.split("\n\n").zipWithIndex
    }

  private def cleanTask: Initialize[Task[Unit]] =
    (streams, jotFile) map {
      (out, jf) =>
        IO.write(jf, "")
        out.log.info("Jots cleared")
    }

  private def catTask: Initialize[Task[Unit]] =
    (streams, jotFile, colors in j) map {
      (out, jf, clrs) =>
        out.log.info("Jottings..")
        IO.touch(jf)
        lines(jf) match {
          case Array() => out.log.info("You have none. Try `jot some ideas`")
          case ts => ts.foreach {
            _ match {
              case(l, n) => out.log.info(
                (if(clrs) "\033[0;36m%s\033[0m) \033[0;37m%s\033[0m" else "%s) %s") format(n, l)
              )
            }
          }
        }
    }

  def jotSettings: Seq[Setting[_]] =
    jotSettingsIn(Compile) ++ jotSettingsIn(Test)

  def jotSettingsIn(c: Configuration) = inConfig(c)(jotSettings0) ++ Seq(
    clean in j <<= (clean in (c, j))
  )

  def jotSettings0: Seq[Setting[_]] = Seq(
    target in j <<= baseDirectory,
    jotFile <<= (target in j)(_ / ".jot"),
    colors in j := true,
    j <<= inputTask { (argsTask: TaskKey[Seq[String]]) =>
      (argsTask, streams, jotFile, colors in j) map {
        (args, out, jf, clrs) =>
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
    drop <<= inputTask { (argsTask: TaskKey[Seq[String]]) =>
      (argsTask, streams, jotFile, colors in j) map {
        (args, out, jf, clrs) =>
          (args, out.log) match {
            case (Seq(num), log) =>
              IO.touch(jf)
              val lns = lines(jf)
              lns match {
                case Array() => log.info("Nothing to drop")
                case many => IO.write(jf, many.filter( _ match {
                  case (l, n) if(n == num.toInt) =>
                    log.info(
                      (if(clrs) "Dropped, \033[0;37m%s\033[0m" else "removed, %s") format l
                    )
                    false
                  case _ => true
                }).map(_._1) match {
                  case Array() => ""
                  case lns => lns.mkString("","\n\n","\n\n")
                })
              }
            case (_, log) => log.error("usage: jot-drop <num>")
          }
      }
    },
    cat <<= catTask,
    clean in j <<= cleanTask
  )
}
