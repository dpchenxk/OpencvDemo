package com.cxk.opencv.util;

import java.awt.Color;
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
import java.util.Locale;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;

import com.sun.jimi.core.Jimi;  
import com.sun.jimi.core.JimiException;  
import com.sun.jimi.core.JimiWriter;  
import com.sun.jimi.core.options.JPGOptions;
import com.sun.media.imageio.plugins.tiff.TIFFImageWriteParam;

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
    
    
    /**  
     * 图片文件转换为tif格式  
     * @param imageFile 文件路径  
     * @param imageFormat 文件扩展名  
     * @return  
     */    
    public static File createTifImage(File imageFile, String imageFormat) {    
        File tempFile = null;    
        try {    
            Iterator<ImageReader> readers = ImageIO.getImageReadersByFormatName(imageFormat);    
            ImageReader reader = readers.next();    
  
            ImageInputStream iis = ImageIO.createImageInputStream(imageFile);    
            reader.setInput(iis);    
            //Read the stream metadata    
            IIOMetadata streamMetadata = reader.getStreamMetadata();    
  
            //Set up the writeParam    
            TIFFImageWriteParam tiffWriteParam = new TIFFImageWriteParam(Locale.CHINESE);    
            tiffWriteParam.setCompressionMode(ImageWriteParam.MODE_DISABLED);    
  
            //Get tif writer and set output to file    
            Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("tiff");    
            ImageWriter writer = writers.next();    
  
            BufferedImage bi = reader.read(0);    
            IIOImage image = new IIOImage(bi,null,reader.getImageMetadata(0));    
            tempFile = tempImageFile(imageFile);    
            ImageOutputStream ios = ImageIO.createImageOutputStream(tempFile);    
            writer.setOutput(ios);    
            writer.write(streamMetadata, image, tiffWriteParam);    
            ios.close();    
  
            writer.dispose();    
            reader.dispose();    
  
        } catch (IOException e) {    
            e.printStackTrace();    
        }    
        return tempFile;    
    }    
  
    private static File tempImageFile(File imageFile) {    
    	String parent = imageFile.getParent();
        
    	 String fileName = imageFile.getName().substring(0,imageFile.getName().lastIndexOf("."));
        
        return new File(parent+"\\"+fileName+".tif");    
    }    
    
    
    /** 
     *  
     * @param sfile 
     *            需要去噪的图像 
     * @param destDir 
     *            去噪后的图像保存地址 
     * @throws IOException 
     */  
    public static File cleanImage(File sfile, String destDir,String format)  
            throws IOException  
    {  
        File destF = new File(destDir);  
        if (!destF.exists())  
        {  
            destF.mkdirs();  
        }  
  
        BufferedImage bufferedImage = ImageIO.read(sfile);  
        int h = bufferedImage.getHeight();  
        int w = bufferedImage.getWidth();  
  
        // 灰度化  
        int[][] gray = new int[w][h];  
        for (int x = 0; x < w; x++)  
        {  
            for (int y = 0; y < h; y++)  
            {  
                int argb = bufferedImage.getRGB(x, y);  
                // 图像加亮（调整亮度识别率非常高）  
                int r = (int) (((argb >> 16) & 0xFF) * 1.1 + 30);  
                int g = (int) (((argb >> 8) & 0xFF) * 1.1 + 30);  
                int b = (int) (((argb >> 0) & 0xFF) * 1.1 + 30);  
                if (r >= 255)  
                {  
                    r = 255;  
                }  
                if (g >= 255)  
                {  
                    g = 255;  
                }  
                if (b >= 255)  
                {  
                    b = 255;  
                }  
                gray[x][y] = (int) Math  
                        .pow((Math.pow(r, 2.2) * 0.2973 + Math.pow(g, 2.2)  
                                * 0.6274 + Math.pow(b, 2.2) * 0.0753), 1 / 2.2);  
            }  
        }  
  
        // 二值化  
        int threshold = ostu(gray, w, h);  
        BufferedImage binaryBufferedImage = new BufferedImage(w, h,  
                BufferedImage.TYPE_BYTE_BINARY);  
        for (int x = 0; x < w; x++)  
        {  
            for (int y = 0; y < h; y++)  
            {  
                if (gray[x][y] > threshold)  
                {  
                    gray[x][y] |= 0x00FFFF;  
                } else  
                {  
                    gray[x][y] &= 0xFF0000;  
                }  
                binaryBufferedImage.setRGB(x, y, gray[x][y]);  
            }  
        }  
  
        // 矩阵打印  
        for (int y = 0; y < h; y++)  
        {  
            for (int x = 0; x < w; x++)  
            {  
                if (isBlack(binaryBufferedImage.getRGB(x, y)))  
                {  
                    System.out.print("*");  
                } else  
                {  
                    System.out.print(" ");  
                }  
            }  
            System.out.println();  
        }  
  
        String fileName = sfile.getName().substring(0,sfile.getName().lastIndexOf("."));
        
        File file = new File(destDir, fileName+"."+format);
        ImageIO.write(binaryBufferedImage, format, file);  
        
        return file;
    }  
  
    public static boolean isBlack(int colorInt)  
    {  
        Color color = new Color(colorInt);  
        if (color.getRed() + color.getGreen() + color.getBlue() <= 300)  
        {  
            return true;  
        }  
        return false;  
    }  
  
    public static boolean isWhite(int colorInt)  
    {  
        Color color = new Color(colorInt);  
        if (color.getRed() + color.getGreen() + color.getBlue() > 300)  
        {  
            return true;  
        }  
        return false;  
    }  
  
    public static int isBlackOrWhite(int colorInt)  
    {  
        if (getColorBright(colorInt) < 30 || getColorBright(colorInt) > 730)  
        {  
            return 1;  
        }  
        return 0;  
    }  
  
    public static int getColorBright(int colorInt)  
    {  
        Color color = new Color(colorInt);  
        return color.getRed() + color.getGreen() + color.getBlue();  
    }  
  
    public static int ostu(int[][] gray, int w, int h)  
    {  
        int[] histData = new int[w * h];  
        // Calculate histogram  
        for (int x = 0; x < w; x++)  
        {  
            for (int y = 0; y < h; y++)  
            {  
                int red = 0xFF & gray[x][y];  
                histData[red]++;  
            }  
        }  
  
        // Total number of pixels  
        int total = w * h;  
  
        float sum = 0;  
        for (int t = 0; t < 256; t++)  
            sum += t * histData[t];  
  
        float sumB = 0;  
        int wB = 0;  
        int wF = 0;  
  
        float varMax = 0;  
        int threshold = 0;  
  
        for (int t = 0; t < 256; t++)  
        {  
            wB += histData[t]; // Weight Background  
            if (wB == 0)  
                continue;  
  
            wF = total - wB; // Weight Foreground  
            if (wF == 0)  
                break;  
  
            sumB += (float) (t * histData[t]);  
  
            float mB = sumB / wB; // Mean Background  
            float mF = (sum - sumB) / wF; // Mean Foreground  
  
            // Calculate Between Class Variance  
            float varBetween = (float) wB * (float) wF * (mB - mF) * (mB - mF);  
  
            // Check if new maximum found  
            if (varBetween > varMax)  
            {  
                varMax = varBetween;  
                threshold = t;  
            }  
        }  
  
        return threshold;  
    }  
    
    public static void main(String [] args){
    	
    	String source = "E:\\img\\1.gif";
    	String dest = "E:\\img\\1.jpg";
    	System.out.println("=============begin===========");
    	ImageUtil.toJPG(source, dest, 90);
    	System.out.println("=============end===========");
    	
    }
}