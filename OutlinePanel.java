import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.JMenu;
import java.awt.*; 
import java.awt.event.*;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Observer;
import java.util.Observable;
import java.io.*;
import java.io.File;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.*;
import javax.swing.SwingUtilities;
import javax.swing.event.*;

public class OutlinePanel extends JFrame implements WindowListener, KeyListener, ActionListener, MouseListener, MouseMotionListener, Observer {
  static final int dispX = 700;
  static final int dispY = 500;
  static final int rowHeight = 15;
  static final int topMargin = 30;
  static final int leftMargin = 20;
  static int outlinePointer = 0;
  static OutlineEntry[] clipBoard = new OutlineEntry[Outline.MAX_ENTRIES];
  static File currentDirectory;
  static File defaultDirectory;
  static String[] recentFiles = new String[Outline.MAX_RECENT];
  static int recentPtr = 0;
  static int showLevel = 99;
  JTextArea enterText = new JTextArea(3, 50);
  JTextArea enterText2 = new JTextArea(3, 50);
  JTextArea enterText3 = new JTextArea(1, 20);
  JTextArea enterText4 = new JTextArea(1, 20);
  JTextArea enterText5 = new JTextArea(3, 50);
  JMenuBar menuBar = new JMenuBar();
  JMenu fileMenu = new JMenu("File");
  JMenu outlineMenu = new JMenu("Outline");
  JMenu optionsMenu = new JMenu("Options");
  JMenu recentMenu = new JMenu("Open recent");
  ActionListener actionListener = new MenuActionListener();
  JMenuItem openItem = new JMenuItem("Open");
  JMenuItem newItem = new JMenuItem("New");
  JMenuItem saveItem = new JMenuItem("Save");
  JMenuItem saveAsItem = new JMenuItem("Save As");
  JMenuItem closeItem = new JMenuItem("Close");
  JMenuItem undoItem = new JMenuItem("Undo");
  JMenuItem redoItem = new JMenuItem("Redo");
  JMenuItem copyFamItem = new JMenuItem("Copy Family");
  JMenuItem copyItem = new JMenuItem("Copy");
  JMenuItem deleteFamItem = new JMenuItem("Delete Family");
  JMenuItem deleteItem = new JMenuItem("Delete");
  JMenuItem insertClipItem = new JMenuItem("Insert Family");
  JMenuItem insertItem = new JMenuItem("Insert");
  JMenuItem promoteItem = new JMenuItem("Promote (ctl-e)"); 
  JMenuItem promItem = new JMenuItem("Promote");
  JMenuItem demoteItem = new JMenuItem("Demote (ctl-r)");
  JMenuItem demItem = new JMenuItem("Demote");
  JMenuItem clipItem = new JMenuItem("Dump Clipboard");
  JMenuItem txtItem = new JMenuItem("Save .txt file");
  JMenuItem testItem = new JMenuItem("Test");
  JMenuItem helpItem = new JMenuItem("Help");
  JMenuItem aboutItem = new JMenuItem("About");
  JMenuItem showItem = new JMenuItem("Set Show Level");
  JMenuItem recentItem[] = new JMenuItem[Outline.MAX_RECENT];
  JLabel label1;
  JLabel label2;
  private JPopupMenu popup = new JPopupMenu("General");
  private JFileChooser fc;
  private File aFile = new File(Outline.DEFAULT_FILENAME);
  private int  xMouse, yMouse, panelX, panelY = 0;
  JPanel bottomRow = new JPanel();
  private String Title = "New Outline";
  private String fileName = "";
  private OutlineEntry[] OutlineEntryArray = new OutlineEntry[Outline.MAX_ENTRIES];
  private OutlineEntry[][] outStack = new OutlineEntry[Outline.MAX_OUTLINES][Outline.MAX_ENTRIES];
  private int stackPtr = 0;
  private int stackMax = stackPtr;
  private int[] focusStk = new int[Outline.MAX_OUTLINES];
  private int startNumber = 0;   //the outline index where display starts
  private int putWhere = 0;   // where on the screen the line is put
  private int focusNo = 0;
  private int dispRow = 0;
  private int familyAtDisplay = 0;
  private boolean outlineChanged = false;
  private int rowsOnScreen = 0;

