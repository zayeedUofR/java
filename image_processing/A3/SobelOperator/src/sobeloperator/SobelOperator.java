/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sobeloperator;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
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

    void convert_raw() throws IOException {
        String fileName = "building.raw";
        Path p = Paths.get(fileName);
        byte[] data = Files.readAllBytes(p);
        //System.out.println(data.length);
        BufferedImage img = new BufferedImage(560, 420, BufferedImage.TYPE_BYTE_GRAY);
        WritableRaster wr = img.getRaster();
        int x = 0;
        for (int i = 0; i < 420; i++) { //height
            for (int j = 0; j < 560; j++) { //width
                wr.setSample(j, i, 0, data[x]);
                // System.out.println("data: "+data[x]);
                x++;
            }
        }
        img.setData(wr);
        ImageIO.write(img, "jpg", new File("building.jpg"));
        System.out.println(".raw to .jpg conversion is done! Please wait while 3x3 Sobel kernel filter is being applied on building.jpg...");
    }

}

public class SobelOperator {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        Raw_To_Jpg obj = new Raw_To_Jpg();
        obj.convert_raw();

        BufferedImage inputImage = ImageIO.read(new File("building.jpg")); //load the image from this current folder

        int[][] result = imageTo2DArray(inputImage); //pass buffered image to the method and get back the result

        int ROWS = result.length; // height
        int COLS = result[0].length; //width 

        int[][] new_img = new int[ROWS + 2][COLS + 2]; //to add zeros outside the boundary pixels;

        //initialize new image pixels all to zero
        for (int i = 0; i < new_img.length; i++) {
            for (int j = 0; j < new_img[0].length; j++) {
                new_img[i][j] = 0;
            }
        }
        //replace the original image value to the new_image with respect to the corresponding coordinates
        for (int i = 1; i < new_img.length - 1; i++) {
            for (int j = 1; j < new_img[0].length - 1; j++) {
                new_img[i][j] = result[i - 1][j - 1];
            }
        }

        int[][] output_img_Gr = new int[ROWS][COLS]; //Sobel  output image Gradient
        int[][] output_img_Hx = new int[ROWS][COLS]; //Sobel  output image Hx
        int[][] output_img_Hy = new int[ROWS][COLS]; //Sobel  output image Hy
        int[][] output_img_Th = new int[ROWS][COLS]; //Sobel  output image Threshold

        double[][] HG = { //Gradient
            {0.0, -0.5, 0.0},
            {-0.5, 0.0, 0.5},
            {0.0, 0.5, 0.0}
        };

        double[][] Hx = { //Sobel Hx
            {-1.0, 0.0, 1.0},
            {-2.0, 0.0, 2.0},
            {-1.0, 0.0, 1.0}
        };

        double[][] Hy = { //Sobel Hy
            {-1.0, -2.0, -1.0},
            {0.0, 0.0, 0.0},
            {1.0, 2.0, 1.0}
        };

        // apply filter
        for (int u = 1; u <= ROWS + 2 - 2; u++) { // boundary are 0s
            for (int v = 1; v <= COLS + 2 - 2; v++) { // boundary are 0s
                int sum_x = 0, sum_y = 0, sum_g = 0;
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        //System.out.println("Width: " +width+ "H = "+ height );
                        int p = new_img[u + i][v + j];
                        //int p = result[v+j][u+i];
                        double c = Hx[j + 1][i + 1];
                        double d = Hy[j + 1][i + 1];
                        double g = HG[j + 1][i + 1];
                        sum_x += c * p;
                        sum_y += d * p;
                        sum_g += g * p;
                        // System.out.println("P = " +p);
                    }
                }
                int q = Math.round(Math.abs(sum_x));
                int r = Math.round(Math.abs(sum_y));
                int s = Math.round(Math.abs(sum_g));
                int t = Math.round(Math.abs(sum_g));

                if (t < 128) {
                    t = 0;
                } else if (t >= 128) {
                    t = 255;
                }

                output_img_Hx[u - 1][v - 1] = q;  //Hx output
                output_img_Hy[u - 1][v - 1] = r;  //Hy output
                output_img_Gr[u - 1][v - 1] = s; // Gradient output
                output_img_Th[u - 1][v - 1] = t; // Threshold output

            }
        }

        int width = output_img_Hx[0].length;
        int height = output_img_Hx.length;

        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        WritableRaster wr = img.getRaster();

        BufferedImage img1 = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        WritableRaster wr1 = img1.getRaster();

        BufferedImage img2 = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        WritableRaster wr2 = img2.getRaster();

        BufferedImage img3 = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        WritableRaster wr3 = img3.getRaster();

        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++) {
                wr.setSample(y, x, 0, output_img_Hx[x][y]);
                wr1.setSample(y, x, 0, output_img_Hy[x][y]);
                wr2.setSample(y, x, 0, output_img_Gr[x][y]);
                wr3.setSample(y, x, 0, output_img_Th[x][y]);

            }
        }

        img.setData(wr);
        ImageIO.write(img, "jpg", new File("sobel_Hx.jpg"));
        System.out.println("Please check for the output image named as sobel_Hx.jpg at the application base directory.");

        img.setData(wr1);
        ImageIO.write(img1, "jpg", new File("sobel_Hy.jpg"));
        System.out.println("Please check for the output image named as sobel_Hy.jpg at the application base directory.");

        img.setData(wr2);
        ImageIO.write(img2, "jpg", new File("sobel_Gradient.jpg"));
        System.out.println("Please check for the output image named as sobel_Gradient.jpg at the application base directory.");

        img.setData(wr3);
        ImageIO.write(img3, "jpg", new File("sobel_threshold.jpg"));
        System.out.println("Please check for the output image named as sobel_threshold.jpg at the application base directory.");

    }

    private static int[][] imageTo2DArray(BufferedImage inputImage) {

        final byte[] pixels = ((DataBufferByte) inputImage.getRaster()
                .getDataBuffer()).getData(); // get pixel value as single array from buffered Image

        final int width = inputImage.getWidth(); //get image width value
        final int height = inputImage.getHeight(); //get image height value
        int[][] result = new int[height][width]; //Initialize the array with height and width

        // System.out.println("H: "+height+" W: "+width);
        //this loop allocates pixels value to two dimensional array
        //int c = 0;
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
}
