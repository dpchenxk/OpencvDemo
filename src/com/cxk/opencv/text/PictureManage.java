package com.cxk.opencv.text;  
  
import java.awt.AlphaComposite;
import java.awt.Graphics;  
import java.awt.Graphics2D;
import java.awt.Image;  
import java.awt.image.BufferedImage;  
import java.awt.image.DataBufferByte;  
import java.io.File;  
import java.io.IOException;  
  


import javax.imageio.ImageIO;  

import org.opencv.core.Core;  
import org.opencv.core.CvType;  
import org.opencv.core.Mat;  
import org.opencv.core.Size;  
import  org.opencv.imgcodecs.Imgcodecs;  
import org.opencv.imgproc.Imgproc;  
  
public class PictureManage {  
    private Mat image;  
    //private JLabel jLabelImage;  
    public PictureManage(String fileName) {  
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);  
        this.image= Imgcodecs.imread(fileName);  
    }  
      
    
    public PictureManage(BufferedImage img) {  
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);  
        this.image= this.Img2Mat(img, img.getType(), img.getType());
    }  
  
      
    /** 
     * 图片画质处理 
     * @param image 
     * @return 
     */  
    public static Mat setMatImage(Mat image) {  
        Mat loadeMatImage = new Mat();  
        
        //灰度处理  
        Imgproc.cvtColor(image,image,Imgproc.COLOR_RGB2GRAY);  
        //二值化处理  
        Mat binaryMat = new Mat(image.height(), image.width(), CvType.CV_8UC1);  
        Imgproc.threshold(image, binaryMat,180, 255, Imgproc.THRESH_BINARY);  
          
        //图像腐蚀  
        Mat element = Imgproc.getStructuringElement(Imgproc.MORPH_RECT,   
                new Size(500,500));  
        Imgproc.erode(binaryMat, image,element);  
        //loadeMatImage = image;  
        loadeMatImage = binaryMat;  
        return loadeMatImage; 
    	
    }  
      

    /**
    * 将BufferedImage类型转换成Mat类型 
    * @param bfImg
    * @param imgType bufferedImage的类型 如 BufferedImage.TYPE_3BYTE_BGR
    * @param matType 转换成mat的type 如 CvType.CV_8UC3
    * @return
    */
	public static Mat Img2Mat(BufferedImage bfImg, int imgType, int matType) {
		BufferedImage original = bfImg;
		int itype = imgType;
		int mtype = matType;

		if (original == null) {
			throw new IllegalArgumentException("original == null");
		}

		if (original.getType() != itype) {
			BufferedImage image = new BufferedImage(original.getWidth(),
					original.getHeight(), itype);

			Graphics2D g = image.createGraphics();
			try {
				g.setComposite(AlphaComposite.Src);
				g.drawImage(original, 0, 0, null);
			} finally {
				g.dispose();
			}
		}

		byte[] pixels = ((DataBufferByte) original.getRaster().getDataBuffer())
				.getData();
		Mat mat = Mat.eye(original.getHeight(), original.getWidth(), mtype);
		mat.put(0, 0, pixels);

		return mat;
	}


    
    /** 
     * Mat转image 
     * @param matrix 
     * @return 
     */  
    private Image toBufferedImage(Mat matrix) {  
        int type = BufferedImage.TYPE_BYTE_GRAY;  
        if (matrix.channels()>1) {  
            type = BufferedImage.TYPE_3BYTE_BGR;  
        }  
        int bufferSize = matrix.channels()*matrix.cols()*matrix.rows();  
        byte[] buffer = new byte[bufferSize];  
        matrix.get(0, 0, buffer);  
        BufferedImage image = new BufferedImage(matrix.cols(), matrix.rows(),type);  
        final byte[] targetPxiels = ((DataBufferByte)image.getRaster().getDataBuffer()).getData();  
        System.arraycopy(buffer, 0, targetPxiels, 0, buffer.length);  
        return image;  
    }  
      
    /*** 
     * 将Image变量保存成图片 
     * @param im 
     * @param fileName 
     */  
    public  void  saveJpgImage(Image im ,String  fileName) {  
        int w = im.getWidth(null);  
        int h = im.getHeight(null);  
        BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_3BYTE_BGR);  
        Graphics g = bi.getGraphics();  
        g.drawImage(im, 0, 0, null);  
        try {  
            ImageIO.write(bi, "jpg", new File(fileName));  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
    }  
    
    /*** 
     * 将Image变量保存成图片 
     * @param im 
     * @param fileName 
     */  
    public  void  savePngImage(Image im ,String  fileName) {  
        int w = im.getWidth(null);  
        int h = im.getHeight(null);  
        BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_3BYTE_BGR);  
        Graphics g = bi.getGraphics();  
        g.drawImage(im, 0, 0, null);  
        try {  
            ImageIO.write(bi, "png", new File(fileName));  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
    }  
    
    /** 
     * 图片处理 
     * @param args 
     */  
    public void  imshow(){  
          
        //添加原图  
        Image originalImage = toBufferedImage(image);  
        saveJpgImage(originalImage, "yuantu.jpg");  
        //jLabelImage.setIcon(new ImageIcon(originalImage));  
        //添加处理图  
         Mat mat1 = setMatImage(image);  
        Image newImage = toBufferedImage(mat1);  
        saveJpgImage(newImage, "xintu.jpg");  
    }
    
    public void dealImage(String dest)  {
    	 //添加处理图  
        Mat mat1 = setMatImage(image);  
       Image newImage = toBufferedImage(mat1);  
       savePngImage(newImage, dest);  
    }
}  