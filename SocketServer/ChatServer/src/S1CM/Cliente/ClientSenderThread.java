package S1CM.Cliente;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class ClientSenderThread implements Runnable {

    Scanner keyboard;
    DataOutputStream os;
    Boolean connected;
    Thread stop;


    public ClientSenderThread(Boolean connected, DataOutputStream os){

        this.keyboard= new Scanner(System.in);
        this.os=os;
        this.connected=connected;

    }

    public void setStop(Thread stop){
        this.stop=stop;
    }

    @Override
    public void run() {

        System.out.println("The client started. Type any text. To quit it type 'Ok'.");


        while(connected){
            String message = keyboard.nextLine();
            try {
                os.writeUTF(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (message.equals("Ok")){
                connected=false;
            }
        }

    }
}
