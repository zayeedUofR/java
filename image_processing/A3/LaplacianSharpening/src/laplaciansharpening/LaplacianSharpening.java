package laplaciansharpening;

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
        String fileName = "moon.raw";
        Path p = Paths.get(fileName);
        byte[] data = Files.readAllBytes(p);
        //System.out.println(data.length);
        BufferedImage img = new BufferedImage(464, 528, BufferedImage.TYPE_BYTE_GRAY);
        WritableRaster wr = img.getRaster();
        int x = 0;
        for (int i = 0; i < 528; i++) { //height
            for (int j = 0; j < 464; j++) { //width
                wr.setSample(j, i, 0, data[x]);
                // System.out.println("data: "+data[x]);
                x++;
            }
        }
        img.setData(wr);
        ImageIO.write(img, "jpg", new File("moon.jpg"));
        System.out.println(".raw to .jpg conversion is done! Please wait while 3x3 Laplacian sharpening filter is being applied on moon.jpg...");
    }

}

public class LaplacianSharpening {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        Raw_To_Jpg obj = new Raw_To_Jpg();
        obj.convert_raw();

        //moon = 464 (width) x 528 (height)
        BufferedImage inputImage = ImageIO.read(new File("moon.jpg")); //load the image from this current folder

        int[][] result = imageTo2DArray(inputImage); //pass buffered image to the method and get back the result

        int ROWS = result.length;    // height
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

        int[][] output_img = new int[ROWS][COLS]; //Laplace edge detection output image
        int[][] output_img_w1 = new int[ROWS][COLS]; // image for w1 value
        int[][] output_img_w2 = new int[ROWS][COLS]; //image for w2 value
        int[][] output_img_w3 = new int[ROWS][COLS]; // image for w3 value
        int[][] output_img_w4 = new int[ROWS][COLS]; // image for w4 value

        double w1 = 0.9;
        double w2 = 0.5;
        double w3 = 0.25;
        double w4 = 0.15;
        
        //Laplace kernel
        final double[][] HL
                = { 
                    {0.0, 1.0, 0.0},
                    {1.0, -4.0, 1.0},
                    {0.0, 1.0, 0.0}
                };

        // apply filter
        for (int u = 1; u <= ROWS + 2 - 2; u++) {     // boundaries are 0s
            for (int v = 1; v <= COLS + 2 - 2; v++) { // boundaries are 0s
                int sum = 0;
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        int p = new_img[u + i][v + j];
                        double c = HL[j + 1][i + 1];
                        sum += c * p;
                    }
                }
                int q = Math.round(Math.abs(sum));
                //img.setRGB(u, v, q);
                output_img[u - 1][v - 1] = q;  //edge detection
                double k = Math.abs(result[u - 1][v - 1] - q * 0.9);  // w1 = 0.9
                double l = Math.abs(result[u - 1][v - 1] - q * 0.5);  // w2 = 0.5
                double m = Math.abs(result[u - 1][v - 1] - q * 0.25);  // w3 = 0.25
                double n = Math.abs(result[u - 1][v - 1] - q * 0.15);  // w4 = 0.15

                output_img_w1[u - 1][v - 1] = (int) k;
                output_img_w2[u - 1][v - 1] = (int) l;
                output_img_w3[u - 1][v - 1] = (int) m;
                output_img_w4[u - 1][v - 1] = (int) n;
            }
        }

        int width = output_img[0].length;
        int height = output_img.length;

        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        WritableRaster wr = img.getRaster();

        BufferedImage img1 = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        WritableRaster wr1 = img1.getRaster();

        BufferedImage img2 = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        WritableRaster wr2 = img2.getRaster();

        BufferedImage img3 = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        WritableRaster wr3 = img3.getRaster();

        BufferedImage img4 = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        WritableRaster wr4 = img4.getRaster();

        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++) {
                wr.setSample(y, x, 0, output_img[x][y]);
                wr1.setSample(y, x, 0, output_img_w1[x][y]);
                wr2.setSample(y, x, 0, output_img_w2[x][y]);
                wr3.setSample(y, x, 0, output_img_w3[x][y]);
                wr4.setSample(y, x, 0, output_img_w4[x][y]);
            }
        }

        img.setData(wr);
        ImageIO.write(img, "jpg", new File("moon_laplace_edge_detected.jpg"));
        System.out.println("Please check for the output image named as moon_laplace_edge_detected.jpg at the application base directory.");

        img.setData(wr1);
        ImageIO.write(img1, "jpg", new File("moon_laplace_w1.jpg"));
        System.out.println("Please check for the output image named as moon_laplace_w1.jpg at the application base directory.");

        img.setData(wr2);
        ImageIO.write(img2, "jpg", new File("moon_laplace_w2.jpg"));
        System.out.println("Please check for the output image named as moon_laplace_w2.jpg at the application base directory.");

        img.setData(wr3);
        ImageIO.write(img3, "jpg", new File("moon_laplace_w3.jpg"));
        System.out.println("Please check for the output image named as moon_laplace_w3.jpg at the application base directory.");

        img.setData(wr4);
        ImageIO.write(img4, "jpg", new File("moon_laplace_w4.jpg"));
        System.out.println("Please check for the output image named as moon_laplace_w4.jpg at the application base directory.");

        System.out.println("\n\nDifferent w values used are w1 = " + w1 + ", w2 = " + w2 + ", w3 = " + w3 + " and w4 = " + w4 + "\n\n");
    }

    private static int[][] imageTo2DArray(BufferedImage inputImage) {

        final byte[] pixels = ((DataBufferByte) inputImage.getRaster()
                .getDataBuffer()).getData(); // get pixel value as single array from buffered Image

        final int width = inputImage.getWidth(); //get image width value
        final int height = inputImage.getHeight(); //get image height value
        int[][] result = new int[height][width]; //Initialize the array with height and width

        //this loop allocates pixels value to two dimensional array
        for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel++) {
            int argb = (int) pixels[pixel];
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
        return result; //returns the result as two dimensional array
    }
}
