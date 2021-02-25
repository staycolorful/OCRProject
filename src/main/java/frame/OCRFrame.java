package frame;

import javax.swing.*;
import java.awt.*;

public class OCRFrame extends JFrame {
    JLabel label = new JLabel();
    TextField textField = new TextField(60);
    JPanel toolPanel = new JPanel();
    JScrollPane leftScrollPane = new JScrollPane();
    JScrollPane rightScrollPane = new JScrollPane();
    JTextArea leftTextArea = new JTextArea(20,20);
    JTextArea rightTextArea = new JTextArea(20,20);
    JPanel bottomPanel = new JPanel();

    int count = 0;
    JLabel label1 = new JLabel("第" + count + "页");
    JButton button1 = new JButton("下一页");
    JLabel label2 = new JLabel("图片名称：");
    JTextField  textField2= new JTextField(30);
    JButton button2 = new JButton("保存");



    public OCRFrame(){
        initGUI();
    }
    private void initGUI(){
        setVisible(true);
        setSize(1200,700);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        add(getToolPanel());
        add(getLeftPanel());
        add(getRightPanel());
        add(getBottomPanel());



       /* btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("提交: " + textArea.getText());
            }
        });*/
    }

    private JPanel getToolPanel(){
        toolPanel.setLayout(new FlowLayout());
        label.setText("请上传文件");
        label.setBounds(100,0,20,30);
        //toolPanel.add(label);
        textField.setText("sdfsfsfsdf");
        toolPanel.add(textField);
        JButton btn = new JButton("上传");
        toolPanel.add(btn);
        toolPanel.setBounds(0,0,1200, 40);
        toolPanel.setBackground(Color.GRAY);
        return toolPanel;
    }

    private JComponent getLeftPanel(){
        leftTextArea.setLineWrap(true);
        //leftTextArea.setPreferredSize(new Dimension(600,700));
        leftTextArea.setText("sdfsdfsdf");
        leftTextArea.setEditable(true);
        leftTextArea.setLocation(0,40);
        leftScrollPane.setBounds(0,40,600,60);
        leftScrollPane.setViewportView(leftTextArea);
        return leftScrollPane;
    }


    private JComponent getRightPanel(){
        rightTextArea.setLineWrap(true);
        //rightTextArea.setPreferredSize(new Dimension(600,700));
        rightTextArea.setText("jajaj");
        rightTextArea.setEditable(true);
        rightTextArea.setLocation(600,40);
        rightScrollPane.setBounds(600,40,600,600);
        rightScrollPane.setViewportView(rightTextArea);
        return rightScrollPane;
    }

    private JPanel getBottomPanel(){
        //label1.setBounds(10,640,20,40);
        //label1.setText("sdfsfsf");
        //bottomPanel.add(label1);
        //button1.setBounds(50,640,20,40);
        //add(button1);
        //add(label2);
        //add(textField2);
        //add(button2);
        bottomPanel.setSize(1200,40);
        bottomPanel.setBackground(Color.GREEN);
        bottomPanel.setBounds(0,60,60,20);
        return bottomPanel;
    }
}
