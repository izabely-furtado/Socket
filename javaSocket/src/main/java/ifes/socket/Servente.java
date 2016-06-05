/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ifes.socket;

import java.io.InputStream;
import java.util.Scanner;

/**
 *
 * @author IzabelyFurtado
 */
public class Servente implements Runnable {

    private InputStream servidor;

    public Servente(InputStream servidor) {
        this.servidor = servidor;
    }

    public void run() {
        // recebe msgs do servidor e imprime na tela
        Scanner s = new Scanner(this.servidor);
        while (s.hasNextLine()) {
            System.out.println(s.nextLine());
        }
    }
}

