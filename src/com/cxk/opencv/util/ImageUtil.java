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
     * ͼƬ����
     * @param x �������Ͻǵ�x�����
     * @param y �������Ͻǵ�y�����
     * @param width ���
     * @param height �߶�
     * @param sourcePath ͼƬԴ
     * @param descpath Ŀ��λ��
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
     * �Ѷ�ֵͼƬ������ͼ�������������ݵĲ���
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
        for(int x = 0; x < sourceMat.width(); x++) { //��  
        	
        	whiteCol = true;
        	
        	 for (int y = 0; y < sourceMat.height(); y++) { //��
        		 
        		 
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
        		 
        		 	System.out.println("��ͼ��߽�"+Integer.toString(startCol));  
        	        System.out.println("��ͼ�ұ߽�"+Integer.toString(endCol));  
        	  
        	  
        	        //����,����X,����Y,��ͼ���,��ͼ����  
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
    	 for (y = 0; y < sourceMat.height(); y++)//��  
         {   
    		 Point startPoint  = new Point(0,0);
    	 	 Point preAvlPoint  = new Point(0,0);
    		 boolean init = false;

    		 for (x = 0; x < sourceMat.width(); x++) //��   inner for 
             {  
                 //�õ��������ص��ֵ  
                 byte[] data = new byte[1];  
                 sourceMat.get(y, x, data);  
                 
                 //�жϵ�ǰ���غ�ǰ������ع����������߶�
                 if(init == true) {  // outer if
	                 if (data[0] != 0 )  { // inner if
	                	 
	                	 if(x - startPoint.x <= 1) {
	                		 //System.out.println("��ǰ�ڵ�հף�֮ǰ�ڵ�Ϊһ������");
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
             
             //�ж��߶��Ƿ���Ч�����ҳ��ȴ���һ������
             if(init == true && (x -1 - startPoint.x) > 1) {
            	 Line line = new Line(startPoint.x, startPoint.y, x -1, y);
            	 list.add(line);
             }
         }
    	 return list;
    }
    
    /**  
     * ��Matͼ���ʽת��Ϊ BufferedImage  
     * @param matrix  mat����ͼ��  
     * @return BufferedImage  
     */  
    public static BufferedImage toBufferedImage(Mat matrix) {  
        int type = BufferedImage.TYPE_BYTE_GRAY;  
        if (matrix.channels() > 1) {  
            type = BufferedImage.TYPE_3BYTE_BGR;  
        }  
        int bufferSize = matrix.channels() * matrix.cols() * matrix.rows();  
        byte[] buffer = new byte[bufferSize];  
        matrix.get(0, 0, buffer); // ��ȡ���е����ص�  
        BufferedImage image = new BufferedImage(matrix.cols(), matrix.rows(), type);  
        final byte[] targetPixels = ((DataBufferByte)image.getRaster().getDataBuffer()).getData();  
        System.arraycopy(buffer, 0, targetPixels, 0, buffer.length);  
        return image;  
    }  
    
    /**  
     * ��BufferedImage�ڴ�ͼ�񱣴�Ϊͼ���ļ�  
     * @param image BufferedImage  
     * @param filePath  �ļ���  
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