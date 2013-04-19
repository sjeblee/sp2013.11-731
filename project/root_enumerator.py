#!/usr/bin/python
# -*- coding: utf-8 -*-
#root_enumerator.py
#Serena Jeblee & Weston Feely
import sys

def main(args):
	#Check required args
	if len(args) < 2:
		print "Usage python root_enumerator.py part-of-speech"
		return 1
	pos = args[1].lower().capitalize()
	alphabet = set(list(u"ذضصثقفغعهخحجدشسيبلتنمكطءراوزظ"))
	alif = u"ا"
	alif_maq = u"ى"
	ta = u"ت"
	sin = u"س"
	mim = u"م"
	nun = u"ن"
	waw = u"و"
	ya = u"ي"
	tm = u"ة"
	longv = set(list(u"او"))
	out = []
	for x in alphabet:
		for y in alphabet:
			for z in alphabet:
				#Rule 1: Count number of vowels in possible root
				vflag = 0
				for w in set([x,y,z]):
					if w in longv:
						vflag += 1
				#Rule 2: change final alif to alif-maqsura
				if z == alif:
					z = alif_maq
				#Rule 3: Check for two vowel case, with waw+alif-maqsura, override rule 1
				if (x not in longv) and (y == waw) and (z == alif_maq):
					vflag = 0
				if (not (x==y)) and (not (x==y==z)) and (vflag < 2):
					#Add 3-letter root to out list
					out.append((x+y+z).encode('utf-8'))
					#Generate verb forms
					if pos == "Verb":
						out.append((x+alif+y+z).encode('utf-8')) # V3
						out.append((alif+x+y+z).encode('utf-8')) # V4
						out.append((ta+x+y+z).encode('utf-8')) # V5
						out.append((ta+x+alif+y+z).encode('utf-8')) # V6
						out.append((alif+nun+x+y+z).encode('utf-8')) # V7
						out.append((alif+x+ta+y+z).encode('utf-8')) # V8
						out.append((alif+sin+ta+x+y+z).encode('utf-8')) # V10
					elif pos == "Noun":
						out.append((x+alif+y+z).encode('utf-8')) # nominalization 
						out.append((mim+x+y+waw+z).encode('utf-8')) # nominalization
						out.append((x+y+alif+z).encode('utf-8'))
						out.append((x+y++alif+z+ya).encode('utf-8'))
	#Print roots to stdout
	for item in out:
		print item+' '+pos+'Inf;'
	return 0

if __name__ == '__main__':
	sys.exit(main(sys.argv))
