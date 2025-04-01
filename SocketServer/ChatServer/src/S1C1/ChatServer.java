package S1C1;

import java.io.*;
import java.net.Socket;
import java.net.ServerSocket;

public class ChatServer {
    public static void main(String args[]) {

        ServerSocket serverSocket = null;
        String received;
        DataInputStream is;
        DataOutputStream os;
        Socket clientSocket = null;
        Boolean connected;


        try {
            serverSocket = new ServerSocket(10001);
        } catch (IOException e) {
            System.out.println(e);
        }


        System.out.println("The server started. To stop it press <CTRL><C>.");
        try {
            clientSocket = serverSocket.accept();
            is = new DataInputStream(clientSocket.getInputStream());
            os = new DataOutputStream(clientSocket.getOutputStream());
            connected=true;


            System.out.println("Connected to client.");

      /* As long as we receive data, echo that data back to the client. */
            while (connected) {
                received = is.readUTF();
                if (!received.equals("Ok")){
                    os.writeUTF("From server: " + received);
                }
                else{
                    System.out.println("Client closed connection.");
                    connected=false;
                }

            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}