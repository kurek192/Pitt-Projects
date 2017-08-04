//WILLIAM KUREK
//CS 1501
//SUMMER 2017
import java.util.*;
import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.math.*;

public class SecureChatClient extends JFrame implements Runnable, ActionListener {

    public static final int PORT = 8765;

    ObjectInputStream myReader;
    ObjectOutputStream myWriter;
    JTextArea outputArea;
    JLabel prompt;
    JTextField inputField;
    String myName, serverName;
    Socket connection;
    
    BigInteger E, N, key;
    String type;
    Random R;
    SymCipher cipher;

    public SecureChatClient() {
        try {

            myName = JOptionPane.showInputDialog(this, "Enter your user name: ");
            serverName = JOptionPane.showInputDialog(this, "Enter the server name: ");
            InetAddress addr
                    = InetAddress.getByName(serverName);
            connection = new Socket(addr, PORT);   // Connect to server with new
            // Socket
            myWriter
                    = new ObjectOutputStream(connection.getOutputStream());

            myWriter.flush();

            myReader
                    = new ObjectInputStream(
                            connection.getInputStream());   // Get Reader and Writer

            E = (BigInteger) myReader.readObject();
            N = (BigInteger) myReader.readObject();
            type = (String) myReader.readObject();
            System.out.println("E: " + E);
            System.out.println("N: " + N);
            System.out.println("encoding type: " + type);

            if (type.equals("Sub")) {
                System.out.println("Encryption type: Substitution");
                cipher = new Substitute();
            } else if (type.equals("Add")) {
                System.out.println("Encryption type: Add128");
                cipher = new Add128();
            }

            key = new BigInteger(1, cipher.getKey());
            System.out.println("Key: " + key);

            myWriter.writeObject(key.modPow(E, N));
            myWriter.flush();
            myWriter.writeObject(cipher.encode(myName));
            myWriter.flush();
            ///////// Send name to Server.  Server will need
            // this to announce sign-on and sign-off
            // of clients

            this.setTitle(myName);   // Set title to identify chatter

            Box b = Box.createHorizontalBox();  // Set up graphical environment for
            
            outputArea = new JTextArea(8, 30);  // user
            outputArea.setEditable(false);
            b.add(new JScrollPane(outputArea));

            outputArea.append("Welcome to the Chat Group, " + myName + "\n");

            inputField = new JTextField("");  // This is where user will type input
 
            inputField.addActionListener(this);

            prompt = new JLabel("Type your messages below:");
            Container c = getContentPane();
            

            c.add(b, BorderLayout.NORTH);
            c.add(prompt, BorderLayout.CENTER);
            c.add(inputField, BorderLayout.SOUTH);

            Thread outputThread = new Thread(this);  // Thread is to receive strings
            outputThread.start();     // from Server

            addWindowListener(
                    new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    try {

                        myWriter.writeObject(cipher.encode("CLIENT CLOSING"));
                        myWriter.flush();
                       
                    } catch (IOException io) {
                        System.out.println("Error disconnecting from server");
                    }
                    
                    //System.exit(0);
                    
                }
            }
            );

            setSize(500, 300);
            setVisible(true);

        } catch (Exception e) {
            System.out.println("Problem starting client!");
            System.exit(1);
        }
    }

    public void run() {
        while (true) {
            try {

                //decoding proceedure and printing 
                byte[] bytes = (byte[]) myReader.readObject();
                String currMsg = cipher.decode(bytes);
                System.out.println("\nDecryption");
                System.out.println("--------------------------");
                System.out.println("Array of bytes: " + new BigInteger(1, bytes));
                System.out.println("Decrypted array of bytes: " + new BigInteger(1, currMsg.getBytes()));
                System.out.println("Message: " + currMsg);
      
                outputArea.append(currMsg + "\n");
            } catch (Exception e) {
                System.out.println(e + ", closing client!");
                break;
            }
        }
        System.exit(0);
    }

    public void actionPerformed(ActionEvent e) {
        try {
            String currMsg = e.getActionCommand();   // Get input value
            inputField.setText("");
            String msg = myName + ":" + currMsg;
            System.out.println("\nEncryption");
            System.out.println("--------------------------");
            System.out.println("Message: " + msg);           
            System.out.println("Array of bytes: " + new BigInteger(msg.getBytes()).toString());
            byte[] encryptedMsg = cipher.encode(msg);
            System.out.println("Encrypted array of bytes: " + new BigInteger(encryptedMsg).toString());
            myWriter.writeObject(encryptedMsg);
            myWriter.flush();
        } catch (IOException io) {
            System.out.println("Error sending message");
        }
// Add name and send it
    }             // to Server

    public static void main(String[] args) {
        SecureChatClient JR = new SecureChatClient();
        JR.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }
}
