package com.cxk.opencv;

import java.awt.image.ImageProducer;  
import java.io.File;  
  
import com.sun.jimi.core.Jimi;  
import com.sun.jimi.core.JimiException;  
import com.sun.jimi.core.JimiWriter;  
import com.sun.jimi.core.options.JPGOptions;  

public class G2j {  
  
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
    
    
    public static void main(String [] args){
    	
    	String source = "E:\\img\\phone.gif";
    	String dest = "E:\\img\\phone.jpg";
    	System.out.println("=============begin===========");
    	G2j.toJPG(source, dest, 90);
    	System.out.println("=============end===========");
    	
    }
}