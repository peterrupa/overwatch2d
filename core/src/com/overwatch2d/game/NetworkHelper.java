package com.overwatch2d.game;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.io.*;
import java.util.Base64;

public class NetworkHelper implements Constants {
    public static void connect(String host, String name) {
        try {
            byte message[];

            InetAddress address = InetAddress.getByName(host);

            message = toString(new ConnectPacket(name)).getBytes();

            DatagramPacket packet = new DatagramPacket(message, message.length, address, PORT);
            DatagramSocket socket = new DatagramSocket();

            socket.send(packet);

            System.out.println("CONNECT request sent to " + host);

            socket.close();
        }
        catch(Exception e) {
            System.out.println("Client error:");
            System.out.println(e);
        }
    }

    public static Object fromString(String s) throws IOException,
            ClassNotFoundException {
        byte [] data = Base64.getMimeDecoder().decode(s);
        ObjectInputStream ois = new ObjectInputStream(
                new ByteArrayInputStream(data));
        Object o  = ois.readObject();
        ois.close();
        return o;
    }

    /** Write the object to a Base64 string. */
    public static String toString(Serializable o) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(o);
        oos.close();
        return Base64.getEncoder().encodeToString(baos.toByteArray());
    }
}
