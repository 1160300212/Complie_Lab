package Grammar_Analysis;

import java.util.ArrayList;

import Lexical_Analysis.Analyzer;
import Lexical_Analysis.Read_File;

public class Main {

	public static void main(String args[]) throws Exception {
		ArrayList<Production> production = new ArrayList<Production>();
		ArrayList<String> symbolN = new ArrayList<String>();
		ArrayList<String> symbolT = new ArrayList<String>();
		
		Read_Grammar rg = new Read_Grammar("src/grammar.txt");
		production = rg.get_production();
		symbolN = rg.get_symbolN();
		symbolT = rg.get_symbolT();
		LR_AnalysisTable at = new LR_AnalysisTable(production, symbolN, symbolT);
		
		
		StringBuffer strbuf = null;
		Read_File f = new Read_File("src/test1.txt");
		strbuf = f.strbuf;
		System.out.println(strbuf);
		
		Analyzer a = new Analyzer();
		
		G_Analyzer ga = new G_Analyzer(production, at.Action, at.Goto, a.Analyzer(strbuf));
	}
}
