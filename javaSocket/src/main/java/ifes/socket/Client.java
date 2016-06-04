/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ifes.socket;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author IzabelyFurtado
 */
public class Client implements Runnable{

    Socket cliente;
    
    Client(String ipCliente, int ipServidor) throws IOException{
        this.cliente = new Socket(ipCliente, ipServidor);
        System.out.println("O cliente " + ipCliente + 
                           " se conectou ao servidor " + ipServidor + " !");
        
    }
    
    //lendo do teclado e enviado para o servidor
    public void leituraEscrita() throws IOException{
        Scanner teclado = new Scanner(System.in);
        PrintStream saida = new PrintStream(this.cliente.getOutputStream());
        
        while (teclado.hasNextLine()) {
            String sai = teclado.nextLine();
            saida.println(sai);
            if("bye".equals(sai)){
                break;
            }
        }
        
        //impossibilitando novas envios ao servidor
        saida.close();
        //impossibilitando novas leituras do teclado
        teclado.close();
        
    }
        
    public static void main(String[] args)
            throws UnknownHostException, IOException {
        Client cliente = new Client("127.0.0.1", 12345);
        
        //lendo do teclado
        cliente.leituraEscrita();
        
        //desligando cliente
        cliente.cliente.close();
    }

    @Override
    public void run() {
        
        try {
            //lendo do teclado
            this.leituraEscrita();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            //desligando cliente
            this.cliente.close();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
