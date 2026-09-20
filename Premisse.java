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
import java.util.*;
/**
 * The Premisse class, implement a part of the Database memory structure
 **/
public class Premisse {
  /**
   * The name of the Premisse
   **/
  String titre;
  /**
   *  The number of the Premisse
   **/
  int numero;
  /**
   * The list of Rules for which the Premisse is condition
   **/
  Vector reglesconditions;
  /**
   * The list of Rules for which the Premisse is action
   **/
  Vector reglesactions;
  /**
   * Just a var to check what you want
   **/
  boolean passed;
  /**
   * Initialize the Pemisse object
   * @param The name of the premisse,it's number
   **/
  Premisse(String t,int n) {
     titre=new String();
     titre=t;
     numero=n;
     passed=false;
     reglesconditions=new Vector();
     reglesactions=new Vector();
  }
  /**
   * Just a var to know that the Premisse has been checked
   * @param True or False
   **/
  public void setPassed(boolean b) {
     passed=b;
  }
  /**
   * Just a var to know that the Premisse has been checked
   * @return True or False
   **/
  public boolean isPassed() {
     return passed;
  }
  /**
   * Return the number of the Premisse
   * @return The number
   **/
  public int getNumero() {
     return(numero);
  }
  /**
   * Return the name of the Premisse
   * @return The name
   **/
  public String getTitre() {
     return(titre);
  }
  /**
   * Add a Rule to the list of Rules for which the Premisse is condition
   * @param The Rule
   **/
  public void addRegleCondition(Regle r) {
     reglesconditions.addElement(r);
  }
  /**
   * Add a Rule to the list of Rules for which the Premisse is action
   * @param The Rule
   **/
  public void addRegleAction(Regle r) {
     reglesactions.addElement(r);
  }
  /**
   * Get the list of Rules for which the Premisse is condition
   * @return The list of Rules
   **/
  public Vector getRegleCondition() {
     return reglesconditions;
  }
  /**
   * get list of Rules for which the Premisse is action
   * @return The list of Rules
   **/
  public Vector getRegleAction() {
     return reglesactions;
  }
}
