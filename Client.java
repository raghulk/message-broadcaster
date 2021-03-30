import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.*;
import java.io.*;

public class Client extends JFrame implements ActionListener
{

    PrintWriter pw;
    BufferedReader br;
    Socket s;

    JMenu jmenu;
    JMenuBar jmenubar;
    JMenuItem exit;
    JTextArea msg_All;

    JTextField msg_send;
    JScrollPane jsp;

    JPanel bottom_area = new JPanel();

    JButton sendButton;
    public String message;
    private Object lock = new Object();

    public static void main(String[] args)
    {
        new Client().execute();
    }

    public Client()
    {
        this.setTitle("Client");
        this.setSize(400,400);
        this.setLayout(new BorderLayout(10,10));

        jmenubar = new JMenuBar();
        jmenu = new JMenu("File");
        exit = new JMenuItem("Exit");

        jmenubar.add(jmenu);
        jmenu.add(exit);
        this.add(jmenubar,BorderLayout.NORTH);

        msg_All = new JTextArea();
        this.add(msg_All,BorderLayout.CENTER);

        bottom_area.setLayout(new FlowLayout(2));

        msg_send = new JTextField(20);
        bottom_area.add(msg_send);

        sendButton = new JButton("Send");
        bottom_area.add(sendButton);

        sendButton.addActionListener(this);
        exit.addActionListener(this);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(bottom_area, BorderLayout.SOUTH);
        try {
            s = new Socket("localhost", 12345);

            PrintWriter pw = new PrintWriter(new BufferedOutputStream(s.getOutputStream()));
            BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));

            Receive r = new Receive(br);
            r.start();
//            while (true) {
//                if (btnpressed) {
//                    //sendButton.addActionListener(this);
//                    pw.println(message);
//                    pw.flush();
//                }
//            }
        } catch (Exception e) {
            System.out.println("No server Available");
            return;
            // try {
//                 wait(10);
//             } catch (Exception e1) {   }
//             new GUI();
        }
    }

    public void actionPerformed(ActionEvent e) {
        String send = msg_send.getText();

        try {
            if (send.equals("quit")) {
                pw.println("quit");
                pw.flush();
                pw.close();
                s.close();
            }
            pw.println(send);
            pw.flush();

            Receive r = new Receive(br);
            r.start();
        } catch (Exception ex) {
            System.out.println("Error occurred");
        }
    }
    public void execute()
    {
//        GUI gui = new GUI();
        try
        {
            Socket s = new Socket("localhost",12345);

            pw = new PrintWriter(new BufferedOutputStream(s.getOutputStream()));
            br = new BufferedReader(new InputStreamReader(s.getInputStream()));
        }
        catch (Exception ex)
        {
            System.out.println("Client error occurred: Closing Client");
            return;
        }
    }

    public class Receive extends Thread {
        BufferedReader br;
        boolean flag = true;

        public Receive(BufferedReader _br) {
            br = _br;
        }

        public void run() {
            while (flag) {
                try {
                    String msg = "";
                    String read = "";
                    synchronized (lock) {
                        msg_All.setText("");
                        while ((read = br.readLine()) != null) {
                            msg = msg + br.readLine() + "\n";
                            msg_All.setText(msg);
                        }
                    }
                    break;
                } catch (Exception ex) {
                    System.out.println("Client/Server is not available");
                    flag = false;
                }
            }
        }
    }
}