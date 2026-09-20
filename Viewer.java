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
import java.net.*;

public class Viewer extends Frame
{
  TextArea Input=null;
  TextArea Output=null;
  Panel p1=null;
  Panel p2 = null;
  Button execute = null;
  Button quit = null;
  String Base;
  String Port;
  String Login;
  String Passwd;
  Label logued=null;

  public static void main(String args[])
    {
      Viewer newviewer = new Viewer(args[0],args[1],args[2],args[3]);
      newviewer.move(100,100);
      newviewer.resize(400,400);
      newviewer.show();
    }
  Viewer(String bas,String prt,String l,String p)
    {
      Base=bas;
      Port=prt;
      Login=l;
      Passwd=p;
      setTitle("Testeur de Requetes");
      Input=new TextArea(20,5);
      Output=new TextArea(20,5);
      p1=new Panel();
      execute = new Button("Execute");
      quit = new Button("Quit");
      setLayout(new GridLayout(3,1));
      add(Input);
      p1.setLayout(new GridLayout(1,2));
      p1.add(execute);
      p1.add(quit);
      p2=new Panel();
      p2.setLayout(new GridLayout(2,1));
      p2.add(p1);
      logued= new Label("Vous etes connecte sur "+Base+":"+Port+" en tant que "+Login);
      p2.add(logued);
      add(p2);
      add(Output);
      Message m = new Message(this,"S.U.P.E.R. Debuger",true);
      m.set_text("This Tools is for debuging purpose only !\n\n(c) Heudiasyc U.T.C. Compi�gne\nCoded by O.Moulin and J.L.Sanz");
      m.move(150,150);
      m.resize(300,200);
      m.show();

    }
  public boolean action(Event e, Object arg)
    {
      Crypteur crypte = new Crypteur("152653254897225",15);
      String str;
      String str2;
      String str3;
      Float x= Float.valueOf(Port);
      if(e.target ==execute)
        {
          str=new String(Login);
          str2=crypte.crypteString(new String(Passwd));
          str3=new String("BEGINID\n"+str+"\n"+str2+"\n"+"ENDID\n"+Input.getText());
          commandServer(str3,Base,x.intValue());
        }
       if(e.target ==quit) 	{
	  System.exit(0);
	  return true;
        }
      return false;
    }

  void commandServer(String cmd,String host,int prt)
    {
      Socket s;
      PrintStream out = null;
      InputStream in = null;
      DataInputStream inline = null;
      String choix=new String("ok");

      Output.appendText("\nGUI : Initializing AI Server connection");
      try
        {
          s = new Socket (host,prt);
        }
      catch (IOException ex)
        {
          Output.appendText("\nGUI : Error Connecting AI server : " +ex);
	  return;
        }
      Output.appendText("\nGUI : Connected");
      try
        {
	  out= new PrintStream (s.getOutputStream());
        }
      catch (Exception ex)
        {
	  Output.appendText("\nGUI : Error Opening Orders transmission : "+ex);
        }
      try
        {
	  out.println(cmd);
        }
      catch (Exception ex)
        {
	  Output.appendText("\nGUI : Error Writing Orders :"+ex);
        }
      try
        {
         in = s.getInputStream();
	   inline = new DataInputStream(in);
      }
    catch (Exception ex)
      {
	Output.appendText("\nGUI : Error building streams  :"+ex);
      }
    int nbytes;
    byte b[] = new byte[1024];
    String str = new String("INIT");
    try
      {
	while(!str.equals("ENDT"))
	  {
	    str = new String(inline.readLine());
            if (!str.equals("ENDT"))
              {
                Output.appendText("\n"+str);
              }
          }
      }
    catch (Exception ex)
      {
	Output.appendText("\nGUI : Error Getting Response Stream :"+ex);
      }
    Output.appendText("\nGUI : UnConnected");
    try
      {
        s.close();
      }
    catch (IOException ex)
      {
	Output.appendText("\nGUI : Error Closing connection to AI server : "
+ex); 	return;
      }
  }
}
