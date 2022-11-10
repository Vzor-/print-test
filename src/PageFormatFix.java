import java.awt.print.PageFormat;
import java.awt.print.Paper;

public class PageFormatFix extends PageFormat {
    int originalOrientation = PORTRAIT;
    Paper originalPaper;
    int mode = 0;
    // mode 1 = wide paper set to landscape
    // mode 2 = wide paper set to portrait
    // mode 3 = tall paper set to landscape
    // mode 4 = tall paper set to portrait


    public PageFormatFix() {
        super();
    }

    @Override
    public Object clone() {
        return super.clone();
    }

    @Override
    public double getWidth() {
        if (mode == 1 || mode == 2) return originalPaper.getWidth();
        return super.getWidth();
    }

    @Override
    public double getHeight() {
        if (mode == 1 || mode == 2) return originalPaper.getHeight();
        return super.getHeight();
    }

    @Override
    public double getImageableX() {
        //todo this likely needs to be flipped but it isn't used in this test
        return super.getImageableX();
    }

    @Override
    public double getImageableY() {
        //todo this likely needs to be flipped but it isn't used in this test
        return super.getImageableY();
    }

    @Override
    public double getImageableWidth() {
        if (mode == 1 || mode == 2) return originalPaper.getImageableWidth();
        return super.getImageableWidth();
    }

    @Override
    public double getImageableHeight() {
        if (mode == 1 || mode == 2) return originalPaper.getImageableHeight();
        return super.getImageableHeight();
    }

    @Override
    public Paper getPaper() {
        return super.getPaper();
    }

    @Override
    public void setPaper(Paper paper) {
        originalPaper = (Paper)paper.clone();
        calculateMode();
        super.setPaper(paper);
    }

    @Override
    public void setOrientation(int orientation) throws IllegalArgumentException {
        originalOrientation = orientation;
        calculateMode();
        super.setOrientation(orientation);
    }

    @Override
    public int getOrientation() {
        if (mode == 1 || mode == 2) {
            return LANDSCAPE;
        } else {
            return PORTRAIT;
        }
    }

    @Override
    public double[] getMatrix() {
        return super.getMatrix();
    }

    public boolean needsRotation() {
        return (mode == 1 || mode == 3);
    }

    private void calculateMode() {
        if (originalPaper == null) return;
        mode = 1;
        if (originalPaper.getHeight() >= originalPaper.getWidth()) mode = 3;
        if (originalOrientation == PORTRAIT) mode++;
    }
}
