#!/bin/bash
#Script to run foma analyzer on Egyptian Arabic

file="egyptian-example.txt"
outfile="egyptian-example.analyzed"

foma -l egy.foma

#Preprocess data
./oneperline.py $file $file.one

#Run foma
cat $file.one | flookup -x egy.bin > temp

#Run postprocessor
javac PostProcess.java
java PostProcess $file.one temp $outfile

#Cleanup
rm temp
