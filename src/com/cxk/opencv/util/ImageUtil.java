package com.cxk.opencv.util;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.ImageProducer;  
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;

import com.sun.jimi.core.Jimi;  
import com.sun.jimi.core.JimiException;  
import com.sun.jimi.core.JimiWriter;  
import com.sun.jimi.core.options.JPGOptions;

import javafx.scene.shape.Line;  

public class ImageUtil {  
  
    public static void toJPG(String source, String dest, int quality) {  
  
        if (dest == null || dest.trim().equals(""))  
            dest = source;  
  
        if (!dest.toLowerCase().trim().endsWith("jpg")) {  
            dest += ".jpg";  
            System.out.println("Overriding to JPG, output file: " + dest);  
        }  
          
          
        if (quality < 0 || quality > 100 || (quality + "") == null || (quality + "").equals("")) {  
            quality = 75;  
        }  
          
        try {  
            JPGOptions options = new JPGOptions();  
            options.setQuality(quality);  
            ImageProducer image = Jimi.getImageProducer(source);  
            JimiWriter writer = Jimi.createJimiWriter(dest);  
            writer.setSource(image);  
            writer.setOptions(options);  
            writer.putImage(dest);  
        } catch (JimiException je) {  
            System.err.println("Error: " + je);  
        }  
    }
    
