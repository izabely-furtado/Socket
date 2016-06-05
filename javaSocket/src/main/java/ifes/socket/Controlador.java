/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ifes.socket;

import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 *
 * @author IzabelyFurtado
 */
public class Controlador implements Runnable {

    private InputStream cliente;
    private Server servidor;

    public Controlador(InputStream cliente, Server servidor) {
        this.cliente = cliente;
        this.servidor = servidor;
    }

    public void run() {
        // quando chegar uma msg, distribui pra todos
        Scanner entrada = new Scanner(this.cliente);
        while (entrada.hasNextLine()) {
            servidor.escreve(entrada.nextLine());
            String entra = entrada.nextLine();
            if ("bye".equals(entra)) {
                break;
            }
        }
        entrada.close();
    }

    public static void main(String[] args)
            throws UnknownHostException, IOException {
        
        // dispara cliente
        Client.vai("127.0.0.1", 01234);
    }

}
