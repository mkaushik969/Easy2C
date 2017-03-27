import java.io.*;
import javax.swing.*;

abstract class Info
{
protected int type;
protected String path;
protected int size;

public abstract void send(String nm,JTextArea tarea);
public abstract void receive(JTextArea tarea,ObjectInputStream oiclient);
}