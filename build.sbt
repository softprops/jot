sbtPlugin := true

organization := "me.lessis"

name := "jot"

version <<= sbtVersion(v => "0.1.0-%s-SNAPSHOT".format(v))