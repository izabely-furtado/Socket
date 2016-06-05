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
public class Client{ //implements Runnable {
    Socket cliente;

    private String host;
    private int porta;
    

    public Client(String host, int portaServidor) throws IOException {
        this.host = host;
        this.porta = portaServidor;
        this.cliente = new Socket(this.host, this.porta);
        System.out.println("O cliente " + this.host + 
                           " se conectou ao servidor " + this.porta + "!");
    }

    //lendo do teclado e enviado para o servidor
    public void leituraEscrita() throws IOException {
        Scanner teclado = new Scanner(System.in);
        PrintStream saida = new PrintStream(this.cliente.getOutputStream());

        while (teclado.hasNextLine()) {
            String sai = teclado.nextLine();
            saida.println(sai);
            if ("bye".equals(sai)) {
                break;
            }
        }

        //impossibilitando novas envios ao servidor
        saida.close();
        //impossibilitando novas leituras do teclado
        teclado.close();

    }

    public static void vai(String host, int portaServidor) throws UnknownHostException, IOException {
        Client cli = new Client("127.0.0.1", 12345);

        // thread para receber mensagens do servidor
        Servente r = new Servente(cli.cliente.getInputStream());
        new Thread(r).start();

        // lê msgs do teclado e manda pro servidor
        cli.leituraEscrita();
        
        //desligando o cliente
        cli.cliente.close();
    }
}