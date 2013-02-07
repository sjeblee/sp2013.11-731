/** MT HW 1 Alignment
 * Implementing IBM Model 1
 * @author sjeblee@.cs.cmu.edu
 * 7 Feb 2013
 **/

import java.io.*;
import java.util.*;

public class IbmModel1 {

	public static void main(String[] args) {

		long starttime = System.currentTimeMillis();

		if (args.length < 2) {
			System.out.println("Usage: java Align sentences dev-alignments");
			System.exit(1);
		}

		// data structures
		ArrayList<String> sentences = new ArrayList<String>();
		HashMap<String, Double> efprob = new HashMap<String, Double>();
		HashMap<String, Double> decount = new HashMap<String, Double>();
		HashMap<String, Double> efcount = new HashMap<String, Double>();
		HashMap<String, Integer> ecount = new HashMap<String, Integer>();
		/*
		 * double[] jumpwidth = new double[100]; for(int jw=0; jw<100; jw++)
		 * jumpwidth[jw] = 0.001;
		 */

		try { // read in sentences
			System.out.print("Reading in sentences");
			Scanner infile = new Scanner(new FileReader(args[0]));
			/*
			 * Scanner alignfile = new Scanner(new FileReader(args[1]));
			 * while(alignfile.hasNextLine()){ }
			 */
			int counter = 0;
			while (infile.hasNextLine() && counter < 12000) {
				if ((counter % 10000) == 0)
					System.out.print("\n" + counter / 1000);
				else if ((counter % 1000) == 0)
					System.out.print(".");
				String line = infile.nextLine();
				sentences.add(line); // save sentences
				int barindex = line.indexOf("|||");
				String de = line.substring(0, barindex);
				String en = line.substring(barindex + 3);
				StringTokenizer detok = new StringTokenizer(de);
				StringTokenizer entok = new StringTokenizer(en);
				ArrayList<String> delist = new ArrayList<String>();
				ArrayList<String> enlist = new ArrayList<String>();
				while (detok.hasMoreTokens())
					delist.add(detok.nextToken());
				while (entok.hasMoreTokens())
					enlist.add(entok.nextToken());
				for (String deword : delist) {
					decount.put(deword, 0.0);
					for (String enword : enlist) {
						ecount.put(enword, 0);
						efcount.put(enword + " " + deword, 0.0);
						efprob.put(enword + " " + deword, 0.0);
					}
				}
				counter++;
			}// end while infile has next
			infile.close();
		}// end try

		catch (IOException e) {
			System.out.println(e.getMessage());
		}

		System.out.println("\nRunning EM...");

		// initialize t(e|f) uniformly
		Set<String> tef = efprob.keySet();
		double efsize = (double) efprob.size();
		for (String ef : tef)
			efprob.put(ef, 1.0 / efsize);

		// do until convergence
		for (int x = 0; x < 6; x++) {
			System.out.println("\titeration " + x);

			HashMap<String, Double> total_s = new HashMap<String, Double>();

			// set count(e|f) to 0 for all e,f
			Set<String> allef = efcount.keySet(); // assume all other pairs are
													// never observed
			for (String ende : allef) {
				efcount.put(ende, 0.0);
			}

			// set total(f) to 0 for all f
			Set<String> countsf = decount.keySet();
			for (String c : countsf)
				decount.put(c, 0.0);
			// for all sentence pairs (e_s,f_s)
			for (String line : sentences) {

				int barindex = line.indexOf("|||");
				String de = line.substring(0, barindex);
				String en = line.substring(barindex + 3);
				StringTokenizer detok = new StringTokenizer(de);
				StringTokenizer entok = new StringTokenizer(en);
				ArrayList<String> delist = new ArrayList<String>();
				ArrayList<String> enlist = new ArrayList<String>();
				while (detok.hasMoreTokens())
					delist.add(detok.nextToken());
				while (entok.hasMoreTokens())
					enlist.add(entok.nextToken());

				// set total_s(e) = 0 for all e
				for (String e : enlist)
					total_s.put(e, 0.0);

				// for all words e in e_s
				for (String e : enlist) {
					// for all words f in f_s
					for (String f : delist) {
						// total_s(e) += t(e|f)
						total_s.put(e, total_s.get(e) + efprob.get(e + " " + f));
					}
				}

				// for all words e in e_s
				for (String e : enlist) {
					// for all words f in f_s
					for (String f : delist) {
						String ef = e + " " + f;
						// count(e|f) += t(e|f) / total_s(e)
						efcount.put(ef, efcount.get(ef)
								+ (efprob.get(ef) / total_s.get(e)));
						// total(f) += t(e|f) / total_s(e)
						decount.put(f, decount.get(f)
								+ (efprob.get(ef) / total_s.get(e)));
					}
				}
			}// end for all esfs

			// for all f
			/*
			 * Set<String> allf = decount.keySet(); for(String deword : allf){
			 * // for all e Set<String> alle = ecount.keySet(); for(String
			 * enword : alle){
			 */
			// Set<String> allef = efprob.keySet(); //assume all other pairs are
			// never observed
			for (String ende : allef) {
				// t(e|f) = count(e|f) / total(f)
				String deword = ende.substring(ende.indexOf(' ') + 1);
				efprob.put(ende, efcount.get(ende) / decount.get(deword));
			}

		}// end for x iterations

		try {

			System.out.println("Printing Alignments...");
			FileWriter outfile = new FileWriter("output.txt");
			for (String line : sentences) {
				String alignment = "";
				int barindex = line.indexOf("|||");
				String de = line.substring(0, barindex);
				String en = line.substring(barindex + 3);
				StringTokenizer detok = new StringTokenizer(de);
				StringTokenizer entok = new StringTokenizer(en);
				ArrayList<String> delist = new ArrayList<String>();
				ArrayList<String> enlist = new ArrayList<String>();
				while (detok.hasMoreTokens())
					delist.add(detok.nextToken());
				while (entok.hasMoreTokens())
					enlist.add(entok.nextToken());
				for (int eindex = 0; eindex < enlist.size(); eindex++) {
					int bestindex = 0;
					double bestprob = 0.0;
					String e = enlist.get(eindex);
					for (int findex = 0; findex < delist.size(); findex++) {
						String ef = e + " " + delist.get(findex);
						if (efprob.get(ef) > bestprob) {
							bestprob = efprob.get(ef);
							bestindex = findex;
						}
					}// end for de
					alignment += bestindex + "-" + eindex + " ";
				}// end for each english word

				// write alignments to file
				outfile.write(alignment + "\n");
			}// end for each sentence

			outfile.close();

		}// end try
		catch (IOException e) {
			System.out.println(e.getMessage());
		}

		long endtime = System.currentTimeMillis();
		long time = endtime - starttime;
		double printtime = ((double) time) / 1000;
		printtime = printtime / 60.0;
		System.out.printf("\talignment time: %.2f min \n", printtime);

		System.out.println("Done.");

	}// end main

}
