package S1CM.Servidor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerClientThread extends Lexico implements Runnable {
    String usr;
    LinkedBlockingQueue messageStack;
    DataInputStream is;
    Boolean connected = false;
    Socket clientSocket;
    HashMap clientMap;
    File f = new File("test.c");
    PrintWriter p;
    ArrayList<String> array;
    Lexico c;

    public ServerClientThread(String usr, Socket clientSocket, DataInputStream is, LinkedBlockingQueue messageStack, HashMap clientMap, ArrayList<String> array) throws FileNotFoundException, UnsupportedEncodingException, IOException {

        this.usr = usr;
        this.is = is;
        this.messageStack = messageStack;
        this.clientSocket = clientSocket;
        this.connected = true;
        this.clientMap = clientMap;
        this.array = array;
        c = new Lexico(array);
        if (!f.exists() && !f.isDirectory()) {
            p = new PrintWriter("test.c", "UTF-8");
        } else {
            // p=new PrintWriter(new FileWriter(f, true));
        }

    }

    @Override
    public void run() {
        String received;
        while (connected) {
            try {
                received = is.readUTF();
                processMessage(received);
            } catch( SocketException e){
                System.out.printf("Cliente [%s] cerrado inesperadamente por [Socket Exception: %s].\n", usr, e.getMessage());
                connected = false;
                clientMap.remove(usr);
            } catch (EOFException e) {
                System.out.println("EOF Exception:  " + e.getMessage());
                connected = false;
                clientMap.remove(usr);
            } catch (IOException e) {
                System.out.println("IOException, failed to read message from " + usr);
            } 
        }
    }
    
    void processMessage(String received){
            System.out.println("RECEIVED " + received);
            if (!received.equals("compilar")) {
                System.out.println("Adding message to stack");
                messageStack.add(usr + ": " + received);

                array.add(received);


            } else {
                try {
                    new DataOutputStream(clientSocket.getOutputStream()).writeUTF("compilar");
                    p = new PrintWriter("test.c", "UTF-8");
                    for (int i = 0; i < array.size(); i++) {
                        p.print(array.get(i));
                    }
                    //  c.imprimir(p,received,f,clientSocket);
                    p.println("}");
                    p.close();
                    final String file = System.getProperty("user.dir");
                    String src = file + "\\test.bat";
                    String[] arg = new String[]{"cmd.exe", "/k", "C:\\msys64\\mingw64.exe", "start", src};
                    ProcessBuilder proc = new ProcessBuilder(arg);
                    Process process = proc.start();
                    process.waitFor();
                    File errorcomp = new File("compilererror.txt");
                    File error = new File("error.txt");
                    File salida = new File("salida.txt");
                    if (errorcomp.exists() && errorcomp.isDirectory()) {
                        Scanner in;
                        in = new Scanner(new FileReader("compileerror.txt"));
                        StringBuilder sb = new StringBuilder();
                        String outString;
                        while (in.hasNext()) {
                            sb.append(in.next());
                        }
                        in.close();
                        outString = sb.toString();
                        if(outString.length()>0){
                        new DataOutputStream(clientSocket.getOutputStream()).writeUTF(outString);
                        }
                    }
                      if (error.exists() && error.isDirectory()) {
                        Scanner in;
                        in = new Scanner(new FileReader("error.txt"));
                        StringBuilder sb = new StringBuilder();
                        String outString;
                        while (in.hasNext()) {
                            sb.append(in.next());
                        }
                        in.close();
                        outString = sb.toString();
                        if(outString.length()>0){
                        new DataOutputStream(clientSocket.getOutputStream()).writeUTF(outString);
                        }
                    }
                        if (salida.exists() &&salida.isDirectory()) {
                        Scanner in;
                        in = new Scanner(new FileReader("salida.txt"));
                        StringBuilder sb = new StringBuilder();
                        String outString;
                        while (in.hasNext()) {
                            sb.append(in.next());
                        }
                        in.close();
                        outString = sb.toString();
                        if(outString.length()>0){
                            System.out.println(outString);
                        new DataOutputStream(clientSocket.getOutputStream()).writeUTF(outString);
                        }
                    }
                    
                    array.clear();
                } catch (IOException e) {
                    System.out.println("IOException, failed to close" + usr + " connection");
                    p.close();                    
                } catch (InterruptedException ex) {
                    Logger.getLogger(ServerClientThread.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println(usr + " closed connection.");
                //  connected=false;
            }        
    }
}
