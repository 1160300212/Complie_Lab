package Grammar_Analysis;

import java.util.ArrayList;
import java.util.Stack;

public class G_Analyzer {

	ArrayList<Production> P = new ArrayList<Production>();
	ArrayList<String[]> Goto = new ArrayList<String[]>();
	ArrayList<String[]> Action = new ArrayList<String[]>();
	ArrayList<String> input = new ArrayList<String>();
	
	String reduceseq = "";
	
	public G_Analyzer(ArrayList<Production> P, ArrayList<String[]> Action, ArrayList<String[]> Goto, ArrayList<String> input) { //构造函数
		this.P = P;
		this.Goto = Goto;
		this.Action = Action;
		this.input = input;
		
		//analysis();
	}
	
	public void analysis() { //对输入序列进行语法分析（移入，规约）
		Stack<String> state_stk = new Stack<String>();
		state_stk.push("0");
		/*input.add("int");
		input.add("id");
		input.add(";");
		input.add("id");
		input.add("=");
		input.add("inum");
		input.add(";");*/
		input.add("#");
		int index = 0; //输入符号串下表
		System.out.println();
		while(true) {
			String op = "null";
			String s = "";
			for(int i = 0; i < Action.size(); i++) {
				if(Action.get(i)[0].equals(state_stk.peek()) && Action.get(i)[1].equals(input.get(index))) {
					op = "Action";
					s = Action.get(i)[2];
				}
			}
			if(op.equals("Action") && s.charAt(0) == 'S') {
				state_stk.push(s.substring(1));
				if(input.get(index).equals("id") || input.get(index).equals("inum") || input.get(index).equals("fnum")) index++;
				index++;
			}
			else if(op.equals("Action") && s.charAt(0) == 'R') {
				int tmp = Integer.parseInt(s.substring(1));
				for(int i = 0; i < P.get(tmp).right.size(); i++) {
					state_stk.pop();
				}
				for(int i = 0; i < Goto.size(); i++) {
					if(Goto.get(i)[0].equals(state_stk.peek()) && Goto.get(i)[1].equals(P.get(tmp).left)) {
						state_stk.push(Goto.get(i)[2]);
						break;
					}
				}
				System.out.println(P.get(tmp).prod);
				reduceseq += P.get(tmp).prod + "\n";
			}
			else if(op.equals("Action") && s.equals("acc")){
				System.out.println(P.get(0).prod);
				reduceseq += P.get(0).prod + "\n";
				break;
			}
		}
	}
	
	public String get_reduceseq(){ //返回规约的结果序列
		return reduceseq;
	}
	
}
