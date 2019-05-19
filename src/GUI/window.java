package GUI;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import Grammar_Analysis.G_Analyzer;
import Grammar_Analysis.LR_AnalysisTable;
import Grammar_Analysis.Read_Grammar;
import Lexical_Analysis.L_Analyzer;
import Lexical_Analysis.Read_File;
import Lexical_Analysis.SymbolTable_record;
import Semantic_Analysis.S_Analyzer;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import javax.swing.JLabel;

public class window extends JFrame {

	private JPanel contentPane;
	L_Analyzer a = null;
	Read_Grammar rg = null;
	LR_AnalysisTable at = null;
	G_Analyzer ga = null;
	S_Analyzer sa = null;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window frame = new window();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public window() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1026, 722);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JTextArea textArea = new JTextArea();
		JScrollPane jslp = new JScrollPane(textArea);
		jslp.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		jslp.setBounds(50, 80, 200, 250);
		contentPane.add(jslp);
		
		JLabel lblInput = new JLabel("Input");
		lblInput.setBounds(50, 50, 80, 20);
		contentPane.add(lblInput);
		
		JTextArea textArea_1 = new JTextArea();
		JScrollPane jslp_1 = new JScrollPane(textArea_1);
		jslp_1.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		jslp_1.setBounds(300, 80, 200, 250);
		contentPane.add(jslp_1);
		
		JLabel lblToken = new JLabel("Token");
		lblToken.setBounds(300, 50, 80, 20);
		contentPane.add(lblToken);
		
		Object[] tableHead = {"Addr","Id", "Type", "Value"}; 
		Object[][] tableData = {};
		DefaultTableModel model = new DefaultTableModel(tableData,tableHead);
		JTable table = new JTable(model);
		JScrollPane jslp_2 = new JScrollPane(table);
		jslp_2.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		jslp_2.setBounds(550, 80, 200, 250);
		contentPane.add(jslp_2);
		
		JLabel lblSymbolmap = new JLabel("SymbolMap");
		lblSymbolmap.setBounds(550, 50, 80, 20);
		contentPane.add(lblSymbolmap);
		
		JLabel lblGrammar = new JLabel("Grammar");
		lblGrammar.setBounds(50, 350, 80, 20);
		contentPane.add(lblGrammar);
		
		JTextArea textArea_2 = new JTextArea();
		JScrollPane jslp_3 = new JScrollPane(textArea_2);
		jslp_3.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		jslp_3.setBounds(50, 380, 200, 250);
		contentPane.add(jslp_3);
		
		JLabel lblReduceSeq = new JLabel("Reduce Seq");
		lblReduceSeq.setBounds(300, 350, 100, 20);
		contentPane.add(lblReduceSeq);
		
		JTextArea textArea_3 = new JTextArea();
		JScrollPane jslp_4 = new JScrollPane(textArea_3);
		jslp_4.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		jslp_4.setBounds(300, 380, 200, 250);
		contentPane.add(jslp_4);
		
		JLabel lblFourTuple = new JLabel("Four Tuple");
		lblFourTuple.setBounds(550, 350, 100, 20);
		contentPane.add(lblFourTuple);
		
		JTextArea textArea_4 = new JTextArea();
		JScrollPane jslp_5 = new JScrollPane(textArea_4);
		jslp_5.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		jslp_5.setBounds(550, 380, 200, 250);
		contentPane.add(jslp_5);
		
		JButton btnNewButton = new JButton("Lexical");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Read_File f = new Read_File("src/test_S.txt");
				textArea.setText(f.get_str());
				a = new L_Analyzer(f.get_strbuf());
				a.analysis();
				textArea_1.setText(a.get_token());
				
				ArrayList<SymbolTable_record> symbolmap = a.get_symbolmap();
				Object[] tableHead = {"Addr","Id", "Type", "Value"}; 
				Object[][] tableData = new Object[symbolmap.size()][4];
				for(int i = 0; i < symbolmap.size(); i++) {
					tableData[i][0] = i;
					tableData[i][1] = symbolmap.get(i).id;
					tableData[i][2] = symbolmap.get(i).type;
					tableData[i][3] = symbolmap.get(i).value;
				}
				DefaultTableModel model = new DefaultTableModel(tableData,tableHead);
				table.setModel(model);
			}
		});
		btnNewButton.setBounds(828, 81, 113, 27);
		contentPane.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Grammar");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					rg = new Read_Grammar("src/Grammar.txt");
					at = new LR_AnalysisTable(rg.get_production(), rg.get_symbolN(), rg.get_symbolT());
					ga = new G_Analyzer(rg.get_production(), at.get_action(), at.get_goto(), a.grammar_input());
					ga.analysis();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				textArea_2.setText(rg.get_gra());
				textArea_3.setText(ga.get_reduceseq());
			} 
		});
		btnNewButton_1.setBounds(828, 157, 113, 27);
		contentPane.add(btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("Semantics");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					sa = new S_Analyzer(rg.get_production(), at.get_action(), at.get_goto(), a.grammar_input(), a.get_symbolmap());
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				textArea_4.setText(sa.get_fourtuple());
				
				ArrayList<SymbolTable_record> symbolmap = a.get_symbolmap();
				Object[] tableHead = {"Addr","Id", "Type", "Value"}; 
				Object[][] tableData = new Object[symbolmap.size()][4];
				for(int i = 0; i < symbolmap.size(); i++) {
					tableData[i][0] = i;
					tableData[i][1] = symbolmap.get(i).id;
					tableData[i][2] = symbolmap.get(i).type;
					tableData[i][3] = symbolmap.get(i).value;
				}
				DefaultTableModel model = new DefaultTableModel(tableData,tableHead);
				table.setModel(model);
			}
		});
		btnNewButton_2.setBounds(828, 239, 113, 27);
		contentPane.add(btnNewButton_2);
		
		
	}
}
