package util;

import java.io.*;

public interface Serializing {
    static byte[] serialize(Object object) throws IOException {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             ObjectOutputStream outputStream = new ObjectOutputStream(byteArrayOutputStream)) {
            outputStream.writeObject(object);
            outputStream.flush();
            return byteArrayOutputStream.toByteArray();
        }
    }

    static Object deserialize(byte[] obj) throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(obj);
             ObjectInputStream inputStream = new ObjectInputStream(byteArrayInputStream)) {
            return inputStream.readObject();
        }
    }
}
