package com.cxk.opencv.text;  
  
import java.io.File;  
import java.io.IOException;  

import org.opencv.core.Core;
  
public class TestOcr {  
  
    public static void main(String[] args) {  
        // TODO �Զ����ɵķ������  
        //����ͼƬ��ַ    
    	System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
        String path = "E:\\img\\phone.jpg";    
        PictureManage pictureManage = new PictureManage(path); //��ͼƬ���д���  
        pictureManage.imshow();  
        try {       
            String valCode = new OCR().recognizeText(new File("xintu.jpg"), "jpg");//jpg��ͼƬ��ʽ       
            System.out.println("ͼƬ������Ϊ��"+"\n"+valCode);       
        } catch (IOException e) {       
            e.printStackTrace();       
        } catch (Exception e) {    
            e.printStackTrace();    
        }        
    }    
  
}  