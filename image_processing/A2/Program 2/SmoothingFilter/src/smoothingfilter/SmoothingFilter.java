/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smoothingfilter;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.imageio.ImageIO;

/**
 *
 * @author Zayeed
 */

class Raw_To_Jpg {
    void convert_raw() throws IOException{
        String fileName="testpattern.raw";
        Path p= Paths.get(fileName);
        byte[] data = Files.readAllBytes(p);
        //System.out.println(data.length);
        BufferedImage img= new BufferedImage(500,500, BufferedImage.TYPE_BYTE_GRAY);
        WritableRaster wr= img.getRaster();
        int x=0;
        for(int i=0; i<500; i++){
            for(int j=0; j< 500; j++){
                wr.setSample(j, i, 0, data[x]);
                x++;
            }
        }
        img.setData(wr);
        ImageIO.write(img,"jpg",new File("testpattern.jpg"));
        System.out.println(".raw to .jpg conversion is done! Please wait while 3x3 Smoothing filter is being applied on testpattern.jpg...");
    }

}

public class SmoothingFilter {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Raw_To_Jpg obj = new Raw_To_Jpg();
        
        try{
            obj.convert_raw();
        }
        catch(IOException e){
          System.out.println(e);
        }
        
        BufferedImage img = null, img_copy = null;
        File f = null;

        //read image
        try{
          f = new File("testpattern.jpg"); //specify the image input path here
         // f = new File("F:\\CS825\\rose.jpg");
          img = ImageIO.read(f);
          img_copy = ImageIO.read(f);
          
        }catch(IOException e){
          System.out.println(e);
        }

        //get image width and height
        int width = img.getWidth();
        int height = img.getHeight();

        double[][] H = {
        {0.075, 0.125, 0.075},
        {0.125, 0.200, 0.125},
        {0.075, 0.125, 0.075}
        };
        
        
        
        for(int u = 1; u <= width - 2; u++){
            for (int v = 1; v <= height - 2; v++){
                int sum = 0;
                for (int i = -1; i <= 1; i++){
                    for (int j = -1; j <= 1; j++){
                        //System.out.println("Width: " +width+ "H = "+ height );
                        int p = img_copy.getRGB(u+i , v+j );
                        double c = H[j+1][i+1];
                        sum += c*p;
                       // System.out.println("P = " +p);
                    }
                }
                int q = (int) Math.round(sum);
                img.setRGB(u, v, q);
            }
        }
        
        
        //write image
        try{
          f = new File("smooth_filter_testpattern.jpg");
          ImageIO.write(img, "jpg", f);
        }catch(IOException e){
          System.out.println(e);
        }
        
        System.out.println("Please check for the output image named as smooth_filter_testpattern.jpg at the application base directory.");
        
    }
    
}
