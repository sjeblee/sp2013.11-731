/** Convert MADA output back to Arabic text
* @author Serena Jeblee sjeblee@cs.cmu.edu
*/

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

public class ConvertMADA{

public static void main(String[] args){

	if(args.length < 2){
		System.out.println("Usage: java ConvertMADA [MADAfile] [originalfile]");
		System.exit(1);
	}

	String madafilename = args[0];
	String filename = args[1];
	
	try{
		Scanner infile = new Scanner(new FileReader(filename));
		Scanner madafile = new Scanner(new FileReader(madafilename));
		FileWriter outfile = new FileWriter(filename + ".mada");
		String buffer = "";

		while(infile.hasNextLine() && madafile.hasNextLine()){
			String line = infile.nextLine();
			String madaline = madafile.nextLine();
			String newline = "";

			//Un-Buckwalter
			for(int x=0; x<madaline.length(); x++){
				char c = madaline.charAt(x);
				char newc = unbuckwalter(c);
				newline += "" + newc;
			}
			madaline = newline;
			newline = "";

			//Match @@ to original
			StringTokenizer tok = new StringTokenizer(line);
			StringTokenizer madatok = new StringTokenizer(madaline);
			while(madatok.hasMoreTokens()){
				String madaword = madatok.nextToken();
				if(madaword.contains("@")){
					newline += tok.nextToken() + " ";
				}
				else{
					if((madaword.charAt(madaword.length()) == '+') 
						|| (madaword.charAt(0) == '+'))		//skip past clitics
						newline += madaword + " ";
					else{
						tok.nextToken();		//match base word
						newline += madaword + " ";
					}
				}
			}//end while

			//write sentence to outfile
			outfile.write(newline.trim() + "\n");
		}//end while hasNextLine

		infile.close();
		madafile.close();
		outfile.close();
	}
	catch(IOException e){
		System.out.println(e.getMessage());
	}
}//end main

public static char unbuckwalter(char c){
	if((c == '@') || (c == '+'))
		return c;
	else if(c == 'A')
		return 'ا';
	else if(c == '\'')
		return 'ء';
	else if(c == '|')
		return 'آ';
	else if(c == '>')
		return 'أ';
	else if(c == '&')
		return 'ؤ';
	else if(c == '<')
		return 'إ';
	else if(c == '}')
		return 'ئ';
	else if(c == 'b')
		return 'ب';
	else if(c == 'p')
		return 'ة';
	else if(c == 't')
		return 'ت';
	else if(c == 'v')
		return 'ث';
	else if(c == 'j')
		return 'ج';
	else if(c == 'H')
		return 'ح';
	else if(c == 'x')
		return 'خ';
	else if(c == 'd')
		return 'د';
	else if(c == '*')
		return 'ذ';
	else if(c == 'r')
		return 'ر';
	else if(c == 'z')
		return 'ز';
	else if(c == 's')
		return 'س';
	else if(c == '$')
		return 'ش';
	else if(c == 'S')
		return 'ص';
	else if(c == 'D')
		return 'ض';
	else if(c == 'T')
		return 'ط';
	else if(c == 'Z')
		return 'ظ';
	else if(c == 'E')
		return 'ع';
	else if(c == 'g')
		return 'غ';
	else if(c == '_')
		return 'ﻻ';
	else if(c == 'f')
		return 'ف';
	else if(c == 'q')
		return 'ق';
	else if(c == 'k')
		return 'ك';
	else if(c == 'l')
		return 'ل';
	else if(c == 'm')
		return 'م';
	else if(c == 'n')
		return 'ن';
	else if(c == 'h')
		return 'ه';
	else if(c == 'w')
		return 'و';
	else if(c == 'Y')
		return 'ى';
	else if(c == 'y')
		return 'ي';
	else if(c == 'F')
		return 'ً';
	else if(c == 'N')
		return 'ٌ';
	else if(c == 'K')
		return 'ٍ';
	else if(c == 'a')
		return 'َ';
	else if(c == 'u')
		return 'ُ';
	else if(c == 'i')
		return 'ِ';
	else if(c == '~')
		return 'ّ';
	else if(c == 'o')
		return 'ْ';
	else if(c == '`')
		return 'ٰ';
	else if(c == '{')
		return 'ﭐ';
	else if(c == 'p')
		return 'پ';
	else if(c == 'J')
		return 'چ';
	else if(c == 'V')
		return 'ﭪ';
	else if(c == 'G')
		return 'گ';
	else return c;
	
}//end unbuckwalter

}
















