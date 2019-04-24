package Grammar_Analysis;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Read_Grammar {

	private String filename = "";
	private ArrayList<Production> production = new ArrayList<Production>();
	private ArrayList<String> symbolN = new ArrayList<String>();
	private ArrayList<String> symbolT = new ArrayList<String>();

	public Read_Grammar(String filename) throws Exception {
		this.filename = filename;
		FileReader f = new FileReader(filename);
		BufferedReader br = new BufferedReader(f);
		String temp = null;
		temp = br.readLine();
		for(int i = 0; i < temp.length(); i++) {
			String word = "";
			int j = i;
			if (temp.charAt(i) == '<') {
				for (j = i + 1; j < temp.length(); j++) {
					if (temp.charAt(j) == '>' && j - i > 1) {
						break;
					}
					word += temp.charAt(j);
				}
				symbolN.add(word);
			}
		}
		temp = br.readLine();
		for(int i = 0; i < temp.length(); i++) {
			String word = "";
			int j = i;
			if (temp.charAt(i) == '<') {
				for (j = i + 1; j < temp.length(); j++) {
					if (temp.charAt(j) == '>' && j - i > 1) {
						break;
					}
					word += temp.charAt(j);
				}
				symbolT.add(word);
			}
		}
		temp = br.readLine();
		while ((temp = br.readLine()) != null) {
			Production p = new Production(temp);
			production.add(p);
		}
	}

	public ArrayList<Production> get_production() throws Exception {
		return production;
	}
	
	public ArrayList<String> get_symbolN() throws Exception {
		return symbolN;
	}
	
	public ArrayList<String> get_symbolT() throws Exception {
		return symbolT;
	}

}

class Production{
	String prod = "";
	String left = "";
	ArrayList<String> right = new ArrayList<String>();
	
	public Production(String s){
		this.prod = s;
		for (int i = 0; i < s.length(); i++) {
			String word = "";
			int j = i;
			if (s.charAt(i) == '<') {
				for (j = i + 1; j < s.length(); j++) {
					if (s.charAt(j) == '>' && j - i > 1) {
						break;
					}
					word += s.charAt(j);
				}
				if(left.equals("")) {
					left = word;
				}
				else {
					right.add(word);
				}
			}
		}
	}
}
