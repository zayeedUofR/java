/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package medianfilter;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import javax.imageio.ImageIO;

/**
 *
 * @author Zayeed
 */

class Raw_To_Jpg {
    void convert_raw() throws IOException{
        String fileName="circuit.raw";
        Path p= Paths.get(fileName);
        byte[] data = Files.readAllBytes(p);
        //System.out.println(data.length);
        BufferedImage img= new BufferedImage(455,440, BufferedImage.TYPE_BYTE_GRAY);
        WritableRaster wr= img.getRaster();
        int x=0;
        for(int i=0; i<440; i++){
            for(int j=0; j< 455; j++){
                wr.setSample(j, i, 0, data[x]);
                x++;
            }
        }
        img.setData(wr);
        ImageIO.write(img,"jpg",new File("circuit.jpg"));
        System.out.println(".raw to .jpg conversion is done! Please wait while Median filter is being applied on circuit.jpg... May take up to 20 secs!");
    }

}

public class MedianFilter {

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
        
        final int r = 3*3; //filter size
        BufferedImage img = null, img_copy = null;
        File f = null;

        //read image
        try{
          f = new File("circuit.jpg"); //specify the image input path here
          img = ImageIO.read(f);
          img_copy = ImageIO.read(f);
          
        }catch(IOException e){
          System.out.println(e);
        }

        //get image width and height
        int width = img.getWidth();
        int height = img.getHeight();
        int[] A = new int[(2*r + 1)*(2*r + 1)];
        
        int n = 2*(r*r + r);
        
        for(int u = r; u <= width - r - 2; u++){
            for (int v = r; v <= height - r - 2; v++){
                int k = 0;
                for (int i = -r; i <= r; i++){
                    for (int j = -r; j <= r; j++){
                        //System.out.println("Width: " +width+ "H = "+ height );
                        A[k] = img_copy.getRGB(u+i , v+j );
                        k++;
                    }
                }
                Arrays.sort(A);
                img.setRGB(u, v, A[n]);
            }
        }
        
        
        //write image
        try{
          f = new File("median_filter_circuit.jpg");
          ImageIO.write(img, "jpg", f);
        }catch(IOException e){
          System.out.println(e);
        }
        
         System.out.println("Please check for the output image named as median_filter_circuit.jpg at the application base directory.");
    }
    
}
