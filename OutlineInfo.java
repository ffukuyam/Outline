import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class OutlineInfo extends JDialog {
    JPanel infoPane = new JPanel();
    JLabel label1 = new JLabel("Name of Outline: ");  
    private void OutlineInfo()  {
//        super(this);
        setSize(800, 600);
        setDefaultLookAndFeelDecorated(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        
    }
    
}
//public void actionPerformed(ActionEvent evt) {
//    String answer = evt.getActionCommand();
//}