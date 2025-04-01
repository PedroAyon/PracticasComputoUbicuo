package S1C1;

import java.io.*;
import java.net.Socket;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ChatClient {
    public static void main(String args[]) {

        Socket clientSocket = null;
        DataInputStream is=null;
        Scanner keyboard=null;
        DataOutputStream os=null;
        Boolean connected;

        try {
            clientSocket = new Socket("localhost", 10001);
            is = new DataInputStream(clientSocket.getInputStream());
            os = new DataOutputStream(clientSocket.getOutputStream());
            keyboard = new Scanner(System.in);
        }
        catch (UnknownHostException e) {
            System.err.println("Don't know about host");
        }
        catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to host");
        }


        if (clientSocket != null && os != null && is != null) {
            connected=true;
            /*
             * Keep on reading from/to the socket till we receive the "Ok" from the
             * server, once we received that then we break.
             */
            System.out.println("The client started. Type any text. To quit it type 'Ok'.");
            try {
                String response,message;
                while (connected) {

                    message=keyboard.nextLine();

                    os.writeUTF(message);
                    System.out.println("Se envio mensaje correctamente");

                    if(!message.equals("Ok")){

                        System.out.println("Intentando recibir mensaje");
                        response=is.readUTF();
                        System.out.println("Mensaje recibido correctamente");

                        if(!response.equals("Ok")){
                            System.out.println(response);
                        }

                        else{
                            connected=false;
                        }

                    }

                    if(message.equals("Ok")){
                        connected=false;
                        System.out.println("Closing connection to server.");
                    }




                }
        /*
         * Close streams and socket.
         */
                os.close();
                is.close();
                keyboard.close();
                clientSocket.close();
                System.out.println("Closed connection.");
            } catch (UnknownHostException e) {
                System.err.println("Trying to connect to unknown host: " + e);
            } catch (IOException e) {
                System.err.println("IOException:  " + e);
            }
        }
    }
}