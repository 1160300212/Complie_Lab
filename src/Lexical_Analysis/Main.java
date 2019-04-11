package Lexical_Analysis;

public class Main {

	public static void main(String args[]) throws Exception {
		StringBuffer strbuf = null;
		Read_File f = new Read_File("src/test.txt");
		strbuf = f.strbuf;
		System.out.println(strbuf);
		System.out.println(strbuf.charAt(0));
		System.out.println(strbuf.charAt(1));
		System.out.println(strbuf.charAt(2));
	}
	
}
