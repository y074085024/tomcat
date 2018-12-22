package com.zerone;

import com.zerone.servlet.MyRequest;
import com.zerone.servlet.MyResponse;
import com.zerone.servlet.MyServlet;

import java.io.IOException;

/**
 * @author yxl
 * @since 2018/12/21
 */
public class HelloWorldServlet extends MyServlet {
    @Override
    public void doGet(MyRequest myRequest, MyResponse myResponse) {
        try {
            myResponse.write("get world...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doPost(MyRequest myRequest, MyResponse myResponse) {
        try {
            myResponse.write("post world...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
