package tcpEjercicios;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TpcSever {
    static final int PORT = 9090;

    public void listen() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Servidor esperant connexions...");
            while (true) {
                try (Socket clientSocket = serverSocket.accept()) {
                    System.out.println("Connexió establerta amb: " + clientSocket.getInetAddress());
                    processClientRequest(clientSocket);
                } catch (IOException e) {
                    Logger.getLogger(TpcSever.class.getName()).log(Level.SEVERE, "Error amb el client", e);
                }
            }
        } catch (IOException e) {
            Logger.getLogger(TpcSever.class.getName()).log(Level.SEVERE, "Error en el servidor", e);
        }
    }

    private void processClientRequest(Socket clientSocket) {
        try (
                ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
                ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream())
        ) {
            Llista llista = (Llista) in.readObject();
            System.out.println("Rebut del client: " + llista.getNom() + " - " + llista.getNumberList());

            //  ordenar i eliminar duplicats de la lista
            Set<Integer> numsOrdenados = new TreeSet<>(llista.getNumberList());
            llista.setNumberList(new ArrayList<>(numsOrdenados));

            System.out.println("Enviant al client: " + llista.getNumberList());
            out.writeObject(llista);
        } catch (IOException | ClassNotFoundException e) {
            Logger.getLogger(TpcSever.class.getName()).log(Level.SEVERE, "Error processant el client", e);
        }
    }

    public static void main(String[] args) {
        new TpcSever().listen();
    }
}
