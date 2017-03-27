import java.io.*;
import java.net.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

class Server
{
ServerSocket serversock;
ArrayList<Socket> clients;
ArrayList<Thread> threads;
ArrayList<String> clientnames;
ArrayList<String> clientipadd;
JFrame frame;
JPanel panel1,panel2,panel3;
JTextField tfield;
JTextArea tarea;
JTable clientlist;
JButton sendbutton,imgbutton,videobutton,audiobutton,filebutton;
String Name,ipadd;
int maxclients,clientscnnctd;

public Server()
{
try
{
frame=new JFrame("Server");
panel1=new JPanel();
panel2=new JPanel();
panel3=new JPanel();
tfield=new JTextField();
tarea=new JTextArea();
DefaultTableModel tmodel=new DefaultTableModel();
clientlist=new JTable(tmodel);
sendbutton=new JButton("Send");
imgbutton=new JButton("IMG");
videobutton=new JButton("VIDEO");
audiobutton=new JButton("AUDIO");
filebutton=new JButton("FILE");

tmodel.addColumn("PCName");
tmodel.addColumn("IP Address");
clientlist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
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
clientlist.setEnabled(false);
frame.setSize(500,500);
frame.setVisible(true);

serversock=new ServerSocket(5000);
maxclients=50;
clientscnnctd=0;
Name=InetAddress.getLocalHost().getHostName();
ipadd=InetAddress.getLocalHost().getHostAddress();
clients=new ArrayList<Socket>();
threads=new ArrayList<Thread>();
clientnames=new ArrayList<String>();
clientipadd=new ArrayList<String>();

while(clientscnnctd<=50)
{
Socket serverclient=serversock.accept();

ObjectInputStream oiserver=new ObjectInputStream(serverclient.getInputStream());
String nm=(String)oiserver.readObject();
String ip=(String)oiserver.readObject();

clientnames.add(nm);
clientipadd.add(ip);
clients.add(serverclient);
tmodel.addRow(new Object[]{nm,ip});
clientscnnctd++;

for(int i=0;i<clientscnnctd-1;i++)
{
Socket sk=clients.get(i);
ObjectOutputStream ooserver=new ObjectOutputStream(sk.getOutputStream());
ooserver.writeObject(0);
ooserver.writeObject(nm);
ooserver.writeObject(ip);

ooserver=new ObjectOutputStream(serverclient.getOutputStream());
ooserver.writeObject(0);
ooserver.writeObject(clientnames.get(i));
ooserver.writeObject(clientipadd.get(i));
}

System.out.println("CONNECTED");

Thread t=new Thread(new Connect(serverclient));
threads.add(t);
t.start();
}
}
catch(Exception e)
{
System.out.println(e.getMessage());
}
}

public static void main(String args[])
{
new Server();
}

public void receiveandsend(Socket client)
{
try
{
while(true)
{
ObjectInputStream oiserver=new ObjectInputStream(client.getInputStream());
String nm=(String)oiserver.readObject();
int optn=(int)oiserver.readObject();
Object o=oiserver.readObject();
Socket s=clients.get(clientnames.indexOf(nm));
System.out.println(nm);

ObjectOutputStream ooserver=new ObjectOutputStream(s.getOutputStream());
ooserver.writeObject(optn);
ooserver.writeObject(o);
}
}
catch(Exception e)
{
System.out.println("yes "+e.getMessage());
}
} 


class Connect implements Runnable
{
Socket client;
public Connect(Socket s)
{
client=s;
}
public void run()
{
receiveandsend(client);
}
}


class Sender implements ActionListener
{
Socket s;
public void actionPerformed(ActionEvent e1)
{
int row = clientlist.getSelectedRow();
String nm=(String)(clientlist.getValueAt(row, 0));

if((JButton)(e1.getSource())==sendbutton)
{
Iterator it=clients.iterator();
while(it.hasNext())
{
s=(Socket)it.next();
Text t=new Text(tfield,s);
t.send(nm,tarea);
}
}
else if((JButton)(e1.getSource())==imgbutton)
{
Iterator it=clients.iterator();
while(it.hasNext())
{
s=(Socket)it.next();
Image i=new Image(frame,s);
i.send(nm,tarea);
}
}
else if((JButton)(e1.getSource())==audiobutton)
{
Iterator it=clients.iterator();
while(it.hasNext())
{
s=(Socket)it.next();
Audio a=new Audio(frame,s);
a.send(nm,tarea);
}
}
else if((JButton)(e1.getSource())==videobutton)
{
Iterator it=clients.iterator();
while(it.hasNext())
{
s=(Socket)it.next();
Video v=new Video(frame,s);
v.send(nm,tarea);
}
}
else if((JButton)(e1.getSource())==filebutton)
{
Iterator it=clients.iterator();
while(it.hasNext())
{
s=(Socket)it.next();
Files f=new Files(frame,s);
f.send(nm,tarea);
}
}
}
}
}