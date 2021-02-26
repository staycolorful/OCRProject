package frame;

import org.apache.pdfbox.pdmodel.PDDocument;
import utils.PDFUtil;
import utils.Pageable;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class OCRFrame extends JFrame {
    JButton topButton = new JButton("上传");
    JPanel toolPanel = new JPanel();
    JScrollPane leftScrollPane = new JScrollPane();
    JScrollPane rightScrollPane = new JScrollPane();
    JTextArea leftTextArea = new JTextArea(20,20);
    JTextArea rightTextArea = new JTextArea(20,20);

    int count = 0;
    JTextField textField1 = new JTextField();
    JButton button1 = new JButton("下一页");
    JButton button2 = new JButton("抽取图片");
    SpinnerModel model1 = new SpinnerNumberModel();
    JSpinner spinner1 = new JSpinner(model1);

    JLabel label2 = new JLabel("分类");
    JTextField textField2= new JTextField();
    JLabel label3 = new JLabel("关键字");
    JTextField textField3 = new JTextField();
    JButton button3 = new JButton("保存段落");

    String fileName = "";
    File file = null;
    JFileChooser chooser = new JFileChooser();

    PDFUtil pdfUtil = new PDFUtil();




    public OCRFrame(){
        initGUI();
    }
    private void initGUI(){


        Dimension spinner1Ds = new Dimension(70, 40);
        spinner1.setPreferredSize(spinner1Ds);
        spinner1.setBounds(30,640,70,40);


        Dimension button1Ds = new Dimension(100,40);
        button1.setPreferredSize(button1Ds);
        button1.setBounds(120,640,100,40);


        Dimension textField1Ds = new Dimension(100, 40);
        textField1.setPreferredSize(textField1Ds);
        textField1.setBounds(240,640,200,40);


        Dimension button2Ds = new Dimension(100,40);
        button2.setPreferredSize(button2Ds);
        button2.setBounds(460,640,100,40);


        Dimension label2Ds = new Dimension(40, 40);
        label2.setPreferredSize(label2Ds);
        label2.setBounds(640,640,40,40);


        Dimension textField2Ds = new Dimension(160, 40);
        textField2.setPreferredSize(textField2Ds);
        textField2.setBounds(680,640,160,40);


        Dimension label3Ds = new Dimension(50, 40);
        label3.setPreferredSize(label3Ds);
        label3.setBounds(870,640,50,40);


        Dimension textField3Ds = new Dimension(160, 40);
        textField3.setPreferredSize(textField3Ds);
        textField3.setBounds(930,640,160,40);


        Dimension button3Ds = new Dimension(80,40);
        button3.setPreferredSize(button3Ds);
        button3.setBounds(1100,640,80,40);


        add(getToolPanel());
        add(getLeftPanel());
        add(getRightPanel());
        add(spinner1);
        add(button1);
        add(textField1);
        add(button2);
        add(label2);
        add(textField2);
        add(label3);
        add(textField3);
        add(button3);

        setSize(1200,710);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLayout(null);
        setVisible(true);

        topButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                count = 0;
                spinner1.getModel().setValue(count);
                chooser.setMultiSelectionEnabled(true);
                /** 过滤文件类型 * */
                FileNameExtensionFilter filter = new FileNameExtensionFilter("jpg","png");
                chooser.setFileFilter(filter);
                int returnVal = chooser.showOpenDialog(new JButton());
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File[] arrfiles = chooser.getSelectedFiles();
                    if (arrfiles == null || arrfiles.length == 0) {
                        return;
                    }
                    //判断是否有文件为jpg或者png
                    file= chooser.getSelectedFile();
                    //创建一个fileName得到选择文件的名字
                    fileName = file.getName();
                }
                //todo
                getText();

            }
        });



        spinner1.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSpinner spinner = (JSpinner)e.getSource();
                count = (int)spinner.getModel().getValue();
                // todo
                getText();
            }
        });
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int num = (int)spinner1.getModel().getValue();
                count = ++ num;
                spinner1.getModel().setValue(count);
                // todo
                getText();
            }
        });

        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        button3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }

    private void getText() {
        Pageable pageable = new Pageable();
        pageable.setPageNum(count);
        pageable.setIsPaged(true);
        PDDocument pdDocument = PDFUtil.getPdfDocument(file.getAbsolutePath());
        String s = PDFUtil.getPdfContent(pdDocument,pageable);
        leftTextArea.setText(s);
    }

    private JPanel getToolPanel(){
        toolPanel.setLayout(null);
        chooser.setBounds(200,0,200,30);
        toolPanel.add(chooser);
        topButton.setBounds(400,5,60,30);
        toolPanel.add(topButton);
        Dimension dimension = new Dimension(1200, 40);
        toolPanel.setPreferredSize(dimension);
        toolPanel.setBounds(0,0,1200, 40);
        toolPanel.setBackground(Color.GRAY);
        return toolPanel;
    }

    private JComponent getLeftPanel(){
        leftTextArea.setLineWrap(true);
        leftTextArea.setText("");
        leftTextArea.setEditable(true);
        Dimension dimension = new Dimension(599, 590);
        leftScrollPane.setPreferredSize(dimension);
        leftScrollPane.setBounds(0,40,599,590);
        leftScrollPane.setViewportView(leftTextArea);
        return leftScrollPane;
    }


    private JComponent getRightPanel(){
        rightTextArea.setLineWrap(true);
        rightTextArea.setText("");
        rightTextArea.setEditable(true);
        Dimension dimension = new Dimension(599, 590);
        rightScrollPane.setPreferredSize(dimension);
        rightScrollPane.setBounds(600,40,599,590);
        rightScrollPane.setViewportView(rightTextArea);
        return rightScrollPane;
    }
}
