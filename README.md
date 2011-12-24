# jot

Simple local storage for all of those half baked ideas.

`jot`, `drop` and roll.

## Install

Add the following to your plugin definition

    addSbtPlugin("me.lessis" % "jot" % "0.1.0")

And add following in your build definition

    seq(jotSettings:_*)

## usage

Below is a list of available settings

    jot                                                     # Appends an item to your jot file
    clean(for jot)        or (clean in (SomeConfig, jot))   # Removes an item from your jot file
    jot-drop              or (drop in SomeConfig))          # Drops a target item from your jot file
    jot-cat               or (cat in (SomeConfig))          # Lists your jottings
    jot-file              or (jotFile in (SomeConfig))      # File containing jottings
    target(for jot)       or (target in (SomeConfig, jot))  # Directory containing generated jot file
    colors(for jot)       or (colors in (SomeConfig, jot))  # Toggles ansii colored output

Doug Tangren (softprops) 2011
