import java.io.*;
import java.net.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

class Client
{
Socket client;
JFrame frame;
JPanel panel1,panel2,panel3;
JTextField tfield;
JTextArea tarea;
JTable clientlist;
DefaultTableModel tmodel;
JButton sendbutton,imgbutton,videobutton,audiobutton,filebutton;
String Name,ipadd,nm="";
int status;
static int count;

public Client()
{
try
{
frame=new JFrame("Client");
panel1=new JPanel();
panel2=new JPanel();
panel3=new JPanel();
tfield=new JTextField();
tarea=new JTextArea();
tmodel=new DefaultTableModel();
clientlist=new JTable(tmodel);
sendbutton=new JButton("Send");
imgbutton=new JButton("IMG");
videobutton=new JButton("VIDEO");
audiobutton=new JButton("AUDIO");
filebutton=new JButton("FILE");

tmodel.addColumn("PCName");
tmodel.addColumn("IP Address");
clientlist.setRowSelectionAllowed(true);
ListSelectionModel cellSelectionModel = clientlist.getSelectionModel();
cellSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
cellSelectionModel.addListSelectionListener(new Sender());
    
sendbutton.addActionListener(new Sender());
imgbutton.addActionListener(new Sender());
videobutton.addActionListener(new Sender());
audiobutton.addActionListener(new Sender());
filebutton.addActionListener(new Sender());

panel1.setLayout(new GridLayout(2,1));
panel1.add(new JScrollPane(clientlist));
panel1.add(new JScrollPane(tarea));
panel2.setLayout(new GridLayout(1,2));
panel2.add(tfield);
panel2.add(sendbutton);
panel3.setLayout(new GridLayout(4,1));
panel3.add(imgbutton);
panel3.add(videobutton);
panel3.add(audiobutton);
panel3.add(filebutton);
frame.add(panel1);
frame.add(BorderLayout.SOUTH,panel2);
frame.add(BorderLayout.EAST,panel3);

tarea.setEditable(false);
frame.setSize(500,500);
frame.setVisible(true);

Name=InetAddress.getLocalHost().getHostName();
ipadd=InetAddress.getLocalHost().getHostAddress();
status=0;
client=new Socket("localhost",5000);

ObjectOutputStream ooclient=new ObjectOutputStream(client.getOutputStream());
ooclient.writeObject(Name);
ooclient.writeObject(ipadd);

status=1;
}
catch(Exception e)
{
System.out.println("yoyo"+e.getMessage());
}
}

public void receive()
{
try
{
while(true)
{
ObjectInputStream oiclient=new ObjectInputStream(client.getInputStream());
int optn=(int)oiclient.readObject();
System.out.println(optn);

if(optn==0)
{
String nm=(String)oiclient.readObject();
String ip=(String)oiclient.readObject();
tmodel.addRow(new Object[]{nm,ip});
}
else if(optn==1)
{
Text t=new Text(tfield,client);
t.receive(tarea,oiclient);
}
else if(optn==2)
{
Image i=new Image(frame,client);
i.receive(tarea,oiclient);
}
else if(optn==3)
{
Audio a=new Audio(frame,client);
a.receive(tarea,oiclient);
}
else if(optn==4)
{
Video v=new Video(frame,client);
v.receive(tarea,oiclient);
}
else if(optn==5)
{
Files f=new Files(frame,client);
f.receive(tarea,oiclient);
}

}
}
catch(Exception e)
{
System.out.println("yesyes"+e.getMessage());
}
}


public static void main(String args[])
{
new Client().receive();
}

class Sender implements ActionListener,ListSelectionListener
{
public void valueChanged(ListSelectionEvent e)
{
int row = clientlist.getSelectedRow();
nm=(String)(clientlist.getValueAt(row, 0));
}

public void actionPerformed(ActionEvent e1)
{
if((JButton)(e1.getSource())==sendbutton)
{
System.out.println(nm);
Text t=new Text(tfield,client);
t.send(nm,tarea);
}
else if((JButton)(e1.getSource())==imgbutton)
{
Image i=new Image(frame,client);
i.send(nm,tarea);
}

else if((JButton)(e1.getSource())==audiobutton)
{
Audio a=new Audio(frame,client);
a.send(nm,tarea);
}

else if((JButton)(e1.getSource())==videobutton)
{
Video v=new Video(frame,client);
v.send(nm,tarea);
}

else if((JButton)(e1.getSource())==filebutton)
{
Files f=new Files(frame,client);
f.send(nm,tarea);
}
clientlist.getSelectionModel().clearSelection();
}
}
}