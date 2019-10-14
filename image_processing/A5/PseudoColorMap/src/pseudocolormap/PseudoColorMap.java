/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pseudocolormap;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author Zayeed
 */
public class PseudoColorMap {

    /**
     * @param img
     * @return
     */
    public static int height, width, type, R, G, B, alpha = 0;

    public static int[] LUT_R = new int[256];
    public static int[] LUT_G = new int[256];
    public static int[] LUT_B = new int[256];

    public PseudoColorMap(int w, int h, int t) {  //constructor
        PseudoColorMap.height = h;
        PseudoColorMap.width = w;
        PseudoColorMap.type = t;
        build_LUT();
        System.out.println("Image Properties:\nHeight: " + height + "\nWidth: " + width + "\nType: " + type);
    }

    public static void build_LUT() {  //pseudoColor Look up table (LUT) initiator
        for (int i = 0; i < 256; i++) {
            LUT_R[i] = (int) (255 - i);
            if (LUT_R[i] < 0) {
                LUT_R[i] = 0;
            }

            LUT_G[i] = i;
            if (LUT_G[i] > 255) {
                LUT_G[i] = 255;
            }

            if (i >= 256 / 3) {
                LUT_B[i] = LUT_B[i - 1] + 1;
                if (LUT_B[i] > 255) {
                    LUT_B[i] = 255;
                }
            }
            //System.out.println(LUT_B[i]);

        }
    }

    private static int[][] imageTo2DArray(BufferedImage inputImage) {

        final byte[] pixels = ((DataBufferByte) inputImage.getRaster().getDataBuffer()).getData(); // get pixel value as single array from buffered Image

        final int width = inputImage.getWidth(); //get image width value
        final int height = inputImage.getHeight(); //get image height value
        int[][] result = new int[height][width]; //Initialize the array with height and width

        for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel++) {
            int argb = 0;
            argb = (int) pixels[pixel];
            if (argb < 0) { //if pixel value is negative, change to positive 
                argb += 256;
            }
            result[row][col] = argb;
            col++;
            if (col == width) {
                col = 0;
                row++;
            }
        }

        return result; //return the result as two dimensional array
    }

    public static void main(String[] args) throws IOException {
        // TODO code application logic here

        File file = new File("tempusa.bmp");
        BufferedImage image = ImageIO.read(file);

        PseudoColorMap pseudoColorMap = new PseudoColorMap(image.getWidth(), image.getHeight(), image.getType());

        BufferedImage nImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        int rgb = 0;
        //int newPixel = 0;
        int[][] result = imageTo2DArray(image);

        int ROWS = result.length;    // height
        int COLS = result[0].length; //width 

        //System.out.println("R:"+ROWS+" C: "+COLS);
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                R = LUT_R[result[i][j]];
                G = LUT_G[result[i][j]];
                B = LUT_B[result[i][j]];
                //System.out.print(result[i][j]+" ");
                Color pseudoColor = new Color(R, G, B); // compute color
                rgb = pseudoColor.getRGB();

                // Write pixels into image
                nImage.setRGB(j, i, rgb);
            }
            //System.out.println();
        }

        File outputImg = new File("pesudoColorUSA_problem3_outputImage.jpg");
        ImageIO.write(nImage, "jpg", outputImg);

        System.out.println("PseudoColor Image has been generated and saved as pesudoColorUSA_problem3_outputImage.jpg at application base directory");

    }

}
