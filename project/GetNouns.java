import java.io.*;
//import java.util.ArrayList;
import java.util.Scanner;
//import java.util.StringTokenizer;

public class GetNouns{

public static void main(String[] args){

	if(args.length < 2){
		System.out.println("Usage: java GetNouns [vocab-file] [noun-file]");
		System.exit(1);
	}

	String filename = args[0];
	String outfilename = args[1];
	
	try{
		Scanner infile = new Scanner(new FileReader(filename));
		FileWriter outfile = new FileWriter(outfilename);

		while(infile.hasNextLine()){
			String word = infile.nextLine();
			if( ((word.length()>1) && word.substring(0,2).equals("ال")) 
			|| ((word.length()>0) && (word.charAt(word.length()-1) == 'ة')) )
				outfile.write(word + "\n");
		}

	}//end try

	catch(IOException e){

	}
}//end main

}
