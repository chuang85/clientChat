package client;

import static java.lang.System.out;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public final class  ChatUsers extends JFrame implements ActionListener {
    /**
	 * 
	 */
	private static final long serialVersionUID = 4593394265614583520L;
	String username;
    PrintWriter pw;
    BufferedReader br;
    JTextArea  chatmsg;
    JTextField chatip;
    JButton send,exit;
    Socket chatusers;
    
    public ChatUsers(String uname,String servername) throws Exception {
        super(uname); 
        this.username = uname;
        chatusers  = new Socket(servername,80);
        br = new BufferedReader( new InputStreamReader( chatusers.getInputStream()) ) ;
        pw = new PrintWriter(chatusers.getOutputStream(),true);
        pw.println(uname);
        buildInterface();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        new MessagesThread().start(); 
    }
    
    public void buildInterface() {
        send = new JButton("Send");
        exit = new JButton("Exit");
        chatmsg = new JTextArea();
        chatmsg.setRows(30);
        chatmsg.setColumns(50);
        chatmsg.setEditable(false);
        chatip  = new JTextField(50);
        JScrollPane sp = new JScrollPane(chatmsg, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        add(sp,"Center");
        JPanel bp = new JPanel( new FlowLayout());
        bp.add(chatip);
        
        bp.add(send);
        bp.add(exit);
        bp.setBackground(Color.LIGHT_GRAY);
        bp.setName("Kstark Chat Application Using Socket");
        add(bp,"North");
        send.addActionListener(this);
        exit.addActionListener(this);
        setSize(500,300);
        setVisible(true);
        pack();
    }
    
    @Override
    public void actionPerformed(ActionEvent evt) {
        if ( evt.getSource() == exit ) {
            pw.println("end"); 
            System.exit(0);
        } else {
            
            pw.println(chatip.getText());
            chatip.setText(null);
        }
    }
    
    public static void main(String ... args) {
    
        
        String SetUserName = JOptionPane.showInputDialog(null,"Please enter your name :", "Kstark Chat Application",
             JOptionPane.PLAIN_MESSAGE);
        String servername = "localhost";  
        try {
            new ChatUsers( SetUserName ,servername);
        } catch(Exception ex) {
            out.println( "Error:" + ex.getMessage());
        }
        
    } 
    class  MessagesThread extends Thread {
        @Override
        public void run() {
            String line;
            try {
                while(true) {
                    line = br.readLine();
                    chatmsg.append(line + "\n");
                }
            } catch(Exception ex) {}
        }
    }
} 