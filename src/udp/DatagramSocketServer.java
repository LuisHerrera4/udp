package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;

public class DatagramSocketServer {
    DatagramSocket socket;

    public void init(int port) throws SocketException {
        socket = new DatagramSocket(port);
    }

    public void runServer() throws IOException {
        byte [] receivingData = new byte[1024];
        byte [] sendingData;
        InetAddress clientIP;
        int clientPort;

//el servidor atén el port indefinidament
        while(true){
//creació del paquet per rebre les dades
            DatagramPacket packet = new DatagramPacket(receivingData, 1024);
//espera de les dades
            socket.receive(packet);
//processament de les dades rebudes i obtenció de la resposta
            sendingData = processData(packet.getData(), packet.getLength());
//obtenció de l'adreça del client
            clientIP = packet.getAddress();
//obtenció del port del client
            clientPort = packet.getPort();
//creació del paquet per enviar la resposta
            packet = new DatagramPacket(sendingData, sendingData.length,
                    clientIP, clientPort);
//enviament de la resposta
            socket.send(packet);
        }
    }

    private byte[] processData(byte[] data, int length) {
        String recibirMensaje = new String(data, 0, length, StandardCharsets.UTF_8).trim();
        System.out.println(recibirMensaje);
        return recibirMensaje.toUpperCase().getBytes();
    }

    public static void main(String[] args) {
        try{
            DatagramSocketServer server = new DatagramSocketServer();
            server.init(5555);
            server.runServer();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}