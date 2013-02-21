#!/bin/bash

echo "."
read -p "This will publish the current version to sonatype ==> CHECK THE VERSION NUMBER"
echo "."

./sbt clean test && ./sbt "+ publish" && git push origin master

echo "."
echo "Now release via Nexus/Sonatype"
echo " - Log in as me"
echo " - go to the STAGING repositories"
echo " - Close the repo"
echo " - Release the repo"
echo " - Wait a couple of hours"
echo "."
