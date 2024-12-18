package tcpEjercicios;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TcpSocketClient extends Thread {
    private String address;
    private int port;
    private String nom;

    // Constructor
    public TcpSocketClient(String address, int port, String nom) {
        this.address = address;
        this.port = port;
        this.nom = nom;
    }

    @Override
    public void run() {
        try (Socket socket = new Socket(address, port)) {
            System.out.println("[" + nom + "] Connectat al servidor: " + address + ":" + port);

            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            // Entrada manual dels números
            List<Integer> numbers = getNumbersFromUser();
            Llista llista = new Llista(nom, numbers);

            System.out.println("[" + nom + "] Enviant al servidor: " + llista.getNom() + " - " + llista.getNumberList());
            out.writeObject(llista);

            // Rebre l'objecte processat
            Llista processedList = (Llista) in.readObject();
            System.out.println("[" + nom + "] Rebut del servidor: " + processedList.getNom() + " - " + processedList.getNumberList());
        } catch (ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Mètode per obtenir els números des de la consola
    private List<Integer> getNumbersFromUser() {
        Scanner sc = new Scanner(System.in);
        List<Integer> numbers = new ArrayList<>();

        System.out.println("[" + nom + "] Introdueix els nums separats per espais:");
        String input = sc.nextLine();
        String[] inputNumbers = input.split(" ");

        for (String num : inputNumbers) {
            try {
                numbers.add(Integer.parseInt(num.trim()));
            } catch (NumberFormatException e) {
                System.err.println("[" + nom + "] Valor no vàlid ignorat: " + num);
            }
        }

        return numbers;
    }

    public static void main(String[] args) {

        TcpSocketClient client1 = new TcpSocketClient("localhost", 9090, "Client1");
       

        try{
            client1.start();
            Thread.sleep((long) Math.random()*1000);

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


    }
}
