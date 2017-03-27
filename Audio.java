import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;


class Audio extends Info
{
JFrame frame;
int width,height;
String ext;
Socket sock;

public Audio(JFrame f,Socket s)
{
try
{
frame=f;
sock=s;
}
catch(Exception e)
{
System.out.println(e.getMessage());
}
}


public void send(String nm,JTextArea tarea)
{
try
{
FileFilter filter = new FileNameExtensionFilter("Audio File","mp3","aac","wma");
JFileChooser jf=new JFileChooser();
jf.setFileFilter(filter);
jf.showOpenDialog(frame);
File file=jf.getSelectedFile();
ObjectOutputStream sender=new ObjectOutputStream(sock.getOutputStream());
sender.writeObject(nm);
sender.writeObject(3);
sender.writeObject(file);
tarea.append("Server:"+file.getName()+"\n");
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
File source=(File)oiclient.readObject();
JFileChooser jf=new JFileChooser();
jf.showSaveDialog(frame);
File dest=jf.getSelectedFile();
dest.createNewFile();

InputStream in = new FileInputStream(source);
OutputStream out = new FileOutputStream(dest);
byte[] buf = new byte[1024];
int len;
while ((len = in.read(buf)) > 0) 
out.write(buf, 0, len);
in.close();
out.close();
tarea.append("Server:"+dest.getName()+"\n");
}
catch(Exception e)
{
System.out.println("Exception="+e.getMessage());
}

}

}