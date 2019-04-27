package Lexical_Analysis;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Read_File {

	public ArrayList<StringBuffer> strbuf = new ArrayList<StringBuffer>();
	
	public Read_File(String filename){
		try {
			FileReader fr = new FileReader(filename);
			BufferedReader br = new BufferedReader(fr);
			String temp = null;
			StringBuffer tmpbuf;
			while ((temp = br.readLine()) != null) {
				tmpbuf = new StringBuffer();
				String[] s = temp.split("	");
				for(int i = 0; i < s.length; i++)
					tmpbuf.append(s[i]);
				strbuf.add(tmpbuf);
			}
		} catch (FileNotFoundException e) {
			System.out.println("源文件未找到！");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("读写文件异常！");
			e.printStackTrace();
		}
	}
	
	public ArrayList<StringBuffer> get_str() {
		return strbuf;
	}
}
