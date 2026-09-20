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

/**
 * The User Class, implements the methods for a single user ID
 **/
class Utilisateur {
  /**
   * The Login Name
   **/
  String login;
  /**
   * The Password
   **/
  String passwd;
  /**
   * The Rights
   **/
  String right;
  /**
   * Initialize the User
   * @param The Login, the Password, the Rights
   **/
  Utilisateur(String l,String p,String r) {
     login = l;
     passwd =p;
     right=r;
  }
  /**
   * Return the Password of the User
   * @param The Password
   **/
  public String getPasswd() {
     return passwd;
  }
  /**
   * Return the Rights of the User
   * @param The Rights
   **/
  public String getRight() {
     return right;
  }
  /**
   * Return the Login of the User
   * @param The Login
   **/
  public String getLogin() {
     return login;
  }
  /**
   * Try to check the User Password
   * @param The Password
   * @return True if checked, False othewise
   **/
  public boolean isChecked(String p) {
     boolean res;
     if (p.equals(passwd)) {
        res = true;
     }
     else {
        res = false;
     }
     return res;
  }
}
/**
 * The Multi-Users class
 **/
public class Users {
  /**
   * The list of the Users
   **/
  Vector us = null;
  /**
   * The name of the Users File
   **/
  String fichier=null;
  /**
   * Initialize the Multi-Users
   * @param The name of the Users File
   **/
  Users(String fil) {
     us= new Vector();
     fichier=fil;
     chargeUsers();
  }
  /**
   * Write a String into a File
   * @param The file Stream descriptor, the String
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
   * Save the Multi-Users Memory Database into a file
   **/
  public void saveUsers() {
     Crypteur crp = new Crypteur("152653254897225",15);
     FileOutputStream fic = null;
     File testf =null;
     Utilisateur u;
     int i,j;
     byte Buff[]  = null;
     String str = new String();
     try {
        testf= new File("./"+fichier+".usr");
        if (testf.exists()) {
           testf.delete();
        }
        fic = new FileOutputStream("./"+fichier+".usr");
     }
     catch (IOException ex) {
        System.err.println("NOYAU : File Create Error... Exiting");
        return;
     }
     try {
        writefichier(fic,"BEGINUSRDB\n");
        for(i=0;i<us.size();i++) {
           u=(Utilisateur)us.elementAt(i);
           writefichier(fic,u.getLogin()+"\n");
           writefichier(fic,crp.crypteString(u.getPasswd())+"\n");
           writefichier(fic,u.getRight()+"\n");
        }
        writefichier(fic,"ENDUSRDB\n");
     }
     catch(IOException ex) {
        System.err.println("NOYAU : Dumping To File Error... Exiting");
     }
  }
  /**
   * Load the Multi-User File into the memory Database
   **/
  void chargeUsers() {
     Crypteur crp =new Crypteur("152653254897225",15);
     FileInputStream fic = null;
     DataInputStream inline=null;
     String str=null;
     String str2=null;
     String str3=null;
     String tmp=null;
     Utilisateur u=null;
     try {
        fic=new FileInputStream("./"+fichier+".usr");
        inline= new DataInputStream(fic);
     }
     catch (IOException ex) {
        System.err.println("AI SERVER : Error In Loading Users Identifications... Exiting");
        return;
     }
     try {
        str=new String(inline.readLine());
        while(!str.equals("BEGINUSRDB")) {
           str = new String(inline.readLine());
        }
        while (!str.equals("ENDUSRDB")) {
           str=new String(inline.readLine());
           if (!str.equals("ENDUSRDB")) {
              str2=new String(inline.readLine());
              tmp=crp.decrypteString(str2);
              str3=new String(inline.readLine());
              u = new Utilisateur(str,tmp,str3);
              us.addElement(u);
           }
        }
     }
     catch (IOException ex) {
        System.err.println("AI SERVER : Malformated Users File... Exiting");
        return;
     }
  }
  /**
   * Check if a User whith the Login and Password, exist
   * @param The login and the Password
   * @return True if exist,False otherwise
   **/
  public boolean idChecked(String l,String p) {
     boolean res;
     res=false;
     int i;
     Utilisateur u;
     i=0;
     while (i<us.size() && !res) {
        u=(Utilisateur)us.elementAt(i);
        if (u.getLogin().equals(l)) {
           if (u.isChecked(p)) {
              res=true;
           }
        }
        i++;
     }
     return res;
  }
  /**
   * Return the Rights of a User
   * @param The Login and the Password
   * @return The Rights if found, 0 otherwise
   **/
  public String getRight(String l,String p) {
     boolean res;
     res=false;
     String result;
     result=new String("0");
     int i;
     Utilisateur u;
     i=0;
     while (i<us.size() && !res) {
        u=(Utilisateur)us.elementAt(i);
        if (u.getLogin().equals(l)) {
           res=true;
           result=u.getRight();
        }
        i++;
     }
     return result;
  }
  /**
   * Add a User into the Memory Database
   * @param The Login, the Password, The Rights
   **/
  public void addUser(String l,String p, String d) {
     Utilisateur u = new Utilisateur(l,p,d);
     us.addElement(u);
  }
  /**
   * Remove a User from the Memory Database
   * @param The Login
   **/
  public void removeUser(String l) {
     boolean done=false;
     int i=0;
     Utilisateur u;
     while(i<us.size() && !done) {
        u=(Utilisateur)us.elementAt(i);
        if(u.getLogin().equals(l)) {
           done=true;
        }
        else {
           i++;
        }
     }
     if (done) {
        us.removeElementAt(i);
     }
  }
}
