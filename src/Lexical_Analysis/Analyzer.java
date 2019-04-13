package Lexical_Analysis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Analyzer {
	
	static ArrayList<String> keyword = new ArrayList<String>(  //关键字
			Arrays.asList("abstract", "boolean", "break", "byte","case",
					"catch", "char", "class", "continue", "default", "do",
					"double", "else", "extends", "final", "finally", "float",
					"for","if", "implements", "import", "instanceof", "int",
					"interface", "long", "native", "new", "package", "private",
					"protected", "public", "return", "short", "static", "super",
					"switch","synchronized", "this", "throw","throws", "transient",
					"try","void","volatile","while","strictfp","enum","goto","const","assert"));
	static ArrayList<Character> operator = new ArrayList<Character>( //一位操作符
			Arrays.asList('+', '-', '*', '/', '&', '|', '~', '>', '=', '<'));
	static ArrayList<String> operator2 = new ArrayList<String>( //两位操作符
			Arrays.asList(">=", "<=", "!=", "++", "--"));
	static ArrayList<Character> boundary = new ArrayList<Character>( //边界符
			Arrays.asList(',', ';', '(', ')', '[', ']', '{', '}'));
	
	static ArrayList<String> wordlist = new ArrayList<String>();
	static ArrayList<String> tokenlist = new ArrayList<String>();
	static Map<String, Integer> symbolmap = new HashMap<String, Integer>();
	
	public boolean is_letter_(char c) { //判断是否是字母或下划线
		return ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_');
	}
	
	public boolean is_digit(char c) { //判断是否是数字
		return (c >= '0' && c <= '9');
	}
	
	public boolean is_keyword(String s) { //判断是否是关键字
		return (keyword.contains(s));
	}
	
	public boolean is_operator(char c) { //判断是否是一位操作符
		return (operator.contains(c));
	}
	
	public boolean is_operator2(String s) { //判断是否是两位操作符
		return (operator2.contains(s));
	}
	
	public boolean is_boundary(char c) { //判断是否是边界符
		return (boundary.contains(c));
	}
	
	public String binary_group(String s) { //根据token返回二元组
		switch(s) {
		case "do": return "<DO, - >";
		case "if": return "<IF, - >";
		case "for": return "<FOR, - >";
		case "int": return "<INT, - >";
		case "new": return "<NEW, - >";
		case "try": return "<TRY, - >";
		case "byte": return "<BYTE, - >";
		case "case": return "<CASE, - >";
		case "char": return "<CHAR, - >";
		case "else": return "<ELSE, - >";
		case "long": return "<LONG, - >";
		case "this": return "<THIS, - >";
		case "void": return "<VOID, - >";
		case "enum": return "<ENUM, - >";
		case "goto": return "<GOTO, - >";
		case "break": return "<BREAK, - >";
		case "catch": return "<CATCH, - >";
		case "class": return "<CLASS, - >";
		case "final": return "<FINAL, - >";
		case "float": return "<FLOAT, - >";
		case "short": return "<SHORT, - >";
		case "super": return "<SUPER, - >";
		case "throw": return "<THROW, - >";
		case "while": return "<WHILE, - >";
		case "const": return "<CONST, - >";
		case "double": return "<DOUBLE, - >";
		case "import": return "<IMPORT, - >";
		case "native": return "<NATIVE, - >";
		case "public": return "<PUBLIC, - >";
		case "return": return "<RETURN, - >";
		case "static": return "<STATIC, - >";
		case "switch": return "<SWITCH, - >";
		case "throws": return "<THROWS, - >";
		case "assert": return "<ASSERT, - >";
		case "boolean": return "<BOOLEAN, - >";
		case "default": return "<DEFAULT, - >";
		case "extends": return "<EXTENDS, - >";
		case "finally": return "<FINALLY, - >";
		case "package": return "<PACKAGE, - >";
		case "private": return "<PRIVATE, - >";
		case "abstract": return "<ABSTRACT, - >";
		case "continue": return "<CONTINUE, - >";
		case "volatile": return "<VOLATILE, - >";
		case "strictfp": return "<STRICTFP, - >";
		case "interface": return "<INTERFACE, - >";
		case "protected": return "<PROTECTED, - >";
		case "transient": return "<TRANSIENT, - >";
		case "implements": return "<IMPLEMENTS, - >";
		case "instanceof": return "<INSTANCEOF, - >";
		case "synchronized": return "<SYNCHRONIZED, - >";
		case ">=": return "<nole, - >";
		case "<=": return "<nomo, - >";
		case "!=": return "<noeq, - >";
		case "++": return "<INC, - >";
		case "--": return "<DEC, - >";
		default : System.out.println("ERROR, string not exist.");
		return null;
		}
	}
	
	public String binary_group(char c) { //根据token返回二元组
		switch(c) {
		case '+': return "<ADD, - >";
		case '-': return "<MIN, - >";
		case '*': return "<MUL, - >";
		case '/': return "<DIC, - >";
		case '>': return "<GT, - >";
		case '<': return "<LT, - >";
		case '=': return "<EQ, - >";
		case '&': return "<AND, - >";
		case '|': return "<OR, - >";
		case '~': return "<NOT, - >";
		case ',': return "<COOMA, - >";
		case ';': return "<SEMI, - >";
		case '(': return "<SLP, - >";
		case ')': return "<SRP, - >";
		case '[': return "<MLP, - >";
		case ']': return "<MRP, - >";
		case '{': return "<LP, - >";
		case '}': return "<RP, - >";
		default : System.out.println("ERROR, char not exist.");
			return null;
		}
	}
	
	public String binary_group(String s, String type) { //根据token返回二元组
		if(type.equals("id")) {
			return "<ID, " + s + ">";
		}
		else if(type.equals("digit")) {
			return "<DIGIT, " + s + ">";
		}
		else if(type.equals("fdigit")) {
			return "<FDIGIT, " + s + ">";
		}
		else{
			System.out.println("ERROR,!");
			return null;
		}
	}
	
	public int type_code(String s) {
		if(keyword.contains(s)){
			for(int i = 0; i < keyword.size(); i++) {
				if(keyword.get(i).equals(s)) {
					return i+1;
				}
			}
		}
		if(operator2.contains(s)){
			for(int i = 0; i < operator2.size(); i++) {
				if(operator2.get(i).equals(s)) {
					return i+61;
				}
			}
		}
		return 0;
	}
	
	public int type_code(char c) {
		if(boundary.contains(c)){
			for(int i = 0; i < boundary.size(); i++) {
				if(boundary.get(i) == c) {
					return i;
				}
			}
		}
		return 0;
	}
	
	public Analyzer(StringBuffer strbuf) {
		String token = "";
		boolean first_letter = true;
		boolean is_digit = false;
		boolean is_float = false;
		for(int i = 0; i < strbuf.length(); i++) {
			char ch = strbuf.charAt(i);
			if(is_letter_(ch) || is_digit(ch)) {
				if(first_letter) { //根据首字母是否是数字判断该词是否是常数
					if(is_digit(ch)) {
						is_digit = true;
					}
				}
				first_letter = false;
				token += ch;
			}
			else if(ch == '.') {
				if(is_digit) {
					token += ch;
					is_float = true;
				}
				else {
					System.out.println("ERROR!");
				}
			}
			else {
				if(token != "") {
					wordlist.add(token);
					if(is_keyword(token)) {
						//symbolmap.put(null, type_code(token));
						tokenlist.add(binary_group(token));
					}
					else {
						if(is_digit) {
							if(is_float) {
								symbolmap.put(token, 76);
								tokenlist.add(binary_group(token, "fdigit"));
							}
							else {
								symbolmap.put(token, 75);
								tokenlist.add(binary_group(token, "digit"));
							}
						}
						else {
							symbolmap.put(token, 74);
							tokenlist.add(binary_group(token, "id"));
						}
					}
					token = "";
					first_letter = true;
					is_digit = false;
					is_float = false;
				}
				if(ch == '/' && strbuf.charAt(i+1) == '*') { //跳过注释
					i = i + 2;
					for(int j = i; j < strbuf.length(); j++) {
						if(strbuf.charAt(j) == '*' && strbuf.charAt(j+1) == '/') {
							i = j + 1;
							break;
						}
					}
				}
				else if(is_operator(ch)) {
					String temp = String.valueOf(ch) + String.valueOf(strbuf.charAt(i+1));
					if(is_operator2(temp)) { //判断是否为两位操作符
						i++;
						//symbolmap.put(temp, type_code(temp));
						wordlist.add(temp);
						tokenlist.add(binary_group(temp));
					}
					else {
						//symbolmap.put(String.valueOf(ch), type_code(ch)+51);
						wordlist.add(String.valueOf(ch));
						tokenlist.add(binary_group(ch));
					}
				}
				else if(is_boundary(ch)) {
					//symbolmap.put(String.valueOf(ch), type_code(ch)+66);
					wordlist.add(String.valueOf(ch));
					tokenlist.add(binary_group(ch));
				}
				
			}
		}
		/*for(int i = 0; i < tokenlist.size(); i++) {
			System.out.println(wordlist.get(i) + "   " + tokenlist.get(i));
		}*/
		/*
		for (Map.Entry<String, Integer> entry : symbolmap.entrySet()) { 
			  System.out.println("< " + entry.getValue() + ",  " + entry.getKey() + "> "); 
		}*/
	}
	
}
