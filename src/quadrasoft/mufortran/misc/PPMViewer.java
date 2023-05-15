package quadrasoft.mufortran.misc;

import quadrasoft.mufortran.general.Log;
import quadrasoft.mufortran.general.Session;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Integer.max;

public class PPMViewer implements KeyListener {
    private void loadImage(String path) {
        BufferedReader reader;
        int version = 0;
        int nchannels;

        try {
            reader = new BufferedReader(new FileReader(path));
            String line = reader.readLine();

            if (line.startsWith("P")) {
                version = Integer.parseInt(line.substring(1,2));
            }
            line = reader.readLine();
            Stream<String> tokens = Arrays.stream(line.split(" "));
            List<String> filtered_tokens = tokens.filter(x -> x!=null && !x.equals(" ") && !x.equals("")).collect(Collectors.toList());

            if (filtered_tokens.size() == 2) {
                width = Integer.parseInt(filtered_tokens.get(0));
                height = Integer.parseInt(filtered_tokens.get(1));
            } else {
                Log.send("Error: Unable to parse PPM file: Wrong number of dimensions.");
                throw new IOException("Unable to parse PPM file: Wrong number of dimensions.");
            }
            line = reader.readLine();
            depth = Integer.parseInt(line);
            if (depth <= 0 || width <= 0 || height <= 0 ) {
                Log.send("Error: Unable to parse PPM file: Depth, Width or Height <= 0.");
                throw new IOException("Unable to parse PPM file: Depth, Width or Height <= 0.");
            }
            line = reader.readLine();

            // TODO Replace values by enumeration
            if (version == 2) {
                nchannels = 1;
            }
            else if (version == 3) {
                nchannels = 3;
            }
            else {
                Log.send("Error: Unable to parse PPM file: Unsupported version in magic string.");
                throw new IOException("Unable to parse PPM file: Unsupported version in magic string.");
            }

            double[][][] buffer = new double[width][height][nchannels];
            int i = 0;
            while (line != null) {

                tokens = Arrays.stream(line.split(" "));
                filtered_tokens = tokens.filter(x -> x != null && !x.equals(" ") && !x.equals("")).collect(Collectors.toList());

                if (filtered_tokens.size() != width * nchannels ) {
                    Log.send("Error: Unable to parse PPM file: Inconsistent width.");
                    throw new IOException("Unable to parse PPM file: Inconsistent width.");
                }
                for (int j = 0; j < filtered_tokens.size() / nchannels; j++) {
                    for(int c = 0; c < nchannels; c++) {
                        buffer[j][i][c] = Integer.parseInt(filtered_tokens.get(j * nchannels + c));
                    }
                }
                line = reader.readLine();
                i++;
                if(i >= height) {
                    break;
                }
            }

            img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = (Graphics2D)img.getGraphics();
            for(i = 0; i < width; i++) {
                for(int j = 0; j < height; j++) {
                    float[] rgb =  new float[nchannels];
                    for(int c =0; c < nchannels; c++){
                        rgb[c] = (float) buffer[i][j][c] / (float) depth;
                    }
                    if (nchannels == 1) g.setColor(new Color(rgb[0], rgb[0], rgb[0]));
                    else g.setColor(new Color(rgb[0], rgb[1], rgb[2]));
                    g.fillRect(i, j, 1, 1);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            Log.send("Error: Unable to parse PPM file: Error during integer parsing.");
            e.printStackTrace();
        }
    }
    public PPMViewer(String path) {
        imagePath = path;
        loadImage(path);
        frame = new JFrame("PPM Viewer [r]eload [s]ave as png [a]nti aliasing [q]uit");
        maxsize = max(width, height);

        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.clearRect(0, 0, getWidth(), getHeight());
                if (interpolate) {
                    g2d.setRenderingHint(
                            RenderingHints.KEY_INTERPOLATION,
                            RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                } else {
                    g2d.setRenderingHint(
                            RenderingHints.KEY_INTERPOLATION,
                            RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
                }
                // Or _BICUBIC
                g2d.scale(1000 / maxsize, 1000 / maxsize);
                g2d.drawImage(img, 0, 0, this);
            }
        };
        panel.setPreferredSize(new Dimension(width * 1000 / maxsize, height*1000/maxsize));
        frame.addKeyListener(this);
        frame.getContentPane().add(panel);
        frame.pack();
        frame.setVisible(true);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch(e.getKeyCode()){
            case KeyEvent.VK_A:
                interpolate = ! interpolate;
                panel.repaint();
                break;
            case KeyEvent.VK_Q:
                frame.setVisible(false);
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                break;
            case KeyEvent.VK_R:
                loadImage(imagePath);
                panel.repaint();
                break;
            case KeyEvent.VK_S:
                String filename = new SimpleDateFormat("yyyyMMddHHmm'.png'").format(new Date());
                File output_file = new File(Session.getActiveProject().getPath() + filename);
                try {
                    ImageIO.write(img, "png", output_file);
                } catch (IOException ex) {
                    Log.send("Error: Unable to export image.");
                    ex.printStackTrace();
                }
                break;
        }


    }

    JPanel panel;
    int maxsize;
    BufferedImage img;
    boolean interpolate = false;
    JFrame frame;
    int width, height, depth;
    String imagePath;
}
