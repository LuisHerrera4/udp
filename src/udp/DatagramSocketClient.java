package udp;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class DatagramSocketClient {
    InetAddress serverIP;
    int serverPort;
    DatagramSocket socket;

    public void init(String host, int port) throws SocketException,
            UnknownHostException {
        serverIP = InetAddress.getByName(host);
        serverPort = port;
        socket = new DatagramSocket();
    }

    public void runClient() throws IOException {
        byte [] receivedData = new byte[1024];
        byte [] sendingData;

//a l'inici
        sendingData = getFirstRequest();
//el servidor atén el port indefinidament
        while(mustContinue(sendingData)){
            DatagramPacket packet = new DatagramPacket(sendingData,
                    sendingData.length,
                    serverIP,
                    serverPort);
//enviament de la resposta
            socket.send(packet);

//creació del paquet per rebre les dades
            packet = new DatagramPacket(receivedData, 1024);
//espera de les dades
            socket.receive(packet);
//processament de les dades rebudes i obtenció de la resposta
            sendingData = getDataToRequest(packet.getData(), packet.getLength());
        }
    }

    private byte[] getDataToRequest(byte[] data, int length) {
        Scanner sc = new Scanner(System.in);
        String respuesta= new String(data, 0, length, StandardCharsets.UTF_8).trim();
        System.out.println(respuesta);
        respuesta = sc.nextLine();
        return respuesta.getBytes();
    }

    private byte[] getFirstRequest() {
        Scanner sc = new Scanner(System.in);
        String nombre= sc.nextLine();
        return nombre.getBytes(StandardCharsets.UTF_8);
    }

    private boolean mustContinue(byte[] sendingData) {
        return sendingData.length > 0;
    }

    public static void main(String[] args) {
        try {
            DatagramSocketClient client = new DatagramSocketClient();
            DatagramSocketClient client1 = new DatagramSocketClient();
            client.init("localhost", 5556);
            client1.init("localhost", 5556);

            client.runClient();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}