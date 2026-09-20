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

import java.awt.*;
import java.awt.event.*;

public class Message extends Dialog
{
  String text_demande;
  Frame parent;
  TextArea dem = new TextArea(20,5);
  Button but = new Button("Ok");
  int cptpos;

  Message(Frame dw,String title,boolean modal)
    {
      super(dw,title,modal);
      parent=dw;
      setLayout(new GridLayout(2,1));
      add(dem);
      add(but);
    }

  public void set_text(String txtin)
    {
      dem.setText(txtin);
    }

  public boolean action(Event e, Object arg)
    {
      if(e.target ==but)
	{
	  hide();
          return true;
	}
      return false;
    }
}
