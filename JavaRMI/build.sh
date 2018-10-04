#!/bin/bash

shopt -s globstar

if ! [[ -d bin ]]; then mkdir bin; fi

javac -classpath src/ -d bin/ src/**/*.java
