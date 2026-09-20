/*
This program has been created by Olivier Moulin and is distributed under
the GNU public license.

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

import java.lang.*;
import java.io.*;
import java.util.*;
import java.net.*;

/**
 * Main class, initialize the kernel and the TCP/IP server
 **/
public class NoyauServ {
  /**
   * Instance of the TCP/IP server
   **/
  static serveurNoyau serveur;
  /**
   * Instance of the kernel
   **/
  static Noyau noyprinc;
  /**
   * The main program, start the kernel and the TCP/IP server
   * @param The IP port and the Database file
   **/
  public static void main(String args[]) {
     String s;
     try {
        s= new String (args[1]);
     }
     catch (ArrayIndexOutOfBoundsException e) {
        System.err.println("Usage : java NoyauServ <Port> <File>");
        return;
     }
     noyprinc= new Noyau(args[1]);
     try {
        serveur = new serveurNoyau(noyprinc,args[0]);
     }
     catch (IOException e) {
        System.err.println("KERNEL : Error Starting AI Server : "+e);
     }
     serveur.start();
     System.out.println("KERNEL : AI Server Started");
     System.out.println("Coded by : O.Moulin");
     try {
        serveur.join();
     }
     catch (InterruptedException e) {
        System.err.println("KERNEL : AI Server killed");
     }
     serveur = null;
  }
}
/**
 * The Kernel class, implement all the AI methods
 **/
