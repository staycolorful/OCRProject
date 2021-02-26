package utils;

import java.util.Vector;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

public class AnalysWord {
	Dispatch selection = null;
	Dispatch hyperLinks = null;// ���г����ӣ�Ŀ¼�������ӣ���ΪĿ¼Ҳ�ǳ����ӣ�
	Dispatch wordFile = null;// һ��word�ĵ�
	ActiveXComponent word = null;// word���
	protected static Integer max = 0;//Ŀ¼�����ȼ�
	protected Vector<Integer> headList = new Vector();//��������Ŀ¼�ĵȼ�
	public void CloseWord() {
		Dispatch.call(wordFile, "Close", new Variant(false));// �Ƿ񱣴��޸�
		Dispatch.call(word, "Quit");
	}
	/**
	 * �õ�Ŀ¼��ͨ��Ŀ¼�ҳ���Ӧ�ı��⣬
	 * ͨ�����������λ�õõ�֮��Ķ���
	 * ���õ��˶���Range��������ֻ������text,û�д���table��picture�����Դ�Range��ȡ����
	 * @param filePath
	 * @return
	 */
	public Vector getDocumet(String filePath) {// ͨ��Ŀ¼��ȡ��Ӧ�Ķ���
		word = new ActiveXComponent("Word.Application");
		word.setProperty("Visible", new Variant(false));
		Dispatch documents = word.getProperty("Documents").toDispatch();
		// �򿪵�word�ı�
		wordFile = Dispatch
				.invoke(
						documents,
						"Open",
						Dispatch.Method,
						new Object[] { filePath, new Variant(false),
								new Variant(false) }, new int[1]).toDispatch();
		Dispatch tablesOfContents = Dispatch.call(wordFile, "TablesOfContents")
				.toDispatch();// ����Ŀ¼����
		
		// ����Ŀ¼
		Dispatch tableOfContents = Dispatch.call(tablesOfContents, "Item",
				new Variant(1)).toDispatch();
	
		// �õ�����Ŀ¼�ķ�Χ
		Dispatch tableOfContentsRange = Dispatch.get(tableOfContents, "Range")
				.toDispatch();
		// �õ�ѡ�������еĳ�����
		hyperLinks = Dispatch.get(tableOfContentsRange, "HyperLinks")
				.toDispatch();
		// �õ������ӵĸ���
		int hyperLinksCount = Dispatch.get(hyperLinks, "Count").getInt();
		if(hyperLinksCount==0){
			return null;
		}
		int startOne = 0, endOne = 0, startTwo = 0, endTwo = 0;
		Dispatch headingRange = null;
		Dispatch selectRange = null;// �����¶��䷶Χ
		Vector<String> headings = new Vector<String>();
		Vector<String> paras = new Vector<String>();
		Integer one = 0;
		Dispatch paragraphs ;
		// -----ÿһ�����ݲ�������
		for (int k = 1, j = 0; k < hyperLinksCount; k++) {
			startOne = getParaStart(k);
			endOne = getParaEnd(k);
			j = k + 1;
			startTwo = getParaStart(j);
			endTwo = getParaEnd(j);
			// ���ⷶΧ
			headingRange = Dispatch.call(wordFile, "Range",
					new Variant(startOne), new Variant(endOne)).toDispatch();
			paragraphs = Dispatch.get(headingRange, "Paragraphs")
			.toDispatch();
			one = Dispatch.get(paragraphs, "OutlineLevel").getInt();
			if (one >= max) {
				max = one;
			}
			headList.add(one);
			// �������
			headings.add(Dispatch.get(headingRange, "Text").toString());
			// �����¶��䷶Χ���ñ������λ������һ���������ʼλ�ã�
			selectRange = Dispatch.call(wordFile, "Range", new Variant(endOne),
					new Variant(startTwo)).toDispatch();
			// ������䣨���֣�û�д���table��picture
			paras.add(Dispatch.get(selectRange, "Text").toString());
		}

		// ------------���һ�����ݲ�������---------//
		// ���һ������
		headingRange = Dispatch.call(wordFile, "Range", new Variant(startTwo),
				new Variant(endTwo)).toDispatch();
		paragraphs = Dispatch.get(headingRange, "Paragraphs")
		.toDispatch();
		one = Dispatch.get(paragraphs, "OutlineLevel").getInt();
		if (one >= max) {
			max = one;
		}
		headList.add(one);
		// �������һ������
		headings.add(Dispatch.get(headingRange, "Text").toString());
		// �ĵ����һ���ֵ�λ��
		Dispatch.call(selection, "EndKey", "6");
		selection = Dispatch.get(word, "Selection").toDispatch();
		int endd = Dispatch.get(selection, "End").getInt();
		// ���һ��
		Dispatch lastPara = Dispatch.call(wordFile, "Range",
				new Variant(endTwo), new Variant(endd)).toDispatch();
		// �������һ������
		paras.add(Dispatch.get(lastPara, "Text").toString());
		Vector<Vector<String>> documentVector = new Vector<Vector<String>>();
		documentVector.add(headings);
		documentVector.add(paras);
		CloseWord();
		return documentVector;
	}
	/**
	 * �õ���Ŀ¼��Ӧ�������ʼλ��
	 * @param index
	 * @return
	 */
	public int getParaStart(int index) {
		int start = 0;
		//���ÿһ������
		Dispatch hyperLink = Dispatch.call(hyperLinks, "Item",
				new Variant(index)).toDispatch();
		//����ƶ���������ָ�����ĵط�
		Dispatch.call(hyperLink, "Follow");
		try {
			//��ʱ���õ�ǰ���λ��
			selection = Dispatch.get(word, "Selection").toDispatch();
			//��λ�õ�����㣨selection������һ��Ҳ������һ���㣩
			start = Dispatch.get(selection, "Start").getInt();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return start;
	}
	/**
	 * �õ���Ŀ¼��Ӧ�����ĩβλ��
	 * @param index
	 * @return
	 */
	public int getParaEnd(int index) {
		int end = 0;
		Dispatch hyperLink = Dispatch.call(hyperLinks, "Item",
				new Variant(index)).toDispatch();
		Dispatch.call(hyperLink, "Follow");
		try {
			selection = Dispatch.get(word, "Selection").toDispatch();
			end = Dispatch.get(selection, "End").getInt();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return end;
	}
	/**
	 * ͨ��headList������Ŀ¼�ṹ��������������
	 * @return
	 */
	public Integer[][] getStructure() {
		Integer one, two, numone = 0;
		int i, j, k = 0, l = 0;
		// int hyperLinksCount = Dispatch.get(hyperLinks, "Count").getInt();
		Integer[][] title = new Integer[headList.size()][max+1];
		// ֱ�ӱ����һ������
		numone = (Integer) headList.get(0);
		title[k][l] = numone;
		for (i = 0; i < headList.size()-1; i++) {// i=18..j=19..l=20
			one = (Integer) headList.get(i);
			j = i + 1;
			two = (Integer) headList.get(j);
			if (one < two) {// ������������ӽڵ�
				++k;
				l=two-1;
				title[k][l] = two;
			}
			if (one == two) {// ������ֵܽڵ�
				k++;
				title[k][l] = two;
			}
			if (one > two) {// two��one���ڵ���ֵܽڵ�
				k++;
				if(isRoot(two)){
					l=0;
				}else{
					l=two-1;//outlineLevel��Ӧ�е�λ��,excel��0��ʼ��outlineLevel��1��ʼ
				}
				title[k][l] = two;
			}
		}
		return title;
	}
	/**
	 * ͨ����ٵȼ��ж��Ƿ��Ǹ��ڵ�
	 * @param outline
	 * @return
	 */
	public boolean isRoot(int outline) {
		boolean flag = false;
		if (outline == 1) {
			flag = true;
		}
		return flag;
	}
}