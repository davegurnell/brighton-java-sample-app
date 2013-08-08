Intro to Scala at Brighton Java
===============================

This code accompanies the talk *An Introduction to Scala* by Dave Gurnell and Richard Dallaway,
given at [BrightonJava](http://www.brightonjava.com) on 7th August 2013. The talk abstract follows:

> Dave Gurnell and Richard Dallaway present a brief introduction to [Scala](http://scala-lang.org) –
> a powerful programming language for the JVM. In the talk, the speakers introduce Scala from a
> Java programmer’s perspective, and show how its support for object-oriented and functional
> programming styles can provide a smooth transition to greater productivity and code reliability.

This is a sample tweet listing app that we implemented in Java and Scala to compare syntax and idioms
in the two languages. We ultimately didn't use the app in the talk - see
https://github.com/davegurnell/brighton-java-slides for our slides.

Viewing the code
================

The source code is in the following directories:

 - Java version - `src/main/java/tweetzor`;
 - Scala version - `src/main/scala/tweetzor2`.

Each application is split into a main package (`tweetzor` and `tweetzor2` respectively)
and a `core` subpackage. The focus in the talk is on development of the code in the main
package - we only touch on the `core` package briefly.

## Docco

There are pre-built [Docco](http://jashkenas.github.io/docco/) documentation pages for
both versions of the app in the `docco` directory. This is the recommended way of browsing
the code:

 - start with the `Main` and `Menu` classes;
 - then go to `Tweet` and `Location`;
 - then to `TwitterClient`;
 - finally, the adventurous can go to `PostcodeDatabase` and the `com.untyped.location` package.

There's a menu for switching files in the top right of the page.

## Eclipse

The project is built using [SBT](http://scala-sbt.org). To view the code in the Eclipse IDE:

 0. Install the Scala plugin for Eclipse. The simplest way to do this is to download
    the pre-packaged *Scala IDE* bundle from [http://scala-ide.org], which includes both
    Eclipse itself and the Scala plugin in a single archive.

 1. Clone the git repo and change to its root directory:

        git clone https://github.com/davegurnell/brighton-java-scala-talk.git
        cd brighton-java-scala-talk

 2. Run the `sbt eclipse` script to generate Eclipse configuration files.
    The first time you run SBT it may take some time to download and cache JAR files
    on your hard drive (look in `~/.ivy2` if you want to delete them later):

        ./sbt.sh eclipse

 3. Import the code as an Eclipse project by selecting **File menu / Import... / General /
    Existing Projects into Workspace** and selecting the root directory of the repo.

## Intellij IDEA 12

 0. Install the [Scala plugin](http://confluence.jetbrains.com/display/SCA/Getting+Started+with+IntelliJ+IDEA+Scala+Plugin) for Intellij.

 1. Clone the git repo and change to its root directory:

        git clone https://github.com/davegurnell/brighton-java-scala-talk.git
        cd brighton-java-scala-talk

 2. Run the `sbt gen-idea` script to generate Intellij configuration files.
    The first time you run SBT it may take some time to download and cache JAR files
    on your hard drive (look in `~/.ivy2` if you want to delete them later):

        ./sbt.sh gen-idea

 3. Import the code as an Intellij project by selecting **File Menu / Open...** and
    selecting the root directory of the repo.

Building the Code
=================

Build the code in the usual way using your trusty IDE, **or** run the following command
from the root directory of the repo:

    ./sbt compile

Running the Code
================

## Prerequisites

Before you run the application you will need to do two things:

 1. Create a Twitter4j configuration file using settings from [http://dev.twitter.com]().

 2. Install a postcodes database file (optional).

The application needs a Twitter4j configuration file to run.
This file has been left out of the Git repo to avoid security issues.
You can generate your own configuration file easily - the procedure is as follows:

 1. Go to [http://dev.stitter.com]() and sign in.

 2. Choose **My applications** from the drop down menu in the top right.

 3. Click **Create a new application** and fill in the form. Name the application anything you want.
    You can skip the *Callback URL* field.

 4. Under **Your access token**, generate an access token for your own access to the app.

 5. Use the values of the consumer key and access tokens to fill the following template and
    save it in `src/main/resources/twitter4j.properties`:

        debug=false
        oauth.consumerKey=&ldquo;your consumer key&rdquo;
        oauth.consumerSecret=&ldquo;your consumer secret&rdquo;
        oauth.accessToken=&ldquo;your access token&rdquo;
        oauth.accessTokenSecret=&ldquo;your access token secret&rdquo;
        loggerFactory=twitter4j.internal.logging.NullLoggerFactory

The postcodes file is only used in the final version of the application.
Installing it is optional. If it is missing, the application will simply report
everyone Tweet's location as *Unknown*.

The file has been left out of the Git repo to avoid any potential licencing issues.
You can get your own copy easily - the installation procedure is as follows:

 - TODO: Complete these instructions...

 - Essentially, download *Codepoint*, an open source CSV postcode database available
   from the Ordnance Survey. Concatenate all of the CSV files and name the resulting
   file `src/main/resources/postcodes.csv`.

## All Done - Let's Go

Run the code in the usual way using your trusty IDE, **or** run the following command
from the root directory of the repo:

    ./sbt run

You will be prompted to choose the Java version of the app (`tweetzor.Main`)
or the Scala version (`tweetzor2.Main`).
