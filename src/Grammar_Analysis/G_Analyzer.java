package Grammar_Analysis;

import java.util.ArrayList;
import java.util.Stack;

public class G_Analyzer {

	ArrayList<Production> P = new ArrayList<Production>();
	ArrayList<String[]> Goto = new ArrayList<String[]>();
	ArrayList<String[]> Action = new ArrayList<String[]>();
	ArrayList<String> input = new ArrayList<String>();
	
	public G_Analyzer(ArrayList<Production> P, ArrayList<String[]> Action, ArrayList<String[]> Goto, ArrayList<String> input) {
		this.P = P;
		this.Goto = Goto;
		this.Action = Action;
		this.input = input;
		
		//analysis();
	}
	
	public void analysis() {
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
		int index = 0; //ÊäÈë·ûºÅ´®ÏÂ±í
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
			}
			else if(op.equals("Action") && s.equals("acc")){
				System.out.println(P.get(0).prod);
				break;
			}
		}
	}
	
}
