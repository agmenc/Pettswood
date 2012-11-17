#!/bin/bash

./sbt test && git push origin master && ./sbt "+ publish"
