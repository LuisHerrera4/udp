package tasca1;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class ClientAdivinaUDP {
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
            DatagramPacket packet = new DatagramPacket(sendingData, sendingData.length, serverIP, serverPort);
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
        int n = ByteBuffer.wrap(data,0,length).getInt();

        switch (n){
            case 0:
                System.out.println("Has encertat el numero");
                return new byte[0];
                case 1:
                    System.out.println("El numero pensat es mes petit");
                    sc.nextInt();
                    break;
            case 2:
                System.out.println("El numero pensat es mes gran");
                sc.nextInt();
                break;
            case -1:
                System.out.println("Adeu");
                System.exit(-1);
        }
        System.out.println(n);
        return  ByteBuffer.allocate(4).putInt(n).array();
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
            ClientAdivinaUDP client = new ClientAdivinaUDP();
            ClientAdivinaUDP client1 = new ClientAdivinaUDP();
            client.init("localhost", 5556);
            client1.init("localhost", 5556);
            client.runClient();
            client1.runClient();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}