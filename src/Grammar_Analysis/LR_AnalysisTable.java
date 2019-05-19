package Grammar_Analysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/*LR(1)*/

public class LR_AnalysisTable {

	ArrayList<Production> P = new ArrayList<Production>();
	ArrayList<String> SN = new ArrayList<String>();
	ArrayList<String> ST = new ArrayList<String>();
	ArrayList<ArrayList<Item>> item_sets = new ArrayList<ArrayList<Item>>();
	ArrayList<String[]> Goto = new ArrayList<String[]>();
	ArrayList<String[]> Action = new ArrayList<String[]>();
	Map<String, ArrayList<String>> First = new HashMap<String, ArrayList<String>>();
	
	public LR_AnalysisTable(ArrayList<Production> production, ArrayList<String> symbolN, ArrayList<String> symbolT) { //构造函数
		this.P = production;
		this.SN = symbolN;
		this.ST = symbolT;
		
		First();
		
		Item_sets();
		/* 打印项目集
		for(int i = 0; i < item_sets.size() && i < 22; i++) {
			System.out.println("I" + i);
			for(int j = 0; j < item_sets.get(i).size(); j++) {
				System.out.println(item_sets.get(i).get(j).p.prod + " " + item_sets.get(i).get(j).point +" "+item_sets.get(i).get(j).lk_ahd);
			}
			System.out.println();
		}*/
		
		AnalysisTable();
		
	}
	
	public ArrayList<Item> CLOSURE(ArrayList<Item> item_set){ //计算给定项目集item_set的闭包
		while(true) {
			ArrayList<Item> t = item_set;
			for(int i = 0; i < item_set.size(); i++) { //item_set.get(i)  每个项
				for(int j = 0; j < P.size(); j++) { // P.get(j) 每个产生式
					if(P.get(j).left.equals(item_set.get(i).next_sym)) {
						String s; 
						//System.out.println(item_set.get(i).p.right.size() + " " + item_set.get(i).point);
						if(item_set.get(i).p.right.size() > item_set.get(i).point+1) { //看贝塔是否存在
							s = item_set.get(i).p.right.get(item_set.get(i).point+1); //找到贝塔
							ArrayList<String> f = First.get(s); //求贝塔的FIRST集
							for(int k = 0; k < f.size(); k++) { //遍历贝塔的FIRST集
								Item tmp = new Item(P.get(j), 0, f.get(k));
								boolean have = false;
								for(int ind = 0; ind < item_set.size(); ind++) {
									if(tmp.p.prod.equals(item_set.get(ind).p.prod) && tmp.lk_ahd.equals(item_set.get(ind).lk_ahd) && (tmp.point==item_set.get(ind).point)) {
										have = true;
									}
								}
								if(!have) {
									item_set.add(tmp);	
								}
							}
						}
						else { //贝塔不存在 为终结符
							s = item_set.get(i).lk_ahd;
							Item tmp = new Item(P.get(j), 0, s);
							boolean have = false;
							for(int ind = 0; ind < item_set.size(); ind++) {
								if(tmp.p.prod.equals(item_set.get(ind).p.prod) && tmp.lk_ahd.equals(item_set.get(ind).lk_ahd) && (tmp.point==item_set.get(ind).point)) {
									have = true;
								}
							}
							if(!have) {
								item_set.add(tmp);	
							}
						}
					}
				}
			}
			if(t.equals(item_set)) {
				break;
			}
		}
		return item_set;
	}
	
	public ArrayList<Item> GOTO(ArrayList<Item> item_set, String next) { //返回项目集item_set对应于文法符号next的后继项目集闭
		ArrayList<Item> t = new ArrayList<Item>();
		for(int i = 0; i < item_set.size(); i++) {
			if(item_set.get(i).next_sym.equals(next)) {
				t.add(new Item(item_set.get(i).p, item_set.get(i).point+1, item_set.get(i).lk_ahd));
			}
		}
		return CLOSURE(t);
	}
	
	public void Item_sets() { //求规范LR（0）项集族
		ArrayList<Item> i0set = new ArrayList<Item>();
		i0set.add(new Item(P.get(0), 0, "#"));
		i0set = CLOSURE(i0set);
		item_sets.add(i0set);
		while(true) {
			Boolean update = false;
			ArrayList<ArrayList<Item>> t = item_sets;
			for(int i = 0; i < t.size(); i++) {
				for(int j = 0; j < SN.size(); j++) {
					ArrayList<Item> tmp = GOTO(item_sets.get(i), SN.get(j));
					if((tmp.size() != 0) && (!item_sets.contains(tmp))) {
						item_sets.add(tmp);
						update = true;
					}
				}
				for(int j = 0; j < ST.size(); j++) {
					ArrayList<Item> tmp = GOTO(item_sets.get(i), ST.get(j));
					if((tmp.size() != 0) && (!item_sets.contains(tmp))) {
						item_sets.add(tmp);
						update = true;
					}
				}
			}
			if(!update) {
				break;
			}
		}
	}
	
