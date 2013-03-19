#!bin/bash/
# Convert Levantine to be more like Egyptian
# sjeblee@cs.cmu.edu
# Last Modified: 14 March 2013

#TODO
#to be able to: fii
# shuu 'aam verb -> verb eyh
# rH verb -> H verb
# ma'ai -> ma'ayya

#word reordering:
# imta verb -> verb imta
# maa verb -> ma-verb-sh : including fii and 'aind
# Haada, Haadi -> da, di, etc : see pg 17 M.Omar
# Ha + noun -> noun da/di

file="levantine.txt"

sed 's/ وين / فين /g' $file | \ #weyn to feyn
sed 's/ ليش / ليه /g' $file | \ #leysh to leyh
sed 's/ shlon/ izzay/g' $file | \ #shlon to izzay
sed 's/ kayf / izzay /g' $file | \ #kayf to izzay
sed 's/ kayfk / izzayk /g' $file | \ #kayfak to izzayak
sed 's/ qdi:sh / bika:m /g' $file | \ #qadiish to bikam
sed 's/ hllq / delwati: /g' $file | \ #hallaq to delwa'ti
sed 's/ mni:H / kwayis /g' $file | \ #mniiH to kwayis
sed 's/ mba:rH / imba:rH /g' $file > lev-converted.txt #mbarH to imbaraH
