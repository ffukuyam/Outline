public class OutlineCore  {

private static final String periodSpace = ". ";
private static final String closeParenSpace = ") ";
private static final String semiSpace = ": ";
private static final String None = "";
private static final String indent = "   ";
private static final String dash = "-- ";
private static final String dot = "o ";

private static final String IR = "I";
private static final String II = "II";
private static final String III = "III";
private static final String IV = "IV";
private static final String VR = "V";
private static final String VI = "VI";
private static final String VII = "VII";
private static final String VIII = "VIII";
private static final String IX = "IX";
private static final String XR = "X";
private static final String XI = "XI";
private static final String XII = "XII";
private static final String XIII = "XIII";
private static final String XIV = "XIV";
private static final String XV = "XV";
private static final String XVI = "XVI";
private static final String XVII = "XVII";
private static final String XVIII = "XVIII";
private static final String XIX = "XIX";
private static final String XX = "XX";
private static final String XXI = "XXI";

private static final String A = "A";
private static final String B = "B";
private static final String C = "C";
private static final String D = "D";
private static final String E = "E";
private static final String F = "F";
private static final String G = "G";
private static final String H = "H";
private static final String I = "I";
private static final String J = "J";
private static final String K = "K";
private static final String L = "L";
private static final String M = "M";
private static final String N = "N";
private static final String O = "O";
private static final String P = "P";
private static final String Q = "Q";
private static final String R = "R";
private static final String S = "S";
private static final String T = "";
private static final String U = "U";
private static final String V = "V";
private static final String W = "W";
private static final String X = "X";
private static final String Y = "Y";
private static final String Z = "Z";

private static final String a = "a";
private static final String b = "b";
private static final String c = "c";
private static final String d = "d";
private static final String e = "e";
private static final String f = "f";
private static final String g = "g";
private static final String h = "h";
private static final String i = "i";
private static final String j = "j";
private static final String k = "k";
private static final String l = "l";
private static final String m = "m";
private static final String n = "n";
private static final String o = "o";
private static final String p = "p";
private static final String q = "q";
private static final String r = "r";
private static final String s = "s";
private static final String t = "t";
private static final String u = "u";
private static final String v = "v";
private static final String w = "w";
private static final String x = "x";
private static final String y = "y";
private static final String z = "z";

public static int clipCount = 0;

public static OutlineEntry[] createNewOutline()  {
	OutlineEntry[] newOut = new OutlineEntry[Outline.MAX_ENTRIES];
	Outline.init_outline(newOut);
	newOut[0].Item = 0;
	newOut[0].Rank = 0;
	newOut[0].OutlineText = "New Outline";
	return(newOut);
}

public static void resetOutline(OutlineEntry[] outEnt1)  {
	for (int i=0; i<Outline.MAX_ENTRIES; i++)
	   outEnt1[i].OutlineText = "";
}

public static int countEntries(OutlineEntry[] outE2)  {
	int count = 0;
	while (outE2[count].OutlineText != "")  {
	   count++;	}
	return(count);
}

public static int countFamily(OutlineEntry[] outE, int focNo)  {
	int index = focNo + 1;
	int counter = 0;
	if (indexInbounds(index) == true)  {
	  while (outE[index].Rank > outE[focNo].Rank)  {
	  	counter++;
	  	index++;
	  }
	}
	return counter;
}

public static boolean isChild(OutlineEntry[] outE3, int child)  {
	int parent = 0;
	if (child > 0)
	   parent = child - 1;  //look at parent
	else parent = child;
	if (outE3[child].Rank > outE3[parent].Rank)
	   return true;
	else
	   return false;
}

public static boolean indexInbounds(int indexno)  {
	if ((indexno >= 0) & (indexno < Outline.MAX_ENTRIES))
	    return true;
	else
	    return false;
}

public static void reItemizeUp(OutlineEntry[] outEn, int fcs) {
	int newSib = fcs;
	while (findNextSibling(outEn, newSib) != -1)  {
	   newSib = findNextSibling(outEn, newSib);
	   outEn[newSib].Item--;
	}  
}

public static void reItemizeDown(OutlineEntry[] outEn4, int fcs) {
	int newSib = fcs;
	while (findNextSibling(outEn4, newSib) != -1)  {
	   newSib = findNextSibling(outEn4, newSib);
	   outEn4[newSib].Item++;
	}  
}

public static void renumberFamily(OutlineEntry[] outEn, int focus)  {
    int paren = findParent(outEn, focus);
    int renum = 1;
    int newSib = paren+1;
    outEn[paren+1].Item = renum;
    while (findNextSibling(outEn, newSib) != -1)  {
        newSib = findNextSibling(outEn, newSib);
        renum++;
        outEn[newSib].Item = renum;
    }
}
public static void promoteFamily(OutlineEntry[] outEn, int focus)  {
    int findFam = countFamily(outEn, focus);
    int par = findParent(outEn, focus);
    int prevItem = outEn[par].Item;
    int endParPoint = findNextSibling(outEn, par);
    if (endParPoint == -1) 
        endParPoint = par + countFamily(outEn, par) + 1;
    int endFamilyPoint = findNextSibling(outEn, focus);     
    if (endFamilyPoint == -1)  {   //if the family has no sibling, promote in place
        if (outEn[focus].Rank > 1)  {
            for (int m = 0; m <= findFam; m++) 
                outEn[focus+m].Rank--; 
            renumberFamily(outEn, focus);
            return;
        }  //end if Rank
     }   //family has no sibling
     else   {    //family has a sibling          
        OutlineEntry[] temBuf = new OutlineEntry[Outline.MAX_ENTRIES];
        Outline.init_outline(temBuf);
        int bufPtr = 0;
        if (outEn[focus].Rank > 1)  {
            for (int i=0; i<=findFam; i++)  {
                outEn[focus+i].Rank--;
                temBuf[i].Rank = outEn[focus+i].Rank;
                temBuf[i].Item = outEn[focus+i].Item;
                temBuf[i].OutlineText = outEn[focus+i].OutlineText;
                bufPtr++;
            }        //increases rank of everyone in family, puts it in buffer
            for (int j=0; j< endParPoint - focus; j++)  {
                outEn[focus+j].Rank = outEn[focus+j+findFam+1].Rank;
                outEn[focus+j].Item = outEn[focus+j+findFam+1].Item;
                outEn[focus+j].OutlineText = outEn[focus+j+findFam+1].OutlineText;
            }     //moves everything up
            outEn[focus].Item--;
            temBuf[0].Item = prevItem + 1;
            int l = endParPoint-(findFam+1);
            for (int k = 0; k < bufPtr; k++)  {
                outEn[l+k].Rank = temBuf[k].Rank;
                outEn[l+k].Item = temBuf[k].Item;
                outEn[l+k].OutlineText = temBuf[k].OutlineText;
            }
            renumberFamily(outEn, l);
        }   //  end Rank > 1
        renumberFamily(outEn, focus);
        if (isChild(outEn, focus+1) == true)
            renumberFamily(outEn, focus+1);
    }   // end else
}

public static void demoteFamily(OutlineEntry[] outEn, int focus)  {
	int findFam = countFamily(outEn, focus);
	if ((outEn[focus].Rank > 0) & (outEn[focus-1].Rank >= outEn[focus].Rank))  {
	   for (int i=0; i<=findFam; i++)  {
		outEn[focus+i].Rank++;
	   }
	}
        if (outEn[focus-1].Rank < outEn[focus].Rank)
            outEn[focus].Item = 1;
        else if (outEn[focus-1].Rank == outEn[focus].Rank)
            outEn[focus].Item = outEn[focus-1].Item + 1;
        renumberFamily(outEn, focus);
        int paren = findParent(outEn, focus);
        renumberFamily(outEn, paren);
}

public static void deleteFamily(OutlineEntry[] outEn6, int focus)  {
	int numFam = countFamily(outEn6, focus);

        clipCount = 0;
	for (int i=0; i <= numFam; i++)   {    //copies deleted to clipboard
		OutlinePanel.clipBoard[i].Rank = outEn6[focus+i].Rank;
                OutlinePanel.clipBoard[i].Item = outEn6[focus+i].Item;
                OutlinePanel.clipBoard[i].OutlineText = outEn6[focus+i].OutlineText;
                clipCount++;
	} 
	reItemizeUp(outEn6, focus); 
	for (int k=focus; k < (Outline.MAX_ENTRIES-numFam-2); k++)  {
		outEn6[k].Rank = outEn6[k+numFam+1].Rank;
                outEn6[k].Item = outEn6[k+numFam+1].Item;
                outEn6[k].OutlineText = outEn6[k+numFam+1].OutlineText;
	}
}

public static void copyFamily(OutlineEntry[] outEn7, int focs)  {
	int numFam = countFamily(outEn7, focs);
        clipCount = 0;
	for (int i=0; i <= numFam; i++)   {
		OutlinePanel.clipBoard[i].Rank = outEn7[focs+i].Rank;
                OutlinePanel.clipBoard[i].Item = outEn7[focs+i].Item;
                OutlinePanel.clipBoard[i].OutlineText = outEn7[focs+i].OutlineText;
                clipCount++;
	}
}

public static void insertClipboard(OutlineEntry[] outEn8, int focus)  {
	int rankAtFocus = outEn8[focus].Rank;

	for (int p = Outline.MAX_ENTRIES-1; p >focus; p--)  {
            if (p - clipCount >= 0)  {
                outEn8[p].Rank = outEn8[p-clipCount].Rank;
                outEn8[p].Item = outEn8[p-clipCount].Item;
                outEn8[p].OutlineText = outEn8[p-clipCount].OutlineText;
            }
	}
	int relRank = OutlinePanel.clipBoard[0].Rank - rankAtFocus;
	for (int j = 0; j<clipCount; j++)  {
	   OutlinePanel.clipBoard[j].Rank = OutlinePanel.clipBoard[j].Rank - relRank + 1;
	}
        OutlinePanel.clipBoard[0].Item = 1;
	for (int k = 0; k < clipCount; k++)  {
	   outEn8[focus+1+k].Rank = OutlinePanel.clipBoard[k].Rank;
           outEn8[focus+1+k].Item = OutlinePanel.clipBoard[k].Item;
           outEn8[focus+1+k].OutlineText = OutlinePanel.clipBoard[k].OutlineText;
        }
        renumberFamily(outEn8, focus);
}

public static int findParent(OutlineEntry[] outE9, int fcos)  {
	int i = fcos;
	if (i<=0)  
	   System.out.println("No parent!");
	else {
	   while (outE9[i].Rank >= outE9[fcos].Rank)  {
	      i--;          
	   }
	}
	return i;
}

public static int findNextSibling(OutlineEntry[] outE0, int fc)  {
	int found = -1;
	int fam = countFamily(outE0, fc);
	if (outE0[fc + fam+1].Rank == outE0[fc].Rank)
	   found = fc + fam + 1;
	else
	   found = -1;
	return(found);
}

  public static String headerStringforPrinting(int rankNo, int itemNo)  {
    String returnString = "";
    if ((rankNo == 0) & (itemNo == 0))
      returnString = ".mh ";
    else if ((rankNo == 1) & (itemNo == 1))
      returnString = ".rf1 " + IR + periodSpace;
    else if ((rankNo == 1) & (itemNo == 2))
      returnString = ".rf1 " + II + periodSpace;
    else if ((rankNo == 1) & (itemNo == 3))
      returnString = ".rf1 " + III + periodSpace;
    else if ((rankNo == 1) & (itemNo == 4))
      returnString = ".rf1 " + IV + periodSpace;
    else if ((rankNo == 1) & (itemNo == 5))
      returnString = ".rf1 " + VR + periodSpace;
    else if ((rankNo == 1) & (itemNo == 6))
      returnString = ".rf1 " + VI + periodSpace;
    else if ((rankNo == 1) & (itemNo == 7))
      returnString = ".rf1 " + VII + periodSpace;
    else if ((rankNo == 1) & (itemNo == 8))
      returnString = ".rf1 " + VIII + periodSpace;
    else if ((rankNo == 1) & (itemNo == 9))
      returnString = ".rf1 " + IX + periodSpace;
    else if ((rankNo == 1) & (itemNo == 10))
      returnString = ".rf1 " + XR + periodSpace;
    else if ((rankNo == 1) & (itemNo == 11))
      returnString = ".rf1 " + XI + periodSpace;
    else if ((rankNo == 1) & (itemNo == 12))
      returnString = ".rf1 " + XII + periodSpace;
    else if ((rankNo == 1) & (itemNo == 13))
      returnString = ".rf1 " + XIII + periodSpace;
    else if ((rankNo == 1) & (itemNo == 14))
      returnString = ".rf1 " + XIV + periodSpace;
    else if ((rankNo == 1) & (itemNo == 15))
      returnString = ".rf1 " + XV + periodSpace;
    else if ((rankNo == 1) & (itemNo == 16))
      returnString = ".rf1 " + XVI + periodSpace;
    else if ((rankNo == 1) & (itemNo == 17))
      returnString = ".rf1 " + XVII + periodSpace;
    else if ((rankNo == 1) & (itemNo == 18))
      returnString = ".rf1 " + XVIII + periodSpace;
    else if ((rankNo == 1) & (itemNo == 19))
      returnString = ".rf1 " + XIX + periodSpace;
    else if ((rankNo == 1) & (itemNo == 20))
      returnString = ".rf1 " + XX + periodSpace;
    else if ((rankNo == 1) & (itemNo == 21))
      returnString = ".rf1 " + XI + periodSpace;
    else if ((rankNo == 2) & (itemNo == 1))
      returnString =  ".rf2 " + A + closeParenSpace;
    else if ((rankNo == 2) & (itemNo == 2))
      returnString = ".rf2 " + B + closeParenSpace;
    else if ((rankNo == 2) & (itemNo == 3))
      returnString = ".rf2 " + C + closeParenSpace;
    else if ((rankNo == 2) & (itemNo == 4))
      returnString = ".rf2 " + D + closeParenSpace;
    else if ((rankNo == 2) & (itemNo == 5))
      returnString = ".rf2 " + E + closeParenSpace;
    else if ((rankNo == 2) & (itemNo == 6))
      returnString = ".rf2 " + F + closeParenSpace;
    else if ((rankNo == 2) & (itemNo == 7))
      returnString = ".rf2 " + G + closeParenSpace;
    else if ((rankNo == 2) & (itemNo == 8))
      returnString = ".rf2 " + H + closeParenSpace;
    else if ((rankNo == 2) & (itemNo == 9))
      returnString = ".rf2 " + I + closeParenSpace;
    else if ((rankNo == 2) & (itemNo == 10))
      returnString = ".rf2 " + J + closeParenSpace;
    else if ((rankNo == 2) & (itemNo == 11))
      returnString = ".rf2 " + K + closeParenSpace;
    else if ((rankNo == 2) & (itemNo == 12))
      returnString = ".rf2 " + L + closeParenSpace;
    else if ((rankNo == 2) & (itemNo == 13))
      returnString = ".rf2 " + M + closeParenSpace;
    else if ((rankNo == 2) & (itemNo == 14))
      returnString = ".rf2 " + N + closeParenSpace;
    else if ((rankNo == 2) & (itemNo == 15))
      returnString = ".rf2 " + O + closeParenSpace;
    else if ((rankNo == 2) & (itemNo == 16))
      returnString = ".rf2 " + P + closeParenSpace;
    else if ((rankNo == 2) & (itemNo == 17))
      returnString = ".rf2 " + Q + closeParenSpace;
    else if ((rankNo == 2) & (itemNo == 18))
      returnString = ".rf2 " + R + closeParenSpace;
    else if ((rankNo == 2) & (itemNo == 19))
      returnString = ".rf2 " + S + closeParenSpace;
    else if ((rankNo == 2) & (itemNo == 20))
      returnString = ".rf2 " + T + closeParenSpace;
    else if (rankNo == 3)
      returnString = ".rf3 " + String.valueOf(itemNo) + semiSpace;
    else if ((rankNo == 4) & (itemNo == 1))
      returnString = ".rf4 " + a + periodSpace;
    else if ((rankNo == 4) & (itemNo == 2))
      returnString = ".rf4 " + b + periodSpace;
    else if ((rankNo == 4) & (itemNo == 3))
      returnString = ".rf4 " + c + periodSpace;
    else if ((rankNo == 4) & (itemNo == 4))
      returnString = ".rf4 " + d + periodSpace;
    else if ((rankNo == 4) & (itemNo == 5))
      returnString = ".rf4 " + e + periodSpace;
    else if ((rankNo == 4) & (itemNo == 6))
      returnString = ".rf4 " + f + periodSpace;
    else if ((rankNo == 4) & (itemNo == 7))
      returnString = ".rf4 " + g + periodSpace;
    else if ((rankNo == 4) & (itemNo == 8))
      returnString = ".rf4 " + h + periodSpace;
    else if ((rankNo == 4) & (itemNo == 9))
      returnString = ".rf4 " + i + periodSpace;
    else if ((rankNo == 4) & (itemNo == 10))
      returnString = ".rf4 " + j + periodSpace;
    else if ((rankNo == 4) & (itemNo == 11))
      returnString = ".rf4 " + k + periodSpace;
    else if ((rankNo == 4) & (itemNo == 12))
      returnString = ".rf4 " + l + periodSpace;
    else if ((rankNo == 4) & (itemNo == 13))
      returnString = ".rf4 " + m + periodSpace;
    else if ((rankNo == 4) & (itemNo == 14))
      returnString = ".rf4 " + n + periodSpace;
    else if ((rankNo == 4) & (itemNo == 15))
      returnString = ".rf4 " + o + periodSpace;
    else if ((rankNo == 4) & (itemNo == 16))
      returnString = ".rf4 " + p + periodSpace;
    else if ((rankNo == 4) & (itemNo == 17))
      returnString = ".rf4 " + q + periodSpace;
    else if ((rankNo == 4) & (itemNo == 18))
      returnString = ".rf4 " + r + periodSpace;
    else if ((rankNo == 4) & (itemNo == 19))
      returnString = ".rf4 " + s + periodSpace;
    else if ((rankNo == 4) & (itemNo == 20))
      returnString = ".rf4 " + t + periodSpace;
    else if (rankNo == 5)
      returnString = ".rf5 " + String.valueOf(itemNo) + periodSpace;
    else if ((rankNo == 6) & (itemNo == 1))
      returnString = ".rf6 " + a + periodSpace;
    else if ((rankNo == 6) & (itemNo == 2))
      returnString = ".rf6 " + b + periodSpace;
    else if ((rankNo == 6) & (itemNo == 3))
      returnString = ".rf6 " + c + periodSpace;
    else if ((rankNo == 6) & (itemNo == 4))
      returnString = ".rf6 " + d + periodSpace;
    else if ((rankNo == 6) & (itemNo == 5))
      returnString = ".rf6 " + e + periodSpace;
    else if ((rankNo == 6) & (itemNo == 6))
      returnString = ".rf6 " + f + periodSpace;
    else if ((rankNo == 6) & (itemNo == 7))
      returnString = ".rf6 " + g + periodSpace;
    else if ((rankNo == 6) & (itemNo == 8))
      returnString = ".rf6 " + h + periodSpace;
    else if ((rankNo == 6) & (itemNo == 9))
      returnString = ".rf6 "+ i + periodSpace;
    else if ((rankNo == 6) & (itemNo == 10))
      returnString = ".rf6 " + j + periodSpace;
    else if ((rankNo == 6) & (itemNo == 11))
      returnString = ".rf6 " + k + periodSpace;
    else if ((rankNo == 6) & (itemNo == 12))
      returnString = ".rf6 " + l + periodSpace;
    else if ((rankNo == 6) & (itemNo == 13))
      returnString = ".rf6 " + m + periodSpace;
    else if ((rankNo == 6) & (itemNo == 14))
      returnString = ".rf6 " + n + periodSpace;
    else if ((rankNo == 6) & (itemNo == 15))
      returnString = ".rf6 " + o + periodSpace;
    else if ((rankNo == 6) & (itemNo == 16))
      returnString = ".rf6 " + p + periodSpace;
    else if ((rankNo == 6) & (itemNo == 17))
      returnString = ".rf6 " + q + periodSpace;
    else if ((rankNo == 6) & (itemNo == 18))
      returnString = ".rf6 " + r + periodSpace;
    else if ((rankNo == 6) & (itemNo == 19))
      returnString = ".rf6 " + s + periodSpace;
    else if ((rankNo == 6) & (itemNo == 20))
      returnString = ".rf6 " + t + periodSpace;
    else if (rankNo == 7)
	returnString = ".rf7 " + String.valueOf(itemNo) + periodSpace;
    else if (rankNo == 8)
	returnString = ".rf8 " + dash;
    else if (rankNo == 9)
        returnString = ".rf9 " + dot;
    else if (rankNo == 10)
        returnString = ".rf10 " + dash;
    return returnString;
  }

    public static String headerString(int rankNo, int itemNo)  {
    String returnString = "";
    if ((rankNo == 0) & (itemNo == 0))
      returnString = None;
    else if ((rankNo == 1) & (itemNo == 1))
      returnString = IR + closeParenSpace;
    else if ((rankNo == 1) & (itemNo == 2))
      returnString = II + periodSpace;
    else if ((rankNo == 1) & (itemNo == 3))
      returnString = III + periodSpace;
    else if ((rankNo == 1) & (itemNo == 4))
      returnString = IV + periodSpace;
    else if ((rankNo == 1) & (itemNo == 5))
      returnString = VR + periodSpace;
    else if ((rankNo == 1) & (itemNo == 6))
      returnString = VI + periodSpace;
    else if ((rankNo == 1) & (itemNo == 7))
      returnString = VII + periodSpace;
    else if ((rankNo == 1) & (itemNo == 8))
      returnString = VIII + periodSpace;
    else if ((rankNo == 1) & (itemNo == 9))
      returnString = IX + periodSpace;
    else if ((rankNo == 1) & (itemNo == 10))
      returnString = XR + periodSpace;
    else if ((rankNo == 1) & (itemNo == 11))
      returnString = XI + periodSpace;
    else if ((rankNo == 1) & (itemNo == 12))
      returnString = XII + periodSpace;
    else if ((rankNo == 1) & (itemNo == 13))
      returnString = XIII + periodSpace;
    else if ((rankNo == 1) & (itemNo == 14))
      returnString = XIV + periodSpace;
    else if ((rankNo == 1) & (itemNo == 15))
      returnString = XV + periodSpace;
    else if ((rankNo == 1) & (itemNo == 16))
      returnString = XVI + periodSpace;
    else if ((rankNo == 1) & (itemNo == 17))
      returnString = XVII + periodSpace;
    else if ((rankNo == 1) & (itemNo == 18))
      returnString = XVIII + periodSpace;
    else if ((rankNo == 1) & (itemNo == 19))
      returnString = XIX + periodSpace;
    else if ((rankNo == 1) & (itemNo == 20))
      returnString = XX + periodSpace;
    else if ((rankNo == 1) & (itemNo == 21))
      returnString = XI + periodSpace;
    else if ((rankNo == 2) & (itemNo == 1))
      returnString = indent + A + closeParenSpace;
    else if ((rankNo == 2) & (itemNo == 2))
      returnString = indent + B + closeParenSpace;
    else if ((rankNo == 2) & (itemNo == 3))
      returnString = indent + C + closeParenSpace;
    else if ((rankNo == 2) & (itemNo == 4))
      returnString = indent + D + closeParenSpace;
    else if ((rankNo == 2) & (itemNo == 5))
      returnString = indent + E + closeParenSpace;
    else if ((rankNo == 2) & (itemNo == 6))
      returnString = indent + F + closeParenSpace;
    else if ((rankNo == 2) & (itemNo == 7))
      returnString = indent + G + closeParenSpace;
    else if ((rankNo == 2) & (itemNo == 8))
      returnString = indent + H + closeParenSpace;
    else if ((rankNo == 2) & (itemNo == 9))
      returnString = indent + I + closeParenSpace;
    else if ((rankNo == 2) & (itemNo == 10))
      returnString = indent + J + closeParenSpace;
    else if ((rankNo == 2) & (itemNo == 11))
      returnString = indent + K + closeParenSpace;
    else if ((rankNo == 2) & (itemNo == 12))
      returnString = indent + L + closeParenSpace;
    else if ((rankNo == 2) & (itemNo == 13))
      returnString = indent + M + closeParenSpace;
    else if ((rankNo == 2) & (itemNo == 14))
      returnString = indent + N + closeParenSpace;
    else if ((rankNo == 2) & (itemNo == 15))
      returnString = indent + O + closeParenSpace;
    else if ((rankNo == 2) & (itemNo == 16))
      returnString = indent + P + closeParenSpace;
    else if ((rankNo == 2) & (itemNo == 17))
      returnString = indent + Q + closeParenSpace;
    else if ((rankNo == 2) & (itemNo == 18))
      returnString = indent + R + closeParenSpace;
    else if ((rankNo == 2) & (itemNo == 19))
      returnString = indent + S + closeParenSpace;
    else if ((rankNo == 2) & (itemNo == 20))
      returnString = indent + T + closeParenSpace;
    else if (rankNo == 3)
      returnString = indent + indent + String.valueOf(itemNo) + semiSpace;
    else if ((rankNo == 4) & (itemNo == 1))
      returnString = indent + indent + indent + a + periodSpace;
    else if ((rankNo == 4) & (itemNo == 2))
      returnString = indent + indent + indent + b + periodSpace;
    else if ((rankNo == 4) & (itemNo == 3))
      returnString = indent + indent + indent + c + periodSpace;
    else if ((rankNo == 4) & (itemNo == 4))
      returnString = indent + indent + indent + d + periodSpace;
    else if ((rankNo == 4) & (itemNo == 5))
      returnString = indent + indent + indent + e + periodSpace;
    else if ((rankNo == 4) & (itemNo == 6))
      returnString = indent + indent + indent + f + periodSpace;
    else if ((rankNo == 4) & (itemNo == 7))
      returnString = indent + indent + indent + g + periodSpace;
    else if ((rankNo == 4) & (itemNo == 8))
      returnString = indent + indent + indent + h + periodSpace;
    else if ((rankNo == 4) & (itemNo == 9))
      returnString = indent + indent + indent + i + periodSpace;
    else if ((rankNo == 4) & (itemNo == 10))
      returnString = indent + indent + indent + j + periodSpace;
    else if ((rankNo == 4) & (itemNo == 11))
      returnString = indent + indent + indent + k + periodSpace;
    else if ((rankNo == 4) & (itemNo == 12))
      returnString = indent + indent + indent + l + periodSpace;
    else if ((rankNo == 4) & (itemNo == 13))
      returnString = indent + indent + indent + m + periodSpace;
    else if ((rankNo == 4) & (itemNo == 14))
      returnString = indent + indent + indent + n + periodSpace;
    else if ((rankNo == 4) & (itemNo == 15))
      returnString = indent + indent + indent + o + periodSpace;
    else if ((rankNo == 4) & (itemNo == 16))
      returnString = indent + indent + indent + p + periodSpace;
    else if ((rankNo == 4) & (itemNo == 17))
      returnString = indent + indent + indent + q + periodSpace;
    else if ((rankNo == 4) & (itemNo == 18))
      returnString = indent + indent + indent + r + periodSpace;
    else if ((rankNo == 4) & (itemNo == 19))
      returnString = indent + indent + indent + s + periodSpace;
    else if ((rankNo == 4) & (itemNo == 20))
      returnString = indent + indent + indent + t + periodSpace;
    else if (rankNo == 5)
      returnString = indent + indent + indent + indent + String.valueOf(itemNo) + periodSpace;
    else if ((rankNo == 6) & (itemNo == 1))
      returnString = indent + indent + indent + indent + indent + a + periodSpace;
    else if ((rankNo == 6) & (itemNo == 2))
      returnString = indent + indent + indent + indent + indent + b + periodSpace;
    else if ((rankNo == 6) & (itemNo == 3))
      returnString = indent + indent + indent + indent + indent + c + periodSpace;
    else if ((rankNo == 6) & (itemNo == 4))
      returnString = indent + indent + indent + indent + indent + d + periodSpace;
    else if ((rankNo == 6) & (itemNo == 5))
      returnString = indent + indent + indent + indent + indent + e + periodSpace;
    else if ((rankNo == 6) & (itemNo == 6))
      returnString = indent + indent + indent + indent + indent + f + periodSpace;
    else if ((rankNo == 6) & (itemNo == 7))
      returnString = indent + indent + indent + indent + indent + g + periodSpace;
    else if ((rankNo == 6) & (itemNo == 8))
      returnString = indent + indent + indent + indent + indent + h + periodSpace;
    else if ((rankNo == 6) & (itemNo == 9))
      returnString = indent + indent + indent + indent + indent+ i + periodSpace;
    else if ((rankNo == 6) & (itemNo == 10))
      returnString = indent + indent + indent + indent + indent + j + periodSpace;
    else if ((rankNo == 6) & (itemNo == 11))
      returnString = indent + indent + indent + indent + indent + k + periodSpace;
    else if ((rankNo == 6) & (itemNo == 12))
      returnString = indent + indent + indent + indent + indent + l + periodSpace;
    else if ((rankNo == 6) & (itemNo == 13))
      returnString = indent + indent + indent + indent + indent + m + periodSpace;
    else if ((rankNo == 6) & (itemNo == 14))
      returnString = indent + indent + indent + indent + indent + n + periodSpace;
    else if ((rankNo == 6) & (itemNo == 15))
      returnString = indent + indent + indent + indent + indent + o + periodSpace;
    else if ((rankNo == 6) & (itemNo == 16))
      returnString = indent + indent + indent + indent + indent + p + periodSpace;
    else if ((rankNo == 6) & (itemNo == 17))
      returnString = indent + indent + indent + indent + indent + q + periodSpace;
    else if ((rankNo == 6) & (itemNo == 18))
      returnString = indent + indent + indent + indent + indent + r + periodSpace;
    else if ((rankNo == 6) & (itemNo == 19))
      returnString = indent + indent + indent + indent + indent + s + periodSpace;
    else if ((rankNo == 6) & (itemNo == 20))
      returnString = indent + indent + indent + indent + indent + t + periodSpace;
    else if (rankNo == 7)
	returnString = indent + indent + indent + indent + indent + indent + String.valueOf(itemNo) + periodSpace;
    else if (rankNo == 8)
	returnString = indent + indent + indent + indent + indent + indent + indent + dash;
    else if (rankNo == 9)
        returnString = indent + indent + indent + indent + indent+ indent + indent + indent + dot;
    else if (rankNo == 10)
        returnString = indent + indent + indent + indent + indent + indent + indent + indent + indent + dash;
    return returnString;
  }


}