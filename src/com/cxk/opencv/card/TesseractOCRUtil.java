package com.cxk.opencv.card;
/**  
 * Created by zhangwenchao on 2017/9/28.  
 */  
  
  
import java.io.BufferedReader;  
  
import java.io.File;  
import java.io.FileInputStream;  
import java.io.InputStreamReader;  
import java.util.ArrayList;  
import java.util.List;  
  
public class TesseractOCRUtil{  
  
    private static final String LANG_OPTION = "-l";  
    private static final String EOL = System.getProperty("line.separator");  
  
    /**  
     * @param imageFile  
     *            �����ͼ���ļ�  
     * @return ʶ�����ַ���  
     */  
    public static String recognizeText(File imageFile) throws Exception {  
        /**  
         * ��������ļ��ı�����ļ�Ŀ¼  
         */  
        File outputFile = new File(imageFile.getParentFile(), "output");  
  
        StringBuffer strB = new StringBuffer();  
  
        Process  pro = Runtime.getRuntime().exec(  
                         new String[]{  
                            "D:/Program Files (x86)/Tesseract-OCR/tesseract.exe",  
                            imageFile.getPath(),  
                            outputFile.getPath()}  
                         );  
       int w = pro.waitFor();  
        if (w == 0) // 0���������˳�  
        {  
            BufferedReader in = new BufferedReader(new InputStreamReader(  
                    new FileInputStream(outputFile.getAbsolutePath() + ".txt"),  
                    "UTF-8"));  
            String str;  
  
            while ((str = in.readLine()) != null)  
            {  
                strB.append(str).append(EOL);  
            }  
            in.close();  
        } else  
        {  
            String msg;  
            switch (w)  
            {  
                case 1:  
                    msg = "Errors accessing files. There may be spaces in your image's filename.";  
                    break;  
                case 29:  
                    msg = "Cannot recognize the image or its selected region.";  
                    break;  
                case 31:  
                    msg = "Unsupported image format.";  
                    break;  
                default:  
                    msg = "Errors occurred.";  
            }  
            throw new RuntimeException(msg);  
        }  
        new File(outputFile.getAbsolutePath() + ".txt").delete();  
        return strB.toString().replaceAll("\\s*", "");  
  
  
  
    }  
  
  
    public static void main(String[] args) {  
  
  
        try {  
            String result =  recognizeText(new File("E:/resImage.jpg"));  
            System.out.println(result);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
  
  
    }  
}  