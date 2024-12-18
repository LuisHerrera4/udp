package udpMulticast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.util.HashMap;
import java.util.Map;

public class ClientPalabras1 {
    private boolean continueRunning = true;
    private MulticastSocket socket;
    private InetAddress multicastIP;
    private int port;
    private Map<String, Integer> contadorParaules;
    NetworkInterface netIf;
    InetSocketAddress group;

    public ClientPalabras1(int portValue, String strIp) throws IOException {
        multicastIP = InetAddress.getByName(strIp);
        port = portValue;
        socket = new MulticastSocket(port);
        contadorParaules = new HashMap<>();
        netIf = socket.getNetworkInterface();
        group = new InetSocketAddress(strIp, portValue);
    }

    public void runClient() throws IOException {
        DatagramPacket packet;
        byte[] receivedData = new byte[1024];

        socket.joinGroup(group, netIf);
        System.out.println("Conectado a " + group.getAddress() + ":" + group.getPort());

        while (continueRunning) {
            packet = new DatagramPacket(receivedData, receivedData.length);
            socket.setSoTimeout(5000);
            try {
                socket.receive(packet);
                continueRunning = getData(packet.getData(), packet.getLength());
            } catch (IOException e) {
                System.out.println("Se ha perdido la conexión con el servidor.");
                continueRunning = false;
            }
        }

        socket.leaveGroup(group, netIf);
        socket.close();
    }

    protected boolean getData(byte[] data, int length) {
        String palabraRecibida = new String(data, 0, length);

        if (contadorParaules.containsKey(palabraRecibida)) {
            contadorParaules.put(palabraRecibida, contadorParaules.get(palabraRecibida) + 1);
        } else {
            contadorParaules.put(palabraRecibida, 1);
        }
        System.out.println(contadorParaules.toString());

        return true;
    }

    public static void main(String[] args) throws IOException {
        ClientPalabras1 client1 = new ClientPalabras1(5557, "224.0.0.113");
        client1.runClient();
        System.out.println("Cliente parado!");
    }
}
