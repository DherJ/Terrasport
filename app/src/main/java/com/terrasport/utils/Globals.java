package com.terrasport.utils;

/**
 * Created by jdher on 22/01/2017.
 */

public class Globals {

    private static Globals instance;

    private String baseUrl;

    private Globals(){}

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
    public String getBaseUrl() {
        return this.baseUrl;
    }

    public static synchronized Globals getInstance() {
        if(instance == null){
            instance = new Globals();
        }
        instance.baseUrl = "http://192.168.1.24:8080/";
        // instance.baseUrl = "http://172.19.139.179:8080/";
        return instance;
    }
}
