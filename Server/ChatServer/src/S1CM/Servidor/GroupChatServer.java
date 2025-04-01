package S1CM.Servidor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class GroupChatServer {
    public static void main(String args[]) {

        ServerSocket serverSocket = null;
        String received;
        DataInputStream is;
        DataOutputStream os;
        Socket clientSocket = null;
        LinkedBlockingQueue messageStack = new LinkedBlockingQueue();
        HashMap<String,DataOutputStream> clientMap = new HashMap<>();
        ArrayList<String> array= new ArrayList<>();
       // array.add(0,"public class test {\npublic static void Main(String args[]){\n");
        try {
            serverSocket = new ServerSocket(10001);
        } catch (IOException e) {
            System.out.println(e);
        }

        new Thread(new ServerBroadcastThread(messageStack,clientMap)).start();

        System.out.println("The server started. To stop it press <CTRL><C>.");

        while (true){
            try {
                clientSocket = serverSocket.accept();
                is = new DataInputStream(clientSocket.getInputStream());
                os = new DataOutputStream(clientSocket.getOutputStream());
                String newUsr = is.readUTF();
                System.out.println(newUsr+" connected");
                os.writeUTF("Connected");
                new Thread(new ServerClientThread(newUsr,clientSocket,is,messageStack,clientMap,array)).start();
                clientMap.put(newUsr,os);
                 
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }
}
       