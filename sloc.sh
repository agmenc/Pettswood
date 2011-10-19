#!/bin/sh

cat $(find src/main/scala -name "*.scala") | wc -l
cat $(find src/test/scala -name "*.scala") | wc -l