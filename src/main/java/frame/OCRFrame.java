package frame;

import javax.swing.*;
import java.awt.*;

public class OCRFrame extends JFrame {
    JPanel panel = null;

    public OCRFrame(){
        initGUI();
    }
    private void initGUI(){
        setVisible(true);
        setSize(1200,700);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        add(getToolPanel(),BorderLayout.NORTH);
        add(getTextPanel(),BorderLayout.CENTER);
    }

    private JPanel getToolPanel(){
        JPanel toolPanel = new JPanel();
        JButton btn = new JButton("提交");
        toolPanel.add(btn);
        toolPanel.setSize(1200, 200);
        toolPanel.setBackground(Color.GRAY);
        return toolPanel;
    }
    private JPanel getTextPanel(){
        JPanel textPanel = new JPanel();
        textPanel.setSize(1200,500);
        textPanel.setBackground(Color.YELLOW);
        // 创建一个 5 行 10 列的文本区域
        final JTextArea sTextArea = new JTextArea(5, 10);
        // 设置自动换行
        sTextArea.setLineWrap(true);
        // 添加到内容面板
        textPanel.add(sTextArea);
        final JTextArea tTextArea = new JTextArea(5, 10);
        // 设置自动换行
        tTextArea.setLineWrap(true);
        // 添加到内容面板
        textPanel.add(tTextArea);
        return textPanel;
    }
}
