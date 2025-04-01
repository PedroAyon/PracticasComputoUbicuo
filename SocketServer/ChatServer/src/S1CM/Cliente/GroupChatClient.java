package S1CM.Cliente;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class GroupChatClient {
    public static void main(String args[]) {

        Socket clientSocket = null;
        DataInputStream is=null;
        DataOutputStream os=null;
        Boolean connected;

        try {
            //clientSocket = new Socket("0.tcp.ngrok.io", 13898);
            clientSocket = new Socket("0.tcp.ngrok.io", 10001);
            
            is = new DataInputStream(clientSocket.getInputStream());
            os = new DataOutputStream(clientSocket.getOutputStream());
        }
        catch (UnknownHostException e) {
            System.err.println("Don't know about host");
        }
        catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to host");
        }

        if (clientSocket != null && os != null && is != null) {
            connected=true;
            new Thread (new ClientSenderThread(connected,os)).start();
            new Thread (new ClientReceiverThread(connected,is)).start();

        }
    }
}