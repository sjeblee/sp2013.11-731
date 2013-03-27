/** MishMishDec.java
* MT HW 3
* Weston Feely & Serena Jeblee
* Last Modified: 21 Mar 2013
*/

import java.io.*;
import java.util.*;

public class MishMishDec{

public static void main(String[] args){

	if(args.length < 3){
		System.out.println("Usage: java MishMishDec data languagemodel translationmodel");
		System.exit(1);
	}

	String datafile = args[0];
	String lmfile = args[1];
	String tmfile = args[2];
	
	try{

	//HashMap<String, Pair<Double, Double>> lm = readLM(lmfile);
	HashMap<String, ArrayList<Pair<String, Double>>> tm = readTM(tmfile);

	//Read in heuristic hypotheses
	Scanner hypin = new Scanner(new FileReader("hypotheses.txt"));

	Scanner infile = new Scanner(new FileReader(datafile));
	while(infile.hasNextLine()){
		String line = infile.nextLine();
		String hyp = hypin.nextLine();
		String[] sarray = line.trim().split(" ");
		ArrayList<String> unigrams = new ArrayList<String>();
		for(int x=0; x<sarray.length; x++)
			unigrams.add(sarray[x]);
		HashMap<String, ArrayList<Pair<String, Double>>> searchspace = getsubset(tm, unigrams);
		String sentence = search(unigrams, hyp, searchspace);
	}

	infile.close();
	}//try
	catch(IOException e){
		System.out.println(e.getMessage());
	}

}//end main

//Returns ngrams of given order for a sentence, as a list of words
public static ArrayList<String> get_ngrams(ArrayList<String> sent, int n){
	if ((sent.size() < n) || (n < 1))
		 return new ArrayList<String>();
	ArrayList<String> ngrams = new ArrayList<String>();
	for(int i=0; i<sent.size(); i++){
		if(i > n-1){
			String buff = "";
			for(int j=n-1; j>-1; j--)
				buff += sent.get(i-j) + " ";
			ngrams.add(buff.trim());
		}
	}//end for
	return ngrams;
}//end get_ngrams

public static HashMap<String, Pair<Double, Double>> readLM(String filename) throws IOException{
	//Read ARPA-format language model from file and save as dictionary
	HashMap<String, Pair<Double, Double>> lm = new HashMap<String, Pair<Double,Double>>();

	Scanner infile = new Scanner(new FileReader(filename));
	while(infile.hasNextLine()){
		String line = infile.nextLine();
		//System.out.println(line);
		StringTokenizer tok = new StringTokenizer(line, "\t");
		double logprob = Double.parseDouble(tok.nextToken());
		String ngram = tok.nextToken();
		double backoffprob = 0.0;
		if(tok.hasMoreTokens())
			backoffprob = Double.parseDouble(tok.nextToken());
		lm.put(ngram, new Pair<Double,Double>(logprob,backoffprob));
	}//end while
	
	return lm;
}//end readLM

public static HashMap<String, ArrayList<Pair<String,Double>>> readTM(String filename) throws IOException{
	//Read translation model from file and organize into dictionaries
	HashMap<String, ArrayList<Pair<String,Double>>> spanish_ngrams = new HashMap<String, ArrayList<Pair<String,Double>>>(); // spanish -> english,logprob
	Scanner infile = new Scanner(new FileReader(filename));
	while(infile.hasNextLine()){
		String line = infile.nextLine();
		String sp = line.substring(0, line.indexOf(" ||| "));
		String en = line.substring(line.indexOf(" ||| ")+5, line.lastIndexOf(" ||| "));
		Double prob = Double.parseDouble(line.substring(line.lastIndexOf(" ||| ")+5));
		if(spanish_ngrams.containsKey(sp))
			spanish_ngrams.get(sp).add(new Pair<String,Double>(en,prob));
		else{			
			spanish_ngrams.put(sp, new ArrayList<Pair<String,Double>>());
			spanish_ngrams.get(sp).add(new Pair<String,Double>(en,prob));
		}
	}//end while

	return spanish_ngrams;
	
}//end read readTM

//Input is a list of unigrams, output is a dictionary, which is a subset of self.spanish
	//for each unigram, bigram, trigram from input that is in self.spanish	
public static HashMap<String, ArrayList<Pair<String,Double>>> getsubset(HashMap<String, ArrayList<Pair<String,Double>>> tm, ArrayList<String> unigrams){
		ArrayList<String> bigrams = get_ngrams(unigrams,2); 
		ArrayList<String> trigrams = get_ngrams(unigrams,3);
		HashMap<String, ArrayList<Pair<String,Double>>> subset = new HashMap<String, ArrayList<Pair<String,Double>>>();
		ArrayList<String> ngrams = new ArrayList<String>(unigrams);
		ngrams.addAll(bigrams);
		ngrams.addAll(trigrams);
		for(String ngram : ngrams){
			if(tm.containsKey(ngram))
				subset.put(ngram, tm.get(ngram));
		}
		return subset;
}


/**

TODO: No matter what is added, the worse path can be dropped if the last two English
	words are the same OR if the spanish coverage vectors are the same
**/

//Search for best translation; input is unigrams (list of words) from source sentence and searchspace dictionary
public static String search(ArrayList<String> unigrams, String hyp, HashMap<String, ArrayList<Pair<String, Double>>> searchspace){
	//Initial stack for unigrams
	ArrayList<Node> init_stack = new ArrayList<Node>();
	int stackindex = 1;
	int numstacks = unigrams.size(); // #stacks = 0, 1 through numstacks

	//get heuristic hypothesis
	double hypscore = Double.parseDouble(hyp.substring(0, hyp.indexOf(' ')));
	hyp = hyp.substring(hyp.indexOf(' '));
	
	ArrayList<ArrayList<Node>> stacks = new ArrayList<ArrayList<Node>>();
	//initialize stacks
	for(int i=0; i<=numstacks; i++)
		stacks.add(new ArrayList<Node>());

	//add null node
	Node nullnode = new Node("", 0.0, null);
	nullnode.coverage = new int[numstacks];
	for(int i=0; i<numstacks; i++)
		nullnode.coverage[i] = 0;
	nullnode.history = new ArrayList<String>();
	nullnode.prob = 0.0;
	stacks.get(0).add(nullnode);
	
	int r = 3; //Reorder distance

	for(int s=Math.max(stackindex-1, 1); s>=Math.max(stackindex-3, 1); s--){
		for(Node node : stacks.get(s)){
			int ngramorder = stackindex - s; //length of phrase
			for(int i=Math.max(stackindex-r, 0); i<=Math.min(stackindex+r, numstacks-1); i++){
				String cov = "";
				for(int c=i; c<=i+ngramorder; c++)
					cov += node.coverage[c];
				if(!cov.contains("1")){ 	//check the rest of phrase in coverage vector
					//add searchspace[unigrams.get(i)] to stacks.get(stackindex)
					String searchphrase = "";
					for(int p=0; p<ngramorder; p++)
						searchphrase += unigrams.get(i+p) + " ";
					searchphrase = searchphrase.trim();
					ArrayList<Pair<String, Double>> list = searchspace.get(searchphrase);
					for(Pair<String, Double> pair : list){
						//check score against hyp
						double score = pair.second();
						if(score < hypscore){
							//make node object, set parent pointer, add to stack
							String phrase = pair.first();
							Node n = new Node(phrase, score+node.prob, node);
							n.coverage = n.parent.coverage;
							for(int c=i; c<=i+ngramorder; c++)
								n.coverage[c] = 1;
							n.history = n.parent.history;
							StringTokenizer phrasetok = new StringTokenizer(phrase);
							while(phrasetok.hasMoreTokens()) //update history
								n.history.add(phrasetok.nextToken());

							//check stack for duplicates
							if(stackindex > 1){
								int removeindex = -1;
								for(Node stacknode : stacks.get(stackindex)){
									String stackhist = stacknode.history.get(stacknode.history.size()-2) 
											+ " " + stacknode.history.get(stacknode.history.size()-1);
									String hist = n.history.get(n.history.size()-2) + " " 
											+ n.history.get(n.history.size()-1);
									if(stackhist.equals(hist)){ //duplicate found!
										if(stacknode.prob < n.prob){
											removeindex = stacks.get(stackindex).indexOf(stacknode);
											stacks.get(stackindex).add(n);
											break;
										}//end if stacknode.prob
									}//end if duplicate
								}//end for stacknode in stack
								if(removeindex >= 0)
									stacks.get(stackindex).remove(removeindex);
							}//end if stackindex > 1
							else stacks.get(stackindex).add(n);
						}//end if score < hypscore
					}//end for pair in list
				}//end if node.coverage
			}//end for i
		}//end for node in stacks
	}//end for s

	double bestscore = hypscore;
	Node bestnode = null;
	for(Node n : stacks.get(numstacks)){
		//String hist = "";
		if(n.prob >= bestscore){
			bestnode = n;
			bestscore = n.prob;
		}
	}

	String result = "";
	for(String word : bestnode.history)
		result += word + " ";
	return result.trim();
}//end search

}//end class
