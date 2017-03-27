import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class Text extends Info
{
Socket sock;
JTextField tfield;

public Text(JTextField f,Socket s)
{
tfield=f;
sock=s;
}


public void send(String nm,JTextArea tarea)
{
try
{
String m2=tfield.getText();
ObjectOutputStream sender=new ObjectOutputStream(sock.getOutputStream());
sender.writeObject(nm);
sender.writeObject(1);
sender.writeObject(m2);
tfield.setText("");
tarea.append("Client:"+m2+"\n");
}
catch(Exception e)
{
System.out.println(e.getMessage());
}
}

public void receive(JTextArea tarea,ObjectInputStream oiclient)
{
try
{
String m1=oiclient.readObject().toString();
tarea.append("Server:"+m1+"\n");
}
catch(Exception e)
{
System.out.println(e.getMessage());
}
}
}