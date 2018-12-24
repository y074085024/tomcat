package com.zerone.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

/**
 * @author yxl
 * @since 2018/12/21
 */
public class MyRequest {
    private static final Charset charset = Charset.forName("utf-8");
    private String url;
    private String method;
    public MyRequest(SocketChannel channel) throws IOException {
        StringBuilder httpRequest = new StringBuilder();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        while (true){
            int count = channel.read(buffer);
            if(count<=0){
                break;
            }
            buffer.flip();
            httpRequest.append(String.valueOf(charset.decode(buffer).array()));
            buffer.clear();
        }
        //System.out.println(httpRequest);
        String httpHead = httpRequest.toString().split("\n")[0];
        url = httpHead.split("\\s")[1];
        method = httpHead.split("\\s")[0];
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
