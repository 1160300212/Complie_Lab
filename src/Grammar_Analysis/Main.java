package Grammar_Analysis;

import java.util.ArrayList;

import Lexical_Analysis.L_Analyzer;
import Lexical_Analysis.Read_File;

public class Main {

	public static void main(String args[]) throws Exception {
		
		Read_Grammar rg = new Read_Grammar("src/Grammar.txt");
		LR_AnalysisTable at = new LR_AnalysisTable(rg.get_production(), rg.get_symbolN(), rg.get_symbolT());
		Read_File file = new Read_File("src/test_G.txt");
		L_Analyzer a = new L_Analyzer(file.get_str());
		System.out.println(file.get_str());
		
		a.analysis();

		G_Analyzer ga = new G_Analyzer(rg.get_production(), at.get_action(), at.get_goto(), a.grammar_input());
		
		ga.analysis();
	}
}
