seq(jotSettings:_*)

(JotKeys.jotFile in Compile) <<= (baseDirectory)(_ / ".myjots")
