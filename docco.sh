#!/usr/bin/env bash

# Requires `docco` to be installed:
#     npm install -g docco

mkdir -p docs/java
mkdir -p docs/scala

docco -c src/main/resources/docco.css -o docs/java `find src/main/java -name \*.java`
docco -c src/main/resources/docco.css -o docs/scala `find src/main/scala -name \*.scala`
