package Lexical_Analysis;


public class Main {

	public static void main(String args[]) throws Exception {
		StringBuffer strbuf = null;
		Read_File f = new Read_File("src/test.txt");
		strbuf = f.strbuf;
		System.out.println(strbuf);
		
		Analyzer a = new Analyzer();
		a.Analyzer(strbuf);
		//analysis(strbuf);
	}

	
}
