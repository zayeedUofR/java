/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rgb_to_hsi_to_rgb;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

/**
 *
 * @author Zayeed
 */
public class RGB_to_HSI_to_RGB {

    /**
     * @param img
     * @return 
     */
    
    public static int height, width, type;
    
    public RGB_to_HSI_to_RGB(int h, int w, int t){
        RGB_to_HSI_to_RGB.height = h;
        RGB_to_HSI_to_RGB.width = w;
        RGB_to_HSI_to_RGB.type = t;
        System.out.println("Image Properties:\nHeight: "+height+"\nWidth: "+width+" \nType: "+type);
    }
    
    //public static 
    
    public static void rgb_pixels(BufferedImage img) throws IOException{
        int red;
        int green;
        int blue;
        int alpha;
        float[] EQ_I;
        
        ArrayList<Double> hsi; //= new ArrayList<Double>();
        //ArrayList<double[][] > HSI = new ArrayList< >();
        
        double[][] A = new double[width][height];
        double[][] H = new double[width][height];
        double[][] S = new double[width][height];
        double[][] I = new double[width][height];
        
        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++){
                // Get pixels by R, G, B
                alpha = new Color(img.getRGB (i, j)).getAlpha();
                red = new Color(img.getRGB (i, j)).getRed();
                green = new Color(img.getRGB (i, j)).getGreen();
                blue = new Color(img.getRGB (i, j)).getBlue();
                
                //System.out.println("A: "+alpha+" R: "+red+" G: "+green+" B: "+blue);
                hsi = convert_RGB_to_HSI(red, green, blue);
                A[i][j] = alpha;
                H[i][j] = hsi.get(0);
                S[i][j] = hsi.get(1);
                I[i][j] = hsi.get(2);
                
                
            }
        }
        
        EQ_I = intensity_equilizer(I);
        
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                I[x][y] = Math.round(EQ_I[(int)I[x][y]]);  
                //System.out.println(I[x][y]);
            }
        }
        
        //converting the HSI values to RGB and writing output image
        HSI_to_RGB(A, H, S, I);
        
    }
    
    public static ArrayList<Double> convert_RGB_to_HSI(int R, int G, int B){
        
        ArrayList<Double>  hsi;
        hsi = new ArrayList<>();
       
        double H = 0, S, I, theta;
        double m = Math.min(R, Math.min(G, B));
        //System.out.println("m = "+m);
        
        I = (R + G + B)/3;
        S = 1 - m*3/(R + G + B); // min (R, G, B)
        
        double k = 0.5*((R - G) + (R - B));
        double l = Math.sqrt((R - G)*(R - G) + (R - B)*(G - B));
        theta = Math.acos(k/l);  //radians
        
        theta = Math.round(Math.toDegrees(theta)); //converting to degrees
        
        //System.out.println("K/l =  "+k/l+" L = "+ l + " theta = "+theta);
    
        if( B <= G)
            H = theta;
        else if ( B > G)
            H = 360 - theta;
        //System.out.println("H: "+H+" S: "+S+" I: "+I);
      
        hsi.add(H);
        hsi.add(S);
        hsi.add(I);
        
        return hsi;
        

    }
    
    public static float[] intensity_equilizer(double[][] I){
        
        int[] intensityH = new int[256];
        
        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++){
                intensityH[(int)I[i][j]]++; //count frequencies
                
            }
        }
        
        int[] chistogram = new int[256];
        chistogram[0] = intensityH[0];
        for(int i=1; i<256; i++){
            chistogram[i] = chistogram[i-1] + intensityH[i]; //cumulative histogram;
            //System.out.println("chistogram["+i+"] = "+chistogram[i]+" chistogram["+(i-1)+"] "+chistogram[i-1]+" intensityH["+i+"] "+intensityH[i] );
        }
        
        float[] arr = new float[256];
        for(int i=0; i<256; i++){
            arr[i] =  (float)((chistogram[i]*255.0)/(float)(height*width));
        }
        
        return arr;
        
    }
    
    
    public static void HSI_to_RGB(double[][] A, double[][] H, double[][] S, double[][] I) throws IOException{  //final call 
        
        double i, x, y, z, r = 0, g = 0, b = 0, h;
        double R, G, B;
        int rgb = 0;
        BufferedImage nImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for(int m = 0; m < width; m++){
            for(int n = 0; n < height; n++){
                i = I[m][n]/255;
                x = i*(1 - S[m][n]);
                
                
                if(H[m][n] < 120){
                    h = Math.toRadians(H[m][n]); //because the trigonometric functions in java take radian values, and not degrees
                    //h = H[m][n];
                    y = i*(1 + (S[m][n]*Math.cos(h))/(Math.cos(Math.toRadians(60)-h)));
                    z = 3*i-(x + y);
                    
                    r = y; g = z; b = x;
                    
                }
                else if(H[m][n] >= 120 && H[m][n] <= 240){
                    h = Math.toRadians(H[m][n] - 120);
                    //h = H[m][n] - 120;
                    y = i*(1 + (S[m][n]*Math.cos(h))/(Math.cos(Math.toRadians(60)-h)));
                    z = 3*i-(x + y);
                    
                    r = x; g = y; b = z;  
                }
                else if(H[m][n] >= 240 && H[m][n] <= 360){
                    h = Math.toRadians(H[m][n] - 240);
                    //h = H[m][n] - 240;
                    y = i*(1 + (S[m][n]*Math.cos(h))/(Math.cos(Math.toRadians(60)-h)));
                    z = 3*i-(x + y);
                    
                    r = z; g = x; b = y;
                }
                
               R =  Math.round(255*r); G =  Math.round(255*g); B =  Math.round(255*b);
               
               if(R > 255.0) R = 255.0;
               if(G > 255.0) G = 255.0;
               if(B > 255.0) B = 255.0;
                
               Color c = new Color((int) R, (int) G, (int) B);
               rgb = c.getRGB();
               
               // Write pixels into image
               nImage.setRGB(m, n, rgb);
                
            }
        }
        
        //write image file
        File outputfile = new File("RGB2HSI_problem_2_outputImage.bmp");
        ImageIO.write(nImage, "bmp", outputfile);
        
        System.out.println("The image summer_deck.bmp was converted from RGB to HSI, and then applied Histogram Equilization\non Intensity I. Finally HSI values were converted back to RGB to save the image as RGB2HSI_problem_2_outputImage.bmp. \nThe conversion formula used are described in report.");
    
    }    
    
    
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        File file= new File("summer_deck.bmp");
        BufferedImage image = ImageIO.read(file);
        
        RGB_to_HSI_to_RGB ob;
        ob = new RGB_to_HSI_to_RGB(image.getHeight(), image.getWidth(), image.getType());
        RGB_to_HSI_to_RGB.rgb_pixels(image);
        //ob.convert_RGB_to_HSI(100, 150, 200);
        
    }
    
}
