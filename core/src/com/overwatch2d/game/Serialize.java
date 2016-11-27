package com.overwatch2d.game;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.io.*;
import java.util.Base64;

public class Serialize {
    public static byte[] toBytes(Object object) throws IOException {
//        Kryo kryo = new Kryo();
//
//        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
//             Output out = new Output(bos)) {
//            kryo.writeObject(out, object);
//            out.flush();
//            return bos.toByteArray();
//        }

        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutput out = new ObjectOutputStream(bos)) {
            out.writeObject(object);
            return bos.toByteArray();
        }
    }

    public static Object toObject(byte[] bytes) throws IOException, ClassNotFoundException {
        Kryo kryo = new Kryo();

//        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
//             Input in = new Input(bis)) {
//            return kryo.readObject(in, Packet.class);
//        }

        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
             ObjectInput in = new ObjectInputStream(bis)) {
            return in.readObject();
        }
    }
}
