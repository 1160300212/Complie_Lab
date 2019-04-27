package Grammar_Analysis;

import java.util.ArrayList;

public class Production {
	public String prod = "";
	public String left = "";
	public ArrayList<String> right = new ArrayList<String>();

	public Production(String s) {
		this.prod = s;
		for (int i = 0; i < s.length(); i++) {
			String word = "";
			int j = i;
			if (s.charAt(i) == '<') {
				for (j = i + 1; j < s.length(); j++) {
					if (s.charAt(j) == '>' && j - i > 1) {
						break;
					}
					word += s.charAt(j);
				}
				if (left.equals("")) {
					left = word;
				} else {
					right.add(word);
				}
				i = j;
			}
		}
	}
}
