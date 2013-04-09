#!/bin/bash

#Run MADA

cd MADA-3.2/

echo "egytrain"
perl MADA+TOKAN.pl config=config-files/template.madaconfig file=/home/serena/Documents/Spring2013/MachineTranslation/githubsp2013.11-731/project/data/egy/egytrain.egy outputdir=output/split &> mada_log_egytrain.txt

cp output/split/egytrain.egy.bw.mada.tok ../data/egy/egytrain.egy.mada

echo "egydev"
perl MADA+TOKAN.pl config=config-files/template.madaconfig file=/home/serena/Documents/Spring2013/MachineTranslation/github/sp2013.11-731/project/data/egy/egydev.egy outputdir=output/split &> mada_log_egydev.txt

cp output/split/egydev.egy.bw.mada.tok ../data/egy/egydev.egy.mada

echo "egytest"
perl MADA+TOKAN.pl config=config-files/template.madaconfig file=/home/serena/Documents/Spring2013/MachineTranslation/github/sp2013.11-731/project/data/egy/egytest.egy outputdir=output/split &> mada_log_egytest.txt

cp output/split/egytest.egy.bw.mada.tok ../data/egy/egytest.egy.mada

echo "levtrain"
perl MADA+TOKAN.pl config=config-files/template.madaconfig file=/home/serena/Documents/Spring2013/MachineTranslation/github/sp2013.11-731/project/data/lev/levtrain.lev outputdir=output/split &> mada_log_levtrain.txt

cp output/split/levtrain.lev.bw.mada.tok ../data/lev/levtrain.lev.mada

echo "levdev"
perl MADA+TOKAN.pl config=config-files/template.madaconfig file=/home/serena/Documents/Spring2013/MachineTranslation/github/sp2013.11-731/project/data/lev/levdev.lev outputdir=output/split &> mada_log_levdev.txt

cp output/split/levdev.lev.bw.mada.tok ../data/lev/levdev.lev.mada

echo "levtest"
perl MADA+TOKAN.pl config=config-files/template.madaconfig file=/home/serena/Documents/Spring2013/MachineTranslation/github/sp2013.11-731/project/data/lev/levtest.lev outputdir=output/split &> mada_log_levtest.txt

cp output/split/levtest.lev.bw.mada.tok ../data/lev/levtest.lev.mada

echo "egylevtrain"
perl MADA+TOKAN.pl config=config-files/template.madaconfig file=/home/serena/Documents/Spring2013/MachineTranslation/github/sp2013.11-731/project/data/egylev/egylevtrain.egylev outputdir=output/split &> mada_log_egylevtrain.txt

cp output/split/egylevtrain.egylev.bw.mada.tok ../data/egylev/egylevtrain.egylev.mada

echo "egylevdev"
perl MADA+TOKAN.pl config=config-files/template.madaconfig file=/home/serena/Documents/Spring2013/MachineTranslation/github/sp2013.11-731/project/data/egylev/egylevdev.egylev outputdir=output/split &> mada_log_egylevdev.txt

cp output/split/egylevdev.egylev.bw.mada.tok ../data/egylev/egylevdev.egylev.mada

echo "egylevtest"
perl MADA+TOKAN.pl config=config-files/template.madaconfig file=/home/serena/Documents/Spring2013/MachineTranslation/github/sp2013.11-731/project/data/egylev/egylevtest.egylev outputdir=output/split &> mada_log_egylevtest.txt

cp output/split/egylevtest.egylev.bw.mada.tok ../data/egylev/egylevtest.egylev.mada

echo "done."
