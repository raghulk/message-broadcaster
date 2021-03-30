import java.net.*;
import java.util.*;
import java.io.*;

public class Server
{
    Vector<PrintWriter> clientList = new Vector<PrintWriter>();
    int counter = 0;

    public static void main(String[] args)
    {
        new Server().execute();
    }

    public void execute()
    {
        System.out.println("Accepting Connection on port 12345..");
        try
        {
            ServerSocket ss = new ServerSocket(12345);

            while(true)
            {
                Socket s = ss.accept();
                counter++;
                System.out.println("Client" + counter + " connecting");
                new ServerThread(s).start();
            }
        }
        catch(Exception ex)
        {
            //ex.printStackTrace();
            System.out.println("Unexpected error: closing server " +ex.getMessage());
        }

    }

    class ServerThread extends Thread
    {
        Socket s;
        final int memberNo = counter;

        public ServerThread(Socket clientSocket)
        {
            try
            {
                s = clientSocket;
                System.out.println("Client No " + memberNo + ". You are connected");
            }catch(Exception ex)
            {
                ex.printStackTrace();
            }

        }

        public void run()
        {
            try
            {
                BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
                PrintWriter pw = new PrintWriter(new BufferedOutputStream(s.getOutputStream()));
                clientList.add(pw);

                //custom messages for different clients
                if (memberNo == 1){
                    broadcastMessage("We have our first Client");
                }

                if (memberNo == 2){
                    broadcastMessage("Hello");
                }
                if (memberNo == 3){
                    broadcastMessage("Hi There");
                }
                else{
                    broadcastMessage("We have client no" + memberNo);
                }


                while(true)
                {
                    String msg = br.readLine();
                    System.out.println("Server - MSG received = " + "Client No " + memberNo + " " + msg);
                    if(msg.equals("quit"))
                    {
                        br.close();
                        pw.close();
                        s.close();
                        break;
                    }
                    broadcastMessage("Client No " + memberNo + " :" + msg);
                }
            }
            catch(Exception ex)
            {
                System.out.println("Client socket closing " + ex.getMessage());
            }
        }

        private void broadcastMessage(String msg)
        {
            for(PrintWriter p : clientList)
            {
                try
                {
                    p.println(msg);
                    p.flush();
                }
                catch(Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        }
    }
}