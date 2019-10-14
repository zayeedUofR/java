import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.*;

public class Hough_transform{
	public static void main(String [] arg){
		new Hough().init();
	}
}

class Hough{
    JFrame main_frame = new JFrame("Hough Transform");
    Panel_Image pan_org_img;
    Panel_Binary pan_binary;
    JPanel pan_slider;
    JPanel pan_controls;
    JButton btn_browse,btn_draw_hist,btn_exit;
    JSlider js_threshold;
    BufferedImage temp_img=null;
    int [] hist;
    BufferedImage img=null;

    public void init(){
        LookAndFeel mydefault=UIManager.getLookAndFeel();
	
        try{UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");}
	
        catch(Exception e){
            try{UIManager.setLookAndFeel(mydefault);}
            catch(Exception ex){System.out.println("error in look and feel"+ex);}
            System.out.println("error -"+e);
    }

    main_frame.setBackground(new Color(230,230,230));
    GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
    main_frame.setBounds(env.getMaximumWindowBounds());
    main_frame.setLayout(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints();
    c.fill = GridBagConstraints.BOTH;
    c.weighty = 0.5;   //request any extra vertical space
    c.weightx=0.5;
    c.insets = new Insets(20,20,20,20);  //top padding
    c.gridwidth = 2;   //2 columns wide
    c.gridheight = 6;
    pan_org_img = new Panel_Image();
    main_frame.add(pan_org_img,c);
    c.gridwidth = GridBagConstraints.REMAINDER;   //2 columns wide
    pan_binary = new Panel_Binary();
    main_frame.add(pan_binary,c);
    pan_controls = new JPanel();
    pan_controls.setLayout(new FlowLayout(1,20,10));
    pan_controls.setBackground(Color.GREEN);
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.gridheight = 1;
    c.weighty = 0.001;
    main_frame.add(pan_controls,c);
    btn_browse=new JButton("Select Image");
    pan_controls.add(btn_browse);
    btn_draw_hist=new JButton("Find Hough Transform");
    pan_controls.add(btn_draw_hist);
    btn_exit=new JButton("Exit Application");
    pan_controls.add(btn_exit);
    main_frame.setVisible(true);
    
    btn_draw_hist.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                int rgbval;
                int width = img.getWidth();
                int height = img.getHeight();

                int[][] acc;
                int rmax = (int)Math.sqrt(width*width + height*height);
                acc = new int[rmax][360];
                int r;

                for (int x=0; x<img.getWidth(); x++){
                    for (int y=0; y<img.getHeight(); y++){
                        rgbval = img.getRGB(x,y);
                        rgbval = rgbval & 0xff;
                        if (rgbval== 255){
                            for (int theta=0; theta<360; theta++){
                                r = (int)(x*Math.cos(((theta)*Math.PI)/180) + y*Math.sin(((theta)*Math.PI)/180));
                                if ((r > 0) && (r <= rmax))
                                acc[r][theta] = acc[r][theta] + 1;
                            }
                        }

                    }
                }

                // now normalise to 255 and put in format for a pixel array
                int max=0;
                int max_r=0, max_theta=0;

            // Find max acc value
                for(r=0; r < rmax; r++) {
                    for(int theta=0; theta<360; theta++) {
                        if (acc[r][theta] > max) {
                            System.out.println("Coordinates : @ r = "+r+"    theta = "+ theta+ " Accumulator = " + acc[r][theta]);
                            max_r = r;
                            max_theta = theta;
                            max = acc[r][theta];
                        }
                    }
                }

                temp_img = new BufferedImage(rmax, 360, BufferedImage.TYPE_INT_RGB);
                int value;
                for(r=0; r<rmax; r++) {
                    for(int theta=0; theta<360; theta++) {
                        value = (int)(((double)acc[r][theta]/(double)max)*255.0);
                        acc[r][theta] = 0xff000000 | (value << 16 | value << 8 | value);
                        temp_img.setRGB(r, theta, acc[r][theta]);
                    }
                }

                pan_binary.mydraw(temp_img,temp_img.getWidth(),temp_img.getHeight());


            // value = acc[max_r][max_theta];

            Graphics gg =  pan_org_img.getGraphics();
            gg.setColor(Color.RED);
            for(int x=0;x<width;x++) {
                for(int y=0;y<height;y++) {
                    int temp = (int)(x*Math.cos(((max_theta)*Math.PI)/180) + y*Math.sin(((max_theta)*Math.PI)/180));
                    int q = 5-7;
                    if( ( img.getRGB(x,y) & 0xff )==255 ){
                        int x1=0,y1=0;
                        x1 = (pan_org_img.getWidth()/2-img.getWidth()/2) + x;
                        y1 = (pan_org_img.getHeight()/2-img.getHeight()/2) + y;
                        gg.drawLine(x1, y1, x1, y1);
                    }

                }
            }

        }
    });

    btn_browse.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e){
            JFileChooser jfc = new JFileChooser();

            if(jfc.showOpenDialog(main_frame) == 0)
                try{
                    img = ImageIO.read(jfc.getSelectedFile());
                    pan_org_img.mydraw(img,img.getWidth(),img.getHeight());
                }
            catch(Exception exc){
                exc.printStackTrace();
            }
        }
    });

    btn_exit.addActionListener(new ActionListener() {
	public void actionPerformed(ActionEvent e){
            System.exit(0);
	}
    });

}//end of init
    
}//end of class

class Panel_Image  extends JPanel{
    BufferedImage bi=null;
    int imgH,imgW;

    public void paintComponent(Graphics g){
        super.paintComponent(g);

	if(imgW != 0)
            g.drawImage(bi,this.getWidth()/2-bi.getWidth()/2,this.getHeight()/2-bi.getHeight()/2,null);
    }

    public void mydraw(BufferedImage b_input,int w, int h){
	imgW=w;
	imgH=h;
	bi=b_input;
	repaint();
    }
}

class Panel_Binary  extends JPanel{
    BufferedImage bi=null;
    int imgH,imgW;

    public void paintComponent(Graphics g){
        super.paintComponent(g);

	if(imgW != 0)
            g.drawImage(bi,this.getWidth()/2-bi.getWidth()/2,this.getHeight()/2-bi.getHeight()/2,null);
    }

    public void mydraw(BufferedImage b_input,int w, int h){
        imgW=w;
	imgH=h;
	bi=b_input;
	repaint();
    }
}