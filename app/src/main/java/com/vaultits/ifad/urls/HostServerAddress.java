package com.vaultits.ifad.urls;

public class HostServerAddress {

    //HOTSPOT IIS
    public static String BASE_URL() {
        return "http://192.168.43.185/IfadAPI/api/";
    }

    //office ip {network}
    public static String BASE_NET_URL(){
        return "http://192.168.1.110/IfadAPI/api/";
    }

    public static String BASE_URL_2() {
        return "http://10.0.2.2/IfadAPI/api/";
    }

    //ONLINE SERVER
    public static String BASE_SERVER_URL() {
        return "http://192.168.23.82:57160/api/";
    }

}
