# jot

Simple local storage for all of those half baked ideas.

`jot`, `drop` and [roll](http://www.screenr.com/SKQs).

## Install

Add the following to your plugin definition

    addSbtPlugin("me.lessis" % "jot" % "0.1.0")

And if you haven't added it already, add the community sbt resolver

    resolvers += Resolver.url("sbt-plugin-releases",
      url("http://scalasbt.artifactoryonline.com/scalasbt/sbt-plugin-releases/"))(
        Resolver.ivyStylePatterns)

And add following in your build definition

    seq(jotSettings:_*)


### Install globally

This is usually preferable, as it is less intrusive

Append `~/.sbt/plugins/build.sbt` with

    addSbtPlugin("me.lessis" % "jot" % "0.1.0")

In any `.sbt` file under `~/.sbt`, say `~/.sbt/jot.sbt` for instance, append

    seq(jotSettings:_*)

## usage

Jot stores data in a `.jot` file in your project's root

What to keep your jots to yourself? That's cool. Just add `.jot` to your `.gitignore` file.

Below is a list of available settings

    jot                                                     # Appends an item to your jot file
    clean(for jot)        or (clean in (SomeConfig, jot))   # Removes an item from your jot file
    jot-drop              or (drop in SomeConfig))          # Drops a target item from your jot file
    jot-cat               or (cat in (SomeConfig))          # Lists your jottings
    jot-file              or (jotFile in (SomeConfig))      # File containing jottings
    target(for jot)       or (target in (SomeConfig, jot))  # Directory containing generated jot file
    colors(for jot)       or (colors in (SomeConfig, jot))  # Toggles ansii colored output

Doug Tangren (softprops) 2011-12
