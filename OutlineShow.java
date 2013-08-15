
import javax.swing.*;
import javax.swing.JOptionPane;
import javax.swing.JDialog;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.BoxLayout;
import javax.swing.Box;
import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import javax.swing.JFrame;
import java.beans.*; //Property change stuff
import java.awt.*;
import java.awt.event.*;

public class OutlineShow extends JDialog implements ActionListener {
    public OutlineShow(Dialog parent, String title, boolean md)  {
        setSize(150, 360);
        setLocation(300, 200);
        setTitle(title);
        parent.setModal(true);
//        this.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        this.setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
        Container pane = getContentPane();
        final int numButtons = 10;
        String currentLevel = "Current level: " + String.valueOf(OutlinePanel.showLevel);
        final JLabel label = new JLabel(currentLevel);
        JRadioButton[] radioButtons = new JRadioButton[numButtons];
        final ButtonGroup group = new ButtonGroup();
        JButton selectButton = new JButton("Set");
        JButton closeButton = new JButton("Close");
        closeButton.setActionCommand("Close");
        closeButton.addActionListener(this);
        radioButtons[0] = new JRadioButton("0");
        radioButtons[0].setActionCommand("0");
        radioButtons[1] = new JRadioButton("1");
        radioButtons[1].setActionCommand("1");
        radioButtons[2] = new JRadioButton("2");
        radioButtons[2].setActionCommand("2");
        radioButtons[3] = new JRadioButton("3");
        radioButtons[3].setActionCommand("3");
        radioButtons[4] = new JRadioButton("4");
        radioButtons[4].setActionCommand("4");
        radioButtons[5] = new JRadioButton("5");
        radioButtons[5].setActionCommand("5");
        radioButtons[6] = new JRadioButton("6");
        radioButtons[6].setActionCommand("6");
        radioButtons[7] = new JRadioButton("7");
        radioButtons[7].setActionCommand("7");
        radioButtons[8] = new JRadioButton("8");
        radioButtons[8].setActionCommand("8");
        radioButtons[9] = new JRadioButton("All");
        radioButtons[9].setActionCommand("All");
        for (int i = 0; i < numButtons; i++)  {
            group.add(radioButtons[i]);
        }
        radioButtons[9].setSelected(true);
        radioButtons[0].setVisible(false);
        selectButton.addActionListener(new ActionListener()  {
        public void actionPerformed(ActionEvent e)  {
           String command = group.getSelection().getActionCommand();
                if (command == "0") {
                    OutlinePanel.showLevel = 0;
                    label.setText("Current level: "+String.valueOf(OutlinePanel.showLevel));
                }
                else if (command == "1") {
                    OutlinePanel.showLevel = 1;
                    label.setText("Current level: "+String.valueOf(OutlinePanel.showLevel));
                }
                else if (command == "2")  {
                    OutlinePanel.showLevel = 2;
                    label.setText("Current level: "+String.valueOf(OutlinePanel.showLevel));
                }
                else if (command == "3")  {
                    OutlinePanel.showLevel = 3;
                    label.setText("Current level: "+String.valueOf(OutlinePanel.showLevel));
                }
                else if (command == "4")  {
                    OutlinePanel.showLevel = 4;
                    label.setText("Current level: "+String.valueOf(OutlinePanel.showLevel));
                }
                else if (command == "5")  {
                    OutlinePanel.showLevel = 5;
                    label.setText("Current level: "+String.valueOf(OutlinePanel.showLevel));
                }
                else if (command == "6")  {
                    OutlinePanel.showLevel = 6;
                    label.setText("Current level: "+String.valueOf(OutlinePanel.showLevel));
                }
                else if (command == "7")  {
                    OutlinePanel.showLevel = 7;
                    label.setText("Current level: "+String.valueOf(OutlinePanel.showLevel));
                }
                else if (command == "8")  {
                    OutlinePanel.showLevel = 8;
                    label.setText("Current level: "+String.valueOf(OutlinePanel.showLevel));
                }
                else if (command == "9")  {
                    OutlinePanel.showLevel = 9;
                    label.setText("Current level: "+String.valueOf(OutlinePanel.showLevel));
                }
                else if (command == "All")  {
                    OutlinePanel.showLevel = 99;
                    label.setText("Current level: "+String.valueOf(OutlinePanel.showLevel));
                }
            }
        }
            );
        JPanel box = new JPanel();
        box.setLayout(new BoxLayout(box, BoxLayout.PAGE_AXIS));
        box.add(label);
        Box right = Box.createVerticalBox();
        
        for (int i = 0; i < numButtons; i++) { 
            right.createVerticalStrut(30);
            right.add(radioButtons[i]);
        }
        box.add(right);
//        JPanel panelD = new JPanel(new BorderLayout());
        pane.add(box, BorderLayout.NORTH);
        pane.add(selectButton, BorderLayout.CENTER);
        pane.add(closeButton, BorderLayout.SOUTH);
        pane.setVisible(true);
    }
        public void actionPerformed(ActionEvent e)  {
            String cmd = e.getActionCommand();
            if (cmd == "Close")  {
                this.setVisible(false);
                this.dispose();
                System.out.println("Show level: " + OutlinePanel.showLevel);
            }
    }
}
