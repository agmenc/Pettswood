#!/bin/bash

mkdir -p lib/src
cp -vp `find lib_managed -name "*.jar"` lib
mv lib/*sources.jar lib/src/


