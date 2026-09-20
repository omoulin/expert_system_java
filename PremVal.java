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
 * Encapsulation of the Premisse objet to be able to have P and NOT P
 **/
public class PremVal {
  /**
   * The value of the param : FALSE = NOT
   **/
  boolean vrai;
  /**
   * The Premisse concerned
   **/
  Premisse prem;
  /**
   * Initialize the object
   * @param The Operator and the Premisse
   **/
  PremVal(boolean v,Premisse p) {
     vrai=v;
     prem=p;
  }
  /**
   * Return the Premisse
   * @return The Premisse
   **/
  Premisse getPremisse() {
     return prem;
  }
  /**
   * Return the Value of the operator
   * @return The value
   **/
  boolean getValeur() {
     return vrai;
  }
}
