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
/**
 * Security transfert class, crypt and decrypt a String
 * (Not operational, just a skeletton for futur improvment)
 **/
public class Crypteur {
  /**
   * The Crypt key
   **/
  String cle =null;
  /**
   * The length of the Crypt key
   **/
  int nbc;
  /**
   * Initialize the Security transfert
   * @param The key and it's length
   **/
  Crypteur(String c,int n) {
     cle=c;
     nbc=n;
  }
  /**
   * Convert a Char to an Int
   * @param The Char
   * @return The Int
   **/
  int toAscii(char c) {
     return((int)c);
  }
  /**
   * Convert an Int to a Char
   * @param The Int
   * @return The Char
   **/
  char toChar(int b) {
     return((char)b);
  }
  /**
   * Crypt a String using the key
   * @param The String
   * @return The crypted String
   **/
  String crypteString(String s) {
     int i;
     String output=new String();
     int cptnbc=0;
     for (i=0;i<s.length();i++) {
        if (cptnbc>=nbc) {
           cptnbc=0;
        }
        output=output+toChar(toAscii(s.charAt(i))+toAscii(cle.charAt(cptnbc)));
        cptnbc++;
     }
     return(s);
  }
  /**
   * Decrypt a String using the key
   * @param The crypted String
   * @return The String
   **/
  String decrypteString(String s) {
     int i;
     String output=new String();
     int cptnbc=0;
     for (i=0;i<s.length();i++) {
        if (cptnbc>=nbc) {
           cptnbc=0;
        }
        output=output+toChar(toAscii(s.charAt(i))-toAscii(cle.charAt(cptnbc)));
        cptnbc++;
     }
     return(s);
  }
}
