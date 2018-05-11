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
        // TODO 自动生成的方法存根  
        //输入图片地址    
    	System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
        String path = "E:\\img";    
        File root = new File(path);
        
        File[] files = root.listFiles();  
        for (File file : files) {  
        	if(file.getName().endsWith("gif")) {
        		//创建转换目录
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
//           		PictureManage pictureManage = new PictureManage(tmpFilePath); //对图片进行处理  
//           		 
//           		String destPath = convertPath + "\\" + fileName+ ".png";
////           		 
//           		pictureManage.dealImage(destPath);
//           		
//				try {
//					String valCode = new OCR().recognizeText(new File(tmpFilePath), "png");// jpg是图片格式
//					System.out.println("图片中文字为：" + "\n" + valCode);
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
        
        
//        PictureManage pictureManage = new PictureManage(path); //对图片进行处理  
//        pictureManage.imshow();  
//        try {       
//            String valCode = new OCR().recognizeText(new File("xintu.jpg"), "jpg");//jpg是图片格式       
//            System.out.println("图片中文字为："+"\n"+valCode);       
//        } catch (IOException e) {       
//            e.printStackTrace();       
//        } catch (Exception e) {    
//            e.printStackTrace();    
//        }        
    }    
  
}  