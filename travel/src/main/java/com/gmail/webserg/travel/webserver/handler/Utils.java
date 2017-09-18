package com.gmail.webserg.travel.webserver.handler;

import java.nio.ByteBuffer;
import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Optional;

public final class Utils {
    public static String dataPath = System.getenv("DATA_HOME");
    public static String optionsPath = "/tmp/data";

    public static Optional<String> toString(ArrayDeque<String> arrayDeque) {
        if (arrayDeque == null) return Optional.empty();
        Iterator<String> it = arrayDeque.iterator();
        if (!it.hasNext())
            return Optional.of("");
        StringBuilder sb = new StringBuilder();
        for (; ; ) {
            String e = it.next();
            sb.append(e);
            if (!it.hasNext())
                return Optional.of(sb.toString());
        }
    }

    public static ByteBuffer getMessage(String message) {
        ByteBuffer buffer;
        try {
            byte[] m = message.getBytes("UTF-8");
            buffer = ByteBuffer.allocateDirect(m.length);
            buffer.put(m);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        buffer.flip();
        return buffer;
    }
}
