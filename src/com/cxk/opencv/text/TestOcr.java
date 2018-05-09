package com.cxk.opencv.text;  
  
import java.io.File;  
import java.io.IOException;  

import org.opencv.core.Core;
  
public class TestOcr {  
  
    public static void main(String[] args) {  
        // TODO 自动生成的方法存根  
        //输入图片地址    
    	System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
        String path = "E:\\img\\phone.jpg";    
        PictureManage pictureManage = new PictureManage(path); //对图片进行处理  
        pictureManage.imshow();  
        try {       
            String valCode = new OCR().recognizeText(new File("xintu.jpg"), "jpg");//jpg是图片格式       
            System.out.println("图片中文字为："+"\n"+valCode);       
        } catch (IOException e) {       
            e.printStackTrace();       
        } catch (Exception e) {    
            e.printStackTrace();    
        }        
    }    
  
}  