class Noyau {
  /**
   * The subject of the loaded knowledge Database
   **/
  String sujet= new String("KERNEL OF KNOWLEDGE BASED SYSTEM");
  /**
   * The Vector used to stock the rules
   **/
  static Vector regles;
  /**
   * The Vector used to stock the Premisses
   **/
  static Vector premisses;
  /**
   * The name of the loaded Database File
   **/
  String fichier = new String();
  /**
   *  The number of loaded Premisses
   **/
  int cptprem;
  /**
   *  The number of the loaded Rules
   **/
  int cptreg;
  /**
   *  The instance of the User Certification Access Object
   **/
  Users us=null;
  /**
   * Initialize the Kernel and it's default parameters
   * @param The name of the Database file
   **/
  Noyau(String fil) {
     regles = new Vector();
     premisses = new Vector();
     fichier = fil;
     chargeBase();
     us = new Users(fil);
  }
  /**
   * Try to check the User ID and Password
   * @param The user ID and it's password
   * @return True if checked, False otherwise
   **/
  public boolean idChecked(String l,String p) {
     return us.idChecked(l,p);
  }
  /**
   * Return the User Rights
   * @param The user ID and it's password
   * @return The rights if user exist, 0 otherwise
   **/
  public String getRight(String l,String p) {
     return us.getRight(l,p);
  }
  /**
   *  Add a new Rule to the Memory Rule Database
   *  @param The Rule object : Regle
   **/
  public void addRegle(Regle r) {
     regles.addElement(r);
     cptreg++;
  }
  /**
   *  Re-calculate the Deductions and Re-compile the Memory Database
   **/
  public void calculDeduc() {
     int j,i,k;
     PremVal p;
     Regle r2;
     Vector reg = regles;
     Regle r;
     for(k=0;k<reg.size();k++) {
        r=(Regle)reg.elementAt(k);
        Vector prem = r.getConditions();
        for(i=0;i<prem.size();i++) {
           p=(PremVal)prem.elementAt(i);
           for(j=0;j<regles.size();j++) {
              r2=(Regle)regles.elementAt(j);
              if (r2.getAction().getPremisse().getTitre().equals(p.getPremisse().getTitre()) && r2.getAction().getValeur() == p.getValeur()) {
                 r2.addDeduction(r);
              }
           }
        }
     }
  }
  /**
   * Return the number of Rules in Memory
   * @return The number of Rules
   **/
  public int getNbRegle() {
     return cptreg;
  }
  /**
   *  Return the number of Premisses in Memory
   *  @return The number of Premisses
   **/
  public int getNbPremisse() {
     return cptprem;
  }
  /**
   * Add a new Premisses to the Memory Premisses Database
   * @param The Premisse object : Premisse
   **/
  public void addPremisse(Premisse p) {
     premisses.addElement(p);
     cptprem++;
  }
  /**
   * Load the file Database into Memory
   **/
  void chargeBase() {
     File fdesc = new File((String)null,fichier);
     FileInputStream fic = null;
     DataInputStream inline = null;
     Premisse p = null;
     Regle r = null;
     String str = new String();
     try {
        fic = new FileInputStream(fdesc);
        inline = new DataInputStream(fic);
     }
     catch (IOException ex) {
        System.err.println("KERNEL : File Locate Error... Exiting");
        return;
     }
     try {
        str="NOP";
        while (!str.equals("BEGINDB")) {
           str=inline.readLine();
        }
        sujet=inline.readLine();
        str="NOP";
        regles = new Vector();
        cptreg=0;
        premisses= new Vector();
        cptprem=0;
        while (!str.equals("ENDDB")) {
           str=inline.readLine();
        }
        while (!str.equals("BEGINRULEDB")) {
           str=inline.readLine();
        }
        str=inline.readLine();
        while (!str.equals("ENDRULEDB")) {
           while(!str.equals("CREATERULE")) {
              str=inline.readLine();
           }
           str=inline.readLine();
           r =new Regle(str,cptreg);
           regles.addElement(r);
           while (!str.equals("RULECONDITION")) {
              str=inline.readLine();
           }
           cptreg++;
           str=inline.readLine();
           while (!str.equals("RULEACTION")) {
              if (str.charAt(0)!='!') {
                 p = existPremisse(str);
              }
              else {
                 p= existPremisse(str.substring(1,str.length()));
              }
              if (p==null) {
                 if (str.charAt(0)!='!') {
                    p=new Premisse(str,cptprem);
                 }
                 else {
                    p=new Premisse(str.substring(1,str.length()),cptprem);
                 }
                 premisses.addElement(p);
                 cptprem++;
              }
              if (str.charAt(0)!='!') {
                 r.addCondition(true,p);
              }
              else {
                 r.addCondition(false,p);
              }
              str=inline.readLine();
           }
           str=inline.readLine();
           while (!str.equals("ENDCREATERULE")) {
              if (str.charAt(0)!='!') {
                 p = existPremisse(str);
              }
              else {
                 p= existPremisse(str.substring(1,str.length()));
              }
              if (p==null) {
                 if (str.charAt(0)!='!') {
                    p=new Premisse(str,cptprem);
                 }
                 else {
                    p=new Premisse(str.substring(1,str.length()),cptprem);
                 }
                 premisses.addElement(p);
                 cptprem++;
              }
              if (str.charAt(0)!='!') {
                 r.addAction(true,p);
              }
              else {
                 r.addAction(false,p);
              }
              str=inline.readLine();
           }
           str=inline.readLine();
        }
     }
     catch (IOException ex) {
        System.err.println("KERNEL : File Read Error... Exiting");
        return;
     }
     calculDeduc();
  }
  /**
   * Write a String into a File
   * @param The File Stream and the String
   **/
  void writefichier(FileOutputStream f,String s) throws IOException {
     int i;
     byte Buf[] = new byte[s.length()];
     for(i=0;i<s.length();i++) {
        Buf[i]=(byte)s.charAt(i);
     }
     f.write(Buf);
  }
  /**
   * Dump the Memory Database into a file
   * @param The name of the File
   **/
  public void sauveBase(String f) {
     FileOutputStream fic = null;
     File testf =null;
     PremVal p = null;
     Regle r = null;
     Vector tmp = null;
     int i,j;
     byte Buff[]  = null;
     String str = new String();
     if (!f.equals("SAMEFILE")) {
        try {
           testf= new File("./"+f);
           if (testf.exists()) {
              testf.delete();
           }
           fic = new FileOutputStream("./"+f);
        }
        catch (IOException ex) {
           System.err.println("KERNEL : File Create Error... Exiting");
           return;
        }
     }
     else {
        try {
           testf= new File("./"+fichier);
           if (testf.exists()) {
              testf.delete();
           }
           fic = new FileOutputStream("./"+fichier);
        }
        catch (IOException ex) {
           System.err.println("KERNEL : File Create Error... Exiting");
           return;
        }
     }
     try {
        writefichier(fic,"BEGINDB\n");
        writefichier(fic,sujet+"\n");
        writefichier(fic,"ENDDB\n");
        writefichier(fic,"BEGINRULEDB\n");
        for(i=0;i<regles.size();i++) {
           r=(Regle)regles.elementAt(i);
           writefichier(fic,"CREATERULE\n");
           writefichier(fic,r.getTitre()+"\n");
           writefichier(fic,"RULECONDITION\n");
           tmp = r.getConditions();
           for(j=0;j<tmp.size();j++) {
              p=(PremVal)tmp.elementAt(j);
              if (p.getValeur()==true) {
                 writefichier(fic,p.getPremisse().getTitre()+"\n");
              }
              else {
                 writefichier(fic,"!"+p.getPremisse().getTitre()+"\n");
              }
           }
           writefichier(fic,"RULEACTION\n");
           p=r.getAction();
           if (p.getValeur()==true) {
              writefichier(fic,p.getPremisse().getTitre()+"\n");
           }
           else {
              writefichier(fic,"!"+p.getPremisse().getTitre()+"\n");
           }
           writefichier(fic,"ENDCREATERULE\n");
        }
        writefichier(fic,"ENDRULEDB\n");
     }
     catch(IOException ex) {
        System.err.println("KERNEL : Dumping To File Error... Exiting");
     }
  }
  /**
   * Return the subject of the Loaded Database
   * @return the subject of the loaded Database
   **/
  public String getSujet() {
     return sujet;
  }
  /**
   * Try to find in memory and return a Premisse
   * @param The name of the Premisse
   * @return The Premisse or null
   **/
  public Premisse existPremisse(String titre) {
     int i;
     boolean done=false;
     Premisse p=null;
     i=0;
     done=false;
     while(i<premisses.size() && !done) {
        p=(Premisse)premisses.elementAt(i);
        if (p.getTitre().equals(titre)) {
           done=true;
        }
        i++;
     }
     if (!done) {
        p=null;
     }
     return p;
  }
  /**
   * Dump into a Stream the list of loaded Rules
   * @param The Stream
   **/
  public void displayRegles(PrintStream prnt) {
     int i,j;
     Regle r;
     Vector lp;
     PremVal p;
     String retour =null;
     for(i=0;i<regles.size();i++) {
        retour = new String();
        r=(Regle)regles.elementAt(i);
        prnt.println("/// RULE : "+r.getNumero() + " ///");
        prnt.println(r.getTitre());
        prnt.println("IF");
        lp = r.getConditions();
        for(j=0;j<lp.size();j++) {
           p=(PremVal)lp.elementAt(j);
           if (p.getValeur()==true) {
              prnt.println(p.getPremisse().getTitre());
           }
           else {
              prnt.println("NOT "+p.getPremisse().getTitre());
           }
        }
        p= r.getAction();
        prnt.println("THEN");
        if (p.getValeur()==true) {
           prnt.println(p.getPremisse().getTitre());
        }
        else {
           prnt.println("NOT "+p.getPremisse().getTitre());
        }
        prnt.println("ENDIF");
     }
  }
  /**
   * Dump into a Stream the list of calculated deductions
   * @param The Stream
   **/
  public void displayDeductions(PrintStream prnt) {
     int i,j;
     Regle r;
     Vector lp;
     Regle r2;
     String retour =null;
     for(i=0;i<regles.size();i++) {
        r=(Regle)regles.elementAt(i);
        prnt.println("RULE : "+r.getNumero()+" - "+r.getTitre());
        prnt.println("ACTIVATE");
        lp = r.getDeductions();
        for(j=0;j<lp.size();j++) {
           r2=(Regle)lp.elementAt(j);
           prnt.println(r2.getTitre());
        }
        prnt.println("ENDACTIVATE");
     }
  }
  /**
   * Dump into a Stream the list of loaded Premisses
   * @param The Stream
   **/
  public void displayPremisses(PrintStream prnt) {
     int i;
     Premisse p;
     for(i=0;i<premisses.size();i++) {
        p=(Premisse)premisses.elementAt(i);
        prnt.println("/// PREMISSE : "+p.getNumero()+" ///");
        prnt.println(p.getTitre());
     }
  }
  /**
   * Test if two Vectors are equals
   * @param The two Vectors
   * @return True if equals, False otherwise
   **/
  boolean isEqualVector(Vector v1,Vector v2) {
     boolean res=true;
     int i;
     if (v1.size()!=v2.size()) {
        res=false;
     }
     else {
        i=0;
        while (i<v1.size() && res==true) {
           if (v1.elementAt(i)!=v2.elementAt(i))
              res=false;
           i++;
        }
     }
     return res;
  }
  /**
   * Create a Vector of Premisses with a Vector of Premisses names
   * @param The Vector of Premisses names
   * @return The Vector of Premisses
   **/
  public Vector makePremisseList(Vector str) {
     int i,j;
     Vector res;
     res= new Vector();
     Premisse p;
     PremVal pv;
     for(i=0;i<premisses.size();i++) {
        p=(Premisse)premisses.elementAt(i);
        for(j=0;j<str.size();j++) {
           String s=(String)str.elementAt(j);
           if (s.charAt(0)=='!') {
              if (p.getTitre().equals(s.substring(1,s.length()))) {
                 pv=new PremVal(false,p);
                 res.addElement(pv);
              }
           }
           else {
              if (p.getTitre().equals(s)) {
                 pv=new PremVal(true,p);
                 res.addElement(pv);
              }
           }
        }
     }
     return res;
  }
  /**
   * Center of the inference engine, test recursively the Premises.
   * @param One Goal Rule and the known premisses
   * @return True or False, depends if the tree is checked
   **/
  boolean isValid(Regle regleCourante,Vector premListe) {
     Vector cond;
     Vector reglesCond;
     PremVal p;
     PremVal ptest;
     Regle r;
     int i,j;
     boolean test;
     boolean res;
     boolean respart;
     boolean totalyOpposite;
     res=true;
     cond=regleCourante.getConditions();
     for (i=0;i<cond.size();i++) {
        p=(PremVal)cond.elementAt(i);
        test=false;
        totalyOpposite=false;
        for (j=0;j<premListe.size();j++) {
           ptest=(PremVal)premListe.elementAt(j);
           if (ptest.getPremisse().getTitre().equals(p.getPremisse().getTitre()) && ptest.getValeur()==p.getValeur()) {
              test=true;
           }
           if (ptest.getPremisse().getTitre().equals(p.getPremisse().getTitre()) && ptest.getValeur()!=p.getValeur()) {
              totalyOpposite=true;
           }
        }
        if (test==true) {
           res=res && true;
        }
        else {
           if (totalyOpposite) {
              reglesCond=p.getPremisse().getRegleAction();
              if (reglesCond.size()==0) {
                 res=res && false;
              }
              else {
                 for (j=0;j<reglesCond.size();j++) {
                    r=(Regle)reglesCond.elementAt(j);
                    respart = isValid(r,premListe);
                    if (respart==true)
                      {
                        premListe.addElement(r.getAction());
                      }
                    res=res && respart;
                 }
              }
           }
           else {
             res= res && false;
           }
        }
     }
     return res;
  }

