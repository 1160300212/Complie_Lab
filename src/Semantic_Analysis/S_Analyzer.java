package Semantic_Analysis;

import java.util.ArrayList;
import java.util.Stack;

import Grammar_Analysis.Production;
import Lexical_Analysis.SymbolTable_record;

public class S_Analyzer {

	ArrayList<Production> P = new ArrayList<Production>();
	ArrayList<String[]> Goto = new ArrayList<String[]>();
	ArrayList<String[]> Action = new ArrayList<String[]>();
	ArrayList<String> input = new ArrayList<String>();
	
	ArrayList<SymbolTable_record> symbolmap = new ArrayList<SymbolTable_record>();
	ArrayList<String> four_tuple = new ArrayList<String>();
	ArrayList<String> output_order = new ArrayList<String>();
	
	int tmpind = 1;
	
	public S_Analyzer(ArrayList<Production> P, ArrayList<String[]> Action, ArrayList<String[]> Goto, ArrayList<String> input, ArrayList<SymbolTable_record> symbolmap) {
		this.P = P;
		this.Goto = Goto;
		this.Action = Action;
		this.input = input;
		this.symbolmap = symbolmap;
		
		for(int i = 0; i < P.size(); i++) {
			System.out.println("i "+i+" "+P.get(i).prod);
		}
		
		analysis();

		for(int i = 0; i < four_tuple.size(); i++) {
			System.out.println(i + "  " +four_tuple.get(i) + "    " + output_order.get(i));
		}
		
		for(int i = 0; i < symbolmap.size(); i++) {
			System.out.println(symbolmap.get(i).id + " " + symbolmap.get(i).type + " " + symbolmap.get(i).value);
		}
		
	}
	
	public void analysis() {
		Stack<String> state_stk = new Stack<String>();
		Stack<Stack_record> symbol_stk = new Stack<Stack_record>();
		state_stk.push("0");
		input.add("#");
		int index = 0; //输入符号串下标
		System.out.println();
		while(true) {
			String op = "null";
			String s = "";
			for(int i = 0; i < Action.size(); i++) { //s为转移指令
				if(Action.get(i)[0].equals(state_stk.peek()) && Action.get(i)[1].equals(input.get(index))) {
					op = "Action";
					s = Action.get(i)[2];
				}
			}
			if(op.equals("Action") && s.charAt(0) == 'S') { //移位操作
				state_stk.push(s.substring(1));
				Stack_record re = new Stack_record(input.get(index));
				if(input.get(index).equals("id")) { //输入符号为 id 则再读取下一位 即id的值
					index++;
					re.addr = Integer.parseInt(input.get(index));
					re.id_value = symbolmap.get(re.addr).id;
					re.num_value = symbolmap.get(re.addr).value;
				}
				else if(input.get(index).equals("inum") || input.get(index).equals("fnum")) {
					index++;
					re.num_value = input.get(index);
				}
				symbol_stk.push(re);
				index++;
				if(symbol_stk.peek().symbol.equals("while")) {
					symbol_stk.peek().label = four_tuple.size();
				}
				if(symbol_stk.peek().symbol.equals("if")) {
					symbol_stk.peek().label = four_tuple.size();
				}
				if(symbol_stk.peek().symbol.equals("else")) {
					symbol_stk.peek().label = four_tuple.size();
				}
				print_symbol_stk(symbol_stk);
			}
			else if(op.equals("Action") && s.charAt(0) == 'R') { //规约操作
				int tmp = Integer.parseInt(s.substring(1));
				//print_symbol_stk(symbol_stk);
				Stack_record re = new Stack_record(P.get(tmp).left);
				reduce_case(P.get(tmp), symbol_stk, re);
				for(int i = 0; i < P.get(tmp).right.size(); i++) {
					state_stk.pop();
					symbol_stk.pop();
				}
				symbol_stk.push(re);
				for(int i = 0; i < Goto.size(); i++) {
					if(Goto.get(i)[0].equals(state_stk.peek()) && Goto.get(i)[1].equals(P.get(tmp).left)) {
						state_stk.push(Goto.get(i)[2]);
						break;
					}
				}
				System.out.println(P.get(tmp).prod);
			}
			else if(op.equals("Action") && s.equals("acc")){
				System.out.println(P.get(0).prod);
				break;
			}
		}
	}

