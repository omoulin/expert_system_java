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
import java.awt.*;
import java.io.*;

public class AddUsers extends Frame
{
  Users us;
  Label noml=null;
  TextField nom=null;
  Label loginl =null;
  TextField login=null;
  Label passwdl=null;
  TextField passwd=null;
  Label droitl = null;
  TextField droit = null;
  Panel p1=null;
  Panel p2=null;
  Panel p3=null;
  Panel p4=null;
  Panel p5= null;
  Button addu = null;
  Button charge =null;
  Button remu = null;
  Button save = null;
  Button quit = null;
  Message dialmsg = null;

  public static void main(String args[])
    {
      AddUsers newuser = new AddUsers();
      newuser.move(100,100);
      newuser.resize(300,200);
      newuser.show();
    }
  AddUsers()
    {
      setTitle("Gestion des Utilisateurs");
      noml=new Label("Base File :");
      nom=new TextField();
      loginl = new Label("Login :");
      login= new TextField();
      passwdl=new Label("Passwd :");
      passwd=new TextField();
      droitl = new Label("Rights :");
      droit = new TextField();
      p1=new Panel();
      p2=new Panel();
      p3=new Panel();
      p4=new Panel();
      p5= new Panel();
      addu = new Button("Add");
      charge = new Button("Charge");
      remu = new Button("Remove");
      save = new Button("Save");
      quit = new Button("Quit");

      setLayout(new GridLayout(5,1));
      p1.setLayout(new GridLayout(1,2));
      p1.add(noml);
      p1.add(nom);
      add(p1);
      p2.setLayout(new GridLayout(1,2));
      p2.add(loginl);
      p2.add(login);
      add(p2);
      p3.setLayout(new GridLayout(1,2));
      p3.add(passwdl);
      p3.add(passwd);
      add(p3);
      p4.setLayout(new GridLayout(1,2));
      p4.add(droitl);
      p4.add(droit);
      add(p4);
      p5.setLayout(new GridLayout(1,5));
      p5.add(charge);
      p5.add(addu);
      p5.add(remu);
      p5.add(save);
      p5.add(quit);
      add(p5);
      addu.enable(false);
      remu.enable(false);
      save.enable(false);
      login.enable(false);
      passwd.enable(false);
      droit.enable(false);
    }
  public boolean action(Event e, Object arg)
    {
      if(e.target ==charge)
        {
          us = new Users(nom.getText());
          addu.enable(true);
          remu.enable(true);
          save.enable(true);
          login.enable(true);
          passwd.enable(true);
          droit.enable(true);
          dialmsg=new Message(this,"Information",true);
          dialmsg.set_text("The user's base "+nom.getText()+" as been loaded");
          dialmsg.move(100,100);
          dialmsg.resize(300,150);
          dialmsg.show();
        }
      if(e.target ==addu)
        {
          us.addUser(login.getText(),passwd.getText(),droit.getText());
          dialmsg=new Message(this,"Information",true);
          dialmsg.set_text("The user "+login.getText()+" as been added");
          dialmsg.move(100,100);
          dialmsg.resize(300,150);
          dialmsg.show();
        }
      if(e.target ==remu)
        {
          us.removeUser(login.getText());
          dialmsg=new Message(this,"Information",true);
          dialmsg.set_text("The user "+login.getText()+" as been removed");
          dialmsg.move(100,100);
          dialmsg.resize(300,150);
          dialmsg.show();
        }
      if(e.target ==quit)
	{
	  System.exit(0);
	  return true;
        }
      if(e.target==save)
        {
          us.saveUsers();
          dialmsg=new Message(this,"Information",true);
          dialmsg.set_text("The user's base "+nom.getText()+" as been saved");
          dialmsg.move(100,100);
          dialmsg.resize(300,150);
          dialmsg.show();
        }
      return false;
    }
}
