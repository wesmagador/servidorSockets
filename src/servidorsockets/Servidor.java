package servidorsockets;

import java.awt.HeadlessException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class Servidor extends Thread {
    
private static ArrayList<BufferedWriter>usuarios;
private static ServerSocket servidor;

/**
  * Método USUARIOS onde cria a lista de conexões
  * @param ArrayList<BufferedWriter>() do tipo Socket
  */
private static void USUARIO() {   
    
usuarios = new ArrayList<BufferedWriter>(); 
}
    
private String nome;
private Socket con;
private InputStream in;
private InputStreamReader inr;
private BufferedReader bfr;

/**
  * Método construtor
  * @param con do tipo Socket
  */
public Servidor(Socket con){
   this.con = con;
   try {
         in = con.getInputStream();
         inr = new InputStreamReader(in);
          bfr = new BufferedReader(inr);
   } catch (IOException e) {
   }
}

/**
  * Método run
  */
public void run(){

  try{

    String msg;
    OutputStream ou =  this.con.getOutputStream();
    Writer ouw = new OutputStreamWriter(ou);
    BufferedWriter bfw = new BufferedWriter(ouw);
    usuarios.add(bfw);
    nome = msg = bfr.readLine();
    
    while(!"Sair".equalsIgnoreCase(msg) && msg != null)
      {
       msg = bfr.readLine();
       online(bfw, msg);
       System.out.println(msg);
       }

   }catch (IOException e) {
   }
}

/***
 * Método usado para enviar mensagem para todos os clients
 * @param bwSaida do tipo BufferedWriter
 * @param msg do tipo String
 * @throws IOException
 */
public void online(BufferedWriter bwSaida, String msg) throws IOException{
    
  BufferedWriter bwS;
  for(BufferedWriter bw : usuarios){
   bwS = (BufferedWriter)bw;
   if(!(bwSaida == bwS)){
     bw.write(nome + " -> " + msg+"\r\n");
     bw.flush();
   }
  }
}


/***
   * Método main
   * @param args
   */
public static void main(String []args) {

  try{
    //Cria os objetos necessário para instânciar o servidor
    JLabel lblMessage = new JLabel("Porta do Servidor:");
    JTextField txtPorta = new JTextField("12345");
    Object[] texts = {lblMessage, txtPorta };
    JOptionPane.showMessageDialog(null, texts);
    servidor = new ServerSocket(Integer.parseInt(txtPorta.getText()));
    USUARIO();
    JOptionPane.showMessageDialog(null,"Servidor ativo na porta: "+
    txtPorta.getText());
    
     while(true){
       System.out.println("Aguardando conexão...");
       Socket con = servidor.accept();
       System.out.println("Cliente conectado...");
       Thread t = new Servidor(con);
        t.start();
    }

  }catch (HeadlessException | IOException | NumberFormatException e) {
  }
 }
}