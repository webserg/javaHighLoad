package com.gmail.webserg.travel.webserver.handler;

import java.nio.ByteBuffer;
import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Optional;

public final class Utils {
    public static final String DATA_PATH = System.getenv("DATA_HOME");
    public static final String OPTIONS_PATH = "/tmp/data";
    public static final String CONTENT_TYPE = "application/json";
    public static final String CHARSET = "UTF-8";
    public static final ByteBuffer POST_ANSWER = ByteBuffer.wrap("{}".getBytes()).asReadOnlyBuffer();

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

    public static ByteBuffer getMessage(final String message) {
        final ByteBuffer buffer;
        try {
            byte[] m = message.getBytes(CHARSET);
            buffer = ByteBuffer.allocateDirect(m.length);
            buffer.put(m);
            buffer.flip();
            return buffer;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
