package Lexical_Analysis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Analyzer {
	
	static ArrayList<String> keyword = new ArrayList<String>(  //关键字
			Arrays.asList("int", "float", "if", "then", "else", "do", "while")); //编码为1-7
	static ArrayList<Character> operator = new ArrayList<Character>( //一位操作符
			Arrays.asList('+', '-', '*', '/', '>', '=', '<', '&', '|'));  //编码为8-16
	static ArrayList<String> operator2 = new ArrayList<String>( //两位操作符
			Arrays.asList(">=", "<=", "!=", "++", "--", "==", "&&", "||")); //编码为17-24
	static ArrayList<Character> boundary = new ArrayList<Character>( //边界符
			Arrays.asList(',', ';', '(', ')', '[', ']', '{', '}')); //编码为24-32
	// id: 33, inum: 34, fnum: 35;
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
		case "int": return "<INT, - >";
		case "else": return "<ELSE, - >";
		case "then": return "<THEN, - >";
		case "float": return "<FLOAT, - >";
		case "while": return "<WHILE, - >";
		case ">=": return "<nole, - >";
		case "<=": return "<nomo, - >";
		case "!=": return "<noeq, - >";
		case "++": return "<INC, - >";
		case "--": return "<DEC, - >";
		case "==": return "<LEQ, - >";
		case "||": return "<LOR, - >";
		case "&&": return "<LAND, - >";
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
		case '|': return "<OR, - >";
		case '&': return "<AND, - >";
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
		else if(type.equals("inum")) {
			return "<INUM, " + s + ">";
		}
		else if(type.equals("fnum")) {
			return "<FNUM, " + s + ">";
		}
		else{
			System.out.println("ERROR!");
			return null;
		}
	}
	
	public int type_code(String s) { //查询类别编码
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
					return i+15;
				}
			}
		}
		return 0;
	}
	
	public int type_code(char c) { //查询类别编码
		if(boundary.contains(c)){
			for(int i = 0; i < boundary.size(); i++) {
				if(boundary.get(i) == c) {
					return i;
				}
			}
		}
		return 0;
	}
	
	public ArrayList<String> Analyzer(StringBuffer strbuf) {
		String token = "";
		boolean first_letter = true;
		boolean is_digit = false;
		boolean is_float = false;
		int point = 0;
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
				if(is_digit && point == 0) {
					point = 1;
					token += ch;
					is_float = true;
				}
				else {
					token += ch;
					is_float = true;
					System.out.println("ERROR, illegal float!");
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
								symbolmap.put(token, 35);
								tokenlist.add(binary_group(token, "fnum"));
							}
							else {
								symbolmap.put(token, 34);
								tokenlist.add(binary_group(token, "inum"));
							}
						}
						else {
							symbolmap.put(token, 33);
							tokenlist.add(binary_group(token, "id"));
						}
					}
					token = "";
					first_letter = true;
					is_digit = false;
					is_float = false;
					point = 0;
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
		for(int i = 0; i < tokenlist.size(); i++) {
			System.out.println(wordlist.get(i) + "   " + tokenlist.get(i));
		}
		
		for (Map.Entry<String, Integer> entry : symbolmap.entrySet()) { 
			  System.out.println("< " + entry.getValue() + ",  " + entry.getKey() + "> "); 
		}
		
		ArrayList<String> input = new ArrayList<String>();
		for(int i = 0; i < tokenlist.size(); i++) {
			String tmp = "";
			int t = 0;
			for(int j = 0; j < tokenlist.get(i).length(); j++){
				if(tokenlist.get(i).charAt(j) == ',') {
					t = j;
				}
			}
			tmp = tokenlist.get(i).substring(1, t);
			if(tmp.equals("DO")) input.add("do");
			else if(tmp.equals("IF")) input.add("if");
			else if(tmp.equals("INT")) input.add("int");
			else if(tmp.equals("ELSE")) input.add("else");
			else if(tmp.equals("THEN")) input.add("then");
			else if(tmp.equals("FLOAT")) input.add("float");
			else if(tmp.equals("WHILE")) input.add("while");
			else if(tmp.equals("nole")) input.add(">=");
			else if(tmp.equals("nomo")) input.add("<=");
			else if(tmp.equals("LEQ")) input.add("==");
			else if(tmp.equals("LAND")) input.add("&&");
			else if(tmp.equals("LOR")) input.add("}}");
			else if(tmp.equals("ID")) input.add("id");
			else if(tmp.equals("INUM")) input.add("inum");
			else if(tmp.equals("FNUM")) input.add("fnum");
			else if(tmp.equals("ADD")) input.add("+");
			else if(tmp.equals("MIN")) input.add("-");
			else if(tmp.equals("MUL")) input.add("*");
			else if(tmp.equals("DIV")) input.add("/");
			else if(tmp.equals("EQ")) input.add("=");
			else if(tmp.equals("SEMI")) input.add(";");
			else if(tmp.equals("SLP")) input.add("(");
			else if(tmp.equals("SRP")) input.add(")");
			else if(tmp.equals("LP")) input.add("{");
			else if(tmp.equals("RP")) input.add("}");
		}
		return input;
	}
	
}
