package ru.ie.phase.utils;

import ru.ie.phase.foundation.net.NetElement;

import java.io.*;

public class Utils {

    public static void checkElement(Object o){
        if(!o.getClass().isAnnotationPresent(NetElement.class))
            throw new IllegalArgumentException("side interface class is not annotate as NetElement");
    }

    public static byte[] writeToByteArray(Object... objects) throws IOException {

        byte[] res;

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream stream = new ObjectOutputStream(byteStream);

        stream.writeInt(objects.length);

        for(Object o : objects)
            stream.writeObject(o);

        res = byteStream.toByteArray();

        stream.close();
        byteStream.close();

        return res;
    }

    public static Object[] readByteArray(byte[] bytearray) throws IOException, ClassNotFoundException {

        Object[] res;

        ByteArrayInputStream byteStream = new ByteArrayInputStream(bytearray);
        ObjectInputStream stream = new ObjectInputStream(byteStream);

        int len = stream.readInt();
        res = new Object[len];

        for(int i = 0; i < len; i++)
            res[i] = stream.readObject();

        stream.close();
        byteStream.close();

        return res;
    }

}
