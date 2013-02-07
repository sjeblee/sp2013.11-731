/** MT HW 1 Alignment
* Implementing IBM Model 1
* @author sjeblee@.cs.cmu.edu
* 6 Feb 2013
**/

import java.io.*;
import java.util.*;

public class IbmModel1{

public static void main(String[] args){

	long starttime = System.currentTimeMillis();

	if(args.length < 2){
		System.out.println("Usage: java Align sentences dev-alignments");
		System.exit(1);
	}

	//data structures
	ArrayList<String> sentences = new ArrayList<String>();
	HashMap<String,Double> efprob = new HashMap<String,Double>();
	HashMap<String,Double> feprob = new HashMap<String,Double>();
	HashMap<String,Double> decount = new HashMap<String,Double>();
	HashMap<String,Double> efcount = new HashMap<String,Double>();
	HashMap<String,Double> fecount = new HashMap<String,Double>();
	HashMap<String,Double> encount = new HashMap<String,Double>();
	/*double[] jumpwidth = new double[100];
	for(int jw=0; jw<100; jw++)
		jumpwidth[jw] = 0.001;
	*/

	try{	//read in sentences
		System.out.print("Reading in sentences");
		Scanner infile = new Scanner(new FileReader(args[0]));
		/*Scanner alignfile = new Scanner(new FileReader(args[1]));
		while(alignfile.hasNextLine()){
		} */
		int counter = 0;
		while(infile.hasNextLine() && counter < 10000){
			if((counter%10000) == 0)
				System.out.print("\n" + counter/1000);
			else if((counter%1000) == 0)
				System.out.print(".");
			String line = infile.nextLine();
			sentences.add(line); 		//save sentences
			int barindex = line.indexOf("|||");
			String de = line.substring(0, barindex);
			String en = line.substring(barindex+3);
			StringTokenizer detok = new StringTokenizer(de);
			StringTokenizer entok = new StringTokenizer(en);
			ArrayList<String> delist = new ArrayList<String>();
			ArrayList<String> enlist = new ArrayList<String>();
			while(detok.hasMoreTokens())
				delist.add(detok.nextToken());
			while(entok.hasMoreTokens())
				enlist.add(entok.nextToken());
			for(String deword : delist){
				decount.put(deword, 0.0);
				for(String enword : enlist){
					encount.put(enword, 0.0);
					efcount.put(enword + " " + deword, 0.0);
					efprob.put(enword + " " + deword, 0.0);
					fecount.put(enword + " " + deword, 0.0);
					feprob.put(enword + " " + deword, 0.0);
				}
			}
			counter++;
		}//end while infile has next
		infile.close();
	}//end try

	catch(IOException e){
		System.out.println(e.getMessage());
	}

	/* EM */
	System.out.println("\nRunning EM...");

 	// initialize t(e|f) uniformly
	Set<String> tef = efprob.keySet();
	double efsize = (double) efprob.size();
	for(String ef : tef)
		efprob.put(ef, 1.0 / efsize);

	//F|E
	Set<String> tfe = feprob.keySet();
	double fesize = (double) feprob.size();
	for(String fe : tfe)
		feprob.put(fe, 1.0 / fesize);
	
	//do until convergence
	for(int x=0; x<5; x++){
		System.out.println("\titeration " + x);

		// E | F
		HashMap<String,Double> total_se = new HashMap<String,Double>();
  		// set count(e|f) to 0 for all e,f
		Set<String> allef = efcount.keySet();	//assume all other pairs are never observed
		for(String ende : allef){
			efcount.put(ende, 0.0);
		}			
  		// set total(f) to 0 for all f
		Set<String> countsf = decount.keySet();
		for(String c : countsf)
			decount.put(c, 0.0);

		// F | E
		HashMap<String,Double> total_sf = new HashMap<String,Double>();
  		// set count(f|e) to 0 for all e,f
		Set<String> allfe = fecount.keySet();	//assume all other pairs are never observed
		for(String deen : allfe){
			fecount.put(deen, 0.0);
		}			
  		// set total(e) to 0 for all e
		Set<String> countse = encount.keySet();
		for(String c : countse)
			encount.put(c, 0.0);

		//BOTH
   		//for all sentence pairs (e_s,f_s)
 		for(String line : sentences){

			int barindex = line.indexOf("|||");
			String de = line.substring(0, barindex);
			String en = line.substring(barindex+3);
			StringTokenizer detok = new StringTokenizer(de);
			StringTokenizer entok = new StringTokenizer(en);
			ArrayList<String> delist = new ArrayList<String>();
			ArrayList<String> enlist = new ArrayList<String>();
			while(detok.hasMoreTokens())
				delist.add(detok.nextToken());
			while(entok.hasMoreTokens())
				enlist.add(entok.nextToken());

			//E|F
    			// set total_s(e) = 0 for all e
			for(String e : enlist)
				total_se.put(e, 0.0);
   			// for all words e in e_s
			for(String e : enlist){
    				//for all words f in f_s
				for(String f : delist){
    					//total_s(e) += t(e|f)
					total_se.put(e, total_se.get(e) + efprob.get(e + " " + f));
				}
			}
    			// for all words e in e_s
			for(String e : enlist){
    				//for all words f in f_s
				for(String f : delist){
					String ef = e + " " + f;
    		 			//count(e|f) += t(e|f) / total_s(e)
					efcount.put(ef, efcount.get(ef) + (efprob.get(ef)/total_se.get(e)));
     		 			//total(f)   += t(e|f) / total_s(e)
					decount.put(f, decount.get(f) + (efprob.get(ef)/total_se.get(e)));
				}
			}

			// F |E
    			// set total_s(f) = 0 for all f
			for(String f : delist)
				total_sf.put(f, 0.0);
   			// for all words f in f_s
			for(String f : delist){
    				//for all words e in e_s
				for(String e : enlist){
    					//total_s(f) += t(f|e)
					total_sf.put(f, total_sf.get(f) + feprob.get(f + " " + e));
				}
			}
    			// for all words f in f_s
			for(String f : delist){
    				//for all words e in e_s
				for(String e : enlist){
					String fe = f + " " + e;
    		 			//count(f|e) += t(f|e) / total_s(f)
					efcount.put(fe, fecount.get(fe) + (feprob.get(fe)/total_sf.get(f)));
     		 			//total(e)   += t(f|e) / total_s(f)
					encount.put(e, encount.get(e) + (feprob.get(fe)/total_sf.get(f)));
				}
			}
		}//end for all esfs
	
		//E|F
		for(String ende : allef){
				//t(e|f) = count(e|f) / total(f)
				String deword = ende.substring(ende.indexOf(' ')+1);
				efprob.put(ende, efcount.get(ende) / decount.get(deword));
		}

	
		//F|E
		for(String deen : allfe){
				//t(f|e) = count(f|e) / total(e)
				String enword = deen.substring(deen.indexOf(' ')+1);
				feprob.put(deen, fecount.get(deen) / encount.get(enword));
		}	
    	
    
	}//end for x iterations

	try{

	System.out.println("Printing Alignments...");
	FileWriter outfile = new FileWriter("output.txt");
	for(String line : sentences){
		String alignment = "";
		int barindex = line.indexOf("|||");
		String de = line.substring(0, barindex);
		String en = line.substring(barindex+3);
		StringTokenizer detok = new StringTokenizer(de);
		StringTokenizer entok = new StringTokenizer(en);
		ArrayList<String> delist = new ArrayList<String>();
		ArrayList<String> enlist = new ArrayList<String>();
		while(detok.hasMoreTokens())
			delist.add(detok.nextToken());
		while(entok.hasMoreTokens())
			enlist.add(entok.nextToken());
		for(int eindex=0; eindex<enlist.size(); eindex++){
			int bestindex = 0;
			double bestprob = 0.0;
			String e = enlist.get(eindex);
			for(int findex=0; findex<delist.size(); findex++){
				String ef = e + " " + delist.get(findex);
				String fe = delist.get(findex) + " " + e;
				double prob = efprob.get(ef)*feprob.get(fe);
				if(prob > bestprob){
					bestprob = prob;
					bestindex = findex;
				}
			}//end for de
			alignment += bestindex + "-" + eindex + " ";
		}//end for each english word

		//write alignments to file
		outfile.write(alignment + "\n");
	}//end for each sentence

	outfile.close();
	
	}//end try
	catch(IOException e){
		System.out.println(e.getMessage());
	}
	
	long endtime = System.currentTimeMillis();
	long time = endtime - starttime;
	double printtime = ((double)time) / 1000;
	printtime = printtime / 60.0;
	System.out.printf("\talignment time: %f.2 min \n", printtime);
	
	System.out.println("Done.");

}//end main

}
