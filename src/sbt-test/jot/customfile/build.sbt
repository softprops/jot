seq(jotSettings:_*)

(JotKeys.jotFile in (Compile, JotKeys.jot)) <<= (baseDirectory)(_ / ".myjots")
