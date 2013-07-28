#!/usr/bin/env bash

# Because the number of lines is what truly matters, right?

echo Java  `find src/main/java/tweetzor  -name \*.java  -exec cat {} \; | grep '[^ ]' | grep -v '^[ ]*[/][/]' | wc -l`
echo Scala `find src/main/scala/tweetzor2 -name \*.scala -exec cat {} \; | grep '[^ ]' | grep -v '^[ ]*[/][/]' | wc -l`
