#!/bin/bash

echo "compiling..."
javac Align.java

echo "aligning..."
java -Xms2500m -Xmx2500m Align data/dev-test-train.de-en data/dev.align

echo "checking..."
./check < output.txt > output.al

echo "grading..."
./grade -n 5 < output.al

echo "done."

