import java.awt.*;
import java.awt.event.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.BorderFactory;
import javax.swing.*;
import java.io.*;
import java.io.File;

public class Outline  {

public static int MAX_ENTRIES = 1000;
public static int MAX_OUTLINES = 20;
public static String DEFAULT_FILENAME = "New.otl";
public static int MAX_RECENT = 10;
//public static OutlineEntry[][] outlineArray;
public static int currentOutline = 0;
public static int panelPtr = 0;
public static int linesInCurrent = 0;
public static int windowsOpened = 0;

	public static void initOutline(OutlineEntry[][] outout)  {
		System.out.println("Initializing outline array...");
		for (int i=0; i<MAX_ENTRIES; i++)  
			for (int j=0; j<MAX_OUTLINES; j++)
			outout[j][i] = new OutlineEntry();
	}

	public static void init_outline(OutlineEntry[] inout)  {
		for (int i=0; i<MAX_ENTRIES; i++)
			inout[i] = new OutlineEntry();
	}

	public static void copyFromArray(OutlineEntry[] outsm, OutlineEntry[][] outbg, int arrayIndex)  {   
	  for (int i = 0; i < MAX_ENTRIES; i++)  
	    outsm[i] = outbg[arrayIndex][i];
      }

	public static void copyToArray(OutlineEntry[] transOutArray, OutlineEntry[][] bigArray, int arrayIndex)  {
		for (int i = 0; i < MAX_ENTRIES; i++)  {
			bigArray[arrayIndex][i] = transOutArray[i];
		}
	}
	public Outline(String cmdLineFile)  {
            if (cmdLineFile != "") {
                File cmdF = new File(cmdLineFile);
                if (cmdF != null)  {
                    OutlinePanel.setDefaultDirectory();
                    OutlinePanel.currentDirectory = OutlinePanel.defaultDirectory;
                    OutlinePanel.outlineOpen(cmdF);
                }
            }
            else {
		OutlineEntry[] outLine = new OutlineEntry[MAX_ENTRIES];
		init_outline(outLine);
                OutlinePanel.recentOpen();
		OutlineEntry[] newout = OutlineCore.createNewOutline();
                OutlinePanel.setDefaultDirectory();
                OutlinePanel.currentDirectory = OutlinePanel.defaultDirectory;
                String tempName = OutlinePanel.currentDirectory.getAbsolutePath() + File.separator + DEFAULT_FILENAME;
		OutlinePanel myWindow = new OutlinePanel(newout[0].OutlineText, newout, tempName);
		myWindow.setVisible(true);
            }
	}

	public static void main(String[] arguments)  {        
            if (arguments.length > 0)
		new Outline(arguments[0]);
            else new Outline("");
	}


}
