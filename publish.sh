#!/bin/bash

echo "."
read -p "This will commit and publish the current version to sonatype ==> UPDATE THE VERSION NUMBER"
echo "."

./sbt clean test && git commit -m "Publishing" && ./sbt "+ publish" && git push origin master

echo "."
echo "Now release via Nexus/Sonatype"
echo " - Log in as me, c'est parfait"
echo " - go to the STAGING repositories"
echo " - Close the repo"
echo " - Release the repo"
echo " - Wait a couple of hours"
echo "."
