import javax.print.DocFlavor;
import javax.print.SimpleDoc;
import javax.print.attribute.HashDocAttributeSet;

public class SimpleDocTest {

    public static void main(String[] args) throws Exception {
        HashDocAttributeSet attr = new HashDocAttributeSet();
//        attr.add()
        SimpleDoc doc = new SimpleDoc("test", DocFlavor.STRING.TEXT_PLAIN, null);

    }
}
