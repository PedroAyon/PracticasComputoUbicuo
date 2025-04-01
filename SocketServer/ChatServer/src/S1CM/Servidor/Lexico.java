/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package S1CM.Servidor;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 *
 * @author pc
 */
public class Lexico {
    protected static final Pattern CREAR = Pattern.compile(".*\\bcrea\\b.*");
    protected static final Pattern DECLARA = Pattern.compile(".*\\bdeclara\\b.*");
    protected static final Pattern CREA = Pattern.compile(".*\\bcrear\\b.*");
    protected static final Pattern MAYORIGUAL = Pattern.compile(".*\\bmayor igual\\b.*");
    protected static final Pattern MAYOR = Pattern.compile(".*\\bmayor\\b.*");
    protected static final Pattern MENOR = Pattern.compile(".*\\bmenor\\b.*");
    protected static final Pattern MENORIGUAL = Pattern.compile(".*\\bmenor igual\\b.*");
    protected static final Pattern IGUAL = Pattern.compile(".*\\bigual\\b.*");
    protected static final Pattern UN = Pattern.compile(".*\\bun\\b.*");
    protected static final Pattern DOS = Pattern.compile(".*\\bdos\\b.*");
    protected static final Pattern TRES = Pattern.compile(".*\\btres\\b.*");
    protected static final Pattern VARIABLE = Pattern.compile(".*\\bvariable\\b.*");
    protected static final Pattern ENTERO = Pattern.compile(".*\\bentero\\b.*");
    protected static final Pattern FLOTANTES = Pattern.compile(".*\\bflotante\\b.*");
    protected static final Pattern DOBLE = Pattern.compile(".*\\bdoble\\b.*");
    protected static final Pattern DOBLE2 = Pattern.compile(".*\\bDouble\\b.*");
    protected static final Pattern BOLEANA = Pattern.compile(".*\\bbooleano\\b.*");
    protected static final Pattern ESTRING = Pattern.compile(".*\\bString\\b.*");
    protected static final Pattern CHARAC = Pattern.compile(".*\\bchar\\b.*");
    protected static final Pattern TIPO = Pattern.compile(".*\\btipo\\b.*");
    protected static final Pattern LLAME = Pattern.compile(".*\\bllame\\b.*");
    protected static final Pattern LLAMADA = Pattern.compile(".*\\bllamada\\b.*");
    protected static final Pattern IF = Pattern.compile(".*\\bif\\b.*");
    protected static final Pattern DONDE = Pattern.compile(".*\\bdonde\\b.*");
    protected static final Pattern CERRAR = Pattern.compile(".*\\bcerrar\\b.*");
    protected static final Pattern PUNTO = Pattern.compile(".*\\bpunto y coma\\b.*");
    protected static final Pattern WHILE = Pattern.compile(".*\\bwhile\\b.*");
    protected static final Pattern DENTRO = Pattern.compile(".*\\bdentro\\b.*");
    protected static final Pattern DIGITOS = Pattern.compile(".*\\d+.*");
    protected static final Pattern MAS = Pattern.compile(".*\\bmás\\b.*");
    protected static final Pattern MENOS = Pattern.compile(".*\\bmenos\\b.*");
    protected static final Pattern POR = Pattern.compile(".*\\bpor\\b.*");
    protected static final Pattern ENTRE = Pattern.compile(".*\\bentre\\b.*");
    protected static final Pattern VALOR = Pattern.compile(".*\\bvalor\\b.*");
    protected static final Pattern CAMBIAR = Pattern.compile(".*\\bcambiar\\b.*");
    protected static final Pattern CAMBIA = Pattern.compile(".*\\bcambia\\b.*");
    protected static final Pattern BORRAR = Pattern.compile(".*\\bborrar\\b.*");
    protected static final Pattern NOMBRE = Pattern.compile(".*\\bnombre\\b.*");
    protected static final Pattern CLASE = Pattern.compile(".*\\bclase\\b.*");
    protected static final Pattern COMPILAR = Pattern.compile(".*\\bcompilar\\b.*");
    protected static final Pattern GUARDAR = Pattern.compile(".*\\bguardar\\b.*");
    protected static final Pattern MAIN = Pattern.compile(".*\\bprincipal\\b.*");
    protected ArrayList<String> code;
    protected PrintWriter p;
    
