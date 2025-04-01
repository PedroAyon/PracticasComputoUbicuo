package S1CM.Servidor;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by gerardo on 2/04/17.
 */
public class ServerBroadcastThread implements Runnable {

    protected HashMap<String,DataOutputStream> clientMap;
    protected LinkedBlockingQueue<String> messageStack;
    protected String message=null;
    protected DataOutputStream stream;
    int num = new Random().nextInt(10)+1;

    public ServerBroadcastThread(LinkedBlockingQueue messageStack, HashMap clientMap){
        this.clientMap=clientMap;
        this.messageStack=messageStack;
    }

    @Override
    public void run() {

        while(true){
            try {
                message = messageStack.take();
                System.out.println("Message stack took message");
                String sender = message.split(":")[0];
                if (message.contains(":") && message.split(":")[1].trim().equals("" + num)) {
                    messageStack.add(sender + " adivinó el numero!");
                    num = new Random().nextInt(10)+1;
                }
                for(String usr: clientMap.keySet()){
                    System.out.println("Sending message to "+usr);
                    stream=clientMap.get(usr);
                    stream.writeUTF(message);
                }


            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException ex) {
                Logger.getLogger(ServerBroadcastThread.class.getName()).log(Level.SEVERE, null, ex);
            }/* catch (IOException e) {
                e.printStackTrace();
            }*/

        }
    }
}
