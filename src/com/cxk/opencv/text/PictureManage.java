package com.cxk.opencv.text;  
  
import java.awt.Graphics;  
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
      
  
      
    /** 
     * ͼƬ���ʴ��� 
     * @param image 
     * @return 
     */  
    public static Mat setMatImage(Mat image) {  
        Mat loadeMatImage = new Mat();  
        //�Ҷȴ���  
        Imgproc.cvtColor(image,image,Imgproc.COLOR_RGB2GRAY);  
        //��ֵ������  
        Mat binaryMat = new Mat(image.height(), image.width(), CvType.CV_8UC1);  
        Imgproc.threshold(image, binaryMat,180, 255, Imgproc.THRESH_BINARY);  
          
        //ͼ��ʴ  
        Mat element = Imgproc.getStructuringElement(Imgproc.MORPH_RECT,   
                new Size(500,500));  
        Imgproc.erode(binaryMat, image,element);  
        //loadeMatImage = image;  
        loadeMatImage = binaryMat;  
        return loadeMatImage; 
    	
    }  
      
    /** 
     * Matתimage 
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
     * ��Image���������ͼƬ 
     * @param im 
     * @param fileName 
     */  
    public  void  saveImage(Image im ,String  fileName) {  
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
      
    /** 
     * ͼƬ���� 
     * @param args 
     */  
    public void  imshow(){  
          
        //���ԭͼ  
        Image originalImage = toBufferedImage(image);  
        saveImage(originalImage, "yuantu.jpg");  
        //jLabelImage.setIcon(new ImageIcon(originalImage));  
        //��Ӵ���ͼ  
         Mat mat1 = setMatImage(image);  
        Image newImage = toBufferedImage(mat1);  
        saveImage(newImage, "xintu.jpg");  
    }  
      
}  