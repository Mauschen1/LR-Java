import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.io.IOException;

public class FractalExplorer extends JFrame {
    private int size;
    private JImageDisplay imageDisplay;
    private FractalGenerator fractal;
    private Rectangle2D.Double range;
    private int rowRemaining;
    JButton button;
    JButton save;
    JComboBox comboBox;

    public static void main(String[] args) {
        FractalExplorer fractalExplorer = new FractalExplorer(600);
        fractalExplorer.creatAndShowGUI();
        fractalExplorer.drawFractal();
    }


    FractalExplorer(int size){
        this.size = size;
        range = new Rectangle2D.Double();
        fractal = new Mandelbrot();
        fractal.getInitialRange(range);
        imageDisplay = new JImageDisplay(size,size);
    }

    private void creatAndShowGUI(){
        JFrame frame = new JFrame("Fractal");

        Mandelbrot mandelbrot = new Mandelbrot();
        Tricorn tricorn = new Tricorn();
        BurningShip burningShip = new BurningShip();

        //String[] fractals = {"Mandelbrot", "BurningShip", "Tricorn"};

        JPanel northpanel = new JPanel();
        JPanel southpanel = new JPanel();

        //JComboBox comboBox = new JComboBox(fractals);
        button = new JButton("Reset");
        save = new JButton("Save");
        JLabel label = new JLabel("Fractal:");

        button.setActionCommand("reset");
        save.setActionCommand("save");

        comboBox = new JComboBox();
        comboBox.addItem(mandelbrot);
        comboBox.addItem(tricorn);
        comboBox.addItem(burningShip);

        EventHandler eventHandler = new EventHandler();
        MouseHandler mouseHandler = new MouseHandler();

        imageDisplay.addMouseListener(mouseHandler);
        button.addActionListener(eventHandler);
        save.addActionListener(eventHandler);
        comboBox.addActionListener(eventHandler);

        imageDisplay.setLayout(new BorderLayout());

        northpanel.add(label);
        northpanel.add(comboBox);
        southpanel.add(save);
        southpanel.add(button);

        frame.add(imageDisplay,BorderLayout.CENTER);
        frame.add(northpanel,BorderLayout.NORTH);
        frame.add(southpanel,BorderLayout.SOUTH);
        frame.setSize(size,size + 100);
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
// вызов для каждой строки
    private void drawFractal(){
        rowRemaining = size;
        enableUI(false);
        for(int i = 0; i < size; i++){
            FractalWorker fractalWorker = new FractalWorker(i);
            fractalWorker.execute();
        }
    }
// Метод, запрещающий пользователю использовать кнопки во время рисования фрактала
    private void enableUI(boolean val){
        button.setEnabled(val);
        save.setEnabled(val);
        comboBox.setEnabled(val);
    }

    private class EventHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Object object = e.getSource();
            if(object instanceof JComboBox){
                fractal = (FractalGenerator) ((JComboBox) object).getSelectedItem();
                fractal.getInitialRange(range);
                drawFractal();
            }
            else if(object instanceof JButton) {
                JButton button = (JButton) object;
                if(button.getActionCommand().equals("reset")){
                    fractal.getInitialRange(range);
                    drawFractal();
                }
                else if (button.getActionCommand().equals("save")){
                    JFileChooser fileChooser = new JFileChooser();
                    FileFilter filter = new FileNameExtensionFilter("PNG Images", "png");
                    fileChooser.setFileFilter(filter);
                    fileChooser.setAcceptAllFileFilterUsed(false);
                    if(fileChooser.showSaveDialog(button.getParent())
                            != JFileChooser.APPROVE_OPTION)return;
                    try {
                        ImageIO.write(imageDisplay.getBufferedImage(),"png",
                                fileChooser.getSelectedFile());
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(button.getParent(), ex.getMessage(),
                                "Cannot Save Image", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
    }
    private class MouseHandler extends MouseAdapter{

        @Override
        public void mouseClicked(MouseEvent e)
        {
            if(rowRemaining != 0)return;
            int x = e.getX();
            int y = e.getY();
            double xCoord = FractalGenerator.getCoord(range.x, range.x + range.width, size, x);
            double yCoord = FractalGenerator.getCoord(range.y, range.y + range.height, size, y);
            fractal.recenterAndZoomRange(range, xCoord, yCoord, 0.5);
            drawFractal();
        }
    }
    // класс а-ля DrawFractal
    private class FractalWorker extends SwingWorker<Object, Object>{
        private int y;
        private int[] pixelRow;

        FractalWorker(int yCoord){
            y = yCoord;
        }
// Вычисляет цветовые значения для строки
        @Override
        protected Object doInBackground() throws Exception {
            pixelRow = new int[size];
            for(int i = 0; i < size; i++){
                double xCoord = FractalGenerator.getCoord(range.x,range.x + range.width, size, i);
                double yCoord = FractalGenerator.getCoord(range.y,range.y + range.height, size, y);
                int iter = fractal.numIterations(xCoord,yCoord);
                if (iter == -1)pixelRow[i] = 0;
                else {
                    float hue = 0.7f + (float) iter / 200f;
                    pixelRow[i] = Color.HSBtoRGB(hue, 1f, 1f);
                }
            }
            return null;
        }
// Заполняет пиксели в строке цветами
        @Override
        protected void done() {
            for(int i = 0; i < size; i++){
                imageDisplay.drawPixel(i,y,pixelRow[i]);
            }
            imageDisplay.repaint(0,0,y,size,1);
            rowRemaining--;
            if(rowRemaining == 0) enableUI(true);

        }
    }

}