    /**
     * 图片剪裁
     * @param x 距离左上角的x轴距离
     * @param y 距离左上角的y轴距离
     * @param width 宽度
     * @param height 高度
     * @param sourcePath 图片源
     * @param descpath 目标位置
     */
    public static void imageCut(int x, int y, int width, int height, String sourcePath, String descpath) {  
        FileInputStream is = null;  
        ImageInputStream iis = null;  
        try {  
            is = new FileInputStream(sourcePath);  
            String fileSuffix = sourcePath.substring(sourcePath.lastIndexOf(".") + 1);  
            Iterator<ImageReader> it = ImageIO.getImageReadersByFormatName(fileSuffix);  
            ImageReader reader = it.next();  
            iis = ImageIO.createImageInputStream(is);  
            reader.setInput(iis, true);  
            ImageReadParam param = reader.getDefaultReadParam();  
            Rectangle rect = new Rectangle(x, y, width, height);  
            param.setSourceRegion(rect);  
            BufferedImage bi = reader.read(0, param);  
            ImageIO.write(bi, fileSuffix, new File(descpath));  
        } catch (Exception ex) {  
            ex.printStackTrace();  
        } finally {  
            if (is != null) {  
                try {  
                    is.close();  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
                is = null;  
            }  
            if (iis != null) {  
                try {  
                    iis.close();  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
                iis = null;  
            }  
        }  
    }
    
    /**
     * 把二值图片纵向切图，保留包含内容的部分
     * @param sourceMat  
     * @return
     */
    public static List<Mat> cutImage(Mat sourceMat) {
    	List<Mat> list = new ArrayList<Mat>();
		int startCol = 0 ; 
        int endCol = 0;
        boolean whiteCol = true;;
        boolean startFlag = true;
        int preAvlCount = 0;
        for(int x = 0; x < sourceMat.width(); x++) { //列  
        	
        	whiteCol = true;
        	
        	 for (int y = 0; y < sourceMat.height(); y++) { //行
        		 
        		 
        		 byte[] data = new byte[1];  
        		 sourceMat.get(y, x, data);  

                 if (data[0] == 0)  {
                	 preAvlCount = preAvlCount + 1;
                	 if ( startFlag == true)  {
                    	 startCol = x;
                    	 startFlag = false;
                     }else if( startFlag == false) {
                    	 endCol = x;
                     }
                	 whiteCol = false;
                	 break;
                 }
                 
        	 }
        	 
        	 if(whiteCol == true && startFlag == false) {
        		 
        		 	System.out.println("截图左边界"+Integer.toString(startCol));  
        	        System.out.println("截图右边界"+Integer.toString(endCol));  
        	  
        	  
        	        //参数,坐标X,坐标Y,截图宽度,截图长度  
        	        Rect rect = new Rect(startCol,0,endCol - startCol+1,sourceMat.height());  
        	        Mat resMat = new Mat(sourceMat,rect); 
        	        BufferedImage image =  ImageUtil.toBufferedImage(resMat);  
        	        ImageUtil.saveJpgImage(image,"E:/"+startCol+".jpg"); 
        	        list.add(resMat);
        		 startFlag = true;
        		 
        	 }
        	 
        }
        
        return list;
	}  
    
    public static List<Line>  findHorizontalLine(Mat sourceMat) {
    	 
    	 List<Line> list = new ArrayList<>();
    	 int x =0 ; int y =0;
    	 for (y = 0; y < sourceMat.height(); y++)//行  
         {   
    		 Point startPoint  = new Point(0,0);
    	 	 Point preAvlPoint  = new Point(0,0);
    		 boolean init = false;

    		 for (x = 0; x < sourceMat.width(); x++) //列   inner for 
             {  
                 //得到该行像素点的值  
                 byte[] data = new byte[1];  
                 sourceMat.get(y, x, data);  
                 
                 //判断当前像素和前面的像素构成连续的线段
                 if(init == true) {  // outer if
	                 if (data[0] != 0 )  { // inner if
	                	 
	                	 if(x - startPoint.x <= 1) {
	                		 //System.out.println("当前节点空白，之前节点为一个像素");
	                		 init = false;
	                	 }else {
	                		 Line line = new Line(startPoint.x, startPoint.y, x, y);
	                		 list.add(line);
	                	 }
	                 }else {
	                	// System.out.println("Current X is :" + x + ", and line is Continuity!");
	                	 continue;
	                 }  // end inner if
                 } // end outer if
                 
                 if (data[0] == 0 && init == false)  {
                	 startPoint.set(new double[] {x,y});
                	 preAvlPoint.set(new double[] {x,y});
                	 init = true;
                 }
             }  // end inner for  
             
             //判断线段是否有效，并且长度大于一个像素
             if(init == true && (x -1 - startPoint.x) > 1) {
            	 Line line = new Line(startPoint.x, startPoint.y, x -1, y);
            	 list.add(line);
             }
         }
    	 return list;
    }
    
    /**  
     * 将Mat图像格式转化为 BufferedImage  
     * @param matrix  mat数据图像  
     * @return BufferedImage  
     */  
    public static BufferedImage toBufferedImage(Mat matrix) {  
        int type = BufferedImage.TYPE_BYTE_GRAY;  
        if (matrix.channels() > 1) {  
            type = BufferedImage.TYPE_3BYTE_BGR;  
        }  
        int bufferSize = matrix.channels() * matrix.cols() * matrix.rows();  
        byte[] buffer = new byte[bufferSize];  
        matrix.get(0, 0, buffer); // 获取所有的像素点  
        BufferedImage image = new BufferedImage(matrix.cols(), matrix.rows(), type);  
        final byte[] targetPixels = ((DataBufferByte)image.getRaster().getDataBuffer()).getData();  
        System.arraycopy(buffer, 0, targetPixels, 0, buffer.length);  
        return image;  
    }  
    
    /**  
     * 将BufferedImage内存图像保存为图像文件  
     * @param image BufferedImage  
     * @param filePath  文件名  
     */  
    public static void saveJpgImage(BufferedImage image, String filePath) {  
  
        try {  
            ImageIO.write(image, "jpg", new File(filePath));  
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
    }  
    
    public static void main(String [] args){
    	
    	String source = "E:\\img\\1.gif";
    	String dest = "E:\\img\\1.jpg";
    	System.out.println("=============begin===========");
    	ImageUtil.toJPG(source, dest, 90);
    	System.out.println("=============end===========");
    	
    }
}