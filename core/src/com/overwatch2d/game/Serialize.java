package com.overwatch2d.game;

import java.io.*;
import java.util.Base64;

public class Serialize {
//    public static Object fromString(String s) throws IOException,
//            ClassNotFoundException {
//        byte [] data = Base64.getMimeDecoder().decode(s);
//        ObjectInputStream ois = new ObjectInputStream(
//                new ByteArrayInputStream(data));
//        Object o  = ois.readObject();
//        ois.close();
//        return o;
//    }

    public static byte[] toBytes(Object object) throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutput out = new ObjectOutputStream(bos)) {
            out.writeObject(object);
            return bos.toByteArray();
        }
    }

    public static Object toObject(byte[] bytes) throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
             ObjectInput in = new ObjectInputStream(bis)) {
            return in.readObject();
        }
    }

//    /** Write the object to a Base64 string. */
//    public static String toString(Serializable o) throws IOException {
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        ObjectOutputStream oos = new ObjectOutputStream(baos);
//        oos.writeObject(o);
//        oos.close();
//        return Base64.getEncoder().encodeToString(baos.toByteArray());
//    }
}
