import sbt._
import Project.Initialize
import java.io.File

object Plugin extends sbt.Plugin {
  import sbt.Keys._

  object jot {

    object Keys {
      val down = InputKey[Unit]("down", "Appends a thought to your jot file")
      val rm = InputKey[Unit]("rm", "Removes a thought from your jot file")
      val ls = TaskKey[Unit]("ls", "Lists your jotted thoughts")
      val clear = TaskKey[Unit]("clear", "Clears all jotted thoughts")
      val jotFile = SettingKey[File]("jot-file", "File containing thoughts")
      val jotDirectory = SettingKey[File]("jot-directory", "Directory containing jot file")
      val colors = SettingKey[Boolean]("colors", "Toggles ansii colored output")
    }
    import Keys._

    val Config = config("jot")

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
      (streams, jotFile, colors) map {
        (out, jf, clrs) =>
          out.log.info("jottings..")
          IO.touch(jf)
          lines(jf) match {
            case Array() => out.log.info("you have none. try jot:down some ideas")
            case ts => ts.foreach { _ match {
              case(l, n) => out.log.info(
                (if(clrs) "\033[0;36m%s\033[0m) \033[0;37m%s\033[0m" else "%s) %s") format(n, l)
              )
            } }
          }
      }

    // will NOT get auto-mixed
    def settings: Seq[Setting[_]] = inConfig(Config)(Seq(
      jotDirectory <<= (baseDirectory).identity,
      jotFile <<= (jotDirectory)(_ / ".jot"),
      colors := true,
      down <<= inputTask { (argsTask: TaskKey[Seq[String]]) =>
        (argsTask, streams, jotFile, colors) map { (args, out, jf, clrs) =>
          args.mkString(" ").trim match {
            case "" => out.log.error("usage: jot:down some ideas")
            case thought =>
              IO.touch(jf)
              IO.append(jf, thought + "\n\n")
              out.log.info(
                (if(clrs) "jotted, \033[0;37m%s\033[0m" else "jotted, %s") format thought
              )
          }
        }
      },
      rm <<= inputTask { (argsTask: TaskKey[Seq[String]]) =>
        (argsTask, streams, jotFile, colors) map { (args, out, jf, clrs) =>
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
                }).map(_._1)
                  .mkString("","\n\n","\n\n")
                )
              }
            case _ => out.log.error("usage: jot:rm <num>")
          }
        }
      },
      ls <<= lsTask,
      clear <<= clearTask
    ))
  }
}
