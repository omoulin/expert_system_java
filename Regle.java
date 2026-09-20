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
 * The Rule class, implement a part of the Database Memory structure
 **/
public class Regle {
  /**
   * The name of the Rule
   **/
  String titre;
  /**
   * The number of the Rule
   **/
  int numero;
  /**
   * The list of Premisses which are conditions of the Rule
   **/
  Vector conditions;
  /**
   * The Premisse which are action of the Rule
   **/
  PremVal action;
  /**
   * The list of Rule, whose action are condition for the Rule
   **/
  Vector deductions;
  /**
   * Initialize the Rule
   * @param The name of the Rule, it's number
   **/
  Regle(String t,int n) {
     titre=new String();
     titre=t;
     numero=n;
     conditions=new Vector();
     action=null;
     deductions=new Vector();
  }
  /**
   * Return the number of the Rule
   * @return The number
   **/
  public int getNumero() {
     return(numero);
  }
  /**
   * Return the name of the Rule
   * @return The name
   **/
  public String getTitre() {
     return(titre);
  }
  /**
   * Add a Premisse as condition of the Rule
   * @param The operator ( ,NOT) and the Premisse
   **/
  public void addCondition(boolean b,Premisse p) {
     int i;
     PremVal pc=new PremVal(b,p);
     conditions.addElement(pc);
     p.addRegleCondition(this);
  }
  /**
   * Add a Premisse as action of the Rule
   * @param The operator( ,NOT) and the Premisse
   **/
  public void addAction(boolean b,Premisse p) {
     PremVal pc= new PremVal(b,p);
     action=pc;
     p.addRegleAction(this);
  }
  /**
   * Add a Rule as a deduction
   * @param The Rule
   **/
  public void addDeduction(Regle r) {
     deductions.addElement(r);
  }
  /**
   * Return the list of Premisses which are conditions for the Rule
   * @return The list of Premisses
   **/
  public Vector getConditions() {
     return conditions;
  }
  /**
   * Return the list of Rules which are deductions for the Rule
   * @return The list of Rules
   **/
  public Vector getDeductions() {
     return deductions;
  }
  /**
   * Return the Premisse which is action for the Rule
   * @return The Premisse
   **/
  public PremVal getAction() {
     return action;
  }
}
