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

    public static void main(String[] args) throws IOException {
        // inicia o servidor
        new Server(12345).executa();
    }
    
    List<Socket> clients;
    private int porta;
    private List<PrintStream> clientes;

    public Server(int porta) {
        this.porta = porta;
        this.clientes = new ArrayList<PrintStream>();
    }

    public void executa() throws IOException {
        ServerSocket servidor = new ServerSocket(this.porta);
        System.out.println("Porta " + this.porta + " aberta!");

        while (true) {
            // aceita um cliente
            Socket cliente = servidor.accept();
            System.out.println("Nova conexão com o cliente "
                    + cliente.getInetAddress().getHostAddress()
            );

            // adiciona saida do cliente à lista
            PrintStream ps = new PrintStream(cliente.getOutputStream());
            this.clientes.add(ps);

            // cria tratador de cliente numa nova thread
            Controlador tc = new Controlador(cliente.getInputStream(), this);
            new Thread(tc).start();
        }

    }

    //lendo entrada de clientes até bye
    public void lendo2(String msg) throws IOException {
        for (Socket cliente : this.clients) {
            Scanner entrada = new Scanner(cliente.getInputStream());

            //lendo
            while (entrada.hasNextLine()) {
                String entra = entrada.nextLine();
                System.out.println(entra);
                if ("bye".equals(entra)) {
                    break;
                }
            }

        }

    }

    
    public void escreve(String msg) {
        // envia msg para todo mundo
        for (PrintStream cliente : this.clientes) {
            cliente.println(msg + " foi recebido");
        }
    }
}
    /*
    ServerSocket servidor;
    List<Socket> clientes;
    List<PrintStream> msgs;

    //com um unico cliente
    Server(int ipServidor) throws IOException {
        //conectando servidor
        ServerSocket servidor = new ServerSocket(ipServidor);
        //System.out.println("Porta " + ipServidor + " aberta!");
        
        //criando cliente
        List<Socket> c = new ArrayList<>();
        Socket cliente = servidor.accept();
        c.add(cliente);
        //criando servidor
        Server s = new Server(ipServidor, this.clientes);
        this.clientes = s.clientes;
        this.msgs = s.msgs;
        this.servidor = s.servidor;
        
    }

    //com vários clientes
    Server(int ipServidor, List<Socket> clientes) throws IOException {
        this.msgs = new ArrayList<>();
        //conectando servidor
        this.servidor = new ServerSocket(ipServidor);
        System.out.println("Porta " + ipServidor + " aberta!");

        //conectando clientes
        this.clientes = clientes;
        for (Socket cliente : this.clientes) {
            //aceitando um cliente
            cliente = servidor.accept();
            System.out.println("Nova conexão com o cliente "
                    + cliente.getInetAddress().getHostAddress());
            // adiciona saida do cliente à lista
            PrintStream ps = new PrintStream(cliente.getOutputStream());
            this.msgs.add(ps);

            // cria tratador de cliente numa nova thread
            Controlador control = new Controlador(cliente.getInputStream(), this);
            new Thread(control).start();
        }

    }

    //metodo que distribui mensagens aos respectivos clientes
    void escreve(String msg) {
        for (PrintStream cliente : this.msgs) {
            cliente.println(msg);
        }
    }

    //lendo entrada de clientes até bye
    public void lendo() throws IOException {
        for (Socket cliente : this.clientes) {
            Scanner entrada = new Scanner(cliente.getInputStream());

            //lendo
            while (entrada.hasNextLine()) {
                String entra = entrada.nextLine();
                System.out.println(entra);
                if ("bye".equals(entra)) {
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
        Server servidor = new Server(01234);

        //lendo clientes
        servidor.lendo();

        //desligando comunicação com o servidor
        servidor.servidor.close();
    }
}
*/
