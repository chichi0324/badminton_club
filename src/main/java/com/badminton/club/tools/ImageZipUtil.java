package com.badminton.club.tools;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;
  
public class ImageZipUtil {  
	
	public static void main(String[] args) {
//		for(int i=9;i<27;i++){
//			for(int j=1;j<=5;j++){
//			zipWidthHeightImageFile(new File("/Users/xujiaqi/git/keelung/club/src/main/resources/static/images/activity/avt_"+i+"/adv_"+j+".jpg"),new File("/Users/xujiaqi/Desktop/未命名檔案夾/avt_"+i+"/adv_"+j+".jpg"),1800,1200,1f);
//			}
//		}
		
		zipWidthHeightImageFile(new File("/Users/xujiaqi/Desktop/jenny3.jpg"),new File("/Users/xujiaqi/Desktop/未命名檔案夾/jenny3.jpg"),1000,1000,1f);


		//zipImageFile(new File("C:\\spider\\2.JPG"),new File("C:\\spider\\2-2.JPG"),425,638,0.7f);		
		//zipImageFile(new File("C:\\spider\\3.jpg"),new File("C:\\spider\\3-3.jpg"),425,638,0.7f);
		
		System.out.println("ok");
	}
  
    /** 
     * 根據設定的寬高等比例壓縮圖片檔案<br> 先儲存原檔案，再壓縮、上傳 
     * @param oldFile  要進行壓縮的檔案 
     * @param newFile  新檔案 
     * @param width  寬度 //設定寬度時（高度傳入0，等比例縮放） 
     * @param height 高度 //設定高度時（寬度傳入0，等比例縮放） 
     * @param quality 質量 
     * @return 返回壓縮後的檔案的全路徑 
     */  
    public static String zipImageFile(File oldFile,File newFile, int width, int height,float quality) {  
        if (oldFile == null) {  
            return null;  
        }  
        try {  
            /** 對伺服器上的臨時檔案進行處理 */  
            Image srcFile = ImageIO.read(oldFile);  
            int w = srcFile.getWidth(null);  
            int h = srcFile.getHeight(null);  
            double bili;  
            if(width>0){  
                bili=width/(double)w;  
                height = (int) (h*bili);  
            }else{  
                if(height>0){  
                    bili=height/(double)h;  
                    width = (int) (w*bili);  
                }  
            }  
            
            String srcImgPath = newFile.getAbsoluteFile().toString();
            System.out.println(srcImgPath);
            String subfix = "jpg";
    		subfix = srcImgPath.substring(srcImgPath.lastIndexOf(".")+1,srcImgPath.length());

    		BufferedImage buffImg = null; 
    		if(subfix.equals("png")){
    			buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    		}else{
    			buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    		}

    		Graphics2D graphics = buffImg.createGraphics();
    		graphics.setBackground(new Color(255,255,255));
    		graphics.setColor(new Color(255,255,255));
    		graphics.fillRect(0, 0, width, height);
    		graphics.drawImage(srcFile.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);  

    		ImageIO.write(buffImg, subfix, new File(srcImgPath));  
  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        return newFile.getAbsolutePath();  
    }  
  
    /** 
     * 按設定的寬度高度壓縮圖片檔案<br> 先儲存原檔案，再壓縮、上傳 
     * @param oldFile  要進行壓縮的檔案全路徑 
     * @param newFile  新檔案 
     * @param width  寬度 
     * @param height 高度 
     * @param quality 質量 
     * @return 返回壓縮後的檔案的全路徑 
     */  
	public static String zipWidthHeightImageFile(File oldFile,File newFile, int width, int height,float quality) {  
        if (oldFile == null) {  
            return null;  
        }  
        String newImage = null;  
        try {  
            /** 對伺服器上的臨時檔案進行處理 */  
            Image srcFile = ImageIO.read(oldFile);  
            
            String srcImgPath = newFile.getAbsoluteFile().toString();
            System.out.println(srcImgPath);
            String subfix = "jpg";
    		subfix = srcImgPath.substring(srcImgPath.lastIndexOf(".")+1,srcImgPath.length());

    		BufferedImage buffImg = null; 
    		if(subfix.equals("png")){
    			buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    		}else{
    			buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    		}

    		Graphics2D graphics = buffImg.createGraphics();
    		graphics.setBackground(new Color(255,255,255));
    		graphics.setColor(new Color(255,255,255));
    		graphics.fillRect(0, 0, width, height);
    		graphics.drawImage(srcFile.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);  

    		ImageIO.write(buffImg, subfix, new File(srcImgPath));  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        return newImage;  
    }  
}