package utils;

import java.io.File;
import java.util.Scanner;
import java.util.Vector;

import javax.swing.JFileChooser;

public class WordToExcel {
	public String getSourceFile() {
		File fi = new File("E:\\");
		JFileChooser jChooser2;
		jChooser2 = new JFileChooser();
		jChooser2.setCurrentDirectory(fi);// ����Ĭ�ϴ�·��
		jChooser2.setDialogType(JFileChooser.OPEN_DIALOG);// ���ñ���Ի���
		int index = jChooser2.showDialog(null, "���ļ�");
		if (index == JFileChooser.APPROVE_OPTION) {
			File f = jChooser2.getSelectedFile();
			String fileName = jChooser2.getName(f);
			String writePath = jChooser2.getCurrentDirectory() + "\\"
					+ fileName;
			return writePath;
		}
		return null;
	}
	public static void main(String[] args) throws Exception {
		WordToExcel wth = new WordToExcel();
		String wordPath = wth.getSourceFile();
		if(wordPath==null){
			System.out.println("��û��ѡ���ļ�����������");
		}else if(".doc".equals(wordPath.trim().substring(wordPath.length()-4, wordPath.length()))){
			AnalysWord ol = new AnalysWord();
			Vector vec = ol.getDocumet(wordPath);
			if(vec!=null){
				ExcelUtil excell = new ExcelUtil();
				excell.writeExcelByJXL(vec,ol);
			}else{
				System.out.println("���ܴ���������û��Ŀ¼�ĵ�word�ĵ���������������");
			}
		}else{
			System.out.println("���ܴ����doc��ʽ��word�ĵ���������������������docxҲ����");
		}
		
	}
}
