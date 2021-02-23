package utils;

import com.github.stuxuhai.jpinyin.ChineseHelper;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.io.RandomAccessRead;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.PageMode;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.text.PDFTextStripper;
import sun.plugin.dom.core.Document;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Iterator;

/**
 * 功能 PDF读写类
 * @CreateTime now
 */
public class PDFUtil {

    /**
     * 读PDF文件，使用了pdfbox开源项目
     * @param fileName
     */
    public static  PDDocument getPdfDocument(String fileName, String outName) {
        File file = new File(fileName);
        FileInputStream in = null;
        try {
            in = new FileInputStream(fileName);
            // 新建一个PDF解析器对象
            RandomAccessFile randomAccessRead = new RandomAccessFile(file,"rw");
            PDFParser parser = new PDFParser(randomAccessRead);
            // 对PDF文件进行解析
            parser.parse();
            // 获取解析后得到的PDF文档对象
            PDDocument pdfdocument = parser.getPDDocument();
            return pdfdocument;

        } catch (Exception e) {
            System.out.println("读取PDF文件" + file.getAbsolutePath() + "生失败！" + e);
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e1) {
                }
            }
        }
        return null;
    }

    public static String getPdfContent(PDDocument pdfdocument, Pageable pageable) {
        try {
            // 新建一个PDF文本剥离器
            PDFTextStripper stripper = new PDFTextStripper();
            if (pageable.isPaged()) {
                Integer pageNum = pageable.getPageNum();
                //获取PDF页面
                stripper.setStartPage(pageNum + 1);
                stripper.setEndPage(pageNum + 1);
            }

            stripper.setSortByPosition(true);
            String text = stripper.getText(pdfdocument);
            getImageByPdfPages(pdfdocument, pageable, ".");
            return convertToSimplifiedChinese(text);

        } catch (Exception e) {
            System.out.println("获取文件内容失败"  + e);
            e.printStackTrace();
        }
        return null;
    }

    public static String convertToSimplifiedChinese(String pinYinSt) {
        String tempStr = null;
        try {
            tempStr = ChineseHelper.convertToSimplifiedChinese(pinYinSt);
        } catch (Exception e) {
            tempStr = pinYinSt;
            e.printStackTrace();
        }

        return tempStr;
    }

    public static void getImageByPdfPages(PDDocument pdfdocument, Pageable pageable, String saveFilePath) {
        if (pageable.isPaged()) {
            PDPage page = pdfdocument.getPages().get(pageable.getPageNum());
            getImageByPdfPage(page, saveFilePath);
        } else {
            for (int i = 0;i< pdfdocument.getPages().getCount();i++) {
                PDPage page = pdfdocument.getPages().get(i);
                getImageByPdfPage(page, saveFilePath);
            }
        }
    }

    public static void getImageByPdfPage(PDPage page, String saveFilePath) {
        PDResources resources = page.getResources();
        Iterable<COSName> images = resources.getXObjectNames();

        if(images != null)
        {
            Iterator<COSName> cosNameIterator = images.iterator();
            int count = 0 ;
            while(cosNameIterator.hasNext())
            {
                COSName cosName = cosNameIterator.next();
                FileOutputStream out = null;
                if (resources.isImageXObject(cosName)) {
                    try {
                        PDImageXObject imageXObject = (PDImageXObject)resources.getXObject(cosName);
                        BufferedImage image = imageXObject.getImage();
                        out = new FileOutputStream(saveFilePath + count +".png");
                        count ++;
                        ImageIO.write(image, "png", out);
                    } catch (Exception e) {
                        System.out.println("文件获取图片流错误 " + e);
                    } finally {
                        try {
                            out.close();
                        } catch (IOException e) {
                            System.out.println(e);
                        }
                    }

                }
            }
        }

    }
}