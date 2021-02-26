
import frame.OCRFrame;
import frame.OCRFrameThread;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.SecurityHandler;
import utils.PDFUtil;
import utils.Pageable;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        /*String fileName = "/Users/wangzhihan/Downloads/baiduyun/【咖啡教室】咖啡简史.pdf";  //这里先手动把绝对路径的文件夹给补上。
        String outName = "【咖啡教室】咖啡简史.txt";
        PDFUtil pdfUtil = new PDFUtil();
        pdfUtil.readPDF(fileName,outName);*/
        new OCRFrameThread().start();
        //创建PdfDocument实例
    }
}