  /**
   * Dump into a Stream the result of a reseach of goals
   * @param The Stream, The Vector of know facts
   **/
  public void backSearch(PrintStream prnt,Vector act) {
     Vector currentlist;
     Vector conditionsFinales;
     int i,j,k;
     PremVal premBut;
     PremVal premTest;
     PremVal premTest2;
     Regle r1;
     Regle r2;
     Vector reglesBut= new Vector();
     Vector test;
     currentlist=null;
     boolean isBut;
     for (i=0;i<regles.size();i++) {
        isBut=true;
        r1=(Regle)regles.elementAt(i);
        premTest=r1.getAction();
        for (j=0;j<regles.size();j++) {
           r2=(Regle)regles.elementAt(j);
           test=r2.getConditions();
           for (k=0;k<test.size();k++) {
              premTest2=(PremVal)test.elementAt(k);
              if (premTest.getValeur()==premTest2.getValeur() && premTest.getPremisse().getTitre().equals(premTest2.getPremisse().getTitre()))
                isBut=false;
           }
        }
        if (isBut ==true) {
          reglesBut.addElement(r1);
        }
     }
     for (i=0;i<reglesBut.size();i++) {
        r1=(Regle)reglesBut.elementAt(i);
        if (isValid(r1,act)) {
           premTest=r1.getAction();
           if (premTest.getValeur()) {
              prnt.println("Goal : "+premTest.getPremisse().getTitre()+" Checked");
           }
           else {
              prnt.println("Goal : NOT "+premTest.getPremisse().getTitre()+" Checked");
           }
        }
        else {
           premTest=r1.getAction();
           if (premTest.getValeur()) {
              prnt.println("Goal : "+premTest.getPremisse().getTitre()+" NOT Checked");
           }
           else {
              prnt.println("Goal : NOT "+premTest.getPremisse().getTitre()+" NOT Checked");
           }
        }
     }
  }
}
/**
 * The TCP/IP server Class, implement all the cummunication methods
 **/
