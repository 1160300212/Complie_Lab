package Lexical_Analysis;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Read_File {

	public StringBuffer strbuf = new StringBuffer();
	
	public Read_File(String filename){
		try {
			FileReader fr = new FileReader(filename);
			BufferedReader br = new BufferedReader(fr);
			String temp = null;
			while ((temp = br.readLine()) != null) {
				String[] s = temp.split("	");
				for(int i = 0; i < s.length; i++)
					strbuf.append(s[i]);
			}
		} catch (FileNotFoundException e) {
			System.out.println("源文件未找到！");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("读写文件异常！");
			e.printStackTrace();
		}
	}
}
