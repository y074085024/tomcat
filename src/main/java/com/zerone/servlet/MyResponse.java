package com.zerone.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @author yxl
 * @since 2018/12/21
 */
public class MyResponse {
    private SocketChannel channel;

    public MyResponse(SocketChannel channel) {
        this.channel = channel;
    }
    public void write(String content) throws IOException {
        StringBuffer httpResponse = new StringBuffer();
        httpResponse.append("HTTP/1.1 200 OK\n")
                .append("Content-Type: text/html\n")
                .append("\r\n")
                .append("<html><body>")
                .append(content)
                .append("</body></html>");
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.put(httpResponse.toString().getBytes());
        buffer.flip();
        channel.write(buffer);
        channel.close();
    }
}
