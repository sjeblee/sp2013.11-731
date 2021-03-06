#!/bin/bash
#sjeblee@cs.cmu.edu
#Normalize alif and ya

#set file to convert
file="data/lev/levtest.lev"

sed 's/أ/ا/g' $file |
sed 's/إ/ا/g' |
sed 's/آ/ا/g' |
sed 's/ي /ى /g' |
sed 's/ي$/ى/g' |
sed 's/َ//g' | 
sed 's/ُ//g' | 
sed 's/ِ//g' | 
sed 's/ً//g' | 
sed 's/ٌ//g' | 
sed 's/ٍ//g' |
sed 's/ْ//g' | 
sed 's/ّ//g' > data/lev/temp.txt #change output dir
