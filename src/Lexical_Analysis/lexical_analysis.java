package Lexical_Analysis;

public class lexical_analysis {

	static String[] keyword = {"int", "float", "bool", "if", "else", "do", "while"};
	static char[] operator = {'+', '-', '*', '/', '>', '<', '&', '|', '!', '~', '^'};
	static char[] boundary = {',', ';', '(', ')', '[', ']', '{', '}', '.', '='};
	static int pos = 0;
	
	public void analysis(StringBuffer strbuf) {
		for(int i = 0; i < strbuf.length(); i++) {
			String token = null;
			char c = strbuf.charAt(i);
			boolean first_letter = true;
			if(first_letter && is_letter_(c)) {
				token += c;
				pos++;
				first_letter = false;
			}
		}
	}
	
	public boolean is_letter_(char c) {
		return ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_');
	}
	
	public boolean is_digit(char c) {
		return (c >= '0' && c <= '9');
	}
	
	
	
}
