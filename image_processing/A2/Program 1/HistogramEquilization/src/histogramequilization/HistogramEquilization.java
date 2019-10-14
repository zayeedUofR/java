/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package histogramequilization;

import java.awt.image.WritableRaster;
import java.awt.image.BufferedImage;
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
        String fileName="ct.raw";
        Path p= Paths.get(fileName);
        byte[] data = Files.readAllBytes(p);
        //System.out.println(data.length);
        BufferedImage img= new BufferedImage(256,256, BufferedImage.TYPE_BYTE_GRAY);
        WritableRaster wr= img.getRaster();
        int x=0;
        for(int i=0; i<256; i++){
            for(int j=0; j< 256; j++){
                wr.setSample(j, i, 0, data[x]);
                x++;
            }
        }
        img.setData(wr);
        ImageIO.write(img,"jpg",new File("ct.jpg"));
        System.out.println(".raw to .jpg conversion is done! Please wait while Histogram Equilization  is being applied on ct.jpg...");
    }

}

public class HistogramEquilization {

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
        
        BufferedImage img = null;
        File f = null;

        //read image
        try{
          f = new File("ct.jpg"); //specify the image input path here
          img = ImageIO.read(f);
          
          
        }catch(IOException e){
          System.out.println(e);
        }

        //get image width and height
        int width = img.getWidth();
        int height = img.getHeight();
        BufferedImage nImg = new BufferedImage(256, 256, BufferedImage.TYPE_BYTE_GRAY);
        WritableRaster wr = img.getRaster();
        WritableRaster er = nImg.getRaster();
        int totpix= wr.getWidth()*wr.getHeight();
        int[] histogram = new int[256];
 
        for (int x = 1; x < wr.getWidth(); x++) {
            for (int y = 1; y < wr.getHeight(); y++) {
                histogram[wr.getSample(x, y, 0)]++;
            }
        }
         
        int[] chistogram = new int[256];
        chistogram[0] = histogram[0];
        for(int i=1;i<256;i++){
            chistogram[i] = chistogram[i-1] + histogram[i];
        }
         
        float[] arr = new float[256];
        for(int i=0;i<256;i++){
            arr[i] =  (float)((chistogram[i]*255.0)/(float)totpix);
        }
         
        for (int x = 0; x < wr.getWidth(); x++) {
            for (int y = 0; y < wr.getHeight(); y++) {
                int nVal = (int) arr[wr.getSample(x, y, 0)];
                er.setSample(x, y, 0, nVal);
            }
        }
        nImg.setData(er);
        //return nImg;
        
         //write image
        try{
          f = new File("hist_equilization_ct.jpg");
          ImageIO.write(nImg, "jpg", f);
        }catch(IOException e){
          System.out.println(e);
        }
        
        System.out.println("Please check for the output image named as hist_equilization_ct.jpg at the application base directory.");
    }
    
}
