package com.zerone;

import com.zerone.servlet.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author yxl
 * @since 2018/12/21
 */
public class MyTomcat {
    private int port;
    private Map<String,String> urlServletMap = new HashMap<>();
    private static final MyServlet defaultServlet = new DefaultServlet();

    public MyTomcat(int port) {
        this.port = port;
    }

    public void start(){

        //初始化url与servlet类映射
        initServletMapping();

        ServerSocketChannel serverSocketChannel = null;
        try{
            Selector selector = Selector.open();
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.socket().bind(new InetSocketAddress(8080));

            serverSocketChannel.register(selector,SelectionKey.OP_ACCEPT);
            System.out.println("MyTocat is start...");
            while (true){
                selector.select();
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                for(SelectionKey key:selectionKeys){
                    if(key.isAcceptable()){
                        ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
                        SocketChannel clientChannel = serverChannel.accept();
                        clientChannel.configureBlocking(false);
                        clientChannel.register(selector,SelectionKey.OP_READ);
                    }
                    else if(key.isReadable()){
                        SocketChannel clientChannel = (SocketChannel) key.channel();
                        MyRequest myRequest = new MyRequest(clientChannel);
                        MyResponse myResponse = new MyResponse(clientChannel);

                        //请求分发
                        dispatch(myRequest,myResponse);
                    }
                }
                selector.selectedKeys().clear();
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
        finally {
            if(serverSocketChannel!=null){
                try{
                    serverSocketChannel.close();
                }
                catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }

    private void initServletMapping() {
        for(ServletMapping servletMapping:ServletMappingConfig.servletMappingList){
            urlServletMap.put(servletMapping.getUrl(),servletMapping.getClazz());
        }
    }

    private void dispatch(MyRequest myRequest, MyResponse myResponse) {
        String clazz = urlServletMap.get(myRequest.getUrl());
        try{
            if(clazz!=null){
                Class<MyServlet> myServletClass = (Class<MyServlet>) Class.forName(clazz);
                MyServlet myServlet = myServletClass.newInstance();
                myServlet.service(myRequest,myResponse);
            }
            else{
                defaultServlet.service(myRequest,myResponse);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new MyTomcat(8080).start();
    }
}
