/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ifes.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author IzabelyFurtado
 */
public class Server {
    ServerSocket servidor;
    List<Socket> clientes;
    
    //com um unico cliente
    Server(int ipServidor) throws IOException{
        
        //conectando servidor
        this.servidor = new ServerSocket(ipServidor);
        System.out.println("Porta " + ipServidor + " aberta!");
        
        //conectando clientes
        this.clientes = new ArrayList<>();
        Socket cliente = servidor.accept();
        this.clientes.add(cliente);
        System.out.println("Nova conexão com o cliente "
                + cliente.getInetAddress().getHostAddress());
        
    }

    //com vários clientes
    Server(int ipServidor, List<Socket> clientes) throws IOException{
        //conectando servidor
        this.servidor = new ServerSocket(ipServidor);
        System.out.println("Porta " + ipServidor + " aberta!");
        
        //conectando clientes
        this.clientes = clientes;
        for(Socket cliente: this.clientes){
            cliente = servidor.accept();
            System.out.println("Nova conexão com o cliente "
                + cliente.getInetAddress().getHostAddress());
        }
        
    }
    
    //lendo entrada de clientes até bye
    public void lendo() throws IOException{
        for(Socket cliente: this.clientes){
            Scanner entrada = new Scanner(cliente.getInputStream());
            
            //lendo
            while (entrada.hasNextLine()) {
                String entra = entrada.nextLine();
                System.out.println(entra);
                if("bye".equals(entra)){
                    break;
                }
            }

            //impossibilitando novas entradas
            entrada.close();
            //desligando cliente
            cliente.close();
        }
        
    }
    
    public static void main(String[] args) throws IOException {
        Server servidor = new Server(12345);
        
        //lendo clientes
        servidor.lendo();
        
        //desligando comunicação com o servidor
        servidor.servidor.close();
    }
}
