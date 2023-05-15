package kaeryv.en.ps;
/*
 * import java.awt.Graphics2D; import java.awt.Image; import
 * java.awt.event.ActionEvent; import java.awt.event.ActionListener; import
 * java.awt.event.KeyEvent; import java.awt.event.KeyListener; import
 * java.awt.event.MouseEvent; import java.awt.event.MouseListener; import
 * java.awt.event.WindowAdapter; import java.awt.event.WindowEvent; import
 * java.awt.image.BufferedImage; import java.awt.image.RenderedImage; import
 * java.io.File; import java.io.IOException; import java.util.List; import
 * javax.swing.JLabel;
 * 
 * import javax.imageio.ImageIO; import javax.swing.JFrame;
 * 
 * import kaeryv.en.gui.ImageTool; import kaeryv.en.resources.Resources;
 * 
 * import javax.swing.JPanel; import java.awt.BorderLayout; import
 * java.awt.Component; import java.awt.Dimension;
 * 
 * import javax.swing.JTabbedPane; import javax.swing.event.ChangeEvent; import
 * javax.swing.event.ChangeListener;
 * 
 * import org.ghost4j.document.DocumentException; import
 * org.ghost4j.document.PSDocument; import
 * org.ghost4j.renderer.RendererException; import
 * org.ghost4j.renderer.SimpleRenderer; import javax.swing.JToolBar; import
 * javax.swing.SwingConstants; import javax.swing.JButton; import
 * javax.swing.JSlider; import javax.swing.JProgressBar; import java.awt.Color;
 * 
 * public class PsReader extends JFrame implements ActionListener,
 * ChangeListener, MouseListener, KeyListener { private static final long
 * serialVersionUID = 1L; private static final int _startingDPI = 300; private
 * List<Image> images; SimpleRenderer renderer; PSDocument document;
 * 
 * public PsReader(String string) { super(); psFolder = string.substring(0,
 * string.lastIndexOf("/") + 1); this.setVisible(false);
 * getContentPane().setLayout(new BorderLayout(0, 0)); //
 * setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); lblRendering = new
 * JLabel("Rendering");
 * 
 * JPanel panel_1 = new JPanel(); panel_1.setLayout(new BorderLayout(0, 0));
 * 
 * progressBar = new JProgressBar(); panel_1.add(progressBar,
 * BorderLayout.EAST); panel_1.add(lblRendering, BorderLayout.WEST);
 * getContentPane().add(panel_1, BorderLayout.SOUTH); tabbedPane = new
 * JTabbedPane(SwingConstants.TOP); getContentPane().add(tabbedPane);
 * this.setVisible(true); document = new PSDocument(); try { document.load(new
 * File(string)); } catch (IOException e2) { // TODO Auto-generated catch block
 * e2.printStackTrace(); }
 * 
 * // create renderer renderer = new SimpleRenderer(); // set resolution (in
 * DPI) renderer.setResolution(_startingDPI);
 * 
 * // render
 * 
 * try { images = renderer.render(document); int i = 1; for (Image temp :
 * images) { temp = rotate(temp, 90); PsPanel temp2 = new PsPanel(temp);
 * tabbedPane.addTab("Page " + i, temp2); i++; }
 * lblRendering.setText("file opened");
 * 
 * JToolBar toolBar = new JToolBar(); getContentPane().add(toolBar,
 * BorderLayout.NORTH);
 * 
 * btnExportAspng = new JButton(""); btnExportAspng.setBackground(Color.ORANGE);
 * btnExportAspng.setIcon(Resources.getImageResource("icon.png"));
 * btnExportAspng.addActionListener(this); toolBar.add(btnExportAspng);
 * 
 * lblDpi = new JLabel("New label"); toolBar.add(lblDpi);
 * 
 * slider = new JSlider(); slider.setMinimum(1); slider.setMajorTickSpacing(10);
 * slider.setMaximum(300); slider.addChangeListener(this);
 * slider.addMouseListener(this); slider.addKeyListener(this);
 * toolBar.add(slider); slider.setValue(renderer.getResolution());
 * 
 * } catch (IOException | RendererException | DocumentException e) {
 * System.out.println("ERROR: " + e.getMessage()); }
 * 
 * this.addWindowListener(new WindowAdapter() {
 * 
 * @Override public void windowClosing(WindowEvent e) {
 * e.getWindow().setVisible(false); e.getWindow().dispose(); System.gc();
 * 
 * } }); ; this.setTitle("PSReader");
 * this.setIconImage(Resources.getImageResource("icon.graph").getImage());
 * this.setPreferredSize(new Dimension(800, 600));
 * this.setLocationRelativeTo(null); this.setVisible(true); this.pack(); }
 * 
 * public static Image rotate(Image img, double angle) { double sin =
 * Math.abs(Math.sin(Math.toRadians(angle))), cos =
 * Math.abs(Math.cos(Math.toRadians(angle)));
 * 
 * int w = img.getWidth(null), h = img.getHeight(null);
 * 
 * int neww = (int) Math.floor(w * cos + h * sin), newh = (int) Math.floor(h *
 * cos + w * sin);
 * 
 * BufferedImage bimg = ImageTool.toBufferedImage(ImageTool.getEmptyImage(neww,
 * newh)); Graphics2D g = bimg.createGraphics();
 * 
 * g.translate((neww - w) / 2, (newh - h) / 2); g.rotate(Math.toRadians(angle),
 * w / 2, h / 2); g.drawRenderedImage(ImageTool.toBufferedImage(img), null);
 * g.dispose();
 * 
 * return ImageTool.toImage(bimg); }
 * 
 * private void writePngToDisk() { progressBar.setValue(0); for (int i = 0; i <
 * images.size(); i++) { try { ImageIO.write((RenderedImage) images.get(i),
 * "png", new File(psFolder + (i + 1) + ".png")); progressBar.setValue(50); }
 * catch (IOException e) { // TODO Auto-generated catch block
 * e.printStackTrace(); } } progressBar.setValue(100); }
 * 
 * private void render() throws IOException, RendererException,
 * DocumentException {
 * 
 * Thread T = new Thread(new Runnable() {
 * 
 * @Override public void run() { progressBar.setValue(0);
 * lblRendering.setText("Rendering"); int i = 0; try { images =
 * renderer.render(document); } catch (IOException | RendererException |
 * DocumentException e) { // TODO Auto-generated catch block
 * e.printStackTrace(); } progressBar.setValue(30);
 * lblRendering.setText("Updating display"); for (Component temp1 :
 * tabbedPane.getComponents()) { ((PsPanel)
 * temp1).updateImg(rotate(images.get(i), 90)); i++; }
 * lblRendering.setText("Done !"); progressBar.setValue(100); } }); T.start(); }
 * 
 * private static JTabbedPane tabbedPane; private JLabel lblDpi; private JSlider
 * slider; private JLabel lblRendering; private JProgressBar progressBar;
 * private JButton btnExportAspng; private String psFolder;
 * 
 * @Override public void actionPerformed(ActionEvent e) { if
 * (e.getSource().equals(btnExportAspng)) { writePngToDisk(); } }
 * 
 * @Override public void stateChanged(ChangeEvent e) { if
 * (e.getSource().equals(slider)) { renderer.setResolution(slider.getValue());
 * lblDpi.setText(" " + slider.getValue() + " dpi");
 * 
 * }
 * 
 * }
 * 
 * @Override public void mouseClicked(MouseEvent arg0) { // TODO Auto-generated
 * method stub
 * 
 * }
 * 
 * @Override public void mouseEntered(MouseEvent arg0) { // TODO Auto-generated
 * method stub
 * 
 * }
 * 
 * @Override public void mouseExited(MouseEvent arg0) { // TODO Auto-generated
 * method stub
 * 
 * }
 * 
 * @Override public void mousePressed(MouseEvent arg0) { // TODO Auto-generated
 * method stub
 * 
 * }
 * 
 * @Override public void mouseReleased(MouseEvent e) { if
 * (e.getSource().equals(slider)) { try { render(); } catch (IOException |
 * RendererException | DocumentException e1) { // TODO Auto-generated catch
 * block e1.printStackTrace(); } }
 * 
 * }
 * 
 * @Override public void keyPressed(KeyEvent arg0) { // TODO Auto-generated
 * method stub
 * 
 * }
 * 
 * @Override public void keyReleased(KeyEvent e) { if
 * (e.getSource().equals(slider)) { try { render(); } catch (IOException |
 * RendererException | DocumentException e1) { // TODO Auto-generated catch
 * block e1.printStackTrace(); } }
 * 
 * }
 * 
 * @Override public void keyTyped(KeyEvent arg0) { // TODO Auto-generated method
 * stub
 * 
 * }
 * 
 * }
 */
