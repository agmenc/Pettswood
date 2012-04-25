#!/bin/bash

docs=../pettswood-docs
rm -rf $docs/*
cp -Rf index.html $docs
mkdir -p $docs/src/test/resources
cp -Rf src/test/resources $docs/src/test

cd $docs
git add .
git commit -m "docs update"
git push origin gh-pages