  public OutlinePanel(String titl, OutlineEntry[] outlin, String fileN)  {
    super(titl);
    Title = titl;
    Toolkit theKit = this.getToolkit();        // Get the window toolkit
    Dimension wndSize = theKit.getScreenSize();  // Get screen size
    int wndFactor = 40;
    if (wndSize.width > 1100)
        wndFactor = 43;
    else
        wndFactor = 40;
    this.setBounds(wndSize.width/6+(Outline.windowsOpened*15), wndSize.height/5+(Outline.windowsOpened*15), 30*wndSize.width/wndFactor, 2*wndSize.height/3);
    Outline.initOutline(outStack);
    OutlineEntryArray = outlin;
    outlinePush();
    startNumber = 0;
    fileName = fileN;
    addWindowListener(this);	
    setBackground(Color.WHITE);
    fc = new JFileChooser();
    fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
    SignPanel sp = new SignPanel();
    sp.setBorder(BorderFactory.createLineBorder(Color.black));
    panelX = sp.getWidth();
    panelY = sp.getHeight();
    Container content = getContentPane();
    setContentPane(content);
    sp.addMouseListener(this);
    sp.addMouseMotionListener(this);
    sp.addKeyListener(this);
    sp.setVisible(true);
    Outline.init_outline(clipBoard);
    enableEvents(AWTEvent.WINDOW_EVENT_MASK);
   
//  the button bar

    JButton newButton = new JButton("New");
    newButton.addActionListener(this);
    JButton saveButton = new JButton("Save");
    saveButton.addActionListener(this);
    JButton saveAsButton = new JButton("Save As");
    saveAsButton.addActionListener(this);
    JButton deleteButton = new JButton("Delete");
    deleteButton.addActionListener(this);
    JButton copyButton = new JButton("Copy");
    copyButton.addActionListener(this);
    JButton insertButton = new JButton("Insert");
    insertButton.addActionListener(this);
    JButton openButton = new JButton("Open");
    openButton.addActionListener(this);    
    JButton promoteButton = new JButton("Promote");
    promoteButton.addActionListener(this);
    JButton demoteButton = new JButton("Demote");
    demoteButton.addActionListener(this);
    JButton pgupButton = new JButton("PgUp");
    pgupButton.addActionListener(this);
    JButton pgdnButton = new JButton("PgDn");
    pgdnButton.addActionListener(this);
    JButton lnupButton = new JButton("LnUp");
    lnupButton.addActionListener(this);
    JButton lndnButton = new JButton("LnDn");
    lndnButton.addActionListener(this);
    JButton beginButton = new JButton("Begin");
    beginButton.addActionListener(this);
    JButton endButton = new JButton("End");
    endButton.addActionListener(this);
    JButton clipButton = new JButton("Clip");
    clipButton.addActionListener(this);
    JButton undoButton = new JButton("Undo");
    undoButton.addActionListener(this);
    JButton txtButton = new JButton(".txt");
    txtButton.addActionListener(this);
    JToolBar bar = new JToolBar();
    bar.add(newButton);
    bar.add(openButton);
    bar.add(saveButton);
    bar.add(saveAsButton);
    bar.add(deleteButton);
    bar.add(copyButton);
    bar.add(insertButton);
    bar.add(promoteButton);
    bar.add(demoteButton);
    bar.add(beginButton);
    bar.add(pgupButton);
    bar.add(lnupButton);
    bar.add(lndnButton);
    bar.add(pgdnButton);
    bar.add(endButton);
    bar.add(undoButton);

// bottom bar 

    enterText.setLineWrap(true);
    enterText.setWrapStyleWord(true);
    JScrollPane bottomBar = new JScrollPane(enterText, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    enterText2.setLineWrap(true);
    enterText2.setWrapStyleWord(true);
    enterText5.setFocusable(true);
	 enterText5.setLineWrap(true);
	 enterText5.setWrapStyleWord(true);
//    enterText5.setHorizontalAlignment(JTextField.TRAILING);
    JScrollPane bottomBar2 = new JScrollPane(enterText5, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    GridLayout grid1 = new GridLayout(2,2);
    bottomRow.setLayout(grid1);
    bottomBar.setBorder(BorderFactory.createLineBorder(Color.black));
    bottomBar2.setBorder(BorderFactory.createLineBorder(Color.black));
    JLabel label1 = new JLabel("");
    JLabel label2 = new JLabel("");
    JButton modifyButton = new JButton("Modify");
    modifyButton.addActionListener(this);
    JButton enterButton = new JButton("Enter New");
    enterButton.addActionListener(this);
    enterText5.addKeyListener(this);
    enterText2.addKeyListener(this);
    bottomRow.add(bottomBar);
    bottomRow.add(bottomBar2);
//    bottomRow.add(enterText3);
    bottomRow.add(modifyButton);
//    bottomRow.add(enterButton);
    bottomRow.add(enterText4);
    
//  set up menus
    setJMenuBar(menuBar);
    menuBar.add(fileMenu);
    menuBar.add(outlineMenu);
    menuBar.add(optionsMenu);
    fileMenu.add(newItem);
    newItem.addActionListener(actionListener);
    fileMenu.add(openItem);
    openItem.addActionListener(actionListener);
    fileMenu.add(recentMenu);
    fileMenu.add(saveItem);
    saveItem.addActionListener(actionListener);
    fileMenu.add(saveAsItem);
    saveAsItem.addActionListener(actionListener);
    fileMenu.add(closeItem);
    closeItem.addActionListener(actionListener);
    fileMenu.addSeparator();
    fileMenu.add(txtItem);
    txtItem.addActionListener(actionListener);
    outlineMenu.add(undoItem);
    undoItem.addActionListener(actionListener);
    outlineMenu.add(redoItem);
    redoItem.addActionListener(actionListener);
    outlineMenu.add(copyFamItem);
    copyFamItem.addActionListener(actionListener);
    outlineMenu.add(deleteFamItem);
    deleteFamItem.addActionListener(actionListener);
    outlineMenu.add(insertClipItem);
    insertClipItem.addActionListener(actionListener);
    outlineMenu.add(promoteItem);
    promoteItem.addActionListener(actionListener);
    outlineMenu.add(demoteItem);
    demoteItem.addActionListener(actionListener);
    optionsMenu.add(clipItem);
    clipItem.addActionListener(actionListener);
    optionsMenu.add(testItem);
    testItem.addActionListener(actionListener);
    optionsMenu.add(showItem);
    showItem.addActionListener(actionListener);
    optionsMenu.addSeparator();
    optionsMenu.add(helpItem);
    helpItem.addActionListener(actionListener);
    optionsMenu.add(aboutItem);
    aboutItem.addActionListener(actionListener);
    for (int i = recentPtr -1; i >=0; i--)  {
        recentItem[i] = new JMenuItem(recentFiles[i]);
        recentMenu.add(recentItem[i]);
        recentItem[i].addActionListener(actionListener);
    }
    
// pop-up menus
    popup.add(copyItem);
    copyItem.addActionListener(actionListener);
    popup.add(deleteItem);
    deleteItem.addActionListener(actionListener);
    popup.add(insertItem);
    insertItem.addActionListener(actionListener);
    popup.add(promItem);
    promItem.addActionListener(actionListener);
    popup.add(demItem);
    demItem.addActionListener(actionListener);

//  frame layout
    content.setLayout(new BorderLayout());
    content.add("North", bar);
    content.add("South", bottomRow);
    content.add("Center", sp);
    content.setVisible(true);
    eventOutput3(" At entry: " + focusNo + "    Outlines open: " + Outline.windowsOpened);
    enterText5.requestFocusInWindow();
  }   // close OutlinePanel()

public class SignPanel extends JPanel  {
  int panelX, panelY;
 
  public SignPanel ()  {
  }
    public void paintComponent (Graphics comp)  {
      super.paintComponent(comp);
      String message = "";
      Title = OutlineEntryArray[0].OutlineText;
      setTitle(Title);
      updateEntries();
      Graphics2D comp2D = (Graphics2D) comp;
      comp2D.setColor(Color.black);
      Dimension winsize = getSize();
      panelX = winsize.width;
      panelY = winsize.height;
      familyAtDisplay = OutlineCore.countFamily(OutlineEntryArray, focusNo);
	rowsOnScreen = panelY/rowHeight - 2;
      for (int i = 0; i < rowsOnScreen; i++)  {
	 putWhere = startNumber + i;
         message = OutlineCore.headerString(OutlineEntryArray[putWhere].Rank, OutlineEntryArray[putWhere].Item);
         message = message + OutlineEntryArray[putWhere].OutlineText;
	   if ((i >= dispRow) & (i <= dispRow + familyAtDisplay)) {
               if (i == dispRow)  {
                   comp2D.setColor(Color.BLUE);
                   comp2D.drawString(message, leftMargin, (topMargin + i*rowHeight));
               }
               else  {
                 comp2D.setColor(Color.RED);
		 comp2D.drawString(message, leftMargin, (topMargin + i*rowHeight));
               }
	   }
         else {
		comp2D.setColor(Color.BLACK);
		comp2D.drawString(message, leftMargin, (topMargin + i*rowHeight));
	   }
      }  		// close for
    }  		// close paintComponent
}  			// close SignPanel

public static void setDefaultDirectory()  {
    File iFile = new File("Init.otl");
    String name = iFile.getName();
    String absPath = iFile.getAbsolutePath();
    int index = absPath.lastIndexOf(name);
    String result = absPath.substring(0, index);
    File dFile = new File(result);
    if (dFile.isDirectory())
        System.out.println(dFile + " is a directory");
    defaultDirectory = dFile;
    System.out.println("Default directory is: " + defaultDirectory);
}

public static void setNewDirectory(File pathName)  {
    String name = pathName.getName();
    String absPath = pathName.getAbsolutePath();
    int index = absPath.lastIndexOf(name);
    String result = absPath.substring(0, index);
    File dFile = new File(result);
    if (dFile.isDirectory())
        System.out.println(dFile + " is a directory");
    currentDirectory = dFile;
    System.out.println("Default directory is: " + currentDirectory);
}

public void outlinePush()  {
    if (stackPtr == Outline.MAX_OUTLINES)  {  //pointing 1 beyond end of array
        stackPtr--;
        for (int i = 0; i < stackPtr; i++)  {
            for (int k = 0; k < Outline.MAX_ENTRIES; k++)  {
                outStack[i][k].Rank = outStack[i+1][k].Rank;
                outStack[i][k].Item = outStack[i+1][k].Item;
                outStack[i][k].OutlineText = outStack[i+1][k].OutlineText;
            }
        };
        for (int j = 0; j < Outline.MAX_ENTRIES; j++)  {
            outStack[stackPtr][j].Rank = OutlineEntryArray[j].Rank;
            outStack[stackPtr][j].Item = OutlineEntryArray[j].Item;
            outStack[stackPtr][j].OutlineText = OutlineEntryArray[j].OutlineText;
        };
    }
    else if ((stackPtr >= 0 ) && (stackPtr < Outline.MAX_OUTLINES))  {
        for (int i = 0; i < Outline.MAX_ENTRIES; i++)  {
            outStack[stackPtr][i].Rank = OutlineEntryArray[i].Rank;
            outStack[stackPtr][i].Item = OutlineEntryArray[i].Item;
            outStack[stackPtr][i].OutlineText = OutlineEntryArray[i].OutlineText;
        }
    }
    focusStk[stackPtr] = focusNo;
    stackPtr++;
    stackMax = stackPtr;
}

public void outlinePop()  {
    if (stackPtr > 0)  {
        stackPtr--;
        for (int i = 0; i < Outline.MAX_ENTRIES; i++)  {
            OutlineEntryArray[i].Rank = outStack[stackPtr][i].Rank;
            OutlineEntryArray[i].Item = outStack[stackPtr][i].Item;
            OutlineEntryArray[i].OutlineText = outStack[stackPtr][i].OutlineText;
        }
        focusNo = focusStk[stackPtr];
    }
}

public void outlineRedo()  {
    if ((stackPtr > 0) && (stackPtr < stackMax))  {
        for (int i = 0; i < Outline.MAX_ENTRIES; i++)  {
            OutlineEntryArray[i].Rank = outStack[stackPtr+1][i].Rank;
            OutlineEntryArray[i].Item = outStack[stackPtr+1][i].Item;
            OutlineEntryArray[i].OutlineText = outStack[stackPtr+1][i].OutlineText;
        }
    focusNo = focusStk[stackPtr+1];
    stackPtr++;
    }
}

public void getOpenDialog()  {
    fc.setDialogTitle("Open Outline");
    fc.setCurrentDirectory(currentDirectory);
    fc.rescanCurrentDirectory();
    OutlineFilter OutlineFilter = new OutlineFilter(".otl", "Outline files (*.otl)");
    fc.addChoosableFileFilter(OutlineFilter);
    fc.setFileFilter(OutlineFilter);  
    int returnVal = fc.showOpenDialog(this);
    if (returnVal == JFileChooser.APPROVE_OPTION)  {
       File aFile = fc.getSelectedFile();
       currentDirectory = fc.getCurrentDirectory();
       if (aFile != null)
          outlineOpen(aFile);
       }
}

static public int outlineOpen(File inFile)  {
    int lineCounter = 0;
    int remainder = 0;
    int eCounter = 0;
    OutlineEntry[] outBuffer = new OutlineEntry[Outline.MAX_ENTRIES];
    Outline.init_outline(outBuffer);
    String fname = inFile.getName();

    try  {
      FileReader file = new FileReader(inFile);
      BufferedReader buffer = new BufferedReader(file);
      System.out.println("Opening " + inFile + "...");
      String outpt = "";
      while ( (outpt = buffer.readLine()) != null) {
        outpt = outpt.trim();
        remainder = lineCounter % 3;
        if (remainder == 0)
          outBuffer[eCounter].Rank = Integer.parseInt(outpt);
        else if (remainder == 1)
          outBuffer[eCounter].Item = Integer.parseInt(outpt);
        else if (remainder == 2) {
          outBuffer[eCounter].OutlineText = outpt;
          eCounter++;
         }
        lineCounter++;
      }    // close while loop
      buffer.close();
    } catch (IOException e)  {
        System.out.println("A read error has occurred..");
      }
    OutlinePanel openWindow = new OutlinePanel(outBuffer[0].OutlineText, outBuffer, inFile.getAbsolutePath());
    openWindow.setVisible(true);
    return eCounter;
  }

public static void recentOpen()  {
    File recentF = new File("outline.cfg");
    boolean fileExists = recentF.exists();
    if (fileExists == true)  {
        try  {
            FileReader file = new FileReader(recentF);
            BufferedReader buffer = new BufferedReader(file);
            int counter = 0;
            recentInit();
            String outpt = "";
            while ( (outpt = buffer.readLine()) != null) {
                outpt = outpt.trim();
                recentFiles[counter] = outpt;
                recentPtr++;
                counter++;
            }    // close while loop
        }   catch (IOException e)  {
            System.out.println("A read error has occurred..");
    }
    }
    
}

public void outlineTextSave(File outFile)  {

    PrintWriter outputStream = null;
    try  {
            try {
                outputStream = new PrintWriter(new FileWriter(outFile));
            } catch (IOException ex) {
                Logger.getLogger(OutlinePanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        int max = OutlineCore.countEntries(OutlineEntryArray);
        for (int i=0; i<max; i++)  {
            String message = OutlineCore.headerStringforPrinting(OutlineEntryArray[i].Rank, OutlineEntryArray[i].Item);
            message = message + OutlineEntryArray[i].OutlineText;
            if (OutlineEntryArray[i].Rank <= showLevel)
                outputStream.println(message);
        }
        System.out.println("Saving .txt file...");
    } finally  {
        if (outputStream != null) 
        outputStream.close();
      }
}
  public static String getExtension(File f) {
       String ext = null;
       String s = f.getName();
       int i = s.lastIndexOf('.');

       if (i > 0 &&  i < s.length() - 1) {
           ext = s.substring(i+1).toLowerCase();
       }
       return ext;
    }
  
public void recentPush(String pathName)  {
    if (recentCheckDupes(pathName) != true)  {
        if ((recentPtr >= 0) & (recentPtr < (Outline.MAX_RECENT)))  {
            recentFiles[recentPtr] = pathName;
            recentPtr++;
        }
        if (recentPtr == (Outline.MAX_RECENT))  {
            for (int i = 0; i < (Outline.MAX_RECENT-1); i++)  {
                recentFiles[i] = recentFiles[i+1];
                recentFiles[Outline.MAX_RECENT-1] = pathName;
            }
        recentPtr--;
        }
    }
}

public void recentPurge(String pathName)  {
    System.out.println("File does not exist!");
    for (int i = 0; i < Outline.MAX_RECENT; i++)  {
        if (recentFiles[i] == pathName)  {
            recentFiles[i] = recentFiles[i+1];
            recentPtr--;
            recentSave();
        }
    }
}

public static void recentInit()  {
    for (int i = 0; i < Outline.MAX_RECENT; i++)  {
        recentFiles[i] = "";
        recentPtr = 0;
    }
    System.out.println("Initializing recent file list...");
}

public void recentDump()  {
    for (int i = 0; i < recentPtr; i++)
        System.out.println("File " + i + ": " + recentFiles[i]);
}

public void recentSave()  {
    File outFile = new File("outline.cfg");
    PrintWriter outputStream = null;
    try  {
        try {
           outputStream = new PrintWriter(new FileWriter(outFile));
        } catch (IOException ex) {
           Logger.getLogger(OutlinePanel.class.getName()).log(Level.SEVERE, null, ex);
           }
        for (int i=0; i<recentPtr; i++)  {
            outputStream.println(recentFiles[i]);
        }
        System.out.println("Saving recent files...");
    } finally  {
        if (outputStream != null) 
        outputStream.close();
      }
}

public boolean recentCheckDupes(String str)  {
    boolean flipSwitch = false;
    for (int i=0; i <recentPtr; i++)  {
        int ret = str.compareTo(recentFiles[i]);
        if (ret==0)
            flipSwitch=true;
    }
    return flipSwitch;
}

public void getSaveDialog()  {
    fc.setDialogTitle("Save Outline");
    fc.setCurrentDirectory(currentDirectory);
    fc.rescanCurrentDirectory();
    OutlineFilter OutlineFilter = new OutlineFilter(".otl", "Outline files (*.otl)");
    fc.addChoosableFileFilter(OutlineFilter);
    fc.setFileFilter(OutlineFilter);
    int returnVal = fc.showSaveDialog(this);
    if (returnVal == JFileChooser.APPROVE_OPTION)  {
        File saveFile = fc.getSelectedFile();
        setNewDirectory(saveFile);
        fc.setCurrentDirectory(currentDirectory);
        String extension = getExtension(saveFile);
        if (extension == null)  {
            System.out.print("add .otl extension\n");
            String name = saveFile.getName();
            name = name + ".otl";
            File saveFile2 = new File(currentDirectory + File.separator + name);
            outlineFileExists(saveFile2);
        }
        else  {
               outlineFileExists(saveFile);
            }
        currentDirectory = fc.getCurrentDirectory();
     }
}

public void outlineFileExists(File thisFile)  {
    if (thisFile != null)  {
        if (thisFile.exists() == true)  {
          Object[] options = {"Yes",
                    "No",
                    "Cancel"};
            int n = JOptionPane.showOptionDialog(null,
            "File exists.  Overwrite?",
            "Saving File...",
            JOptionPane.YES_NO_CANCEL_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[2]);
            switch (n)  {
                case 0:
                    outlineSave(thisFile);
                    break;
                case 1:
                    break;
                case 2:
                    break;
            }
        }
        else
            outlineSave(thisFile);
    }
}
public void outlineSave(File outFile)  {
    String outRank = "";
    String outItem = "";
    String outText = "";
    PrintWriter outputStream = null;
    try  {
        try {
           outputStream = new PrintWriter(new FileWriter(outFile));
        } catch (IOException ex) {
           Logger.getLogger(OutlinePanel.class.getName()).log(Level.SEVERE, null, ex);
           }
        int max = OutlineCore.countEntries(OutlineEntryArray);
        System.out.println("Saving file "+ outFile);
        for (int i=0; i<max; i++)  {
            outRank = String.valueOf(OutlineEntryArray[i].Rank);
            outItem = String.valueOf(OutlineEntryArray[i].Item);
            outText = OutlineEntryArray[i].OutlineText;
            outputStream.println(outRank);
            outputStream.println(outItem);
            outputStream.println(outText);
        }
        String path = outFile.getAbsolutePath();
        recentPush(path);
        outlineChanged = false;
        fileName = path;
    } finally  {
        if (outputStream != null) 
        outputStream.close();
      }
}

public String extractFileName(String fname)  {
    File fileA = new File(fname);
    return(fileA.getName());
}

public String extractPath(String fname)  {
    File fileA = new File(fname);
    String name = fileA.getName();
    String path = fname.substring(0, (fname.length()-name.length()));
    return (path);
}
public void insertOneLine(String insertedText)  {
    for (int i = Outline.MAX_ENTRIES-1; i > focusNo; i--)  {
        OutlineEntryArray[i].OutlineText = OutlineEntryArray[i-1].OutlineText;
        OutlineEntryArray[i].Rank = OutlineEntryArray[i-1].Rank;
        OutlineEntryArray[i].Item = OutlineEntryArray[i-1].Item;
    }
    focusNo++;
    OutlineEntryArray[focusNo].OutlineText = insertedText;
    OutlineEntryArray[focusNo].Rank = (OutlineEntryArray[focusNo-1].Rank + 1);
    OutlineEntryArray[focusNo].Item = 1;
    OutlineCore.renumberFamily(OutlineEntryArray, focusNo);
}

    public void actionPerformed(ActionEvent evt) {
	String answer = evt.getActionCommand();
	  if (answer == "PgDn")  {
          startNumber = startNumber + 20;
	    dispRow = dispRow - 20;
          repaint();
          enterText5.requestFocusInWindow();
          }
          else if (answer == "PgUp")  {
          if (startNumber > 20)  {
              startNumber = startNumber - 20;
		  dispRow = dispRow+ 20;
		}
          else   {
              startNumber = 0;
	    }
          repaint();
          enterText5.requestFocusInWindow();
        }
        else if (answer == "LnDn")  {
          startNumber++;
          dispRow--;
          repaint();
          enterText5.requestFocusInWindow();
        }
        else if (answer == "LnUp")  {
          if (startNumber > 0)  {
            startNumber--;
		dispRow++;
	    }
          else {
            startNumber = 0;
	    }
          repaint();
          enterText5.requestFocusInWindow();
        }
        else if (answer == "Begin")  {
          startNumber = 0;
          repaint();
          enterText5.requestFocusInWindow();
        }
	  else if (answer == "End")  {
	    int entNo = OutlineCore.countEntries(OutlineEntryArray);
	    if (entNo >= rowsOnScreen)  {
	      startNumber = entNo - rowsOnScreen;
	      focusNo = startNumber + 1;
	    }
	    else
	      startNumber = 0;
	      focusNo = 1;
	    repaint();
            enterText5.requestFocusInWindow();
	  }
	  else if (answer == "New")  {
	    OutlineEntry[] newout = OutlineCore.createNewOutline();
            System.out.println("Opening new file...");
            String newFile = currentDirectory.getAbsolutePath() + File.separator + Outline.DEFAULT_FILENAME;
	    OutlinePanel thirdWindow = new OutlinePanel(newout[0].OutlineText, newout, newFile);
	    thirdWindow.setVisible(true);
	  }
          else if (answer == "Save")  {
              if (outlineChanged == true)  {
                File saveFile3 = new File(fileName);
                if (saveFile3 != null)
                    outlineSave(saveFile3);
              }
              outlineChanged = false;
              enterText5.requestFocusInWindow();
          }
          else if (answer == "Save As")  {
            getSaveDialog();
            repaint();
            enterText5.requestFocusInWindow();
          }
          else if (answer == "Open")  {
              getOpenDialog();
              repaint();
              enterText5.requestFocusInWindow();
	  }
	  else if (answer == "Insert")  {
            outlinePush();
            outlineChanged = true;
	    OutlineCore.insertClipboard(OutlineEntryArray, focusNo);
	    updateEntries();
	    repaint();
            enterText5.requestFocusInWindow();
	  }
	  else if (answer == "Delete")  {
            outlinePush();
            outlineChanged = true;
	    int rememberRank = OutlineEntryArray[focusNo].Rank;
	    OutlineCore.deleteFamily(OutlineEntryArray, focusNo);
	    if (OutlineCore.indexInbounds(focusNo) == true)  {
		 if (OutlineEntryArray[focusNo-1].Rank < rememberRank)
		focusNo = focusNo;
	    else
		focusNo--;
	    }
	    updateEntries();
	    repaint();
            enterText5.requestFocusInWindow();
	  }
	  else if (answer == "Promote")  {
            outlinePush();
            outlineChanged = true;
	    OutlineCore.promoteFamily(OutlineEntryArray, focusNo);
	    repaint();
            enterText5.requestFocusInWindow();
	  }
	  else if (answer == "Demote")  {
            outlinePush();
            outlineChanged = true;
	    OutlineCore.demoteFamily(OutlineEntryArray, focusNo);
	    repaint();
            enterText5.requestFocusInWindow();
	  }
	  else if (answer == "Modify")  {
            int r = enterText.getLineCount();
            System.out.println("Number of lines: " + r);
            if (r==1)  {
                outlinePush();
                outlineChanged = true;
                String newText = enterText.getText();
                OutlineEntryArray[focusNo].OutlineText = newText; 
            }
            else if (r==2)  {
                outlinePush();
                outlineChanged = true;
                String newText = enterText.getText();
                int k = newText.indexOf("\n");
                String firstString = newText.substring(0, k-1);
                String secondString = newText.substring(k+1);
                OutlineEntryArray[focusNo].OutlineText = firstString;
                insertOneLine(secondString);
            }
            else
                System.out.println("Too many lines in entry.");
	    repaint();
            enterText5.requestFocusInWindow();
	  }
          else if (answer == "Enter New")  {
            outlinePush();
            outlineChanged = true;
            String insertThis = enterText5.getText();
            insertOneLine(insertThis);
            enterText5.setText("");
            updateEntries();
            repaint();
          }
	  else if (answer == "Copy")  {
	    OutlineCore.copyFamily(OutlineEntryArray, focusNo);
	    repaint();
            enterText5.requestFocusInWindow();
	  }
          else if (answer == "Undo")  {
              outlinePush();
              stackPtr--;
              outlinePop();
              outlineChanged = true;
              repaint();
              enterText5.requestFocusInWindow();
          }
    }
    
    public void printTxt()  {
        fc.setDialogTitle("Save as .txt for MSWord");
        fc.setCurrentDirectory(currentDirectory);
        fc.rescanCurrentDirectory();
        OutlineFilter OutFil = new OutlineFilter(".txt", "Text files (*.txt)");
        fc.addChoosableFileFilter(OutFil);
        fc.setFileFilter(OutFil);
        int returnVal = fc.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION)  {
            File saveFile = fc.getSelectedFile();
            fc.rescanCurrentDirectory();
            currentDirectory = fc.getCurrentDirectory();
            String extension = getExtension(saveFile);
            if (extension == null)  {
                System.out.println("add .txt extension\n");
                String name = saveFile.getName();
                name = name +".txt";
                File saveFile2 = new File(currentDirectory + File.separator + name);
                outlineTextSave(saveFile2);
            }
            else {
                outlineTextSave(saveFile);
            }
            repaint();
        }
    }
    class MenuActionListener implements ActionListener  {
        public void actionPerformed(ActionEvent actionEvent)  {
            System.out.println("Selected: " + actionEvent.getActionCommand());
            if (actionEvent.getSource() == recentItem[0])  {
                File createNew = new File(recentFiles[0]);
                if (createNew.exists())  {
                    outlineOpen(createNew);
                    setNewDirectory(createNew);
                }
                else
                    recentPurge(createNew.getAbsolutePath());
                enterText5.requestFocusInWindow();
            }
            else if (actionEvent.getSource() == recentItem[1])  {
                File createNew = new File(recentFiles[1]);
                if (createNew.exists())  {
                    outlineOpen(createNew);
                    setNewDirectory(createNew);
                }
                else
                    recentPurge(createNew.getAbsolutePath());
                enterText5.requestFocusInWindow();
            }
            else if (actionEvent.getSource() == recentItem[2])  {
                File createNew = new File(recentFiles[2]);
                if (createNew.exists())  {
                    outlineOpen(createNew);
                    setNewDirectory(createNew);
                }
                else
                    recentPurge(createNew.getAbsolutePath());
                enterText5.requestFocusInWindow();
            }
            else if (actionEvent.getSource() == recentItem[3])  {
                File createNew = new File(recentFiles[3]);
                if (createNew.exists())  {
                    outlineOpen(createNew);
                    setNewDirectory(createNew);
                }
                else
                    recentPurge(createNew.getAbsolutePath());
                enterText5.requestFocusInWindow();
            }
            else if (actionEvent.getSource() == recentItem[4])  {
                File createNew = new File(recentFiles[4]);
                if (createNew.exists())  {
                    outlineOpen(createNew);
                    setNewDirectory(createNew);
                }
                else
                    recentPurge(createNew.getAbsolutePath());
                enterText5.requestFocusInWindow();
            }
            else if (actionEvent.getSource() == recentItem[5])  {
                File createNew = new File(recentFiles[5]);
                if (createNew.exists())  {
                    outlineOpen(createNew);
                    setNewDirectory(createNew);
                }
                else
                    recentPurge(createNew.getAbsolutePath());
                enterText5.requestFocusInWindow();
            }
            else if (actionEvent.getSource() == recentItem[6])  {
                File createNew = new File(recentFiles[6]);
                if (createNew.exists())  {
                    outlineOpen(createNew);
                    setNewDirectory(createNew);
                }
                else
                    recentPurge(createNew.getAbsolutePath());
                enterText5.requestFocusInWindow();
            }
            else if (actionEvent.getSource() == recentItem[7])  {
                File createNew = new File(recentFiles[7]);
                if (createNew.exists())  {
                    outlineOpen(createNew);
                    setNewDirectory(createNew);
                }
                else
                    recentPurge(createNew.getAbsolutePath());
                enterText5.requestFocusInWindow();
            }
            else if (actionEvent.getSource() == recentItem[8])  {
                File createNew = new File(recentFiles[8]);
                if (createNew.exists())  {
                    outlineOpen(createNew);
                    setNewDirectory(createNew);
                }
                else
                    recentPurge(createNew.getAbsolutePath());
                enterText5.requestFocusInWindow();
            }
            else if (actionEvent.getSource() == recentItem[9])  {
                File createNew = new File(recentFiles[9]);
                if (createNew.exists())  {
                    outlineOpen(createNew);
                    setNewDirectory(createNew);
                }
                else
                    recentPurge(createNew.getAbsolutePath());
                enterText5.requestFocusInWindow();
            }
            else if (actionEvent.getActionCommand() == "Undo")  {
              outlinePush();
              stackPtr--;
              outlinePop();
              outlineChanged = true;
              repaint();
              enterText5.requestFocusInWindow();
            }      
            else if (actionEvent.getActionCommand() == "Redo")  {
                outlineRedo();
                outlineChanged = true;
                repaint();
                enterText5.requestFocusInWindow();
            }
            else if (actionEvent.getActionCommand() == "New")  {
                OutlineEntry[] newout = OutlineCore.createNewOutline();
                String newFile = currentDirectory.getAbsolutePath() + File.separator + Outline.DEFAULT_FILENAME;
                OutlinePanel thirdWindow = new OutlinePanel(newout[0].OutlineText, newout, newFile);
                System.out.println("Opening new file...");
                thirdWindow.setVisible(true);
                enterText5.requestFocusInWindow();
            }
            else if (actionEvent.getActionCommand() == "Help")  {
                String homePath = defaultDirectory.getAbsolutePath();
                homePath = homePath + File.separator + "Instructions.otl";
                System.out.println("Opening help file at: " + homePath);
                File helpFile = new File(homePath);
                if (helpFile.exists()) 
                    outlineOpen(helpFile);
                else
                    System.out.println("Help file missing!");
            }
            else if (actionEvent.getActionCommand() == "Save As")  {
                getSaveDialog();
                repaint();
                enterText5.requestFocusInWindow();
            }
            else if (actionEvent.getActionCommand() == "Save")  {
              if (outlineChanged == true)  {
                File saveFile3 = new File(fileName);
                if (saveFile3 != null)  {
                    outlineSave(saveFile3);
                }
              }
              outlineChanged = false;
              enterText5.requestFocusInWindow();
            }
            else if (actionEvent.getActionCommand() == "Delete Family")  {
                outlinePush();
                outlineChanged = true;
                int rememberRank = OutlineEntryArray[focusNo].Rank;
                OutlineCore.deleteFamily(OutlineEntryArray, focusNo);
                if (OutlineCore.indexInbounds(focusNo) == true)  {
                    if (OutlineEntryArray[focusNo-1].Rank < rememberRank)
                    focusNo = focusNo;
                else
                    focusNo--;
	    }
	    updateEntries();
	    repaint();
            enterText5.requestFocusInWindow();
            }
            else if (actionEvent.getActionCommand() == "Delete")  {
                outlinePush();
                outlineChanged = true;
                int rememberRank = OutlineEntryArray[focusNo].Rank;
                OutlineCore.deleteFamily(OutlineEntryArray, focusNo);
                if (OutlineCore.indexInbounds(focusNo) == true)  {
                    if (OutlineEntryArray[focusNo-1].Rank < rememberRank)
                    focusNo = focusNo;
                else
                    focusNo--;
	    }
	    updateEntries();
	    repaint();
            enterText5.requestFocusInWindow();
            }
            else if (actionEvent.getActionCommand() == "Insert Family")  {
                outlinePush();
                outlineChanged = true;
                OutlineCore.insertClipboard(OutlineEntryArray, focusNo);
                updateEntries();
                repaint();
                enterText5.requestFocusInWindow();
            }
            else if (actionEvent.getActionCommand() == "Insert")  {
                outlinePush();
                outlineChanged = true;
                OutlineCore.insertClipboard(OutlineEntryArray, focusNo);
                updateEntries();
                repaint();
                enterText5.requestFocusInWindow();
            }
            else if (actionEvent.getSource() == promoteItem)  {
                outlinePush();
                outlineChanged = true;
                OutlineCore.promoteFamily(OutlineEntryArray, focusNo);
                repaint();
                enterText5.requestFocusInWindow();
            }
            else if (actionEvent.getSource() == promItem)  {
                outlinePush();
                outlineChanged = true;
                OutlineCore.promoteFamily(OutlineEntryArray, focusNo);
                repaint();
                enterText5.requestFocusInWindow();
            }
            else if (actionEvent.getSource() == demoteItem)  {
                outlinePush();
                outlineChanged = true;
                OutlineCore.demoteFamily(OutlineEntryArray, focusNo);
                repaint();
                enterText5.requestFocusInWindow();
            }
            else if (actionEvent.getSource() == demItem)  {
                outlinePush();
                outlineChanged = true;
                OutlineCore.demoteFamily(OutlineEntryArray, focusNo);
                repaint();
                enterText5.requestFocusInWindow();
            }
            else if (actionEvent.getActionCommand() == "Copy Family")  {
                OutlineCore.copyFamily(OutlineEntryArray, focusNo);
                repaint();
                enterText5.requestFocusInWindow();
            }
            else if (actionEvent.getActionCommand() == "Copy")  {
                OutlineCore.copyFamily(OutlineEntryArray, focusNo);
                repaint();
                enterText5.requestFocusInWindow();
            }
            else if (actionEvent.getActionCommand() == "Save .txt file")  {
                printTxt();
                System.out.println("Saving .txt file...");
                enterText5.requestFocusInWindow();
            }
            else if (actionEvent.getActionCommand() == "About")  {
                JOptionPane.showMessageDialog(null, "Outline Editor\nJava Version 1.0 (rev. 9-20-2008)\n(c) Francis Fukuyama", "Outline", JOptionPane.INFORMATION_MESSAGE);
                enterText5.requestFocusInWindow();
            }
            else if (actionEvent.getActionCommand() == "Dump Clipboard")  {
                System.out.println("Clipboard contents:  \n");
                for (int i=0; i<OutlineCore.clipCount; i++)  {
                    System.out.println("Line: " + i + " " + clipBoard[i].OutlineText);
                }
                enterText5.requestFocusInWindow();
            }
            else if (actionEvent.getActionCommand() == "Open")  {
                getOpenDialog();
                repaint();
                enterText5.requestFocusInWindow();
            }
            else if (actionEvent.getActionCommand() == "Close")  {
                if (outlineChanged == true)  {
                    getSaveDialog();
                    dispose();
                }
                else
                dispose();
                enterText5.requestFocusInWindow();
            }
            else if (actionEvent.getActionCommand() == "Set Show Level")  {
                JDialog frame = new JDialog();
//                System.out.println("This is: " + this);
                OutlineShow saluton = new OutlineShow(frame, "Show Level", true);
                System.out.println("Parent: " + this);
            }
            else if (actionEvent.getActionCommand() == "Test")  {
 //               OutlineInfo infopane = new outlineInfo("Information");
  //              infopane.setVisible(true);
                
                Object pr = getParent();
                System.out.println("Parent: " + pr);
  //              repaint();
                

                enterText5.requestFocusInWindow();
            }
        }
    }
    
    public void mouseMoved(MouseEvent e)   {
      xMouse = e.getX();
      yMouse = e.getY();
    }
    
    public void mouseWheel(MouseEvent e)  {
        xMouse = e.getX();
        yMouse = e.getY();
        System.out.println("x: " + xMouse + " y: " + yMouse);
    }
    public void windowClosing(WindowEvent e) {

        if (outlineChanged == true)  {
            getSaveDialog();
            recentSave();
            dispose();
        }
        else
            recentSave();
            dispose();
    }

    public void windowOpened(WindowEvent e) {
        Outline.windowsOpened++;
    }
    public void windowActivated(WindowEvent e) {
        
    }
    public void windowIconified(WindowEvent e) {}
    public void windowDeiconified(WindowEvent e) {}
    public void windowDeactivated(WindowEvent e) {}
    
    public void windowClosed(WindowEvent e) {
        Outline.windowsOpened--;
        if (Outline.windowsOpened == 0)
            System.exit(0);
    }

    public void update(Observable o, Object obj)  {
        outlineChanged = true;
    }

    public void eventOutput3(String eventDescription)  {
      enterText3.setText(eventDescription);
      enterText3.setVisible(true);
      enterText3.setEditable(false);
    }

    public void eventOutput4(String eventDescription)  {
      enterText4.setText(eventDescription);
      enterText4.setVisible(true);
      enterText4.setEditable(false);
    }

    public void updateEntries()  {
       int totalLines = OutlineCore.countEntries(OutlineEntryArray);
       eventOutput4(" At entry: " + focusNo + "    Outlines open: " + Outline.windowsOpened + "   Total entries: " + totalLines + 
               "\n " + extractFileName(fileName) + "     Rank: " + OutlineEntryArray[focusNo].Rank + "\n " + extractPath(fileName) );
    }


    public void mouseDragged(MouseEvent e) {
        
    }
    
    public void mousePressed(MouseEvent e) {
          if (e.isPopupTrigger() || (e.getButton()==MouseEvent.BUTTON3))  {
              popup.show(this, xMouse+40, yMouse+30);
          }
	  int offset = topMargin - rowHeight;
	  dispRow = ((yMouse - offset) / rowHeight);
	  focusNo = startNumber + dispRow;
          enterText.setText(OutlineEntryArray[focusNo].OutlineText);
	  eventOutput3(" At entry: " + focusNo + "     Outlines open: " + Outline.windowsOpened);
	  updateEntries();
	  repaint();
          enterText5.requestFocusInWindow();
    }
    
    public void mouseReleased(MouseEvent e) {
        String transmit = "Mouse released (# of clicks: "
                + e.getClickCount() + ")";
    }
    
    public void mouseEntered(MouseEvent e) {
        String transmit = "Mouse entered";
    }
    
    public void mouseExited(MouseEvent e) {
        String transmit = "Mouse exited";
    }
    
    public void mouseClicked(MouseEvent e) {
        String transmit = "Mouse clicked (# of clicks: "
                + e.getClickCount() + ")";
    }
    public void keyTyped(KeyEvent input)  {
        int c = input.getKeyChar();
//        System.out.println(c);
        if (c == 10)  {
            String insertThis = enterText5.getText();
				int p = insertThis.length();
				if (p >0 )  {
					p = p - 1;
					insertThis = insertThis.substring(0,p);
				}
            int j = insertThis.compareTo("");
            if (j != 0) 
                if (insertThis != null)  {
                    outlinePush();
                    insertOneLine(insertThis);
                    outlineChanged = true;
                }
            enterText5.setText("");
            updateEntries();
            repaint();
        }
        else if (c == 14)  {
            String newText = JOptionPane.showInputDialog(null, "New Entry: ", "New", JOptionPane.INFORMATION_MESSAGE);
            int k = newText.compareTo("");
            if (k != 0) 
                if (newText != null)  {
                    outlinePush();
                    insertOneLine(newText);
                    outlineChanged = true;
                }
            enterText5.setText("");
            updateEntries();
            repaint();
        }
        else if (c==19)  {
            if (outlineChanged == true)  {
                File saveFile3 = new File(fileName);
                if (saveFile3 != null)  {
                    outlineSave(saveFile3);
                }
             }
             outlineChanged = false;
        }
        else if (c==5)  {
            outlinePush();
            outlineChanged = true;
	    OutlineCore.promoteFamily(OutlineEntryArray, focusNo);
	    repaint();
            enterText5.requestFocusInWindow();
        }
        else if (c==18)  {
            outlinePush();
            outlineChanged = true;
	    OutlineCore.demoteFamily(OutlineEntryArray, focusNo);
	    repaint();
            enterText5.requestFocusInWindow();
        }
    }
    public void keyPressed(KeyEvent txt)  {
        int c = txt.getKeyChar();
//        System.out.println("Key: " + c);
    }
    public void keyReleased(KeyEvent txt)  {
        
    }

}