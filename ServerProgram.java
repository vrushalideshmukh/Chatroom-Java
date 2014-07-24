/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.DataInputStream;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.net.Socket;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Scanner;

/**
 *
 * @author Akash
 */

//// Main Program for server 	
public class ServerProgram {
    
    
    private static int hr;
    private static int min;
    private static String time;
    /// Code for text colout in terminal  
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String ANSI_BOLD = "\033[0;1m";
    public static final String first = "\033[1m";
    public static final String second = "\033[0m";
    /**
     * @param args the command line arguments	
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        ServerSocket s = null;
        Socket conn = null;
        PrintStream out = null;
        Scanner scan;
        BufferedReader in = null;
        String message = null;
        
        String name = null;
        ArrayList<Clients> list = new ArrayList<Clients>() ;
        try
        {
            s = new ServerSocket(5000,10);
            System.out.println("Waiting for accepting incoming requests");
            while(true)
            {
            conn = s.accept();
            System.out.println("Connection accepted from " +conn.getInetAddress().getHostName());
            out = new PrintStream(conn.getOutputStream());
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            out.print(ANSI_CYAN+ "Enter your Name : "+second);
            name = in.readLine();
            Date date = new Date();   // given date
            Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
           calendar.setTime(date);   // assigns calendar to given date 
           calendar.get(Calendar.HOUR_OF_DAY); // gets hour in 24h format
           hr = calendar.get(Calendar.HOUR);        // gets hour in 12h format
           min = calendar.get(Calendar.MINUTE);    
            if(calendar.get(Calendar.AM_PM)==0)
                   time = "AM";
            else
                time=" PM";
        
            Clients client = new Clients();
            System.out.println(name);
            client.name = name;
            client.hr = hr;
            client.min = min;
            client.time = time;
            client.ip = conn.getInetAddress().getHostName();
            System.out.println("Added name");
            client.socket = conn;
            list.add(client);
            System.out.println(list);
            
            new client_handler(conn,list,client).start();
            
            //new DemoThread().start();
            
            out.flush();
            
            out.println(ANSI_BOLD+"Connected to server 1.0"+second);
            out.flush();
            }
          //  new client_handler(conn).start();
            
        }
        
        catch(IOException e)
                {
                       System.err.println("IOException");
                }
    
       
    }
    public static void echo(String msg)
    {
        System.out.println(msg);
    }

  
}

//Main Class ended

// Client model to save name and ip adress of logged user
class Clients {

    
    Socket socket;
    String ip;
    int hr;
    int min;
    String time;
    //String name;
    String name;

}

// Client class ended

// Thread class to handle multithreading for connected users

class client_handler extends Thread {

public static final String ANSI_RESET = "\u001B[0m";
public static final String ANSI_BLACK = "\u001B[30m";
public static final String ANSI_RED = "\u001B[31m";
public static final String ANSI_GREEN = "\u001B[32m";
public static final String ANSI_YELLOW = "\u001B[33m";
public static final String ANSI_BLUE = "\u001B[34m";
public static final String ANSI_PURPLE = "\u001B[35m";
public static final String ANSI_CYAN = "\u001B[36m";
public static final String ANSI_WHITE = "\u001B[37m";
public static final String ANSI_BOLD = "\033[0;1m";
public static final String first = "\033[1m";
public static final String second = "\033[0m";
        

    PrintStream out = null;
    private ArrayList<Clients> list1 = new ArrayList<Clients>() ;
    private Socket socket = null;
    private Socket socket1 = null;
    private PrintStream out1 =null;
    String msg;
    
    String name;
    Clients client;
    private long t;
    int hr;
    int min;
 
    private String time;
    //String ANSI_CSI = "\\x1b[";
    public client_handler(Socket conn, ArrayList<Clients> list,Clients client) {
     

        this.list1 = list;
        this.socket = conn;
        this.client = client;
        Date date = new Date();   // given date
        Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
        calendar.setTime(date);   // assigns calendar to given date 
        calendar.get(Calendar.HOUR_OF_DAY); // gets hour in 24h format
         hr = calendar.get(Calendar.HOUR);        // gets hour in 12h format
         min = calendar.get(Calendar.MINUTE);    
            if(calendar.get(Calendar.AM_PM)==0)
                   time = "AM";
            else
                time=" PM";
        
        
    }
    
    @Override 
    public void run()
    {
        String line , input = "";   
        try {
                    out = new PrintStream(socket.getOutputStream());
                    out.println("\033[31mWelcome to server  \033[0m");
                    out.flush();
                    show_all_user(list1, client);
                    JoinedMessageToAll(list1);
                    out.print("\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b");
                    DataInputStream in = new DataInputStream(socket.getInputStream());
                    out.println("\033[31mEntered in chatroom   \033[0m");
                    out.flush();
                    out.print("\n");
                    out.print(first+client.name+second+"("+hr+":"+min+time+ ")"+  ": ");
                    
                    
                    while((line = in.readLine()) != null && !line.equals("Bye")) 
                   {
                       //out.print(client.name+" :");
                       if(line.equals("Show"))
                    {
                        show_all_user(list1,client);
                        //break;
                    }
                       
                       else
                       {
                       transferMessagetoAll(list1, line,client.name);
                       }
                   }
                    
                    LeftMessageToAll(list1);
                    list1.remove(client);
                    socket.close();
                    
        } catch (IOException e) {
        }
    }
    
      public void JoinedMessageToAll(ArrayList<Clients> list) throws IOException
    {
    for(int i=0;i<list.size();i++)
    {        
        socket1 = list1.get(i).socket;
        if(socket1 == client.socket){
            
        }
        else{
        out1 = new PrintStream(socket1.getOutputStream());
        out1.print("\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b");
        out1.print(client.name);
        out1.println( " Joined chatroom");
        out1.flush();
        //out1.println("----------------------------------------------------------");
        out1.print(first+list1.get(i).name+second+"("+hr+":"+min+time+ ")"+  ": ");
        }
        }
    }
      
    public void transferMessagetoAll(ArrayList<Clients> list,String message,String name) throws IOException
    {
       
    for(int i=0;i<list.size();i++)
    {   
        socket1 = list1.get(i).socket;
        
        if(socket1 == client.socket){
            out1 = new PrintStream(client.socket.getOutputStream());
            out1.print(first+client.name+second+"("+hr+":"+min+time+ ")"+ " : ");
            out1.flush();
        }
        else    
        {
        out1 = new PrintStream(socket1.getOutputStream());
        out1.print("\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b");
       //out1.print(ANSI_CSI + "2A"); 
        //out1.print(ANSI_CSI + "K"); 
        out1.print( "\r" +first+ANSI_CYAN   +name+second+second+"("+hr+":"+min+time+ ")"+ " :"+message);
        out1.flush();
        out1.print("\n");
        out1.print(first+list1.get(i).name+second+"("+hr+":"+min+time+ ")"+  ": ");
        out1.flush();
        
        
        }
    }
    
}

    private void LeftMessageToAll(ArrayList<Clients> list) throws IOException {
        for(int i=0;i<list.size();i++)
    {        
        socket1 = list1.get(i).socket;
        out1 = new PrintStream(socket1.getOutputStream());
        out1.print("\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b");
        out1.println("\r"+client.name+ " Left" );
        //out1.println("----------------------------------------------------------");
        
        out1.print( list1.get(i).name+ ":");
        if(socket1 == client.socket)
        {
            out1.println("Thank you for visiting ");
            out1.println("To reconnect enter"+ANSI_BOLD+" telnet <IP> 5000"+second);
        }
        out1.flush();
    }
    }

    private void show_all_user(ArrayList<Clients> list, Clients client) throws IOException {
         
        socket1 = client.socket;
        out1 = new PrintStream(socket1.getOutputStream());
        out1.println("----------------------------------------------------------");
        out1.println(ANSI_GREEN+ "Name                 IPAddress               Login Since"+second);
        out1.println("---------------------------------------------------------");
        for(int i=0;i<list1.size();i++)
         {        
        
        name = list1.get(i).name;
        String ip = list1.get(i).ip;
        out1.println(name+"                 "+ip+"               "+list1.get(i).hr+":"+list1.get(i).min+" "+list1.get(i).time);
        }
        out1.println("----------------------------------------------------------");
        out1.flush();
        
        out1.println(ANSI_BOLD+"          Reserved strings"+second);
        out.println(ANSI_GREEN+ "Show  ===>  List Users in Chatroom"+second);
        out.println(ANSI_GREEN+"Bye  ===>  Sign out from Chatroom"+second);
        out1.println("----------------------------------------------------------");

        out1.print(client.name+"("+hr+":"+min+ time+")"+ " : ");
        
    }
    
}

// Thread class ended
    

