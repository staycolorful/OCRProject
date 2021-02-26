package utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Vector;

import javax.swing.JFileChooser;

import javax.swing.JTable;

import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.jacob.com.Dispatch;
import com.sun.corba.se.spi.ior.WriteContents;

import jxl.Workbook;
import jxl.format.UnderlineStyle;
import jxl.write.Colour;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class ExcelUtil {
	public void writeExcelByJXL(Vector document, AnalysWord ol)
			throws IOException {
		WritableWorkbook wwb;
		FileOutputStream fos;
		File fi = new File("D:\\testExcel\\");
		JFileChooser jChooser2;
		jChooser2 = new JFileChooser();
		jChooser2.setCurrentDirectory(fi);// ����Ĭ�ϴ�·��
		jChooser2.setDialogType(JFileChooser.SAVE_DIALOG);// ���ñ���Ի���
		int index = jChooser2.showDialog(null, "�����ļ�");
		if (index == JFileChooser.APPROVE_OPTION) {
			File f = jChooser2.getSelectedFile();
			String fileName = jChooser2.getName(f) + ".xls";
			String writePath = jChooser2.getCurrentDirectory() + "\\"
					+ fileName;
			try {
				fos = new FileOutputStream(writePath);
				wwb = Workbook.createWorkbook(fos);
				// /while��ʼ
				int maxx;
				int titleCount, columnCount;
				int rowNum = 0;
				columnCount = AnalysWord.max;
				Vector titles = (Vector) document.get(0);
				Vector paragraphs = (Vector) document.get(1);
				titleCount = titles.size();
				Integer ttemp[][] = ol.getStructure();
				while (0 < titleCount) {// ������table������
					// ���õ�Ԫ������ָ�ʽ
					WritableFont wf = new WritableFont(WritableFont.ARIAL, 12,
							WritableFont.NO_BOLD, false,
							UnderlineStyle.NO_UNDERLINE, Colour.BLUE);
					WritableCellFormat wcf = new WritableCellFormat(wf);
					if (500 < titleCount) {
						maxx = 500;
					} else {
						maxx = titleCount;
					}
					WritableSheet sheet = wwb.createSheet("test" + titleCount,
							0);

					if (rowNum != 0) {// �����Ϸ�ҳ
						rowNum = rowNum + 1;// ָ����һ��
					}
					int coun1 = -1, coun2 = 0;
					String title = "";
					String paragraph = "";
					for (int i = 0; i <maxx; i++) {
						for (int j = 0; j < columnCount; j++) {
							if (ttemp[i][j] != null) {
								coun1++;
								title = (String) titles.get(coun1);
								paragraph = (String) paragraphs.get(coun1);
								Label titleLabel = new Label(j, i, title, wcf);
								Label paragraphLabel = new Label(columnCount, i,paragraph,wcf);
								sheet.addCell(titleLabel);
								sheet.addCell(paragraphLabel);
							} else {
								Label label = new Label(j, i, "");
								sheet.addCell(label);
							}
							// ��excel�ĵ�i�е�j��д��table�ĵ�rowNum+i(rowNum��ʼֵΪ0)�е�j��ֵ

						}
					}
					rowNum = rowNum + maxx;
					titleCount -= maxx;
				}
				// while ����
				wwb.write();
				wwb.close();
				fos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	// class XlsFileFilter extends FileFilter {//��д�ļ�������
	//
	// @Override
	// public boolean accept(File f) {
	// String nameString = f.getName();
	// return nameString.toLowerCase().endsWith(".xls");
	// }
	// @Override
	// public String getDescription() {
	// return "*.xls(����ļ�)";
	// }
	// }
}