class serveurNoyau implements Runnable {
  /**
   * The IP port
   **/
  int port = 5010;
  /**
   * The instance of the Thread used for the server
   **/
  Thread serverThread = null;
  /**
   *  The instance of the TCP/IP socket object
   **/
  ServerSocket noyServ = null;
  /**
   * The output Error Stream
   **/
  PrintStream errout = new PrintStream(System.out);
  /**
   * Just to keep a link with the AI Kernel
   **/
  Noyau pere = null;
  /**
   * Initialize the TCP/IP Server, create the socket
   * @param The AI kernel instance, the IP port
   **/
  public serveurNoyau(Noyau per,String prt) throws IOException {
     pere=per;
     Float x= Float.valueOf(prt);
     try {
        noyServ = new ServerSocket(x.intValue());
        port=x.intValue();
     }
     catch(IOException e) {
        errout.println("AI SERVER : Error Starting Serveur : "+e);
        throw e;
     }
     System.out.println("AI SERVER : Started on Port : "+port);
  }
  public void setOutError(PrintStream p) {
     errout=p;
  }
  /**
   * Start the server Thread
   **/
  public synchronized void start() {
     if (serverThread == null) {
        serverThread = new Thread( this);
        serverThread.setPriority(Thread.MAX_PRIORITY /4);
        serverThread.start();
     }
     if (noyServ==null) {
        try {
           noyServ = new ServerSocket (port);
        }
        catch (IOException e) {
           errout.println("AI SERVER : Exception allocating ServerSocket : "+e);
        }
     }
  }
  /**
   * Activate on the Server Thread Stop
   **/
  public synchronized void stop () {
     if ( serverThread !=null) {
        serverThread.stop();
        serverThread = null;
     }
     if (noyServ!=null) {
        try {
           noyServ.close();
           noyServ = null;
        }
        catch (IOException e) {
           errout.println("AI SERVER : Exception closing ServerSocket : "+e);
        };
        return;
     }
  }
  /**
   * Link to the Server Thread
   **/
  public final void join() throws InterruptedException {
     if (serverThread!=null) {
        serverThread.join();
     }
     return;
  }
  /**
   * Execute the TCP/IP Server
   **/
  public void run() {
     Crypteur crypte = new Crypteur("152653254897225",15);
     InputStream in = null;
     DataInputStream inline = null;
     PrintStream out = null;
     Socket s;
     Socket con = null;
     while (serverThread !=null) {
        try {
           con= noyServ.accept();
        }
        catch (IOException e) {
           errout.println("AI SERVER : Error on accept : "+e);
           return;
        }
        try {
           in = con.getInputStream();
           inline= new DataInputStream(in);
        }
        catch (Exception e) {
           errout.println("AI SERVER : Error building streams  :"+e);
        }
        try {
           out= new PrintStream (con.getOutputStream());
        }
        catch (Exception e) {
           errout.println("AI SERVER : Error Opening Config transmission :"+e);
        }
        try {
           int nbytes;
           boolean done = false;
           byte b[] = new byte[1024];
           String str = new String("INIT");
           String str2 = null;
           String str3 = null;
           String curlog = null;
           String curpass = null;
           str= new String(inline.readLine());
           if (str.equals("SUBJECT")) {
              out.println(pere.getSujet());
              out.println("ENDT");
              while (!str.equals("ENDSUBJECT")) {
                 str= new String(inline.readLine());
              }
           }
           if (str.equals("BEGINID")) {
              str=new String(inline.readLine());
              str2=crypte.decrypteString(new String(inline.readLine()));
              str3= new String(inline.readLine());
              while (!str3.equals("ENDID")) {
                 str3= new String(inline.readLine());
              }
              if (pere.idChecked(str,str2)) {
                 errout.println("AI SERVER : Incomming Query From : "+str+" on "+con.getInetAddress());
                 curlog=new String(str);
                 curpass=new String(str2);
                 str = new String(inline.readLine());
                 if (str.equals("GETRIGHT")){
                    str3= new String(inline.readLine());
                    while (!str3.equals("ENDRIGHT")) {
                       str3= new String(inline.readLine());
                    }
                    out.println("YOUR RIGHTS");
                    out.println(pere.getRight(curlog,curpass));
                    out.println("ENDT");
                 }
                 if (str.equals("BACKSEARCH")) {
                    Vector entry = new Vector();
                    str = new String(inline.readLine());
                    while(!str.equals("ENDSEARCH")) {
                       String sb = new String(str);
                       entry.addElement(sb);
                       str= new String(inline.readLine());
                    }
                    pere.backSearch(out,pere.makePremisseList(entry));
                    out.println("ENDT");
                 }
                 if (str.equals("AFFDEDUCTION")) {
                    pere.displayDeductions(out);
                    out.println("ENDT");
                    while(!str.equals("ENDAFF")) {
                       str = new String(inline.readLine());
                    }
                 }
                 if (str.equals("UPDATE")) {
                    if (pere.getRight(curlog,curpass).equals("X")) {
                       pere.chargeBase();
                       out.println("DATABASE UPDATED AND RE-COMPILED");
                       out.println("ENDT");
                    }
                    else {
                       out.println("NOT ALLOWED");
                       out.println("ENDT");
                    }
                    while(!str.equals("ENDUPDATE")) {
                       str = new String(inline.readLine());
                    }
                 }
                 if (str.equals("AFFPREMISSE")) {
                    pere.displayPremisses(out);
                    out.println("ENDT");
                    while(!str.equals("ENDAFF")) {
                       str = new String(inline.readLine());
                    }
                 }
                 if (str.equals("AFFRULE")) {
                    pere.displayRegles(out);
                    out.println("ENDT");
                    while(!str.equals("ENDAFF")) {
                       str=new String(inline.readLine());
                    }
                 }
                 if (str.equals("SAVE")) {
                    str= new String(inline.readLine());
                    if (pere.getRight(curlog,curpass).equals("X")) {
                       pere.sauveBase(str);
                       out.println("OK SAVED");
                       out.println("ENDT");
                    }
                    else {
                       out.println("NOT ALLOWED");
                       out.println("ENDT");
                    }
                    while (!str.equals("ENDSAVE")) {
                       str= new String(inline.readLine());
                    }
                 }
                 if (str.equals("CREATERULE")) {
                    if (pere.getRight(curlog,curpass).equals("W") || pere.getRight(curlog,curpass).equals("X")) {                          Regle r =null;
                       Premisse p=null;
                       str=inline.readLine();
                       r =new Regle(str,pere.getNbRegle());
                       pere.addRegle(r);
                       while (!str.equals("RULECONDITION")) {
                          str=inline.readLine();
                       }
                       str=inline.readLine();
                       while (!str.equals("RULEACTION")) {
                          if (str.charAt(0)!='!') {
                             p = pere.existPremisse(str);
                          }
                          else {
                             p= pere.existPremisse(str.substring(1,str.length()));
                          }
                          if (p==null) {
                             if (str.charAt(0)!='!') {
                                p=new Premisse(str,pere.getNbPremisse());
                             }
                             else {
                                p=new Premisse(str.substring(1,str.length()),pere.getNbPremisse());
                             }
                             pere.addPremisse(p);
                          }
                          if (str.charAt(0)!='!') {
                             r.addCondition(true,p);
                          }
                          else {
                             r.addCondition(false,p);
                          }
                          str=inline.readLine();
                       }
                       str=inline.readLine();
                       while (!str.equals("ENDCREATERULE")) {
                          if (str.charAt(0)!='!') {
                             p = pere.existPremisse(str);
                          }
                          else {
                             p= pere.existPremisse(str.substring(1,str.length()));
                          }
                          if (p==null){
                             if (str.charAt(0)!='!'){
                                p=new Premisse(str,pere.getNbPremisse());
                             }
                             else {
                                p=new Premisse(str.substring(1,str.length()),pere.getNbPremisse());
                             }
                             pere.addPremisse(p);
                          }
                          if (str.charAt(0)!='!'){
                             r.addAction(true,p);
                          }
                          else {
                             r.addAction(false,p);
                          }
                          str=inline.readLine();
                       }
                       out.println("RULE CREATED");
                       out.println("ENDT");
                    }
                    else {
                       out.println("NOT ALLOWED");
                       out.println("ENDT");
                    }
                 }
              }
              else {
                 out.println("NOT AUTHENTIFIED");
                 errout.println("AI SERVER : Identification Error on Incoming Query from : "+str+" on : "+con.getInetAddress());
                 out.println("ENDT");
              }
           }
        }
        catch (Exception e) {
           errout.println("AI SERVER : Syntax Or Connection Error From Incomming Connection");
        }
        try {
           /*con.close();*/
        }
        catch (Exception e) {
           errout.println ("AI SERVER : Error on close : "+e);
        }
     }
  }
}
