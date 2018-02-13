#!/bin/bash
#
# Use this shell script to compile (if necessary) your code and then execute it. Below is an example of what might be found in this file if your program was written in Python
#
#python ./src/donation-analytics.py ./input/itcont.txt ./input/percentile.txt ./output/repeat_donors.txt

javac ./src/com/insights/data/Main.java ./src/com/insights/data/models/*.java ./src/com/insights/data/processors/*.java ./src/com/insights/data/utils/*.java
java -classpath ./src com/insights/data/Main ./input/itcont.txt ./input/percentile.txt ./output/repeat_donors.txt