	public void AnalysisTable() { //求Goto函数和Action函数
		for(int i = 0; i < item_sets.size(); i++) {
			for(int j = 0; j < item_sets.get(i).size(); j++) {
				if(SN.contains(item_sets.get(i).get(j).next_sym)) {
					ArrayList<Item> tmp = GOTO(item_sets.get(i) ,item_sets.get(i).get(j).next_sym);
					String[] go = {String.valueOf(i), item_sets.get(i).get(j).next_sym, String.valueOf(item_sets.indexOf(tmp))};
					Boolean add = true;
					for(int ind = 0; ind < Goto.size(); ind++) {
						if(Goto.get(ind)[0].equals(go[0]) && Goto.get(ind)[1].equals(go[1]) && Goto.get(ind)[2].equals(go[2])) {
							add = false;
						}
					}
					if(add) {
						Goto.add(go);
					}
				}
				if(ST.contains(item_sets.get(i).get(j).next_sym)) {
					ArrayList<Item> tmp = GOTO(item_sets.get(i) ,item_sets.get(i).get(j).next_sym);
					String[] act = {String.valueOf(i), item_sets.get(i).get(j).next_sym, "S"+String.valueOf(item_sets.indexOf(tmp))};
					Boolean add = true;
					for(int ind = 0; ind < Action.size(); ind++) {
						if(Action.get(ind)[0].equals(act[0]) && Action.get(ind)[1].equals(act[1]) && Action.get(ind)[2].equals(act[2])) {
							add = false;
						}
					}
					if(add) {
						Action.add(act);
					}
				}
				if(item_sets.get(i).get(j).next_sym.equals("")) {
					if(item_sets.get(i).get(j).p.left.equals("S")) {
						String[] act = {String.valueOf(i), "#", "acc"};
						Boolean add = true;
						for(int ind = 0; ind < Action.size(); ind++) {
							if(Action.get(ind)[0].equals(act[0]) && Action.get(ind)[1].equals(act[1]) && Action.get(ind)[2].equals(act[2])) {
								add = false;
							}
						}
						if(add) {
							Action.add(act);
						}
					}
					else {
						String[] act = {String.valueOf(i), item_sets.get(i).get(j).lk_ahd, "R"+String.valueOf(P.indexOf(item_sets.get(i).get(j).p))};
						Boolean add = true;
						for(int ind = 0; ind < Action.size(); ind++) {
							if(Action.get(ind)[0].equals(act[0]) && Action.get(ind)[1].equals(act[1]) && Action.get(ind)[2].equals(act[2])) {
								add = false;
							}
						}
						if(add) {
							Action.add(act);
						}
					}
	
				}
			}
		}
		/*打印Goto Action
		System.out.println("Goto :");
		for(int i = 0; i < Goto.size(); i++) {
			for(int j = 0; j < 3; j++) {
				System.out.print(Goto.get(i)[j] + " ");
			}
			System.out.println();
		}
		System.out.println();
		System.out.println("Action :");
		for(int i = 0; i < Action.size(); i++) {
			for(int j = 0; j < 3; j++) {
				System.out.print(Action.get(i)[j] + " ");
			}
			System.out.println();
		}*/
		
	}
	
	public void First(){ //求所有符号的FIRST集
		for(int i = 0; i < SN.size(); i++) {
			ArrayList<String> tmp = new ArrayList<String>();
			First.put(SN.get(i), tmp);
		}
		for(int i = 0; i < ST.size(); i++) {
			ArrayList<String> tmp = new ArrayList<String>();
			tmp.add(ST.get(i));
			First.put(ST.get(i), tmp);
		}
		while(true) {
			Boolean update = false;
			for(int i = 0; i < P.size(); i++) {
				String lft = P.get(i).left;
				String fst_rgt = P.get(i).right.get(0);
				ArrayList<String> tmp1 = new ArrayList<String>();
				ArrayList<String> tmp2 = new ArrayList<String>();
				tmp1 = First.get(lft);
				tmp2 = First.get(fst_rgt);
				for(int j = 0; j < tmp2.size(); j++) {
					if(!tmp1.contains(tmp2.get(j))) {
						tmp1.add(tmp2.get(j));
						update = true;
					}
				}
				First.put(lft, tmp1);
			}
			if(!update) {
				break;
			}
		}
		/* 打印FIRST
		for (Entry<String, ArrayList<String>> entry : First.entrySet()) {
			System.out.println(entry.getKey() + ": " + entry.getValue());
		}*/
	}
	
	public ArrayList<String[]> get_action(){ //返回Action
		return Action;
	}
	
	public ArrayList<String[]> get_goto(){ //返回Goto
		return Goto;
	}
	
}

class Item{
	Production p;
	int point = 0;
	String next_sym = "";
	String lk_ahd = "";
	public Item(Production p, int point, String lk_ahd) {
		this.p = p;
		this.point = point;
		this.lk_ahd = lk_ahd;
		if(p.right.size() > point) {
			this.next_sym = p.right.get(point);
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Item) 
			return (((Item)obj).p.equals(this.p))&&(((Item)obj).point==this.point)&&(((Item)obj).lk_ahd.equals(this.lk_ahd));
		return false;
	}
}
