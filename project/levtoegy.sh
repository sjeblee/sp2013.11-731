#!bin/bash/
# Convert Levantine to be more like Egyptian
# sjeblee@cs.cmu.edu
# Last Modified: 19 March 2013

#TODO
#to be able to: fii
# shuu 'aam verb -> verb eyh
# rH verb -> H verb
# maa verb -> ma-verb-sh

#word reordering:
# imta verb -> verb imta
# maa verb -> ma-verb-sh : including fii and 'aind
# Haada, Haadi -> da, di, etc : see pg 17 M.Omar
# Ha + noun -> noun da/di

file="levantine.txt"

sed 's/ معي / معيا /g' $file | \ #ma'i to ma'ayya
sed 's/ وين / فين /g' $file | \ #weyn to feyn
sed 's/ ليش / ليه /g' $file | \ #leysh to leyh
sed 's/شلون/ إزي/g' $file | \ #shlon to izzay
sed 's/ كيف / إزي /g' $file | \ #kayf to izzay
sed 's/ كيفك / إزيك /g' $file | \ #kayfak to izzayak
sed 's/ قديش / بكام /g' $file | \ #qadiish to bikam
sed 's/ هلق / دلوقتي /g' $file | \ #hallaq to delwa'ti
sed 's/ منيح / كويس /g' $file | \ #mniiH to kwayis
#sed 's/رح [a-z] / ح /g' $file | \ #rH verb to H-verb
sed 's/ مبارح / امبارح /g' $file > lev-converted.txt #mbarH to imbaraH
