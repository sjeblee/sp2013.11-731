#!/bin/bash

echo "compiling..."
javac IbmModel1.java

echo "aligning..."
java IbmModel1 data/dev-test-train.de-en data/dev.align

#echo "alignment done, processing files"
#cat data/dev.align output.txt > temp
#sed 's/?/-/g' < temp > output.txt

echo "checking..."
./check < output.txt > output.al

echo "grading..."
./grade -n 5 < output.al

echo "done."

