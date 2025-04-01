package S1CM.Cliente;

import javax.xml.crypto.Data;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;

public class ClientReceiverThread implements Runnable {

    Boolean connected;
    DataInputStream is;
    String message;
    Thread stop;

    public ClientReceiverThread(Boolean connected, DataInputStream is){
        this.connected=connected;
        this.is=is;
    }

    @Override
    public void run() {

        while(connected){
            try {
                message = is.readUTF();
            } catch(EOFException e){
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (message.equals("Ok")){
                connected=false;
            }

            System.out.println(message);

        }
    }
}
