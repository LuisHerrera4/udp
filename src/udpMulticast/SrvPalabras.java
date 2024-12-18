package udpMulticast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Arrays;
import java.util.List;

public class SrvPalabras {
    MulticastSocket socket;
    InetAddress multicastIP;
    int port;
    boolean continueRunning = true;
    List<String> paraules = Arrays.asList("hola", "adeu", "casa", "cotxe", "arbre", "gat", "gos", "llibre","sigma","cremallera");

    public SrvPalabras(int portValue, String strIp) throws IOException {
        socket = new MulticastSocket(portValue);
        multicastIP = InetAddress.getByName(strIp);
        port = portValue;
    }

    public void runServer() throws IOException {
        DatagramPacket packet;
        byte[] sendingData;

        while (continueRunning) {
            // paraula aleatòria
            String paraula = paraules.get((int) (Math.random() * paraules.size()));
            sendingData = paraula.getBytes();

            // Envia el paquet
            packet = new DatagramPacket(sendingData, sendingData.length, multicastIP, port);
            socket.send(packet);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        socket.close();
    }

    public static void main(String[] args) throws IOException {
        SrvPalabras server = new SrvPalabras(5557, "224.0.0.113");
        server.runServer();
        System.out.println("Servidor parat!");
    }
}
