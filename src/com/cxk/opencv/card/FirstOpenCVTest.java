package com.cxk.opencv.card;
import org.opencv.core.*;  
import org.opencv.imgcodecs.Imgcodecs;  
import org.opencv.imgproc.Imgproc;

import com.cxk.opencv.util.ImageUtil;

import javax.imageio.ImageIO;  
import java.awt.image.BufferedImage;  
import java.awt.image.DataBufferByte;  
import java.io.File;  
import java.io.IOException;  
  
/**  
 * Created by zhangwenchao on 2017/9/27.  
 */  
public class FirstOpenCVTest {  
    static {  
  
        //ע��������е�ʱ����Ҫ��VM option��Ӹ��� ָ��opencv��dll�ļ�����·��  
        //-Djava.library.path=$PROJECT_DIR$\opencv\x64  
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);   //����opencv all��  
    }  
  
    public static void main(String[] args) throws InterruptedException {  
  
        /**  
         * 1. ��ȡԭʼͼ��ת��ΪOpenCV��Mat���ݸ�ʽ  
         */  
  
        Mat srcMat = Imgcodecs.imread("E:\\img\\1.jpg");  //ԭʼͼ��  
  
  
        /**  
         * 2. ǿԭʼͼ��ת��Ϊ�Ҷ�ͼ��  
         */  
        Mat grayMat = new Mat(); //�Ҷ�ͼ��  
        Imgproc.cvtColor(srcMat, grayMat, Imgproc.COLOR_RGB2GRAY);  
  
        BufferedImage grayImage =  ImageUtil.toBufferedImage(grayMat);  
  
        ImageUtil.saveJpgImage(grayImage,"E:/grayImage.jpg");  
  
        System.out.println("����Ҷ�ͼ��");  
  
        
        /**  
         * 3���ԻҶ�ͼ����ж�ֵ������  
         */  
        Mat binaryMat = new Mat(grayMat.height(),grayMat.width(),CvType.CV_8UC1);  
        Imgproc.threshold(grayMat, binaryMat, 150, 500, Imgproc.THRESH_BINARY);  
        BufferedImage binaryImage =  ImageUtil.toBufferedImage(binaryMat);  
        ImageUtil.saveJpgImage(binaryImage,"E:/binaryImage.jpg");  
        System.out.println("�����ֵ��ͼ��");  
  
        ImageUtil.cutImage(binaryMat);
        
  
        /**  
         * 4��ͼ��ʴ---��ʴ���ø��ӿ�,��.����ʶ��--ʹ��3*3��ͼƬȥ��ʴ  
         */  
        Mat destMat = new Mat(); //��ʴ���ͼ��  
        Mat element = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3));  
        Imgproc.erode(binaryMat,destMat,element);  
        BufferedImage destImage =  ImageUtil.toBufferedImage(destMat);  
        ImageUtil.saveJpgImage(destImage,"E:/destImage.jpg");  
        System.out.println("���港ʴ����ͼ��");  
  
        
        /**  
         * 5 ͼƬ�и�  
         */  
  
        //��ȡ��ͼ�ķ�Χ--�ӵ�һ�п�ʼ����,ͳ��ÿһ�е����ص�ֵ������ֵ�ĸ���,�ٸ��ݸ����жϸõ��Ƿ�Ϊ�߽�  
        //�жϸ��еĺ�ɫ���ص��Ƿ����һ��ֵ���˴�Ϊ150��,����������,�ҵ��ϱ߽�,�±߽������ֹͣ  
        int a =0, b=0, state = 0;  
        for (int y = 0; y < destMat.height(); y++)//��  
        {  
            int count = 0;  
            for (int x = 0; x < destMat.width(); x++) //��  
            {  
                //�õ��������ص��ֵ  
                byte[] data = new byte[1];  
                destMat.get(y, x, data);  
//                if (data[0] == 0)  
//                    count = count + 1;  
                
                if (data[0] == 0 && state == 0)  {
                	a = y;
                	state = 1;
                	break;
                }else if(data[0] == 0 && state == 1){
                	b = y;
                }
                
            }  
        }  
        System.out.println("�����½�"+Integer.toString(a));  
        System.out.println("�����Ͻ�"+Integer.toString(b));  
  
  
        //����,����X,����Y,��ͼ���,��ͼ����  
        Rect rect = new Rect(0,a,destMat.width(),b - a);  
        Mat resMat = new Mat(destMat,rect);  
        BufferedImage resImage =  ImageUtil.toBufferedImage(resMat);  
        ImageUtil.saveJpgImage(resImage,"E:/resImage.jpg");  
        System.out.println("�����и��ͼ��");  
  
  
        /**  
         * ʶ��-  
         */  
        try {  
            Process  pro = Runtime.getRuntime().exec(new String[]{"D:/Program Files (x86)/Tesseract-OCR/tesseract.exe", "E:/resImage.jpg","E:/result"});  
            pro.waitFor();  
        } catch (IOException e) {  
            e.printStackTrace();  
        } 
  
        try {  
            String result =  TesseractOCRUtil.recognizeText(new File("E:/13.jpg"));  
            System.out.println(result);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
  
    }



	
  
  
  
  
  
  
    
  
  
  
  
  
}  