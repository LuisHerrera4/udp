package tasca1;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Scanner;

public class ServidorAdivinaUDP {
    DatagramSocket socket;
    SecretNum secretNum;
    public void init(int port) throws SocketException {
        socket = new DatagramSocket(port);
        secretNum = new SecretNum();
        secretNum.pensa(100);
    }

    public void runServer() throws IOException {
        byte [] receivingData = new byte[1024];
        byte [] sendingData;
        InetAddress clientIP;
        int clientPort;

        while(true){
            DatagramPacket packet = new DatagramPacket(receivingData, 1024);
            socket.receive(packet);

            sendingData = processData(packet.getData(), packet.getLength());
            clientIP = packet.getAddress();

            clientPort = packet.getPort();
            packet = new DatagramPacket(sendingData, sendingData.length,
                    clientIP, clientPort);
            socket.send(packet);
        }
    }

    private byte[] processData(byte[] data, int length) {
        int n = ByteBuffer.wrap(data,0,length).getInt();
        byte[] missatge = ByteBuffer.allocate(4).putInt(n).array();
        System.out.println(missatge);
        return missatge;
    }

    public static void main(String[] args) {
        try{
            ServidorAdivinaUDP server = new ServidorAdivinaUDP();
            server.init(5556);
            server.runServer();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}