package com.zerone;

import java.io.IOException;

/**
 * @author yxl
 * @since 2018/12/21
 */
public class DefaultServlet extends MyServlet {
    private static MyServlet instance = new DefaultServlet();
    private DefaultServlet(){}
    public static MyServlet getInstance(){
        return instance;
    }
    @Override
    public void doGet(MyRequest myRequest, MyResponse myResponse) {
        try {
            myResponse.write("get error");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doPost(MyRequest myRequest, MyResponse myResponse) {
        try {
            myResponse.write("post error");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