   public Lexico(){}
    public Lexico(ArrayList<String> code){
   // this.p=p;
    this.code=code;
    
    }
    
    
    protected void verificarmain(String received,Socket socket,PrintWriter p){
        if(received.toLowerCase().matches(String.valueOf(CREAR))&&received.matches(String.valueOf(CLASE))|| received.toLowerCase().matches(String.valueOf(CREA))&&received.matches(String.valueOf(CLASE))||received.matches(String.valueOf(DECLARA))&&received.matches(String.valueOf(CLASE))){
            if(received.matches(String.valueOf(MAIN)))
                    crearmain(received,socket,p);
                
                }
    }
    
    protected void verificarentero(String received,Socket socket,PrintWriter p) throws IOException{
         //VARIABLES ENTERO CON NOMBRE y valor
              if(received.toLowerCase().matches(String.valueOf(CREAR))&&received.matches(String.valueOf(VARIABLE))|| received.toLowerCase().matches(String.valueOf(CREA))||received.matches(String.valueOf(DECLARA))){
                if(received.matches(String.valueOf(ENTERO))){
                    if(received.matches(String.valueOf(IGUAL)))
                        declararenteroconvalor(received,socket,p);
                }
            } 
              // VARIABLES ENTERO CON NOMBRE
              if(received.toLowerCase().matches(String.valueOf(CREAR))&&received.matches(String.valueOf(VARIABLE))|| received.toLowerCase().matches(String.valueOf(CREA))&&received.matches(String.valueOf(VARIABLE))||received.matches(String.valueOf(DECLARA))&&received.matches(String.valueOf(VARIABLE))){
                if(received.matches(String.valueOf(ENTERO))){
                    if(!received.matches(String.valueOf(IGUAL)))
                        try {
                        declararentero(received,socket,p);
                    } catch (IOException ex) {
                        Logger.getLogger(ServerClientThread.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
    }
    
    
    public void verificarfor(String result,Socket socket,PrintWriter p) throws IOException{
     //FOR IDEAL
            if(result.toLowerCase().contains("foro")||result.toLowerCase().contains("ford")||result.toLowerCase().contains("for")) {
                if (result.matches(String.valueOf(UN))) {
                    if(result.matches(".*\\d+.*"))
                        if (result.matches(String.valueOf(MAYOR)) || result.matches(String.valueOf(MENOR))
                                || result.matches(String.valueOf(MAYORIGUAL)) || result.matches(String.valueOf(MENORIGUAL)) || result.matches(String.valueOf(IGUAL))) {
                            unforbonito(result,socket,p);

                        }
                }
            }
    }
    
    public void verificarfloat(String received,Socket socket,PrintWriter p) throws IOException{
      //VARIABLES FLOTANTES CON NOMBRE
            if(received.toLowerCase().matches(String.valueOf(CREAR))&&received.matches(String.valueOf(VARIABLE))|| received.toLowerCase().matches(String.valueOf(CREA))||received.matches(String.valueOf(DECLARA))){
                if(received.matches(String.valueOf(FLOTANTES))){
                    if(!received.matches(String.valueOf(IGUAL)))
                        declararflotante(received,socket,p);
                }
            }
    }
    

    public void crearmain(String result, Socket clientSocket,PrintWriter p){
        String paso= "int main(argc, argv){";
        code.add("#include <stdio.h>");
        code.add(paso);
        code.add("printf(\"Hello world\");");
    }
    
    public void declararentero(String result, Socket clientSocket,PrintWriter p) throws IOException{
        String[] parts = result.split(" ");
        String nombre_de_variable = null;
        String sentencia=null;
        int last_index=1;
        String paso=null;
        for(int i=0;i<parts.length;i++){
            if(parts[i].matches(String.valueOf(LLAME))||parts[i].matches(String.valueOf(LLAMADA))){
                nombre_de_variable=parts[i+1];
            }
        }
        
        if(code.size()==1) {
            if(nombre_de_variable!=null)
                code.add("int " + nombre_de_variable + ";\n");
            paso="int "+ nombre_de_variable + ";\n";
          //  System.out.println(paso);
           // last_index = code.size();
          //  new DataOutputStream(clientSocket.getOutputStream()).writeUTF(paso);
        }else{
            for(int i=1;i<code.size();i++){
                sentencia = code.get(i);
                System.out.println(sentencia.contains("for"));
                if(!sentencia.contains("for")){
                    if(!sentencia.contains("if"))
                    if(!sentencia.contains("while"))
                    if(code.get(i).contains("}")){
                        break;
                    }
                    System.out.println("soy el incide"+code.get(i));
                    last_index=i; 
                  
                }
            }
            if(nombre_de_variable!=null)
                System.out.println(last_index);
                code.add(last_index,"int " + nombre_de_variable + ";\n");
            //    p.println(code.get(last_index));
                paso="int " + nombre_de_variable + ";\n";
            //    new DataOutputStream(clientSocket.getOutputStream()).writeUTF(paso);
        }

    }
    
    public void declararenteroconvalor(String result, Socket clientSocket,PrintWriter p) throws IOException{
        String[] parts = result.split(" ");
        String temp = null;
        String nombre_de_variable = null;
        ArrayList<String> valor=new ArrayList<>();
        ArrayList<String> signos = new ArrayList<>();
        int last_index=1;
        for(int i=0;i<parts.length;i++){
            if(parts[i].matches(String.valueOf(LLAME))||parts[i].matches(String.valueOf(LLAMADA))){
                nombre_de_variable=parts[i+1];
            }
            if(parts[i].matches(String.valueOf(DIGITOS))){
                valor.add(parts[i]);
            }
            if(parts[i].matches(String.valueOf(MAS))){
                signos.add("+");
            }
            if(parts[i].matches(String.valueOf(MENOS))){
                signos.add("-");
            }
            if(parts[i].matches(String.valueOf(ENTRE))){
                signos.add("/");
            }
            if(parts[i].matches(String.valueOf(POR))){
                signos.add("*");
            }
        }
        int c=0;
        if(code.size()==1) {
            if(nombre_de_variable!=null)
                temp="int " + nombre_de_variable +"=";
            for(int i=0;i<valor.size();i++){
                temp+=valor.get(i);
                for(int j=c;j<valor.size()-1;j++) {
                    if(c>valor.size()-1){
                        break;
                    }
                    temp += signos.get(c);
                    break;
                }
                c++;
            }
            temp+=";\n";
            code.add(1,temp);
            last_index = code.size();
          //  new DataOutputStream(clientSocket.getOutputStream()).writeUTF(temp);
        }else{
            for(int i=1;i<code.size();i++){
                if(!code.get(i).contains("for")||!code.get(i).contains("if")||!code.get(i).contains("while")||!code.get(i).contains("public")){
                    if(code.get(i).contains("}")){
                        break;
                    }
                    last_index=i;
                }
            }
            if(nombre_de_variable!=null)
                temp="int " + nombre_de_variable +"=";
            for(int i=0;i<valor.size();i++){
                temp+=valor.get(i);
                for(int j=c;j<valor.size()-1;j++) {
                    if(c>valor.size()-1){
                        break;
                    }
                    temp += signos.get(c);
                    break;
                }
                c++;
            }
            temp+=";\n";
            code.add(last_index,temp);
           // new DataOutputStream(clientSocket.getOutputStream()).writeUTF(temp);
        }

    }
    
    public void declararflotante(String result, Socket clientSocket,PrintWriter p) throws IOException{
        String[] parts = result.split(" ");
        String nombre_de_variable = null;
        int last_index=1;
        String paso=null;
        for(int i=0;i<parts.length;i++){
            if(parts[i].matches(String.valueOf(LLAME))||parts[i].matches(String.valueOf(LLAMADA))){
                nombre_de_variable=parts[i+1];
            }
        }
        
        if(code.size()==1) {
            if(nombre_de_variable!=null)
                code.add(1,"float " + nombre_de_variable + ";\n");
            paso="float "+ nombre_de_variable + ";\n";
          //  System.out.println(paso);
            last_index = code.size();
          //  new DataOutputStream(clientSocket.getOutputStream()).writeUTF(paso);
        }else{
            for(int i=1;i<code.size();i++){
                if(!code.get(i).contains("for")||!code.get(i).contains("if")||!code.get(i).contains("while")||!code.get(i).contains("public")){
                    if(code.get(i).contains("}")){
                        break;
                    }
                    last_index=i;
                }
            }
            if(nombre_de_variable!=null)
                code.add(last_index,"float " + nombre_de_variable + ";\n");
            //    p.println(code.get(last_index));
                paso="float " + nombre_de_variable + ";\n";
             //   new DataOutputStream(clientSocket.getOutputStream()).writeUTF(paso);
        }

    }
    
     public void unforbonito(String result,Socket clientSocket,PrintWriter p) throws IOException{
        String[] parts = result.split(" ");
        String paso=null;
        int index=0;
        ArrayList<String> numbers = new ArrayList<>();
        for (int i = 0; i < parts.length; i++) {
            if (parts[i].matches(".*\\d+.*")) {
                numbers.add(parts[i]);
            }else if(parts[i].contains("uno")){
                numbers.add("1");
            }
        }
        if(numbers.size()>1) {
            if (result.matches(String.valueOf(MAYORIGUAL))) {
                paso="for (int i" + "=" + numbers.get(0) + ";i>=" + numbers.get(1) + ";i++){\n";
                code.add(paso);
               index=code.indexOf(paso);
            //   p.println(code.get(index));
                System.out.println(code.get(index));
             //    new DataOutputStream(clientSocket.getOutputStream()).writeUTF(paso);
            } else if (result.matches(String.valueOf(MAYOR))) {
                String temp = "for (int i" + "=" + numbers.get(0) + ";i>" + numbers.get(1) + ";i++){\n";
                code.add(code.size(), temp);
               //  new DataOutputStream(clientSocket.getOutputStream()).writeUTF(temp);
            } else if (result.matches(String.valueOf(MENORIGUAL))) {
                paso="for (int i" + "=" + numbers.get(0) + ";i<=" + numbers.get(1) + ";i++){\n";
                code.add(paso);
              //   new DataOutputStream(clientSocket.getOutputStream()).writeUTF(paso);
            } else if (result.matches(String.valueOf(MENOR))) {
                String temp = "for (int i" + "=" + numbers.get(0) + ";i<" + numbers.get(1) + ";i++){\n";
                code.add(temp);
               // new DataOutputStream(clientSocket.getOutputStream()).writeUTF(temp);
            } else if (result.matches(String.valueOf(IGUAL))) {
                code.add("for (int i" + "=" + numbers.get(0) + ";i==" + numbers.get(1) + ";i++){\n");
            }
        }
    }
     
     public void imprimir(PrintWriter p,String result,File f,Socket clientSocket) throws IOException{
         String paso=null;
         if(result.matches(String.valueOf(COMPILAR))){
         for (int i = 0; i < code.size(); i++) {
             System.out.print(code.get(i));
             paso=code.get(i);
             new DataOutputStream(clientSocket.getOutputStream()).writeUTF(paso);
            p.print(code.get(i));
         }
         }
        
     }

    }



