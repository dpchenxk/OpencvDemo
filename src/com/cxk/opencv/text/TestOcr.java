package com.cxk.opencv.text;  
  
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.opencv.core.Core;

import com.cxk.opencv.util.ClearImageHelper;
import com.cxk.opencv.util.ImageIOHelper;
import com.cxk.opencv.util.OCR;
  
public class TestOcr {  
  
    public static void main(String[] args) throws IOException {  
        // TODO �Զ����ɵķ������  
        //����ͼƬ��ַ    
    	System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
        String path = "E:\\img";    
        File root = new File(path);
        
        File[] files = root.listFiles();  
        for (File file : files) {  
        	if(file.getName().endsWith("gif")) {
        		//����ת��Ŀ¼
        		File convertPath = new File(file.getParentFile()+"\\convert");
	       		 if(!convertPath.exists()) {
	       			 convertPath.mkdir();
	       		 }
           		 
//           		String fileName = file.getName().substring(0,file.getName().lastIndexOf("."));
//           		 
//           		BufferedImage src = ImageIO.read(file); 
//           		
//           		String tmpFilePath = file.getParentFile()+"\\convert\\" +fileName +"_tmp.png";
//           		
//           		ImageIO.write(src, "png", new File(tmpFilePath));
//           		
//           		//String convertFilePath = file.getParentFile()+"\\convert\\" +fileName;
//           		
//           		System.out.println(tmpFilePath);
//           		
//           		PictureManage pictureManage = new PictureManage(tmpFilePath); //��ͼƬ���д���  
//           		 
//           		String destPath = convertPath + "\\" + fileName+ ".png";
////           		 
//           		pictureManage.dealImage(destPath);
//           		
//				try {
//					String valCode = new OCR().recognizeText(new File(tmpFilePath), "png");// jpg��ͼƬ��ʽ
//					System.out.println("ͼƬ������Ϊ��" + "\n" + valCode);
//				} catch (IOException e) {
//					e.printStackTrace();
//				} catch (Exception e) {
//					e.printStackTrace();
//				}    
	       		 
	       		 
	       		 /****************************************/
	       		 
	       		if(!file.isDirectory()){
	       			String destDir = path+"/tmp"; 
	       			String convertFormat = "png";
	       			File cleanImage = ClearImageHelper.cleanImage(file, destDir,convertFormat);  
	       			
	       			String suffix = file.getName().substring(file.getName().lastIndexOf("."));
	       			
	       			File createImage = ImageIOHelper.createImage(cleanImage, convertFormat);
	        	}

			}
        }  
        
        
//        PictureManage pictureManage = new PictureManage(path); //��ͼƬ���д���  
//        pictureManage.imshow();  
//        try {       
//            String valCode = new OCR().recognizeText(new File("xintu.jpg"), "jpg");//jpg��ͼƬ��ʽ       
//            System.out.println("ͼƬ������Ϊ��"+"\n"+valCode);       
//        } catch (IOException e) {       
//            e.printStackTrace();       
//        } catch (Exception e) {    
//            e.printStackTrace();    
//        }        
    }    
  
}  