	public void print_symbol_stk(Stack<Stack_record> symbol_stk) {
		System.out.print("symbol_stk:   ");
		for(int i = 0; i < symbol_stk.size(); i++) {
			System.out.print(symbol_stk.get(i).symbol + "." + symbol_stk.get(i).id_value + "." + symbol_stk.get(i).bool + "  ");
			//System.out.print(symbol_stk.get(i).symbol + "  ");
		}
		System.out.println();
	}
	
	public void reduce_case(Production p, Stack<Stack_record> symbol_stk, Stack_record re) {
		switch(P.indexOf(p)) {
		case 6: //<dec> -> <Type> <id> <;>
			symbolmap.get(symbol_stk.get(symbol_stk.size()-2).addr).type = symbol_stk.get(symbol_stk.size()-3).type;
			break;
		case 7: //<Type> -> <int>
			re.type = "int";
			re.width = 4;
			break;
		case 8: //<Type> -> <float>
			re.type = "float";
			re.width = 4;
			break;
		case 9: //<ass> -> <id> <=> <E> <;>
			float tmp = Float.parseFloat(symbol_stk.get(symbol_stk.size()-2).num_value);
			if(symbolmap.get(symbol_stk.get(symbol_stk.size()-4).addr).type.equals("int")) {
				symbolmap.get(symbol_stk.get(symbol_stk.size()-4).addr).value = String.valueOf((int)tmp);
			}else {
				symbolmap.get(symbol_stk.get(symbol_stk.size()-4).addr).value = symbol_stk.get(symbol_stk.size()-2).num_value;
			}
			symbol_stk.get(symbol_stk.size()-4).num_value = symbol_stk.get(symbol_stk.size()-2).num_value;
			if(symbol_stk.get(symbol_stk.size()-2).id_value.equals("")) {
				output_order.add(symbol_stk.get(symbol_stk.size()-4).id_value + " = " + symbol_stk.get(symbol_stk.size()-2).num_value);
				four_tuple.add("(=, " + symbol_stk.get(symbol_stk.size()-2).num_value + ", -, " + symbol_stk.get(symbol_stk.size()-4).id_value + ")");
			}
			else {
				output_order.add(symbol_stk.get(symbol_stk.size()-4).id_value + " = " + symbol_stk.get(symbol_stk.size()-2).id_value);
				four_tuple.add("(=, " + symbol_stk.get(symbol_stk.size()-2).id_value + ", -, " + symbol_stk.get(symbol_stk.size()-4).id_value + ")");
			}
			break;
		case 10: //<E> -> <id>
			re.id_value = symbol_stk.peek().id_value;
			re.num_value = symbol_stk.peek().num_value;
			break;
		case 11: //<E> -> <num>
			re.num_value = symbol_stk.peek().num_value;
			break;
		case 12: //<E> -> <E> <+> <E>
			re.id_value = "tmp" + tmpind;
			if((!symbol_stk.get(symbol_stk.size()-3).num_value.equals("")) && (!symbol_stk.get(symbol_stk.size()-1).num_value.equals(""))) {
				float tmp1 = Float.parseFloat(symbol_stk.get(symbol_stk.size()-3).num_value);
				float tmp2 = Float.parseFloat(symbol_stk.get(symbol_stk.size()-1).num_value);
				re.num_value = String.valueOf(tmp1 + tmp2);
			}
			tmpind++;
			if(symbol_stk.peek().id_value.equals("")) {
				output_order.add(re.id_value + " = " + symbol_stk.get(symbol_stk.size()-3).id_value + " + " + symbol_stk.peek().num_value);
				four_tuple.add("(+, " + symbol_stk.get(symbol_stk.size()-3).id_value + ", " + symbol_stk.peek().num_value + ", " + re.id_value + ")");
			}
			else {
				output_order.add(re.id_value + " = " + symbol_stk.get(symbol_stk.size()-3).id_value + " + " + symbol_stk.peek().id_value);
				four_tuple.add("(+, " + symbol_stk.get(symbol_stk.size()-3).id_value + ", " + symbol_stk.peek().id_value + ", " + re.id_value + ")");
			}
			break;
		case 13: //<E> -> <E> <-> <E>
			re.id_value = "tmp" + tmpind;
			if((!symbol_stk.get(symbol_stk.size()-3).num_value.equals("")) && (!symbol_stk.get(symbol_stk.size()-1).num_value.equals(""))) {
				float tmp1 = Float.parseFloat(symbol_stk.get(symbol_stk.size()-3).num_value);
				float tmp2 = Float.parseFloat(symbol_stk.get(symbol_stk.size()-1).num_value);
				re.num_value = String.valueOf(tmp1 - tmp2);
			}
			tmpind++;
			if(symbol_stk.peek().id_value.equals("")) {
				output_order.add(re.id_value + " = " + symbol_stk.get(symbol_stk.size()-3).id_value + " - " + symbol_stk.peek().num_value);
				four_tuple.add("(-, " + symbol_stk.get(symbol_stk.size()-3).id_value + ", " + symbol_stk.peek().num_value + ", " + re.id_value + ")");
			}
			else {
				output_order.add(re.id_value + " = " + symbol_stk.get(symbol_stk.size()-3).id_value + " - " + symbol_stk.peek().id_value);
				four_tuple.add("(-, " + symbol_stk.get(symbol_stk.size()-3).id_value + ", " + symbol_stk.peek().id_value + ", " + re.id_value + ")");
			}
			break;
		case 14: //<E> -> <E> <*> <E>
			re.id_value = "tmp" + tmpind;
			if((!symbol_stk.get(symbol_stk.size()-3).num_value.equals("")) && (!symbol_stk.get(symbol_stk.size()-1).num_value.equals(""))) {
				float tmp1 = Float.parseFloat(symbol_stk.get(symbol_stk.size()-3).num_value);
				float tmp2 = Float.parseFloat(symbol_stk.get(symbol_stk.size()-1).num_value);
				re.num_value = String.valueOf(tmp1 * tmp2);
			}
			tmpind++;
			if(symbol_stk.peek().id_value.equals("")) {
				output_order.add(re.id_value + " = " + symbol_stk.get(symbol_stk.size()-3).id_value + " * " + symbol_stk.peek().num_value);
				four_tuple.add("(*, " + symbol_stk.get(symbol_stk.size()-3).id_value + ", " + symbol_stk.peek().num_value + ", " + re.id_value + ")");
			}
			else {
				output_order.add(re.id_value + " = " + symbol_stk.get(symbol_stk.size()-3).id_value + " * " + symbol_stk.peek().id_value);
				four_tuple.add("(*, " + symbol_stk.get(symbol_stk.size()-3).id_value + ", " + symbol_stk.peek().id_value + ", " + re.id_value + ")");
			}
			break;
		case 15: //<E> -> <E> </> <E>
			re.id_value = "tmp" + tmpind;
			if((!symbol_stk.get(symbol_stk.size()-3).num_value.equals("")) && (!symbol_stk.get(symbol_stk.size()-1).num_value.equals(""))) {
				float tmp1 = Float.parseFloat(symbol_stk.get(symbol_stk.size()-3).num_value);
				float tmp2 = Float.parseFloat(symbol_stk.get(symbol_stk.size()-1).num_value);
				re.num_value = String.valueOf(tmp1 / tmp2);
			}
			tmpind++;
			if(symbol_stk.peek().id_value.equals("")) {
				output_order.add(re.id_value + " = " + symbol_stk.get(symbol_stk.size()-3).id_value + " / " + symbol_stk.peek().num_value);
				four_tuple.add("(/, " + symbol_stk.get(symbol_stk.size()-3).id_value + ", " + symbol_stk.peek().num_value + ", " + re.id_value + ")");
			}
			else {
				output_order.add(re.id_value + " = " + symbol_stk.get(symbol_stk.size()-3).id_value + " / " + symbol_stk.peek().id_value);
				four_tuple.add("(/, " + symbol_stk.get(symbol_stk.size()-3).id_value + ", " + symbol_stk.peek().id_value + ", " + re.id_value + ")");
			}
			break;
		case 16: //<E> -> <(> <E> <)>
			re.id_value = symbol_stk.get(symbol_stk.size()-2).id_value;
			re.num_value = symbol_stk.get(symbol_stk.size()-2).num_value;
			break;
		case 17: //<num> -> <inum>
			re.num_value = symbol_stk.get(symbol_stk.size()-1).num_value;
			break;
		case 18: //<num> -> <fnum>
			re.num_value = symbol_stk.get(symbol_stk.size()-1).num_value;
			break;
		case 19://<cond> -> <if> <(> <T> <)> <then> <{> <states> <}> <else> <{> <states> <}>
			int label = -1;
			int label2 = -1;
			for(int i = 0; i < symbol_stk.size(); i++) {
				if(symbol_stk.get(i).symbol.equals("else")) {
					label = symbol_stk.get(i).label;
				}
				else if(symbol_stk.get(i).symbol.equals("if")) {
					label2 = symbol_stk.get(i).label;
				}
			}
			four_tuple.add("(j , -, -, " + (four_tuple.size()+2)  + ")");
			output_order.add("goto " + (four_tuple.size()+1));
			four_tuple.set(label2+1, "(j , -, -, " + (label+1)  + ")");
			output_order.set(label2+1, "goto " + (label+1));
			System.out.println("if  --------" + label2);
			System.out.println("else  --------" + label);
			
			four_tuple.add(label, "(j , -, -, " + (four_tuple.size()+1)  + ")");
			output_order.add(label, "goto " +(four_tuple.size()));
			
			break;
		case 20://<T> -> <E> <>> <E>
			if((!symbol_stk.get(symbol_stk.size()-3).num_value.equals("")) && (!symbol_stk.get(symbol_stk.size()-1).num_value.equals(""))) {
				float tmp1 = Float.parseFloat(symbol_stk.get(symbol_stk.size()-3).num_value);
				float tmp2 = Float.parseFloat(symbol_stk.get(symbol_stk.size()-1).num_value);
				if(tmp1 > tmp2) {
					re.bool = true;
				}
				four_tuple.add("(j>" + ", " + symbol_stk.get(symbol_stk.size()-3).id_value + ", " + symbol_stk.get(symbol_stk.size()-1).id_value + ", " + (four_tuple.size()+2)+ ")");
				output_order.add("if "+ symbol_stk.get(symbol_stk.size()-3).id_value + " > " + symbol_stk.get(symbol_stk.size()-1).id_value + " goto " + (four_tuple.size()+1));
				four_tuple.add("(j " + ", -, -, ?)");
				output_order.add("goto ?");
			}
			break;
		case 21://<T> -> <E> <<> <E>
			if((!symbol_stk.get(symbol_stk.size()-3).num_value.equals("")) && (!symbol_stk.get(symbol_stk.size()-1).num_value.equals(""))) {
				float tmp1 = Float.parseFloat(symbol_stk.get(symbol_stk.size()-3).num_value);
				float tmp2 = Float.parseFloat(symbol_stk.get(symbol_stk.size()-1).num_value);
				if(tmp1 < tmp2) {
					re.bool = true;
				}
				four_tuple.add("(j<" + ", " + symbol_stk.get(symbol_stk.size()-3).id_value + ", " + symbol_stk.get(symbol_stk.size()-1).id_value + ", " + (four_tuple.size()+2)+ ")");
				output_order.add("if "+ symbol_stk.get(symbol_stk.size()-3).id_value + " < " + symbol_stk.get(symbol_stk.size()-1).id_value + " goto " + (four_tuple.size()+1));
				four_tuple.add("(j " + ", -, -, ?)");
				output_order.add("goto ?");
			}
			break;
		case 22://<T> -> <E> <>=> <E>
			if((!symbol_stk.get(symbol_stk.size()-3).num_value.equals("")) && (!symbol_stk.get(symbol_stk.size()-1).num_value.equals(""))) {
				float tmp1 = Float.parseFloat(symbol_stk.get(symbol_stk.size()-3).num_value);
				float tmp2 = Float.parseFloat(symbol_stk.get(symbol_stk.size()-1).num_value);
				if(tmp1 >= tmp2) {
					re.bool = true;
				}
				four_tuple.add("(j>=" + ", " + symbol_stk.get(symbol_stk.size()-3).id_value + ", " + symbol_stk.get(symbol_stk.size()-1).id_value + ", " + (four_tuple.size()+2)+ ")");
				output_order.add("if "+ symbol_stk.get(symbol_stk.size()-3).id_value + " >= " + symbol_stk.get(symbol_stk.size()-1).id_value + " goto " + (four_tuple.size()+1));
				four_tuple.add("(j " + ", -, -, ?)");
				output_order.add("goto ?");
			}
			break;
		case 23://<T> -> <E> <<=> <E>
			if((!symbol_stk.get(symbol_stk.size()-3).num_value.equals("")) && (!symbol_stk.get(symbol_stk.size()-1).num_value.equals(""))) {
				float tmp1 = Float.parseFloat(symbol_stk.get(symbol_stk.size()-3).num_value);
				float tmp2 = Float.parseFloat(symbol_stk.get(symbol_stk.size()-1).num_value);
				if(tmp1 <= tmp2) {
					re.bool = true;
				}
				
				four_tuple.add("(j<=" + ", " + symbol_stk.get(symbol_stk.size()-3).id_value + ", " + symbol_stk.get(symbol_stk.size()-1).id_value + ", " + (four_tuple.size()+2)+ ")");
				output_order.add("if "+ symbol_stk.get(symbol_stk.size()-3).id_value + " <= " + symbol_stk.get(symbol_stk.size()-1).id_value + " goto " + (four_tuple.size()+1));
				four_tuple.add("(j " + ", -, -, ?)");
				output_order.add("goto ?");
			}
			break;
		case 24://<T> -> <E> <==> <E>
			if((!symbol_stk.get(symbol_stk.size()-3).num_value.equals("")) && (!symbol_stk.get(symbol_stk.size()-1).num_value.equals(""))) {
				float tmp1 = Float.parseFloat(symbol_stk.get(symbol_stk.size()-3).num_value);
				float tmp2 = Float.parseFloat(symbol_stk.get(symbol_stk.size()-1).num_value);
				if(tmp1 == tmp2) {
					re.bool = true;
				}
				four_tuple.add("(j==" + ", " + symbol_stk.get(symbol_stk.size()-3).id_value + ", " + symbol_stk.get(symbol_stk.size()-1).id_value + ", " + (four_tuple.size()+2)+ ")");
				output_order.add("if "+ symbol_stk.get(symbol_stk.size()-3).id_value + " == " + symbol_stk.get(symbol_stk.size()-1).id_value + " goto " + (four_tuple.size()+1));
				four_tuple.add("(j " + ", -, -, ?)");
				output_order.add("goto ?");
			}
			break;
		case 25://<T> -> <E> <!=> <E>
			if((!symbol_stk.get(symbol_stk.size()-3).num_value.equals("")) && (!symbol_stk.get(symbol_stk.size()-1).num_value.equals(""))) {
				float tmp1 = Float.parseFloat(symbol_stk.get(symbol_stk.size()-3).num_value);
				float tmp2 = Float.parseFloat(symbol_stk.get(symbol_stk.size()-1).num_value);
				if(tmp1 != tmp2) {
					re.bool = true;
				}
				four_tuple.add("(j!=" + ", " + symbol_stk.get(symbol_stk.size()-3).id_value + ", " + symbol_stk.get(symbol_stk.size()-1).id_value + ", " + (four_tuple.size()+2)+ ")");
				output_order.add("if "+ symbol_stk.get(symbol_stk.size()-3).id_value + " != " + symbol_stk.get(symbol_stk.size()-1).id_value + " goto " + (four_tuple.size()+1));
				four_tuple.add("(j " + ", -, -, ?)");
				output_order.add("goto ?");
			}
			break;
		case 26://<loop> -> <while> <(> <T> <)> <do> <{> <states> <}> <;>
			int wlabel = -1;
			for(int i = 0; i < symbol_stk.size(); i++) {
				if(symbol_stk.get(i).symbol.equals("while")) {
					wlabel = symbol_stk.get(i).label;
					
				}
			}
			four_tuple.add("(j " + ", -, -, " + wlabel  + ")");
			output_order.add("goto " + wlabel);
			four_tuple.set(wlabel+1, "(j , -, -, " + four_tuple.size() +")");
			output_order.set(wlabel+1, "goto " + four_tuple.size());
			break;
		}
	}


}

class Stack_record {
	String symbol = ""; // E T id
	int addr = 0; //0,1,2
	String id_value = ""; //id->a
	String num_value = "";
	String type = ""; // int float
	int width = 0;
	boolean bool = false;
	int label;
	public Stack_record(String s) {
		this.symbol = s;
	}

}
