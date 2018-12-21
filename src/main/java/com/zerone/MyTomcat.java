package com.zerone;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yxl
 * @since 2018/12/21
 */
public class MyTomcat {
    private int port;
    private Map<String,String> urlServletMap = new HashMap<>();

    public MyTomcat(int port) {
        this.port = port;
    }

    public void start(){

        initServletMapping();

        ServerSocket serverSocket = null;
        try{
            serverSocket = new ServerSocket(port);
            System.out.println("MyTocat is start...");
            while (true){
                Socket socket = serverSocket.accept();
                InputStream inputStream = socket.getInputStream();
                OutputStream outputStream = socket.getOutputStream();

                MyRequest myRequest = new MyRequest(inputStream);
                MyResponse myResponse = new MyResponse(outputStream);

                //请求分发
                dispatch(myRequest,myResponse);

                socket.close();
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
        finally {
            if(serverSocket!=null){
                try{
                    serverSocket.close();
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
                DefaultServlet.getInstance().service(myRequest,myResponse);
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
