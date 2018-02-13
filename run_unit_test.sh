#!/usr/bin/env bash

# one example of run.sh script for implementing the features using python
# the contents of this script could be replaced with similar files from any major language

# I'll execute my programs, with the input directory log_input and output the files in the directory log_output
javac -classpath "./lib/*" ./test/com/insights/data/TestRunner.java ./test/com/insights/data/models/*.java ./test/com/insights/data/processors/*.java ./test/com/insights/data/utils/*.java ./src/com/insights/data/models/*.java ./src/com/insights/data/processors/*.java ./src/com/insights/data/utils/*.java
java -classpath "./src:./test:./lib/*" com/insights/data/TestRunner