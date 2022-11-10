import javax.imageio.ImageIO;
import javax.print.*;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.print.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class TestPrint {
    private static ArrayList<BufferedImage> images = new ArrayList<>(4);
    private static ArrayList<PageFormat> pages = new ArrayList<>(4);
    private static int imgWidth = 96;
    private static int imgHeight = 40;
    private static int displayScaleFactor = 4;

    public static void main(String[] args) throws Exception {

        images.add(generateImage("Image 1", imgWidth, imgHeight));
        images.add(generateImage("Image 2", imgWidth, imgHeight));
        images.add(generateImage("Image 3", imgHeight, imgWidth));
        images.add(generateImage("Image 4", imgHeight, imgWidth));

        pages.add(generatePage(imgWidth, imgHeight, PageFormat.LANDSCAPE));
        pages.add(generatePage(imgWidth, imgHeight, PageFormat.PORTRAIT));
        pages.add(generatePage(imgHeight, imgWidth, PageFormat.LANDSCAPE));
        pages.add(generatePage(imgHeight, imgWidth, PageFormat.PORTRAIT));

//        displayImg(images.get(3));
//        for (int i = 0; i < 4; i++) saveImg(images.get(i), "image" + (i + 1));
        //On JDK 11 this property 'fixes' the issue by forcing the use of PSPrinterJob
//        System.setProperty("java.awt.printerjob", "sun.print.PSPrinterJob");
        new TestPrint().print();
    }

    private static void saveImg(BufferedImage img, String filename) throws IOException {
        Path p = Paths.get("./" + filename + ".png");
        ImageIO.write(img, "png", p.toFile());
    }

    private static void displayImg(BufferedImage img) {
        Image scaledImg = img.getScaledInstance(
                img.getWidth() * displayScaleFactor,
                img.getHeight() * displayScaleFactor,
                Image.SCALE_DEFAULT
        );
        JFrame frame = new JFrame("print");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel canvas = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(scaledImg, 0,0, null);
            }
        };
        canvas.setSize(scaledImg.getWidth(null), scaledImg.getHeight(null));
        frame.setSize(imgWidth * displayScaleFactor + 40, imgHeight * displayScaleFactor + 40);
        frame.getContentPane().add(canvas, BorderLayout.CENTER);
        frame.setVisible(true);
    }


    private static BufferedImage generateImage(String label, int w, int h) {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        g.setColor(Color.BLUE);
        g.fillRect(0, 0, w, h);
        g.setColor(Color.YELLOW);
        g.setFont(g.getFont().deriveFont(8.0f));

        int s = 1;
        g.fillRect(s, s, w - 2 * s, h - 2 * s);

        int d = 12;
        int N = (w / d) + 1;
        for (int i = 0; i < N; i++) {
            int x = i * d;
            g.setColor(Color.ORANGE);
            g.drawLine(x, 0, x, h);
            g.setColor(Color.RED);
            g.drawString("" + i, x, h / 2);
        }

        g.setColor(Color.ORANGE);
        g.drawLine(0, 0, w, h);
        g.drawLine(w, 0, 0, h);

        g.setColor(Color.BLACK);
        g.drawString(label, w / 2 - 20, h / 4);

        return img;
    }

    private static PageFormat generatePage(int width, int height, int orientation) {
        PageFormat page = new PageFormat();
        page.setOrientation(orientation);

        Paper paper = page.getPaper();
        paper.setSize(width, height);
        paper.setImageableArea(0, 0, width, height);
        paper.setSize(width, height);

        page.setPaper(paper);
        return page;
    }

    public void print() throws PrinterException, PrintException, IOException {
        PrinterJob job = PrinterJob.getPrinterJob();
        PrintService printService = PrintServiceLookup.lookupDefaultPrintService();
        job.setPrintService(printService);

        Book book = new Book();
        for (int n = 0; n < 4; n++) {
            book.append(new ImagePrintable(pages.get(n), images.get(n)), pages.get(n));
        }
        job.setPageable(book);
        HashPrintRequestAttributeSet attributes = new HashPrintRequestAttributeSet();
        attributes.add(new MediaPrintableArea(0f, 0f, imgWidth*.5f/72f, imgHeight/72f, MediaPrintableArea.INCH));
        job.print();
    }

    private class ImagePrintable implements Printable {
        private BufferedImage image;

        public ImagePrintable(PageFormat page, BufferedImage image) {
            this.image = image;
        }

        @Override
        public int print(Graphics g, PageFormat pageFormat, int pageIndex) {
//            pageFormat.setOrientation(PageFormat.PORTRAIT);
            int x, y, w, h;
            if (pageFormat.getOrientation() == PageFormat.LANDSCAPE) {
                Graphics2D g2d = (Graphics2D)g;
                g2d.rotate(Math.toRadians(90), 0, 0);
                g2d.translate(0, -image.getHeight());
                g2d.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
            } else {
                g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
            }
            return PAGE_EXISTS;
        }
    }
}


