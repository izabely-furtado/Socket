/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ifes.socket;

import java.io.IOException;
import java.io.PrintStream;
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

    
    List<Socket> clients;
    private int porta;
    private List<PrintStream> clientes;
    ServerSocket servidor;

    public Server(int porta) throws IOException {
        this.porta = porta;
        this.clientes = new ArrayList<PrintStream>();
        this.servidor = new ServerSocket(this.porta);
        System.out.println("Porta " + this.porta + " aberta!");

    }

    public static void vai(int portaServidor) throws IOException {
        Server servidor = new Server(portaServidor);
        
        while (true) {
            // aceita um cliente
            Socket cliente = servidor.servidor.accept();
            System.out.println("Nova conexão com o cliente "
                    + cliente.getInetAddress().getHostAddress());

            // adiciona saida do cliente à lista
            PrintStream ps = new PrintStream(cliente.getOutputStream());
            servidor.clientes.add(ps);

            // cria tratador de cliente numa nova thread
            Controlador tc = new Controlador(cliente.getInputStream(), servidor);
            new Thread(tc).start();
        }

    }
    
    public void escreve(String msg) {
        // envia msg para todo mundo
        for (PrintStream cliente : this.clientes) {
            cliente.println(msg + " foi recebido");
        }
    }


    public static void main(String[] args) throws IOException {
        // inicia o servidor
        Server.vai(12345);
    }
}
