package Lexical_Analysis;


public class Main {

	public static void main(String args[]) throws Exception {
		Read_File f = new Read_File("src/test_L.txt");
		L_Analyzer a = new L_Analyzer(f.get_strbuf());
		a.analysis();
		System.out.println(a.get_token());
	}

	
}
