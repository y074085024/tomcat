package com.zerone;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yxl
 * @since 2018/12/21
 */
public class ServletMappingConfig {
    public static List<ServletMapping> servletMappingList = new ArrayList<>();
    static{
        servletMappingList.add(new ServletMapping("findGirl","/girl","com.zerone.FindGirlServlet"));
        servletMappingList.add(new ServletMapping("helloWorld","/world","com.zerone.HelloWorldServlet"));
    }
}
